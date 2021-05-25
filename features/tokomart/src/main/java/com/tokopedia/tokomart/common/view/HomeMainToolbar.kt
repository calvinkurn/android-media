package com.tokopedia.tokomart.common.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.asynclayoutinflater.view.AsyncLayoutInflater.OnInflateFinishedListener
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.Ease
import com.tokopedia.searchbar.helper.EasingInterpolator
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.tokomart.R
import kotlinx.android.synthetic.main.home_main_toolbar_tokonow.view.*
import kotlinx.coroutines.*
import java.net.URLEncoder
import java.util.concurrent.Callable
import kotlin.coroutines.CoroutineContext
import kotlin.text.Charsets.UTF_8

@Deprecated( message = "this class is used as oldtoolbar")
class HomeMainToolbar : MainToolbar, CoroutineScope {

    companion object {
        const val TOOLBAR_LIGHT_TYPE = 0
        const val TOOLBAR_DARK_TYPE = 1
        private const val HOME_SOURCE = "home"
        private const val PARAM_APPLINK_AUTOCOMPLETE = "?navsource={source}&hint={hint}&first_install={first_install}"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var searchMagnifierIcon: Drawable
    private lateinit var animationJob: Job
    private var toolbarType: Int = 0
    private var shadowApplied: Boolean = false
    private var afterInflationCallable: Callable<Any?>? = null
    private var viewHomeMainToolBar: View? = null
    private var iconCart: Drawable? = null
    private var iconSharing: Drawable? = null
    private var iconBackButton: Drawable? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun setViewAttributesAfterInflation(){
        showShadow()

        setBackgroundAlpha(0f)

        toolbarType = TOOLBAR_LIGHT_TYPE
        launch {
            //main
            initializeinBackground()
            setImageDrawables()
            findViewById<ImageView>(R.id.search_magnify_icon).setImageDrawable(searchMagnifierIcon)
            //..........
        }
    }

    fun setAfterInflationCallable(callable: Callable<Any?>){
        afterInflationCallable = callable
    }

    suspend fun initializeinBackground() : Unit = withContext(Dispatchers.IO) {
        initToolbarIcon()
    }

    private fun initToolbarIcon() {
        searchMagnifierIcon = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_search_bar)
        iconSharing = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_sharing_old_toolbar)
        iconCart = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_cart_old_toolbar)
        iconBackButton = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_back_button_old_toolbar)
    }

    private fun setImageDrawables(){
        btnSharing?.setImageDrawable(iconSharing)
        btnCart?.setImageDrawable(iconCart)
        btnBack?.setImageDrawable(iconBackButton)
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
        asyncLayoutInflater.inflate(R.layout.home_main_toolbar_tokonow, this@HomeMainToolbar, inflateFinishCallBack)
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
        if (toolbarType != TOOLBAR_DARK_TYPE) {
            toolbarType = TOOLBAR_DARK_TYPE
        }
    }

    fun switchToLightToolbar() {
        if (toolbarType != TOOLBAR_LIGHT_TYPE ) {
            toolbarType = TOOLBAR_LIGHT_TYPE
        }
    }

    private fun getBitmapDrawableFromVectorDrawable(context: Context, drawableId: Int): Drawable {
        return BitmapDrawable(context.resources, getBitmapFromVectorDrawable(context, drawableId))
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
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
            editTextSearch?.setSingleLine()
            editTextSearch?.ellipsize = TextUtils.TruncateAt.END
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
                        editTextSearch?.hint = hint
                        editTextSearch?.startAnimation(slideUpIn)
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                })
                editTextSearch?.startAnimation(slideOutUp)
                editTextSearch?.setOnClickListener {
                    onClickHint(keyword, isFirstInstall)
                }
                delay(durationAutoTransition)
            }
        }
    }

    private fun setHintSingle(hint: HintData, isFirstInstall: Boolean) {
        editTextSearch?.hint = if (hint.placeholder.isEmpty()) context.getString(R.string.search_tokopedia) else hint.placeholder
        editTextSearch?.setOnClickListener {
            onClickHint(hint.keyword, isFirstInstall)
        }
    }

    private fun onClickHint(keyword: String, isFirstInstall: Boolean) {
        searchBarAnalytics?.eventTrackingSearchBar(screenName, keyword)
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
}
