package com.tokopedia.product.manage.common.feature.list.view.mapper

import android.content.Context
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageTicker
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageTicker.*
import com.tokopedia.unifycomponents.ticker.TickerData

object ProductManageTickerMapper {

    fun mapToTickerData(context: Context?, tickerList: List<ProductManageTicker>): List<TickerData> {
        return tickerList.map {
            val descriptionResId = when (it) {
                is ManageStockNoAccessTicker -> R.string.product_manage_single_location_stock_no_access_description
                is EmptyStockTicker -> R.string.product_manage_stock_warning_ticker_description
                is CampaignStockTicker -> R.string.product_manage_campaign_stock_open_campaign
                else -> R.string.product_manage_stock_ticker_description
            }
            val description = context?.getString(descriptionResId).orEmpty()
            TickerData(description, it.type)
        }
    }

    fun mapToTickerList(
        multiLocationShop: Boolean,
        canEditStock: Boolean,
        isAllStockEmpty: Boolean
    ): List<ProductManageTicker> {
        val tickerList = mutableListOf<ProductManageTicker>()

        val adminTicker = when {
            multiLocationShop && canEditStock -> MultiLocationTicker
            multiLocationShop && !canEditStock -> ManageStockNoAccessTicker
            else -> NoTicker
        }

        if (isAllStockEmpty) {
            tickerList.add(EmptyStockTicker)
        }

        if(adminTicker.shouldShow()) {
            tickerList.add(adminTicker)
        }

        return tickerList
    }
}