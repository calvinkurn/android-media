package com.tokopedia.home.beranda.data.newatf.banner

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.BaseAtfRepository
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomePageBannerRepository
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import javax.inject.Inject

/**
 * Created by Frenzel
 */
@HomeScope
class HomepageBannerRepository @Inject constructor(
    private val homePageBannerRepository: HomePageBannerRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
): BaseAtfRepository() {

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
        val (data, status) = try {
            homePageBannerRepository.getRemoteData(bannerParam).banner to AtfKey.STATUS_SUCCESS
        } catch (_: Exception) {
            BannerDataModel() to AtfKey.STATUS_ERROR
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
