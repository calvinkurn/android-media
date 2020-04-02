package com.tokopedia.sellerhome.data

import com.tokopedia.config.GlobalConfig
import com.tokopedia.sellerhome.data.remote.TickerService
import com.tokopedia.sellerhome.domain.mapper.TickerMapper
import com.tokopedia.sellerhome.view.model.TickerUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-01-17
 */

class GetTickerRepository @Inject constructor(
        private val service: TickerService,
        private val userSession: UserSessionInterface,
        private val tickerMapper: TickerMapper
) {

    suspend fun getTicker(): List<TickerUiModel> {
        try {
            val versionName = "android-${GlobalConfig.VERSION_NAME}"
            val response = service.getTicker(
                    userId = userSession.userId,
                    device = versionName,
                    pageHeader = TickerService.PAGE_HEADER_VALUE,
                    pageSize = TickerService.SIZE,
                    filterDevice = TickerService.FILTER_SELLERAPP_ANDROID_DEVICE
            )
            if (null != response.data?.tickers) {
                val data = response.data.tickers.orEmpty()
                return tickerMapper.mapRemoteModelToUiModel(data)
            } else {
                throw RuntimeException()
            }
        } catch (e: Exception) {
            throw e
        }
    }
}