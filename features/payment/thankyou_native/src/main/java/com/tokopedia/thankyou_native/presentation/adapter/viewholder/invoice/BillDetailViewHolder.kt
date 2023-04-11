package com.tokopedia.thankyou_native.presentation.adapter.viewholder.invoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.FeeDetail
import com.tokopedia.thankyou_native.presentation.adapter.model.TotalFee
import com.tokopedia.thankyou_native.presentation.fragment.ToolTipInfoBottomSheet.Companion.openTooltipInfoBottomSheet
import com.tokopedia.unifycomponents.ImageUnify
import kotlinx.android.synthetic.main.thank_widget_bill_detail.view.*

class BillDetailViewHolder(val view: View) : AbstractViewHolder<TotalFee>(view) {

    private var inflater: LayoutInflater? = null
    private val tvInvoiceTotalBillValue = itemView.tvInvoiceTotalBillValue
    private val llContainer: LinearLayout = itemView.llContainerFeeDetail

    override fun bind(element: TotalFee?) {
        element?.let {
            tvInvoiceTotalBillValue.text = getString(R.string.thankyou_rp_without_space,
                    element.totalBillAmountStr)
            element.feeDetailList.forEach {
                addFeeDetail(itemView.context, it)
            }
        } ?: run {
            itemView.gone()
        }
    }


    private fun addFeeDetail(context: Context, feeDetail: FeeDetail) {
        if (inflater == null)
            inflater = LayoutInflater.from(context)
        inflater?.let { inflater ->
            val feeDetailView = inflater.inflate(R.layout.thank_payment_mode_item,
                    null, false)
            val feeDetailTitle = feeDetailView.findViewById<TextView>(R.id.tvInvoicePaymentModeName)
            feeDetailTitle.text = feeDetail.feeTitle
            feeDetailView.findViewById<TextView>(R.id.tvInvoicePaidWithModeValue)
                    .text = feeDetail.feeAmountStr
            val tooltipInfoIcon = feeDetailView.findViewById<IconUnify>(R.id.infoToolTip)
            setUpToolTipInfoIcon(context,tooltipInfoIcon,feeDetail)
            llContainer.addView(feeDetailView)
        }
    }

    private fun setUpToolTipInfoIcon(context: Context,toolTipIconView:IconUnify,feeDetail: FeeDetail){
        feeDetail.showToolTip?.let {
            if(feeDetail.showToolTip.toBoolean()){
                toolTipIconView.visible()
                toolTipIconView.setOnClickListener {
                    openTooltipInfoBottomSheet(
                        context as FragmentActivity,feeDetail.tooltipTitle,feeDetail.tooltipDesc)
                }
            }
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_bill_detail
    }
}
