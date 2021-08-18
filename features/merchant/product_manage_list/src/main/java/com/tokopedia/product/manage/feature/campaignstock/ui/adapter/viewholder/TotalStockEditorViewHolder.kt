package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.text.InputFilter
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.TotalStockEditorUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.textwatcher.StockEditorTextWatcher
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_campaign_stock_total_editor.view.*

class TotalStockEditorViewHolder(itemView: View?,
                                 private val onTotalStockChanged: (Int) -> Unit,
                                 private val onOngoingPromotionClicked: (campaignTypeList: List<ProductCampaignType>) -> Unit
): AbstractViewHolder<TotalStockEditorUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_total_editor

        private const val MAXIMUM_LENGTH = 7
    }

    private val stockEditor by lazy { itemView?.qte_campaign_stock_amount }
    private val emptyStockInfo by lazy { itemView?.emptyStockInfo }
    private val textStock by lazy { itemView?.textStock }
    private val ongoingCampaignTypeText by lazy { itemView?.findViewById<Typography>(R.id.tv_campaign_stock_count) }

    private val stockTextWatcher by lazy {
        StockEditorTextWatcher(stockEditor, emptyStockInfo, onTotalStockChanged)
    }

    override fun bind(stock: TotalStockEditorUiModel) {
        stockEditor?.setElement(stock)
        emptyStockInfo?.showWithCondition(stock.isEmpty())
    }

    private fun QuantityEditorUnify.setElement(element: TotalStockEditorUiModel) {
        val maxLength = InputFilter.LengthFilter(MAXIMUM_LENGTH)
        editText.filters = arrayOf(maxLength)
        minValue = EditProductConstant.MINIMUM_STOCK
        maxValue = EditProductConstant.MAXIMUM_STOCK

        removeEditorTextListener()
        setValue(element.totalStock)
        addEditorTextListener()

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                ProductManageTracking.eventClickAllocationInputStock(isVariant = false)
            }
        }
        setAddClickListener {
            ProductManageTracking.eventClickAllocationIncreaseStock(isVariant = false)
        }
        setSubstractListener {
            ProductManageTracking.eventClickAllocationDecreaseStock(isVariant = false)
        }
        setupStockEditor(element)
        setupCampaignCountText(element)
    }

    private fun QuantityEditorUnify.removeEditorTextListener() {
        editText.removeTextChangedListener(stockTextWatcher)
    }

    private fun QuantityEditorUnify.addEditorTextListener() {
        editText.addTextChangedListener(stockTextWatcher)
    }

    private fun setupStockEditor(element: TotalStockEditorUiModel) {
        val canEditStock = element.access?.editStock == true

        if(canEditStock) {
            stockEditor?.show()
            textStock?.hide()
        } else {
            stockEditor?.hide()
            textStock?.show()
            textStock?.text = element.totalStock.toString()
        }
    }

    private fun setupCampaignCountText(element: TotalStockEditorUiModel) {
        val isCampaign = element.isCampaign == true
        ongoingCampaignTypeText?.run {
            showWithCondition(isCampaign)
            element.campaignTypeList?.let { campaignList ->
                text = String.format(getString(R.string.product_manage_campaign_count), campaignList.count().orZero())
                setOnClickListener {
                    onOngoingPromotionClicked(campaignList)
                }
            }
        }
    }
}