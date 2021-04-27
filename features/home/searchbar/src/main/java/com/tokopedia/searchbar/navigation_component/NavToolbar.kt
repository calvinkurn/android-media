package com.tokopedia.searchbar.navigation_component

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.BackType.BACK_TYPE_BACK
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.BackType.BACK_TYPE_CLOSE
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.BackType.BACK_TYPE_NONE
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_CUSTOM
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.Fill.TOOLBAR_FILLED
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.Fill.TOOLBAR_TRANSPARENT
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.Theme.TOOLBAR_DARK_TYPE
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
import com.tokopedia.searchbar.navigation_component.analytics.NavToolbarTracking
import com.tokopedia.searchbar.navigation_component.di.DaggerNavigationComponent
import com.tokopedia.searchbar.navigation_component.di.module.NavigationModule
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.TopNavComponentListener
import com.tokopedia.searchbar.navigation_component.util.StatusBarUtil
import com.tokopedia.searchbar.navigation_component.util.getActivityFromContext
import com.tokopedia.searchbar.navigation_component.viewModel.NavigationViewModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.nav_main_toolbar.view.*
import kotlinx.android.synthetic.main.nav_main_toolbar.view.layout_search
import kotlinx.android.synthetic.main.nav_main_toolbar.view.navToolbar
import java.lang.ref.WeakReference
import javax.inject.Inject

class NavToolbar: Toolbar, LifecycleObserver, TopNavComponentListener {
    companion object {
        object Theme {
            const val TOOLBAR_DARK_TYPE = 0
            const val TOOLBAR_LIGHT_TYPE = 1
        }

        object Fill {
            const val TOOLBAR_TRANSPARENT = 0
            const val TOOLBAR_FILLED = 1
        }

        object BackType {
            const val BACK_TYPE_NONE = 0
            const val BACK_TYPE_CLOSE = 1
            const val BACK_TYPE_BACK = 2
        }

        object ContentType {
            const val TOOLBAR_TYPE_SEARCH = 0
            const val TOOLBAR_TYPE_TITLE = 1
            const val TOOLBAR_TYPE_CUSTOM = 2
        }

        object StatusBar {
            const val STATUS_BAR_LIGHT = 0
            const val STATUS_BAR_DARK = 1
        }
    }

    //public variable
    var toolbarThemeType: Int = 0
    private set

    //attribution variable
    private val DEFAULT_PAGE_NAME = "Unknown page"
    private var backType = BACK_TYPE_NONE
    private var initialTheme = TOOLBAR_DARK_TYPE
    private var toolbarFillColor = getLightIconColor()
    private var toolbarAlwaysShowShadow = false
    private var backDrawable: Drawable? = null
    private var toolbarContentType = TOOLBAR_TYPE_TITLE
    private var toolbarTitle = ""
    private var toolbarDefaultHint = ""
    private var toolbarCustomReference: Int? = null
    private var toolbarCustomViewContent: View? = null
    private var toolbarPageName: String = DEFAULT_PAGE_NAME
    private var toolbarInitialFillColor: Int = TOOLBAR_FILLED
    private var invertSearchBarColor: Boolean = false
    private var lifecycleOwner: LifecycleOwner? = null
    private var useCentralizedIconNotification = mapOf<Int, Boolean>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: NavigationViewModel? by lazy {
        //TODO : REMOTE CONFIG HERE
        context?.let {
            val component = DaggerNavigationComponent.builder()
                    .navigationModule(NavigationModule(it.applicationContext))
                    .build()
            component.inject(this)

            when (it) {
                is AppCompatActivity -> {
                    val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
                    viewModelProvider[NavigationViewModel::class.java]
                }
                is ContextThemeWrapper -> {
                    val activity = it.getActivityFromContext()
                    activity?.let {
                        if (activity is AppCompatActivity) {
                            val viewModelProvider = ViewModelProviders.of(activity, viewModelFactory)
                            viewModelProvider[NavigationViewModel::class.java]
                        } else {
                            null
                        }
                    }
                }
                else -> {
                    null
                }
            }
        }
    }

    //helper variable
    var shadowApplied: Boolean = false
    private var userSessionInterface: UserSessionInterface? = null

    //controller variable
    internal var statusBarUtil: StatusBarUtil? = null
    private lateinit var navSearchBarController: NavSearchbarController
    private var navIconAdapter: NavToolbarIconAdapter? = null

