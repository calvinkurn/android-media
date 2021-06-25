package com.tokopedia.minicart.cartlist.viewholder

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.Action
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_DELETE
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_NOTES
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_SIMILARPRODUCT
import com.tokopedia.minicart.databinding.ItemMiniCartProductBinding
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*

class MiniCartProductViewHolder(private val viewBinding: ItemMiniCartProductBinding,
                                private val listener: MiniCartListActionListener) :
        AbstractViewHolder<MiniCartProductUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_product
    }

    private var qtyTextWatcher: TextWatcher? = null
    private var delayChangeQty: Job? = null
    private var delayChangeNotes: Job? = null

    override fun onViewRecycled() {
        super.onViewRecycled()
        delayChangeQty?.cancel()
        delayChangeNotes?.cancel()
        qtyTextWatcher = null
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
            textProductName.text = element.productName
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
                    textQtyLeft.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_4), itemView.resources.getDimensionPixelOffset(R.dimen.dp_2), 0, 0)
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
            textProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productPrice, false)

            val hasPriceOriginal = element.productOriginalPrice != 0L
            val hasWholesalePrice = element.productWholeSalePrice != 0L
            val hasPriceDrop = element.productInitialPriceBeforeDrop > 0 && element.productInitialPriceBeforeDrop > element.productPrice
            if (hasPriceOriginal || hasWholesalePrice || hasPriceDrop) {
                if (element.productSlashPriceLabel.isNotBlank()) {
                    // Slash price
                    renderSlashPriceFromCampaign(element)
                } else if (element.productInitialPriceBeforeDrop != 0L) {
                    val wholesalePrice = element.productWholeSalePrice
                    if (wholesalePrice > 0 && wholesalePrice < element.productPrice) {
                        // Wholesale
                        renderSlashPriceFromWholesale(element)
                    } else {
                        // Price drop
                        renderSlashPriceFromPriceDrop(element)
                    }
                } else if (element.productWholeSalePrice != 0L) {
                    // Wholesale
                    renderSlashPriceFromWholesale(element)
                }
                textSlashPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                textSlashPrice.show()
                textProductPrice.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_4), 0, 0, 0)
            } else {
                textSlashPrice.gone()
                labelSlashPricePercentage.gone()
                textProductPrice.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
            }
        }
    }

    private fun renderSlashPriceFromWholesale(element: MiniCartProductUiModel) {
        with(viewBinding) {
            val priceDropValue = element.productInitialPriceBeforeDrop
            val price = element.productPrice
            val originalPrice = if (priceDropValue > price) price else if (priceDropValue > price) priceDropValue else price
            textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(originalPrice, false)
            textSlashPrice.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
            textProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productWholeSalePrice, false)
            labelSlashPricePercentage.gone()
        }
    }

    private fun renderSlashPriceFromPriceDrop(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productInitialPriceBeforeDrop, false)
            textSlashPrice.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
            labelSlashPricePercentage.gone()
        }
    }

    private fun renderSlashPriceFromCampaign(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productOriginalPrice, false)
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
            setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
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
                renderProductNotesEmpty()
            }
        }
    }

    private fun renderProductNotesEditable(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textFieldNotes.requestFocus()
            textNotes.gone()
            textFieldNotes.show()
            textNotesChange.gone()
            textNotesFilled.gone()
            textNotesFilled.text = element.productNotes
            textFieldNotes.setCounter(element.maxNotesLength)
            textFieldNotes.textFieldInput.setText(element.productNotes)
            textFieldNotes.textFieldInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    delayChangeNotes?.cancel()
                    delayChangeNotes = GlobalScope.launch(Dispatchers.Main) {
                        delay(250)
                        val notes = s.toString()
                        element.productNotes = notes
                        listener.onNotesChanged(element.productId, notes)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
            textFieldNotes.textFieldInput.imeOptions = EditorInfo.IME_ACTION_DONE
            textFieldNotes.context?.let {
                textFieldNotes.textFieldInput.setOnEditorActionListener { v, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        KeyboardHandler.DropKeyboard(it, v)
                        if (element.productNotes.isNotBlank()) {
                            renderProductNotesFilled(element)
                        } else {
                            renderProductNotesEmpty()
                        }
                        true
                    } else false
                }
            }
        }
    }

    private fun renderProductNotesEmpty() {
        with(viewBinding) {
            textNotes.show()
            textNotesFilled.gone()
            textNotesChange.gone()
            textFieldNotes.gone()
        }
    }

    private fun renderProductNotesFilled(element: MiniCartProductUiModel) {
        with(viewBinding) {
            textFieldNotes.gone()
            textNotesFilled.text = element.productNotes
            setProductNotesWidth()
            textNotesFilled.show()
            textNotesChange.show()
            textNotes.gone()
        }
    }

    private fun setProductNotesWidth() {
        with(viewBinding) {
            val padding = itemView.resources.getDimensionPixelOffset(R.dimen.dp_16)
            val paddingLeftRight = padding * 2
            buttonDeleteCart.measure(0, 0)
            val buttonDeleteWidth = buttonDeleteCart.measuredWidth
            qtyEditorProduct.measure(0, 0)
            val qtyEditorProductWidth = qtyEditorProduct.measuredWidth
            val textNotesChangeWidth = itemView.resources.getDimensionPixelOffset(R.dimen.dp_40)
            val screenWidth = getScreenWidth()
            val maxNotesWidth = screenWidth - paddingLeftRight - buttonDeleteWidth - qtyEditorProductWidth
            val noteWidth = maxNotesWidth - textNotesChangeWidth

            textNotesFilled.measure(0, 0)
            val currentWidth = textNotesFilled.measuredWidth
            if (currentWidth >= maxNotesWidth) {
                textNotesFilled.layoutParams?.width = noteWidth
                textNotesFilled.requestLayout()
            } else {
                textNotesFilled.layoutParams?.width = currentWidth
                textNotesFilled.requestLayout()
            }
        }
    }

    private fun renderActionDelete(element: MiniCartProductUiModel) {
        with(viewBinding) {
            val marginTop = itemView.context.resources.getDimension(R.dimen.dp_16).toInt()
            if (element.isProductDisabled) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProduct)
                constraintSet.connect(R.id.button_delete_cart, ConstraintSet.END, R.id.text_product_unavailable_action, ConstraintSet.START, 0)
                if (layoutProductInfo.isVisible) {
                    constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.layout_product_info, ConstraintSet.BOTTOM, marginTop)
                } else {
                    constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.image_product, ConstraintSet.BOTTOM, marginTop)
                }
                constraintSet.applyTo(containerProduct)
            } else {
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProduct)
                constraintSet.connect(R.id.button_delete_cart, ConstraintSet.END, R.id.qty_editor_product, ConstraintSet.START, 0)
                if (layoutProductInfo.isVisible) {
                    constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.layout_product_info, ConstraintSet.BOTTOM, marginTop)
                } else {
                    constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.image_product, ConstraintSet.BOTTOM, marginTop)
                }
                constraintSet.applyTo(containerProduct)
            }

            buttonDeleteCart.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onDeleteClicked(element)
                }
            }
            buttonDeleteCart.show()
        }
    }

    private fun renderProductQty(element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (element.isProductDisabled) {
                qtyEditorProduct.gone()
                return
            }

            qtyEditorProduct.show()
            if (qtyTextWatcher != null) {
                // reset listener
                qtyEditorProduct.editText.removeTextChangedListener(qtyTextWatcher)
            }
            qtyEditorProduct.autoHideKeyboard = true
            qtyEditorProduct.minValue = element.productMinOrder
            qtyEditorProduct.maxValue = element.productMaxOrder
            qtyEditorProduct.setValue(element.productQty)
            qtyTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    delayChangeQty?.cancel()
                    delayChangeQty = GlobalScope.launch(Dispatchers.Main) {
                        delay(500)
                        val newValue = s.toString().replace(".", "").toIntOrZero()
                        if (element.productQty != newValue) {
                            validateQty(newValue, element)
                            if (newValue != 0) {
                                element.productQty = newValue
                                listener.onQuantityChanged(element.productId, newValue)
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
                    true
                } else false
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
            if (newValue == element.productMinOrder && newValue == element.productMaxOrder) {
                qtyEditorProduct.addButton.isEnabled = false
                qtyEditorProduct.subtractButton.isEnabled = false
            } else if (newValue >= element.productMaxOrder) {
                qtyEditorProduct.setValue(element.productMaxOrder)
                qtyEditorProduct.addButton.isEnabled = false
                qtyEditorProduct.subtractButton.isEnabled = true
            } else if (newValue <= element.productMinOrder) {
                qtyEditorProduct.setValue(element.productMinOrder)
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

    private fun renderActionSimilarProduct(action: Action, element: MiniCartProductUiModel) {
        with(viewBinding) {
            textProductUnavailableAction.text = action.message
            textProductUnavailableAction.setOnClickListener {
                if (element.selectedUnavailableActionLink.isNotBlank()) {
                    listener.onShowSimilarProductClicked(element.selectedUnavailableActionLink, element)
                }
            }
            textProductUnavailableAction.context?.let {
                textProductUnavailableAction.setTextColor(ContextCompat.getColor(it, R.color.Unify_N700_68))
            }
            textProductUnavailableAction.show()
        }
    }

    private fun renderProductAlpha(element: MiniCartProductUiModel) {
        with(viewBinding) {
            if (element.isProductDisabled) {
                listener.onShowUnavailableItem(element)
                imageProduct.alpha = 0.5f
                textProductName.alpha = 0.5f
                textProductVariant.alpha = 0.5f
                textQtyLeft.alpha = 0.5f
                labelSlashPricePercentage.alpha = 0.5f
                textSlashPrice.alpha = 0.5f
                textProductPrice.alpha = 0.5f
                layoutProductInfo.alpha = 0.5f
            } else {
                imageProduct.alpha = 1.0f
                textProductName.alpha = 1.0f
                textProductVariant.alpha = 1.0f
                textQtyLeft.alpha = 1.0f
                labelSlashPricePercentage.alpha = 1.0f
                textSlashPrice.alpha = 1.0f
                textProductPrice.alpha = 1.0f
                layoutProductInfo.alpha = 1.0f
            }
        }
    }
}