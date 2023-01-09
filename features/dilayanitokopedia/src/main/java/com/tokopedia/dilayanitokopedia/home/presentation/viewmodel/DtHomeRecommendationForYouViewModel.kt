package com.tokopedia.dilayanitokopedia.home.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dilayanitokopedia.home.domain.mapper.recommendationforyou.HomeRecommendationMapper
import com.tokopedia.dilayanitokopedia.home.domain.usecase.DtGetRecommendationForYouUseCase
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationEmpty
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationError
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationLoading
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class DtHomeRecommendationForYouViewModel @Inject constructor(
    private val dtGetRecommendationForYouUseCase: DtGetRecommendationForYouUseCase,
    homeDispatcher: CoroutineDispatchers
) : BaseViewModel(homeDispatcher.io) {

    companion object {
        private const val TAB_DILAYANI_TOKOPEDIA = "dt"
    }

    val homeRecommendationLiveData get() = _homeRecommendationLiveData
    private val _homeRecommendationLiveData: MutableLiveData<HomeRecommendationDataModel> = MutableLiveData()

    private val loadingModel = HomeRecommendationLoading()

    fun loadInitialPage(locationParamString: String) {
        launchCatchError(coroutineContext, block = {
            val data = dtGetRecommendationForYouUseCase.execute(locationParamString)
            val visitableData = HomeRecommendationMapper.mapToHomeRecommendationDataModel(data, TAB_DILAYANI_TOKOPEDIA, 1)
            if (data.products.isEmpty()) {
                _homeRecommendationLiveData.postValue(
                    HomeRecommendationDataModel(homeRecommendations = listOf(HomeRecommendationEmpty()))
                )
            } else {
                _homeRecommendationLiveData.postValue(visitableData)
            }
        }) {
            _homeRecommendationLiveData.postValue(HomeRecommendationDataModel(homeRecommendations = listOf(HomeRecommendationError())))
        }
    }

    fun loadLoading() {
        _homeRecommendationLiveData.postValue(HomeRecommendationDataModel(homeRecommendations = listOf(loadingModel)))
    }
}
