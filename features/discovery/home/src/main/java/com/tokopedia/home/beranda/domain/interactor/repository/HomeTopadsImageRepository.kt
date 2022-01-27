package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import dagger.Lazy
import javax.inject.Inject

/**
 * @author by devara on 2020-02-18
 */

class HomeTopadsImageRepository @Inject constructor(
        private val topAdsImageViewUseCase: Lazy<TopAdsImageViewUseCase>,
        private val irisSession: TopAdsIrisSession
        )
    : HomeRepository<ArrayList<TopAdsImageViewModel>> {

    companion object {
        private const val TOP_ADS_BANNER_DIMEN_ID = 3
        private const val TOP_ADS_COUNT = 1
        private const val TOP_ADS_HOME_SOURCE = "1"
    }

    override suspend fun getRemoteData(bundle: Bundle): ArrayList<TopAdsImageViewModel> {
        val results = topAdsImageViewUseCase.get().getImageData(
            topAdsImageViewUseCase.get().getQueryMap(
                "",
                TOP_ADS_HOME_SOURCE,
                "",
                TOP_ADS_COUNT,
                TOP_ADS_BANNER_DIMEN_ID,
                ""
            ), irisSession.getSessionId()
        )
        return results
    }

    override suspend fun getCachedData(bundle: Bundle): ArrayList<TopAdsImageViewModel> {
        return arrayListOf()
    }
}