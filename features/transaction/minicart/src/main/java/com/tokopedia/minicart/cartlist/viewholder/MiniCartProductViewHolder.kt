package com.tokopedia.minicart.cartlist.viewholder

import android.graphics.Paint
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.Action
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_DELETE
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_NOTES
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_SIMILARPRODUCT
import com.tokopedia.minicart.databinding.ItemMiniCartProductBinding
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

class MiniCartProductViewHolder(
    private val viewBinding: ItemMiniCartProductBinding,
    private val listener: MiniCartListActionListener
) :
    AbstractViewHolder<MiniCartProductUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_product
        const val NOTES_CHANGE_DELAY = 250L
        const val QUANTITY_CHANGE_DELAY = 500L
        const val QUANTITY_RESET_DELAY = 1000L
        const val ALPHA_FULL = 1.0f
        const val ALPHA_HALF = 0.5f
    }

    private var qtyTextWatcher: TextWatcher? = null
    private var delayChangeQty: Job? = null
    private var delayChangeNotes: Job? = null

    override fun onViewRecycled() {
        super.onViewRecycled()
        delayChangeQty?.cancel()
        delayChangeNotes?.cancel()
        if (qtyTextWatcher != null) {
            viewBinding.qtyEditorProduct.editText.removeTextChangedListener(qtyTextWatcher)
            qtyTextWatcher = null
        }
    }

    override fun bind(element: MiniCartProductUiModel) {
        renderDefaultState()
        renderProductImage(element)
        renderProductName(element)
        renderProductVariant(element)
        renderProductQtyLeft(element)
        renderProductPrice(element)
        renderProductInformation(element)
        renderProductQty(element)
        renderProductAction(element)
        renderProductAlpha(element)
        renderBundleHeader(element)
        renderBundleQuantity(element)
        renderVerticalLine(element)
        renderBottomDivider(element)
    }

    private fun renderDefaultState() {
        with(viewBinding) {
            textNotes.gone()
            textProductUnavailableAction.gone()
        }
    }

    private fun renderProductVariant(element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (element.productVariantName.isNotBlank()) {
                textProductVariant.text = element.productVariantName
                textProductVariant.show()
            } else {
                if (element.productQtyLeft.isNotBlank()) {
                    textProductVariant.invisible()
                } else {
                    textProductVariant.gone()
                }
            }
        }
    }

    private fun renderProductName(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textProductName.text = Utils.getHtmlFormat(element.productName)
            textProductName.setOnClickListener(productInfoClickListener(element))
        }
    }

    private fun productInfoClickListener(element: MiniCartProductUiModel): View.OnClickListener {
        return View.OnClickListener {
            listener.onProductInfoClicked(element)
        }
    }

    private fun renderProductQtyLeft(element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (!element.isProductDisabled && element.productQtyLeft.isNotBlank()) {
                textQtyLeft.text = element.productQtyLeft
                textQtyLeft.show()
                if (element.productVariantName.isNotBlank()) {
                    textQtyLeft.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_4), itemView.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_2), 0, 0)
                } else {
                    textQtyLeft.setPadding(0, 0, 0, 0)
                }
            } else {
                textQtyLeft.gone()
            }
        }
    }

    private fun renderProductPrice(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productPrice, false).removeDecimalSuffix()

            val hasPriceOriginal = element.productOriginalPrice > 0
            val hasWholesalePrice = element.productWholeSalePrice > 0
            val hasPriceDrop = element.productInitialPriceBeforeDrop > 0 && element.productInitialPriceBeforeDrop > element.productPrice
            val paddingLeft = if (element.isBundlingItem) {
                itemView.resources.getDimensionPixelOffset(R.dimen.dp_0)
            } else {
                itemView.resources.getDimensionPixelOffset(R.dimen.dp_4)
            }
            val margin16dp = itemView.resources.getDimensionPixelOffset(R.dimen.dp_16)

            if (hasPriceOriginal || hasWholesalePrice || hasPriceDrop) {
                if (element.productSlashPriceLabel.isNotBlank()) {
                    // Slash price
                    renderSlashPriceFromCampaign(element)
                } else if (element.productInitialPriceBeforeDrop > 0) {
                    val wholesalePrice = element.productWholeSalePrice
                    if (wholesalePrice > 0 && wholesalePrice < element.productPrice) {
                        // Wholesale
                        renderSlashPriceFromWholesale(element)
                    } else {
                        // Price drop
                        renderSlashPriceFromPriceDrop(element)
                    }
                } else if (element.productWholeSalePrice > 0) {
                    // Wholesale
                    renderSlashPriceFromWholesale(element)
                }
                textProductPrice.setPadding(paddingLeft, 0, 0, 0)
                textSlashPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                textSlashPrice.show()
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProduct)
                constraintSet.connect(
                    R.id.label_slash_price_percentage,
                    ConstraintSet.START,
                    R.id.image_product,
                    ConstraintSet.END,
                    margin16dp
                )
                constraintSet.connect(
                    R.id.text_slash_price,
                    ConstraintSet.START,
                    R.id.label_slash_price_percentage,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    R.id.text_product_price,
                    ConstraintSet.START,
                    R.id.text_slash_price,
                    ConstraintSet.END
                )
                constraintSet.applyTo(containerProduct)
            } else {
                textSlashPrice.gone()
                labelSlashPricePercentage.gone()
                textProductPrice.setPadding(0, 0, 0, 0)
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProduct)
                constraintSet.connect(
                    R.id.text_product_price,
                    ConstraintSet.START,
                    R.id.text_product_name,
                    ConstraintSet.START
                )
                constraintSet.applyTo(containerProduct)
            }

            if (element.isBundlingItem) {
                adjustProductPriceConstraint()
            }
        }
    }

    private fun renderSlashPriceFromWholesale(element: MiniCartProductUiModel) {
        with(viewBinding) {
            val priceDropValue = element.productInitialPriceBeforeDrop
            val price = element.productPrice
            val originalPrice = if (priceDropValue > price) price else if (priceDropValue > price) priceDropValue else price
            textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(originalPrice, false).removeDecimalSuffix()
            textSlashPrice.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
            textProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productWholeSalePrice, false).removeDecimalSuffix()
            labelSlashPricePercentage.gone()
        }
    }

    private fun renderSlashPriceFromPriceDrop(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productInitialPriceBeforeDrop, false).removeDecimalSuffix()
            textSlashPrice.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
            labelSlashPricePercentage.gone()
        }
    }

    private fun renderSlashPriceFromCampaign(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productOriginalPrice, false).removeDecimalSuffix()
            labelSlashPricePercentage.text = element.productSlashPriceLabel
            labelSlashPricePercentage.show()
            textSlashPrice.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_4), 0, 0, 0)
        }
    }

    private fun renderProductImage(element: MiniCartProductUiModel) {
        with(viewBinding) {
            imageProduct.let {
                if (element.productImageUrl.isNotBlank()) {
                    ImageHandler.loadImageWithoutPlaceholder(it, element.productImageUrl)
                }
            }
            imageProduct.setOnClickListener(productInfoClickListener(element))
        }
    }

    private fun renderProductInformation(element: MiniCartProductUiModel) {
        with(viewBinding) {
            val tmpProductInformation = element.productInformation.toMutableList()
            if (element.productWholeSalePrice > 0) {
                val wholesaleLabel = layoutProductInfo.context?.getString(R.string.mini_cart_label_wholesale_price)
                    ?: ""
                tmpProductInformation.add(wholesaleLabel)
            }
            if (tmpProductInformation.isNotEmpty()) {
                layoutProductInfo.gone()
                if (tmpProductInformation.isNotEmpty()) {
                    layoutProductInfo.removeAllViews()
                    tmpProductInformation.forEach {
                        val productInfo = createProductInfoText(it)
                        layoutProductInfo.addView(productInfo)
                    }
                    layoutProductInfo.show()
                }
            } else {
                layoutProductInfo.gone()
            }
        }
    }

    private fun createProductInfoText(it: String): Typography {
        return Typography(itemView.context).apply {
            setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
            setType(Typography.BODY_3)
            text = if (viewBinding.layoutProductInfo.childCount > 0) ", $it" else it
        }
    }

    private fun renderProductNotes(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textNotes.setOnClickListener {
                listener.onWriteNotesClicked()
                renderProductNotesEditable(element)
            }
            textNotesChange.setOnClickListener {
                listener.onChangeNotesClicked()
                renderProductNotesEditable(element)
            }

            if (element.productNotes.isNotBlank()) {
                renderProductNotesFilled(element)
            } else {
                renderProductNotesEmpty(element)
            }
        }
    }

    private fun renderProductNotesEditable(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textFieldNotes.context?.let {
                textFieldNotes.editText.setOnEditorActionListener { v, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        KeyboardHandler.DropKeyboard(it, v)
                        textFieldNotes.editText.clearFocus()
                        if (element.productNotes.isNotBlank()) {
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
            textFieldNotes.editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            textFieldNotes.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            textFieldNotes.editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
            textFieldNotes.setPlaceholder(Utils.getHtmlFormat(element.placeholderNote))

            textNotes.gone()
            textFieldNotes.show()
            textNotesChange.gone()
            textNotesFilled.gone()
            textNotesFilled.text = Utils.getHtmlFormat(element.productNotes)
            textFieldNotes.setCounter(element.maxNotesLength)
            textFieldNotes.editText.setText(Utils.getHtmlFormat(element.productNotes))
            textFieldNotes.editText.setSelection(textFieldNotes.editText.length())
            textFieldNotes.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    delayChangeNotes?.cancel()
                    delayChangeNotes = GlobalScope.launch(Dispatchers.Main) {
                        delay(NOTES_CHANGE_DELAY)
                        val notes = s.toString()
                        element.productNotes = notes
                        listener.onNotesChanged(element.productId, element.isBundlingItem, element.bundleId, element.bundleGroupId, notes)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            adjustButtonDeleteVisibility(element)
            adjustButtonDeleteConstraint(element)
            adjustVerticalLineConstraint(element)
            textFieldNotes.editText.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    KeyboardHandler.DropKeyboard(v.context, v)
                }
            }
            textFieldNotes.editText.requestFocus()
            showSoftKeyboard(textFieldNotes.context, textFieldNotes.editText)
        }
    }

    private fun renderProductNotesEmpty(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textNotes.show()
            textNotesFilled.gone()
            textNotesChange.gone()
            textFieldNotes.gone()
            adjustButtonDeleteVisibility(element)
            adjustButtonDeleteConstraint(element)
            adjustTextNotesConstraint(element)
            adjustVerticalLineConstraint(element)
        }
    }

    private fun renderProductNotesFilled(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textFieldNotes.gone()
            textNotesFilled.text = Utils.getHtmlFormat(element.productNotes)
            setProductNotesWidth()
            textNotesFilled.show()
            textNotesChange.show()
            textNotes.gone()
            adjustButtonDeleteConstraint(element)
            adjustVerticalLineConstraint(element)
        }
    }

    private fun setProductNotesWidth() {
        with(viewBinding) {
            val paddingParent = itemView.resources.getDimensionPixelSize(R.dimen.dp_16) * 2
            val textNotesChangeWidth = itemView.resources.getDimensionPixelSize(R.dimen.dp_32)
            val paddingLeftTextNotesChange = itemView.resources.getDimensionPixelSize(R.dimen.dp_4)
            val screenWidth = getScreenWidth()
            val maxNotesWidth = screenWidth - paddingParent - paddingLeftTextNotesChange - textNotesChangeWidth

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

    private fun renderActionDelete(element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (!element.isBundlingItem || element.isLastProductItem) {
                adjustButtonDeleteConstraint(element)
                buttonDeleteCart.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onDeleteClicked(element)
                    }
                }
                buttonDeleteCart.show()
            } else {
                buttonDeleteCart.hide()
            }
        }
    }

    private fun adjustButtonDeleteConstraint(element: MiniCartProductUiModel) {
        with(viewBinding) {
            val marginTop = itemView.context.resources.getDimension(R.dimen.dp_12).toInt()
            val margin16dp = itemView.context.resources
                .getDimension(com.tokopedia.abstraction.R.dimen.dp_16).toInt()
            if (element.isProductDisabled) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProduct)
                constraintSet.connect(R.id.button_delete_cart, ConstraintSet.END, R.id.text_product_unavailable_action, ConstraintSet.START, 0)
                if (textFieldNotes.isVisible) {
                    constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.text_field_notes, ConstraintSet.BOTTOM, marginTop)
                } else {
                    constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.text_notes_filled, ConstraintSet.BOTTOM, marginTop)
                }
                constraintSet.applyTo(containerProduct)
            } else if (element.isBundlingItem && element.isLastProductItem) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProduct)
                constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.text_notes, ConstraintSet.BOTTOM, margin16dp)
                constraintSet.connect(R.id.button_delete_cart, ConstraintSet.BOTTOM, R.id.divider_bottom, ConstraintSet.TOP)
                constraintSet.connect(R.id.button_delete_cart, ConstraintSet.END, R.id.qty_editor_product, ConstraintSet.START, margin16dp)
                if (textFieldNotes.isVisible) {
                    constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.text_field_notes, ConstraintSet.BOTTOM, marginTop)
                } else {
                    constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.text_notes_filled, ConstraintSet.BOTTOM, marginTop)
                }
                constraintSet.applyTo(containerProduct)
            } else {
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProduct)
                constraintSet.connect(R.id.button_delete_cart, ConstraintSet.END, R.id.qty_editor_product, ConstraintSet.START, margin16dp)
                if (textFieldNotes.isVisible) {
                    constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.text_field_notes, ConstraintSet.BOTTOM, marginTop)
                } else {
                    constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.text_notes_filled, ConstraintSet.BOTTOM, marginTop)
                }
                constraintSet.applyTo(containerProduct)
            }
        }
    }

    private fun renderProductQty(element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (element.isProductDisabled || !element.hasDeleteAction()) {
                qtyEditorProduct.gone()
                return
            }

            val minOrder = element.getMinOrder()
            val maxOrder = element.getMaxOrder()
            val productQty = element.getQuantity()

            qtyEditorProduct.show()
            qtyEditorProduct.editText.clearFocus()
            if (qtyTextWatcher != null) {
                // reset listener
                qtyEditorProduct.editText.removeTextChangedListener(qtyTextWatcher)
            }
            qtyEditorProduct.autoHideKeyboard = true
            qtyEditorProduct.minValue = minOrder
            qtyEditorProduct.maxValue = maxOrder
            qtyEditorProduct.setValue(productQty)
            qtyTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    delayChangeQty?.cancel()
                    delayChangeQty = GlobalScope.launch(Dispatchers.Main) {
                        val newValue = s.toString().replace(".", "").toIntOrZero()
                        if (newValue >= minOrder) {
                            delay(QUANTITY_CHANGE_DELAY)
                        } else {
                            // Use longer delay for reset qty, to support automation
                            delay(QUANTITY_RESET_DELAY)
                        }
                        if (isActive && element.getQuantity() != newValue) {
                            validateQty(newValue, element)
                            if (isActive && newValue != 0) {
                                element.setQuantity(newValue)
                                listener.onQuantityChanged(element, newValue)
                            }
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
            qtyEditorProduct.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            qtyEditorProduct.editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    val qty = qtyEditorProduct.editText.text?.toString().toIntOrZero()
                    listener.onInputQuantityClicked(qty)
                }
            }
            qtyEditorProduct.editText.addTextChangedListener(qtyTextWatcher)
            qtyEditorProduct.editText.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    qtyEditorProduct.editText.context?.let {
                        KeyboardHandler.DropKeyboard(it, v)
                    }
                    qtyEditorProduct.editText.clearFocus()
                    true
                } else {
                    false
                }
            }
            qtyEditorProduct.setAddClickListener {
                listener.onQuantityPlusClicked()
            }
            qtyEditorProduct.setSubstractListener {
                listener.onQuantityMinusClicked()
            }
        }
    }

    private fun validateQty(newValue: Int, element: MiniCartProductUiModel) {
        with(viewBinding) {
            val minOrder = element.getMinOrder()
            val maxOrder = element.getMaxOrder()

            if (newValue == minOrder && newValue == maxOrder) {
                qtyEditorProduct.addButton.isEnabled = false
                qtyEditorProduct.subtractButton.isEnabled = false
            } else if (newValue >= maxOrder) {
                if (newValue > maxOrder) {
                    qtyEditorProduct.setValue(maxOrder)
                }
                qtyEditorProduct.addButton.isEnabled = false
                qtyEditorProduct.subtractButton.isEnabled = true
            } else if (newValue <= minOrder) {
                if (newValue < minOrder) {
                    qtyEditorProduct.setValue(minOrder)
                }
                qtyEditorProduct.addButton.isEnabled = true
                qtyEditorProduct.subtractButton.isEnabled = false
            } else {
                qtyEditorProduct.addButton.isEnabled = true
                qtyEditorProduct.subtractButton.isEnabled = true
            }
        }
    }

    private fun renderProductAction(element: MiniCartProductUiModel) {
        if (element.productActions.isNotEmpty()) {
            resetProductActionState()
            element.productActions.forEach {
                when (it.id) {
                    ACTION_NOTES -> {
                        renderProductNotes(element)
                    }
                    ACTION_DELETE -> {
                        renderActionDelete(element)
                    }
                    ACTION_SIMILARPRODUCT -> {
                        if (element.selectedUnavailableActionId == ACTION_SIMILARPRODUCT) {
                            renderActionSimilarProduct(it, element)
                        }
                    }
                }
            }
        }
    }

    private fun resetProductActionState() {
        with(viewBinding) {
            buttonDeleteCart.hide()
            textProductUnavailableAction.hide()
        }
    }

    private fun renderActionSimilarProduct(action: Action, element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (!element.isBundlingItem || element.isLastProductItem) {
                textProductUnavailableAction.text = action.message
                textProductUnavailableAction.setOnClickListener {
                    if (element.selectedUnavailableActionLink.isNotBlank()) {
                        listener.onShowSimilarProductClicked(element.selectedUnavailableActionLink, element)
                    }
                }
                textProductUnavailableAction.context?.let {
                    textProductUnavailableAction.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
                }
                textProductUnavailableAction.show()
            } else {
                textProductUnavailableAction.hide()
            }
        }
    }

    private fun renderProductAlpha(element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (element.isProductDisabled) {
                imageProduct.alpha = ALPHA_HALF
                textProductName.alpha = ALPHA_HALF
                textProductVariant.alpha = ALPHA_HALF
                textQtyLeft.alpha = ALPHA_HALF
                labelSlashPricePercentage.alpha = ALPHA_HALF
                textSlashPrice.alpha = ALPHA_HALF
                textProductPrice.alpha = ALPHA_HALF
                layoutProductInfo.alpha = ALPHA_HALF
            } else {
                imageProduct.alpha = ALPHA_FULL
                textProductName.alpha = ALPHA_FULL
                textProductVariant.alpha = ALPHA_FULL
                textQtyLeft.alpha = ALPHA_FULL
                labelSlashPricePercentage.alpha = ALPHA_FULL
                textSlashPrice.alpha = ALPHA_FULL
                textProductPrice.alpha = ALPHA_FULL
                layoutProductInfo.alpha = ALPHA_FULL
            }
        }
    }

    private fun renderBundleHeader(element: MiniCartProductUiModel) {
        with(viewBinding.layoutBundleHeader) {
            if (element.showBundlingHeader) {
                renderBundleDiscount(element)
                textBundleTitle.text = element.bundleName
                textBundlePrice.text = element.bundlePriceFmt
                imageBundle.loadImage(element.bundleIconUrl)
                if (element.isProductDisabled) {
                    textChangeBundle.gone()
                } else {
                    textChangeBundle.visible()
                    textChangeBundle.setOnClickListener {
                        listener.onChangeBundleClicked(element)
                    }
                }
                containerBundleHeader.show()
            } else {
                containerBundleHeader.hide()
            }
        }
    }

    private fun renderBundleQuantity(element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (element.isBundlingItem && element.bundleLabelQty > 1) {
                labelBundleQty.text = itemView.context.getString(
                    R.string.mini_cart_bundle_qty_label,
                    element.bundleLabelQty
                )
                labelBundleQty.show()
            } else {
                labelBundleQty.hide()
            }
        }
    }

    private fun renderBundleDiscount(element: MiniCartProductUiModel) {
        with(viewBinding.layoutBundleHeader) {
            if (element.slashPriceLabel.isNotBlank()) {
                labelBundleDiscount.text = element.slashPriceLabel
                textBundleSlashPrice.text = element.bundleOriginalPriceFmt
                textBundleSlashPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                labelBundleDiscount.show()
                textBundleSlashPrice.show()
            } else {
                labelBundleDiscount.hide()
                textBundleSlashPrice.hide()
            }
        }
    }

    private fun renderVerticalLine(element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (element.isBundlingItem) {
                adjustVerticalLine(element)
                verticalLine.show()
            } else {
                verticalLine.hide()
            }
        }
    }

    private fun renderBottomDivider(element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (element.showBottomDivider) {
                dividerBottom.show()
            } else {
                dividerBottom.hide()
            }
        }
        adjustBottomMargin(element)
    }

    private fun adjustVerticalLine(element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (element.isLastProductItem) {
                val constraint = ConstraintSet()
                constraint.clone(containerProduct)
                constraint.connect(
                    R.id.vertical_line,
                    ConstraintSet.BOTTOM,
                    R.id.text_notes,
                    ConstraintSet.BOTTOM
                )
                constraint.applyTo(containerProduct)
            }
        }
    }

    private fun adjustTextNotesConstraint(element: MiniCartProductUiModel) {
        if (element.isBundlingItem && element.isLastProductItem) {
            with(viewBinding) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProduct)
                constraintSet.connect(
                    R.id.text_notes,
                    ConstraintSet.TOP,
                    R.id.image_product,
                    ConstraintSet.BOTTOM
                )
                constraintSet.connect(
                    R.id.text_notes,
                    ConstraintSet.BOTTOM,
                    com.tokopedia.design.R.id.delete_button,
                    ConstraintSet.TOP
                )
                constraintSet.applyTo(containerProduct)
            }
        }
    }

    private fun adjustVerticalLineConstraint(element: MiniCartProductUiModel) {
        if (element.isBundlingItem && element.isLastProductItem) {
            with(viewBinding) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProduct)
                when {
                    textFieldNotes.isVisible -> {
                        constraintSet.connect(
                            R.id.vertical_line,
                            ConstraintSet.BOTTOM,
                            R.id.text_field_notes,
                            ConstraintSet.BOTTOM
                        )
                    }
                    textNotesFilled.isVisible -> {
                        constraintSet.connect(
                            R.id.vertical_line,
                            ConstraintSet.BOTTOM,
                            R.id.text_notes_filled,
                            ConstraintSet.BOTTOM
                        )
                    }
                    else -> {
                        constraintSet.connect(
                            R.id.vertical_line,
                            ConstraintSet.BOTTOM,
                            R.id.text_notes,
                            ConstraintSet.BOTTOM
                        )
                    }
                }
                constraintSet.applyTo(containerProduct)
            }
        }
    }

    private fun adjustProductPriceConstraint() {
        with(viewBinding) {
            val margin4dp = itemView.context.resources
                .getDimension(R.dimen.dp_4).toInt()
            val constraintSet = ConstraintSet()
            constraintSet.clone(containerProduct)
            constraintSet.connect(
                R.id.text_product_price,
                ConstraintSet.START,
                R.id.text_product_name,
                ConstraintSet.START
            )
            constraintSet.connect(
                R.id.label_slash_price_percentage,
                ConstraintSet.START,
                R.id.text_product_price,
                ConstraintSet.END,
                margin4dp
            )
            constraintSet.applyTo(containerProduct)
        }
    }

    private fun adjustButtonDeleteVisibility(element: MiniCartProductUiModel) {
        if (element.isBundlingItem && !element.isLastProductItem) {
            with(viewBinding) {
                if (textFieldNotes.isVisible || textNotesFilled.isVisible) {
                    buttonDeleteCart.gone()
                } else {
                    buttonDeleteCart.invisible()
                }
            }
        }
    }

    private fun adjustBottomMargin(element: MiniCartProductUiModel) {
        with(viewBinding) {
            val lp = containerProduct.layoutParams

            val margin = if (element.showBottomDivider) {
                itemView.context.resources.getDimensionPixelSize(
                    com.tokopedia.abstraction.R.dimen.dp_0
                )
            } else {
                itemView.context.resources.getDimensionPixelSize(
                    com.tokopedia.abstraction.R.dimen.dp_16
                )
            }

            when (lp) {
                is ConstraintLayout.LayoutParams -> {
                    containerProduct.layoutParams = lp.apply {
                        bottomMargin = margin
                    }
                }
                is RecyclerView.LayoutParams -> {
                    containerProduct.layoutParams = lp.apply {
                        bottomMargin = margin
                    }
                }
            }
        }
    }
}
