package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home_component.usecase.featuredshop.DisplayHeadlineAdsEntity
import com.tokopedia.home_component.usecase.featuredshop.GetDisplayHeadlineAds
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import dagger.Lazy
import javax.inject.Inject

/**
 * @author by devara on 2020-02-18
 */

class HomeHeadlineAdsRepository @Inject constructor(
        private val getDisplayHeadlineAds: Lazy<GetDisplayHeadlineAds>,
        private val topAdsAddressHelper: Lazy<TopAdsAddressHelper>
        )
    : HomeRepository<List<DisplayHeadlineAdsEntity.DisplayHeadlineAds>> {

    companion object {
        const val WIDGET_PARAM = "widgetParam"
    }

    override suspend fun getRemoteData(bundle: Bundle): List<DisplayHeadlineAdsEntity.DisplayHeadlineAds> {
        getDisplayHeadlineAds.get().createParams(bundle.getString(WIDGET_PARAM, ""), topAdsAddressHelper.get().getAddressData())
        return getDisplayHeadlineAds.get().executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): List<DisplayHeadlineAdsEntity.DisplayHeadlineAds> {
        return listOf()
    }
}