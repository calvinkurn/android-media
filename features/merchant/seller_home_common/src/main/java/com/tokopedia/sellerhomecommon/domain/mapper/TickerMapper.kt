package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.common.SellerHomeCommonUtils
import com.tokopedia.sellerhomecommon.domain.model.TickerItemModel
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 02/09/20
 */

class TickerMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(tickers: List<TickerItemModel>): List<TickerItemUiModel> {
        return tickers.map {
            TickerItemUiModel(
                    color = it.color.orEmpty(),
                    id = it.id.orEmpty(),
                    message = it.message.orEmpty(),
                    title = it.title.orEmpty(),
                    type = it.tickerType ?: 0,
                    redirectUrl = SellerHomeCommonUtils.extractUrls(it.message.orEmpty())
                            .getOrNull(0)
                            .orEmpty()
            )
        }
    }
}