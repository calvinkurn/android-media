package com.tokopedia.tokomart.common.view

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.asynclayoutinflater.view.AsyncLayoutInflater.OnInflateFinishedListener
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.searchbar.MainToolbar
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.Ease
import com.tokopedia.searchbar.helper.EasingInterpolator
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.tokomart.R
import kotlinx.android.synthetic.main.home_main_toolbar_tokonow.view.*
import kotlinx.coroutines.*
import java.net.URLEncoder
import java.util.concurrent.Callable
import kotlin.coroutines.CoroutineContext
import kotlin.text.Charsets.UTF_8

@Deprecated("this class is used as oldtoolbar")
class HomeMainToolbar : MainToolbar, CoroutineScope {

    private var KEY_BUNDLE_TOOLBAR_TYPE: String = "key_bundle_toolbar_type"
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var toolbarType: Int = 0

    private var shadowApplied: Boolean = false

    private lateinit var wishlistCrossfader: TransitionDrawable

    private lateinit var notifCrossfader: TransitionDrawable

    private lateinit var inboxCrossfader: TransitionDrawable

    private lateinit var wishlistBitmapWhite: Drawable

    private lateinit var notifBitmapWhite: Drawable

    private lateinit var inboxBitmapWhite: Drawable

    private lateinit var wishlistBitmapGrey: Drawable

    private lateinit var notifBitmapGrey: Drawable

    private lateinit var inboxBitmapGrey: Drawable

    private lateinit var searchMagnifierIcon: Drawable

    private var afterInflationCallable: Callable<Any?>? = null

    private lateinit var animationJob: Job

    private var viewHomeMainToolBar: View? = null

    private var navToolbar: IconUnify? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setViewAttributesAfterInflation(){
        showShadow()

        setBackgroundAlpha(0f)

        toolbarType = TOOLBAR_LIGHT_TYPE
        launch {
            //main
            val result = initializeinBackground()
            result.await()
            setImageDrawables()
            findViewById<ImageView>(R.id.search_magnify_icon).setImageDrawable(searchMagnifierIcon)
            //..........
        }
    }

    fun setAfterInflationCallable(callable: Callable<Any?>){
        afterInflationCallable = callable
    }

    fun initializeinBackground() : Deferred<Unit> = async(Dispatchers.IO){
            initToolbarIcon()
    }

