package com.tokopedia.home.beranda.data.newatf.ticker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.AtfRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTickerRepository
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import javax.inject.Inject

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
        val data = homeTickerRepository.getRemoteData(tickerParam)
        val atfData = AtfData(atfMetadata, data.ticker, isCache = false)
        Log.d("atfflow", "Ticker getData: $atfData")
        emitAndSaveData(atfData)
    }
}
