package com.tokopedia.searchbar.navigation_component

import android.app.Activity
import android.content.Context
import android.graphics.ColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.discovery.common.microinteraction.navtoolbar.NavToolbarMicroInteraction
import com.tokopedia.iconnotification.IconNotification
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.BackType.BACK_TYPE_BACK
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.BackType.BACK_TYPE_BACK_WITHOUT_COLOR
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
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.di.DaggerNavigationComponent
import com.tokopedia.searchbar.navigation_component.di.module.NavigationModule
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.TopNavComponentListener
import com.tokopedia.searchbar.navigation_component.util.StatusBarUtil
import com.tokopedia.searchbar.navigation_component.util.getActivityFromContext
import com.tokopedia.searchbar.navigation_component.viewModel.NavigationViewModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
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
            const val BACK_TYPE_BACK_WITHOUT_COLOR = 3
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

        object SearchBarType {
            const val TYPE_CLICK = 0
            const val TYPE_EDITABLE = 1
        }
        private const val MAX_BACKGROUND_ALPHA = 225f
        // for set transparent searchbar on toolbar
        const val ALPHA_MAX = 255f
    }

    //public variable
    var toolbarThemeType: Int = 0
    private set

    //attribution variable
    private val DEFAULT_PAGE_NAME = "Unknown page"
    private var backType = BACK_TYPE_NONE
    private var initialTheme = TOOLBAR_DARK_TYPE
    private var toolbarFillColor = getLightBackgroundColor()
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
    private var searchbarType: Int? = null
    private var darkIconColor: Int? = getDarkIconColor()
    private var lightIconColor: Int? = getLightIconColor()

    private val navIconRecyclerView : RecyclerView by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(R.id.rv_icon_list)
    }
    private val divider: View by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(R.id.divider)
    }
    private val navToolbar : Toolbar by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(R.id.navToolbar)
    }
    private val tvToolbarTitle: Typography by lazy(LazyThreadSafetyMode.NONE){
        findViewById(R.id.toolbar_title)
    }
    private val navIconBack: IconUnify by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(R.id.nav_icon_back)
    }
    private val layoutSearch : ViewGroup by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(R.id.layout_search)
    }
    private val layoutCustomView: ViewGroup by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(R.id.layout_custom_view)
    }
    private val etSearch : EditText by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(R.id.et_search)
    }
    private val iconSearchMagnify : IconUnify by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(R.id.search_magnify_icon)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: NavigationViewModel? by lazy {
        context?.let {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            val enableNotif = remoteConfig.getBoolean(RemoteConfigKey.NAVIGATION_ENABLE_NOTIF, true)
            if (enableNotif) {
                initializeViewModel(it)
            } else {
                null
            }
        }
    }

    private fun initializeViewModel(it: Context): NavigationViewModel? {
        val component = DaggerNavigationComponent.builder()
                .navigationModule(NavigationModule(it.applicationContext))
                .build()
        component.inject(this)
        return when (it) {
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
        etSearch.typeface = Typography.getFontType(context, false, Typography.DISPLAY_2)
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
        viewModel?.setRegisteredIconList(iconConfig)
        this.useCentralizedIconNotification = iconConfig.useCentralizedIconNotification
        navIconAdapter = NavToolbarIconAdapter(iconConfig, this)
        navIconAdapter?.setHasStableIds(true)
        navIconRecyclerView.adapter = navIconAdapter
        navIconRecyclerView.itemAnimator = null
        navIconRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        toolbarThemeCondition(
                lightCondition = { navIconAdapter?.setThemeState(NavToolbarIconAdapter.STATE_THEME_LIGHT) },
                darkCondition = { navIconAdapter?.setThemeState(NavToolbarIconAdapter.STATE_THEME_DARK) }
        )

        viewModel?.navNotificationLiveData?.value?.let {
            updateCentralizedNotificationData(it)
        }
    }

    /**
     * Call this function to let the NavToolbar manage your status bar transparency
     */
    fun setupToolbarWithStatusBar(activity: Activity, statusBarTheme: Int? = null, applyPadding: Boolean = true, applyPaddingNegative: Boolean = false) {
        statusBarUtil = StatusBarUtil(WeakReference(activity))

        statusBarTheme?.let {
            when (it) {
                StatusBar.STATUS_BAR_LIGHT -> statusBarUtil?.requestStatusBarDark()
                StatusBar.STATUS_BAR_DARK -> statusBarUtil?.requestStatusBarLight()
                else -> statusBarUtil?.requestStatusBarDark()
            }
        }

        if (applyPadding) applyStatusBarPadding()
        if (applyPaddingNegative) resetPadding()
    }

    /**
     * Hide shadow and adjust padding
     */
    fun hideShadow(lineShadow: Boolean = true) {
        if(shadowApplied){
            shadowApplied = false
            if (lineShadow) {
                divider.visibility = View.INVISIBLE
                navToolbar.background = ColorDrawable(getLightBackgroundColor())
                setBackgroundAlpha(0f)
                navToolbar.updatePadding(bottom = 0)
            } else {
                val pB = resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8)
                navToolbar.background = ColorDrawable(getLightBackgroundColor())
                navToolbar.updatePadding(bottom = pB)
            }
        }
    }

    /**
     * Call this method first before another method
     */
    fun setIconCustomColor(darkColor: Int?, lightColor: Int?) {
        darkIconColor = darkColor
        lightIconColor = lightColor
    }

    /**
     * Show shadow and adjust padding
     */
    fun showShadow(lineShadow: Boolean = true) {
        if(!shadowApplied && toolbarAlwaysShowShadow){
            shadowApplied = true

            if (lineShadow) {
                setBackgroundAlpha(MAX_BACKGROUND_ALPHA)
                divider.visibility = View.VISIBLE
                navToolbar.updatePadding(bottom = 0)
            } else {
                val pB = resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8)
                navToolbar.background = ContextCompat.getDrawable(context, R.drawable.searchbar_bg_shadow_bottom)
                navToolbar.updatePadding(bottom = pB)
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
            tvToolbarTitle.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            setBackButtonColorBasedOnTheme()
            setTitleTextColorBasedOnTheme()
        }
    }

    fun setupMicroInteraction(navToolbarMicroInteraction: NavToolbarMicroInteraction?) {
        navToolbarMicroInteraction?.setNavToolbarComponents(layoutSearch)
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
                       disableDefaultGtmTracker: Boolean = false,
                       searchbarType: Int = SearchBarType.TYPE_CLICK,
                       navSearchbarInterface: ((text: CharSequence?,
                                                start: Int,
                                                count: Int,
                                                after: Int) -> Unit)? = null,
                       editorActionCallback: ((hint: String) -> Unit)? = null

    ) {
        var applinkForController = applink
        if (applink.isEmpty()) applinkForController = ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE
        navSearchBarController = NavSearchbarController(
                this,
                applinkForController,
                searchbarClickCallback = searchbarClickCallback,
                searchbarImpressionCallback = searchbarImpressionCallback,
                topNavComponentListener = this,
                disableDefaultGtmTracker = disableDefaultGtmTracker,
                navSearchbarInterface = navSearchbarInterface,
                editorActionCallback = editorActionCallback
        )
        this.searchbarType = searchbarType
        searchbarTypeValidation(
            searchbarType = searchbarType,
            ifClickSearchbarType = {
                navSearchBarController.setHint(hints, shouldShowTransition, durationAutoTransition)
            },
            ifEditableSearchbarType = {
                val hint = hints.getOrNull(0)
                navSearchBarController.setEditableSearchbar(hint?.placeholder?:"")
            }
        )
    }

    fun searchbarTypeValidation(searchbarType: Int,
                                ifClickSearchbarType: () -> Unit = {},
                                ifEditableSearchbarType: () -> Unit = {}) {
        if (searchbarType == SearchBarType.TYPE_CLICK) {
            ifClickSearchbarType.invoke()
        } else if (searchbarType == SearchBarType.TYPE_EDITABLE) {
            ifEditableSearchbarType.invoke()
        }
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
        navIconBack.setOnClickListener {
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
        return layoutCustomView
    }

    fun setBackButtonType(newBackType: Int? = null) {
        newBackType?.let { backType = newBackType }

        if (backType != BACK_TYPE_NONE) {
            setBackButtonColorBasedOnTheme()
            navIconBack.visibility = VISIBLE
            navIconBack.tag = backType
            if (context is Activity) {
                navIconBack.setOnClickListener {
                    NavToolbarTracking.clickNavToolbarComponent(
                            pageName = toolbarPageName,
                            componentName = IconList.NAME_BACK_BUTTON,
                            userId = getUserId()
                    )
                    (context as? Activity)?.onBackPressed()
                }
            }
        } else {
            navIconBack.visibility = GONE
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
            val viewholder = navIconRecyclerView.findViewHolderForAdapterPosition(it)
            return viewholder?.itemView
        }
        return null
    }

    //this needed to enable coachmark on homepage
    fun getInboxIconView(): View? {
        val globalNavPosition = navIconAdapter?.getInboxIconPosition()
        globalNavPosition?.let {
            val viewholder = navIconRecyclerView.findViewHolderForAdapterPosition(it)
            return viewholder?.itemView
        }
        return null
    }

    //this needed to enable coachmark on wishlist detail page
    fun getShareIconView(): View? {
        val shareIconPosition = navIconAdapter?.getShareIconPosition()
        shareIconPosition?.let {
            val viewholder = navIconRecyclerView.findViewHolderForAdapterPosition(it)
            return viewholder?.itemView
        }
        return null
    }

    private fun getNoteBookIconView(): IconNotification? {
        val noteBookPosition = navIconAdapter?.getNoteBookPosition()
        noteBookPosition?.let {
            val viewholder = navIconRecyclerView.findViewHolderForAdapterPosition(it) as? ImageIconHolder
            return viewholder?.iconImage
        }
        return null
    }

    internal fun setBackgroundAlpha(alpha: Float) {
        navToolbar.apply {
            val drawable = background
            drawable.alpha = alpha.toInt()
            background = drawable
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

    fun hideKeyboard() {
        navSearchBarController.etSearch?.clearFocus()
        val `in` = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(navSearchBarController.etSearch?.windowToken, 0)
    }

    fun setSearchbarText(text: String) {
        navSearchBarController.etSearch?.setText(text)
    }

    fun getCurrentSearchbarText(): String {
        return navSearchBarController.etSearch?.text?.toString() ?: ""
    }

    fun clearSearchbarText() {
        navSearchBarController.etSearch?.text?.clear()
        navSearchBarController.etSearch?.clearFocus()
    }

    private fun applyStatusBarPadding() {
        var pT = 0
        pT = ViewHelper.getStatusBarHeight(context)
        navToolbar.updatePadding(top = pT)
    }

    private fun resetPadding() {
        var pT = 0
        navToolbar.updatePadding(top = pT)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onStartListener(owner: LifecycleOwner) {
        this.lifecycleOwner = owner
        observeLiveData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun startSearchBarHintAnimation() {
        if (::navSearchBarController.isInitialized) {
            navSearchBarController.startHintAnimation()
        }
        viewModel?.getNotification()
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
                    updateCentralizedNotificationData(it)
                }
            })
        }
    }

    private fun updateCentralizedNotificationData(it: TopNavNotificationModel) {
        setCentralizedBadgeCounter(IconList.ID_MESSAGE, it.totalInbox)
        setCentralizedBadgeCounter(IconList.ID_CART, it.totalCart)
        setCentralizedBadgeCounter(IconList.ID_NOTIFICATION, it.totalNotif)
        setCentralizedBadgeCounter(IconList.ID_INBOX, it.totalNewInbox)
        setCentralizedBadgeCounter(IconList.ID_NAV_GLOBAL, it.totalGlobalNavNotif)
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
            val parentLayout = layoutCustomView
            val childView = LayoutInflater.from(context).inflate(it, parentLayout, false)
            childView.id = generateViewId()
            parentLayout.addView(childView)
        }

        toolbarCustomViewContent?.let {
            val parentLayout = layoutCustomView
            it.id = generateViewId()
            parentLayout.removeAllViews()
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
            layoutSearch.background = ContextCompat.getDrawable(context, R.drawable.nav_toolbar_searchbar_bg_corner_inverted)
        }
    }

    private fun configureInitialFillBasedOnAttribute() {
        if (toolbarInitialFillColor == TOOLBAR_TRANSPARENT) {
            toolbarFillColor = getLightBackgroundColor()
            divider.visibility = View.INVISIBLE
            navToolbar.background = ColorDrawable(toolbarFillColor)
            setBackgroundAlpha(0f)
        } else {
            navToolbar.background = ColorDrawable(toolbarFillColor)
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
            navIconBack.setImageDrawable(it)
        }
    }

    fun setCustomBackButton(icon: Int = IconUnify.ARROW_BACK, color: Int) {
        navIconBack.setImage(newIconId = icon,
                newLightEnable = color,
                newLightDisable = color,
                newDarkDisable = color,
                newDarkEnable = color)
    }

    private fun setBackButtonColorBasedOnTheme() {
        when (backType) {
            BACK_TYPE_CLOSE -> {
                toolbarThemeCondition(
                    lightCondition = { navIconBack.setImage(newIconId = IconUnify.CLOSE, newLightEnable = darkIconColor) },
                    darkCondition = { navIconBack.setImage(newIconId = IconUnify.CLOSE, newLightEnable = lightIconColor) }
                )
            }
            BACK_TYPE_BACK -> {
                toolbarThemeCondition(
                    lightCondition = { navIconBack.setImage(newIconId = IconUnify.ARROW_BACK, newLightEnable = darkIconColor) },
                    darkCondition = { navIconBack.setImage(newIconId = IconUnify.ARROW_BACK, newLightEnable = lightIconColor) }
                )
            }
            BACK_TYPE_BACK_WITHOUT_COLOR -> {
                val arrowBackIcon = getIconUnifyDrawable(context, IconUnify.ARROW_BACK)
                navIconBack.setImageDrawable(arrowBackIcon)
            }
        }
    }

    private fun showTitle() {
        hideToolbarContent(hideTitle = false)
        showToolbarContent(showTitle = true)
        tvToolbarTitle.text = toolbarTitle

        setTitleTextColorBasedOnTheme()
    }

    private fun showSearchbar(hints: List<HintData>? = null) {
        hideToolbarContent(hideSearchbar = false)
        showToolbarContent(showSearchbar = true)
    }

    private fun showCustomView() {
        hideToolbarContent(hideCustomContent = false)
        showToolbarContent(showCustomContent = true)
    }

    private fun getDarkIconColor(): Int {
        val unifyColor = if (context.isDarkMode()) {
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        } else {
            R.color.searchbar_dms_state_light_icon
        }
        return ContextCompat.getColor(context, unifyColor)
    }

    private fun getLightIconColor() = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900)

    private fun getLightBackgroundColor() = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)

    private fun setTitleTextColorBasedOnTheme() {
        val lightColorCondition = darkIconColor ?: ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        val darkCondition = lightIconColor ?: ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)

        toolbarThemeCondition(
            lightCondition = { tvToolbarTitle.setTextColor(lightColorCondition) },
            darkCondition = { tvToolbarTitle.setTextColor(darkCondition) }
        )
    }

    private fun hideToolbarContent(hideTitle: Boolean = true, hideSearchbar: Boolean = true, hideCustomContent: Boolean = true) {
        if (hideTitle) tvToolbarTitle.visibility = View.GONE
        if (hideSearchbar) layoutSearch.visibility = View.GONE
        if (hideCustomContent) layoutCustomView.visibility = View.GONE
    }

    private fun showToolbarContent(showTitle: Boolean = false, showSearchbar: Boolean = false, showCustomContent: Boolean = false) {
        if (showTitle) tvToolbarTitle.visibility = View.VISIBLE
        if (showSearchbar) layoutSearch.visibility = View.VISIBLE
        if (showCustomContent) layoutCustomView.visibility = View.VISIBLE
    }

    private fun toolbarThemeCondition(lightCondition: () -> Unit = {}, darkCondition: () -> Unit = {}) {
        if (toolbarThemeType == TOOLBAR_LIGHT_TYPE) lightCondition.invoke()
        if (toolbarThemeType == TOOLBAR_DARK_TYPE) darkCondition.invoke()
    }

    override fun getUserId(): String = userSessionInterface?.userId?:""

    override fun isLoggedIn(): Boolean = userSessionInterface?.isLoggedIn?:false

    override fun getPageName(): String = toolbarPageName

    fun setSearchBarAlpha(alpha: Float) = runCatching {
        etSearch.alpha = alpha / ALPHA_MAX
        layoutSearch.alpha = alpha / ALPHA_MAX
        iconSearchMagnify.alpha = alpha / ALPHA_MAX
        etSearch.isEnabled = etSearch.alpha > Float.ZERO
    }

    private fun getColor(resourceId: Int): Int {
        return ContextCompat.getColor(
            context,
            resourceId
        )
    }

    private fun getDrawable(resourceId: Int): Drawable? {
        return ContextCompat.getDrawable(
            context,
            resourceId
        )
    }

    private fun blendColor(color: Int): ColorFilter? {
        return BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            color,
            BlendModeCompat.SRC_ATOP
        )
    }

    @Suppress("DEPRECATION")
    private fun switchStatusBarIconColor(needDarkColor: Boolean, activity: Activity?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window?.decorView?.systemUiVisibility = if (!needDarkColor)  View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    /**
     * Switch to static color of toolbar.
     * In NavRecyclerViewScrollListener, put this function into onSwitchToDarkToolbar function.
     */
    fun switchToStaticBackground(activity: Activity?) {
        if (context.isDarkMode()) {
            val searchBackgroundColor = getDrawable(R.drawable.nav_toolbar_searchbar_bg_corner_static)
            val blendIconColor = blendColor(getColor(R.color.searchbar_dms_icon_dark_color))
            val blendIconSearchColor = blendColor(getColor(R.color.searchbar_dms_search_icon_dark_color))
            val hintTextColor = getColor(R.color.searchbar_dms_text_color)

            layoutSearch.background = searchBackgroundColor
            navIconBack.colorFilter = blendIconColor
            iconSearchMagnify.colorFilter = blendIconSearchColor
            etSearch.setHintTextColor(hintTextColor)
            getNoteBookIconView()?.imageDrawable?.colorFilter = blendIconColor

            switchStatusBarIconColor(
                needDarkColor = true,
                activity = activity
            )
        }
    }

    /**
     * Switch to normal color of toolbar.
     * Use this function if switchToStaticBackground function is used as well.
     * In NavRecyclerViewScrollListener, put this function into onSwitchToLightToolbar function.
     */
    fun switchToNormalBackground(activity: Activity?) {
        if (context.isDarkMode()) {
            val searchBackgroundColor = getDrawable(R.drawable.nav_toolbar_searchbar_bg_corner_inverted)
            val blendIconColor = blendColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN900))
            val blendIconSearchColor = blendColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN500))
            val hintTextColor = getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600)

            layoutSearch.background = searchBackgroundColor
            navIconBack.colorFilter = blendIconColor
            iconSearchMagnify.colorFilter = blendIconSearchColor
            etSearch.setHintTextColor(hintTextColor)
            getNoteBookIconView()?.imageDrawable?.colorFilter = blendIconColor

            switchStatusBarIconColor(
                needDarkColor = false,
                activity = activity
            )
        }
    }
}