    constructor(context: Context) : super(context) { init(context, null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(context, attrs) }

    private fun init(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.nav_main_toolbar, this)

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.NavToolbar, 0, 0)
            try {
                backType = ta.getInt(R.styleable.NavToolbar_toolbarBackButton, BACK_TYPE_NONE)
                initialTheme = ta.getInt(R.styleable.NavToolbar_toolbarInitialTheme, TOOLBAR_DARK_TYPE)
                toolbarAlwaysShowShadow = ta.getBoolean(R.styleable.NavToolbar_toolbarAlwaysShowShadow, false)
                toolbarContentType = ta.getInt(R.styleable.NavToolbar_toolbarContentType, TOOLBAR_TYPE_TITLE)
                toolbarTitle = ta.getString(R.styleable.NavToolbar_toolbarTitle)?:""
                toolbarDefaultHint = ta.getString(R.styleable.NavToolbar_toolbarDefaultHint)?:""
                toolbarPageName = ta.getString(R.styleable.NavToolbar_toolbarPageName)?:DEFAULT_PAGE_NAME
                toolbarInitialFillColor = ta.getInt(R.styleable.NavToolbar_toolbarInitialFillColor, TOOLBAR_FILLED)
                invertSearchBarColor = ta.getBoolean(R.styleable.NavToolbar_toolbarInvertSearchBarColor, false)
                toolbarCustomReference = ta.getResourceIdOrThrow(R.styleable.NavToolbar_toolbarCustomContent)
            } catch (e: IllegalArgumentException) {

            } finally {
                ta.recycle()
            }
        }
        userSessionInterface = UserSession(context)
        configureInvertedSearchBar()
        configureInitialFillBasedOnAttribute()
        configureThemeBasedOnAttribute()
        configureBackButtonBasedOnAttribute()
        configureShadowBasedOnAttribute()
        configureToolbarContentTypeBasedOnAttribute()
    }

    /**
     * Set icon config. For available icon
     * @see
     * IconList.kt
     */
    fun setIcon(iconBuilder: IconBuilder) {
        val iconConfig = iconBuilder.build()
        this.useCentralizedIconNotification = iconConfig.useCentralizedIconNotification
        navIconAdapter = NavToolbarIconAdapter(iconConfig, this)
        navIconAdapter?.setHasStableIds(true)
        val navIconRecyclerView = rv_icon_list
        navIconRecyclerView.adapter = navIconAdapter
        navIconRecyclerView.itemAnimator = null
        navIconRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        toolbarThemeCondition(
                lightCondition = { navIconAdapter?.setThemeState(NavToolbarIconAdapter.STATE_THEME_LIGHT) },
                darkCondition = { navIconAdapter?.setThemeState(NavToolbarIconAdapter.STATE_THEME_DARK) }
        )
    }

    /**
     * Call this function to let the NavToolbar manage your status bar transparency
     */
    fun setupToolbarWithStatusBar(activity: Activity, statusBarTheme: Int? = null, applyPadding: Boolean = true) {
        statusBarUtil = StatusBarUtil(WeakReference(activity))

        statusBarTheme?.let {
            when (it) {
                StatusBar.STATUS_BAR_LIGHT -> statusBarUtil?.requestStatusBarLight()
                StatusBar.STATUS_BAR_DARK -> statusBarUtil?.requestStatusBarDark()
                else -> statusBarUtil?.requestStatusBarDark()
            }
        }

        if (applyPadding) applyStatusBarPadding()
    }

    /**
     * Hide shadow and adjust padding
     */
    fun hideShadow(lineShadow: Boolean = true) {
        if(shadowApplied){
            shadowApplied = false
            if (lineShadow) {
                dividerUnify?.visibility = View.INVISIBLE
                navToolbar?.background = ColorDrawable(getLightIconColor())
                setBackgroundAlpha(0f)
                navToolbar?.updatePadding(bottom = 0)
            } else {
                val pB = resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8)
                navToolbar?.background = ColorDrawable(getLightIconColor())
                navToolbar?.updatePadding(bottom = pB)
            }
        }
    }

    /**
     * Show shadow and adjust padding
     */
    fun showShadow(lineShadow: Boolean = true) {
        if(!shadowApplied && toolbarAlwaysShowShadow){
            shadowApplied = true

            if (lineShadow) {
                setBackgroundAlpha(225f)
                dividerUnify?.visibility = View.VISIBLE
                navToolbar?.updatePadding(bottom = 0)
            } else {
                val pB = resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8)
                navToolbar?.background = ContextCompat.getDrawable(context, R.drawable.searchbar_bg_shadow_bottom)
                navToolbar?.updatePadding(bottom = pB)
            }
        }
    }

    /**
     * Switch to dark toolbar manually. To manage toolbar automatically
     * @see
     * NavRecyclerViewScrollListener
     */
    fun switchToDarkToolbar() {
        if (toolbarThemeType != TOOLBAR_DARK_TYPE) {
            navIconAdapter?.setThemeState(NavToolbarIconAdapter.STATE_THEME_DARK)
            toolbarThemeType = TOOLBAR_DARK_TYPE
            setBackButtonColorBasedOnTheme()
            setTitleTextColorBasedOnTheme()
        }
    }

    /**
     * Switch to light toolbar manually. To manage toolbar automatically
     * @see
     * NavRecyclerViewScrollListener
     */
    fun switchToLightToolbar() {
        if (toolbarThemeType != TOOLBAR_LIGHT_TYPE) {
            navIconAdapter?.setThemeState(NavToolbarIconAdapter.STATE_THEME_LIGHT)
            toolbarThemeType = TOOLBAR_LIGHT_TYPE
            toolbar_title.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            setBackButtonColorBasedOnTheme()
            setTitleTextColorBasedOnTheme()
        }
    }

    /**
     * Show and setup searchbar
     * @durationAutoTransition is delay for each hint in ms
     */
    fun setupSearchbar(hints: List<HintData>,
                       applink: String = "",
                       searchbarClickCallback: ((hint: String) -> Unit)? = null,
                       searchbarImpressionCallback: ((hint: String) -> Unit)? = null,
                       durationAutoTransition: Long = 0,
                       shouldShowTransition: Boolean = true,
                       disableDefaultGtmTracker: Boolean = false
    ) {
        showSearchbar()

        var applinkForController = applink
        if (applink.isEmpty()) applinkForController = ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE
        navSearchBarController = NavSearchbarController(
                this,
                applinkForController,
                searchbarClickCallback = searchbarClickCallback,
                searchbarImpressionCallback = searchbarImpressionCallback,
                topNavComponentListener = this,
                disableDefaultGtmTracker = disableDefaultGtmTracker
        )
        navSearchBarController.setHint(hints, shouldShowTransition, durationAutoTransition)
    }

    fun setBadgeCounter(iconId: Int, counter: Int) {
        // only allow direct set badge counter when viewmodel is not initialized
        // means remote config for centralized notification is off
        if (viewModel == null) {
            navIconAdapter?.setIconCounter(iconId, counter)
        }
    }

    fun triggerLottieAnimation(lottieIconId: Int) {
        navIconAdapter?.triggerLottieAnimation(lottieIconId)
    }

    fun triggerAnimatedVectorDrawableAnimation(animatedIconId: Int) {
        navIconAdapter?.triggerAnimatedVectorDrawableAnimation(animatedIconId)
    }

    fun setOnBackButtonClickListener(disableDefaultGtmTracker: Boolean = false, backButtonClickListener: () -> Unit) {
        nav_icon_back.setOnClickListener {
            if (!disableDefaultGtmTracker) {
                NavToolbarTracking.clickNavToolbarComponent(
                        pageName = toolbarPageName,
                        componentName = IconList.NAME_BACK_BUTTON,
                        userId = getUserId()
                )
            }
            backButtonClickListener.invoke()
        }
    }

    fun setToolbarContentType(toolbarContentType: Int) {
        when(toolbarContentType) {
            TOOLBAR_TYPE_TITLE -> {
                showTitle()
            }
            TOOLBAR_TYPE_SEARCH -> {
                showSearchbar()
            }
            TOOLBAR_TYPE_CUSTOM -> {
                setupCustomView()
            }
        }
    }

    fun getCustomViewContentView(): View? {
        if (toolbarCustomReference == null) return null
        return layout_custom_view
    }

    fun setBackButtonType(newBackType: Int? = null) {
        newBackType?.let { backType = newBackType }

        if (backType != BACK_TYPE_NONE) {
            setBackButtonColorBasedOnTheme()
            nav_icon_back.visibility = VISIBLE
            nav_icon_back.tag = backType
            if (context is Activity) {
                nav_icon_back.setOnClickListener {
                    NavToolbarTracking.clickNavToolbarComponent(
                            pageName = toolbarPageName,
                            componentName = IconList.NAME_BACK_BUTTON,
                            userId = getUserId()
                    )
                    (context as? Activity)?.onBackPressed()
                }
            }
        } else {
            nav_icon_back.visibility = GONE
        }
    }

    fun setToolbarTitle(title: String) {
        toolbarTitle = title
        showTitle()
    }

    fun setCustomViewContentRef(viewRef: Int) {
        toolbarCustomReference = viewRef
        toolbarCustomViewContent = null
    }

    fun setCustomViewContentView(view: View) {
        toolbarCustomViewContent = view
        toolbarCustomReference = null
    }

    fun setShowShadowEnabled(enabled: Boolean) {
        toolbarAlwaysShowShadow = enabled
    }

    fun setToolbarPageName(pageName: String) {
        toolbarPageName = pageName
    }

    //this needed to enable coachmark on homepage
    fun getGlobalNavIconView(): View? {
        val globalNavPosition = navIconAdapter?.getGlobalNavIconPosition()
        globalNavPosition?.let {
            val viewholder = rv_icon_list.findViewHolderForAdapterPosition(it)
            return viewholder?.itemView
        }
        return null
    }

    //this needed to enable coachmark on homepage
    fun getInboxIconView(): View? {
        val globalNavPosition = navIconAdapter?.getInboxIconPosition()
        globalNavPosition?.let {
            val viewholder = rv_icon_list.findViewHolderForAdapterPosition(it)
            return viewholder?.itemView
        }
        return null
    }

    internal fun setBackgroundAlpha(alpha: Float) {
        navToolbar?.let {
            val drawable = it.background
            drawable.alpha = alpha.toInt()
            it.background = drawable
        }
    }

    fun setCentralizedBadgeCounter(iconId: Int, counter: Int) {
        useCentralizedIconNotification[iconId]?.let {
            if (it) {
                navIconAdapter?.setIconCounter(iconId, counter)
            }
        }
    }

    fun updateNotification() {
        viewModel?.getNotification()
    }

    fun applyNotification() {
        viewModel?.applyNotification()
    }

    private fun applyStatusBarPadding() {
        var pT = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pT = ViewHelper.getStatusBarHeight(context)
        }
        navToolbar?.updatePadding(top = pT)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onStartListener(owner: LifecycleOwner){
        this.lifecycleOwner = owner
        observeLiveData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun startSearchBarHintAnimation() {
        if (::navSearchBarController.isInitialized) {
            navSearchBarController.startHintAnimation()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun stopSearchBarHintAnimation() {
        if (::navSearchBarController.isInitialized) {
            navSearchBarController.stopHintAnimation()
        }
    }

    private fun observeLiveData() {
        lifecycleOwner?.let {owner ->
            viewModel?.navNotificationLiveData?.observe(owner, Observer {
                it?.let {
                    setCentralizedBadgeCounter(IconList.ID_MESSAGE, it.totalInbox)
                    setCentralizedBadgeCounter(IconList.ID_CART, it.totalCart)
                    setCentralizedBadgeCounter(IconList.ID_NOTIFICATION, it.totalNotif)
                    setCentralizedBadgeCounter(IconList.ID_INBOX, it.totalNewInbox)
                    setCentralizedBadgeCounter(IconList.ID_NAV_GLOBAL, it.totalGlobalNavNotif)
                }
            })
        }
    }

    private fun Toolbar.updatePadding(left: Int = paddingLeft, top: Int = ViewHelper.getStatusBarHeight(context), right: Int = paddingRight, bottom: Int = paddingBottom) {
        setPadding(left, top, right, bottom)
    }

    private fun configureToolbarContentTypeBasedOnAttribute() {
        when(toolbarContentType) {
            TOOLBAR_TYPE_SEARCH -> {
                showSearchbar(listOf(HintData(toolbarDefaultHint, toolbarDefaultHint)))
            }
            TOOLBAR_TYPE_TITLE -> {
                showTitle()
            }
            TOOLBAR_TYPE_CUSTOM -> {
                setupCustomView()
            }
        }
    }

    private fun setupCustomView() {
        showCustomView()
        toolbarCustomReference?.let {
            val parentLayout = layout_custom_view
            val childView = LayoutInflater.from(context).inflate(it, parentLayout, false)
            childView.id = generateViewId()
            parentLayout.addView(childView)
        }

        toolbarCustomViewContent?.let {
            val parentLayout = layout_custom_view
            it.id = generateViewId()
            parentLayout.addView(it)
        }
    }

    private fun configureShadowBasedOnAttribute() {
        if (toolbarAlwaysShowShadow && toolbarInitialFillColor != TOOLBAR_TRANSPARENT) {
            showShadow()
        } else {
            hideShadow()
        }
    }

    private fun configureInvertedSearchBar() {
        if (invertSearchBarColor) {
            layout_search.background = ContextCompat.getDrawable(context, R.drawable.nav_toolbar_searchbar_bg_corner_inverted)
        }
    }

    private fun configureInitialFillBasedOnAttribute() {
        if (toolbarInitialFillColor == TOOLBAR_TRANSPARENT) {
            toolbarFillColor = getLightIconColor()
            dividerUnify?.visibility = View.INVISIBLE
            navToolbar?.background = ColorDrawable(toolbarFillColor)
            setBackgroundAlpha(0f)
        } else {
            navToolbar?.background = ColorDrawable(toolbarFillColor)
        }
    }

    private fun configureThemeBasedOnAttribute() {
        if (initialTheme == TOOLBAR_DARK_TYPE) {
            switchToDarkToolbar()
        } else {
            switchToLightToolbar()
        }
    }

    private fun configureBackButtonBasedOnAttribute() {
        setBackButtonType()
    }

    private fun setBackButtonColor(color: Int) {
        backDrawable?.let {
            val unwrappedDrawable: Drawable = it
            val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedDrawable)
            DrawableCompat.setTint(wrappedDrawable, color)
            nav_icon_back.setImageDrawable(it)
        }
    }

    private fun setBackButtonColorBasedOnTheme() {
        when (backType) {
            BACK_TYPE_CLOSE -> {
                toolbarThemeCondition(
                        lightCondition = { nav_icon_back.setImage(newIconId = IconUnify.CLOSE, newLightEnable = getDarkIconColor()) },
                        darkCondition = { nav_icon_back.setImage(newIconId = IconUnify.CLOSE, newLightEnable = getLightIconColor()) }
                )
            }
            BACK_TYPE_BACK -> {
                toolbarThemeCondition(
                        lightCondition = { nav_icon_back.setImage(newIconId = IconUnify.ARROW_BACK, newLightEnable = getDarkIconColor()) },
                        darkCondition = { nav_icon_back.setImage(newIconId = IconUnify.ARROW_BACK, newLightEnable = getLightIconColor()) }
                )
            }
        }
    }

    private fun showTitle() {
        hideToolbarContent(hideTitle = false)
        showToolbarContent(showTitle = true)
        toolbar_title.text = toolbarTitle

        setTitleTextColorBasedOnTheme()
    }

    private fun showSearchbar(hints: List<HintData>? = null) {
        hideToolbarContent(hideSearchbar = false)
        showToolbarContent(showSearchbar = true)

        hints?.let { setupSearchbar(hints = hints) }
    }

    private fun showCustomView() {
        hideToolbarContent(hideCustomContent = false)
        showToolbarContent(showCustomContent = true)
    }

    private fun getDarkIconColor() = ContextCompat.getColor(context, R.color.searchbar_dms_state_light_icon)

    private fun getLightIconColor() = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)

    private fun setTitleTextColorBasedOnTheme() {
        toolbarThemeCondition(
                lightCondition = { toolbar_title.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)) },
                darkCondition = { toolbar_title.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)) }
        )
    }

    private fun hideToolbarContent(hideTitle: Boolean = true, hideSearchbar: Boolean = true, hideCustomContent: Boolean = true) {
        if (hideTitle) toolbar_title.visibility = View.GONE
        if (hideSearchbar) layout_search.visibility = View.GONE
        if (hideCustomContent) layout_custom_view.visibility = View.GONE
    }

    private fun showToolbarContent(showTitle: Boolean = false, showSearchbar: Boolean = false, showCustomContent: Boolean = false) {
        if (showTitle) toolbar_title.visibility = View.VISIBLE
        if (showSearchbar) layout_search.visibility = View.VISIBLE
        if (showCustomContent) layout_custom_view.visibility = View.VISIBLE
    }

    private fun toolbarThemeCondition(lightCondition: () -> Unit = {}, darkCondition: () -> Unit = {}) {
        if (toolbarThemeType == TOOLBAR_LIGHT_TYPE) lightCondition.invoke()
        if (toolbarThemeType == TOOLBAR_DARK_TYPE) darkCondition.invoke()
    }

    override fun onVisibilityAggregated(isVisible: Boolean) {
        super.onVisibilityAggregated(isVisible)
        if (isVisible) {
            viewModel?.getNotification()
        }
    }

    override fun getUserId(): String = userSessionInterface?.userId?:""

    override fun isLoggedIn(): Boolean = userSessionInterface?.isLoggedIn?:false

    override fun getPageName(): String = toolbarPageName
}