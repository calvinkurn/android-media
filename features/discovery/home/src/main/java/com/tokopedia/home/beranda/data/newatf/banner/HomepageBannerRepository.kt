package com.tokopedia.home.beranda.data.newatf.banner

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.AtfRepository
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomePageBannerRepository
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import javax.inject.Inject

@HomeScope
class HomepageBannerRepository @Inject constructor(
    private val homePageBannerRepository: HomePageBannerRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
    atfDao: AtfDao,
): AtfRepository(atfDao) {

    @SuppressLint("PII Data Exposure")
    override suspend fun getData(atfMetadata: AtfMetadata) {
        val bannerParam = Bundle().apply {
            putString(
                HomePageBannerRepository.PARAM,
                atfMetadata.param
            )
            putString(
                HomePageBannerRepository.PARAM_LOCATION,
                homeChooseAddressRepository.getRemoteData()
                    ?.convertToLocationParams()
            )
        }
        val data = homePageBannerRepository.getRemoteData(bannerParam)
        val atfData = AtfData(atfMetadata, data.banner, isCache = false)
        emitAndSaveData(atfData)
    }
}
