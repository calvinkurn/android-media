package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutProductBinding
import com.tokopedia.checkout.databinding.LayoutCheckoutProductBinding
import com.tokopedia.checkout.databinding.LayoutCheckoutProductBundleBinding
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingWordingModel
import com.tokopedia.purchase_platform.common.utils.getHtmlFormat
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.util.*

class CheckoutProductViewHolder(
    private val binding: ItemCheckoutProductBinding,
    private val listener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    private val productBinding: LayoutCheckoutProductBinding =
        LayoutCheckoutProductBinding.bind(binding.root)
    private val bundleBinding: LayoutCheckoutProductBundleBinding =
        LayoutCheckoutProductBundleBinding.bind(binding.root)

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
        renderGroupInfo(product)
        renderShopInfo(product)
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

    private fun renderGroupInfo(product: CheckoutProductModel) {
        if (product.shouldShowGroupInfo) {
            binding.tvCheckoutOrderNumber.text = itemView.context.getString(
                R.string.label_order_counter,
                product.orderNumber
            )
            binding.tvCheckoutOrderNumber.isVisible = true
            if (product.groupInfoBadgeUrl.isNotEmpty()) {
                binding.ivCheckoutOrderBadge.setImageUrl(product.groupInfoBadgeUrl)
                if (product.uiGroupType == GroupShop.UI_GROUP_TYPE_NORMAL) {
                    binding.ivCheckoutOrderBadge.contentDescription = itemView.context.getString(
                        com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type,
                        product.groupInfoName.lowercase(
                            Locale.getDefault()
                        )
                    )
                }
                binding.ivCheckoutOrderBadge.isVisible = true
            } else {
                binding.ivCheckoutOrderBadge.isVisible = false
            }
            binding.tvCheckoutOrderName.text = product.groupInfoName.getHtmlFormat()
            binding.tvCheckoutOrderName.isVisible = true
            val freeShippingBadgeUrl = product.freeShippingBadgeUrl
            if (freeShippingBadgeUrl.isNotBlank()) {
                binding.imgFreeShipping.setImageUrl(freeShippingBadgeUrl)
                if (product.isFreeShippingPlus) {
                    binding.imgFreeShipping.contentDescription =
                        itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_plus)
                } else {
                    binding.imgFreeShipping.contentDescription =
                        itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo)
                }
                binding.imgFreeShipping.isVisible = true
                if (!product.hasSeenFreeShippingBadge && product.isFreeShippingPlus) {
                    product.hasSeenFreeShippingBadge = true
                    listener.onViewFreeShippingPlusBadge()
                }
            } else {
                binding.imgFreeShipping.isVisible = false
            }
        } else {
            binding.tvCheckoutOrderNumber.isVisible = false
            binding.ivCheckoutOrderBadge.isVisible = false
            binding.tvCheckoutOrderName.isVisible = false
            binding.imgFreeShipping.isVisible = false
        }
    }

    private fun renderShopInfo(product: CheckoutProductModel) {
        if (product.shouldShowShopInfo) {
            if (product.shopTypeInfoData.shopBadge.isNotEmpty()) {
                binding.ivCheckoutShopBadge.setImageUrl(product.shopTypeInfoData.shopBadge)
                binding.ivCheckoutShopBadge.visible()
                binding.ivCheckoutShopBadge.contentDescription = itemView.context.getString(
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type,
                    product.shopTypeInfoData.title.lowercase(
                        Locale.getDefault()
                    )
                )
            } else {
                binding.ivCheckoutShopBadge.gone()
            }
            binding.tvCheckoutShopName.text = product.shopName.getHtmlFormat()
            binding.tvCheckoutShopName.isVisible = true
        } else {
            binding.ivCheckoutShopBadge.isVisible = false
            binding.tvCheckoutShopName.isVisible = false
        }
    }

//    private fun renderAddOnProduct(product: CheckoutProductModel) {
//        val addOnProduct = product.addOnProduct
//        if (addOnProduct.listAddOnProductData.isEmpty()) {
//            productBinding.tvCheckoutAddons.gone()
//            productBinding.tvCheckoutAddonsSeeAll.gone()
//            productBinding.llAddonProductItems.gone()
//        } else {
//            productBinding.llAddonProductItems.removeAllViews()
//
////            binding.itemShipmentAddonProduct.apply {
//                tvTitleAddonProduct.text = cartItemModel.addOnProduct.title
//                if (addOnProduct.bottomsheet.isShown) {
//                    productBinding.tvCheckoutAddonsSeeAll.apply {
//                        visible()
//                        setOnClickListener {
//                            addOnProduct.listAddOnProductData.forEach { addOnItem ->
//                                if (addOnItem.addOnDataStatus == 1) {
//                                    listSelectedAddOnId.add(addOnItem.addOnDataId)
//                                }
//                            }
//                            listener?.onClickSeeAllAddOnProductService(cartItemModel, listSelectedAddOnId)
//                        }
//                    }
//                } else {
//                    tvSeeAllAddonProduct.gone()
//                }
////            }
//            cartItemModel.addOnProduct.listAddOnProductData.forEach { addon ->
//                if (addon.addOnDataName.isEmpty()) {
//                    binding.itemShipmentAddonProduct.llAddonProductItems.visibility = View.GONE
//                } else {
//                    binding.itemShipmentAddonProduct.llAddonProductItems.visible()
//                    val addOnView = ItemShipmentAddonProductItemBinding.inflate(layoutInflater, null, false)
//                    val addOnName = addOnView.tvShipmentAddOnName
//                    addOnName.text = addon.addOnDataName
//                    val addOnPrice = addOnView.tvShipmentAddOnPrice
//                    addOnPrice.text = CurrencyFormatUtil
//                        .convertPriceValueToIdrFormat(addon.addOnDataPrice.toLong(), false)
//                        .removeDecimalSuffix()
//                    addOnView.apply {
//                        cbAddonItem.isChecked = (addon.addOnDataStatus == AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK)
//                        cbAddonItem.setOnCheckedChangeListener { compoundButton, isChecked ->
//                            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
//                                listener?.onCheckboxAddonProductListener(isChecked, addon, cartItemModel, bindingAdapterPosition)
//                            }
//                        }
//                        icProductAddonInfo.setOnClickListener {
//                            listener?.onClickAddonProductInfoIcon(addon.addOnDataInfoLink)
//                        }
//                    }
//                    binding.itemShipmentAddonProduct.llAddonProductItems.addView(addOnView.root)
//                }
//            }
//        }
//    }

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
