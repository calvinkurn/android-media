package com.tokopedia.home_component.customview.pullrefresh2

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import com.tokopedia.home_component.R

class LottieSwipeRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : SimpleSwipeRefreshLayout(context, attrs, defStyle) {

    private var animationFile: Int = -1

//    private val lottieAnimationView by lazy {
//        LottieAnimationView(context).apply {
//            if (animationFile == -1) {
//                throw IllegalStateException("Could not resolve an animation for your pull to refresh layout")
//            }
//
//            setAnimation(animationFile)
//            repeatCount = LottieDrawable.INFINITE
//            val size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_INDICATOR_TARGET, context.resources.displayMetrics).toInt()
//
//            layoutParams = LayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size))
//            setPadding(0, 26f.toDpInt(), 0, 0)
//
//            val density = context.resources.displayMetrics.density
////            val circle = ShapeDrawable(OvalShape())
////            circle.paint.color = Color.WHITE
//            ViewCompat.setElevation(this, ELEVATION * density)
//
////            background = circle
//        }
//    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LottieSwipeRefreshLayout, defStyle, 0).let { style ->
            val layout = LinearLayout(context)
//            layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            layout.background = ColorDrawable(resources.getColor(R.color.GeneralDarkBlue))
//            background = ColorDrawable(resources.getColor(R.color.GeneralDarkBlue))
            animationFile = style.getResourceId(R.styleable.LottieSwipeRefreshLayout_lottie_rawRes, -1)
//            addView(layout)
//            layout.addView(lottieAnimationView)
//            addView()

//            addView(lottieAnimationView)
            style.recycle()
        }

        setOnRefreshListener {
            Log.d("dhabalog", "startRefresh")
//            lottieSwipeRefreshListener?.changeStatusBarToDark()
        }

        addProgressListener {
            Log.d("dhabalog", it.toString())
//            lottieAnimationView.progress = it
//            if (it > 0.5f) {
//                lottieSwipeRefreshListener?.changeStatusBarToDark()
//            }
        }

        addTriggerListener {
//            lottieAnimationView.resumeAnimation()
        }

        removeOnTriggerListener {
            Log.d("dhabalog", "stopRefresh")
        }
    }

    fun setColorSchemeResources(color: Int) {
        val filter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
//        val callback: LottieValueCallback<ColorFilter> = LottieValueCallback(filter)
//        lottieAnimationView.addValueCallback(KeyPath("**"), LottieProperty.COLOR_FILTER, callback)
    }

    override fun stopRefreshing() {
        super.stopRefreshing()
//        lottieAnimationView.pauseAnimation()
//        if (lottieAnimationView.progress < 0.65f) {
//            lottieSwipeRefreshListener?.changeStatusBarToLight()
//        }
    }

    override fun startRefreshing() {
        super.startRefreshing()
//        lottieAnimationView.resumeAnimation()
//        lottieSwipeRefreshListener?.changeStatusBarToDark()
    }

//    fun setCanChildScrollUp(canChildScrollUp: Boolean) {
//        this@LottieSwipeRefreshLayout.canChildScrollUp = canChildScrollUp
//    }
}
