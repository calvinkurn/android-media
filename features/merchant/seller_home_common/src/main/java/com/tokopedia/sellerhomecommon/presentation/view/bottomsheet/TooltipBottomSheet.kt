package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.Context
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By @ilhamsuaib on 16/06/20
 */

class TooltipBottomSheet : BottomSheetUnify() {

    companion object {
        fun createInstance(): TooltipBottomSheet {
            return TooltipBottomSheet().apply {
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                showCloseIcon = false
                isDragable = true
                showKnob = true
                isHideable = true
            }
        }
    }

    fun init(context: Context, tooltip: TooltipUiModel) {
        setTitle(tooltip.title)
        val child = SellerHomeBottomSheetContent(context)
        child.setTooltipData(tooltip)
        setChild(child)
        child.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        customPeekHeight = child.measuredHeight
        setShowListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}