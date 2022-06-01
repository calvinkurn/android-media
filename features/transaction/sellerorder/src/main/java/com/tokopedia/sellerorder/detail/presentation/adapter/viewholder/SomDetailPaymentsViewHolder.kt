package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.DetailPaymentsItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailPayments
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailDynamicPriceAdapter
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by fwidjaja on 2019-10-07.
 */
class SomDetailPaymentsViewHolder(itemView: View?) : AbstractViewHolder<SomDetailData>(itemView) {

    companion object {
        val LAYOUT = R.layout.detail_payments_item
    }

    private var somDetailDynamicPriceAdapter: SomDetailDynamicPriceAdapter? = null
    private val binding by viewBinding<DetailPaymentsItemBinding>()

    override fun bind(item: SomDetailData) {
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
            }
        }
    }
}