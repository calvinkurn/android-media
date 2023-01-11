package com.tokopedia.dilayanitokopedia.home.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dilayanitokopedia.home.domain.mapper.recommendationforyou.HomeRecommendationMapper
import com.tokopedia.dilayanitokopedia.home.domain.usecase.GetRecommendationForYouUseCase
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationEmpty
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationError
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationLoadMore
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationLoading
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.HomeRecommendationVisitable
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class DtHomeRecommendationForYouViewModel @Inject constructor(
    private val getRecommendationForYouUseCase: GetRecommendationForYouUseCase,
    homeDispatcher: CoroutineDispatchers
) : BaseViewModel(homeDispatcher.io) {

    companion object {
        const val TAB_DILAYANI_TOKOPEDIA = "dt"
    }

    val homeRecommendationLiveData get() = _homeRecommendationLiveData
    private val _homeRecommendationLiveData: MutableLiveData<HomeRecommendationDataModel> = MutableLiveData()

    private val loadingModel = HomeRecommendationLoading()
    private val loadMoreModel = HomeRecommendationLoadMore()

    fun loadInitialPage(locationParamString: String) {
        val INIT_NUMBER = 1
        launchCatchError(coroutineContext, block = {
            val data = getRecommendationForYouUseCase.execute(locationParamString, INIT_NUMBER)
            val visitableData =
                HomeRecommendationMapper.mapToHomeRecommendationDataModel(data, TAB_DILAYANI_TOKOPEDIA, INIT_NUMBER)
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

    fun loadNextData(page: Int, locationParamString: String = "") {
        val list = _homeRecommendationLiveData.value?.homeRecommendations?.toMutableList() ?: mutableListOf()
        list.addLoadMore()
        _homeRecommendationLiveData.postValue(_homeRecommendationLiveData.value?.copy(homeRecommendations = list))

        launchCatchError(coroutineContext, block = {
            val data = getRecommendationForYouUseCase.execute(locationParamString, page)
            val visitableData = HomeRecommendationMapper.mapToHomeRecommendationDataModel(data, TAB_DILAYANI_TOKOPEDIA, page)
            list.remove(loadMoreModel)
            list.addAll(visitableData.homeRecommendations)
            _homeRecommendationLiveData.postValue(visitableData.copy(homeRecommendations = list))
        }) {
            list.remove(loadMoreModel)
            _homeRecommendationLiveData.postValue(_homeRecommendationLiveData.value?.copy(homeRecommendations = list))
        }
    }

    private fun MutableList<HomeRecommendationVisitable>.addLoadMore() {
        val isContainLoadMore = this.contains(loadMoreModel)
        if (!isContainLoadMore) {
            val list = _homeRecommendationLiveData.value?.homeRecommendations?.toMutableList() ?: mutableListOf()
            list.add(loadMoreModel)
        }
    }

    private fun MutableList<HomeRecommendationVisitable>.removeLoadMore() {
        this.remove(loadMoreModel)
    }
}
