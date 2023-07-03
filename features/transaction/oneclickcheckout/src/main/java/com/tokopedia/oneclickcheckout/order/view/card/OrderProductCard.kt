package com.tokopedia.oneclickcheckout.order.view.card

import android.graphics.Paint
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.CardOrderProductBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.ADD_ON_PRODUCT_STATUS_DEFAULT
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.ADD_ON_PRODUCT_STATUS_UNCHECK
import com.tokopedia.purchase_platform.common.databinding.ItemProductInfoAddOnBinding
import com.tokopedia.purchase_platform.common.databinding.ItemShipmentAddonProductBinding
import com.tokopedia.purchase_platform.common.databinding.ItemShipmentAddonProductItemBinding
import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.AddOnsProductDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.EthicalDrugDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.response.AddOnGiftingResponse
import com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.purchase_platform.common.utils.showSoftKeyboard
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class OrderProductCard(
    private val binding: CardOrderProductBinding,
    private val listener: OrderProductCardListener,
    private val orderSummaryAnalytics: OrderSummaryAnalytics,
    private val inflater: LayoutInflater
): RecyclerView.ViewHolder(binding.root), CoroutineScope {

    private var product: OrderProduct = OrderProduct()
    private var shop: OrderShop = OrderShop()
    private var productIndex: Int = 0

    private var quantityTextWatcher: TextWatcher? = null
    private var noteTextWatcher: TextWatcher? = null

    private var oldQtyValue: Int = 0

    private var resetQuantityJob: Job? = null
    private var checkAddOnProductItemJob: Job? = null

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
        renderAddOn(binding, product, shop)
        renderAddOnsProduct()
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
            if (!product.hasTriggerViewErrorProductLevelTicker) {
                orderSummaryAnalytics.eventViewErrorProductLevelTicker(shop.shopId, product.errorMessage)
                product.hasTriggerViewErrorProductLevelTicker = true
            }
        } else {
            binding.tickerOrderProduct.gone()
            product.hasTriggerViewErrorProductLevelTicker = false
        }
    }

    private fun renderProductNames() {
        binding.apply {
            ivProductImage.setImageUrl(product.productImageUrl)
            tvProductName.text = Utils.getHtmlFormat(product.productName)
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

            val alpha = if (product.isError) DISABLE_ALPHA else ENABLE_ALPHA
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
                var slashPrice = 0.0
                if (product.wholesalePrice > 0 && product.wholesalePrice < product.productPrice) {
                    slashPrice = product.productPrice
                }
                if (product.initialPrice > 0 && product.productPrice < product.initialPrice) {
                    slashPrice = if (slashPrice == 0.0) {
                        product.initialPrice
                    } else {
                        minOf(slashPrice, product.initialPrice)
                    }
                }
                if (slashPrice > 0L) {
                    tvProductSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(slashPrice, false).removeDecimalSuffix()
                    tvProductSlashPrice.paintFlags = tvProductSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvProductSlashPrice.visible()
                } else {
                    tvProductSlashPrice.gone()
                }
            } else {
                tvProductSlashPrice.gone()
                lblProductSlashPricePercentage.gone()
            }

            flexboxOrderProductPrices.alpha = if (product.isError) DISABLE_ALPHA else ENABLE_ALPHA
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
            if (!product.isError && product.ethicalDrug.needPrescription) {
                flexboxOrderProductInfo.addView(renderEthicalDrugMessage(product.ethicalDrug))
            }
        }
    }

    private fun renderEthicalDrugMessage(ethicalDrugDataModel: EthicalDrugDataModel): LinearLayout {
        val propertyLayoutWithIcon = LinearLayout(itemView.context)
        propertyLayoutWithIcon.orientation = LinearLayout.HORIZONTAL
        val itemProductInfoBinding = ItemProductInfoAddOnBinding.inflate(LayoutInflater.from(itemView.context), propertyLayoutWithIcon, false)
        if (!TextUtils.isEmpty(ethicalDrugDataModel.iconUrl)) {
            ImageHandler.loadImageWithoutPlaceholderAndError(
                itemProductInfoBinding.ppIvProductInfoAddOn,
                ethicalDrugDataModel.iconUrl
            )
        }
        if (!TextUtils.isEmpty(ethicalDrugDataModel.text)) {
            itemProductInfoBinding.ppLabelProductInfoAddOn.text = ethicalDrugDataModel.text
        }
        propertyLayoutWithIcon.addView(itemProductInfoBinding.root)
        return propertyLayoutWithIcon
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
                    orderSummaryAnalytics.eventClickSellerNotes(product.productId, shop.shopId)
                    showNotesTextField()
                }
                return@apply
            }
            tvProductNotesPreview.text = Utils.getHtmlFormat(product.notes)
            tvProductNotesPlaceholder.gone()
            tvProductNotesPreview.visible()
            tvProductNotesEdit.visible()
            tvProductNotesEdit.setOnClickListener {
                orderSummaryAnalytics.eventClickSellerNotes(product.productId, shop.shopId)
                showNotesTextField()
                tfNote.editText.setSelection(tfNote.editText.length())
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
            tfNote.editText.requestFocus()
            tfNote.editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            tfNote.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            tfNote.editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
            tfNote.setPlaceholder(Utils.getHtmlFormat(product.placeholderNote))
            tfNote.setCounter(product.maxCharNote)
            if (noteTextWatcher != null) {
                tfNote.editText.removeTextChangedListener(noteTextWatcher)
            }
            tfNote.editText.setText(Utils.getHtmlFormat(product.notes))
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
            tfNote.editText.addTextChangedListener(noteTextWatcher)
            tfNote.editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    product.isEditingNotes = false
                    renderNotes()
                    KeyboardHandler.DropKeyboard(binding.tfNote.context, itemView)
                    listener.forceUpdateCart()
                    true
                } else {
                    false
                }
            }
            tfNote.editText.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    KeyboardHandler.DropKeyboard(v.context, v)
                }
            }
            tfNote.editText.requestFocus()
            showSoftKeyboard(tfNote.context, tfNote.editText)
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
                orderSummaryAnalytics.eventEditQuantityIncrease(
                    product.productId,
                    shop.shopId,
                    product.orderQuantity.toString()
                )
            }
            qtyEditorProduct.setSubstractListener {
                orderSummaryAnalytics.eventEditQuantityDecrease(
                    product.productId,
                    shop.shopId,
                    product.orderQuantity.toString()
                )
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
                        listener.onPurchaseProtectionInfoClicked(url, product.categoryId, product.purchaseProtectionPlanData.protectionPricePerProduct, product.purchaseProtectionPlanData.protectionTitle)
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

    private fun renderAddOn(binding: CardOrderProductBinding, product: OrderProduct, shop: OrderShop) {
        with(binding) {
            val addOn: AddOnGiftingDataModel = if (shop.isFulfillment) {
                shop.addOn
            } else {
                product.addOn
            }
            when (addOn.status) {
                AddOnGiftingResponse.STATUS_SHOW_ENABLED_ADD_ON_BUTTON -> {
                    buttonGiftingAddon.apply {
                        state = ButtonGiftingAddOnView.State.ACTIVE
                        setAddOnButtonData(addOn)
                        setOnClickListener {
                            listener.onClickAddOnButton(AddOnGiftingResponse.STATUS_SHOW_ENABLED_ADD_ON_BUTTON, addOn, product, shop)
                            orderSummaryAnalytics.eventClickAddOnsDetail(product.productId)
                        }
                        show()
                        orderSummaryAnalytics.eventViewAddOnsWidget(product.productId)
                    }
                }
                AddOnGiftingResponse.STATUS_SHOW_DISABLED_ADD_ON_BUTTON -> {
                    buttonGiftingAddon.apply {
                        state = ButtonGiftingAddOnView.State.INACTIVE
                        setAddOnButtonData(addOn)
                        setOnClickListener {
                            listener.onClickAddOnButton(AddOnGiftingResponse.STATUS_SHOW_DISABLED_ADD_ON_BUTTON, addOn, product, shop)
                        }
                        show()
                        orderSummaryAnalytics.eventViewAddOnsWidget(product.productId)
                    }
                }
                else -> {
                    buttonGiftingAddon.gone()
                }
            }
        }
    }

    private fun renderAddOnsProduct() {
        val addOnsProductData = product.addOnsProductData
        binding.addOnsProduct.root.showIfWithBlock(addOnsProductData.data.isNotEmpty()) {
            binding.addOnsProduct.run {
                setTitleAddOnsProduct(
                    binding = this,
                    addOnsProductData = addOnsProductData
                )
                setSeeAllAddOnsProduct(
                    binding = this,
                    addOnsProductData = addOnsProductData
                )
                setAddOnsProductItem(
                    binding = this,
                    addOnsProductData = addOnsProductData
                )
            }
        }
    }

    private fun setTitleAddOnsProduct(
        binding: ItemShipmentAddonProductBinding,
        addOnsProductData: AddOnsProductDataModel
    ) {
        binding.tvTitleAddonProduct.text = addOnsProductData.title
    }

    private fun setSeeAllAddOnsProduct(
        binding: ItemShipmentAddonProductBinding,
        addOnsProductData: AddOnsProductDataModel
    ) {
        binding.tvSeeAllAddonProduct.showIfWithBlock(
            predicate = addOnsProductData.bottomsheet.title.isNotBlank()
                && addOnsProductData.bottomsheet.applink.isNotBlank()
                && addOnsProductData.bottomsheet.isShown
        ) {
            text = addOnsProductData.bottomsheet.title
            setOnClickListener {
                RouteManager.route(
                    binding.root.context,
                    addOnsProductData.bottomsheet.applink
                )
            }
        }
    }

    private fun setAddOnsProductItem(
        binding: ItemShipmentAddonProductBinding,
        addOnsProductData: AddOnsProductDataModel
    ) {
        binding.apply {
            // show product addons layout
            icProductAddon.visible()
            tvTitleAddonProduct.visible()
            tvSeeAllAddonProduct.visible()
            llAddonProductItems.visible()
            llAddonProductItems.removeAllViews()

            // hide shimmer
            loaderIcProductAddon.gone()
            loaderTvTitle.gone()

            addOnsProductData.data.forEach { addOn ->
                val addOnView = ItemShipmentAddonProductItemBinding.inflate(
                    inflater,
                    null,
                    false
                )

                addOnView.apply {
                    // set data on the views
                    setAddOnProductName(
                        binding = addOnView,
                        addOn = addOn
                    )
                    setAddOnProductName(
                        binding = addOnView,
                        addOn = addOn
                    )
                    setAddOnProductPrice(
                        binding = addOnView,
                        addOn = addOn
                    )
                    setAddOnProductStatus(
                        binding = addOnView,
                        addOn = addOn
                    )

                    // set addon listeners
                    cbAddonItem.setOnCheckedChangeListener { _, _ ->
                        changeAddOnProductStatus(
                            addOn = addOn
                        )
                        checkAddOnProductItemJob = launch {
                            delay(DEBOUNCE_CHECK_ADD_ON_PRODUCT_MS)
                            listener.onCheckAddOnProduct(
                                newAddOnProductData = addOn,
                                product = product
                            )
                        }
                    }
                    icProductAddonInfo.showIfWithBlock(addOn.infoLink.isNotBlank()) {
                        setOnClickListener {
                            listener.onClickAddOnProductInfoIcon(
                                url = addOn.infoLink
                            )
                        }
                    }
                }

                // add addon to the layout
                llAddonProductItems.addView(addOnView.root)
            }
        }
    }

    private fun setAddOnProductName(
        binding: ItemShipmentAddonProductItemBinding,
        addOn: AddOnsProductDataModel.Data
    ) {
        binding.tvShipmentAddOnName.text = addOn.name
    }

    private fun setAddOnProductPrice(
        binding: ItemShipmentAddonProductItemBinding,
        addOn: AddOnsProductDataModel.Data
    ) {
        binding.tvShipmentAddOnPrice.text = CurrencyFormatUtil
            .convertPriceValueToIdrFormat(addOn.price, false)
            .removeDecimalSuffix()
    }

    private fun setAddOnProductStatus(
        binding: ItemShipmentAddonProductItemBinding,
        addOn: AddOnsProductDataModel.Data
    ) {
        binding.apply {
            when (addOn.status) {
                ADD_ON_PRODUCT_STATUS_DEFAULT, ADD_ON_PRODUCT_STATUS_UNCHECK -> {
                    cbAddonItem.isChecked = false
                }
                ADD_ON_PRODUCT_STATUS_CHECK -> {
                    cbAddonItem.isChecked = true
                }
                else -> {
                    cbAddonItem.isChecked = true
                    cbAddonItem.isEnabled = false
                }
            }
        }
    }

    private fun changeAddOnProductStatus(
        addOn: AddOnsProductDataModel.Data
    ) {
        addOn.status = when (addOn.status) {
            ADD_ON_PRODUCT_STATUS_DEFAULT, ADD_ON_PRODUCT_STATUS_UNCHECK -> ADD_ON_PRODUCT_STATUS_CHECK
            ADD_ON_PRODUCT_STATUS_CHECK -> ADD_ON_PRODUCT_STATUS_UNCHECK
            else -> ADD_ON_PRODUCT_STATUS_MANDATORY
        }
    }

    private fun ButtonGiftingAddOnView.setAddOnButtonData(addOn: AddOnGiftingDataModel) {
        title = addOn.addOnsButtonModel.title
        desc = addOn.addOnsButtonModel.description
        urlLeftIcon = addOn.addOnsButtonModel.leftIconUrl
        urlRightIcon = addOn.addOnsButtonModel.rightIconUrl
    }

    interface OrderProductCardListener {

        fun onProductChange(product: OrderProduct, productIndex: Int, shouldReloadRates: Boolean = true)

        fun forceUpdateCart()

        fun onPurchaseProtectionInfoClicked(url: String, categoryId: String, protectionPricePerProduct: Int, protectionTitle: String)

        fun onPurchaseProtectionCheckedChange(isChecked: Boolean, productId: String)

        fun getLastPurchaseProtectionCheckState(productId: String): Int

        fun onClickAddOnButton(addOnButtonType: Int, addOn: AddOnGiftingDataModel, product: OrderProduct, shop: OrderShop)

        fun onCheckAddOnProduct(newAddOnProductData: AddOnsProductDataModel.Data, product: OrderProduct)

        fun onClickAddOnProductInfoIcon(url: String)
    }

    companion object {
        const val VIEW_TYPE = 3

        private const val ENABLE_ALPHA = 1.0f
        private const val DISABLE_ALPHA = 0.5f
        private const val DEBOUNCE_CHECK_ADD_ON_PRODUCT_MS = 500L
        private const val DEBOUNCE_RESET_QUANTITY_MS = 1000L
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    fun clearJob() {
        resetQuantityJob?.cancel()
        checkAddOnProductItemJob?.cancel()
    }
}
