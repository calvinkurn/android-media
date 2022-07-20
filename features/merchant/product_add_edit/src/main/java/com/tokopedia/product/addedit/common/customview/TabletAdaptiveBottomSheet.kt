package com.tokopedia.product.addedit.common.customview

import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.lang.Exception

open class TabletAdaptiveBottomSheet: BottomSheetUnify() {

    companion object {
        const val MODAL_WIDTH_RATIO = 0.7
        const val MODAL_WIDE_WIDTH_RATIO = 0.9
        const val MODAL_MARGIN_PERCENTAGE = 0.4f
    }

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
            val isTablet = DeviceScreenInfo.isTablet(requireContext())
            if (isTablet) {
                changeToModalLayout(bsLayout)
                setAlignToCenter(bsLayout)
            }
            changeTitleLayout(isTablet)
        } catch (e: Exception) { /* no-op */ }
    }

    private fun setAlignToCenter(bsLayout: LinearLayout) {
        val wSpec = View.MeasureSpec.makeMeasureSpec(bsLayout.layoutParams.width, View.MeasureSpec.EXACTLY)
        val hSpec = View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
        bsLayout.measure(wSpec, hSpec)
        val bsHeight = bsLayout.measuredHeight
        val screenHeight = getScreenHeight()
        val diffHeight = screenHeight - bsHeight
        val topBottomMargin = (diffHeight * MODAL_MARGIN_PERCENTAGE).toIntSafely()

        bsLayout.setMargin(Int.ZERO, topBottomMargin, Int.ZERO, topBottomMargin)
        (bsLayout.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER
        bsLayout.requestLayout()
    }

    private fun changeToModalLayout(bsLayout: LinearLayout) {
        bsLayout.layoutParams.width = if (useWideModal) {
            getScreenWidth() * MODAL_WIDE_WIDTH_RATIO
        } else {
            getScreenWidth() * MODAL_WIDTH_RATIO
        }.toInt()
        bsLayout.background = requireContext().getDrawable(R.drawable.product_add_edit_modal_bg)
        bsLayout.requestLayout()
    }

    private fun changeTitleLayout(isTablet: Boolean) {
        val fontSize = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.fontSize_lvl5).toDp()
        bottomSheetTitle.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
            textAlignment = if (isTablet && !useWideModal) View.TEXT_ALIGNMENT_CENTER else View.TEXT_ALIGNMENT_TEXT_START
            if (isTablet) {
                (layoutParams as RelativeLayout.LayoutParams).removeRule(RelativeLayout.LEFT_OF)
                (layoutParams as RelativeLayout.LayoutParams).removeRule(RelativeLayout.RIGHT_OF)
            }
        }

        bottomSheetClose.apply {
            setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_bottomsheet_close))
            (layoutParams as RelativeLayout.LayoutParams).apply {
                if (isTablet) addRule(RelativeLayout.ALIGN_PARENT_END)
                width = requireContext().resources.getDimension(R.dimen.tooltip_close_size).toInt()
                height = requireContext().resources.getDimension(R.dimen.tooltip_close_size).toInt()
            }
        }
    }
}