package com.tokopedia.product.manage.common.feature.variant.adapter.viewholder

import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.product.manage.common.R
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.common.databinding.ItemProductVariantStockBinding
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.quickedit.common.interfaces.ProductCampaignInfoListener
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class ProductVariantStockViewHolder(
    itemView: View,
    private val listener: ProductVariantStockListener,
    private val campaignListener: ProductCampaignInfoListener
) : AbstractViewHolder<ProductVariant>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_variant_stock

        private const val MAXIMUM_STOCK = 999999
        private const val MINIMUM_STOCK = 0
        private const val MAXIMUM_LENGTH = 7
        private const val STATUS_SWITCH_DELAY = 500L
    }

    private var tempStock: Int? = null
    private var onClickStatusSwitch: Job? = null
    private var textChangeListener: TextWatcher? = null

    private val binding by viewBinding<ItemProductVariantStockBinding>()

    override fun bind(variant: ProductVariant) {
        setProductName(variant)
        setupIconInfo(variant, variant.stock)
        setupStockQuantityEditor(variant)
        setupStatusSwitch(variant)
        setupStatusLabel(variant)
        setupCampaignInfo(variant)
    }

    private fun setProductName(variant: ProductVariant) {
        binding?.textProductName?.text = variant.name
    }

    private fun setupStockQuantityEditor(variant: ProductVariant) {
        removeStockEditorTextChangedListener()
        setStockMinMaxValue(variant)
        setStockEditorValue(variant.stock, variant.maxStock)
        setAddButtonClickListener(variant)
        setSubtractButtonClickListener(variant)
        setupStockEditor(variant)
        addStockEditorTextChangedListener(variant)

    }

    private fun setupStockEditor(variant: ProductVariant) {
        val canEditStock = variant.access.editStock

        if (canEditStock) {
            binding?.quantityEditorStock?.show()
            binding?.textStock?.hide()
        } else {
            binding?.quantityEditorStock?.hide()
            binding?.textStock?.show()
            binding?.textStock?.text = variant.stock.toString()
        }
    }

    private fun setupStatusSwitch(variant: ProductVariant) {
        val canEditProduct = variant.access.editProduct
        binding?.switchStatus?.setOnCheckedChangeListener(null)
        binding?.switchStatus?.isChecked = variant.isActive()
        binding?.switchStatus?.setOnCheckedChangeListener { _, isChecked ->
            onClickStatusSwitch?.cancel()
            onClickStatusSwitch = runWithDelay({
                val status = if (isChecked) {
                    ProductStatus.ACTIVE
                } else {
                    ProductStatus.INACTIVE
                }
                val shouldShowInactiveLabel = !isChecked || getInactivityByStock(variant)
                binding?.labelInactive?.showWithCondition(shouldShowInactiveLabel)
                listener.onStatusChanged(variant.id, status)
            }, STATUS_SWITCH_DELAY)
        }
        binding?.switchStatus?.isEnabled = canEditProduct
    }

    private fun setupStatusLabel(variant: ProductVariant) {
        showHideInactiveLabel(variant)
    }

    private fun setupCampaignInfo(variant: ProductVariant) {
        binding?.tvProductManageVariantStockCountVariant?.run {
            text = String.format(
                getString(R.string.product_manage_campaign_count),
                variant.campaignTypeList?.count().orZero()
            )
            setOnClickListener {
                ProductManageVariantMapper.mapVariantCampaignTypeToProduct(variant.campaignTypeList)
                    ?.let { campaignList ->
                        campaignListener.onClickCampaignInfo(campaignList)
                    }
            }
            showWithCondition(variant.isCampaign)
        }
    }

    private fun setupIconInfo(variant: ProductVariant, currentStock: Int) {
        when {
            variant.haveNotifyMe() && currentStock == Int.ZERO -> {
                setupNotifyMeInfo(variant)
            }
            variant.isEmpty() || currentStock == Int.ZERO -> {
                setupEmptyStockInfo(variant)
            }
            variant.isBelowStockAlert ||
                    (currentStock < variant.stockAlertCount && variant.haveStockAlertActive()) -> {
                setupStockAlertActiveInfo(variant)
            }
            variant.haveStockAlertActive() -> {
                setupHasStockAlertInfo(variant)
            }
            else -> {
                binding?.tvIconInfo?.hide()
            }
        }
    }

    private fun setupNotifyMeInfo(variant: ProductVariant) {
        binding?.tvIconInfo?.run {
            text = getString(
                com.tokopedia.product.manage.common.R.string.product_manage_notify_me_buyer_info_in_edit_stock_variant,
                variant.notifymeCount.toString()
            ).parseAsHtml()
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_notify_me_buyer, 0, 0, 0)
            show()
        }
    }

    private fun setupEmptyStockInfo(variant: ProductVariant) {
        binding?.tvIconInfo?.run {
            text = getString(
                com.tokopedia.product.manage.common.R.string.product_manage_zero_stock_info_in_edit_stock_variant,
                variant.notifymeCount.toString()
            ).parseAsHtml()
            show()
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning_oos, 0, 0, 0)
        }
    }

    private fun setupStockAlertActiveInfo(variant: ProductVariant) {
        binding?.tvIconInfo?.run {
            text = getString(
                com.tokopedia.product.manage.common.R.string.product_manage_stock_alert_active_info_in_edit_stock_variant,
                variant.notifymeCount.toString()
            ).parseAsHtml()
            show()
            setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_information_filled_yellow,
                0,
                0,
                0
            )
        }
    }

    private fun setupHasStockAlertInfo(variant: ProductVariant) {
        binding?.tvIconInfo?.run {
            text = getString(
                com.tokopedia.product.manage.common.R.string.product_manage_has_stock_alert_info_in_edit_stock_variant,
                variant.notifymeCount.toString()
            ).parseAsHtml()
            show()
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bell_filled, 0, 0, 0)
        }
    }

    private fun setStockMinMaxValue(variant: ProductVariant) {
        binding?.quantityEditorStock?.run {
            val maxLength = LengthFilter(MAXIMUM_LENGTH)
            editText.filters = arrayOf(maxLength)
            minValue = MINIMUM_STOCK
            maxValue = variant.maxStock ?: MAXIMUM_STOCK
        }
    }

    private fun setStockEditorValue(stock: Int, maxStock: Int?) {
        binding?.quantityEditorStock?.setValue(stock)
        setQuantityEditorError(stock, maxStock)
    }

    private fun setQuantityEditorError(stock: Int, maxStock: Int?) {
        maxStock?.let {
            binding?.quantityEditorStock?.errorMessageText =
                if (stock > it) {
                    itemView.context?.getString(
                        R.string.product_manage_quick_edit_stock_max_stock,
                        it.getNumberFormatted()
                    ).orEmpty()
                } else {
                    String.EMPTY
                }
        }
    }

    private fun addStockEditorTextChangedListener(variant: ProductVariant) {
        val quantityEditor = binding?.quantityEditorStock
        tempStock = quantityEditor?.getValue().orZero()

        quantityEditor?.editText?.run {
            textChangeListener = createTextChangeListener(variant)
            addTextChangedListener(textChangeListener)

            setOnFocusChangeListener { _, isFocus ->
                if (!isFocus) {
                    val currentStock = quantityEditor.getValue()
                    tempStock?.let { previousStock ->
                        // if previous stock is not the same as current stock, hit the tracker
                        if (previousStock != currentStock) {
                            ProductManageTracking.eventClickChangeAmountVariant()
                            tempStock = currentStock
                        }
                    }
                }
            }
        }
    }

    private fun removeStockEditorTextChangedListener() {
        textChangeListener?.let {
            binding?.quantityEditorStock?.editText?.run {
                removeTextChangedListener(it)
            }
        }
    }

    private fun createTextChangeListener(variant: ProductVariant): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s?.toString().orEmpty()
                val stock: Int
                if (input.isNotEmpty()) {
                    stock = binding?.quantityEditorStock?.getValue().orZero()
                    toggleQuantityEditorBtn(stock, variant.maxStock)
                    listener.onStockChanged(variant.id, stock)
                } else {
                    stock = MINIMUM_STOCK
                    binding?.quantityEditorStock?.editText?.setText(stock.getNumberFormatted())
                    toggleQuantityEditorBtn(stock, variant.maxStock)
                }
                setQuantityEditorError(stock, variant.maxStock)
                binding?.labelInactive?.showWithCondition(
                    getInactivityByStock(variant) || getInactivityByStatus()
                )
                listener.onStockBtnClicked()
                setupIconInfo(variant, stock)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
    }

    private fun setAddButtonClickListener(variant: ProductVariant) {
        binding?.quantityEditorStock?.run {
            addButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if (input.isNotEmpty()) {
                    input.toInt()
                } else {
                    MINIMUM_STOCK
                }

                stock++

                if (stock <= variant.maxStock ?: MAXIMUM_STOCK) {
                    tempStock = stock
                    editText.setText(stock.getNumberFormatted())
                    listener.onStockBtnClicked()
                    ProductManageTracking.eventClickChangeAmountVariant()
                }

                setupStatusLabel(variant)
            }
        }
    }

    private fun setSubtractButtonClickListener(variant: ProductVariant) {
        binding?.quantityEditorStock?.run {
            subtractButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if (input.isNotEmpty()) {
                    input.toInt()
                } else {
                    MINIMUM_STOCK
                }

                stock--

                if (stock >= MINIMUM_STOCK) {
                    tempStock = stock
                    editText.setText(stock.getNumberFormatted())
                    listener.onStockBtnClicked()
                    ProductManageTracking.eventClickChangeAmountVariant()
                }

                setupStatusLabel(variant)
            }
        }
    }

    private fun toggleQuantityEditorBtn(stock: Int, maxStock: Int?) {
        val enableAddBtn = stock <= (maxStock ?: MAXIMUM_STOCK)
        val enableSubtractBtn = stock > MINIMUM_STOCK
        binding?.quantityEditorStock?.run {
            addButton.isEnabled = enableAddBtn
            subtractButton.isEnabled = enableSubtractBtn
        }
    }

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
    }

    private fun runWithDelay(block: () -> Unit, delayMs: Long): Job {
        return CoroutineScope(Dispatchers.Main)
            .launchCatchError(block = {
                delay(delayMs)
                block()
            }, onError = {})
    }

    private fun getInactivityByStock(variant: ProductVariant): Boolean {
        val stock = getCurrentStockInput()
        return stock == 0 && !variant.isAllStockEmpty
    }

    private fun getInactivityByStatus(): Boolean {
        return binding?.switchStatus?.isChecked == false
    }

    private fun showHideInactiveLabel(variant: ProductVariant) {
        binding?.labelInactive?.showWithCondition(getInactivityByStock(variant) || getInactivityByStatus())
    }

    private fun getCurrentStockInput(): Int {
        val quantityEditorStock = binding?.quantityEditorStock
        val input = quantityEditorStock?.editText?.text?.toString().orEmpty()
        return if (input.isNotEmpty()) {
            quantityEditorStock?.getValue().orZero()
        } else {
            MINIMUM_STOCK
        }
    }

    interface ProductVariantStockListener {
        fun onStockBtnClicked()
        fun onStockChanged(variantId: String, stock: Int)
        fun onStatusChanged(variantId: String, status: ProductStatus)
    }
}
