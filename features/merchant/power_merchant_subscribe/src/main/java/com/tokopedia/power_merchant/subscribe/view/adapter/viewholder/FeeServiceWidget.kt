package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.databinding.ItemFeeServiceBinding
import com.tokopedia.power_merchant.subscribe.view.model.WidgetFeeServiceUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class FeeServiceWidget(
    itemView: View,
    private val listener: Listener,
    private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<WidgetFeeServiceUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_fee_service
    }

    private val binding: ItemFeeServiceBinding? by viewBinding()

    override fun bind(element: WidgetFeeServiceUiModel) {
        binding?.run {

            tvPmLearnFee.setOnClickListener {
                onLearnFeeClicked(element)
            }
            chevron.setOnClickListener {
                onLearnFeeClicked(element)
            }
            root.addOnImpressionListener(element.impressHolder){
                powerMerchantTracking.sendEventImpressFeeService(element.shopScore.toString())
            }
        }
    }

    private fun onLearnFeeClicked(element: WidgetFeeServiceUiModel) {
        listener.showServiceFeeByCategory()
        powerMerchantTracking.sendEventClickFeeService(element.shopScore.orZero().toString())
    }

    interface Listener {
        fun showServiceFeeByCategory()
    }
}