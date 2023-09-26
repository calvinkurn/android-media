package com.tokopedia.home.beranda.data.newatf.channel

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.AtfRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeDynamicChannelsRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTickerRepository
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import javax.inject.Inject

class AtfChannelRepository @Inject constructor(
    private val homeDynamicChannelsRepository: HomeDynamicChannelsRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
    atfDao: AtfDao,
): AtfRepository(atfDao) {

    @SuppressLint("PII Data Exposure")
    override suspend fun getData(atfMetadata: AtfMetadata) {
        val dcParam = Bundle().apply {
            putString(
                HomeDynamicChannelsRepository.PARAMS,
                atfMetadata.param
            )
            putString(
                HomeDynamicChannelsRepository.LOCATION,
                homeChooseAddressRepository.getRemoteData()
                    ?.convertToLocationParams()
            )
        }
        val data = homeDynamicChannelsRepository.getRemoteData(dcParam)
        val atfData = AtfData(atfMetadata, data.dynamicHomeChannel, isCache = false)
        emitAndSaveData(atfData)
    }
}
