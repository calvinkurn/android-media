package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailPayments
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailDynamicPriceAdapter
import kotlinx.android.synthetic.main.detail_payments_item.view.*

/**
 * Created by fwidjaja on 2019-10-07.
 */
class SomDetailPaymentsViewHolder(itemView: View) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {

    private var somDetailDynamicPriceAdapter: SomDetailDynamicPriceAdapter? = null

    override fun bind(item: SomDetailData, position: Int) {
        if (item.dataObject is SomDetailPayments) {
            val somDetailPayments = item.dataObject
            with(itemView) {
                rvDynamicPrice.apply {
                    somDetailDynamicPriceAdapter = SomDetailDynamicPriceAdapter(somDetailPayments.pricingData)
                    layoutManager = LinearLayoutManager(context)
                    adapter = somDetailDynamicPriceAdapter
                }

                total_price_value.text = somDetailPayments.paymentDataUiModel.value
                total_price_label.text = somDetailPayments.paymentDataUiModel.label
                if(somDetailPayments.paymentDataUiModel.textColor.isNotBlank()) {
                    total_price_label.setTextColor(Color.parseColor(somDetailPayments.paymentDataUiModel.textColor))
                    total_price_value.setTextColor(Color.parseColor(somDetailPayments.paymentDataUiModel.textColor))
                }
            }
        }
    }
}