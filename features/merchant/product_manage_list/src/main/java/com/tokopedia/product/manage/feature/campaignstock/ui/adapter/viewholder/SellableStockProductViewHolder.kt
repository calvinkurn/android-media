package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant
import com.tokopedia.product.manage.databinding.ItemCampaignStockVariantEditorBinding
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.utils.view.binding.viewBinding

class SellableStockProductViewHolder (itemView: View?,
                                      private val onVariantStockChanged: (productId: String, stock: Int) -> Unit,
                                      private val onVariantStatusChanged: (productId: String, status: ProductStatus) -> Unit,
                                      private val onOngoingPromotionClicked: (campaignTypeList: List<ProductCampaignType>) -> Unit,
                                      private val source: String,
                                      private val shopId: String
): AbstractViewHolder<SellableStockProductUIModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_variant_editor

        private const val MAXIMUM_LENGTH = 7
        private const val MINIMUM_INPUT = 0
    }

    private val binding by viewBinding<ItemCampaignStockVariantEditorBinding>()

    private var stockEditTextWatcher: TextWatcher? = null

    override fun onViewRecycled() {
        removeListeners()
        super.onViewRecycled()
    }

    override fun bind(element: SellableStockProductUIModel) {
        binding?.run {
            tvCampaignStockVariantEditorName.text = element.productName
            qteCampaignStockVariantEditor.setElement(element)
            labelCampaignStockInactive.visibleWithCondition(!element.isActive)
            tvCampaignStockCountVariant?.run {
                visibleWithCondition(element.isCampaign)
                if (element.isCampaign) {
                    element.campaignTypeList?.let { campaignList ->
                        text = String.format(getString(com.tokopedia.product.manage.common.R.string.product_manage_campaign_count), campaignList.count().orZero())
                        setOnClickListener {
                            onOngoingPromotionClicked(campaignList)
                        }
                    }
                }
            }
            switchCampaignStockVariantEditor.run {
                isChecked = element.isActive
                setOnCheckedChangeListener { _, isChecked ->
                    element.isActive = isChecked
                    val status = if (isChecked) {
                        ProductStatus.ACTIVE
                    } else {
                        ProductStatus.INACTIVE
                    }
                    val shouldShowInactiveLabel = !isChecked || getInactivityByStock(element)
                    labelCampaignStockInactive.visibleWithCondition(shouldShowInactiveLabel)
                    onVariantStatusChanged(element.productId, status)
                    ProductManageTracking.eventClickAllocationProductStatus(
                        isVariant = true,
                        isOn = isChecked,
                        source = source,
                        productId = element.productId,
                        shopId = shopId
                    )
                }
            }
            switchCampaignStockVariantEditor.isEnabled = element.access.editProduct
        }
        showHideInactiveLabel(element)
    }

    private fun QuantityEditorUnify.setElement(element: SellableStockProductUIModel) {
        val maxLength = InputFilter.LengthFilter(MAXIMUM_LENGTH)
        editText.filters = arrayOf(maxLength)
        minValue = EditProductConstant.MINIMUM_STOCK
        maxValue = EditProductConstant.MAXIMUM_STOCK

        setValue(element.stock.toIntOrZero())

        stockEditTextWatcher = getStockTextChangeListener {
            val stock: Int
            if(it.isNotEmpty()) {
                stock = getValue()
                toggleQuantityEditorBtn(stock)
                onVariantStockChanged(element.productId, stock)
            } else {
                stock = EditProductConstant.MINIMUM_STOCK
                editText.setText(EditProductConstant.MINIMUM_STOCK.getNumberFormatted())
                toggleQuantityEditorBtn(stock)
            }
            showHideInactiveLabel(element)
            element.stock = stock.toString()
        }
        editText.addTextChangedListener(stockEditTextWatcher)

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                ProductManageTracking.eventClickAllocationInputStock(isVariant = true)
            }
        }

        setAddClickListener {
            ProductManageTracking.eventClickAllocationIncreaseStock(
                isVariant = true,
                source = source,
                productId = element.productId,
                shopId = shopId
            )
        }
        setSubstractListener {
            ProductManageTracking.eventClickAllocationDecreaseStock(
                isVariant = true,
                source = source,
                productId = element.productId,
                shopId = shopId
            )
        }

        setupStockEditor(element)
    }

    private fun getInactivityByStock(element: SellableStockProductUIModel): Boolean {
        val stock = getCurrentStockInput()
        return stock == 0 && !element.isAllStockEmpty
    }

    private fun getInactivityByStatus(): Boolean {
        return binding?.switchCampaignStockVariantEditor?.isChecked == false
    }

    private fun showHideInactiveLabel(element: SellableStockProductUIModel) {
        binding?.labelCampaignStockInactive?.visibleWithCondition(
            getInactivityByStock(element) || getInactivityByStatus())
    }

    private fun View.visibleWithCondition(isVisible: Boolean) {
        visibility =
            if (isVisible) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
    }

    private fun setupStockEditor(element: SellableStockProductUIModel) {
        val canEditStock = element.access.editStock

        if(canEditStock) {
            binding?.qteCampaignStockVariantEditor?.show()
            binding?.textStock?.hide()
        } else {

            binding?.qteCampaignStockVariantEditor?.hide()
            binding?.textStock?.show()
            binding?.textStock?.text = element.stock
        }
    }

    private fun QuantityEditorUnify.toggleQuantityEditorBtn(stock: Int) {
        val enableAddBtn = stock < EditProductConstant.MAXIMUM_STOCK
        val enableSubtractBtn = stock > EditProductConstant.MINIMUM_STOCK

        addButton.isEnabled = enableAddBtn
        subtractButton.isEnabled = enableSubtractBtn
    }

    private fun getStockTextChangeListener(afterTextChanged: (String) -> Unit): TextWatcher =
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(editable: Editable?) {
                    afterTextChanged(editable?.toString().orEmpty())
                }
            }

    private fun removeListeners() {
        itemView.run {
            stockEditTextWatcher?.let {
                binding?.qteCampaignStockVariantEditor?.editText?.removeTextChangedListener(it)
            }
            binding?.switchCampaignStockVariantEditor?.setOnCheckedChangeListener(null)
        }
    }

    private fun getCurrentStockInput(): Int {
        val stockEditor = binding?.qteCampaignStockVariantEditor
        val input = stockEditor?.editText?.text.toString()

        return if(input.isNotEmpty()) {
            stockEditor?.getValue().orZero()
        } else {
            MINIMUM_INPUT
        }
    }
}