package com.tokopedia.merchantvoucher.voucherList.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget

/**
 * Created by hendry on 01/10/18.
 */
class MerchantVoucherViewHolder(
        itemView: View,
        private val merchantVoucherViewListener: MerchantVoucherView.OnMerchantVoucherViewListener?,
        private val onMerchantVoucherListWidgetListener: MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener?
) : AbstractViewHolder<MerchantVoucherViewModel>(itemView) {


    companion object {
        val LAYOUT = R.layout.item_merchant_voucher
        val HORIZONTAL_LAYOUT = R.layout.item_merchant_voucher_horizontal
    }

    override fun bind(element: MerchantVoucherViewModel?) {
        itemView.findViewById<MerchantVoucherView?>(R.id.merchantVoucherView)?.run {
            setData(element)
            onMerchantVoucherViewListener = merchantVoucherViewListener
        }
        element?.let{
            itemView.addOnImpressionListener(it){
                onMerchantVoucherListWidgetListener?.onVoucherItemImpressed(it, adapterPosition)
            }
        }
    }

}
