package com.tokopedia.cartrevamp.view.viewholder

import android.annotation.SuppressLint
import android.graphics.Paint
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cart.databinding.ItemCartProductRevampBinding
import com.tokopedia.cartrevamp.view.adapter.cart.CartItemAdapter
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData.Companion.BUNDLING_ITEM_FOOTER
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData.Companion.BUNDLING_ITEM_HEADER
import com.tokopedia.cartrevamp.view.uimodel.CartMainCoachMarkUiModel
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("ClickableViewAccessibility")
class CartItemViewHolder constructor(
    private val binding: ItemCartProductRevampBinding,
    private var actionListener: CartItemAdapter.ActionListener?,
    private var mainCoachMark: CartMainCoachMarkUiModel
) : RecyclerView.ViewHolder(binding.root) {

    private var viewHolderListener: ViewHolderListener? = null

    private var dataSize: Int = 0
    private var delayChangeCheckboxState: Job? = null
    private var delayChangeWishlistStatus: Job? = null
    private var delayChangeQty: Job? = null
    private var informationLabel: MutableList<String> = mutableListOf()
    private var qtyTextWatcher: TextWatcher? = null
    private var lastQty: Int = 0

    @SuppressLint("ClickableViewAccessibility")
    fun clear() {
        actionListener = null
        viewHolderListener = null
        delayChangeCheckboxState?.cancel()
        delayChangeQty?.cancel()
        delayChangeWishlistStatus?.cancel()
        qtyTextWatcher = null
    }

    fun bindData(data: CartItemHolderData, viewHolderListener: ViewHolderListener?, dataSize: Int) {
        this.viewHolderListener = viewHolderListener
        this.dataSize = dataSize

        initCoachMark()
        setNoteAnimationResource()
        renderAlpha(data)
        renderContainer(data)
        renderDivider(data)
        renderShopInfo(data)
        renderLeftAnchor(data)
        renderProductInfo(data)
        renderQuantity(data, viewHolderListener)
        renderProductAction(data)
    }

    private fun initCoachMark() {
        if (mainCoachMark.coachMark != null && !CoachMarkPreference.hasShown(
                itemView.context,
                CART_MAIN_COACH_MARK
            )
        ) {
            val coachMarkItems = mainCoachMark.coachMarkItems
            val wishlistCoachMark = CoachMark2Item(
                binding.buttonToggleWishlist,
                String.EMPTY,
                mainCoachMark.wishlistOnBoardingData.text,
                CoachMark2.POSITION_BOTTOM
            )
            val noteCoachMark = CoachMark2Item(
                binding.buttonChangeNote,
                String.EMPTY,
                mainCoachMark.noteOnBoardingData.text,
                CoachMark2.POSITION_BOTTOM
            )
            coachMarkItems.add(Int.ZERO, wishlistCoachMark)
            coachMarkItems.add(Int.ZERO, noteCoachMark)
            mainCoachMark.coachMark?.showCoachMark(coachMarkItems)
            CoachMarkPreference.setShown(itemView.context, CART_MAIN_COACH_MARK, true)
        }
    }

    private fun setNoteAnimationResource() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.buttonChangeNoteLottie.setAnimation(R.raw.anim_cart_note_dark)
        } else {
            binding.buttonChangeNoteLottie.setAnimation(R.raw.anim_cart_note)
        }
    }

    private fun renderLeftAnchor(data: CartItemHolderData) {
        binding.vBundlingProductSeparator.show()
        if (data.isBundlingItem) {
            with(binding) {
                checkboxProduct.gone()
                vBundlingProductSeparator.show()
                val marginStart = if (data.isError) {
                    IMAGE_PRODUCT_MARGIN_START_6.dpToPx(itemView.resources.displayMetrics)
                } else {
                    BUNDLING_SEPARATOR_MARGIN_START.dpToPx(itemView.resources.displayMetrics)
                }
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProductInformation)
                constraintSet.connect(
                    R.id.iu_image_product,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                    marginStart
                )
                constraintSet.connect(
                    R.id.iu_image_product,
                    ConstraintSet.TOP,
                    R.id.product_bundling_info,
                    ConstraintSet.BOTTOM
                )
                constraintSet.applyTo(containerProductInformation)
                renderCheckBoxBundle(data)
            }
        } else {
            with(binding) {
                vBundlingProductSeparator.gone()
                checkboxProduct.show()
                val marginStart =
                    IMAGE_PRODUCT_MARGIN_START_6.dpToPx(itemView.resources.displayMetrics)
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProductInformation)
                constraintSet.connect(
                    R.id.iu_image_product,
                    ConstraintSet.START,
                    R.id.checkbox_product,
                    ConstraintSet.END,
                    marginStart
                )
                constraintSet.connect(
                    R.id.iu_image_product,
                    ConstraintSet.TOP,
                    R.id.container_product_information,
                    ConstraintSet.TOP
                )
                constraintSet.applyTo(containerProductInformation)
                renderCheckBoxProduct(data)
            }
        }
        adjustProductVerticalSeparatorConstraint(data)
    }

    private fun renderProductAction(data: CartItemHolderData) {
        with(binding) {
            buttonChangeNote.gone()
            buttonChangeNote.gone()
            buttonToggleWishlist.gone()
            ivAnimatedWishlist.gone()
            textProductUnavailableAction.gone()
            buttonDeleteCart.gone()
        }
        if (data.actionsData.isNotEmpty()) {
            data.actionsData.forEach {
                when (it.id) {
                    Action.ACTION_NOTES -> {
                        renderProductNotes(data)
                    }

                    Action.ACTION_WISHLIST, Action.ACTION_WISHLISTED -> {
                        renderActionWishlist(data)
                    }

                    Action.ACTION_CHECKOUTBROWSER, Action.ACTION_SIMILARPRODUCT, Action.ACTION_FOLLOWSHOP, Action.ACTION_VERIFICATION -> {
                        when {
                            data.selectedUnavailableActionId == Action.ACTION_CHECKOUTBROWSER && it.id == Action.ACTION_CHECKOUTBROWSER -> {
                                renderActionCheckoutInBrowser(it, data)
                            }

                            data.selectedUnavailableActionId == Action.ACTION_SIMILARPRODUCT && it.id == Action.ACTION_SIMILARPRODUCT -> {
                                renderActionSimilarProduct(it, data)
                                renderActionDelete(data)
                            }

                            data.selectedUnavailableActionId == Action.ACTION_FOLLOWSHOP && it.id == Action.ACTION_FOLLOWSHOP -> {
                                renderFollowShop(it, data)
                            }

                            data.selectedUnavailableActionId == Action.ACTION_VERIFICATION && it.id == Action.ACTION_VERIFICATION -> {
                                renderVerification(it, data)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderActionDelete(data: CartItemHolderData) {
        binding.buttonDeleteCart.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemDeleteButtonClicked(data, true)
            }
        }
        binding.buttonDeleteCart.show()
    }

    private fun renderCheckBoxProduct(data: CartItemHolderData) {
        val checkboxProduct = binding.checkboxProduct
        if (data.isError) {
            checkboxProduct.gone()
            return
        }

        checkboxProduct.show()
        checkboxProduct.setOnCheckedChangeListener { compoundButton, b ->
            // disable listener before setting current selection state
        }
        checkboxProduct.isChecked = data.isSelected
        checkboxProduct.skipAnimation()

        var prevIsChecked: Boolean = checkboxProduct.isChecked
        checkboxProduct.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != prevIsChecked) {
                prevIsChecked = isChecked

                delayChangeCheckboxState?.cancel()
                delayChangeCheckboxState = GlobalScope.launch(Dispatchers.Main) {
                    delay(DEBOUNCE_TIME)
                    if (isChecked == prevIsChecked && isChecked != data.isSelected) {
                        if (!data.isError) {
                            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                                actionListener?.onCartItemCheckChanged(bindingAdapterPosition, data)
                                viewHolderListener?.onNeedToRefreshSingleShop(
                                    data,
                                    bindingAdapterPosition
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderCheckBoxBundle(data: CartItemHolderData) {
        val checkboxBundle = binding.checkboxBundle
        val padding16 = PRODUCT_ACTION_MARGIN.dpToPx(itemView.resources.displayMetrics)
        binding.productBundlingInfo.setPadding(0, 0, 0, padding16)
        if (data.isError) {
            checkboxBundle.gone()
            return
        }
        checkboxBundle.show()
        checkboxBundle.setOnCheckedChangeListener { compoundButton, b ->
            // disable listener before setting current selection state
        }
        checkboxBundle.isChecked = data.isSelected
        checkboxBundle.skipAnimation()

        var prevIsChecked: Boolean = checkboxBundle.isChecked
        checkboxBundle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != prevIsChecked) {
                prevIsChecked = isChecked

                delayChangeCheckboxState?.cancel()
                delayChangeCheckboxState = GlobalScope.launch(Dispatchers.Main) {
                    delay(DEBOUNCE_TIME)
                    if (isChecked == prevIsChecked && isChecked != data.isSelected) {
                        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                            actionListener?.onBundleItemCheckChanged(data)
                            viewHolderListener?.onNeedToRefreshSingleShop(
                                data,
                                bindingAdapterPosition
                            )
                        }
                    }
                }
            }
        }
    }

    private fun renderShopInfo(data: CartItemHolderData) {
        if (data.isShopShown) {
            binding.llShopHeader.visible()
            val shopHolderData = data.shopHolderData
            binding.tvShopName.text = Utils.getHtmlFormat(shopHolderData.shopName)
            binding.tvShopName.setOnClickListener {
                actionListener?.onCartShopNameClicked(
                    data.shopHolderData.shopId,
                    data.shopHolderData.shopName,
                    data.shopHolderData.isTokoNow
                )
            }
            if (shopHolderData.shopTypeInfo.shopBadge.isNotBlank()) {
                binding.imageShopBadge.loadImageWithoutPlaceholder(shopHolderData.shopTypeInfo.shopBadge)
                binding.imageShopBadge.contentDescription = itemView.context.getString(
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type,
                    shopHolderData.shopTypeInfo.title
                )
                binding.imageShopBadge.show()
            } else {
                binding.imageShopBadge.gone()
            }
        } else {
            binding.llShopHeader.gone()
        }
    }

    private fun renderProductInfo(data: CartItemHolderData) {
        renderBundlingInfo(data)
        renderProductName(data)
        renderImage(data)
        renderPrice(data)
        renderVariant(data)
        renderQuantityLeft(data)
        renderSlashPrice(data)
        renderProductProperties(data)
        renderProductActionSection(data)
        renderProductAddOns(data)
        sendAnalyticsInformationLabel(data)
    }

    private fun renderBundlingInfo(data: CartItemHolderData) {
        if (data.isBundlingItem && data.bundlingItemPosition == BUNDLING_ITEM_HEADER) {
            binding.productBundlingInfo.show()
            if (data.isError) {
                binding.checkboxBundle.gone()
            } else {
                binding.checkboxBundle.show()
            }

            renderBundlingInfoDetail(data)
        } else {
            binding.productBundlingInfo.gone()
            binding.checkboxBundle.gone()
        }
    }

    private fun renderProductActionSection(data: CartItemHolderData) {
        if (data.isBundlingItem) {
            if (data.isMultipleBundleProduct && (data.bundlingItemPosition == BUNDLING_ITEM_HEADER || data.bundlingItemPosition == CartItemHolderData.BUNDLING_ITEM_DEFAULT)) {
                binding.qtyEditorProduct.gone()
            } else {
                binding.qtyEditorProduct.show()
            }
            binding.buttonChangeNote.show()
            binding.buttonToggleWishlist.show()
        } else {
            binding.qtyEditorProduct.show()
            binding.buttonChangeNote.show()
            binding.buttonToggleWishlist.show()
        }
        adjustProductActionConstraint(data)
    }

    private fun adjustProductActionConstraint(data: CartItemHolderData) {
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(binding.containerProductInformation)
            val margin = IMAGE_PRODUCT_MARGIN_START.dpToPx(itemView.resources.displayMetrics)
            val marginTop = WISHLIST_ANIMATED_MARGIN_TOP.dpToPx(itemView.resources.displayMetrics)
            if (data.isBundlingItem) {
                connect(
                    R.id.button_change_note,
                    ConstraintSet.TOP,
                    R.id.item_addon_cart,
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.button_change_note_lottie,
                    ConstraintSet.TOP,
                    R.id.item_addon_cart,
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.button_toggle_wishlist,
                    ConstraintSet.TOP,
                    R.id.item_addon_cart,
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.iv_animated_wishlist,
                    ConstraintSet.TOP,
                    R.id.item_addon_cart,
                    ConstraintSet.BOTTOM,
                    marginTop
                )
                if (data.bundlingItemPosition == BUNDLING_ITEM_FOOTER || (!data.isMultipleBundleProduct)) {
                    connect(
                        R.id.qty_editor_product,
                        ConstraintSet.TOP,
                        R.id.button_change_note,
                        ConstraintSet.BOTTOM,
                        marginTop
                    )
                } else {
                    connect(
                        R.id.qty_editor_product,
                        ConstraintSet.TOP,
                        R.id.item_addon_cart,
                        ConstraintSet.BOTTOM,
                        marginTop
                    )
                }
                clear(R.id.button_change_note, ConstraintSet.BOTTOM)
                clear(R.id.button_change_note_lottie, ConstraintSet.BOTTOM)
                clear(R.id.button_toggle_wishlist, ConstraintSet.BOTTOM)
                clear(R.id.iv_animated_wishlist, ConstraintSet.BOTTOM)
                clear(R.id.qty_editor_product, ConstraintSet.BOTTOM)
            } else {
                connect(
                    R.id.button_change_note,
                    ConstraintSet.TOP,
                    R.id.qty_editor_product,
                    ConstraintSet.TOP,
                    margin
                )
                connect(
                    R.id.button_change_note_lottie,
                    ConstraintSet.TOP,
                    R.id.qty_editor_product,
                    ConstraintSet.TOP,
                    margin
                )
                connect(
                    R.id.button_change_note,
                    ConstraintSet.BOTTOM,
                    R.id.qty_editor_product,
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.button_change_note_lottie,
                    ConstraintSet.BOTTOM,
                    R.id.qty_editor_product,
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.button_toggle_wishlist,
                    ConstraintSet.TOP,
                    R.id.qty_editor_product,
                    ConstraintSet.TOP,
                    margin
                )
                connect(
                    R.id.iv_animated_wishlist,
                    ConstraintSet.TOP,
                    R.id.qty_editor_product,
                    ConstraintSet.TOP,
                    margin
                )
                connect(
                    R.id.button_toggle_wishlist,
                    ConstraintSet.BOTTOM,
                    R.id.qty_editor_product,
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.iv_animated_wishlist,
                    ConstraintSet.BOTTOM,
                    R.id.qty_editor_product,
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.qty_editor_product,
                    ConstraintSet.TOP,
                    R.id.item_addon_cart,
                    ConstraintSet.BOTTOM,
                    margin
                )
            }
            applyTo(binding.containerProductInformation)
        }
    }

    private fun adjustProductVerticalSeparatorConstraint(data: CartItemHolderData) {
        if (data.isError || !data.isBundlingItem) {
            binding.vBundlingProductSeparator.gone()
            return
        }
        binding.vBundlingProductSeparator.visible()
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(binding.containerProductInformation)

            // Top
            if (data.isMultipleBundleProduct) {
                if (data.bundlingItemPosition != BUNDLING_ITEM_HEADER) {
                    connect(
                        R.id.v_bundling_product_separator,
                        ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.TOP,
                        0
                    )
                } else {
                    connect(
                        R.id.v_bundling_product_separator,
                        ConstraintSet.TOP,
                        R.id.checkbox_bundle,
                        ConstraintSet.BOTTOM,
                        MARGIN_VERTICAL_SEPARATOR.dpToPx(itemView.resources.displayMetrics)
                    )
                }
            } else {
                connect(
                    R.id.v_bundling_product_separator,
                    ConstraintSet.TOP,
                    R.id.checkbox_bundle,
                    ConstraintSet.BOTTOM,
                    MARGIN_VERTICAL_SEPARATOR.dpToPx(itemView.resources.displayMetrics)
                )
            }

            // Bottom
            if (data.isMultipleBundleProduct) {
                if (data.bundlingItemPosition == BUNDLING_ITEM_FOOTER) {
                    connect(
                        R.id.v_bundling_product_separator,
                        ConstraintSet.BOTTOM,
                        R.id.iu_image_product,
                        ConstraintSet.BOTTOM,
                        0
                    )
                } else {
                    connect(
                        R.id.v_bundling_product_separator,
                        ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.BOTTOM,
                        0
                    )
                }
            } else {
                connect(
                    R.id.v_bundling_product_separator,
                    ConstraintSet.BOTTOM,
                    R.id.iu_image_product,
                    ConstraintSet.BOTTOM,
                    MARGIN_VERTICAL_SEPARATOR.dpToPx(itemView.resources.displayMetrics)
                )
            }

            applyTo(binding.containerProductInformation)
        }
    }

    private fun renderBundlingInfoDetail(data: CartItemHolderData) {
        with(binding) {
            textBundleTitle.text = data.bundleTitle
            textBundlePrice.text =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(data.bundlePrice, false)
                    .removeDecimalSuffix()

            if (data.bundleSlashPriceLabel.isNotBlank()) {
                labelBundleSlashPricePercentage.text = String.format(
                    itemView.context.getString(R.string.cart_label_discount_percentage),
                    data.bundleSlashPriceLabel
                )
                labelBundleSlashPricePercentage.show()
            } else {
                labelBundleSlashPricePercentage.gone()
            }

            if (data.bundleOriginalPrice > 0) {
                textBundleSlashPrice.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(data.bundleOriginalPrice, false)
                        .removeDecimalSuffix()
                textBundleSlashPrice.paintFlags =
                    binding.textBundleSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textBundleSlashPrice.show()
            } else {
                textBundleSlashPrice.gone()
            }

            if (data.isError) {
                textChangeBundle.gone()
            } else {
                textChangeBundle.show()
                textChangeBundle.setOnClickListener {
                    actionListener?.onEditBundleClicked(data)
                }
            }

//            if (data.bundleIconUrl.isNotBlank()) {
//                imageBundle.loadImage(data.bundleIconUrl)
//                imageBundle.show()
//            } else {
//                imageBundle.gone()
//            }
        }
    }

    private fun renderProductName(data: CartItemHolderData) {
        validateErrorView(binding.textProductName, data.isError)

        val marginTop = itemView.context.resources.getDimension(R.dimen.dp_2).toInt()
        if (data.isBundlingItem && !data.isMultipleBundleProduct && data.bundleLabelQuantity > 0) {
            val textProductNameLayoutParams =
                binding.textProductName.layoutParams as ViewGroup.MarginLayoutParams
            textProductNameLayoutParams.topMargin = marginTop
            val labelBundleQuantityText = String.format(
                itemView.context.getString(R.string.cart_label_product_name_with_quantity),
                data.bundleLabelQuantity,
                data.productName
            )
            binding.textProductName.text = Utils.getHtmlFormat(labelBundleQuantityText)
        } else {
            val textProductNameLayoutParams =
                binding.textProductName.layoutParams as ViewGroup.MarginLayoutParams
            textProductNameLayoutParams.topMargin = 0
            binding.textProductName.text = Utils.getHtmlFormat(data.productName)
        }
        binding.textProductName.setOnClickListener(
            getOnClickProductItemListener(
                absoluteAdapterPosition,
                data
            )
        )
    }

    private fun renderImage(data: CartItemHolderData) {
        data.productImage.let {
            binding.iuImageProduct.loadImage(it)
        }
        binding.iuImageProduct.setOnClickListener(
            getOnClickProductItemListener(
                bindingAdapterPosition,
                data
            )
        )
    }

    private fun renderProductAddOns(data: CartItemHolderData) {
        if (data.addOnsProduct.listData.isNotEmpty() && data.addOnsProduct.widget.wording.isNotEmpty()) {
            binding.itemAddonCart.apply {
                root.show()
                this.descAddon.text = MethodChecker.fromHtml(data.addOnsProduct.widget.wording)
                val addOnType = data.addOnsProduct.listData.firstOrNull()?.type ?: 0
                root.setOnClickListener {
                    actionListener?.onProductAddOnClicked(data)
                    actionListener?.onClickAddOnsProductWidgetCart(addOnType, data.productId)
                }
                if (data.addOnsProduct.listData.isNotEmpty()) {
                    actionListener?.onAddOnsProductWidgetImpression(addOnType, data.productId)
                }
            }
        } else {
            binding.itemAddonCart.root.gone()
        }
    }

    private fun sendAnalyticsInformationLabel(data: CartItemHolderData) {
        if (informationLabel.isNotEmpty()) {
            sendAnalyticsShowInformation(informationLabel, data.productId)
        }
    }

    private fun renderProductProperties(data: CartItemHolderData) {
        val layoutProductInfo = binding.layoutProductInfo
        layoutProductInfo.gone()

        if (data.needPrescription) {
            binding.iuPrescription.visible()
            binding.textPrescription.visible()
            binding.iuPrescription.loadIcon(data.butuhResepIconUrl)
            binding.textPrescription.text = data.butuhResepText
        } else {
            binding.iuPrescription.gone()
            binding.textPrescription.gone()
        }

        val productInformationList = data.productInformation
        if (productInformationList.isNotEmpty()) {
            binding.textProductInformation.visible()
            binding.textProductInformationSeparator.visible()

            productInformationList.getOrNull(0)?.let {
                var tmpLabel = it
                if (tmpLabel.lowercase(Locale.ROOT).contains(LABEL_CASHBACK)) {
                    tmpLabel = LABEL_CASHBACK
                }
                informationLabel.add(tmpLabel.lowercase(Locale.ROOT))

                val productInfo = createProductInfoText(it)
                layoutProductInfo.addView(productInfo)
            }
        } else {
            binding.textProductInformation.gone()
            binding.textProductInformationSeparator.gone()
        }
    }

    private fun createProductInfoText(it: String): Typography {
        return Typography(itemView.context).apply {
            setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                )
            )
            setType(Typography.DISPLAY_3)
            text = if (binding.layoutProductInfo.childCount > 0) ", $it" else it
        }
    }

    private fun sendAnalyticsShowInformation(informationList: List<String>, productId: String) {
        val informations = informationList.joinToString(", ")
        actionListener?.onCartItemShowInformationLabel(productId, informations)
    }

    private fun renderPrice(data: CartItemHolderData) {
        if (data.isBundlingItem && data.showBundlePrice) {
            binding.textProductPrice.gone()
        } else {
            binding.textProductPrice.visible()
        }
        validateErrorView(binding.textProductPrice, data.isError)

        if (data.wholesalePriceFormatted != null) {
            binding.textProductPrice.text = data.wholesalePriceFormatted
                ?: ""
        } else {
            binding.textProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                data.productPrice,
                false
            ).removeDecimalSuffix()
        }
    }

    private fun renderSlashPrice(data: CartItemHolderData) {
        if (data.isBundlingItem) {
            return
        }
        val hasPriceOriginal = data.productOriginalPrice > 0
        val hasWholesalePrice = data.wholesalePrice > 0
        val hasPriceDrop = data.productInitialPriceBeforeDrop > 0 &&
            data.productInitialPriceBeforeDrop > data.productPrice
        if (hasPriceOriginal || hasWholesalePrice || hasPriceDrop) {
            if (data.productSlashPriceLabel.isNotBlank()) {
                // Slash price
                renderSlashPriceFromCampaign(data)
            } else if (data.productInitialPriceBeforeDrop > 0) {
                val wholesalePrice = data.wholesalePrice
                if (wholesalePrice > 0 && wholesalePrice < data.productPrice) {
                    // Wholesale
                    renderSlashPriceFromWholesale(data)
                } else {
                    // Price drop
                    renderSlashPriceFromPriceDrop(data)
                }
            } else if (data.wholesalePrice > 0) {
                // Wholesale
                renderSlashPriceFromWholesale(data)
            }

            binding.textSlashPrice.paintFlags =
                binding.textSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.textSlashPrice.show()
        } else {
            binding.textSlashPrice.gone()
            binding.groupSlashPricePercentage.gone()
        }
    }

    private fun renderSlashPriceFromWholesale(data: CartItemHolderData) {
        val priceDropValue = data.productInitialPriceBeforeDrop
        val productPrice = data.productPrice
        val originalPrice = if (priceDropValue > productPrice) productPrice else priceDropValue
        binding.textSlashPrice.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(originalPrice, false)
                .removeDecimalSuffix()
    }

    private fun renderSlashPriceFromPriceDrop(data: CartItemHolderData) {
        binding.textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
            data.productInitialPriceBeforeDrop,
            false
        ).removeDecimalSuffix()
    }

    private fun renderSlashPriceFromCampaign(data: CartItemHolderData) {
        binding.textSlashPrice.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(data.productOriginalPrice, false)
                .removeDecimalSuffix()
        binding.labelSlashPricePercentage.text = data.productSlashPriceLabel
        binding.groupSlashPricePercentage.show()
        informationLabel.add(LABEL_DISCOUNT)
    }

    private fun renderQuantityLeft(data: CartItemHolderData) {
        if (data.productQtyLeft.isNotBlank()) {
            binding.textQtyLeft.text = data.productQtyLeft
            binding.textQtyLeft.show()
            actionListener?.onCartItemShowRemainingQty(data.productId)
        } else {
            binding.textQtyLeft.gone()
        }
    }

    private fun renderVariant(data: CartItemHolderData) {
        validateErrorView(binding.textProductVariant, data.isError)
        var paddingRight = 0
        val paddingTop = itemView.resources.getDimensionPixelOffset(R.dimen.dp_2)
        val textProductVariant = binding.textProductVariant
        if (data.variant.isNotBlank()) {
            textProductVariant.text = data.variant
            textProductVariant.show()
            paddingRight = itemView.resources.getDimensionPixelOffset(R.dimen.dp_4)
        } else {
            textProductVariant.gone()
        }
        textProductVariant.setPadding(0, paddingTop, paddingRight, 0)
    }

    private fun renderProductNotes(element: CartItemHolderData) {
        if (element.isError) return
        binding.buttonChangeNote.show()
        binding.buttonChangeNoteLottie.gone()
        binding.buttonChangeNote.setOnClickListener {
            actionListener?.onNoteClicked(
                element,
                binding.buttonChangeNote,
                binding.buttonChangeNoteLottie,
                absoluteAdapterPosition
            )
        }

        if (element.notes.isNotBlank()) {
            renderProductNotesFilled()
        } else {
            renderProductNotesEmpty()
        }
    }

    private fun renderProductNotesEmpty() {
        binding.buttonChangeNote.setImageResource(R.drawable.ic_cart_add_note)
    }

    private fun renderProductNotesFilled() {
        binding.buttonChangeNote.setImageResource(R.drawable.ic_cart_add_note_completed)
    }

    private fun renderQuantity(data: CartItemHolderData, viewHolderListener: ViewHolderListener?) {
        val qtyEditorProduct = binding.qtyEditorProduct
        if (data.isError) {
            binding.qtyEditorProduct.gone()
            return
        }
        qtyEditorProduct.autoHideKeyboard = true
        qtyEditorProduct.errorMessage.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        qtyEditorProduct.errorMessage.setType(Typography.DISPLAY_3)

        if (data.isAlreadyShowMinimumQuantityPurchasedError) {
            binding.labelQuantityError.text = String.format(
                itemView.context.getString(R.string.cart_min_quantity_error),
                data.minOrder
            )
            binding.labelQuantityError.visible()
        }

        if (data.isAlreadyShowMaximumQuantityPurchasedError) {
            binding.labelQuantityError.text = String.format(
                itemView.context.getString(R.string.cart_max_quantity_error),
                data.maxOrder
            )
            binding.labelQuantityError.visible()
        }

        if (!data.isAlreadyShowMinimumQuantityPurchasedError && !data.isAlreadyShowMaximumQuantityPurchasedError) {
            binding.labelQuantityError.gone()
        }

        if (qtyTextWatcher != null) {
            // reset listener
            qtyEditorProduct.editText.removeTextChangedListener(qtyTextWatcher)
        }
        qtyEditorProduct.minValue = 0
        qtyEditorProduct.maxValue = data.maxOrder
        if (data.isBundlingItem) {
            qtyEditorProduct.setValue(data.bundleQuantity)
        } else {
            qtyEditorProduct.setValue(data.quantity)
        }
        qtyTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                delayChangeQty?.cancel()
                delayChangeQty = GlobalScope.launch(Dispatchers.Main) {
                    val newValue = s.toString().replace(".", "").toIntOrZero()
                    lastQty = newValue
                    val minOrder = data.minOrder
                    if (newValue >= minOrder) {
                        delay(DEBOUNCE_TIME)
                    } else {
                        // Use longer delay for reset qty, to support automation
                        delay(RESET_QTY_DEBOUNCE_TIME)
                    }
                    val previousQuantity =
                        if (data.isBundlingItem) data.bundleQuantity else data.quantity
                    if (isActive && previousQuantity != newValue) {
                        if (!qtyEditorProduct.editText.isFocused) {
                            validateQty(newValue, data)
                            if (isActive && newValue != 0) {
                                actionListener?.onCartItemQuantityChanged(data, newValue)
                                handleRefreshType(data, viewHolderListener)
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        qtyEditorProduct.editText.addTextChangedListener(qtyTextWatcher)
        qtyEditorProduct.setSubstractListener {
            if (!data.isError && bindingAdapterPosition != RecyclerView.NO_POSITION) {
                val currentQuantity =
                    if (data.isBundlingItem) data.bundleQuantity else data.quantity
                if ((currentQuantity == 1 && data.minOrder == 1) || (currentQuantity == data.minOrder && data.isAlreadyShowMinimumQuantityPurchasedError)) {
                    delayChangeQty?.cancel()
                    actionListener?.onCartItemDeleteButtonClicked(data, false)
                }
                actionListener?.onCartItemQuantityMinusButtonClicked()
            }
        }
        qtyEditorProduct.setAddClickListener {
            if (!data.isError && bindingAdapterPosition != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemQuantityPlusButtonClicked()
            }
        }
        qtyEditorProduct.editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val qtyStr = (v as? AppCompatEditText)?.text?.toString() ?: ""
                actionListener?.onCartItemQuantityInputFormClicked(
                    if (!TextUtils.isEmpty(qtyStr)) qtyStr else ""
                )
            }
        }
        qtyEditorProduct.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardHandler.DropKeyboard(qtyEditorProduct.editText.context, itemView)
                if (qtyEditorProduct.editText.text.toString() == "0") {
                    actionListener?.onCartItemDeleteButtonClicked(data, true)
                }
                if (lastQty > data.maxOrder) {
                    binding.labelQuantityError.text = String.format(
                        itemView.context.getString(R.string.cart_max_quantity_error),
                        data.maxOrder
                    )
                    data.isAlreadyShowMaximumQuantityPurchasedError = true
                    binding.labelQuantityError.show()
                } else if (lastQty > data.minOrder && lastQty < data.maxOrder) {
                    data.isAlreadyShowMaximumQuantityPurchasedError = false
                    binding.labelQuantityError.gone()
                }
                true
            } else {
                false
            }
        }
        qtyEditorProduct.editText.imeOptions = EditorInfo.IME_ACTION_DONE
        qtyEditorProduct.editText.isEnabled = data.isError == false
    }

    private fun validateQty(newValue: Int, element: CartItemHolderData) {
        val qtyEditorCart = binding.qtyEditorProduct
        if (newValue > element.minOrder) {
            element.isAlreadyShowMinimumQuantityPurchasedError = false
        }
        if (newValue < element.maxOrder) {
            element.isAlreadyShowMaximumQuantityPurchasedError = false
        }
        if (newValue < element.minOrder) {
            if (element.minOrder <= 1) {
                actionListener?.onCartItemDeleteButtonClicked(element, false)
                return
            }
            binding.labelQuantityError.show()
            binding.labelQuantityError.text = String.format(
                itemView.context.getString(R.string.cart_min_quantity_error),
                element.minOrder
            )
            if (!element.isAlreadyShowMinimumQuantityPurchasedError) {
                qtyEditorCart.setValue(element.minOrder)
                element.isAlreadyShowMinimumQuantityPurchasedError = true
            } else {
                element.isAlreadyShowMinimumQuantityPurchasedError = false
                actionListener?.onCartItemDeleteButtonClicked(element, false)
            }
        }
        qtyEditorCart.addButton.isEnabled = true
        qtyEditorCart.subtractButton.isEnabled = true
    }

    private fun handleRefreshType(
        data: CartItemHolderData,
        viewHolderListener: ViewHolderListener?
    ) {
        if (data.wholesalePriceData.isNotEmpty()) {
            viewHolderListener?.onNeedToRefreshSingleShop(data, bindingAdapterPosition)
        } else if (data.shouldValidateWeight) {
            viewHolderListener?.onNeedToRefreshWeight(data)
            viewHolderListener?.onNeedToRefreshSingleProduct(bindingAdapterPosition)
        } else if (data.shouldCheckBoAffordability) {
            viewHolderListener?.onNeedToRefreshBoAffordability(data)
            viewHolderListener?.onNeedToRefreshSingleProduct(bindingAdapterPosition)
        } else {
            viewHolderListener?.onNeedToRefreshSingleProduct(bindingAdapterPosition)
        }
    }

    private fun renderActionWishlist(data: CartItemHolderData) {
        if (data.isError) {
            return
        }
        if (data.isWishlisted) {
            val inWishlistColor = ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_RN500
            )
            binding.buttonToggleWishlist.setImage(
                IconUnify.HEART_FILLED,
                inWishlistColor,
                inWishlistColor,
                inWishlistColor,
                inWishlistColor
            )
        } else {
            val notInWishlistColor = ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN500
            )
            binding.buttonToggleWishlist.setImage(
                IconUnify.HEART,
                notInWishlistColor,
                notInWishlistColor,
                notInWishlistColor,
                notInWishlistColor
            )
        }
        binding.buttonToggleWishlist.setOnClickListener {
            delayChangeWishlistStatus?.cancel()
            delayChangeWishlistStatus = GlobalScope.launch(Dispatchers.Main) {
                delay(DEBOUNCE_TIME)
                data.isWishlisted = !data.isWishlisted
                actionListener?.onWishlistCheckChanged(
                    data,
                    binding.buttonToggleWishlist,
                    binding.ivAnimatedWishlist,
                    absoluteAdapterPosition
                )
            }
        }
        binding.buttonToggleWishlist.show()
    }

    private fun getOnClickProductItemListener(
        @SuppressLint("RecyclerView") position: Int,
        data: CartItemHolderData
    ): View.OnClickListener {
        return View.OnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemProductClicked(data)
            }
        }
    }

    private fun renderActionCheckoutInBrowser(action: Action, data: CartItemHolderData) {
        binding.textProductUnavailableAction.apply {
            text = action.message
            setOnClickListener {
                if (data.selectedUnavailableActionLink.isNotBlank()) {
                    actionListener?.onTobaccoLiteUrlClicked(
                        data.selectedUnavailableActionLink,
                        data,
                        action
                    )
                }
            }
            setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                )
            )
            actionListener?.onShowTickerTobacco()
            show()
        }
    }

    private fun renderActionSimilarProduct(action: Action, data: CartItemHolderData) {
        binding.textProductUnavailableAction.apply {
            text = action.message
            setOnClickListener {
                if (data.selectedUnavailableActionLink.isNotBlank()) {
                    actionListener?.onSimilarProductUrlClicked(data)
                }
            }
            actionListener?.onShowActionSeeOtherProduct(data.productId, data.errorType)
            show()
        }
    }

    private fun renderFollowShop(action: Action, data: CartItemHolderData) {
        binding.textProductUnavailableAction.apply {
            text = action.message
            setOnClickListener {
                if (data.shopHolderData.shopId.isNotEmpty()) {
                    actionListener?.onFollowShopClicked(data.shopHolderData.shopId, data.errorType)
                }
            }
            setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
            )
            show()
        }
    }

    private fun renderVerification(action: Action, data: CartItemHolderData) {
        binding.textProductUnavailableAction.apply {
            text = action.message
            setOnClickListener {
                if (data.selectedUnavailableActionLink.isNotEmpty()) {
                    actionListener?.onVerificationClicked(data.selectedUnavailableActionLink)
                }
            }
            setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                )
            )
            show()
        }
    }

    private fun renderAlpha(cartItemHolderData: CartItemHolderData) {
        with(binding) {
            if (cartItemHolderData.isError) {
                productBundlingInfo.alpha = ALPHA_HALF
            } else {
                productBundlingInfo.alpha = ALPHA_FULL
            }
        }
    }

    private fun renderContainer(cartItemHolderData: CartItemHolderData) {
        val layoutParams =
            binding.containerProductInformation.layoutParams as ViewGroup.MarginLayoutParams
        val layoutParamsIuImageProduct =
            binding.iuImageProduct.layoutParams as ViewGroup.MarginLayoutParams
        if (cartItemHolderData.isError) {
            layoutParamsIuImageProduct.topMargin = 0
            layoutParams.bottomMargin =
                PRODUCT_ACTION_MARGIN.dpToPx(itemView.resources.displayMetrics)
        } else {
            if (cartItemHolderData.isBundlingItem && cartItemHolderData.isMultipleBundleProduct) {
                if (cartItemHolderData.bundlingItemPosition != BUNDLING_ITEM_HEADER) {
                    layoutParamsIuImageProduct.topMargin = IMAGE_PRODUCT_MARGIN_START.dpToPx(itemView.resources.displayMetrics)
                } else {
                    layoutParamsIuImageProduct.topMargin = 0
                }

                if (cartItemHolderData.bundlingItemPosition == BUNDLING_ITEM_FOOTER) {
                    layoutParams.bottomMargin = PRODUCT_ACTION_MARGIN.dpToPx(itemView.resources.displayMetrics)
                } else {
                    layoutParams.bottomMargin = 0
                }
            } else {
                layoutParamsIuImageProduct.topMargin = 0
                layoutParams.bottomMargin =
                    PRODUCT_ACTION_MARGIN.dpToPx(itemView.resources.displayMetrics)
            }
        }
    }

    private fun renderDivider(cartItemHolderData: CartItemHolderData) {
        if (cartItemHolderData.showErrorBottomDivider) {
            binding.bottomDivider.layoutParams.height =
                DEFAULT_DIVIDER_HEIGHT.dpToPx(itemView.resources.displayMetrics)
            val layoutParams = binding.bottomDivider.layoutParams as ViewGroup.MarginLayoutParams
            if (cartItemHolderData.shouldDivideHalfErrorBottomDivider) {
                layoutParams.marginStart =
                    BOTTOM_DIVIDER_MARGIN_START.dpToPx(itemView.resources.displayMetrics)
            } else {
                layoutParams.marginStart = 0
            }
            binding.bottomDivider.visible()
        } else {
            binding.bottomDivider.gone()
        }
    }

    private fun validateErrorView(typography: Typography, isError: Boolean) {
        if (isError) {
            typography.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        } else {
            typography.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                )
            )
        }
    }

    fun getItemViewBinding(): ItemCartProductRevampBinding {
        return binding
    }

    interface ViewHolderListener {

        fun onNeedToRefreshSingleProduct(childPosition: Int)

        fun onNeedToRefreshSingleShop(cartItemHolderData: CartItemHolderData, itemPosition: Int)

        fun onNeedToRefreshWeight(cartItemHolderData: CartItemHolderData)

        fun onNeedToRefreshBoAffordability(cartItemHolderData: CartItemHolderData)
    }

    companion object {
        val TYPE_VIEW_ITEM_CART = R.layout.item_cart_product

        const val LABEL_CASHBACK = "cashback"
        const val LABEL_DISCOUNT = "label diskon"

        private const val DEBOUNCE_TIME = 500L
        private const val RESET_QTY_DEBOUNCE_TIME = 1000L
        const val ALPHA_HALF = 0.5f
        const val ALPHA_FULL = 1.0f

        private const val DEFAULT_DIVIDER_HEIGHT = 2
        private const val IMAGE_PRODUCT_MARGIN_START_6 = 6
        private const val MARGIN_VERTICAL_SEPARATOR = 8
        private const val WISHLIST_ANIMATED_MARGIN_TOP = 13
        private const val IMAGE_PRODUCT_MARGIN_START = 12
        private const val PRODUCT_ACTION_MARGIN = 16
        private const val BUNDLING_SEPARATOR_MARGIN_START = 38
        private const val BOTTOM_DIVIDER_MARGIN_START = 114

        private const val CART_MAIN_COACH_MARK = "cart_main_coach_mark"
    }
}
