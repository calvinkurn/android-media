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
        const val VERTICAL: String = "vertical"
        const val HORIZONTAL: String = "horizontal"
        const val TOP_ADS_BANNER_TYPE = "type"
        private const val TOP_ADS_BANNER_DIMEN_ID = 3
        private const val TOP_ADS_VERTICAL_BANNER_DIMEN_ID = 10
        private const val TOP_ADS_COUNT = 1
        private const val VERTICAL_TOP_ADS_COUNT = 12
        private const val TOP_ADS_HOME_SOURCE = "1"
        private const val VERTICAL_TOP_ADS_HOME_SOURCE = "23"
        private const val TOP_ADS_PAGE_DEFAULT = "1"

        const val TOP_ADS_PAGE = "topadsPage"
    }

    override suspend fun getRemoteData(bundle: Bundle): ArrayList<TopAdsImageViewModel> {
        val page = bundle.getString(TOP_ADS_PAGE, TOP_ADS_PAGE_DEFAULT)
        val type = bundle.getString(TOP_ADS_BANNER_TYPE, HORIZONTAL)
        return topAdsImageViewUseCase.get().getImageData(
            if (type == HORIZONTAL)
                getQueryMap(TOP_ADS_HOME_SOURCE, TOP_ADS_COUNT, TOP_ADS_BANNER_DIMEN_ID, page)
            else getQueryMap(VERTICAL_TOP_ADS_HOME_SOURCE, VERTICAL_TOP_ADS_COUNT, TOP_ADS_VERTICAL_BANNER_DIMEN_ID)
        )
    }

    private fun getQueryMap(
        source: String,
        adsCount: Int,
        dimenId: Int,
        page: String = ""
    ): MutableMap<String, Any> {
        return topAdsImageViewUseCase.get().getQueryMap(
            query = "",
            source = source,
            pageToken = "",
            adsCount = adsCount,
            dimenId = dimenId,
            depId = "",
            page = page
        )
    }

    override suspend fun getCachedData(bundle: Bundle): ArrayList<TopAdsImageViewModel> {
        return arrayListOf()
    }
}
