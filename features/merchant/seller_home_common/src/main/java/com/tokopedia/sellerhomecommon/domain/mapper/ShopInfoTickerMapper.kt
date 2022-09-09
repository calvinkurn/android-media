package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhomecommon.common.SellerHomeCommonUtils
import com.tokopedia.sellerhomecommon.domain.model.GetShopInfoTickerResponse
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 25/04/22.
 */

class ShopInfoTickerMapper @Inject constructor() :
    BaseResponseMapper<GetShopInfoTickerResponse, List<TickerItemUiModel>> {

    companion object {
        private const val TYPE_WARNING = "warning"
        private const val TYPE_DANGER = "danger"
        private const val SHOP_STATUS_INCUBATED = 6
    }

    override fun mapRemoteDataToUiData(
        response: GetShopInfoTickerResponse,
        isFromCache: Boolean
    ): List<TickerItemUiModel> {
        return response.shopInfo.result.filter {
            it.statusInfo.title.isNotBlank() && it.statusInfo.message.isNotBlank()
                    && it.statusInfo.shopStatus == SHOP_STATUS_INCUBATED
        }.map {
            val ticker = it.statusInfo
            return@map TickerItemUiModel(
                id = String.EMPTY,
                title = ticker.title.parseAsHtml().toString(),
                message = ticker.message,
                redirectUrl = SellerHomeCommonUtils.extractUrls(ticker.message)
                    .getOrNull(Int.ZERO)
                    .orEmpty(),
                isFromCache = isFromCache,
                type = getTickerType(ticker.tickerType)
            )
        }
    }

    private fun getTickerType(tickerType: String): Int {
        return when (tickerType) {
            TYPE_WARNING -> Ticker.TYPE_WARNING
            TYPE_DANGER -> Ticker.TYPE_ERROR
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }
}