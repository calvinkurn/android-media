package com.tokopedia.oneclickcheckout.order.view.card

import android.graphics.Paint
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.databinding.CardOrderProductBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class OrderProductCard(private val binding: CardOrderProductBinding, private val listener: OrderProductCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics) : RecyclerView.ViewHolder(binding.root), CoroutineScope {

    private var product: OrderProduct = OrderProduct()
    private var shop: OrderShop = OrderShop()
    private var productIndex: Int = 0

    private var quantityTextWatcher: TextWatcher? = null
    private var noteTextWatcher: TextWatcher? = null

    private var oldQtyValue: Int = 0

    private var resetQuantityJob: Job? = null

    fun setData(product: OrderProduct, shop: OrderShop, productIndex: Int) {
        this.product = product
        this.shop = shop
        this.productIndex = productIndex
        initView()
    }

    private fun initView() {
        renderDivider()
        renderProductTicker()
        renderProductNames()
        renderPrice()
        renderNotes()
        renderQuantity()
        renderPurchaseProtection()
    }

    private fun renderDivider() {
        if (productIndex == 0) {
            binding.dividerOrderProduct.gone()
        } else {
            binding.dividerOrderProduct.visible()
        }
    }

    private fun renderProductTicker() {
        if (product.isError) {
            binding.tickerOrderProduct.setHtmlDescription(product.errorMessage)
            binding.tickerOrderProduct.visible()
        } else {
            binding.tickerOrderProduct.gone()
        }
    }

    private fun renderProductNames() {
        binding.apply {
            ivProductImage.setImageUrl(product.productImageUrl)
            tvProductName.text = product.productName
            if (product.tickerMessage.message.isNotEmpty()) {
                var completeText = product.tickerMessage.message
                for (replacement in product.tickerMessage.replacement) {
                    completeText = completeText.replace("{{${replacement.identifier}}}", replacement.value)
                }
                tvQtyLeft.text = MethodChecker.fromHtml(completeText)
                tvQtyLeft.visible()
            } else {
                tvQtyLeft.gone()
            }

            val alpha = if (product.isError) 0.5f else 1.0f
            ivProductImage.alpha = alpha
            tvProductName.alpha = alpha
            flexboxOrderProductNames.alpha = alpha
        }
    }

    private fun renderPrice() {
        binding.apply {
            tvProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(product.getPrice(), false).removeDecimalSuffix()

            if (product.originalPrice.isNotBlank()) {
                tvProductSlashPrice.text = product.originalPrice
                tvProductSlashPrice.paintFlags = tvProductSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvProductSlashPrice.visible()
            } else {
                tvProductSlashPrice.gone()
            }

            if (product.cashback.isNotEmpty()) {
                labelProductSlashPricePercentage.setLabel(product.cashback)
                labelProductSlashPricePercentage.visible()
            } else {
                labelProductSlashPricePercentage.gone()
            }

            flexboxOrderProductPrices.alpha = if (product.isError) 0.5f else 1.0f
        }
    }

    private fun renderNotes() {
        binding.apply {
            if (product.isError) {
                tfNote.gone()
                tvProductNotesPlaceholder.gone()
                tvProductNotesPreview.gone()
                tvProductNotesEdit.gone()
                return@apply
            }
            if (product.isEditingNotes) {
                showNotesTextField()
                return@apply
            }
            if (product.notes.isEmpty()) {
                tfNote.gone()
                tvProductNotesPreview.gone()
                tvProductNotesEdit.gone()
                tvProductNotesPlaceholder.visible()
                tvProductNotesPlaceholder.setOnClickListener {
                    showNotesTextField()
                }
                return@apply
            }
            tvProductNotesPreview.text = product.notes
            tvProductNotesPlaceholder.gone()
            tvProductNotesPreview.visible()
            tvProductNotesEdit.visible()
            tvProductNotesEdit.setOnClickListener {
                showNotesTextField()
            }
        }
    }

    private fun showNotesTextField() {
        binding.apply {
            tvProductNotesPlaceholder.gone()
            tvProductNotesPreview.gone()
            tvProductNotesEdit.gone()
            tfNote.visible()
            product.isEditingNotes = true
            tfNote.textFieldInput.textSize = 16f
            tfNote.textFieldInput.isSingleLine = false
            tfNote.setCounter(MAX_NOTES_LENGTH)
            tfNote.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
            tfNote.textFieldInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    orderSummaryAnalytics.eventClickSellerNotes(product.productId.toString(), shop.shopId.toString())
                }
            }
            if (noteTextWatcher != null) {
                tfNote.textFieldInput.removeTextChangedListener(noteTextWatcher)
            }
            tfNote.textFieldInput.setText(product.notes)
            noteTextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    product.notes = s?.toString() ?: ""
                    listener.onProductChange(product, false)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    /* no-op */
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    /* no-op */
                }
            }
            tfNote.textFieldInput.addTextChangedListener(noteTextWatcher)
            tfNote.textFieldInput.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    product.isEditingNotes = false
                    KeyboardHandler.DropKeyboard(binding.tfNote.context, itemView)
                    true
                } else false
            }
        }
    }

    private fun renderQuantity() {
        binding.apply {
            if (product.isError) {
                qtyEditorProduct.gone()
                return@apply
            }
            qtyEditorProduct.visible()
            if (quantityTextWatcher != null) {
                // reset listener
                qtyEditorProduct.editText.removeTextChangedListener(quantityTextWatcher)
                qtyEditorProduct.setValueChangedListener { _, _, _ -> }
            }
            qtyEditorProduct.autoHideKeyboard = true
            qtyEditorProduct.minValue = product.quantity.minOrderQuantity
            qtyEditorProduct.maxValue = product.quantity.maxOrderStock
            oldQtyValue = product.quantity.orderQuantity
            qtyEditorProduct.setValue(product.quantity.orderQuantity)
            qtyEditorProduct.setValueChangedListener { newValue, _, _ ->
                // prevent multiple callback with same newValue
                if (product.quantity.orderQuantity != newValue) {
                    product.quantity.orderQuantity = newValue
                    listener.onProductChange(product)
                    renderPrice()
                }
            }
            qtyEditorProduct.setAddClickListener {
                orderSummaryAnalytics.eventEditQuantityIncrease(product.productId.toString(), shop.shopId.toString(), product.quantity.orderQuantity.toString())
            }
            qtyEditorProduct.setSubstractListener {
                orderSummaryAnalytics.eventEditQuantityDecrease(product.productId.toString(), shop.shopId.toString(), product.quantity.orderQuantity.toString())
            }
            quantityTextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // for automatic reload rates when typing
                    val newValue = s.toString().replace("[^0-9]".toRegex(), "").toIntOrZero()
                    if (newValue > 0 && oldQtyValue != newValue) {
                        resetQuantityJob?.cancel()
                        oldQtyValue = newValue
                        qtyEditorProduct.setValue(newValue)
                    } else if (newValue <= 0) {
                        // trigger reset quantity debounce to prevent empty quantity edit text
                        resetQuantityJob?.cancel()
                        resetQuantityJob = launch {
                            delay(DEBOUNCE_RESET_QUANTITY_MS)
                            if (isActive) {
                                qtyEditorProduct.setValue(product.quantity.minOrderQuantity)
                            }
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    /* no-op */
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    /* no-op */
                }
            }
            qtyEditorProduct.editText.addTextChangedListener(quantityTextWatcher)
        }
    }

    private fun renderPurchaseProtection() {
        binding.apply {
            if (!product.isError && product.purchaseProtectionPlanData.isProtectionAvailable) {
                tvProtectionTitle.text = product.purchaseProtectionPlanData.protectionTitle
                tvProtectionDescription.text = product.purchaseProtectionPlanData.protectionSubtitle
                tvProtectionPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(product.purchaseProtectionPlanData.protectionPricePerProduct, false).removeDecimalSuffix()
                btnProtectionInfo.setOnClickListener {
                    val url = product.purchaseProtectionPlanData.protectionLinkUrl
                    if (url.isNotBlank()) {
                        listener.onPurchaseProtectionInfoClicked(url, product.categoryId, product.purchaseProtectionPlanData.protectionTitle)
                    }
                }
                cbPurchaseProtection.isEnabled = !product.purchaseProtectionPlanData.isProtectionCheckboxDisabled
                cbPurchaseProtection.setOnCheckedChangeListener { _, isChecked ->
                    handleOnPurchaseProtectionCheckedChange(isChecked)
                }
                val lastState = listener.getLastPurchaseProtectionCheckState(product.productId)
                if (lastState != PurchaseProtectionPlanData.STATE_EMPTY) {
                    val tmpIsChecked = lastState == PurchaseProtectionPlanData.STATE_TICKED
                    cbPurchaseProtection.isChecked = tmpIsChecked
                    handleOnPurchaseProtectionCheckedChange(tmpIsChecked)
                } else {
                    cbPurchaseProtection.isChecked = product.purchaseProtectionPlanData.isProtectionOptIn
                }
                tvProtectionUnit.text = product.purchaseProtectionPlanData.unit

                groupPurchaseProtection.show()
            } else {
                groupPurchaseProtection.gone()
            }
        }
    }

    private fun handleOnPurchaseProtectionCheckedChange(tmpIsChecked: Boolean) {
        product.purchaseProtectionPlanData.stateChecked = if (tmpIsChecked) PurchaseProtectionPlanData.STATE_TICKED else PurchaseProtectionPlanData.STATE_UNTICKED
        listener.onProductChange(product, false)
        listener.onPurchaseProtectionCheckedChange(tmpIsChecked, product.productId)
    }

    interface OrderProductCardListener {

        fun onProductChange(product: OrderProduct, shouldReloadRates: Boolean = true)

        fun onPurchaseProtectionInfoClicked(url: String, categoryId: String, protectionTitle: String)

        fun onPurchaseProtectionCheckedChange(isChecked: Boolean, productId: Long)

        fun getLastPurchaseProtectionCheckState(productId: Long): Int
    }

    companion object {
        const val VIEW_TYPE = 3
        const val MAX_NOTES_LENGTH = 144

        private const val DEBOUNCE_RESET_QUANTITY_MS = 1000L
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    fun clearJob() {
        resetQuantityJob?.cancel()
    }
}