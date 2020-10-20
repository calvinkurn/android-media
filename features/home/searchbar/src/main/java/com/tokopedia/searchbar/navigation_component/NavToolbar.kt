package com.tokopedia.searchbar.navigation_component

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.util.StatusBarUtil
import kotlinx.android.synthetic.main.nav_main_toolbar.view.*
import java.lang.ref.WeakReference

class NavToolbar: Toolbar, LifecycleObserver {
    companion object {
        const val TOOLBAR_DARK_TYPE = 0
        const val TOOLBAR_LIGHT_TYPE = 1

        private const val BACK_TYPE_NONE = 0
        private const val BACK_TYPE_CLOSE = 1
        private const val BACK_TYPE_BACK = 2

        private const val TOOLBAR_TYPE_SEARCH = 0
        private const val TOOLBAR_TYPE_TITLE = 1
    }

    //public variable
    var toolbarThemeType: Int = 0
    private set

    //attribution variable
    private var backType = BACK_TYPE_NONE
    private var initialTheme = TOOLBAR_DARK_TYPE
    private var toolbarFillColor = getLightIconColor()
    private var toolbarAlwaysShowShadow = false
    private var backDrawable: Drawable? = null
    private var toolbarContentType = TOOLBAR_TYPE_TITLE
    private var toolbarTitle = ""
    private var toolbarDefaultHint = ""

    //helper variable
    private var shadowApplied: Boolean = false
    private var isSearchbarShown = false

