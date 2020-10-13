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
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.util.StatusBarUtil
import kotlinx.android.synthetic.main.nav_main_toolbar.view.*
import java.lang.ref.WeakReference

class NavToolbar: Toolbar, LifecycleObserver {
    companion object {
        const val TOOLBAR_LIGHT_TYPE = 0
        const val TOOLBAR_DARK_TYPE = 1
        private const val HOME_SOURCE = "home"

        private const val BACK_TYPE_NONE = 0
        private const val BACK_TYPE_CLOSE = 1
        private const val BACK_TYPE_BACK = 2
    }

    //public variable
    var toolbarType: Int = 0
    private set

    internal var statusBarUtil: StatusBarUtil? = null

    var screenName = ""
    private var shadowApplied: Boolean = false

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

        toolbar!!.background = ColorDrawable(ContextCompat.getColor(context, R.color.white))
        navSearchBarController = NavSearchbarController(this,
                SearchBarTrackingParam(screenName, HOME_SOURCE, PARAM_APPLINK_AUTOCOMPLETE))
        showShadow()
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
            toolbar?.background = ColorDrawable(ContextCompat.getColor(context, R.color.white))
            toolbar?.updatePadding(bottom = pB)
        }
    }

    /**
     * Show shadow and adjust padding
     */
    fun showShadow() {
        if(!shadowApplied){
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
        }
    }

    /**
     * Show and setup searchbar
     * @durationAutoTransition is delay for each hint in ms
     */
    fun setupSearchbar(hints: List<HintData>,
                       paramAutoComplete: String = "",
                       searchbarClickCallback: ((hint: String) -> Unit)? = null,
                       searchbarImpressionCallback: ((hint: String) -> Unit)? = null,
                       durationAutoTransition: Long = 0,
                       shouldShowTransition: Boolean = true) {
        navSearchBarController = NavSearchbarController(
                this,
                paramAutoComplete,
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
}