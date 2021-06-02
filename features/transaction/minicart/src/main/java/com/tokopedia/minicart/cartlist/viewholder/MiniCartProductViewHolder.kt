package com.tokopedia.minicart.cartlist.viewholder

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.Action
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_DELETE
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_NOTES
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_SIMILARPRODUCT
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*


class MiniCartProductViewHolder(private val view: View,
                                private val listener: MiniCartListActionListener) :
        AbstractViewHolder<MiniCartProductUiModel>(view) {

    companion object {
        const val LABEL_CASHBACK = "cashback"
        const val LABEL_DISCOUNT = "label diskon"

        val LAYOUT = R.layout.item_mini_cart_product
    }

    private val imageProduct: ImageUnify? by lazy {
        view.findViewById(R.id.image_product)
    }
    private val textProductName: Typography? by lazy {
        view.findViewById(R.id.text_product_name)
    }
    private val textProductVariant: Typography? by lazy {
        view.findViewById(R.id.text_product_variant)
    }
    private val textQtyLeft: Typography? by lazy {
        view.findViewById(R.id.text_qty_left)
    }
    private val labelSlashPricePercentage: Label? by lazy {
        view.findViewById(R.id.label_slash_price_percentage)
    }
    private val textSlashPrice: Typography? by lazy {
        view.findViewById(R.id.text_slash_price)
    }
    private val textProductPrice: Typography? by lazy {
        view.findViewById(R.id.text_product_price)
    }
    private val layoutProductInfo: FlexboxLayout? by lazy {
        view.findViewById(R.id.layout_product_info)
    }
    private val textFieldNotes: TextFieldUnify? by lazy {
        view.findViewById(R.id.text_field_notes)
    }
    private val textNotes: Typography? by lazy {
        view.findViewById(R.id.text_notes)
    }
    private val textNotesFilled: Typography? by lazy {
        view.findViewById(R.id.text_notes_filled)
    }
    private val textNotesChange: Typography? by lazy {
        view.findViewById(R.id.text_notes_change)
    }
    private val buttonDelete: IconUnify? by lazy {
        view.findViewById(R.id.button_delete_cart)
    }
    private val textProductUnavailableAction: Typography? by lazy {
        view.findViewById(R.id.text_product_unavailable_action)
    }
    private val qtyEditorProduct: QuantityEditorUnify? by lazy {
        view.findViewById(R.id.qty_editor_product)
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
        textNotes?.gone()
        textProductUnavailableAction?.gone()
    }

    private fun renderProductVariant(element: MiniCartProductUiModel) {
        if (element.productVariantName.isNotBlank()) {
            textProductVariant?.text = element.productVariantName
            textProductVariant?.show()
        } else {
            if (element.productQtyLeft.isNotBlank()) {
                textProductVariant?.invisible()
            } else {
                textProductVariant?.gone()
            }
        }
    }

    private fun renderProductName(element: MiniCartProductUiModel) {
        textProductName?.text = element.productName
    }

    private fun renderProductQtyLeft(element: MiniCartProductUiModel) {
        if (!element.isProductDisabled && element.productQtyLeft.isNotBlank()) {
            textQtyLeft?.text = element.productQtyLeft
            textQtyLeft?.show()
            if (element.productVariantName.isNotBlank()) {
                textQtyLeft?.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_4), 0, 0, 0)
            } else {
                textQtyLeft?.setPadding(0, 0, 0, 0)
            }
        } else {
            textQtyLeft?.gone()
        }
    }

    private fun renderProductPrice(element: MiniCartProductUiModel) {
        textProductPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productPrice, false)

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
            textSlashPrice?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            textSlashPrice?.show()
            textProductPrice?.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_4), 0, 0, 0)
        } else {
            textSlashPrice?.gone()
            labelSlashPricePercentage?.gone()
            textProductPrice?.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
        }
    }

    private fun renderSlashPriceFromWholesale(element: MiniCartProductUiModel) {
        val priceDropValue = element.productInitialPriceBeforeDrop
        val price = element.productPrice
        val originalPrice = if (priceDropValue > price) price else priceDropValue
        textSlashPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(originalPrice, false)
        textSlashPrice?.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
        textProductPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productWholeSalePrice, false)
        labelSlashPricePercentage?.gone()
    }

    private fun renderSlashPriceFromPriceDrop(element: MiniCartProductUiModel) {
        textSlashPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productInitialPriceBeforeDrop, false)
        textSlashPrice?.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
        labelSlashPricePercentage?.gone()
    }

    private fun renderSlashPriceFromCampaign(element: MiniCartProductUiModel) {
        textSlashPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productOriginalPrice, false)
        labelSlashPricePercentage?.text = element.productSlashPriceLabel
        labelSlashPricePercentage?.show()
        textSlashPrice?.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_4), 0, 0, 0)
    }

    private fun renderProductImage(element: MiniCartProductUiModel) {
        imageProduct?.let {
            if (element.productImageUrl.isNotBlank()) {
                ImageHandler.loadImageWithoutPlaceholder(it, element.productImageUrl)
            }
        }
    }

    private fun renderProductInformation(element: MiniCartProductUiModel) {
        val tmpProductInformation = element.productInformation.toMutableList()
        if (element.productWholeSalePrice > 0) {
            val wholesaleLabel = "Harga Grosir"
            tmpProductInformation.add(wholesaleLabel)
        }
        if (tmpProductInformation.isNotEmpty()) {
            layoutProductInfo?.gone()
            if (tmpProductInformation.isNotEmpty()) {
                layoutProductInfo?.removeAllViews()
                tmpProductInformation.forEach {
                    val productInfo = createProductInfoText(it)
                    layoutProductInfo?.addView(productInfo)
                }
                layoutProductInfo?.show()
            }
        } else {
            layoutProductInfo?.gone()
        }
    }

    private fun createProductInfoText(it: String): Typography {
        return Typography(itemView.context).apply {
            setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            setType(Typography.BODY_3)
            text = if (layoutProductInfo?.childCount ?: 0 > 0) ", $it" else it
        }
    }

    private fun renderProductNotes(element: MiniCartProductUiModel) {
        textNotes?.setOnClickListener {
            renderProductNotesEditable(element)
        }
        textNotesChange?.setOnClickListener {
            renderProductNotesEditable(element)
        }

        if (element.productNotes.isNotBlank()) {
            renderProductNotesFilled(element)
        } else {
            renderProductNotesEmpty()
        }
    }

    private fun renderProductNotesEditable(element: MiniCartProductUiModel) {
        textFieldNotes?.requestFocus()
        textNotes?.gone()
        textFieldNotes?.show()
        textNotesChange?.gone()
        textNotesFilled?.gone()
        textNotesFilled?.text = element.productNotes
        textFieldNotes?.setCounter(element.maxNotesLength)
        textFieldNotes?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                delayChangeQty?.cancel()
                delayChangeQty = GlobalScope.launch(Dispatchers.Main) {
                    delay(250)
                    element.productNotes = s.toString()
                    listener.onNotesChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        textFieldNotes?.textFieldInput?.imeOptions = EditorInfo.IME_ACTION_DONE
        textFieldNotes?.context?.let {
            textFieldNotes?.textFieldInput?.setOnEditorActionListener { v, actionId, event ->
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

    private fun renderProductNotesEmpty() {
        textNotes?.show()
        textNotesFilled?.gone()
        textNotesChange?.gone()
        textFieldNotes?.gone()
    }

    private fun renderProductNotesFilled(element: MiniCartProductUiModel) {
        textFieldNotes?.gone()
        textNotesFilled?.text = element.productNotes
        setProductNotesWidth()
        textNotesFilled?.show()
        textNotesChange?.show()
        textNotes?.gone()
    }

    private fun setProductNotesWidth() {
        val padding = itemView.resources.getDimensionPixelOffset(R.dimen.dp_16)
        val paddingLeftRight = padding * 2
        buttonDelete?.measure(0, 0)
        val buttonDeleteWidth = buttonDelete?.measuredWidth ?: 0
        qtyEditorProduct?.measure(0, 0)
        val qtyEditorProductWidth = qtyEditorProduct?.measuredWidth ?: 0
        val textNotesChangeWidth = itemView.resources.getDimensionPixelOffset(R.dimen.dp_40)
        val screenWidth = getScreenWidth()
        val maxNotesWidth = screenWidth - paddingLeftRight - buttonDeleteWidth - qtyEditorProductWidth
        val noteWidth = maxNotesWidth - textNotesChangeWidth

        textNotesFilled?.measure(0, 0)
        val currentWidth = textNotesFilled?.measuredWidth
        if (currentWidth ?: 0 >= maxNotesWidth) {
            textNotesFilled?.layoutParams?.width = noteWidth
            textNotesFilled?.requestLayout()
        } else {
            textNotesFilled?.layoutParams?.width = currentWidth
            textNotesFilled?.requestLayout()
        }
    }

    private fun renderActionDelete(element: MiniCartProductUiModel) {
        val marginTop = itemView.context.resources.getDimension(R.dimen.dp_16).toInt()
        if (element.isProductDisabled) {
            val constraintLayout: ConstraintLayout = view.findViewById(R.id.container_product)
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(R.id.button_delete_cart, ConstraintSet.END, R.id.text_product_unavailable_action, ConstraintSet.START, 0)
            if (element.productInformation.isNotEmpty()) {
                constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.layout_product_info, ConstraintSet.BOTTOM, marginTop)
            } else {
                constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.image_product, ConstraintSet.BOTTOM, marginTop)
            }
            constraintSet.applyTo(constraintLayout)
        } else {
            val constraintLayout: ConstraintLayout = view.findViewById(R.id.container_product)
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(R.id.button_delete_cart, ConstraintSet.END, R.id.qty_editor_product, ConstraintSet.START, 0)
            if (element.productInformation.isNotEmpty()) {
                constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.layout_product_info, ConstraintSet.BOTTOM, marginTop)
            } else {
                constraintSet.connect(R.id.button_delete_cart, ConstraintSet.TOP, R.id.image_product, ConstraintSet.BOTTOM, marginTop)
            }
            constraintSet.applyTo(constraintLayout)
        }

        buttonDelete?.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onDeleteClicked(element)
            }
        }
        buttonDelete?.show()
    }

    private fun renderProductQty(element: MiniCartProductUiModel) {
        if (element.isProductDisabled) {
            qtyEditorProduct?.gone()
            return
        }

        qtyEditorProduct?.show()
        if (qtyTextWatcher != null) {
            // reset listener
            qtyEditorProduct?.editText?.removeTextChangedListener(qtyTextWatcher)
            qtyEditorProduct?.setValueChangedListener { _, _, _ -> }
        }
        qtyEditorProduct?.autoHideKeyboard = true
        qtyEditorProduct?.minValue = element.productMinOrder
        qtyEditorProduct?.maxValue = element.productMaxOrder
        qtyEditorProduct?.setValue(element.productQty)
        qtyEditorProduct?.editText?.imeOptions = EditorInfo.IME_ACTION_DONE
        qtyEditorProduct?.setValueChangedListener { newValue, oldValue, isOver ->
            if (element.productQty != newValue) {
                listener.onQuantityChanged(element.productId, newValue)
            }
        }
        qtyTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                delayChangeQty?.cancel()
                delayChangeQty = GlobalScope.launch(Dispatchers.Main) {
                    delay(250)
                    if (s.toString().toIntOrZero() > element.productMaxOrder) {
                        qtyEditorProduct?.setValue(element.productMaxOrder)
                    } else if (s.toString().toIntOrZero() < element.productMinOrder) {
                        qtyEditorProduct?.setValue(element.productMinOrder)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        qtyEditorProduct?.editText?.addTextChangedListener(qtyTextWatcher)
        qtyEditorProduct?.editText?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                qtyEditorProduct?.editText?.context?.let {
                    KeyboardHandler.DropKeyboard(it, v)
                }
                true
            } else false
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
        textProductUnavailableAction?.text = action.message
        textProductUnavailableAction?.setOnClickListener {
            if (element.selectedUnavailableActionLink.isNotBlank()) {
                listener.onShowSimilarProductClicked()
            }
        }
        textProductUnavailableAction?.context?.let {
            textProductUnavailableAction?.setTextColor(ContextCompat.getColor(it, R.color.Unify_N700_68))
        }
        textProductUnavailableAction?.show()
    }

    private fun renderProductAlpha(element: MiniCartProductUiModel) {
        if (element.isProductDisabled) {
            imageProduct?.alpha = 0.5f
            textProductName?.alpha = 0.5f
            textProductVariant?.alpha = 0.5f
            textQtyLeft?.alpha = 0.5f
            labelSlashPricePercentage?.alpha = 0.5f
            textSlashPrice?.alpha = 0.5f
            textProductPrice?.alpha = 0.5f
            layoutProductInfo?.alpha = 0.5f
        } else {
            imageProduct?.alpha = 1.0f
            textProductName?.alpha = 1.0f
            textProductVariant?.alpha = 1.0f
            textQtyLeft?.alpha = 1.0f
            labelSlashPricePercentage?.alpha = 1.0f
            textSlashPrice?.alpha = 1.0f
            textProductPrice?.alpha = 1.0f
            layoutProductInfo?.alpha = 1.0f
        }
    }
}