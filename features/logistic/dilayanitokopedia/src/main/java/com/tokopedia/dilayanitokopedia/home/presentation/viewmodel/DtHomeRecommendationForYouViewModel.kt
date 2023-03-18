package com.tokopedia.dilayanitokopedia.home.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dilayanitokopedia.home.domain.mapper.recommendationforyou.HomeRecommendationMapper
import com.tokopedia.dilayanitokopedia.home.domain.usecase.GetRecommendationForYouUseCase
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationEmpty
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationError
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationItemDataModel
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
        val defaultPage = 1
        launchCatchError(coroutineContext, block = {
            val data = getRecommendationForYouUseCase(GetRecommendationForYouUseCase.getParam(location = locationParamString, productPage = defaultPage))
            val visitableData =
                HomeRecommendationMapper.mapToHomeRecommendationDataModel(data.response, TAB_DILAYANI_TOKOPEDIA, defaultPage)
            if (data.response.products.isEmpty()) {
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
            val data = getRecommendationForYouUseCase(GetRecommendationForYouUseCase.getParam(location = locationParamString, productPage = page))
            val visitableData = HomeRecommendationMapper.mapToHomeRecommendationDataModel(data.response, TAB_DILAYANI_TOKOPEDIA, page)
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

    fun updateWishlist(id: String, position: Int, isWishlisted: Boolean) {
        val list = _homeRecommendationLiveData.value?.homeRecommendations?.toMutableList() ?: mutableListOf()
        var recommendationItem: HomeRecommendationItemDataModel? = null
        var recommendationItemPosition: Int = -1
        if (list.getOrNull(position)?.getUniqueIdentity().toString() == id) {
            recommendationItem = list[position] as HomeRecommendationItemDataModel
            recommendationItemPosition = position
        } else {
            list.withIndex().find { it.value.getUniqueIdentity() == id && it.value is HomeRecommendationItemDataModel }?.let {
                recommendationItemPosition = it.index
                recommendationItem = (it.value as HomeRecommendationItemDataModel)
            }
        }
        if (recommendationItemPosition != -1 && recommendationItem != null) {
            list[recommendationItemPosition] = recommendationItem!!.copy(
                product = recommendationItem!!.product.copy(isWishlist = isWishlisted)
            )
            _homeRecommendationLiveData.postValue(_homeRecommendationLiveData.value?.copy(homeRecommendations = list.toList()))
        }
    }
}
