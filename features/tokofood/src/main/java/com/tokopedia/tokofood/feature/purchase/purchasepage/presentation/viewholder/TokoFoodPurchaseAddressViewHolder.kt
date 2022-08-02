package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseAddressBinding
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseAddressTokoFoodPurchaseUiModel

class TokoFoodPurchaseAddressViewHolder(private val viewBinding: ItemPurchaseAddressBinding,
                                        private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseAddressTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_address
    }

    override fun bind(element: TokoFoodPurchaseAddressTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            chooseAddressButton.setOnClickListener {
                listener.onTextChangeShippingAddressClicked()
            }
            textAddressName.text = element.addressName
            if (element.isMainAddress) {
                labelMainAddress.show()
            } else {
                labelMainAddress.gone()
            }
            val addressAndPhone = itemView.context?.getString(
                com.tokopedia.tokofood.R.string.text_purchase_address_phone,
                element.receiverName,
                element.receiverPhone
            ).orEmpty()
            textReceiverNameAndPhone.text = addressAndPhone
            textAddressDetail.text = element.addressDetail
        }
    }

}