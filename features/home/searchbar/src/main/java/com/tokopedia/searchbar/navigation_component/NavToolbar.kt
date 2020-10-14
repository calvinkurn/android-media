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
    }

    //public variable
    var toolbarType: Int = 0
    private set

    //attribution variable
    private var backType = BACK_TYPE_NONE
    private var showSearchbar = false
    private var initialTheme = TOOLBAR_DARK_TYPE
    private var toolbarFillColor = getLightIconColor()
    private var toolbarAlwaysShowShadow = false
    private var backDrawable: Drawable? = null

    //helper variable
    private var shadowApplied: Boolean = false

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
                showSearchbar = ta.getBoolean(R.styleable.NavToolbar_toolbarShowSearchbar, false)
                initialTheme = ta.getInt(R.styleable.NavToolbar_toolbarInitialTheme, TOOLBAR_DARK_TYPE)
                toolbarAlwaysShowShadow = ta.getBoolean(R.styleable.NavToolbar_toolbarAlwaysShowShadow, false)
            } finally {
                ta.recycle()
            }
        }
        toolbar?.background = ColorDrawable(toolbarFillColor)
        configureThemeBasedOnAttribute()
        configureBackButtonBasedOnAttribute(context)
        configureShadowBasedOnAttribute()
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

        if (toolbarType == TOOLBAR_DARK_TYPE) {
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
        if (toolbarType != TOOLBAR_DARK_TYPE) {
            navIconAdapter.setThemeState(NavToolbarIconAdapter.STATE_THEME_DARK)
            toolbarType = TOOLBAR_DARK_TYPE
            setBackButtonColor(getLightIconColor())
        }
    }

    /**
     * Switch to light toolbar manually. To manage toolbar automatically
     * @see
     * NavRecyclerViewScrollListener
     */
    fun switchToLightToolbar() {
        if (toolbarType != TOOLBAR_LIGHT_TYPE) {
            navIconAdapter.setThemeState(NavToolbarIconAdapter.STATE_THEME_LIGHT)
            toolbarType = TOOLBAR_LIGHT_TYPE
            setBackButtonColor(getDarkIconColor())
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
            if (toolbarType == TOOLBAR_DARK_TYPE) {
                DrawableCompat.setTint(wrappedDrawable, getLightIconColor())
            } else {
                DrawableCompat.setTint(wrappedDrawable, getDarkIconColor())
            }
            nav_icon_back.setImageDrawable(it)
        }
    }

    private fun getDarkIconColor() = ContextCompat.getColor(context, R.color.Neutral_N700)

    private fun getLightIconColor() = ContextCompat.getColor(context, R.color.white)
}