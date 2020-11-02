package com.tokopedia.common.travel.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import javax.inject.Inject

/**
 * @author by furqan on 02/11/2020
 */
class TravelVideoBannerViewModel @Inject constructor(
        private val bannerUseCase: GetTravelCollectiveBannerUseCase,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {


}