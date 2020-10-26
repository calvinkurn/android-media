package com.tokopedia.home_account.view.custom

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
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.home_account_dynamic_action.view.*
import kotlinx.android.synthetic.main.home_account_toolbar.view.*

/**
 * Created by Yoris Prayogo on 24/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountUserToolbar : Toolbar {
    private var currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT
    var tvToolbarTitle: TextView? = null
    var mContext: Context? = null
    var backArrowWhite: Drawable? = null
    var view: View? = null

    constructor(context: Context) : super(context) {
        inflateResource(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inflateResource(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflateResource(context)
    }

    private fun inflateResource(context: Context) {
        mContext = context
        view = View.inflate(context, R.layout.home_account_toolbar, this)
        tvToolbarTitle = findViewById(R.id.tv_tpToolbar_title)
        initDrawableResources()

        //  setCouponCount(0, "");
        setNavIcon(context)
    }

    private fun setNavIcon(context: Context) {
        post {
            val NAV_ICON_POSITION = 1
            val v = getChildAt(NAV_ICON_POSITION)
            if (v != null && v.layoutParams is LayoutParams && v is AppCompatImageButton) {
                val lp = v.getLayoutParams() as LayoutParams
                lp.width = context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_48)
                v.setLayoutParams(lp)
                v.invalidate()
                v.requestLayout()
            }
        }
    }

    private fun initDrawableResources() {
        backArrowWhite = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_back_arrow)
        navigationIcon = backArrowWhite
    }

    override fun setTitle(title: CharSequence) {
        if (title == resources.getString(R.string.home_account_title)) return
        super.setTitle(resources.getString(R.string.home_account_title))
        tvToolbarTitle?.text = resources.getString(R.string.home_account_title)
    }


    fun switchToDarkMode() {
        if (currentToolbarState == ToolbarState.TOOLBAR_DARK) return
        currentToolbarState = ToolbarState.TOOLBAR_DARK
        val whiteColor = MethodChecker.getColor(context, R.color.home_account_toolbar_navigation_white_color)
        val greyColor = MethodChecker.getColor(context, R.color.home_account_toolbar_navigation_grey_color)
        toggleNavigationIconColor(whiteColor, greyColor)
        val colorAnim = ObjectAnimator.ofInt(tvToolbarTitle, "textColor",
                whiteColor, greyColor)
        colorAnim.duration = 200
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.start()
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
        currentToolbarState = ToolbarState.TOOLBAR_TRANSPARENT
        val whiteColor = MethodChecker.getColor(context, R.color.home_account_toolbar_navigation_grey_color)
        val greyColor = MethodChecker.getColor(context, R.color.home_account_toolbar_navigation_white_color)
        toggleNavigationIconColor(greyColor, whiteColor)
        val colorAnim = ObjectAnimator.ofInt(tvToolbarTitle, "textColor", greyColor, whiteColor)
        colorAnim.duration = 200
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.start()
    }

    fun applyAlphaToToolbarBackground(alpha: Float) {
        mContext?.resources?.getColor(android.R.color.white)?.let { adjustAlpha(it, alpha) }?.let {
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
        transition.addTarget(container_scrolledState)
        TransitionManager.beginDelayedTransition(this, transition)
        container_scrolledState.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun convertDpToPixel(dp: Int, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }


    fun addItem(dynamicActionItem: ProfileDataView) : View? {
        view?.let {
            val viewCntainer = View.inflate(context, R.layout.home_account_dynamic_action, null)
            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1F)
            param.rightMargin = convertDpToPixel(10,viewCntainer.context)
            ImageUtils.loadImage(viewCntainer.iv_dynamic, dynamicActionItem.avatar)
//            if (dynamicActionItem.counter?.isShowCounter != null && dynamicActionItem.counter.counterStr != null
//                    && dynamicActionItem.counter.counterStr.isNotEmpty() && dynamicActionItem.counter.counterStr != "0") {
//                viewCntainer.notif_dynamic.visibility = View.VISIBLE
//                viewCntainer.notif_dynamic.setNotification(dynamicActionItem.counter.counterStr, NotificationUnify.NONE_TYPE, NotificationUnify.COLOR_PRIMARY)
//            }
            it.container_scrolledState.addView(viewCntainer, param)
            return viewCntainer
        }
        return null
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