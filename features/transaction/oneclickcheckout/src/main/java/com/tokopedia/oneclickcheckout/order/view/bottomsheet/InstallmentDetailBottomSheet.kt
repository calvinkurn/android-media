package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.view.View
import android.widget.LinearLayout
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class InstallmentDetailBottomSheet {

    private var bottomSheetUnify: BottomSheetUnify? = null

    fun show(fragment: OrderSummaryPageFragment) {
        fragment.fragmentManager?.let {
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showCloseIcon = true
                showHeader = true
                setTitle("Pilih Jenis Pembayaran")

                val child = View.inflate(fragment.context, R.layout.bottom_sheet_installment, null)
                val ll = child.findViewById<LinearLayout>(R.id.main_content)
                ll.addView(View.inflate(fragment.context, R.layout.item_installment_detail, null), 0)
                ll.addView(View.inflate(fragment.context, R.layout.item_installment_detail, null), 0)
                ll.addView(View.inflate(fragment.context, R.layout.item_installment_detail, null), 0)
                ll.addView(View.inflate(fragment.context, R.layout.item_installment_detail, null), 0)
                ll.addView(View.inflate(fragment.context, R.layout.item_installment_detail, null), 0)
                ll.addView(View.inflate(fragment.context, R.layout.item_installment_detail, null), 0)
                setChild(child)
                show(it, null)
            }
        }
    }
}