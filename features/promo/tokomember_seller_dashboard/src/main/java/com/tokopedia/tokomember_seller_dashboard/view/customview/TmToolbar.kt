package com.tokopedia.tokomember_seller_dashboard.view.customview

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class TmToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {
    var tvToolbarTitle: TextView? = null
    var backArrowWhite: Drawable? = null
    var mContext: Context? = null
    var view: View? = null
    private var currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT

    init {
        mContext = context
        view = View.inflate(context, R.layout.tm_toolbar_layout, this)
        tvToolbarTitle = findViewById(R.id.tv_tpToolbar_title)
        initDrawableResources()
        setNavIcon(context)
    }

    private fun setNavIcon(context: Context) {
        post {
            val NAV_ICON_POSITION = 1
            val v = getChildAt(NAV_ICON_POSITION)
            if (v != null && v.layoutParams is LayoutParams && v is AppCompatImageButton) {
                val lp = v.getLayoutParams() as LayoutParams
                lp.width =
                    context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl6)
                lp.gravity = Gravity.CENTER_VERTICAL
                v.setLayoutParams(lp)
                v.invalidate()
                v.requestLayout()
            }
        }
    }

    private fun initDrawableResources() {
        backArrowWhite = getBitmapDrawableFromVectorDrawable(mContext, com.tokopedia.abstraction.R.drawable.ic_action_back)
        navigationIcon = backArrowWhite
    }

    private fun getBitmapDrawableFromVectorDrawable(context: Context?, drawableId: Int): Drawable? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            context?.let { ContextCompat.getDrawable(it, drawableId) }
        } else BitmapDrawable(context?.resources, getBitmapFromVectorDrawable(context, drawableId))
    }

    private fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap? {
        var drawable = context?.let { ContextCompat.getDrawable(it, drawableId) }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = drawable?.let { DrawableCompat.wrap(it).mutate() }
        }
        val bitmap = drawable?.intrinsicWidth?.let {
            Bitmap.createBitmap(
                it,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
        } ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun switchToDarkMode() {
        if (currentToolbarState == ToolbarState.TOOLBAR_DARK) return
        else {
            currentToolbarState = ToolbarState.TOOLBAR_DARK
            val whiteColor =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            val greyColor =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N400)
            toggleNavigationIconColor(whiteColor, greyColor)
            val colorAnim = ObjectAnimator.ofInt(
                tvToolbarTitle, "textColor",
                whiteColor, greyColor
            )
            colorAnim.duration = 200
            colorAnim.setEvaluator(ArgbEvaluator())
            colorAnim.start()
        }
    }

    fun switchToTransparentMode() {
        if (currentToolbarState == ToolbarState.TOOLBAR_TRANSPARENT) return
        else {
            currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT
            val whiteColor =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            val greyColor =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N400)
            toggleNavigationIconColor(greyColor, whiteColor)
            val colorAnim = ObjectAnimator.ofInt(tvToolbarTitle, "textColor", greyColor, whiteColor)
            colorAnim.duration = 200
            colorAnim.setEvaluator(ArgbEvaluator())
            colorAnim.start()
        }
    }

    private fun toggleNavigationIconColor(startColor: Int, endColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && navigationIcon != null) {
            val valueAnimator = ValueAnimator.ofArgb(startColor, endColor)
            valueAnimator.duration = 200L
            valueAnimator.addUpdateListener { animation: ValueAnimator -> navigationIcon?.setColorFilter((animation.animatedValue as Int), PorterDuff.Mode.SRC_ATOP) }
            valueAnimator.start()
        }
    }

    fun applyAlphaToToolbarBackground(alpha: Float) {
        mContext?.resources?.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0)?.let { adjustAlpha(it, alpha) }?.let {
            setBackgroundColor(it)
        }
    }

    @ColorInt
    fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
        val alpha = Math.round(Color.alpha(color) * factor)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    internal enum class ToolbarState {
        TOOLBAR_DARK, TOOLBAR_TRANSPARENT
    }

    override fun setTitle(title: CharSequence) {
        if (title == "TokoMember") return
        super.setTitle("TokoMember")
        tvToolbarTitle?.text = "TokoMember"
    }

}