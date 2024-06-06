package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.color
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutProductBinding
import com.tokopedia.checkout.databinding.LayoutCheckoutProductBinding
import com.tokopedia.checkout.databinding.LayoutCheckoutProductBmgmBinding
import com.tokopedia.checkout.databinding.LayoutCheckoutProductBundleBinding
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.revamp.utils.CheckoutBmgmMapper
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.nest.components.quantityeditor.QtyField
import com.tokopedia.nest.components.quantityeditor.QtyState
import com.tokopedia.nest.components.quantityeditor.view.QuantityEditorView
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.databinding.ItemAddOnProductBinding
import com.tokopedia.purchase_platform.common.databinding.ItemAddOnProductRevampBinding
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import com.tokopedia.purchase_platform.common.utils.getHtmlFormat
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CheckoutProductViewHolder(
    private val binding: ItemCheckoutProductBinding,
    private val listener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    private val productBinding: LayoutCheckoutProductBinding =
        LayoutCheckoutProductBinding.bind(binding.root)

    private val bundleBinding: LayoutCheckoutProductBundleBinding =
        LayoutCheckoutProductBundleBinding.bind(binding.root)

    private val bmgmBinding: LayoutCheckoutProductBmgmBinding =
        LayoutCheckoutProductBmgmBinding.bind(binding.root)

    private var delayChangeCheckboxAddOnState: Job? = null

    init {
        productBinding.qtyEditorProduct.apply {
            isExpand = true
            expandState.value = true
            enableManualInput.value = true
            configState.value = configState.value.copy(
                qtyField = QtyField(cursorColor = unifyprinciplesR.color.Unify_NN1000)
            )
        }
        bmgmBinding.qtyEditorProductBmgm.apply {
            isExpand = true
            expandState.value = true
            enableManualInput.value = true
            configState.value = configState.value.copy(
                qtyField = QtyField(cursorColor = unifyprinciplesR.color.Unify_NN1000)
            )
        }
        bundleBinding.qtyEditorProductBundle.apply {
            isExpand = true
            expandState.value = true
            enableManualInput.value = true
            configState.value = configState.value.copy(
                qtyField = QtyField(cursorColor = unifyprinciplesR.color.Unify_NN1000)
            )
        }
    }

    fun bind(product: CheckoutProductModel) {
        renderErrorAndWarningGroup(product)
        if (product.isBundlingItem) {
            renderBundleItem(product)
            setNoteAnimationResource(bundleBinding.buttonChangeNoteLottieBundle)
        } else if (product.isBMGMItem) {
            renderBMGMItem(product)
            setNoteAnimationResource(bmgmBinding.buttonChangeNoteLottieBmgm)
        } else {
            renderProductItem(product)
            setNoteAnimationResource(productBinding.buttonChangeNoteLottie)
        }
        renderErrorProduct(product)
    }

    @SuppressLint("SetTextI18n")
    private fun renderBundleItem(product: CheckoutProductModel) {
        hideProductViews()
        hideBMGMViews()
        renderGroupInfo(product)
        renderShopInfo(product)

        bundleBinding.ivProductImageBundle.setImageUrl(product.imageUrl)
        bundleBinding.ivProductImageBundleFrame.isVisible = true
        bundleBinding.ivProductImageBundle.isVisible = true
        bundleBinding.tvProductNameBundle.text = "${product.quantity} x ${product.name}"
        bundleBinding.tvProductNameBundle.isVisible = true
        if (product.ethicalDrugDataModel.needPrescription && product.ethicalDrugDataModel.iconUrl.isNotEmpty()) {
            val px = EPHARMACY_ICON_SIZE.dpToPx(binding.root.context.resources.displayMetrics)
            product.ethicalDrugDataModel.iconUrl.getBitmapImageUrl(bundleBinding.root.context, {
                this.overrideSize(
                    Resize(
                        px,
                        px
                    )
                )
            }) {
                try {
                    bundleBinding.tvProductNameBundle.text =
                        SpannableStringBuilder("  ${product.quantity} x ${product.name}").apply {
                            setSpan(
                                ImageSpan(
                                    bundleBinding.root.context,
                                    it,
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) DynamicDrawableSpan.ALIGN_CENTER else DynamicDrawableSpan.ALIGN_BASELINE
                                ),
                                0,
                                1,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                            )
                        }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }
        if (product.variant.isNotBlank()) {
            bundleBinding.tvProductVariantBundle.text = product.variant
            bundleBinding.tvProductVariantBundle.isVisible = true
        } else {
            bundleBinding.tvProductVariantBundle.isVisible = false
        }

        if (product.bundlingItemPosition == 1) {
            bundleBinding.tvCheckoutBundleName.text = product.bundleTitle
            val priceInRp =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(product.bundlePrice, false)
                    .removeDecimalSuffix()
            val qty = product.bundleQuantity
            bundleBinding.tvCheckoutBundlePrice.text = "$qty x $priceInRp"
            bundleBinding.tvProductIdentifierBundle.isVisible = true
            bundleBinding.tvCheckoutBundleSeparator.isVisible = true
            bundleBinding.tvCheckoutBundleName.isVisible = true
            bundleBinding.tvCheckoutBundlePrice.isVisible = true
            (bundleBinding.vBundlingProductSeparator.layoutParams as? MarginLayoutParams)?.topMargin =
                8.dpToPx(itemView.resources.displayMetrics)
        } else {
            bundleBinding.tvProductIdentifierBundle.isVisible = false
            bundleBinding.tvCheckoutBundleSeparator.isVisible = false
            bundleBinding.tvCheckoutBundleName.isVisible = false
            bundleBinding.tvCheckoutBundlePrice.isVisible = false
            (bundleBinding.vBundlingProductSeparator.layoutParams as? MarginLayoutParams)?.topMargin =
                0
        }
        bundleBinding.vBundlingProductSeparator.isVisible = true

        if (product.noteToSeller.isNotEmpty()) {
            bundleBinding.tvProductNotesBundle.text = "\"${product.noteToSeller}\""
            bundleBinding.tvProductNotesBundle.isVisible = true
        } else {
            bundleBinding.tvProductNotesBundle.isVisible = false
        }

        renderAddOnProductBundle(product)
        renderAddOnGiftingProductBundle(product)
        renderNotes(product, bundleBinding.buttonChangeNoteBundle, bundleBinding.buttonChangeNoteLottieBundle)
        renderQuantity(product, bundleBinding.qtyEditorProductBundle, bundleBinding.labelQuantityErrorBundle)
    }

    private fun hideProductViews() {
        productBinding.apply {
            ivProductImage.isVisible = false
            ivProductImageFrame.isVisible = false
            tvProductName.isVisible = false
            tvProductVariant.isVisible = false
            tvProductPrice.isVisible = false
            buttonChangeNote.isVisible = false
            tvProductNotes.isVisible = false
            tvProductAddOnsSectionTitle.isVisible = false
            tvProductAddOnsSeeAll.isVisible = false
            llAddonProductItems.isVisible = false
            buttonGiftingAddonProduct.isVisible = false
            qtyEditorProduct.isVisible = false
            labelQuantityError.isVisible = false
            buttonChangeNote.isVisible = false
            buttonChangeNoteLottie.isVisible = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderProductItem(product: CheckoutProductModel) {
        hideBundleViews()
        hideBMGMViews()
        renderGroupInfo(product)
        renderShopInfo(product)

        productBinding.ivProductImage.setImageUrl(product.imageUrl)
        productBinding.ivProductImageFrame.isVisible = true
        productBinding.ivProductImage.isVisible = true
        productBinding.tvProductName.text = product.name
        productBinding.tvProductName.isVisible = true
        if (product.ethicalDrugDataModel.needPrescription && product.ethicalDrugDataModel.iconUrl.isNotEmpty()) {
            val px = EPHARMACY_ICON_SIZE.dpToPx(binding.root.context.resources.displayMetrics)
            product.ethicalDrugDataModel.iconUrl.getBitmapImageUrl(productBinding.root.context, {
                this.overrideSize(
                    Resize(
                        px,
                        px
                    )
                )
            }) {
                try {
                    productBinding.tvProductName.text =
                        SpannableStringBuilder("  ${product.name}").apply {
                            setSpan(
                                ImageSpan(
                                    productBinding.root.context,
                                    it,
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) DynamicDrawableSpan.ALIGN_CENTER else DynamicDrawableSpan.ALIGN_BASELINE
                                ),
                                0,
                                1,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                            )
                        }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }
        if (product.variant.isNotBlank()) {
            productBinding.tvProductVariant.text = product.variant
            productBinding.tvProductVariant.isVisible = true
        } else {
            productBinding.tvProductVariant.isVisible = false
        }

        val priceInRp =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(product.price, false)
                .removeDecimalSuffix()
        if (product.isCartTypeOcc) {
            productBinding.tvProductPrice.text = priceInRp
        } else {
            val qty = product.quantity
            productBinding.tvProductPrice.text = "$qty x $priceInRp"
        }
        productBinding.tvProductPrice.isVisible = true

        if (product.noteToSeller.isNotEmpty()) {
            productBinding.tvProductNotes.text = "\"${product.noteToSeller}\""
            productBinding.tvProductNotes.isVisible = true
        } else {
            productBinding.tvProductNotes.isVisible = false
        }

        renderNotes(product, productBinding.buttonChangeNote, productBinding.buttonChangeNoteLottie)
        renderQuantity(product, productBinding.qtyEditorProduct, productBinding.labelQuantityError)
        renderAddOnProduct(product)
        renderAddOnGiftingProduct(product)
    }

    private fun renderNotes(product: CheckoutProductModel, buttonChangeNote: ImageUnify, notesLottie: LottieAnimationView) {
        if (product.enableNoteEdit) {
            buttonChangeNote.show()
            if (product.shouldShowLottieNotes) {
                notesLottie.visible()
                listener.onShowLottieNotes(
                    buttonChangeNote,
                    notesLottie,
                    bindingAdapterPosition
                )
            } else {
                notesLottie.gone()
            }
            buttonChangeNote.setOnClickListener {
                listener.onNoteClicked(product, bindingAdapterPosition)
            }
            if (product.noteToSeller.isNotBlank()) {
                renderNotesFilled(buttonChangeNote)
            } else {
                renderNotesEmpty(buttonChangeNote)
            }
        }
    }

    private fun renderQuantity(product: CheckoutProductModel, qtyEditorProduct: QuantityEditorView, labelQuantityError: Typography) {
        if (product.enableQtyEdit) {
            if (product.isError) {
                qtyEditorProduct.gone()
                return
            } else {
                qtyEditorProduct.show()
            }

            val maxQty = if (product.invenageValue < product.maxOrder) {
                product.invenageValue
            } else {
                product.maxOrder
            }
            showHideQuantityError(product, labelQuantityError)

            qtyEditorProduct.apply {
                onFocusChanged = { focus ->
                    val currentFocus = qtyState.value
                    if (currentFocus is QtyState.Focus && !focus.isFocused) {
                        val newQty = qtyValue.value
                        listener.onCheckoutItemQuantityChanged(product, newQty)
                        hideKeyboard()
                        listener.clearAllFocus()
                    }
                    qtyState.value = if (focus.isFocused) QtyState.Focus else QtyState.Enabled
                    if (focus.isFocused) {
                        listener.onClickInputQty()
                    }
                }
                keyboardOptions.value = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                )
                keyboardActions.value = KeyboardActions(
                    onDone = {
                        hideKeyboard()
                        listener.clearAllFocus()
                    }
                )
                qtyValue.value = product.quantity
                configState.value = configState.value.copy(
                    minInt = product.minOrder,
                    maxInt = maxQty,
                    qtyField = configState.value.qtyField.copy(
                        maxLine = 1
                    ),
                    qtyMinusButton = configState.value.qtyMinusButton.copy(
                        onClick = {
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onQtyMinusButtonClicked()
                            }
                        }
                    ),
                    qtyPlusButton = configState.value.qtyPlusButton.copy(
                        onClick = {
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onQtyPlusButtonClicked()
                            }
                        }
                    )
                )

                onValueChanged = { qty ->
                    if (qtyState.value !is QtyState.Focus) {
                        if (qty != 0) {
                            listener.onCheckoutItemQuantityChanged(product, qty)
                        }
                    } else {
                        qtyValue.value = qty
                    }
                }
            }
        }
    }

    private fun showHideQuantityError(product: CheckoutProductModel, labelQuantityError: Typography) {
        if (product.shouldShowMaxQtyError || product.shouldShowMinQtyError) {
            labelQuantityError.show()
            labelQuantityError.setPadding(
                0,
                0,
                0,
                MARGIN_16.dpToPx(itemView.context.resources.displayMetrics)
            )
            if (product.shouldShowMaxQtyError) {
                val maxQty = if (product.invenageValue < product.maxOrder) {
                    product.invenageValue
                } else {
                    product.maxOrder
                }
                labelQuantityError.text = String.format(
                    itemView.context.resources.getString(R.string.checkout_max_quantity_error),
                    maxQty
                )
            }
            if (product.shouldShowMinQtyError) {
                labelQuantityError.text = String.format(
                    itemView.context.resources.getString(R.string.checkout_min_quantity_error),
                    product.minOrder
                )
            }
        } else {
            labelQuantityError.gone()
        }
    }

    private fun renderNotesEmpty(buttonChangeNote: ImageUnify) {
        buttonChangeNote.setImageResource(purchase_platformcommonR.drawable.ic_pp_add_note)
        buttonChangeNote.contentDescription =
            binding.root.context.getString(purchase_platformcommonR.string.cart_button_notes_empty_content_desc)
    }

    private fun renderNotesFilled(buttonChangeNote: ImageUnify) {
        buttonChangeNote.setImageResource(purchase_platformcommonR.drawable.ic_pp_add_note_completed)
        buttonChangeNote.contentDescription =
            binding.root.context.getString(purchase_platformcommonR.string.cart_button_notes_filled_content_desc)
    }

    private fun setNoteAnimationResource(notesLottie: LottieAnimationView) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            notesLottie.setAnimation(R.raw.anim_checkout_note_dark)
        } else {
            notesLottie.setAnimation(R.raw.anim_checkout_note)
        }
    }

    private fun hideBundleViews() {
        bundleBinding.apply {
            tvProductIdentifierBundle.isVisible = false
            tvCheckoutBundleSeparator.isVisible = false
            tvCheckoutBundleName.isVisible = false
            tvCheckoutBundlePrice.isVisible = false
            vBundlingProductSeparator.isVisible = false
            ivProductImageBundle.isVisible = false
            ivProductImageBundleFrame.isVisible = false
            tvProductNameBundle.isVisible = false
            tvProductVariantBundle.isVisible = false
            tvProductNotesBundle.isVisible = false
            tvProductAddOnsSectionTitleBundle.isVisible = false
            tvProductAddOnsSeeAllBundle.isVisible = false
            llAddonProductItemsBundle.isVisible = false
            buttonGiftingAddonProductBundle.isVisible = false
            qtyEditorProductBundle.isVisible = false
            labelQuantityErrorBundle.isVisible = false
            buttonChangeNoteBundle.isVisible = false
            buttonChangeNoteLottieBundle.isVisible = false
        }
    }

    private fun renderBMGMItem(product: CheckoutProductModel) {
        fun renderAdjustableFirstItemMarginBmgm() {
            with(bmgmBinding) {
                if (product.shouldShowBmgmInfo) {
                    (ivProductImageFrameBmgm.layoutParams as? MarginLayoutParams)?.topMargin = MARGIN_TOP_BMGM_WITH_HEADER_CARD.dpToPx(itemView.resources.displayMetrics)
                } else {
                    (ivProductImageFrameBmgm.layoutParams as? MarginLayoutParams)?.topMargin = MARGIN_TOP_BMGM_CARD.dpToPx(itemView.resources.displayMetrics)
                }
            }
        }

        fun renderProductNameBmgm() {
            with(bmgmBinding) {
                tvProductNameBmgm.text = product.name
                tvProductNameBmgm.show()

                if (product.ethicalDrugDataModel.needPrescription && product.ethicalDrugDataModel.iconUrl.isNotEmpty()) {
                    product.ethicalDrugDataModel.iconUrl.getBitmapImageUrl(bmgmBinding.root.context) {
                        try {
                            tvProductNameBmgm.text = SpannableStringBuilder("  ${product.name}").apply {
                                setSpan(ImageSpan(bmgmBinding.root.context, it, DynamicDrawableSpan.ALIGN_CENTER), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                            }
                        } catch (t: Throwable) {
                            t.printStackTrace()
                        }
                    }
                }
            }
        }

        fun renderImageBmgm() {
            with(bmgmBinding) {
                ivProductImageBmgm.show()
                ivProductImageFrameBmgm.show()
                ivProductImageBmgm.setImageUrl(product.imageUrl)
            }
        }

        fun renderPriceBmgm() {
            with(bmgmBinding) {
                val priceInRp =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(product.price, false)
                        .removeDecimalSuffix()
                val qty = product.quantity
                tvProductPriceBmgm.text = "$qty x $priceInRp"
                tvProductPriceBmgm.show()
            }
        }

        fun renderVariantBmgm() {
            with(bmgmBinding) {
                tvProductVariantBmgm.shouldShowWithAction(product.variant.isNotBlank()) {
                    tvProductVariantBmgm.text = product.variant
                }
            }
        }

        fun renderSellerNotesBmgm() {
            with(bmgmBinding) {
                tvProductNotesBmgm.shouldShowWithAction(product.noteToSeller.isNotEmpty()) {
                    tvProductNotesBmgm.text = "\"${product.noteToSeller}\""
                }
            }
        }

        fun renderAdjustableSeparatorMarginBmgm() {
            with(bmgmBinding) {
                if (product.shouldShowBmgmInfo) {
                    (vProductSeparatorBmgm.layoutParams as? MarginLayoutParams)?.topMargin = MARGIN_TOP_BMGM_WITH_HEADER_CARD.dpToPx(itemView.resources.displayMetrics)
                } else {
                    (vProductSeparatorBmgm.layoutParams as? MarginLayoutParams)?.topMargin = Int.ZERO
                }
                vProductSeparatorBmgm.show()
            }
        }

        hideProductViews()
        hideBundleViews()
        renderGroupInfo(product)
        renderShopInfo(product)

        renderProductNameBmgm()
        renderImageBmgm()
        renderPriceBmgm()
        renderVariantBmgm()
        renderSellerNotesBmgm()
        renderAdjustableFirstItemMarginBmgm()
        renderAdjustableSeparatorMarginBmgm()

        renderNotes(product, bmgmBinding.buttonChangeNoteBmgm, bmgmBinding.buttonChangeNoteLottieBmgm)
        renderQuantity(product, bmgmBinding.qtyEditorProductBmgm, bmgmBinding.labelQuantityErrorBmgm)
        renderAddOnBMGM(product)
        renderAddOnGiftingProductBmgm(product)
    }

    private fun hideBMGMViews() {
        with(bmgmBinding) {
            ivProductImageBmgm.hide()
            ivProductImageFrameBmgm.hide()
            vProductSeparatorBmgm.hide()
            tvProductNameBmgm.hide()
            tvProductPriceBmgm.hide()
            tvProductVariantBmgm.hide()
            tvProductNotesBmgm.hide()
            tvProductAddOnsSectionTitleBmgm.hide()
            tvProductAddOnsSeeAllBmgm.hide()
            llAddonProductItemsBmgm.hide()
            buttonGiftingAddonProductBmgm.hide()
            qtyEditorProductBmgm.hide()
            labelQuantityErrorBmgm.hide()
            buttonChangeNoteBmgm.hide()
            buttonChangeNoteLottieBmgm.hide()
        }
    }

    private fun renderGroupInfo(product: CheckoutProductModel) {
        if (product.shouldShowGroupInfo) {
            binding.vDividerOrder.isVisible = product.orderNumber > 1
            if (product.orderNumber == -1) {
                if (product.groupInfoDescription.isNotEmpty()) {
                    binding.tvCheckoutOrderNumber.text = itemView.context.getString(
                        R.string.label_order_counter_new,
                        1
                    )
                    binding.tvCheckoutOrderDescription.text = product.groupInfoDescription
                    binding.tvCheckoutOrderNumber.isVisible = true
                    binding.tvCheckoutOrderDescription.isVisible = true
                } else {
                    binding.tvCheckoutOrderNumber.isVisible = false
                    binding.tvCheckoutOrderDescription.isVisible = false
                }
            } else {
                binding.tvCheckoutOrderNumber.text = itemView.context.getString(
                    R.string.label_order_counter_new,
                    product.orderNumber
                )
                binding.tvCheckoutOrderNumber.isVisible = true
                if (product.groupInfoDescription.isNotEmpty()) {
                    binding.tvCheckoutOrderDescription.text = product.groupInfoDescription
                    binding.tvCheckoutOrderDescription.isVisible = true
                } else {
                    binding.tvCheckoutOrderDescription.isVisible = false
                }
            }
            binding.bgCheckoutSupergraphicOrder.isVisible = true
            if (product.groupInfoBadgeUrl.isNotEmpty()) {
                binding.ivCheckoutOrderBadge.setImageUrl(product.groupInfoBadgeUrl)
                if (product.uiGroupType == GroupShop.UI_GROUP_TYPE_NORMAL) {
                    binding.ivCheckoutOrderBadge.contentDescription = itemView.context.getString(
                        purchase_platformcommonR.string.pp_cd_image_shop_badge_with_shop_type,
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
                binding.ivCheckoutFreeShipping.setImageUrl(freeShippingBadgeUrl)
                if (product.isFreeShippingPlus) {
                    binding.ivCheckoutFreeShipping.contentDescription =
                        itemView.context.getString(purchase_platformcommonR.string.pp_cd_image_badge_plus)
                } else {
                    binding.ivCheckoutFreeShipping.contentDescription =
                        itemView.context.getString(purchase_platformcommonR.string.pp_cd_image_badge_bo)
                }
                binding.ivCheckoutFreeShipping.isVisible = true
                if (!product.hasSeenFreeShippingBadge && product.isFreeShippingPlus) {
                    product.hasSeenFreeShippingBadge = true
                    listener.onViewFreeShippingPlusBadge()
                }
            } else {
                binding.ivCheckoutFreeShipping.isVisible = false
            }
        } else {
            binding.vDividerOrder.isVisible = false
            binding.tvCheckoutOrderNumber.isVisible = false
            binding.bgCheckoutSupergraphicOrder.isVisible = false
            binding.ivCheckoutOrderBadge.isVisible = false
            binding.tvCheckoutOrderName.isVisible = false
            binding.ivCheckoutFreeShipping.isVisible = false
            binding.tvCheckoutOrderDescription.isVisible = false
        }
        renderBMGMGroupInfo(product)
    }

    private fun renderBMGMGroupInfo(product: CheckoutProductModel) {
        fun renderBmgmGroupTitle() {
            binding.tvCheckoutBmgmTitle.shouldShowWithAction(product.shouldShowBmgmInfo) {
                val spannedTitle = SpannableStringBuilder()
                val color = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_TN500)
                product.bmgmOfferMessage.forEachIndexed { idx, htmlMessage ->
                    val message = MethodChecker.fromHtml(htmlMessage)
                    if (idx > Int.ZERO) {
                        spannedTitle.color(color) { append(" • $message") }
                    } else {
                        spannedTitle.color(color) { append(message) }
                    }
                }
                binding.tvCheckoutBmgmTitle.text = spannedTitle
            }
        }

        fun renderBmgmGroupBadge() {
            binding.ivCheckoutBmgmBadge.shouldShowWithAction(product.shouldShowBmgmInfo) {
                binding.ivCheckoutBmgmBadge.setImageUrl(product.bmgmIconUrl)
            }
        }

        fun renderBmgmGroupDetail() {
            binding.ivCheckoutBmgmDetail.shouldShowWithAction(product.shouldShowBmgmInfoIcon) {
                binding.ivCheckoutBmgmDetail.setOnClickListener {
                    val bmgmCommonData = CheckoutBmgmMapper.mapBmgmCommonDataModel(
                        product,
                        product.warehouseId.toLongOrZero(),
                        product.shopId,
                        itemView.context.getString(R.string.bmgm_mini_cart_bottom_sheet_title)
                    )
                    PersistentCacheManager.instance.put(BmgmCommonDataModel.PARAM_KEY_BMGM_DATA, bmgmCommonData)
                    listener.onClickBmgmInfoIcon(product.bmgmOfferId.toString(), product.shopId)
                }
            }
        }

        renderBmgmGroupTitle()
        renderBmgmGroupBadge()
        renderBmgmGroupDetail()
    }

    private fun renderShopInfo(product: CheckoutProductModel) {
        if (product.shouldShowShopInfo) {
            if (product.shopTypeInfoData.shopBadge.isNotEmpty()) {
                binding.ivCheckoutShopBadge.setImageUrl(product.shopTypeInfoData.shopBadge)
                binding.ivCheckoutShopBadge.visible()
                binding.ivCheckoutShopBadge.contentDescription = itemView.context.getString(
                    purchase_platformcommonR.string.pp_cd_image_shop_badge_with_shop_type,
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
            productBinding.tvProductAddOnsSectionTitle.gone()
            productBinding.tvProductAddOnsSeeAll.gone()
            productBinding.llAddonProductItems.gone()
        } else {
            productBinding.llAddonProductItems.removeAllViews()
            if (addOnProduct.bottomsheet.isShown) {
                productBinding.tvProductAddOnsSectionTitle.text = addOnProduct.title
                productBinding.tvProductAddOnsSectionTitle.visible()
                productBinding.tvProductAddOnsSeeAll.apply {
                    visible()
                    text = addOnProduct.bottomsheet.title
                    setOnClickListener {
                        listener.onClickSeeAllAddOnProductService(product)
                    }
                }
            } else {
                productBinding.tvProductAddOnsSectionTitle.gone()
                productBinding.tvProductAddOnsSeeAll.gone()
            }
            val layoutInflater = LayoutInflater.from(itemView.context)
            addOnProduct.listAddOnProductData.forEach { addon ->
                if (addon.name.isNotEmpty()) {
                    val addOnView =
                        ItemAddOnProductRevampBinding.inflate(
                            layoutInflater,
                            productBinding.llAddonProductItems,
                            false
                        )
                    addOnView.apply {
                        icCheckoutAddOnsItem.setImageUrl(addon.iconUrl)
                        val colorMatrix = ColorMatrix()
                        colorMatrix.setSaturation(0F)
                        icCheckoutAddOnsItem.colorFilter = ColorMatrixColorFilter(colorMatrix)
                        tvCheckoutAddOnsItemName.text = SpannableString(addon.name).apply {
                            setSpan(
                                UnderlineSpan(),
                                0,
                                addon.name.length,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                            )
                        }
                        tvCheckoutAddOnsItemPrice.text = " (${
                        CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(addon.price, false)
                            .removeDecimalSuffix()
                        })"
                        cbCheckoutAddOns.setOnCheckedChangeListener { _, _ -> }
                        when (addon.status) {
                            AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK -> {
                                cbCheckoutAddOns.isChecked = true
                                cbCheckoutAddOns.isEnabled = true
                            }

                            AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY -> {
                                cbCheckoutAddOns.isChecked = true
                                cbCheckoutAddOns.isEnabled = false
                            }

                            else -> {
                                cbCheckoutAddOns.isChecked = false
                                cbCheckoutAddOns.isEnabled = true
                            }
                        }
                        cbCheckoutAddOns.skipAnimation()
                        cbCheckoutAddOns.setOnCheckedChangeListener { _, isChecked ->
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
                        tvCheckoutAddOnsItemName.setOnClickListener {
                            listener.onClickAddonProductInfoIcon(addon)
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
            bundleBinding.tvProductAddOnsSectionTitleBundle.gone()
            bundleBinding.tvProductAddOnsSeeAllBundle.gone()
            bundleBinding.llAddonProductItemsBundle.gone()
        } else {
            bundleBinding.llAddonProductItemsBundle.removeAllViews()
            if (addOnProduct.bottomsheet.isShown) {
                bundleBinding.tvProductAddOnsSectionTitleBundle.text = addOnProduct.title
                bundleBinding.tvProductAddOnsSectionTitleBundle.visible()
                bundleBinding.tvProductAddOnsSeeAllBundle.apply {
                    visible()
                    text = addOnProduct.bottomsheet.title
                    setOnClickListener {
                        listener.onClickSeeAllAddOnProductService(product)
                    }
                }
            } else {
                bundleBinding.tvProductAddOnsSectionTitleBundle.gone()
                bundleBinding.tvProductAddOnsSeeAllBundle.gone()
            }
            val layoutInflater = LayoutInflater.from(itemView.context)
            addOnProduct.listAddOnProductData.forEach { addon ->
                if (addon.name.isNotEmpty()) {
                    val addOnView =
                        ItemAddOnProductRevampBinding.inflate(
                            layoutInflater,
                            bundleBinding.llAddonProductItemsBundle,
                            false
                        )
                    addOnView.apply {
                        icCheckoutAddOnsItem.setImageUrl(addon.iconUrl)
                        val colorMatrix = ColorMatrix()
                        colorMatrix.setSaturation(0F)
                        icCheckoutAddOnsItem.colorFilter = ColorMatrixColorFilter(colorMatrix)
                        tvCheckoutAddOnsItemName.text = SpannableString(addon.name).apply {
                            setSpan(
                                UnderlineSpan(),
                                0,
                                addon.name.length,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                            )
                        }
                        tvCheckoutAddOnsItemPrice.text = " (${
                        CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(addon.price, false)
                            .removeDecimalSuffix()
                        })"
                        cbCheckoutAddOns.setOnCheckedChangeListener { _, _ -> }
                        when (addon.status) {
                            AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK -> {
                                cbCheckoutAddOns.isChecked = true
                                cbCheckoutAddOns.isEnabled = true
                            }

                            AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY -> {
                                cbCheckoutAddOns.isChecked = true
                                cbCheckoutAddOns.isEnabled = false
                            }

                            else -> {
                                cbCheckoutAddOns.isChecked = false
                                cbCheckoutAddOns.isEnabled = true
                            }
                        }
                        cbCheckoutAddOns.skipAnimation()
                        cbCheckoutAddOns.setOnCheckedChangeListener { _, isChecked ->
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
                        tvCheckoutAddOnsItemName.setOnClickListener {
                            listener.onClickAddonProductInfoIcon(addon)
                        }
                    }
                    bundleBinding.llAddonProductItemsBundle.addView(addOnView.root)
                    bundleBinding.llAddonProductItemsBundle.visible()
                    listener.onImpressionAddOnProductService(
                        addon.type,
                        product.productId.toString()
                    )
                }
            }
        }
    }

    private fun renderAddOnBMGM(product: CheckoutProductModel) {
        with(bmgmBinding) {
            val addOnProduct = product.addOnProduct
            if (addOnProduct.listAddOnProductData.isEmpty()) {
                tvProductAddOnsSectionTitleBmgm.gone()
                tvProductAddOnsSeeAllBmgm.gone()
                llAddonProductItemsBmgm.gone()
            } else {
                llAddonProductItemsBmgm.removeAllViews()
                if (addOnProduct.bottomsheet.isShown) {
                    tvProductAddOnsSectionTitleBmgm.run {
                        text = addOnProduct.title
                        show()
                    }
                    tvProductAddOnsSeeAllBmgm.run {
                        text = addOnProduct.bottomsheet.title
                        show()
                        setOnClickListener {
                            listener.onClickSeeAllAddOnProductService(product)
                        }
                    }
                } else {
                    tvProductAddOnsSectionTitleBmgm.gone()
                    tvProductAddOnsSeeAllBmgm.gone()
                }
                val layoutInflater = LayoutInflater.from(itemView.context)
                addOnProduct.listAddOnProductData.forEach { addon ->
                    if (addon.name.isNotEmpty()) {
                        val addOnView =
                            ItemAddOnProductBinding.inflate(layoutInflater, llAddonProductItemsBmgm, false)
                        addOnView.apply {
                            val colorMatrix = ColorMatrix()
                            colorMatrix.setSaturation(0F)
                            icCheckoutAddOnsItem.colorFilter = ColorMatrixColorFilter(colorMatrix)
                            icCheckoutAddOnsItem.setImageUrl(addon.iconUrl)
                            tvCheckoutAddOnsItemName.text = SpannableString(addon.name).apply {
                                setSpan(UnderlineSpan(), 0, addon.name.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                            }
                            tvCheckoutAddOnsItemPrice.text = " (${CurrencyFormatUtil
                                .convertPriceValueToIdrFormat(addon.price, false)
                                .removeDecimalSuffix()})"
                            cbCheckoutAddOns.setOnCheckedChangeListener { _, _ -> }
                            when (addon.status) {
                                AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK -> {
                                    cbCheckoutAddOns.isChecked = true
                                    cbCheckoutAddOns.isEnabled = true
                                }
                                AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY -> {
                                    cbCheckoutAddOns.isChecked = true
                                    cbCheckoutAddOns.isEnabled = false
                                }
                                else -> {
                                    cbCheckoutAddOns.isChecked = false
                                    cbCheckoutAddOns.isEnabled = true
                                }
                            }
                            cbCheckoutAddOns.skipAnimation()
                            cbCheckoutAddOns.setOnCheckedChangeListener { _, isChecked ->
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
                            tvCheckoutAddOnsItemName.setOnClickListener {
                                listener.onClickAddonProductInfoIcon(addon)
                            }
                        }
                        llAddonProductItemsBmgm.addView(addOnView.root)
                        llAddonProductItemsBmgm.visible()
                        listener.onImpressionAddOnProductService(
                            addon.type,
                            product.productId.toString()
                        )
                    }
                }
            }
        }
    }

    private fun renderAddOnGiftingProduct(
        product: CheckoutProductModel
    ) {
        with(productBinding) {
            val addOns = product.addOnGiftingProductLevelModel
            if (addOns.status == 0) {
                buttonGiftingAddonProduct.visibility = View.GONE
                buttonGiftingAddonProduct.setOnClickListener { }
            } else {
                if (addOns.status == 1) {
                    if (addOns.addOnsDataItemModelList.isNotEmpty()) {
                        buttonGiftingAddonProduct.showActive(
                            addOns.addOnsButtonModel.title,
                            addOns.addOnsButtonModel.description,
                            addOns.addOnsButtonModel.rightIconUrl
                        )
                    } else {
                        buttonGiftingAddonProduct.showEmptyState(
                            addOns.addOnsButtonModel.title,
                            addOns.addOnsButtonModel.description.ifEmpty { "(opsional)" },
                            addOns.addOnsButtonModel.rightIconUrl
                        )
                    }
                } else if (addOns.status == 2) {
                    buttonGiftingAddonProduct.showInactive(
                        addOns.addOnsButtonModel.title,
                        addOns.addOnsButtonModel.description,
                        addOns.addOnsButtonModel.rightIconUrl
                    )
                }
                buttonGiftingAddonProduct.setOnClickListener {
                    listener.onClickAddOnGiftingProductLevel(
                        product
                    )
                }
                buttonGiftingAddonProduct.visibility = View.VISIBLE
                listener.onImpressionAddOnGiftingProductLevel(product.productId.toString())
            }
        }
    }

    private fun renderAddOnGiftingProductBundle(
        product: CheckoutProductModel
    ) {
        with(bundleBinding) {
            val addOns = product.addOnGiftingProductLevelModel
            if (addOns.status == 0) {
                buttonGiftingAddonProductBundle.visibility = View.GONE
                buttonGiftingAddonProductBundle.setOnClickListener { }
            } else {
                if (addOns.status == 1) {
                    if (addOns.addOnsDataItemModelList.isNotEmpty()) {
                        buttonGiftingAddonProductBundle.showActive(
                            addOns.addOnsButtonModel.title,
                            addOns.addOnsButtonModel.description,
                            addOns.addOnsButtonModel.rightIconUrl
                        )
                    } else {
                        buttonGiftingAddonProductBundle.showEmptyState(
                            addOns.addOnsButtonModel.title,
                            addOns.addOnsButtonModel.description.ifEmpty { "(opsional)" },
                            addOns.addOnsButtonModel.rightIconUrl
                        )
                    }
                } else if (addOns.status == 2) {
                    buttonGiftingAddonProductBundle.showInactive(
                        addOns.addOnsButtonModel.title,
                        addOns.addOnsButtonModel.description,
                        addOns.addOnsButtonModel.rightIconUrl
                    )
                }
                buttonGiftingAddonProductBundle.setOnClickListener {
                    listener.onClickAddOnGiftingProductLevel(
                        product
                    )
                }
                buttonGiftingAddonProductBundle.visibility = View.VISIBLE
                listener.onImpressionAddOnGiftingProductLevel(product.productId.toString())
            }
        }
    }

    private fun renderAddOnGiftingProductBmgm(
        product: CheckoutProductModel
    ) {
        with(bmgmBinding) {
            val addOns = product.addOnGiftingProductLevelModel
            if (addOns.status == 0) {
                buttonGiftingAddonProductBmgm.visibility = View.GONE
                buttonGiftingAddonProductBmgm.setOnClickListener { }
            } else {
                if (addOns.status == 1) {
                    if (addOns.addOnsDataItemModelList.isNotEmpty()) {
                        buttonGiftingAddonProductBmgm.showActive(
                            addOns.addOnsButtonModel.title,
                            addOns.addOnsButtonModel.description,
                            addOns.addOnsButtonModel.rightIconUrl
                        )
                    } else {
                        buttonGiftingAddonProductBmgm.showEmptyState(
                            addOns.addOnsButtonModel.title,
                            addOns.addOnsButtonModel.description.ifEmpty { "(opsional)" },
                            addOns.addOnsButtonModel.rightIconUrl
                        )
                    }
                } else if (addOns.status == 2) {
                    buttonGiftingAddonProductBmgm.showInactive(
                        addOns.addOnsButtonModel.title,
                        addOns.addOnsButtonModel.description,
                        addOns.addOnsButtonModel.rightIconUrl
                    )
                }
                buttonGiftingAddonProductBmgm.setOnClickListener {
                    listener.onClickAddOnGiftingProductLevel(
                        product
                    )
                }
                buttonGiftingAddonProductBmgm.visibility = View.VISIBLE
                listener.onImpressionAddOnGiftingProductLevel(product.productId.toString())
            }
        }
    }

    private fun renderErrorAndWarningGroup(product: CheckoutProductModel) {
        if (product.shouldShowGroupInfo) {
            val order = listener.getOrderByCartStringGroup(product.cartStringGroup)
            if (order != null) {
                renderGroupError(order)
                if (!binding.checkoutTickerShopError.isVisible) {
                    renderWarningGroup(order)
                }
                if (!binding.checkoutTickerShopError.isVisible) {
                    renderCustomError(order)
                }
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
                            checkoutTickerShopError.setDescriptionClickEvent(object :
                                    TickerCallback {
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
                            checkoutTickerShopError.setDescriptionClickEvent(object :
                                    TickerCallback {
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
                } else {
                    checkoutTickerShopError.gone()
                }
            } else {
                checkoutTickerShopError.gone()
            }
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
                checkoutTickerShopError.setHtmlDescription(
                    "$errorMessage " + itemView.context.getString(
                        R.string.checkout_ticker_lihat_cta_suffix
                    )
                )
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

        private const val EPHARMACY_ICON_SIZE = 12
        private const val MARGIN_TOP_BMGM_CARD = 24
        private const val MARGIN_TOP_BMGM_WITH_HEADER_CARD = 12
        private const val MARGIN_16 = 16
    }
}
