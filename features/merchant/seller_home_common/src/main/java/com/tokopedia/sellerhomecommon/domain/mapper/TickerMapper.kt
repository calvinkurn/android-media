package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.common.SellerHomeCommonUtils
import com.tokopedia.sellerhomecommon.domain.model.GetTickerResponse
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.sellerhomecommon.utils.Utils
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 02/09/20
 */

class TickerMapper @Inject constructor() : BaseResponseMapper<GetTickerResponse, List<TickerItemUiModel>> {

    override fun mapRemoteDataToUiData(response: GetTickerResponse, isFromCache: Boolean): List<TickerItemUiModel> {
        return response.ticker?.tickers.orEmpty().map {
            TickerItemUiModel(
                    id = it.id.orEmpty(),
                    message = Utils.fromHtmlWithoutExtraSpace(it.message.orEmpty()),
                    title = Utils.fromHtmlWithoutExtraSpace(it.title.orEmpty()),
                    type = it.tickerType ?: 0,
                    redirectUrl = SellerHomeCommonUtils.extractUrls(it.message.orEmpty())
                            .getOrNull(0)
                            .orEmpty(),
                    isFromCache = isFromCache
            )
        }
    }
}