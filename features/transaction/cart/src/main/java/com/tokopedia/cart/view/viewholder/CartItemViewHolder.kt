package com.tokopedia.cart.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.text.*
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cart.databinding.ItemCartProductBinding
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*
import java.util.*

@SuppressLint("ClickableViewAccessibility")
class CartItemViewHolder constructor(private val binding: ItemCartProductBinding,
                                     private var actionListener: CartItemAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    private var context: Context? = null
    private var viewHolderListener: ViewHolderListener? = null

    private var cartItemHolderData: CartItemHolderData? = null
    private var parentPosition: Int = 0
    private var dataSize: Int = 0
    private var delayChangeCheckboxState: Job? = null
    private var delayChangeQty: Job? = null
    private var informationLabel: MutableList<String> = mutableListOf()
    private var qtyTextWatcher: TextWatcher? = null

    init {
        context = itemView.context
        setNoteTouchListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setNoteTouchListener() {
        binding.etRemark.setOnTouchListener { view, event ->
            if (view.id == R.id.et_remark) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
    }

    fun clear() {
        context = null
        actionListener = null
        viewHolderListener = null
        delayChangeCheckboxState?.cancel()
        delayChangeQty?.cancel()
        qtyTextWatcher = null
    }

    fun bindData(data: CartItemHolderData, parentPosition: Int, viewHolderListener: ViewHolderListener, dataSize: Int) {
        this.viewHolderListener = viewHolderListener
        this.parentPosition = parentPosition
        cartItemHolderData = data
        this.dataSize = dataSize

        renderProductInfo(data)
        renderSelection(data, parentPosition)
        renderQuantity(data, parentPosition, viewHolderListener)
        renderDefaultActionState()
        renderProductAction(data, viewHolderListener)
    }

    private fun renderDefaultActionState() {
        with(binding) {
            llShopNoteSection.gone()
            textMoveToWishlist.gone()
            btnDeleteCart.gone()
        }
    }

    private fun renderProductAction(data: CartItemHolderData, viewHolderListener: ViewHolderListener) {
        if (data.actionsData.isNotEmpty()) {
            data.actionsData.forEach {
                when (it.id) {
                    Action.ACTION_NOTES -> {
                        renderProductNotes(data)
                    }
                    Action.ACTION_WISHLIST, Action.ACTION_WISHLISTED -> {
                        renderActionWishlist(it, data)
                    }
                    Action.ACTION_DELETE -> {
                        renderActionDelete(data)
                    }
                }
            }
        }
    }

    private fun renderActionDelete(data: CartItemHolderData) {
        binding.btnDeleteCart.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemDeleteButtonClicked(data)
            }
        }
        binding.btnDeleteCart.show()
    }

    private fun renderSelection(data: CartItemHolderData, parentPosition: Int) {
        val cbSelectItem = binding.cbSelectItem
        cbSelectItem.isEnabled = data.isError == false
        cbSelectItem.isChecked = data.isError == false && data.isSelected
        cbSelectItem.skipAnimation()

        var prevIsChecked: Boolean = cbSelectItem.isChecked
        cbSelectItem.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != prevIsChecked) {
                prevIsChecked = isChecked

                delayChangeCheckboxState?.cancel()
                delayChangeCheckboxState = GlobalScope.launch(Dispatchers.Main) {
                    delay(500L)
                    if (isChecked == prevIsChecked && isChecked != data.isSelected) {
                        if (!data.isError) {
                            data.isSelected = isChecked
                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                actionListener?.onCartItemCheckChanged(adapterPosition, parentPosition, data.isSelected)
                                viewHolderListener?.onNeedToRefreshSingleShop(parentPosition)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderProductInfo(data: CartItemHolderData) {
        renderProductName(data)
        renderImage(data)
        renderPrice(data)
        renderVariant(data)
        renderQuantityLeft(data)
        renderSlashPrice(data)
        renderProductProperties(data)
        renderProductPropertyIncidentLabel(data)

        sendAnalyticsInformationLabel(data)

        binding.holderItemCartDivider.visibility = if (layoutPosition == dataSize - 1) View.GONE else View.VISIBLE
    }

    private fun renderProductName(data: CartItemHolderData) {
        binding.textProductName.text = Html.fromHtml(data.productName)
        binding.textProductName.setOnClickListener(getOnClickProductItemListener(adapterPosition, parentPosition, data))
    }

    private fun renderImage(data: CartItemHolderData) {
        data.productImage.let {
            binding.iuImageProduct.loadImage(it)
        }
        binding.iuImageProduct.setOnClickListener(getOnClickProductItemListener(adapterPosition, parentPosition, data))
    }

    private fun sendAnalyticsInformationLabel(data: CartItemHolderData) {
        if (informationLabel.isNotEmpty()) {
            sendAnalyticsShowInformation(informationLabel, data.productId)
        }
    }

    private fun renderProductProperties(data: CartItemHolderData) {
        val layoutProductInfo = binding.layoutProductInfo
        layoutProductInfo.gone()
        val productInformationList = data.productInformation
        if (productInformationList.isNotEmpty()) {
            layoutProductInfo.removeAllViews()
            productInformationList.forEach {
                var tmpLabel = it
                if (tmpLabel.toLowerCase(Locale.getDefault()).contains(LABEL_CASHBACK)) {
                    tmpLabel = LABEL_CASHBACK
                }
                informationLabel.add(tmpLabel.toLowerCase(Locale.getDefault()))

                val productInfo = createProductInfoText(it)
                layoutProductInfo.addView(productInfo)
            }
            layoutProductInfo.show()
        }

        if (data.wholesalePrice > 0) {
            val wholesaleLabel = itemView.context.getString(R.string.label_wholesale_product)
            val productInfo = createProductInfoText(wholesaleLabel)
            layoutProductInfo.addView(productInfo)
            layoutProductInfo.show()
            informationLabel.add(wholesaleLabel.toLowerCase(Locale.getDefault()))
        }
    }

    private fun createProductInfoText(it: String): Typography {
        return Typography(itemView.context).apply {
            setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            setType(Typography.BODY_3)
            text = if (binding.layoutProductInfo.childCount > 0) ", $it" else it
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
                    data.productPrice, false).removeDecimalSuffix()
        }
    }

    private fun renderSlashPrice(data: CartItemHolderData) {
        val hasPriceOriginal = data.productOriginalPrice != 0L
        val hasWholesalePrice = data.wholesalePrice != 0L
        val hasPriceDrop = data.productInitialPriceBeforeDrop > 0 &&
                data.productInitialPriceBeforeDrop > data.productPrice.toLong()
        if (hasPriceOriginal || hasWholesalePrice || hasPriceDrop) {
            if (data.productSlashPriceLabel.isNotBlank()) {
                // Slash price
                renderSlashPriceFromCampaign(data)
            } else if (data.productInitialPriceBeforeDrop != 0L) {
                val wholesalePrice = data.wholesalePrice
                if (wholesalePrice > 0 && wholesalePrice.toDouble() < data.productPrice) {
                    // Wholesale
                    renderSlashPriceFromWholesale(data)
                } else {
                    // Price drop
                    renderSlashPriceFromPriceDrop(data)
                }
            } else if (data.wholesalePrice != 0L) {
                // Wholesale
                renderSlashPriceFromWholesale(data)
            }

            binding.textSlashPrice.paintFlags = binding.textSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
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
        binding.textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(originalPrice, false).removeDecimalSuffix()
    }

    private fun renderSlashPriceFromPriceDrop(data: CartItemHolderData) {
        binding.textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.productInitialPriceBeforeDrop, false).removeDecimalSuffix()
    }

    private fun renderSlashPriceFromCampaign(data: CartItemHolderData) {
        binding.textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.productOriginalPrice, false).removeDecimalSuffix()
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
            paddingRight = itemView.resources.getDimensionPixelOffset(R.dimen.dp_4)
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
            tvLabelRemarkOption.setOnClickListener {
                renderProductNotesEditable(element)
            }

            if (element.notes.isNotBlank()) {
                renderProductNotesFilled(element)
            } else {
                renderProductNotesEmpty(element)
            }
            llShopNoteSection.show()
        }
    }

    private fun renderProductNotesEditable(element: CartItemHolderData) {
        with(binding) {
            etRemark.textFieldInput.inputType = InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
            etRemark.textFieldInput.imeOptions = EditorInfo.IME_ACTION_DONE
            etRemark.textFieldInput.setRawInputType(InputType.TYPE_CLASS_TEXT)
            etRemark.context?.let {
                etRemark.textFieldInput.setOnEditorActionListener { v, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        KeyboardHandler.DropKeyboard(it, v)
                        etRemark.textFieldInput.clearFocus()
                        if (element.notes.isNotBlank()) {
                            renderProductNotesFilled(element)
                        } else {
                            renderProductNotesEmpty(element)
                        }
                        true
                    } else false
                }
            }

            etRemark.requestFocus()
            textNotes.gone()
            etRemark.show()
            tvLabelRemarkOption.gone()
            tvRemark.gone()
            tvRemark.text = element.notes
            etRemark.setCounter(element.maxNotesLength)
            etRemark.textFieldInput.setText(element.notes)
            etRemark.textFieldInput.setSelection(etRemark.textFieldInput.length())
            etRemark.textFieldInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val notes = s.toString()
                    element.notes = notes
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    private fun renderProductNotesEmpty(element: CartItemHolderData) {
        with(binding) {
            textNotes.show()
            tvRemark.gone()
            tvLabelRemarkOption.gone()
            etRemark.gone()
        }
    }

    private fun renderProductNotesFilled(element: CartItemHolderData) {
        with(binding) {
            etRemark.gone()
            tvRemark.text = element.notes
            setProductNotesWidth()
            tvRemark.show()
            tvLabelRemarkOption.show()
            textNotes.gone()
        }
    }

    private fun setProductNotesWidth() {
        with(binding) {
            val paddingParent = itemView.resources.getDimensionPixelSize(R.dimen.dp_16) * 2
            val textNotesChangeWidth = itemView.resources.getDimensionPixelSize(R.dimen.dp_32)
            val paddingLeftTextNotesChange = itemView.resources.getDimensionPixelSize(R.dimen.dp_4)
            val screenWidth = getScreenWidth()
            val maxNotesWidth = screenWidth - paddingParent - paddingLeftTextNotesChange - textNotesChangeWidth

            tvRemark.measure(0, 0)
            val currentWidth = tvRemark.measuredWidth
            if (currentWidth >= maxNotesWidth) {
                tvRemark.layoutParams?.width = maxNotesWidth
                tvRemark.requestLayout()
            } else {
                tvRemark.layoutParams?.width = currentWidth
                tvRemark.requestLayout()
            }
        }
    }

    private fun renderQuantity(data: CartItemHolderData, parentPosition: Int, viewHolderListener: ViewHolderListener) {
        val qtyEditorCart = binding.qtyEditorCart

        qtyEditorCart.autoHideKeyboard = true
        qtyEditorCart.minValue = data.minOrder
        qtyEditorCart.maxValue = data.maxOrder
        qtyEditorCart.setValue(data.quantity)
        if (qtyTextWatcher != null) {
            // reset listener
            qtyEditorCart.editText.removeTextChangedListener(qtyTextWatcher)
        }
        qtyTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                delayChangeQty?.cancel()
                delayChangeQty = GlobalScope.launch(Dispatchers.Main) {
                    delay(500)
                    val newValue = s.toString().replace(".", "").toIntOrZero()
                    if (data.quantity != newValue) {
                        validateQty(newValue, data)
                        if (newValue != 0) {
                            cartItemHolderData?.quantity = newValue
                            actionListener?.onCartItemQuantityChangedThenHitUpdateCartAndValidateUse(cartItemHolderData?.isTokoNow)
                            cartItemHolderData?.let { handleRefreshType(it, viewHolderListener, parentPosition) }
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        qtyEditorCart.editText.addTextChangedListener(qtyTextWatcher)
        qtyEditorCart.setSubstractListener {
            if (!data.isError && adapterPosition != RecyclerView.NO_POSITION && cartItemHolderData != null) {
                actionListener?.onCartItemQuantityMinusButtonClicked()
            }
        }
        qtyEditorCart.setAddClickListener {
            if (!data.isError && adapterPosition != RecyclerView.NO_POSITION && cartItemHolderData != null) {
                actionListener?.onCartItemQuantityPlusButtonClicked()
            }
        }
        qtyEditorCart.editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val qtyStr = (v as? AppCompatEditText)?.text?.toString()
                actionListener?.onCartItemQuantityInputFormClicked(
                        if (!TextUtils.isEmpty(qtyStr)) qtyStr else ""
                )
            }
        }
        qtyEditorCart.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardHandler.DropKeyboard(binding.qtyEditorCart.editText.context, itemView)
                true
            } else false
        }
        qtyEditorCart.editText.imeOptions = EditorInfo.IME_ACTION_DONE
        qtyEditorCart.editText.isEnabled = data.isError == false
    }

    private fun validateQty(newValue: Int, element: CartItemHolderData) {
        val qtyEditorCart = binding.qtyEditorCart
        if (newValue == element.minOrder && newValue == element.maxOrder) {
            qtyEditorCart.addButton.isEnabled = false
            qtyEditorCart.subtractButton.isEnabled = false
        } else if (newValue >= element.maxOrder) {
            qtyEditorCart.setValue(element.maxOrder)
            qtyEditorCart.addButton.isEnabled = false
            qtyEditorCart.subtractButton.isEnabled = true
        } else if (newValue <= element.minOrder) {
            qtyEditorCart.setValue(element.minOrder)
            qtyEditorCart.addButton.isEnabled = true
            qtyEditorCart.subtractButton.isEnabled = false
        } else {
            qtyEditorCart.addButton.isEnabled = true
            qtyEditorCart.subtractButton.isEnabled = true
        }
    }

    private fun handleRefreshType(data: CartItemHolderData, viewHolderListener: ViewHolderListener?, parentPosition: Int) {
        if (data.wholesalePriceData.isNotEmpty()) {
            if (data.isPreOrder) {
                viewHolderListener?.onNeedToRefreshAllShop()
            } else {
                viewHolderListener?.onNeedToRefreshSingleShop(parentPosition)
            }
        } else if (data.shouldValidateWeight) {
            viewHolderListener?.onNeedToRefreshWeight(parentPosition)
            viewHolderListener?.onNeedToRefreshSingleProduct(adapterPosition)
        } else {
            viewHolderListener?.onNeedToRefreshSingleProduct(adapterPosition)
        }
    }

    private fun renderActionWishlist(action: Action, data: CartItemHolderData) {
        val textMoveToWishlist = binding.textMoveToWishlist
        if (data.isWishlisted && action.id == Action.ACTION_WISHLISTED) {
            textMoveToWishlist.text = action.message
            textMoveToWishlist.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            textMoveToWishlist.setOnClickListener { }
        } else if (!data.isWishlisted && action.id == Action.ACTION_WISHLIST) {
            textMoveToWishlist.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            textMoveToWishlist.setOnClickListener {
                actionListener?.onWishlistCheckChanged(data.productId, data.cartId, binding.iuImageProduct)
            }
        }
        textMoveToWishlist.show()
    }

    private fun getOnClickProductItemListener(
            @SuppressLint("RecyclerView") position: Int, parentPosition: Int,
            data: CartItemHolderData): View.OnClickListener {
        return View.OnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemProductClicked(data)
            }
        }
    }

    interface ViewHolderListener {

        fun onNeedToRefreshSingleProduct(childPosition: Int)

        fun onNeedToRefreshSingleShop(parentPosition: Int)

        fun onNeedToRefreshWeight(parentPosition: Int)

        fun onNeedToRefreshAllShop()

    }

    companion object {
        val TYPE_VIEW_ITEM_CART = R.layout.item_cart_product

        const val LABEL_CASHBACK = "cashback"
        const val LABEL_DISCOUNT = "label diskon"

        private const val QUANTITY_REGEX = "[^0-9]"
    }
}
