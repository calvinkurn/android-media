package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutProductBinding
import com.tokopedia.checkout.databinding.LayoutCheckoutProductBinding
import com.tokopedia.checkout.databinding.LayoutCheckoutProductBundleBinding
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.databinding.ItemAddOnProductBinding
import com.tokopedia.purchase_platform.common.utils.getHtmlFormat
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class CheckoutProductViewHolder(
    private val binding: ItemCheckoutProductBinding,
    private val listener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    private val productBinding: LayoutCheckoutProductBinding =
        LayoutCheckoutProductBinding.bind(binding.root)

    private val bundleBinding: LayoutCheckoutProductBundleBinding =
        LayoutCheckoutProductBundleBinding.bind(binding.root)

    private var delayChangeCheckboxAddOnState: Job? = null

    fun bind(product: CheckoutProductModel) {
        renderErrorAndWarningGroup(product)
        if (product.isBundlingItem) {
            renderBundleItem(product)
        } else {
            renderProductItem(product)
        }
        renderErrorProduct(product)
    }

    @SuppressLint("SetTextI18n")
    private fun renderBundleItem(product: CheckoutProductModel) {
        hideProductViews()
        renderGroupInfo(product)
        renderShopInfo(product)

        bundleBinding.ivProductBundleImage.setImageUrl(product.imageUrl)
        bundleBinding.tvProductBundleName.text = "${product.quantity} x ${product.name}"
        if (product.ethicalDrugDataModel.needPrescription && product.ethicalDrugDataModel.iconUrl.isNotEmpty()) {
            product.ethicalDrugDataModel.iconUrl.getBitmapImageUrl(bundleBinding.root.context) {
                try {
                    bundleBinding.tvProductBundleName.text = SpannableStringBuilder("  ${product.quantity} x ${product.name}").apply {
                        setSpan(ImageSpan(bundleBinding.root.context, it, DynamicDrawableSpan.ALIGN_CENTER), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }
        if (product.variant.isNotBlank()) {
            bundleBinding.textVariantBundle.text = product.variant
            bundleBinding.textVariantBundle.isVisible = true
        } else {
            bundleBinding.textVariantBundle.isVisible = false
        }

        if (product.bundlingItemPosition == 1) {
            bundleBinding.tvCheckoutBundleName.text = product.bundleTitle
            val priceInRp =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(product.bundlePrice, false)
                    .removeDecimalSuffix()
            val qty = product.bundleQuantity
            bundleBinding.tvCheckoutBundlePrice.text = "$qty x $priceInRp"
            bundleBinding.tvCheckoutBundle.isVisible = true
            bundleBinding.tvCheckoutBundleSeparator.isVisible = true
            bundleBinding.tvCheckoutBundleName.isVisible = true
            bundleBinding.tvCheckoutBundlePrice.isVisible = true
            (bundleBinding.vBundlingProductSeparator.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(itemView.resources.displayMetrics)
        } else {
            bundleBinding.tvCheckoutBundle.isVisible = false
            bundleBinding.tvCheckoutBundleSeparator.isVisible = false
            bundleBinding.tvCheckoutBundleName.isVisible = false
            bundleBinding.tvCheckoutBundlePrice.isVisible = false
            (bundleBinding.vBundlingProductSeparator.layoutParams as? MarginLayoutParams)?.topMargin = 0
        }

        if (product.noteToSeller.isNotEmpty()) {
            bundleBinding.tvProductBundleNote.text = "\"${product.noteToSeller}\""
            bundleBinding.tvProductBundleNote.isVisible = true
        } else {
            bundleBinding.tvProductBundleNote.isVisible = false
        }

        renderAddOnProductBundle(product)
        renderAddOnGiftingProductLevel(product)
    }

    private fun hideProductViews() {
        productBinding.apply {
            ivProductImage.isVisible = false
            tvProductName.isVisible = false
            textVariant.isVisible = false
            tvProductPrice.isVisible = false
            tvOptionalNoteToSeller.isVisible = false
            tvCheckoutAddons.isVisible = false
            tvCheckoutAddonsSeeAll.isVisible = false
            llAddonProductItems.isVisible = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderProductItem(product: CheckoutProductModel) {
        hideBundleViews()
        renderGroupInfo(product)
        renderShopInfo(product)

        productBinding.ivProductImage.setImageUrl(product.imageUrl)
        productBinding.tvProductName.text = product.name
        if (product.ethicalDrugDataModel.needPrescription && product.ethicalDrugDataModel.iconUrl.isNotEmpty()) {
            product.ethicalDrugDataModel.iconUrl.getBitmapImageUrl(productBinding.root.context) {
                try {
                    productBinding.tvProductName.text = SpannableStringBuilder("  ${product.name}").apply {
                        setSpan(ImageSpan(productBinding.root.context, it, DynamicDrawableSpan.ALIGN_CENTER), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }
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
            productBinding.tvOptionalNoteToSeller.text = "\"${product.noteToSeller}\""
            productBinding.tvOptionalNoteToSeller.isVisible = true
        } else {
            productBinding.tvOptionalNoteToSeller.isVisible = false
        }

        renderAddOnProduct(product)
        renderAddOnGiftingProductLevel(product)
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
            binding.bgCheckoutSupergraphicOrder.isVisible = true
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
            binding.bgCheckoutSupergraphicOrder.isVisible = false
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
            binding.vDividerShop.isVisible = product.cartItemPosition > 0
        } else {
            binding.ivCheckoutShopBadge.isVisible = false
            binding.tvCheckoutShopName.isVisible = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderAddOnProduct(product: CheckoutProductModel) {
        val addOnProduct = product.addOnProduct
        if (addOnProduct.listAddOnProductData.isEmpty()) {
            productBinding.tvCheckoutAddons.gone()
            productBinding.tvCheckoutAddonsSeeAll.gone()
            productBinding.llAddonProductItems.gone()
        } else {
            productBinding.llAddonProductItems.removeAllViews()
            if (addOnProduct.bottomsheet.isShown) {
                productBinding.tvCheckoutAddons.text = addOnProduct.title
                productBinding.tvCheckoutAddonsSeeAll.apply {
                    visible()
                    setOnClickListener {
                        listener.onClickSeeAllAddOnProductService(product)
                    }
                }
            } else {
                productBinding.tvCheckoutAddons.gone()
                productBinding.tvCheckoutAddonsSeeAll.gone()
            }
            val layoutInflater = LayoutInflater.from(itemView.context)
            addOnProduct.listAddOnProductData.forEach { addon ->
                if (addon.name.isNotEmpty()) {
                    val addOnView =
                        ItemAddOnProductBinding.inflate(layoutInflater, productBinding.llAddonProductItems, false)
                    addOnView.apply {
//                        icProductAddon.setImageUrl(addon)
                        tvProductAddonName.text = SpannableString(addon.name).apply {
                            setSpan(UnderlineSpan(), 0, addon.name.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        }
                        tvProductAddonPrice.text = " (${CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(addon.price, false)
                            .removeDecimalSuffix()})"
                        cbProductAddon.setOnCheckedChangeListener { _, _ -> }
                        when (addon.status) {
                            AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK -> {
                                cbProductAddon.isChecked = true
                                cbProductAddon.isEnabled = true
                            }
                            AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY -> {
                                cbProductAddon.isChecked = true
                                cbProductAddon.isEnabled = false
                            }
                            else -> {
                                cbProductAddon.isChecked = false
                                cbProductAddon.isEnabled = true
                            }
                        }
                        cbProductAddon.skipAnimation()
                        cbProductAddon.setOnCheckedChangeListener { _, isChecked ->
                            delayChangeCheckboxAddOnState?.cancel()
                            delayChangeCheckboxAddOnState = GlobalScope.launch(Dispatchers.Main) {
                                delay(DEBOUNCE_TIME_ADDON)
                                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                                    listener.onCheckboxAddonProductListener(
                                        isChecked,
                                        addon,
                                        product,
                                        bindingAdapterPosition
                                    )
                                }
                            }
                        }
                        tvProductAddonName.setOnClickListener {
                            listener.onClickAddonProductInfoIcon(addon.infoLink)
                        }
                    }
                    productBinding.llAddonProductItems.addView(addOnView.root)
                    productBinding.llAddonProductItems.visible()
                    listener.onImpressionAddOnProductService(
                        addon.type,
                        product.productId.toString()
                    )
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderAddOnProductBundle(product: CheckoutProductModel) {
        val addOnProduct = product.addOnProduct
        if (addOnProduct.listAddOnProductData.isEmpty()) {
            bundleBinding.tvCheckoutBundleAddons.gone()
            bundleBinding.tvCheckoutBundleAddonsSeeAll.gone()
            bundleBinding.llAddonProductBundleItems.gone()
        } else {
            bundleBinding.llAddonProductBundleItems.removeAllViews()
            if (addOnProduct.bottomsheet.isShown) {
                bundleBinding.tvCheckoutBundleAddons.text = addOnProduct.title
                bundleBinding.tvCheckoutBundleAddonsSeeAll.apply {
                    visible()
                    setOnClickListener {
                        listener.onClickSeeAllAddOnProductService(product)
                    }
                }
            } else {
                bundleBinding.tvCheckoutBundleAddons.gone()
                bundleBinding.tvCheckoutBundleAddonsSeeAll.gone()
            }
            val layoutInflater = LayoutInflater.from(itemView.context)
            addOnProduct.listAddOnProductData.forEach { addon ->
                if (addon.name.isNotEmpty()) {
                    val addOnView =
                        ItemAddOnProductBinding.inflate(layoutInflater, bundleBinding.llAddonProductBundleItems, false)
                    addOnView.apply {
                        tvProductAddonName.text = addon.name
                        tvProductAddonPrice.text = " (${CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(addon.price, false)
                            .removeDecimalSuffix()})"
                        cbProductAddon.setOnCheckedChangeListener { _, _ -> }
                        when (addon.status) {
                            AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK -> {
                                cbProductAddon.isChecked = true
                                cbProductAddon.isEnabled = true
                            }
                            AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY -> {
                                cbProductAddon.isChecked = true
                                cbProductAddon.isEnabled = false
                            }
                            else -> {
                                cbProductAddon.isChecked = false
                                cbProductAddon.isEnabled = true
                            }
                        }
                        cbProductAddon.skipAnimation()
                        cbProductAddon.setOnCheckedChangeListener { _, isChecked ->
                            delayChangeCheckboxAddOnState?.cancel()
                            delayChangeCheckboxAddOnState = GlobalScope.launch(Dispatchers.Main) {
                                delay(DEBOUNCE_TIME_ADDON)
                                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                                    listener.onCheckboxAddonProductListener(
                                        isChecked,
                                        addon,
                                        product,
                                        bindingAdapterPosition
                                    )
                                }
                            }
                        }
                        tvProductAddonName.setOnClickListener {
                            listener.onClickAddonProductInfoIcon(addon.infoLink)
                        }
                    }
                    bundleBinding.llAddonProductBundleItems.addView(addOnView.root)
                    bundleBinding.llAddonProductBundleItems.visible()
                    listener.onImpressionAddOnProductService(
                        addon.type,
                        product.productId.toString()
                    )
                }
            }
        }
    }

    private fun renderAddOnGiftingProductLevel(
        product: CheckoutProductModel
    ) {
        val addOns = product.addOnGiftingProductLevelModel
        if (addOns.status == 0) {
            binding.buttonGiftingAddonProductLevel.visibility = View.GONE
        } else {
            if (addOns.status == 1) {
                if (addOns.addOnsDataItemModelList.isNotEmpty()) {
                    binding.buttonGiftingAddonProductLevel.showActive(
                        addOns.addOnsButtonModel.title,
                        addOns.addOnsButtonModel.description
                    )
                } else {
                    binding.buttonGiftingAddonProductLevel.showEmptyState(
                        addOns.addOnsButtonModel.title,
                        addOns.addOnsButtonModel.description.ifEmpty { "(opsional)" }
                    )
                }
            } else if (addOns.status == 2) {
                binding.buttonGiftingAddonProductLevel.showInactive(addOns.addOnsButtonModel.title, addOns.addOnsButtonModel.description)
            }
            binding.buttonGiftingAddonProductLevel.setOnClickListener {
                listener.onClickAddOnGiftingProductLevel(
                    product
                )
            }
            binding.buttonGiftingAddonProductLevel.visibility = View.VISIBLE
            listener.onImpressionAddOnGiftingProductLevel(product.productId.toString())
        }
    }

    private fun renderErrorAndWarningGroup(product: CheckoutProductModel) {
        if (product.shouldShowGroupInfo) {
            val order = listener.getOrderByCartStringGroup(product.cartStringGroup)
            if (order != null) {
                renderGroupError(order)
                renderWarningGroup(order)
                renderCustomError(order)
                return
            }
        }
        binding.checkoutTickerShopError.isVisible = false
    }

    private fun renderGroupError(order: CheckoutOrderModel) {
        with(binding) {
            if (order.isError) {
                val errorTitle = order.errorTitle
                val errorDescription = order.errorDescription
                if (errorTitle.isNotEmpty()) {
                    if (errorDescription.isNotEmpty()) {
                        checkoutTickerShopError.tickerTitle = errorTitle
                        checkoutTickerShopError.setTextDescription(errorDescription)
                        checkoutTickerShopError.setDescriptionClickEvent(object : TickerCallback {
                            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                // no-op
                            }

                            override fun onDismiss() {
                                // no-op
                            }
                        })
                    } else {
                        if (order.isCustomEpharmacyError) {
                            checkoutTickerShopError.setHtmlDescription(
                                "$errorTitle ${itemView.context.getString(R.string.checkout_ticker_lihat_cta_suffix)}"
                            )
                            checkoutTickerShopError.setDescriptionClickEvent(object : TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                    listener.onClickLihatOnTickerOrderError(
                                        order.shopId.toString(),
                                        errorTitle,
                                        order,
                                        bindingAdapterPosition
                                    )
                                }

                                override fun onDismiss() {
                                    // no op
                                }
                            })
                        } else {
                            checkoutTickerShopError.setTextDescription(errorTitle)
                            checkoutTickerShopError.setDescriptionClickEvent(object : TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                    // no op
                                }

                                override fun onDismiss() {
                                    // no op
                                }
                            })
                        }
                    }
                    checkoutTickerShopError.tickerType = Ticker.TYPE_ERROR
                    checkoutTickerShopError.tickerShape = Ticker.SHAPE_LOOSE
                    checkoutTickerShopError.closeButtonVisibility = View.GONE
                    checkoutTickerShopError.visible()
//                    layoutError.visible()
                } else {
                    checkoutTickerShopError.gone()
//                    layoutError.gone()
                }
            } else {
                checkoutTickerShopError.gone()
//                layoutError.gone()
            }
//            layoutWarning.gone()
        }
    }

    private fun renderWarningGroup(order: CheckoutOrderModel) {
        with(binding) {
            if (!order.isError && order.shopTicker.isNotEmpty()) {
                checkoutTickerShopError.tickerTitle =
                    order.shopTickerTitle
                checkoutTickerShopError.setHtmlDescription(order.shopTicker)
                checkoutTickerShopError.visible()
                checkoutTickerShopError.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        // no-op
                    }

                    override fun onDismiss() {
                        order.shopTicker = ""
                        checkoutTickerShopError.gone()
                    }
                })
            } else {
                checkoutTickerShopError.gone()
            }
        }
    }

    private fun renderCustomError(order: CheckoutOrderModel) {
        with(binding) {
            if ((
                    !order.isError && order.isHasUnblockingError &&
                        order.unblockingErrorMessage.isNotEmpty()
                    ) &&
                order.firstProductErrorIndex > -1
            ) {
                val errorMessage = order.unblockingErrorMessage
                checkoutTickerShopError.setHtmlDescription(errorMessage + " " + itemView.context.getString(R.string.checkout_ticker_lihat_cta_suffix))
                checkoutTickerShopError.setDescriptionClickEvent(object : TickerCallback {

                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        listener.onClickLihatOnTickerOrderError(
                            order.shopId.toString(),
                            errorMessage,
                            order,
                            bindingAdapterPosition
                        )
                    }

                    override fun onDismiss() {
                        // no-op
                    }
                })
                checkoutTickerShopError.tickerType = Ticker.TYPE_ERROR
                checkoutTickerShopError.tickerShape = Ticker.SHAPE_LOOSE
                checkoutTickerShopError.closeButtonVisibility = View.GONE
                checkoutTickerShopError.visibility = View.VISIBLE
            }
        }
    }

    private fun renderErrorProduct(product: CheckoutProductModel) {
        if (product.isError || product.isShopError) {
            binding.frameCheckoutProductContainer.alpha = VIEW_ALPHA_DISABLED
        } else {
            binding.frameCheckoutProductContainer.alpha = VIEW_ALPHA_ENABLED
        }
        renderErrorProductTicker(product)
    }

    private fun renderErrorProductTicker(cartItemModel: CheckoutProductModel) {
        if (cartItemModel.errorMessage.isNotEmpty()) {
            if (cartItemModel.errorMessageDescription.isNotEmpty()) {
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
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_product

        private const val VIEW_ALPHA_ENABLED = 1.0f
        private const val VIEW_ALPHA_DISABLED = 0.5f
        private const val DEBOUNCE_TIME_ADDON = 500L
    }
}