    private fun initToolbarIcon() {
        wishlistBitmapWhite = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_wishlist_white)
        notifBitmapWhite = getBitmapDrawableFromVectorDrawable(context, com.tokopedia.resources.common.R.drawable.ic_system_action_notification_pressed_24)
        inboxBitmapWhite = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_inbox_white)
        searchMagnifierIcon = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_search_bar)

        wishlistBitmapGrey = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_wishlist_grey)
        notifBitmapGrey = getBitmapDrawableFromVectorDrawable(context, com.tokopedia.resources.common.R.drawable.ic_system_action_notification_normal_24)
        inboxBitmapGrey = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_inbox_grey)

        wishlistCrossfader = TransitionDrawable(arrayOf<Drawable>(wishlistBitmapGrey, wishlistBitmapWhite))
        notifCrossfader = TransitionDrawable(arrayOf<Drawable>(notifBitmapGrey, notifBitmapWhite))
        inboxCrossfader = TransitionDrawable(arrayOf<Drawable>(inboxBitmapGrey, inboxBitmapWhite))
    }

    fun setImageDrawables(){
        wishlistCrossfader.startTransition(0)
        notifCrossfader.startTransition(0)
        inboxCrossfader.startTransition(0)

        btnWishlist.setImageDrawable(wishlistCrossfader)
        btnNotification.setImageDrawable(notifCrossfader)
        // hide the third button
        btnNotification.hide()
        btnInbox.setImageDrawable(inboxCrossfader)

        if (toolbarType == TOOLBAR_DARK_TYPE) {
            wishlistCrossfader.resetTransition()
            notifCrossfader.resetTransition()
            inboxCrossfader.resetTransition()

            wishlistCrossfader.reverseTransition(0)
            notifCrossfader.reverseTransition(0)
            inboxCrossfader.reverseTransition(0)
        } else if (toolbarType == TOOLBAR_LIGHT_TYPE) {
            wishlistCrossfader.resetTransition()
            notifCrossfader.resetTransition()
            inboxCrossfader.resetTransition()

            wishlistCrossfader.startTransition(0)
            notifCrossfader.startTransition(0)
            inboxCrossfader.startTransition(0)
        }
    }

    fun hideShadow() {
        if(isShadowApplied()){
            shadowApplied = false
            val pL = toolbar.paddingLeft
            var pT = 0
            pT = ViewHelper.getStatusBarHeight(context)
            val pR = toolbar.paddingRight
            val pB = 0
            toolbar!!.background = ColorDrawable(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            toolbar!!.setPadding(pL, pT, pR, pB)
        }
    }

    fun showShadow() {
        if(!isShadowApplied()){
            shadowApplied = true
            val pL = toolbar.paddingLeft
            var pT = 0
            pT = ViewHelper.getStatusBarHeight(context)
            val pR = toolbar.paddingRight
            val pB = resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8)

            toolbar!!.background = ContextCompat.getDrawable(context, R.drawable.searchbar_bg_shadow_bottom)
            toolbar!!.setPadding(pL, pT, pR, pB)
        }
    }

    override fun inflateResource(context: Context) {

        val asyncLayoutInflater = AsyncLayoutInflater(context)
        val inflateFinishCallBack = OnInflateFinishedListener { view, resid, parent ->
            viewHomeMainToolBar = view
            actionAfterInflation(context, view)
            setViewAttributesAfterInflation()
            afterInflationCallable?.call()
            this@HomeMainToolbar.addView(view)
        }
        if (inflateFinishCallBack != null) {
            asyncLayoutInflater.inflate(R.layout.home_main_toolbar_tokonow, this@HomeMainToolbar, inflateFinishCallBack)
        }
    }

    fun getViewHomeMainToolBar() : View?{
        return viewHomeMainToolBar
    }

    fun setBackgroundAlpha(alpha: Float) {
        val drawable = toolbar!!.background
        drawable.alpha = alpha.toInt()
        toolbar!!.background = drawable
    }

    fun switchToDarkToolbar() {
        if (toolbarType != TOOLBAR_DARK_TYPE && crossfaderIsInitialized()) {
            wishlistCrossfader.reverseTransition(200)
            notifCrossfader.reverseTransition(200)
            inboxCrossfader.reverseTransition(200)

            toolbarType = TOOLBAR_DARK_TYPE
        } else if (!crossfaderIsInitialized()) {
            initToolbarIcon()
            toolbarType = TOOLBAR_DARK_TYPE
        }
        setBackButtonColorBasedOnTheme()
    }

    private fun crossfaderIsInitialized() =
            ::wishlistCrossfader.isInitialized
                    && ::notifCrossfader.isInitialized
                    && ::inboxCrossfader.isInitialized

    private fun getBitmapDrawableFromVectorDrawable(context: Context, drawableId: Int): Drawable {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ContextCompat.getDrawable(context, drawableId) as Drawable
        } else BitmapDrawable(context.resources, getBitmapFromVectorDrawable(context, drawableId))
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }

        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    fun switchToLightToolbar() {
        if (toolbarType != TOOLBAR_LIGHT_TYPE && crossfaderIsInitialized()) {
            wishlistCrossfader.reverseTransition(200)
            notifCrossfader.reverseTransition(200)
            inboxCrossfader.reverseTransition(200)

            toolbarType = TOOLBAR_LIGHT_TYPE
        } else if (!crossfaderIsInitialized()) {
            initToolbarIcon()
            toolbarType = TOOLBAR_LIGHT_TYPE
        }
        setBackButtonColorBasedOnTheme()
    }

    fun isShadowApplied() : Boolean {
        return shadowApplied
    }

    fun setHint(
            hint: HintData,
            hints: ArrayList<HintData>,
            isFirstInstall: Boolean,
            isShowTransition: Boolean,
            durationAutoTransition: Long
    ) {
        if (viewHomeMainToolBar != null) {
            if (::animationJob.isInitialized) {
                animationJob.cancel()
            }
            if (hints.size > 1 && isShowTransition) {
                setHintAnimation(hints, isFirstInstall, durationAutoTransition)
            } else {
                setHintSingle(hint, isFirstInstall)
            }
            editTextSearch.setSingleLine()
            editTextSearch.ellipsize = TextUtils.TruncateAt.END
        }
    }

    private fun setHintAnimation(
            hints: ArrayList<HintData>,
            isFirstInstall: Boolean,
            durationAutoTransition: Long
    ) {
        var iterator = hints.iterator()

        animationJob = launch {
            while (true) {
                var hint = context.getString(R.string.search_tokopedia)
                var keyword = ""
                val slideUpIn = AnimationUtils.loadAnimation(context, R.anim.search_bar_slide_up_in)
                slideUpIn.interpolator = EasingInterpolator(Ease.QUART_OUT)
                val slideOutUp = AnimationUtils.loadAnimation(context, R.anim.slide_out_up)
                slideOutUp.interpolator = EasingInterpolator(Ease.QUART_IN)
                slideOutUp.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        if (iterator.hasNext()) {
                            val placeholder = iterator.next()
                            hint = placeholder.placeholder
                            keyword = placeholder.keyword
                        } else {
                            iterator = hints.iterator()
                            val placeholder = iterator.next()
                            hint = placeholder.placeholder
                            keyword = placeholder.keyword
                        }
                        editTextSearch.hint = hint
                        editTextSearch.startAnimation(slideUpIn)
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                })
                editTextSearch.startAnimation(slideOutUp)
                editTextSearch.setOnClickListener {
                    onClickHint(keyword, isFirstInstall)
                }
                delay(durationAutoTransition)
            }
        }
    }

    fun startHintAnimation() {
        if (::animationJob.isInitialized) {
            animationJob.start()
        }
    }

    fun stopHintAnimation() {
        if (::animationJob.isInitialized) {
            animationJob.cancel()
        }
    }

    private fun setHintSingle(hint: HintData, isFirstInstall: Boolean) {
        editTextSearch.hint = if (hint.placeholder.isEmpty()) context.getString(R.string.search_tokopedia) else hint.placeholder
        editTextSearch.setOnClickListener {
            onClickHint(hint.keyword, isFirstInstall)
        }
    }

    private fun onClickHint(keyword: String, isFirstInstall: Boolean) {
        searchBarAnalytics.eventTrackingSearchBar(screenName, keyword)
        if (keyword.isEmpty()) {
            RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
        } else {
            RouteManager.route(context,
                    ApplinkConstInternalDiscovery.AUTOCOMPLETE + PARAM_APPLINK_AUTOCOMPLETE,
                    HOME_SOURCE,
                    safeEncodeUTF8(keyword),
                    isFirstInstall.toString())
        }
    }

    private fun safeEncodeUTF8(value: String): String {
        return try {
            URLEncoder.encode(value, UTF_8.toString())
        }
        catch (e: Throwable) {
            value
        }
    }

    private fun toolbarThemeCondition(lightCondition: () -> Unit = {}, darkCondition: () -> Unit = {}) {
        if (toolbarType == NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE) lightCondition.invoke()
        if (toolbarType == NavToolbar.Companion.Theme.TOOLBAR_DARK_TYPE) darkCondition.invoke()
    }

    private fun getDarkIconColor() = ContextCompat.getColor(context, com.tokopedia.searchbar.R.color.searchbar_dms_state_light_icon)

    private fun getLightIconColor() = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)

    private fun setBackButtonColorBasedOnTheme() {
        navToolbar = findViewById(com.tokopedia.searchbar.R.id.nav_icon_back)
        toolbarThemeCondition(
                lightCondition = {
                    navToolbar?.setImage(newIconId = IconUnify.ARROW_BACK, newLightEnable = getDarkIconColor())
                },
                darkCondition = {
                    navToolbar?.setImage(newIconId = IconUnify.ARROW_BACK, newLightEnable = getLightIconColor())
                }
        )
        navToolbar?.setOnClickListener {
            (context as? Activity)?.onBackPressed()
        }
    }

    companion object {
        const val TOOLBAR_LIGHT_TYPE = 0
        const val TOOLBAR_DARK_TYPE = 1
        private const val HOME_SOURCE = "home"

        private const val PARAM_APPLINK_AUTOCOMPLETE = "?navsource={source}&hint={hint}&first_install={first_install}"
    }
}