    //controller variable
    internal var statusBarUtil: StatusBarUtil? = null
    private lateinit var navSearchBarController: NavSearchbarController
    private lateinit var navIconAdapter: NavToolbarIconAdapter

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
            } finally {
                ta.recycle()
            }
        }
        toolbar?.background = ColorDrawable(toolbarFillColor)
        configureThemeBasedOnAttribute()
        configureBackButtonBasedOnAttribute(context)
        configureShadowBasedOnAttribute()
        configureToolbarContentTypeBasedOnAttribute()
    }

    /**
     * Set icon config. For available icon
     * @see
     * IconList.kt
     */
    fun setIcon(iconConfig: IconConfig) {
        navIconAdapter = NavToolbarIconAdapter(iconConfig)
        val navIconRecyclerView = rv_icon_list
        navIconRecyclerView.adapter = navIconAdapter
        navIconRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        if (toolbarThemeType == TOOLBAR_DARK_TYPE) {
            navIconAdapter.setThemeState(NavToolbarIconAdapter.STATE_THEME_DARK)
        } else {
            navIconAdapter.setThemeState(NavToolbarIconAdapter.STATE_THEME_LIGHT)
        }
    }

    /**
     * Call this function to let the NavToolbar manage your status bar transparency
     */
    fun setupToolbarWithStatusBar(activity: Activity) {
        statusBarUtil = StatusBarUtil(WeakReference(activity))
        applyStatusBarPadding()
    }

    /**
     * Hide shadow and adjust padding
     */
    fun hideShadow() {
        if(shadowApplied){
            shadowApplied = false
            val pB = 0
            toolbar?.background = ColorDrawable(getLightIconColor())
            toolbar?.updatePadding(bottom = pB)
        }
    }

    /**
     * Show shadow and adjust padding
     */
    fun showShadow() {
        if(!shadowApplied && toolbarAlwaysShowShadow){
            shadowApplied = true
            val pB = resources.getDimensionPixelSize(R.dimen.dp_8)
            toolbar?.background = ContextCompat.getDrawable(context, R.drawable.searchbar_bg_shadow_bottom)
            toolbar?.updatePadding(bottom = pB)
        }
    }

    /**
     * Switch to dark toolbar manually. To manage toolbar automatically
     * @see
     * NavRecyclerViewScrollListener
     */
    fun switchToDarkToolbar() {
        if (toolbarThemeType != TOOLBAR_DARK_TYPE) {
            navIconAdapter.setThemeState(NavToolbarIconAdapter.STATE_THEME_DARK)
            toolbarThemeType = TOOLBAR_DARK_TYPE
            setBackButtonColor(getLightIconColor())
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
            navIconAdapter.setThemeState(NavToolbarIconAdapter.STATE_THEME_LIGHT)
            toolbarThemeType = TOOLBAR_LIGHT_TYPE
            setBackButtonColor(getDarkIconColor())
            toolbar_title.setTextColor(ContextCompat.getColor(context, R.color.Neutral_N700_96))
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
                       shouldShowTransition: Boolean = true) {
        showSearchbar()

        var applinkForController = applink
        if (applink.isEmpty()) applinkForController = ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE
        navSearchBarController = NavSearchbarController(
                this,
                applinkForController,
                searchbarClickCallback = searchbarClickCallback, searchbarImpressionCallback = searchbarImpressionCallback)
        navSearchBarController.setHint(hints, shouldShowTransition, durationAutoTransition)
    }

    fun setBadgeCounter(iconId: Int, counter: Int) {
        navIconAdapter.setIconCounter(iconId, counter)
    }

    fun setOnBackButtonClickListener(backButtonClickListener: ()-> Unit) {
        nav_icon_back.setOnClickListener { backButtonClickListener }
    }

    fun setToolbarContentType(toolbarContentType: Int) {
        when(toolbarContentType) {
            TOOLBAR_TYPE_TITLE -> {
                showTitle()
            }
            TOOLBAR_TYPE_SEARCH -> {
                showSearchbar()
            }
        }
    }

    internal fun setBackgroundAlpha(alpha: Float) {
        toolbar?.let {
            val drawable = it.background
            drawable.alpha = alpha.toInt()
            it.background = drawable
        }
    }

    private fun applyStatusBarPadding() {
        var pT = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pT = ViewHelper.getStatusBarHeight(context)
        }
        toolbar?.updatePadding(top = pT)
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

    private fun Toolbar.updatePadding(left: Int = paddingLeft, top: Int = paddingTop, right: Int = paddingRight, bottom: Int = paddingBottom) {
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
        }
    }

    private fun configureShadowBasedOnAttribute() {
        if (toolbarAlwaysShowShadow) {
            showShadow()
        } else {
            hideShadow()
        }
    }

    private fun configureThemeBasedOnAttribute() {
        if (initialTheme == TOOLBAR_DARK_TYPE) {
            switchToDarkToolbar()
        } else {
            switchToLightToolbar()
        }
    }

    private fun configureBackButtonBasedOnAttribute(context: Context) {
        if (backType != BACK_TYPE_NONE) {
            when (backType) {
                BACK_TYPE_CLOSE -> {
                    backDrawable = ContextCompat.getDrawable(context, R.drawable.ic_home_nav_close_dark)
                }
                BACK_TYPE_BACK -> {
                    backDrawable = ContextCompat.getDrawable(context, R.drawable.ic_home_nav_back_dark)
                }
            }
            backDrawable?.let {
                setBackButtonColorBasedOnTheme()
                nav_icon_back.visibility = VISIBLE
                if (context is Activity) {
                    nav_icon_back.setOnClickListener { context.onBackPressed() }
                }
            }
        } else {
            nav_icon_back.visibility = GONE
        }
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
        backDrawable?.let {
            val unwrappedDrawable: Drawable = it
            val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedDrawable)
            if (toolbarThemeType == TOOLBAR_DARK_TYPE) {
                DrawableCompat.setTint(wrappedDrawable, getLightIconColor())
            } else {
                DrawableCompat.setTint(wrappedDrawable, getDarkIconColor())
            }
            nav_icon_back.setImageDrawable(it)
        }
    }

    private fun showTitle() {
        isSearchbarShown = false
        toolbar_title.visibility = VISIBLE
        layout_search.visibility = GONE
        toolbar_title.text = toolbarTitle
        setTitleTextColorBasedOnTheme()
    }

    private fun showSearchbar(hints: List<HintData>? = null) {
        isSearchbarShown = true
        toolbar_title.visibility = GONE
        layout_search.visibility = VISIBLE

        hints?.let { setupSearchbar(hints = hints) }
    }

    private fun getDarkIconColor() = ContextCompat.getColor(context, R.color.Neutral_N700)

    private fun getLightIconColor() = ContextCompat.getColor(context, R.color.white)

    private fun setTitleTextColorBasedOnTheme() {
        when(toolbarThemeType) {
            TOOLBAR_DARK_TYPE -> {
                toolbar_title.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            TOOLBAR_LIGHT_TYPE -> {
                toolbar_title.setTextColor(ContextCompat.getColor(context, R.color.Neutral_N700_96))
            }
        }
    }
}