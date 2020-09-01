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

class TooltipBottomSheet(
        mContext: Context,
        tooltip: TooltipUiModel
) : BottomSheetUnify() {

    init {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        setTitle(tooltip.title)
        showCloseIcon = false
        isDragable = true
        showKnob = true
        isHideable = true

        val child = SellerHomeBottomSheetContent(mContext)
        child.setTooltipData(tooltip)
        setChild(child)
        child.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        customPeekHeight = child.measuredHeight
        setShowListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}