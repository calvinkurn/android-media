package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.Context
import androidx.fragment.app.DialogFragment
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

        val bottomSheetContentView = SellerHomeBottomSheetContent(mContext)
        bottomSheetContentView.setTooltipData(tooltip)
        setChild(bottomSheetContentView)
    }
}