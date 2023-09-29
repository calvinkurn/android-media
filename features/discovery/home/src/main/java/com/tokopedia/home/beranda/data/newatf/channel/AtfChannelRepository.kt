package com.tokopedia.home.beranda.data.newatf.channel

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.AtfRepository
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeDynamicChannelsRepository
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import javax.inject.Inject

@HomeScope
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
        val (data, status) = try {
            homeDynamicChannelsRepository.getRemoteData(dcParam).dynamicHomeChannel to AtfKey.STATUS_SUCCESS
        } catch (_: Exception) {
            DynamicHomeChannel() to AtfKey.STATUS_ERROR
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
