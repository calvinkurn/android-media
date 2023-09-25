package com.tokopedia.home.beranda.data.newatf.banner

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeDynamicChannelsRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomePageBannerRepository
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BannerRepository @Inject constructor(
    private val homePageBannerRepository: HomePageBannerRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
    private val atfDao: AtfDao,
) {
    val flow: StateFlow<AtfData?>
        get() = _flow
    private var _flow: MutableStateFlow<AtfData?> = MutableStateFlow(null)

    @SuppressLint("PII Data Exposure")
    suspend fun getData(atfMetadata: AtfMetadata) {
        val bannerParam = Bundle().apply {
            putString(
                HomeDynamicChannelsRepository.PARAMS,
                atfMetadata.param
            )
            this.putString(
                HomePageBannerRepository.BANNER_LOCATION_PARAM,
                homeChooseAddressRepository.getRemoteData()
                    ?.convertToLocationParams()
            )
        }
        val data = homePageBannerRepository.getRemoteData(bannerParam)
        val atfData = AtfData(atfMetadata, data.banner, isCache = false)
        val atfCacheEntity = AtfCacheEntity(
            id = atfData.atfMetadata.id,
            position = atfData.atfMetadata.position,
            name = atfData.atfMetadata.name,
            component = atfData.atfMetadata.component,
            param = atfData.atfMetadata.param,
            isOptional = atfData.atfMetadata.isOptional,
            content = atfData.getAtfContentAsJson(),
            status = atfData.atfStatus,
        )
        saveData(atfCacheEntity)
        _flow.emit(atfData)
    }

    private suspend fun saveData(atfCacheEntity: AtfCacheEntity) {
        atfDao.updateAtfData(atfCacheEntity)
    }
}
