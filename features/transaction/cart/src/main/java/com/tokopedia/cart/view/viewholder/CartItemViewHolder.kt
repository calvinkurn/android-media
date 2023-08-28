package com.tokopedia.cart.view.viewholder

import android.annotation.SuppressLint
import android.graphics.Paint
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ProductInformationWithIcon
import com.tokopedia.cart.databinding.ItemAddonCartIdentifierBinding
import com.tokopedia.cart.databinding.ItemCartProductBinding
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.purchase_platform.common.utils.showSoftKeyboard
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
    private val binding: ItemCartProductBinding,
    private var actionListener: CartItemAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    private var viewHolderListener: ViewHolderListener? = null

    private var dataSize: Int = 0
    private var delayChangeCheckboxState: Job? = null
    private var delayChangeQty: Job? = null
    private var informationLabel: MutableList<String> = mutableListOf()
    private var qtyTextWatcher: TextWatcher? = null

    init {
        setNoteTouchListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setNoteTouchListener() {
        binding.textFieldNotes.setOnTouchListener { view, event ->
            if (view.id == R.id.text_field_notes) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
    }

    fun clear() {
        actionListener = null
        viewHolderListener = null
        delayChangeCheckboxState?.cancel()
        delayChangeQty?.cancel()
        qtyTextWatcher = null
    }

    fun bindData(data: CartItemHolderData, viewHolderListener: ViewHolderListener?, dataSize: Int) {
        this.viewHolderListener = viewHolderListener
        this.dataSize = dataSize

        renderAlpha(data)
        renderShopInfo(data)
        renderProductInfo(data)
        renderLeftAnchor(data)
        renderQuantity(data, viewHolderListener)
        renderProductAction(data)
    }

    private fun renderLeftAnchor(data: CartItemHolderData) {
        if (data.isBundlingItem) {
            with(binding) {
                checkboxProduct.gone()
                vBundlingProductSeparator.show()
                val marginStart =
                    IMAGE_PRODUCT_MARGIN_START.dpToPx(itemView.resources.displayMetrics)
                val marginTop = itemView.context.resources.getDimension(com.tokopedia.cart.R.dimen.dp_4).toInt()
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProductInformation)
                constraintSet.connect(
                    R.id.iu_image_product,
                    ConstraintSet.START,
                    R.id.v_bundling_product_separator,
                    ConstraintSet.END,
                    marginStart
                )
                constraintSet.connect(
                    R.id.iu_image_product,
                    ConstraintSet.TOP,
                    R.id.container_product_information,
                    ConstraintSet.TOP,
                    marginTop
                )
                constraintSet.applyTo(containerProductInformation)
                renderCheckBoxBundle(data)

                val params = vBundlingProductSeparator.layoutParams as ViewGroup.MarginLayoutParams
                if (data.isError) {
                    params.leftMargin = 0
                } else {
                    params.leftMargin =
                        BUNDLING_SEPARATOR_MARGIN_START.dpToPx(itemView.resources.displayMetrics)
                }

                val textFieldNotesParams =
                    textFieldNotes.layoutParams as ViewGroup.MarginLayoutParams
                textFieldNotesParams.leftMargin = marginStart
                val textNotesParams = textNotes.layoutParams as ViewGroup.MarginLayoutParams
                textNotesParams.leftMargin = marginStart
                val textNotesFilledParams =
                    textNotesFilled.layoutParams as ViewGroup.MarginLayoutParams
                textNotesFilledParams.leftMargin = marginStart
            }
        } else {
            with(binding) {
                vBundlingProductSeparator.gone()
                checkboxProduct.show()
                val marginStart =
                    IMAGE_PRODUCT_MARGIN_START.dpToPx(itemView.resources.displayMetrics)
                val marginTop = itemView.context.resources.getDimension(com.tokopedia.cart.R.dimen.dp_4).toInt()
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
                    ConstraintSet.TOP,
                    marginTop
                )
                constraintSet.applyTo(containerProductInformation)
                renderCheckBoxProduct(data)

                val textFieldNotesParams =
                    textFieldNotes.layoutParams as ViewGroup.MarginLayoutParams
                textFieldNotesParams.leftMargin = 0
                val textNotesParams = textNotes.layoutParams as ViewGroup.MarginLayoutParams
                textNotesParams.leftMargin = 0
                val textNotesFilledParams =
                    textNotesFilled.layoutParams as ViewGroup.MarginLayoutParams
                textNotesFilledParams.leftMargin = 0
            }
        }
    }

    private fun renderProductAction(data: CartItemHolderData) {
        with(binding) {
            textNotes.gone()
            textNotesFilled.gone()
            textNotesChange.gone()
            textFieldNotes.gone()
            textMoveToWishlist.gone()
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
                        if (data.isBundlingItem) {
                            binding.textMoveToWishlist.gone()
                        } else {
                            renderActionWishlist(it, data)
                        }
                    }
                    Action.ACTION_CHECKOUTBROWSER, Action.ACTION_SIMILARPRODUCT, Action.ACTION_FOLLOWSHOP, Action.ACTION_VERIFICATION -> {
                        when {
                            data.selectedUnavailableActionId == Action.ACTION_CHECKOUTBROWSER && it.id == Action.ACTION_CHECKOUTBROWSER -> {
                                renderActionCheckoutInBrowser(it, data)
                            }
                            data.selectedUnavailableActionId == Action.ACTION_SIMILARPRODUCT && it.id == Action.ACTION_SIMILARPRODUCT -> {
                                renderActionSimilarProduct(it, data)
                            }
                            data.selectedUnavailableActionId == Action.ACTION_FOLLOWSHOP && it.id == Action.ACTION_FOLLOWSHOP -> {
                                renderFollowShop(it, data)
                            }
                            data.selectedUnavailableActionId == Action.ACTION_VERIFICATION && it.id == Action.ACTION_VERIFICATION -> {
                                renderVerification(it, data)
                            }
                        }
                    }
                    Action.ACTION_DELETE -> {
                        renderActionDelete(data)
                    }
                }
            }
        }
    }

    private fun renderActionDelete(data: CartItemHolderData) {
        adjustButtonDeleteConstraint(data)
        binding.buttonDeleteCart.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemDeleteButtonClicked(data)
            }
        }
        binding.buttonDeleteCart.show()
    }

    private fun adjustButtonDeleteConstraint(data: CartItemHolderData) {
        with(binding) {
            if (data.isError) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProductAction)
                constraintSet.connect(
                    R.id.button_delete_cart,
                    ConstraintSet.END,
                    R.id.text_product_unavailable_action,
                    ConstraintSet.START,
                    0
                )
                constraintSet.applyTo(containerProductAction)
            } else {
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProductAction)
                constraintSet.connect(
                    R.id.button_delete_cart,
                    ConstraintSet.END,
                    R.id.qty_editor_product,
                    ConstraintSet.START,
                    itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)
                        .toInt()
                )
                constraintSet.applyTo(containerProductAction)
            }
        }
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
                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                actionListener?.onCartItemCheckChanged(adapterPosition, data)
                                viewHolderListener?.onNeedToRefreshSingleShop(data, adapterPosition)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderCheckBoxBundle(data: CartItemHolderData) {
        val checkboxBundle = binding.checkboxBundle
        if (data.isError) {
            checkboxBundle.gone()
            val padding16 = itemView.resources.getDimensionPixelSize(R.dimen.dp_16)
            binding.productBundlingInfo.setPadding(padding16, 0, 0, 0)
            return
        }
        binding.productBundlingInfo.setPadding(0, 0, 0, 0)
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
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            actionListener?.onBundleItemCheckChanged(data)
                            viewHolderListener?.onNeedToRefreshSingleShop(data, adapterPosition)
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
                ImageHandler.loadImageWithoutPlaceholder(binding.imageShopBadge, shopHolderData.shopTypeInfo.shopBadge)
                binding.imageShopBadge.contentDescription = itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type, shopHolderData.shopTypeInfo.title)
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
        renderProductPropertyIncidentLabel(data)
        renderProductActionSection(data)
        renderProductAddOns(data)
        sendAnalyticsInformationLabel(data)
    }

    private fun renderBundlingInfo(data: CartItemHolderData) {
        if (data.isBundlingItem && data.bundlingItemPosition == CartItemHolderData.BUNDLING_ITEM_HEADER) {
            binding.productBundlingInfo.show()
            renderBundlingInfoDetail(data)
        } else {
            binding.productBundlingInfo.gone()
        }

        if (data.isBundlingItem && !data.isMultipleBundleProduct && data.bundleLabelQuantity > 0) {
            val labelBundleQuantityText = String.format(
                itemView.context.getString(R.string.label_cart_bundle_product_quantity),
                data.bundleLabelQuantity
            )
            binding.labelBundleQuantity.text = labelBundleQuantityText
            binding.labelBundleQuantity.show()
        } else {
            binding.labelBundleQuantity.gone()
        }
    }

    private fun renderProductActionSection(data: CartItemHolderData) {
        if (data.isBundlingItem) {
            if (data.isMultipleBundleProduct && (data.bundlingItemPosition == CartItemHolderData.BUNDLING_ITEM_HEADER || data.bundlingItemPosition == CartItemHolderData.BUNDLING_ITEM_DEFAULT)) {
                binding.containerProductAction.gone()
                binding.holderItemCartDivider.gone()
            } else {
                binding.containerProductAction.show()
                binding.holderItemCartDivider.visibility =
                    if (data.isFinalItem) View.GONE else View.VISIBLE
            }
        } else {
            binding.containerProductAction.show()
            binding.holderItemCartDivider.visibility =
                if (data.isFinalItem) View.GONE else View.VISIBLE
        }
    }

    private fun renderBundlingInfoDetail(data: CartItemHolderData) {
        with(binding) {
            textBundleTitle.text = data.bundleTitle
            textBundlePrice.text =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(data.bundlePrice, false)
                    .removeDecimalSuffix()

            if (data.bundleSlashPriceLabel.isNotBlank()) {
                labelBundleSlashPricePercentage.text = data.bundleSlashPriceLabel
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

            if (data.bundleIconUrl.isNotBlank()) {
                imageBundle.loadImage(data.bundleIconUrl)
                imageBundle.show()
            } else {
                imageBundle.gone()
            }
        }
    }

    private fun renderProductName(data: CartItemHolderData) {
        binding.textProductName.text = Utils.getHtmlFormat(data.productName)
        binding.textProductName.setOnClickListener(
            getOnClickProductItemListener(
                adapterPosition,
                data
            )
        )
        val marginTop = itemView.context.resources.getDimension(R.dimen.dp_2).toInt()
        if (data.isBundlingItem && !data.isMultipleBundleProduct && data.bundleLabelQuantity > 0) {
            val textProductNameLayoutParams =
                binding.textProductName.layoutParams as ViewGroup.MarginLayoutParams
            textProductNameLayoutParams.topMargin = marginTop
        } else {
            val textProductNameLayoutParams =
                binding.textProductName.layoutParams as ViewGroup.MarginLayoutParams
            textProductNameLayoutParams.topMargin = 0
        }
    }

    private fun renderImage(data: CartItemHolderData) {
        data.productImage.let {
            binding.iuImageProduct.loadImage(it)
        }
        binding.iuImageProduct.setOnClickListener(
            getOnClickProductItemListener(
                adapterPosition,
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

        var isProductInformationExist = false

        val productInformationWithIcon = data.productInformationWithIcon
        if (productInformationWithIcon.isNotEmpty()) {
            isProductInformationExist = true
            layoutProductInfo.removeAllViews()
            productInformationWithIcon.forEach {
                val productInfoWithIcon = createProductInfoTextWithIcon(it)
                layoutProductInfo.addView(productInfoWithIcon)
            }
            layoutProductInfo.show()
        }
        if (data.needPrescription) {
            if (!isProductInformationExist) {
                layoutProductInfo.removeAllViews()
            }
            isProductInformationExist = true
            val needPrescriptionView = createProductInfoTextWithIcon(
                ProductInformationWithIcon(
                    data.butuhResepText,
                    data.butuhResepIconUrl
                )
            )
            if (layoutProductInfo.childCount > 0) {
                needPrescriptionView.setPadding(
                    itemView.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_4),
                    0,
                    0,
                    0
                )
            }
            layoutProductInfo.addView(needPrescriptionView)
            layoutProductInfo.show()
        }

        val productInformationList = data.productInformation
        if (productInformationList.isNotEmpty()) {
            if (!isProductInformationExist) {
                layoutProductInfo.removeAllViews()
            }
            isProductInformationExist = true
            productInformationList.forEach {
                var tmpLabel = it
                if (tmpLabel.lowercase(Locale.ROOT).contains(LABEL_CASHBACK)) {
                    tmpLabel = LABEL_CASHBACK
                }
                informationLabel.add(tmpLabel.lowercase(Locale.ROOT))

                val productInfo = createProductInfoText(it)
                layoutProductInfo.addView(productInfo)
            }
            layoutProductInfo.show()
        }

        if (data.wholesalePrice > 0) {
            if (!isProductInformationExist) {
                layoutProductInfo.removeAllViews()
            }
            val wholesaleLabel = itemView.context.getString(R.string.label_wholesale_product)
            val productInfo = createProductInfoText(wholesaleLabel)
            layoutProductInfo.addView(productInfo)
            layoutProductInfo.show()
            informationLabel.add(wholesaleLabel.lowercase(Locale.ROOT))
        }
    }

    private fun createProductInfoText(it: String): Typography {
        return Typography(itemView.context).apply {
            setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                )
            )
            setType(Typography.BODY_3)
            text = if (binding.layoutProductInfo.childCount > 0) ", $it" else it
        }
    }

    private fun createProductInfoTextWithIcon(dataProduct: ProductInformationWithIcon): LinearLayout {
        return LinearLayout(itemView.context).apply {
            orientation = LinearLayout.HORIZONTAL
            val identifierBinding =
                ItemAddonCartIdentifierBinding.inflate(LayoutInflater.from(itemView.context))
            identifierBinding.run {
                ivIdentifier.loadIcon(dataProduct.iconUrl)
                labelIdentifier.text = dataProduct.text
            }
            this.addView(identifierBinding.root)
        }
    }

    private fun sendAnalyticsShowInformation(informationList: List<String>, productId: String) {
        val informations = informationList.joinToString(", ")
        actionListener?.onCartItemShowInformationLabel(productId, informations)
    }

    private fun renderProductPropertyIncidentLabel(data: CartItemHolderData) {
        if (data.productAlertMessage.isNotEmpty()) {
            binding.textIncident.text = data.productAlertMessage
            binding.textIncident.show()
        } else {
            binding.textIncident.gone()
        }
    }

    private fun renderPrice(data: CartItemHolderData) {
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
                if (wholesalePrice > 0 && wholesalePrice.toDouble() < data.productPrice) {
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
            binding.labelSlashPricePercentage.gone()
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
        binding.labelSlashPricePercentage.show()
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
        var paddingRight = 0
        val paddingTop = itemView.resources.getDimensionPixelOffset(R.dimen.dp_2)
        val textProductVariant = binding.textProductVariant
        if (data.variant.isNotBlank()) {
            textProductVariant.text = data.variant
            textProductVariant.show()
            paddingRight = itemView.resources.getDimensionPixelOffset(com.tokopedia.cart.R.dimen.dp_4)
        } else {
            if (data.productQtyLeft.isNotBlank()) {
                textProductVariant.text = ""
                textProductVariant.invisible()
            } else {
                textProductVariant.gone()
            }
        }
        textProductVariant.setPadding(0, paddingTop, paddingRight, 0)
    }

    private fun renderProductNotes(element: CartItemHolderData) {
        with(binding) {
            textNotes.setOnClickListener {
                renderProductNotesEditable(element)
            }
            textNotesChange.setOnClickListener {
                renderProductNotesEditable(element)
            }

            if (element.notes.isNotBlank()) {
                renderProductNotesFilled(element)
            } else {
                renderProductNotesEmpty(element)
            }
        }
    }

    private fun renderProductNotesEditable(element: CartItemHolderData) {
        with(binding) {
            textFieldNotes.editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            textFieldNotes.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            textFieldNotes.editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
            textFieldNotes.setPlaceholder(Utils.getHtmlFormat(element.placeholderNote))
            textFieldNotes.context?.let {
                textFieldNotes.editText.setOnEditorActionListener { v, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        KeyboardHandler.DropKeyboard(it, v)
                        textFieldNotes.editText.clearFocus()
                        if (element.notes.isNotBlank()) {
                            renderProductNotesFilled(element)
                        } else {
                            renderProductNotesEmpty(element)
                        }
                        true
                    } else {
                        false
                    }
                }
            }

            textNotes.gone()
            textFieldNotes.show()
            textNotesChange.gone()
            textNotesFilled.gone()
            textNotesFilled.text = Utils.getHtmlFormat(element.notes)
            textFieldNotes.setCounter(element.maxNotesLength)
            textFieldNotes.editText.setText(Utils.getHtmlFormat(element.notes))
            textFieldNotes.editText.setSelection(textFieldNotes.editText.length())
            textFieldNotes.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val notes = s.toString()
                    element.notes = notes
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            textFieldNotes.editText.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    KeyboardHandler.DropKeyboard(v.context, v)
                }
            }
            textFieldNotes.editText.requestFocus()
            showSoftKeyboard(textFieldNotes.context, textFieldNotes.editText)
        }
    }

    private fun renderProductNotesEmpty(element: CartItemHolderData) {
        with(binding) {
            textNotes.show()
            textNotesFilled.gone()
            textNotesChange.gone()
            textFieldNotes.gone()
        }
    }

    private fun renderProductNotesFilled(element: CartItemHolderData) {
        with(binding) {
            textFieldNotes.gone()
            textNotesFilled.text = Utils.getHtmlFormat(element.notes)
            setProductNotesWidth(element)
            textNotesFilled.show()
            textNotesChange.show()
            textNotes.gone()
        }
    }

    private fun setProductNotesWidth(data: CartItemHolderData) {
        with(binding) {
            val paddingParent = itemView.resources.getDimensionPixelSize(R.dimen.dp_16) * 2
            val textNotesChangeWidth =
                TEXT_NOTES_CHANGE_WIDTH.dpToPx(itemView.resources.displayMetrics)
            val paddingLeftTextNotesChange = itemView.resources.getDimensionPixelSize(com.tokopedia.cart.R.dimen.dp_4)
            val screenWidth = getScreenWidth()
            var maxNotesWidth =
                screenWidth - paddingParent - paddingLeftTextNotesChange - textNotesChangeWidth
            if (data.isBundlingItem) {
                val bundlingSeparatorMargin =
                    BUNDLING_SEPARATOR_WIDTH.dpToPx(itemView.resources.displayMetrics)
                maxNotesWidth -= bundlingSeparatorMargin
            }

            textNotesFilled.measure(0, 0)
            val currentWidth = textNotesFilled.measuredWidth
            if (currentWidth >= maxNotesWidth) {
                textNotesFilled.layoutParams?.width = maxNotesWidth
                textNotesFilled.requestLayout()
            } else {
                textNotesFilled.layoutParams?.width = currentWidth
                textNotesFilled.requestLayout()
            }
        }
    }

    private fun renderQuantity(data: CartItemHolderData, viewHolderListener: ViewHolderListener?) {
        val qtyEditorProduct = binding.qtyEditorProduct
        if (data.isError) {
            qtyEditorProduct.gone()
            return
        }
        qtyEditorProduct.show()
        qtyEditorProduct.autoHideKeyboard = true
        if (qtyTextWatcher != null) {
            // reset listener
            qtyEditorProduct.editText.removeTextChangedListener(qtyTextWatcher)
        }
        qtyEditorProduct.minValue = data.minOrder
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
                        validateQty(newValue, data)
                        if (isActive && newValue != 0) {
                            actionListener?.onCartItemQuantityChanged(data, newValue)
                            handleRefreshType(data, viewHolderListener)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        qtyEditorProduct.editText.addTextChangedListener(qtyTextWatcher)
        qtyEditorProduct.setSubstractListener {
            if (!data.isError && adapterPosition != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemQuantityMinusButtonClicked()
            }
        }
        qtyEditorProduct.setAddClickListener {
            if (!data.isError && adapterPosition != RecyclerView.NO_POSITION) {
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
        if (newValue == element.minOrder && newValue == element.maxOrder) {
            qtyEditorCart.addButton.isEnabled = false
            qtyEditorCart.subtractButton.isEnabled = false
        } else if (newValue >= element.maxOrder) {
            if (newValue > element.maxOrder) {
                qtyEditorCart.setValue(element.maxOrder)
            }
            qtyEditorCart.addButton.isEnabled = false
            qtyEditorCart.subtractButton.isEnabled = true
        } else if (newValue <= element.minOrder) {
            if (newValue < element.minOrder) {
                qtyEditorCart.setValue(element.minOrder)
            }
            qtyEditorCart.addButton.isEnabled = true
            qtyEditorCart.subtractButton.isEnabled = false
        } else {
            qtyEditorCart.addButton.isEnabled = true
            qtyEditorCart.subtractButton.isEnabled = true
        }
    }

    private fun handleRefreshType(
        data: CartItemHolderData,
        viewHolderListener: ViewHolderListener?
    ) {
        if (data.wholesalePriceData.isNotEmpty()) {
            viewHolderListener?.onNeedToRefreshSingleShop(data, adapterPosition)
        } else if (data.shouldValidateWeight) {
            viewHolderListener?.onNeedToRefreshWeight(data)
            viewHolderListener?.onNeedToRefreshSingleProduct(adapterPosition)
        } else if (data.shouldCheckBoAffordability) {
            viewHolderListener?.onNeedToRefreshBoAffordability(data)
            viewHolderListener?.onNeedToRefreshSingleProduct(adapterPosition)
        } else {
            viewHolderListener?.onNeedToRefreshSingleProduct(adapterPosition)
        }
    }

    private fun renderActionWishlist(action: Action, data: CartItemHolderData) {
        val textMoveToWishlist = binding.textMoveToWishlist
        if (data.isWishlisted && action.id == Action.ACTION_WISHLISTED) {
            textMoveToWishlist.text = action.message
            textMoveToWishlist.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_44
                )
            )
            textMoveToWishlist.setOnClickListener { }
        } else if (!data.isWishlisted && action.id == Action.ACTION_WISHLIST) {
            textMoveToWishlist.text = action.message
            textMoveToWishlist.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                )
            )
            textMoveToWishlist.setOnClickListener {
                actionListener?.onWishlistCheckChanged(
                    data.productId,
                    data.cartId,
                    binding.iuImageProduct,
                    data.isError,
                    data.errorType
                )
            }
        }
        textMoveToWishlist.show()
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
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
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
            setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                )
            )
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
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                )
            )
            show()
        }
    }

    private fun renderAlpha(cartItemHolderData: CartItemHolderData) {
        with(binding) {
            if (cartItemHolderData.isError) {
                productBundlingInfo.alpha = ALPHA_HALF
                containerProductInformation.alpha = ALPHA_HALF
            } else {
                productBundlingInfo.alpha = ALPHA_FULL
                containerProductInformation.alpha = ALPHA_FULL
            }
        }
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

        private const val IMAGE_PRODUCT_MARGIN_START = 12
        private const val TEXT_NOTES_CHANGE_WIDTH = 40
        private const val BUNDLING_SEPARATOR_MARGIN_START = 32
        private const val BUNDLING_SEPARATOR_WIDTH = 48
    }
}
