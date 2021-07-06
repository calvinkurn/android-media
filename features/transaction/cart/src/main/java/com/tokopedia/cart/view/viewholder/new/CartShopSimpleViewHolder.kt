package com.tokopedia.cart.view.viewholder.new

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartShopSimpleBinding
import com.tokopedia.cart.view.uimodel.new.CartShopSimpleHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class CartShopSimpleViewHolder(val viewBinding: ItemCartShopSimpleBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        var LAYOUT = R.layout.item_cart_shop_simple
    }

    fun bind(cartShopSimpleHolderData: CartShopSimpleHolderData) {
        renderCheckBox(cartShopSimpleHolderData)
        renderShopBadge(cartShopSimpleHolderData)
        renderShopName(cartShopSimpleHolderData)
        renderPinImage(cartShopSimpleHolderData)
        renderFulfillment(cartShopSimpleHolderData)
        renderEta(cartShopSimpleHolderData)
        renderPreOrder(cartShopSimpleHolderData)
        renderIncidentInfo(cartShopSimpleHolderData)
        renderFreeShipping(cartShopSimpleHolderData)
    }

    private fun renderPinImage(cartShopSimpleHolderData: CartShopSimpleHolderData) {
        if (cartShopSimpleHolderData.isTokoNow) {
            viewBinding.imagePin.show()
        } else {
            viewBinding.imagePin.gone()
        }
    }

    private fun renderFreeShipping(cartShopSimpleHolderData: CartShopSimpleHolderData) {
        if (cartShopSimpleHolderData.freeShippingUrl.isNotBlank()) {
            ImageHandler.loadImageWithoutPlaceholder(viewBinding.imageFreeShipping, cartShopSimpleHolderData.freeShippingUrl)
            viewBinding.imageFreeShipping.show()
            viewBinding.separatorFreeShipping.show()
        } else {
            viewBinding.imageFreeShipping.show()
            viewBinding.separatorFreeShipping.show()
        }
    }

    private fun renderIncidentInfo(cartShopSimpleHolderData: CartShopSimpleHolderData) {
        if (cartShopSimpleHolderData.incidentInfo.isNotBlank()) {
            viewBinding.labelIncident.text = cartShopSimpleHolderData.incidentInfo
            viewBinding.labelIncident.show()
            viewBinding.separatorIncident.show()
        } else {
            viewBinding.labelIncident.show()
            viewBinding.separatorIncident.show()
        }
    }

    private fun renderPreOrder(cartShopSimpleHolderData: CartShopSimpleHolderData) {
        if (cartShopSimpleHolderData.preOrderInfo.isNotBlank()) {
            viewBinding.labelPreOrder.text = cartShopSimpleHolderData.preOrderInfo
            viewBinding.labelPreOrder.show()
            viewBinding.separatorPreOrder.show()
        } else {
            viewBinding.labelPreOrder.gone()
            viewBinding.separatorPreOrder.gone()
        }
    }

    private fun renderFulfillment(cartShopSimpleHolderData: CartShopSimpleHolderData) {
        viewBinding.textShopLocation.text = cartShopSimpleHolderData.shopLocation
        if (cartShopSimpleHolderData.imageFulfilmentUrl.isNotBlank()) {
            ImageHandler.loadImageWithoutPlaceholder(viewBinding.imageFulfillment, cartShopSimpleHolderData.imageFulfilmentUrl)
            viewBinding.imageFulfillment.show()
        } else {
            viewBinding.imageFulfillment.gone()
        }
    }

    private fun renderEta(cartShopSimpleHolderData: CartShopSimpleHolderData) {
        if (cartShopSimpleHolderData.estimatedTimeArrival.isNotBlank()) {
            viewBinding.textEstimatedTimeArrival.text = cartShopSimpleHolderData.estimatedTimeArrival
            viewBinding.textEstimatedTimeArrival.show()
            viewBinding.separatorEstimatedTimeArrival.show()
        } else {
            viewBinding.textEstimatedTimeArrival.gone()
            viewBinding.separatorEstimatedTimeArrival.gone()
        }
    }

    private fun renderShopBadge(cartShopSimpleHolderData: CartShopSimpleHolderData) {
        viewBinding.imageShopBadge.let {
            if (cartShopSimpleHolderData.shopBadgeUrl.isNotBlank()) {
                ImageHandler.loadImageWithoutPlaceholder(it, cartShopSimpleHolderData.shopBadgeUrl)
                it.show()
            } else {
                it.gone()
            }
        }
    }

    private fun renderShopName(cartShopSimpleHolderData: CartShopSimpleHolderData) {
        viewBinding.textShopName.text = cartShopSimpleHolderData.shopName
    }

    private fun renderCheckBox(cartShopSimpleHolderData: CartShopSimpleHolderData) {
        viewBinding.cbSelectShop.isChecked = cartShopSimpleHolderData.isChecked
    }

}