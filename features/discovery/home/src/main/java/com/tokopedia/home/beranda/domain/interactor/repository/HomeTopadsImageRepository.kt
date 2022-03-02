package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import dagger.Lazy
import javax.inject.Inject

/**
 * @author by devara on 2020-02-18
 */

class HomeTopadsImageRepository @Inject constructor(
        private val topAdsImageViewUseCase: Lazy<TopAdsImageViewUseCase>
        )
    : HomeRepository<ArrayList<TopAdsImageViewModel>> {

    companion object {
        private const val TOP_ADS_BANNER_DIMEN_ID = 3
        private const val TOP_ADS_COUNT = 1
        private const val TOP_ADS_HOME_SOURCE = "1"
        private const val TOP_ADS_PAGE_DEFAULT = "1"

        const val TOP_ADS_PAGE = "topadsPage"
    }

    override suspend fun getRemoteData(bundle: Bundle): ArrayList<TopAdsImageViewModel> {
        val page = bundle.getString(TOP_ADS_PAGE, TOP_ADS_PAGE_DEFAULT)
        val results = topAdsImageViewUseCase.get().getImageData(
                topAdsImageViewUseCase.get().getQueryMap(
                        query = "",
                        source = TOP_ADS_HOME_SOURCE,
                        pageToken = "",
                        adsCount = TOP_ADS_COUNT,
                        dimenId = TOP_ADS_BANNER_DIMEN_ID,
                        depId = "",
                        page = page
                )
        )
        return results
    }

    override suspend fun getCachedData(bundle: Bundle): ArrayList<TopAdsImageViewModel> {
        return arrayListOf()
    }
}