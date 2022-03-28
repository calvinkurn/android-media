package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseShippingBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseShippingUiModel
import com.tokopedia.tokofood.purchase.renderAlpha

class TokoFoodPurchaseShippingViewHolder(private val viewBinding: ItemPurchaseShippingBinding,
                                         private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseShippingUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_shipping
    }

    override fun bind(element: TokoFoodPurchaseShippingUiModel) {
        if (element.isNeedPinpoint) {
            renderPinpoint(viewBinding, element)
        } else {
            renderShipping(viewBinding, element)
        }
        itemView.renderAlpha(element)
    }

    private fun renderPinpoint(viewBinding: ItemPurchaseShippingBinding, element: TokoFoodPurchaseShippingUiModel) {
        with(viewBinding) {
            containerPinpoint.show()
            containerShipping.gone()
            val noPinpointFullInformation = itemView.context.getString(R.string.text_purchase_message_need_pinpoint)
            textNoPinpoint.text = MethodChecker.fromHtml(noPinpointFullInformation)
            textNoPinpoint.setOnClickListener {
                listener.onTextSetPinpointClicked()
            }
        }
    }

    private fun renderShipping(viewBinding: ItemPurchaseShippingBinding, element: TokoFoodPurchaseShippingUiModel) {
        with(viewBinding) {
            containerShipping.show()
            containerPinpoint.gone()
            imageShippingLogo.setImageUrl(element.shippingLogoUrl)
            textShippingCourierName.text = element.shippingCourierName
            textShippingEta.text = element.shippingEta
        }
    }


}