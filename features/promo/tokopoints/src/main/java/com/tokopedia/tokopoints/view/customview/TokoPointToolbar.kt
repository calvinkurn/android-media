package com.tokopedia.tokopoints.view.customview

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
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.databinding.TpItemDynamicActionBinding
import com.tokopedia.tokopoints.databinding.TpToolbarBinding
import com.tokopedia.tokopoints.view.model.rewardtopsection.DynamicActionListItem
import com.tokopedia.tokopoints.view.util.ImageUtil
import com.tokopedia.tokopoints.view.util.convertDpToPixel
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TokoPointToolbar : Toolbar {
    private var currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT
    var mContext: Context? = null
    var backArrowWhite: Drawable? = null

    constructor(context: Context) : super(context) {
        inflateResource(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inflateResource(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflateResource(context)
    }

    private val binding: TpToolbarBinding = TpToolbarBinding.inflate(LayoutInflater.from(context), this, true)

    private fun inflateResource(context: Context) {
        mContext = context
        initDrawableResources()

        //  setCouponCount(0, "");
        setNavIcon(context)
        if (context.isDarkMode()) {
            setDarkmode(context)
        }
    }

    private fun setDarkmode(context: Context){
        navigationIcon?.setColorFilter(
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_Static_White),
            PorterDuff.Mode.SRC_ATOP
        )
        binding.tvTpToolbarTitle.setTextColor(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_Static_White))
    }

    private fun setNavIcon(context: Context) {
        post {
            val NAV_ICON_POSITION = 1
            val v = getChildAt(NAV_ICON_POSITION)
            if (v != null && v.layoutParams is LayoutParams && v is AppCompatImageButton) {
                val lp = v.getLayoutParams() as LayoutParams
                lp.width = context.resources.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_48)
                v.setLayoutParams(lp)
                v.invalidate()
                v.requestLayout()
            }
        }
    }

    private fun initDrawableResources() {
        backArrowWhite = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_new_action_back_tokopoints)
        navigationIcon = backArrowWhite
    }

    override fun setTitle(title: CharSequence) {
        if (title == resources.getString(R.string.tp_title_tokopoints)) return
        super.setTitle(resources.getString(R.string.tp_title_tokopoints))
        binding.tvTpToolbarTitle.text = resources.getString(R.string.tp_title_tokopoints)
    }


    fun switchToDarkMode() {
        if (currentToolbarState == ToolbarState.TOOLBAR_DARK) return
        else if (context.isDarkMode()) { setDarkmode(context) }
        else {
            currentToolbarState = ToolbarState.TOOLBAR_DARK
            val whiteColor =
                MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN0)
            val greyColor =
                MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN600)
            toggleNavigationIconColor(whiteColor, greyColor)
            val colorAnim = ObjectAnimator.ofInt(
                binding.tvTpToolbarTitle, "textColor",
                whiteColor, greyColor
            )
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

    fun switchToTransparentMode() {
        if (currentToolbarState == ToolbarState.TOOLBAR_TRANSPARENT) return
        else if (context.isDarkMode()) { setDarkmode(context) }
        else {
            currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT
            val whiteColor =
                MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN0)
            val greyColor =
                MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN600)
            toggleNavigationIconColor(greyColor, whiteColor)
            val colorAnim = ObjectAnimator.ofInt(binding.tvTpToolbarTitle, "textColor", greyColor, whiteColor)
            colorAnim.duration = 200
            colorAnim.setEvaluator(ArgbEvaluator())
            colorAnim.start()
        }
    }

    fun applyAlphaToToolbarBackground(alpha: Float) {
        mContext?.resources?.getColor(unifyprinciplesR.color.Unify_NN0)?.let { adjustAlpha(it, alpha) }?.let {
            setBackgroundColor(it)
        }
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
            Bitmap.createBitmap(it,
                    drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        } ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun showToolbarIcon() {
        toggle(true)
    }

    fun hideToolbarIcon() {
        toggle(false)
    }

    private fun toggle(show: Boolean) {
        val transition: Transition = Fade()
        transition.duration = 300
        transition.addTarget(binding.containerScrolledState)
        TransitionManager.beginDelayedTransition(this, transition)
        binding.containerScrolledState.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun addItem(dynamicActionItem: DynamicActionListItem) : TpItemDynamicActionBinding {
        val viewCntainer = TpItemDynamicActionBinding.inflate(LayoutInflater.from(context), null, false)
        val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1F)
        param.rightMargin = convertDpToPixel(10,viewCntainer.root.context)
        ImageUtil.loadImage(viewCntainer.ivDynamic, dynamicActionItem.iconImageURLScrolled)
        if (dynamicActionItem.counter?.isShowCounter != null && dynamicActionItem.counter.counterStr != null
            && dynamicActionItem.counter.counterStr.isNotEmpty() && dynamicActionItem.counter.counterStr != "0") {
            viewCntainer.notifDynamic.visibility = View.VISIBLE
            viewCntainer.notifDynamic.setNotification(dynamicActionItem.counter.counterStr, NotificationUnify.NONE_TYPE, NotificationUnify.COLOR_PRIMARY)
        }
        binding.containerScrolledState.addView(viewCntainer.root, param)
        return viewCntainer
    }

    internal enum class ToolbarState {
        TOOLBAR_DARK, TOOLBAR_TRANSPARENT
    }

    companion object {
        @ColorInt
        fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
            val alpha = Math.round(Color.alpha(color) * factor)
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)
            return Color.argb(alpha, red, green, blue)
        }
    }
}
