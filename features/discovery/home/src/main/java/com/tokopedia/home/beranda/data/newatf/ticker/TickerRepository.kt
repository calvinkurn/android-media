package com.tokopedia.home.beranda.data.newatf.ticker

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.BaseAtfRepository
import com.tokopedia.home.beranda.data.newatf.ticker.mapper.TargetedTickerMapper
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTickerRepository
import com.tokopedia.home.beranda.domain.interactor.repository.TargetedTicketRepository
import com.tokopedia.home.beranda.domain.model.Ticker
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import javax.inject.Inject

/**
 * Created by Frenzel
 */
@HomeScope
class TickerRepository @Inject constructor(
    private val homeTickerRepository: HomeTickerRepository,
    private val targetedTicketRepository: TargetedTicketRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
): BaseAtfRepository() {

    override suspend fun getData(atfMetadata: AtfMetadata) {
        if (shouldUseNewTicker()) {
            getTargetedTickerData(atfMetadata)
        } else {
            getOldTickerData(atfMetadata)
        }
    }

    /**
     * Since a new targeted ticker hasn't gradual rollout, for the manual mitigation,
     * we enable throughout hansel patch if fallback needed.
     */
    private fun shouldUseNewTicker() = true

    private suspend fun getTargetedTickerData(atfMetadata: AtfMetadata) {
        val (data, status) = try {
            val result = targetedTicketRepository.getRemoteData(
                TargetedTicketRepository.createParam()
            )

            val uiModel = TargetedTickerMapper.map(result)

            uiModel.firstOrNull() to AtfKey.STATUS_SUCCESS
        } catch (_: Exception) {
            Ticker() to AtfKey.STATUS_ERROR
        }

        val atfData = AtfData(
            atfMetadata = atfMetadata,
            atfContent = data,
            atfStatus = status,
            isCache = false,
        )
        emitData(atfData)
    }

    @SuppressLint("PII Data Exposure")
    private suspend fun getOldTickerData(atfMetadata: AtfMetadata) {
        val tickerParam = Bundle().apply {
            putString(
                HomeTickerRepository.PARAM_LOCATION,
                homeChooseAddressRepository.getRemoteData()
                    ?.convertToLocationParams()
            )
        }
        val (data, status) = try {
            homeTickerRepository.getRemoteData(tickerParam).ticker to AtfKey.STATUS_SUCCESS
        } catch (_: Exception) {
            Ticker() to AtfKey.STATUS_ERROR
        }

        val atfData = AtfData(
            atfMetadata = atfMetadata,
            atfContent = data,
            atfStatus = status,
            isCache = false,
        )
        emitData(atfData)
    }
}
