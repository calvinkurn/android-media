package com.tokopedia.checkout.view.viewholder

import android.graphics.Paint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentProductBinding
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.utils.WeightFormatterUtil
import com.tokopedia.checkout.view.uimodel.ShipmentGroupProductModel
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel
import com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.SMALL
import com.tokopedia.utils.currency.CurrencyFormatUtil

class ShipmentCartItemViewHolder(
    itemView: View,
    private val listener: Listener? = null
) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmField
        val LAYOUT = R.layout.item_shipment_product

        private const val VIEW_ALPHA_ENABLED = 1.0f
        private const val VIEW_ALPHA_DISABLED = 0.5f
    }

    private val binding: ItemShipmentProductBinding = ItemShipmentProductBinding.bind(itemView)

    fun bind(
        shipmentGroupProduct: ShipmentGroupProductModel
    ) {
        val cartItem = shipmentGroupProduct.shipmentCartItem.cartItemModels[shipmentGroupProduct.cartItemPosition]
        val isFirstItem = shipmentGroupProduct.cartItemPosition == 0

        renderError(shipmentGroupProduct)
        renderItem(cartItem)
        renderProductPrice(cartItem)
        renderNotesToSeller(cartItem)
        renderPurchaseProtection(cartItem)
        renderProductTicker(cartItem)
        renderProductProperties(cartItem)
        renderBundlingInfo(cartItem, isFirstItem)
        renderAddOnProductLevel(cartItem, shipmentGroupProduct.shipmentCartItem.addOnWordingModel!!)
    }

    @Deprecated("")
    fun bind(
        cartItem: CartItemModel,
        addOnWordingModel: AddOnWordingModel,
        isFirstItem: Boolean
    ) {
        renderItem(cartItem)
        renderProductPrice(cartItem)
        renderNotesToSeller(cartItem)
        renderPurchaseProtection(cartItem)
        renderProductTicker(cartItem)
        renderProductProperties(cartItem)
        renderBundlingInfo(cartItem, isFirstItem)
        renderAddOnProductLevel(cartItem, addOnWordingModel)
    }

    private fun renderError(shipmentGroupProduct: ShipmentGroupProductModel) {
        if (shipmentGroupProduct.shipmentCartItem.isError) {
            binding.llItemProductContainer.alpha = VIEW_ALPHA_DISABLED
            binding.checkboxPpp.isEnabled = false
            binding.iconTooltip.isClickable = false
        } else {
            binding.llItemProductContainer.alpha = VIEW_ALPHA_ENABLED
            binding.checkboxPpp.isEnabled = true
            binding.iconTooltip.isClickable = true
        }
    }

    private fun renderItem(cartItem: CartItemModel) {
        if (cartItem.isError) {
            showShipmentWarning(cartItem)
        } else {
            hideShipmentWarning()
        }
        binding.ivProductImage.setImageUrl(cartItem.imageUrl)
        binding.tvProductName.text = cartItem.name
        binding.tvItemCountAndWeight.text = String.format(
            binding.tvItemCountAndWeight.context.getString(R.string.iotem_count_and_weight_format),
            cartItem.quantity.toString(),
            WeightFormatterUtil.getFormattedWeight(cartItem.weight, cartItem.quantity)
        )
        if (!TextUtils.isEmpty(cartItem.variant)) {
            binding.textVariant.text = cartItem.variant
            binding.textVariant.visible()
        } else {
            binding.textVariant.gone()
        }
    }

    private fun renderProductProperties(cartItemModel: CartItemModel) {
        val productInformationList = cartItemModel.productInformation
        if (productInformationList.isNotEmpty()) {
            for (i in productInformationList.indices) {
                val productInfo = Typography(itemView.context)
                productInfo.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                    )
                )
                productInfo.setType(SMALL)
                if (binding.layoutProductInfo.childCount > 0) {
                    productInfo.text = ", " + productInformationList[i]
                } else {
                    productInfo.text = productInformationList[i]
                }
                binding.layoutProductInfo.addView(productInfo)
            }
            binding.layoutProductInfo.visibility = View.VISIBLE
        } else {
            binding.checkoutTickerProductError.visibility = View.GONE
        }
        renderEthicalDrugsProperty(cartItemModel)
    }

    private fun renderEthicalDrugsProperty(cartItemModel: CartItemModel) {
        if (cartItemModel.ethicalDrugDataModel.needPrescription) {
            val ethicalDrugView: View = createProductInfoTextWithIcon(cartItemModel)
            if (binding.checkoutTickerProductError.childCount > 0) {
                ethicalDrugView.setPadding(
                    4.dpToPx(ethicalDrugView.resources.displayMetrics),
                    0,
                    0,
                    0
                )
            }
            binding.checkoutTickerProductError.addView(ethicalDrugView)
            binding.checkoutTickerProductError.visibility = View.VISIBLE
        }
    }

    private fun createProductInfoTextWithIcon(cartItemModel: CartItemModel): LinearLayout {
        val propertyLayoutWithIcon = LinearLayout(itemView.context)
        propertyLayoutWithIcon.orientation = LinearLayout.HORIZONTAL
        val propertiesBinding = LayoutInflater.from(itemView.context)
            .inflate(
                com.tokopedia.purchase_platform.common.R.layout.item_product_info_add_on,
                null,
                false
            )
        val propertyIcon =
            propertiesBinding.findViewById<ImageUnify>(com.tokopedia.purchase_platform.common.R.id.pp_iv_product_info_add_on)
        if (propertyIcon != null && !TextUtils.isEmpty(cartItemModel.ethicalDrugDataModel.iconUrl)) {
            ImageHandler.loadImageWithoutPlaceholderAndError(
                propertyIcon,
                cartItemModel.ethicalDrugDataModel.iconUrl
            )
        }
        val propertyText =
            propertiesBinding.findViewById<Typography>(com.tokopedia.purchase_platform.common.R.id.pp_label_product_info_add_on)
        if (propertyText != null && !TextUtils.isEmpty(cartItemModel.ethicalDrugDataModel.text)) {
            propertyText.text = cartItemModel.ethicalDrugDataModel.text
        }
        propertyLayoutWithIcon.addView(propertiesBinding)
        return propertyLayoutWithIcon
    }

    private fun renderProductPrice(cartItem: CartItemModel) {
        binding.tvProductPrice.text = CurrencyFormatUtil
            .convertPriceValueToIdrFormat(cartItem.price.toLong(), false)
            .removeDecimalSuffix()
        val dp4 =
            binding.tvProductPrice.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_4)
        if (cartItem.originalPrice > 0) {
            binding.tvProductPrice.setPadding(0, dp4, 0, 0)
            binding.tvProductOriginalPrice.setPadding(0, dp4, 0, 0)
            binding.tvProductOriginalPrice.text = CurrencyFormatUtil
                .convertPriceValueToIdrFormat(cartItem.originalPrice.toLong(), false)
                .removeDecimalSuffix()
            binding.tvProductOriginalPrice.paintFlags =
                binding.tvProductOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvProductOriginalPrice.visibility = View.VISIBLE
        } else {
            binding.tvProductPrice.setPadding(0, dp4, 0, 0)
            binding.tvProductOriginalPrice.visibility = View.GONE
        }
    }

    private fun renderNotesToSeller(cartItem: CartItemModel) {
        if (!TextUtils.isEmpty(cartItem.noteToSeller)) {
            binding.tvOptionalNoteToSeller.text = cartItem.noteToSeller
            binding.tvOptionalNoteToSeller.visibility = View.VISIBLE
        } else {
            binding.tvOptionalNoteToSeller.visibility = View.GONE
        }
    }

    private fun renderPurchaseProtection(cartItem: CartItemModel) {
        binding.rlayoutPurchaseProtection.visibility =
            if (cartItem.isProtectionAvailable && !cartItem.isError) {
                View.VISIBLE
            } else {
                View.GONE
            }
        if (cartItem.isProtectionAvailable && !cartItem.isError) {
            binding.iconTooltip.setOnClickListener {
                listener?.onClickPurchaseProtectionTooltip(
                    cartItem
                )
            }
            binding.textLinkText.text = cartItem.protectionTitle
            binding.textProtectionDesc.text = cartItem.protectionSubTitle
            binding.textItemPerProduct.text = CurrencyFormatUtil
                .convertPriceValueToIdrFormat(cartItem.protectionPricePerProduct.toLong(), false)
                .removeDecimalSuffix()
            if (cartItem.isProtectionCheckboxDisabled) {
                binding.checkboxPpp.isEnabled = false
                binding.checkboxPpp.isChecked = true
                binding.checkboxPpp.skipAnimation()
            } else {
                binding.checkboxPpp.isEnabled = true
                binding.checkboxPpp.isChecked = cartItem.isProtectionOptIn
                binding.checkboxPpp.skipAnimation()
                binding.checkboxPpp.setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean ->
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        listener?.onCheckPurchaseProtection(bindingAdapterPosition, checked)
                    }
                }
            }
        }
    }

    private fun renderProductTicker(cartItemModel: CartItemModel) {
        if (cartItemModel.isShowTicker && !TextUtils.isEmpty(cartItemModel.tickerMessage)) {
            binding.productTicker.visibility = View.VISIBLE
            binding.productTicker.setTextDescription(cartItemModel.tickerMessage)
        } else {
            binding.productTicker.visibility = View.GONE
        }
    }

    private fun showShipmentWarning(cartItemModel: CartItemModel) {
        if (!TextUtils.isEmpty(cartItemModel.errorMessage)) {
            if (!TextUtils.isEmpty(cartItemModel.errorMessageDescription)) {
                binding.checkoutTickerProductError.tickerTitle = cartItemModel.errorMessage
                binding.checkoutTickerProductError.setTextDescription(cartItemModel.errorMessageDescription)
            } else {
                binding.checkoutTickerProductError.setTextDescription(cartItemModel.errorMessage)
            }

            if (cartItemModel.isBundlingItem) {
                if (cartItemModel.bundlingItemPosition == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                    binding.checkoutTickerProductError.visible()
                    binding.checkoutTickerProductError.post {
                        binding.checkoutTickerProductError.requestLayout()
                    }
                } else {
                    binding.checkoutTickerProductError.gone()
                }
            } else {
                binding.checkoutTickerProductError.visible()
                binding.checkoutTickerProductError.post {
                    binding.checkoutTickerProductError.requestLayout()
                }
            }
        } else {
            binding.checkoutTickerProductError.gone()
        }

        if (!cartItemModel.isShopError) {
            disableItemView()
        }
    }

    private fun hideShipmentWarning() {
        binding.checkoutTickerProductError.gone()
        enableItemView()
    }

    private fun enableItemView() {
        binding.productBundlingInfo.alpha = VIEW_ALPHA_ENABLED
        binding.llFrameItemProductContainer.alpha = VIEW_ALPHA_ENABLED
    }

    private fun disableItemView() {
        binding.productBundlingInfo.alpha = VIEW_ALPHA_DISABLED
        binding.llFrameItemProductContainer.alpha = VIEW_ALPHA_DISABLED
    }

    private fun renderBundlingInfo(cartItemModel: CartItemModel, isFirstItem: Boolean) {
        val ivProductImageLayoutParams = binding.ivProductImage.layoutParams as MarginLayoutParams
        val tvOptionalNoteToSellerLayoutParams =
            binding.tvOptionalNoteToSeller.layoutParams as MarginLayoutParams
        val productContainerLayoutParams =
            binding.llFrameItemProductContainer.layoutParams as MarginLayoutParams
        val productInfoLayoutParams = binding.rlProductInfo.layoutParams as MarginLayoutParams
        val bottomMargin =
            itemView.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8)
        if (cartItemModel.isBundlingItem) {
            if (!TextUtils.isEmpty(cartItemModel.bundleIconUrl)) {
                ImageHandler.loadImage2(
                    binding.imageBundle,
                    cartItemModel.bundleIconUrl,
                    com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder
                )
            }

            ivProductImageLayoutParams.leftMargin =
                itemView.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_14)
            tvOptionalNoteToSellerLayoutParams.leftMargin =
                itemView.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_14)
            binding.vBundlingProductSeparator.visibility = View.VISIBLE
            val productImageLayoutParams = binding.ivProductImage.layoutParams as MarginLayoutParams
            val productNameLayoutParams = binding.tvProductName.layoutParams as MarginLayoutParams
            val productMarginTop =
                itemView.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_12)
            if (cartItemModel.bundlingItemPosition == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                binding.productBundlingInfo.visibility = View.VISIBLE
                productImageLayoutParams.topMargin = 0
                productNameLayoutParams.topMargin = 0
                binding.vSeparatorMultipleProductSameStore.invisible()
            } else {
                binding.productBundlingInfo.visibility = View.GONE
                productImageLayoutParams.topMargin = productMarginTop
                productNameLayoutParams.topMargin = productMarginTop
                binding.vSeparatorMultipleProductSameStore.gone()
            }
            binding.textBundleTitle.text = cartItemModel.bundleTitle
            binding.textBundlePrice.text =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(cartItemModel.bundlePrice, false)
                    .removeDecimalSuffix()
            binding.textBundleSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                cartItemModel.bundleOriginalPrice,
                false
            ).removeDecimalSuffix()
            binding.textBundleSlashPrice.paintFlags =
                binding.textBundleSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            productContainerLayoutParams.bottomMargin = 0
            productInfoLayoutParams.bottomMargin = 0
        } else {
            ivProductImageLayoutParams.leftMargin = 0
            tvOptionalNoteToSellerLayoutParams.leftMargin = 0
            binding.vBundlingProductSeparator.visibility = View.GONE
            binding.productBundlingInfo.visibility = View.GONE
            if (isFirstItem) {
                binding.vSeparatorMultipleProductSameStore.gone()
            } else {
                binding.vSeparatorMultipleProductSameStore.show()
            }
            productContainerLayoutParams.bottomMargin = bottomMargin
            productInfoLayoutParams.bottomMargin = bottomMargin
        }
    }

    private fun renderAddOnProductLevel(
        cartItemModel: CartItemModel,
        addOnWordingModel: AddOnWordingModel
    ) {
        val addOns = cartItemModel.addOnProductLevelModel
        if (addOns.status == 0) {
            binding.llGiftingAddonProductLevel.visibility = View.GONE
        } else {
            with(binding.itemShipmentGiftingAddonProductLevel) {
                if (addOns.status == 1) {
                    buttonGiftingAddonProductLevel.state = ButtonGiftingAddOnView.State.ACTIVE
                } else if (addOns.status == 2) {
                    buttonGiftingAddonProductLevel.state = ButtonGiftingAddOnView.State.INACTIVE
                }
                buttonGiftingAddonProductLevel.title = addOns.addOnsButtonModel.title
                buttonGiftingAddonProductLevel.desc = addOns.addOnsButtonModel.description
                buttonGiftingAddonProductLevel.urlLeftIcon = addOns.addOnsButtonModel.leftIconUrl
                buttonGiftingAddonProductLevel.urlRightIcon = addOns.addOnsButtonModel.rightIconUrl
                buttonGiftingAddonProductLevel.setOnClickListener {
                    listener?.onClickAddOnProductLevel(
                        cartItemModel,
                        addOnWordingModel
                    )
                }
            }
            binding.llGiftingAddonProductLevel.visibility = View.VISIBLE
            listener?.onImpressionAddOnProductLevel(cartItemModel.productId.toString())
        }
    }

    interface Listener {

        fun onCheckPurchaseProtection(position: Int, isChecked: Boolean)

        fun onClickPurchaseProtectionTooltip(cartItem: CartItemModel)

        fun onClickAddOnProductLevel(cartItem: CartItemModel, addOnWording: AddOnWordingModel)

        fun onImpressionAddOnProductLevel(productId: String)
    }
}
