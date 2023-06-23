package com.tokopedia.shop.flashsale.common.customcomponent

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.flashsale.common.util.KeyboardHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.seller_shop_flash_sale.R

open class ModalBottomSheet : BottomSheetUnify() {

    companion object {
        const val MODAL_WIDTH_RATIO = 0.7
        const val MODAL_WIDE_WIDTH_RATIO = 0.9
        const val MODAL_MARGIN_PERCENTAGE = 0.4f
    }

    var modalWidthRatio = MODAL_WIDTH_RATIO
    var modalMarginPercentage = MODAL_MARGIN_PERCENTAGE
    var useWideModal = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initParentLayout()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        initParentLayout()
    }

    private fun initParentLayout() {
        try {
            val bsLayout = view as LinearLayout
            changeToModalLayout(bsLayout)
            setAlignToCenter(bsLayout)
            setSoftKeyboardHandler(bsLayout)
        } catch (e: Exception) { /* no-op */ }
    }

    private fun setAlignToCenter(bsLayout: LinearLayout) {
        val wSpec = View.MeasureSpec.makeMeasureSpec(bsLayout.layoutParams.width, View.MeasureSpec.EXACTLY)
        val hSpec = View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
        bsLayout.measure(wSpec, hSpec)
        val bsHeight = bsLayout.measuredHeight
        val screenHeight = getScreenHeight()
        val diffHeight = screenHeight - bsHeight
        val topBottomMargin = (diffHeight * modalMarginPercentage).toIntSafely()

        bsLayout.setMargin(Int.ZERO, topBottomMargin, Int.ZERO, topBottomMargin)
        (bsLayout.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER
        bsLayout.requestLayout()
    }

    private fun changeToModalLayout(bsLayout: LinearLayout) {
        bsLayout.layoutParams.width = if (useWideModal) {
            getScreenWidth() * MODAL_WIDE_WIDTH_RATIO
        } else {
            getScreenWidth() * modalWidthRatio
        }.toInt()
        bsLayout.background = requireContext().getDrawable(R.drawable.bg_dialog)
        bsLayout.requestLayout()
    }

    private fun setSoftKeyboardHandler(bsLayout: LinearLayout) {
        KeyboardHandler(
            view ?: return,
            object : KeyboardHandler.OnKeyBoardVisibilityChangeListener {
                override fun onKeyboardShow() {
                    bsLayout.setMargin(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
                    bsLayout.requestLayout()
                }

                override fun onKeyboardHide() {
                    setAlignToCenter(bsLayout)
                }
            }
        )
    }

    fun refreshLayout() {
        try {
            val bsLayout = view as LinearLayout
            changeToModalLayout(bsLayout)
            setAlignToCenter(bsLayout)
        } catch (e: Exception) { /* no-op */ }
    }
}
