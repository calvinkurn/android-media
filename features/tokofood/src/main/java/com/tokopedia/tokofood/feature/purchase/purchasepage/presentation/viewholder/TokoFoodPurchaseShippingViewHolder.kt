package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.SPACE
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseShippingBinding
import com.tokopedia.tokofood.feature.purchase.goneAllChildren
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseShippingTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.renderAlpha
import com.tokopedia.tokofood.feature.purchase.visibleAllChildren

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
            containerPinpoint.goneAllChildren()
            containerShipping.goneAllChildren()
            containerLoading.visibleAllChildren()
        }
    }

    private fun renderPinpoint(viewBinding: ItemPurchaseShippingBinding, element: TokoFoodPurchaseShippingTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            containerPinpoint.visibleAllChildren()
            containerShipping.goneAllChildren()
            containerLoading.goneAllChildren()
            val noPinpointFullInformation = itemView.context.getString(com.tokopedia.tokofood.R.string.text_purchase_message_need_pinpoint)
            textNoPinpoint.text = MethodChecker.fromHtml(noPinpointFullInformation)
            textNoPinpoint.setOnClickListener {
                listener.onTextSetPinpointClicked()
            }
        }
    }

    private fun renderShipping(viewBinding: ItemPurchaseShippingBinding, element: TokoFoodPurchaseShippingTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            containerShipping.visibleAllChildren()
            containerPinpoint.goneAllChildren()
            containerLoading.goneAllChildren()
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