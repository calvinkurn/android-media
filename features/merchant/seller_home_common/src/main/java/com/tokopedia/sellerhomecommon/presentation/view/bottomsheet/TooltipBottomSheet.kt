package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
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
                showCloseIcon = false
                isDragable = true
                showKnob = true
                isHideable = true
            }
        }
    }

    private var tooltipShowLister: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, com.tokopedia.unifycomponents.R.style.UnifyBottomSheetNotOverlapStyle)
    }

    fun init(context: Context, tooltip: TooltipUiModel) {
        if (tooltip.title.isNotBlank()) {
            setTitle(tooltip.title)
        } else {
            showHeader = false
        }
        val child = SellerHomeBottomSheetContent(context)
        child.setTooltipData(tooltip)
        setChild(child)
        child.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        customPeekHeight = child.measuredHeight
        setShowListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            tooltipShowLister?.invoke()
        }
    }

    fun setOnShowListener(action: () -> Unit) {
        tooltipShowLister = action
    }
}