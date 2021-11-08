package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.databinding.DetailPaymentsItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailPayments
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailDynamicPriceAdapter
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by fwidjaja on 2019-10-07.
 */
class SomDetailPaymentsViewHolder(itemView: View) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {

    private var somDetailDynamicPriceAdapter: SomDetailDynamicPriceAdapter? = null
    private val binding by viewBinding<DetailPaymentsItemBinding>()

    override fun bind(item: SomDetailData, position: Int) {
        if (item.dataObject is SomDetailPayments) {
            val somDetailPayments = item.dataObject
            binding?.run {
                if(somDetailPayments.paymentMethodUiModel.isNotEmpty()) {
                    paymentsMethodLabel.text = somDetailPayments.paymentMethodUiModel.firstOrNull()?.label
                    val value = somDetailPayments.paymentMethodUiModel.firstOrNull()?.value
                    paymentsMethodValue.text = value
                    paymentsMethodLabel.show()
                    paymentsMethodValue.show()
                } else {
                    paymentsMethodLabel.hide()
                    paymentsMethodValue.hide()
                }

                rvDynamicPrice.apply {
                    somDetailDynamicPriceAdapter = SomDetailDynamicPriceAdapter(somDetailPayments.pricingData)
                    layoutManager = LinearLayoutManager(context)
                    adapter = somDetailDynamicPriceAdapter
                }

                totalPriceValue.text = somDetailPayments.paymentDataUiModel.value
                totalPriceLabel.text = somDetailPayments.paymentDataUiModel.label
                if(somDetailPayments.paymentDataUiModel.textColor.isNotBlank()) {
                    totalPriceLabel.setTextColor(Color.parseColor(somDetailPayments.paymentDataUiModel.textColor))
                    totalPriceValue.setTextColor(Color.parseColor(somDetailPayments.paymentDataUiModel.textColor))
                }
            }
        }
    }
}