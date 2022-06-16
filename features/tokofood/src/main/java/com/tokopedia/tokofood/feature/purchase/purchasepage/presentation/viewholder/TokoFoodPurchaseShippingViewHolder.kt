package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.SPACE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseShippingBinding
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseShippingTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.renderAlpha

class TokoFoodPurchaseShippingViewHolder(private val viewBinding: ItemPurchaseShippingBinding,
                                         private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseShippingTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_shipping
    }

    override fun bind(element: TokoFoodPurchaseShippingTokoFoodPurchaseUiModel) {
        when {
            element.isLoading -> {
                renderLoading(viewBinding)
            }
            element.isNeedPinpoint -> {
                renderPinpoint(viewBinding, element)
            }
            else -> {
                renderShipping(viewBinding, element)
            }
        }
        itemView.renderAlpha(element)
    }

    private fun renderLoading(viewBinding: ItemPurchaseShippingBinding) {
        with(viewBinding) {
            containerPinpoint.gone()
            containerShipping.gone()
            containerLoading.show()
        }
    }

    private fun renderPinpoint(viewBinding: ItemPurchaseShippingBinding, element: TokoFoodPurchaseShippingTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            containerPinpoint.show()
            containerShipping.gone()
            containerLoading.gone()
            val noPinpointFullInformation = itemView.context.getString(com.tokopedia.tokofood.R.string.text_purchase_message_need_pinpoint)
            textNoPinpoint.text = MethodChecker.fromHtml(noPinpointFullInformation)
            textNoPinpoint.setOnClickListener {
                listener.onTextSetPinpointClicked()
            }
        }
    }

    private fun renderShipping(viewBinding: ItemPurchaseShippingBinding, element: TokoFoodPurchaseShippingTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            containerShipping.show()
            containerPinpoint.gone()
            containerLoading.gone()
            imageShippingLogo.setImageUrl(element.shippingLogoUrl)
            textShippingCourierName.text =
                getShippingCourierInfo(element.shippingCourierName, element.shippingPriceFmt)
            textShippingEta.text = element.shippingEta
        }
    }

    private fun getShippingCourierInfo(courierName: String,
                                       shippingPriceFmt: String): String {
        var shippingInfo = courierName
        if (shippingPriceFmt.isNotEmpty()) {
            shippingInfo += String.SPACE + itemView.context.getString(
                com.tokopedia.tokofood.R.string.text_purchase_shipping_price,
                shippingPriceFmt
            )
        }
        return shippingInfo
    }


}