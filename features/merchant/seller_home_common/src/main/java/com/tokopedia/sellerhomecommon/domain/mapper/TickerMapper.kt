package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.sellerhomecommon.common.SellerHomeCommonUtils
import com.tokopedia.sellerhomecommon.domain.model.GetTickerResponse
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 02/09/20
 */

class TickerMapper @Inject constructor() : BaseResponseMapper<GetTickerResponse, List<TickerItemUiModel>> {

    override fun mapRemoteDataToUiData(response: GetTickerResponse, isFromCache: Boolean): List<TickerItemUiModel> {
        return response.ticker?.tickers.orEmpty().map {
            TickerItemUiModel(
                    color = it.color,
                    id = it.id.orEmpty(),
                    message = MethodChecker.fromHtmlWithoutExtraSpace(it.message.orEmpty()).toString(),
                    title = MethodChecker.fromHtmlWithoutExtraSpace(it.title.orEmpty()).toString(),
                    type = it.tickerType ?: 0,
                    redirectUrl = SellerHomeCommonUtils.extractUrls(it.message.orEmpty())
                            .getOrNull(0)
                            .orEmpty(),
                    isFromCache = isFromCache
            )
        }
    }
}