package com.tokopedia.home.beranda.data.newatf.ticker

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.AtfRepository
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTickerRepository
import com.tokopedia.home.beranda.domain.model.Ticker
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import javax.inject.Inject

@HomeScope
class TickerRepository @Inject constructor(
    private val homeTickerRepository: HomeTickerRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
    atfDao: AtfDao,
): AtfRepository(atfDao) {

    @SuppressLint("PII Data Exposure")
    override suspend fun getData(atfMetadata: AtfMetadata) {
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
