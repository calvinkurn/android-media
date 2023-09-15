package com.tokopedia.home.beranda.data.newatf.banner

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeDynamicChannelsRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomePageBannerRepository
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BannerRepository @Inject constructor(
    private val homePageBannerRepository: HomePageBannerRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
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
        val atfData = AtfData(atfMetadata, data.banner, false)
        _flow.emit(atfData)
    }
}
