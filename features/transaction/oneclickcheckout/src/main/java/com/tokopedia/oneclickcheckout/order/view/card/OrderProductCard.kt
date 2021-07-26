package com.tokopedia.oneclickcheckout.order.view.card

import android.graphics.Paint
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.CardOrderProductBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifyprinciples.Typography
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
        renderProductInfo()
        renderProductAlert()
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
        if (product.errorMessage.isNotEmpty()) {
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
            if (product.variant.isNotBlank()) {
                tvProductVariant.text = product.variant
                tvProductVariant.visible()
            } else {
                tvProductVariant.gone()
            }
            if (!product.isError && product.productWarningMessage.isNotBlank()) {
                tvQtyLeft.text = MethodChecker.fromHtml(product.productWarningMessage)
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
            tvProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(product.finalPrice, false).removeDecimalSuffix()

            if (!product.isError && product.slashPriceLabel.isNotBlank()) {
                lblProductSlashPricePercentage.setLabel(product.slashPriceLabel)
                lblProductSlashPricePercentage.visible()
                if (product.originalPrice > 0) {
                    tvProductSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(product.originalPrice, false).removeDecimalSuffix()
                    tvProductSlashPrice.paintFlags = tvProductSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvProductSlashPrice.visible()
                } else {
                    tvProductSlashPrice.gone()
                }
            } else if (!product.isError) {
                lblProductSlashPricePercentage.gone()
                if (product.wholesalePrice > 0 && product.wholesalePrice < product.productPrice) {
                    tvProductSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(product.wholesalePrice, false).removeDecimalSuffix()
                    tvProductSlashPrice.paintFlags = tvProductSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvProductSlashPrice.visible()
                } else if (product.initialPrice > 0 && product.initialPrice < product.productPrice) {
                    tvProductSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(product.initialPrice, false).removeDecimalSuffix()
                    tvProductSlashPrice.paintFlags = tvProductSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvProductSlashPrice.visible()
                } else {
                    tvProductSlashPrice.gone()
                }
            } else {
                tvProductSlashPrice.gone()
                lblProductSlashPricePercentage.gone()
            }

            flexboxOrderProductPrices.alpha = if (product.isError) 0.5f else 1.0f
        }
    }

    private fun renderProductInfo() {
        binding.apply {
            flexboxOrderProductInfo.removeAllViews()
            if (!product.isError && product.wholesalePrice > 0) {
                val textView = Typography(flexboxOrderProductInfo.context).apply {
                    setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    setType(Typography.BODY_3)
                    text = root.context.getString(R.string.lbl_wholesale_product)
                }
                flexboxOrderProductInfo.addView(textView, 0)
            }
            if (!product.isError && product.productInformation.isNotEmpty()) {
                for (information in product.productInformation) {
                    val textView = Typography(flexboxOrderProductInfo.context).apply {
                        setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                        setType(Typography.BODY_3)
                        text = if (flexboxOrderProductInfo.childCount > 0) "$information, " else information
                    }
                    flexboxOrderProductInfo.addView(textView, 0)
                }
            }
        }
    }

    private fun renderProductAlert() {
        binding.apply {
            if (!product.isError && product.productAlertMessage.isNotBlank()) {
                tvProductAlertMessage.text = product.productAlertMessage
                tvProductAlertMessage.visible()
            } else {
                tvProductAlertMessage.gone()
            }
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
            tfNote.gone()
            if (product.notes.isEmpty()) {
                tvProductNotesPreview.gone()
                tvProductNotesEdit.gone()
                tvProductNotesPlaceholder.visible()
                tvProductNotesPlaceholder.setOnClickListener {
                    orderSummaryAnalytics.eventClickSellerNotes(product.productId.toString(), shop.shopId.toString())
                    showNotesTextField()
                }
                return@apply
            }
            tvProductNotesPreview.text = product.notes
            tvProductNotesPlaceholder.gone()
            tvProductNotesPreview.visible()
            tvProductNotesEdit.visible()
            tvProductNotesEdit.setOnClickListener {
                orderSummaryAnalytics.eventClickSellerNotes(product.productId.toString(), shop.shopId.toString())
                showNotesTextField()
                tfNote.textFieldInput.setSelection(tfNote.textFieldInput.length())
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
            tfNote.requestFocus()
            tfNote.textFieldInput.textSize = 16f
            tfNote.textFieldInput.imeOptions = EditorInfo.IME_ACTION_DONE
            tfNote.setCounter(product.maxCharNote)
            tfNote.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
            if (noteTextWatcher != null) {
                tfNote.textFieldInput.removeTextChangedListener(noteTextWatcher)
            }
            tfNote.textFieldInput.setText(product.notes)
            noteTextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    product.notes = s?.toString() ?: ""
                    listener.onProductChange(product, productIndex, false)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    /* no-op */
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    /* no-op */
                }
            }
            tfNote.textFieldInput.addTextChangedListener(noteTextWatcher)
            tfNote.textFieldInput.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    product.isEditingNotes = false
                    renderNotes()
                    KeyboardHandler.DropKeyboard(binding.tfNote.context, itemView)
                    listener.forceUpdateCart()
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
            qtyEditorProduct.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            qtyEditorProduct.autoHideKeyboard = true
            qtyEditorProduct.minValue = product.minOrderQuantity
            qtyEditorProduct.maxValue = product.maxOrderStock
            oldQtyValue = product.orderQuantity
            qtyEditorProduct.setValue(product.orderQuantity)
            qtyEditorProduct.setValueChangedListener { newValue, _, _ ->
                // prevent multiple callback with same newValue
                if (product.orderQuantity != newValue) {
                    product.orderQuantity = newValue
                    listener.onProductChange(product, productIndex)
                    renderPrice()
                    renderProductInfo()
                }
            }
            qtyEditorProduct.setAddClickListener {
                orderSummaryAnalytics.eventEditQuantityIncrease(product.productId.toString(), shop.shopId.toString(), product.orderQuantity.toString())
            }
            qtyEditorProduct.setSubstractListener {
                orderSummaryAnalytics.eventEditQuantityDecrease(product.productId.toString(), shop.shopId.toString(), product.orderQuantity.toString())
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
                                qtyEditorProduct.setValue(product.minOrderQuantity)
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
        listener.onProductChange(product, productIndex, false)
        listener.onPurchaseProtectionCheckedChange(tmpIsChecked, product.productId)
    }

    interface OrderProductCardListener {

        fun onProductChange(product: OrderProduct, productIndex: Int, shouldReloadRates: Boolean = true)

        fun forceUpdateCart()

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