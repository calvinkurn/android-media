package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutProductBinding
import com.tokopedia.checkout.databinding.LayoutCheckoutProductBinding
import com.tokopedia.checkout.databinding.LayoutCheckoutProductBundleBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingWordingModel
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class CheckoutProductViewHolder(
    private val binding: ItemCheckoutProductBinding,
    private val listener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    private val productBinding: LayoutCheckoutProductBinding = LayoutCheckoutProductBinding.bind(binding.root)
    private val bundleBinding: LayoutCheckoutProductBundleBinding = LayoutCheckoutProductBundleBinding.bind(binding.root)

    fun bind(product: CheckoutProductModel) {
        if (product.isBundlingItem) {
            renderBundleItem(product)
        } else {
            renderProductItem(product)
        }
    }

    private fun renderBundleItem(product: CheckoutProductModel) {

    }

    @SuppressLint("SetTextI18n")
    private fun renderProductItem(product: CheckoutProductModel) {
        hideBundleViews()
        productBinding.ivProductImage.setImageUrl(product.imageUrl)
        productBinding.tvProductName.text = product.name
        if (product.variant.isNotBlank()) {
            productBinding.textVariant.text = product.variant
            productBinding.textVariant.isVisible = true
        } else {
            productBinding.textVariant.isVisible = false
        }
        val priceInRp =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(product.price, false)
                .removeDecimalSuffix()
        val qty = product.quantity
        productBinding.tvProductPrice.text = "$qty x $priceInRp"

        if (product.noteToSeller.isNotEmpty()) {
            productBinding.tvOptionalNoteToSeller.text = product.noteToSeller
            productBinding.tvOptionalNoteToSeller.isVisible = true
        } else {
            productBinding.tvOptionalNoteToSeller.isVisible = false
        }
    }

    private fun hideBundleViews() {
        bundleBinding.apply {
            tvCheckoutBundle.isVisible = false
            tvCheckoutBundleSeparator.isVisible = false
            tvCheckoutBundleName.isVisible = false
            tvCheckoutBundlePrice.isVisible = false
            vBundlingProductSeparator.isVisible = false
            ivProductBundleImage.isVisible = false
            tvProductBundleName.isVisible = false
            textVariantBundle.isVisible = false
            tvProductBundleNote.isVisible = false
            tvCheckoutBundleAddons.isVisible = false
            tvCheckoutBundleAddonsSeeAll.isVisible = false
            llAddonProductBundleItems.isVisible = false
        }
    }

    private fun renderAddOnGiftingProductLevel(
        product: CheckoutProductModel,
        addOnWordingModel: AddOnGiftingWordingModel
    ) {
        val addOns = product.addOnGiftingProductLevelModel
        if (addOns.status == 0) {
            binding.buttonGiftingAddonProductLevel.visibility = View.GONE
        } else {
            if (addOns.status == 1) {
                binding.buttonGiftingAddonProductLevel.state =
                    com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView.State.ACTIVE
            } else if (addOns.status == 2) {
                binding.buttonGiftingAddonProductLevel.state =
                    com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView.State.INACTIVE
            }
            binding.buttonGiftingAddonProductLevel.title = addOns.addOnsButtonModel.title
            binding.buttonGiftingAddonProductLevel.desc = addOns.addOnsButtonModel.description
            binding.buttonGiftingAddonProductLevel.urlLeftIcon =
                addOns.addOnsButtonModel.leftIconUrl
            binding.buttonGiftingAddonProductLevel.urlRightIcon =
                addOns.addOnsButtonModel.rightIconUrl
            binding.buttonGiftingAddonProductLevel.setOnClickListener {
                listener.onClickAddOnProductLevel(
                    product,
                    addOnWordingModel
                )
            }
            binding.buttonGiftingAddonProductLevel.visibility = View.VISIBLE
            listener.onImpressionAddOnProductLevel(product.productId.toString())
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_product
    }
}
