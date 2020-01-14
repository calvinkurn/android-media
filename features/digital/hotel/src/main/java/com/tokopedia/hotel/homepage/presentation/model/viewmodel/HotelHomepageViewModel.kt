package com.tokopedia.hotel.homepage.presentation.model.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.constant.TravelType
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.domain.TravelRecentSearchUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 04/04/19
 */
class HotelHomepageViewModel @Inject constructor(private val bannerUseCase: GetTravelCollectiveBannerUseCase,
                                                 private val travelRecentSearchUseCase: TravelRecentSearchUseCase,
                                                 dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val promoData = MutableLiveData<Result<TravelCollectiveBannerModel>>()

    private val _recentSearch = MutableLiveData<Result<List<TravelRecentSearchModel.Item>>>()
    val recentSearch: MutableLiveData<Result<List<TravelRecentSearchModel.Item>>>
        get() = _recentSearch

    fun getHotelPromo(rawQuery: String) {
        launch {
            promoData.value = bannerUseCase.execute(rawQuery, TravelType.HOTEL, true)
        }
    }

    fun getRecentSearch(rawQuery: String) {
        launchCatchError(block = {
            val data = travelRecentSearchUseCase.execute(rawQuery, true)
            _recentSearch.value = Success(data.items)
        }) {
            _recentSearch.value = Fail(it)
        }
    }
}