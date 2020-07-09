package com.tokopedia.home.beranda.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.Job
import javax.inject.Inject

class HomeRecommendationViewModel @Inject constructor(
        private val getHomeRecommendationUseCase: GetHomeRecommendationUseCase,
        homeDispatcher: HomeDispatcherProvider
) : BaseViewModel(homeDispatcher.io()){
    val homeRecommendationLiveData get() = _homeRecommendationLiveData
    private val _homeRecommendationLiveData: MutableLiveData<HomeRecommendationDataModel> = MutableLiveData()
    val homeRecommendationNetworkLiveData get() = _homeRecommendationNetworkLiveData
    private val _homeRecommendationNetworkLiveData: MutableLiveData<Result<HomeRecommendationDataModel>> = MutableLiveData()
    private val loadingModel = HomeRecommendationLoading()
    private val loadMoreModel = HomeRecommendationLoadMore()
    private var recommendationJob: Job? = null
    fun loadInitialPage(tabName: String, recommendationId: Int,count: Int){
        if(recommendationJob?.isActive == true) return
        _homeRecommendationLiveData.postValue(HomeRecommendationDataModel(homeRecommendations = listOf(loadingModel)))
        recommendationJob = launchCatchError(coroutineContext, block = {
            getHomeRecommendationUseCase.setParams(tabName, recommendationId, count, 1)
            val data = getHomeRecommendationUseCase.executeOnBackground()
            if(data.homeRecommendations.isEmpty()){
                _homeRecommendationLiveData.postValue(data.copy(homeRecommendations = listOf(HomeRecommendationEmpty())))
            } else {
                _homeRecommendationLiveData.postValue(data)
            }
            _homeRecommendationNetworkLiveData.postValue(Result.success(data))
        }){
            _homeRecommendationLiveData.postValue(HomeRecommendationDataModel(homeRecommendations = listOf(HomeRecommendationError())))
            _homeRecommendationNetworkLiveData.postValue(Result.failure(it))
        }
    }

    fun loadNextData(tabName: String, recomId: Int, count: Int, page: Int) {
        if(recommendationJob?.isActive == true) return
        val list = _homeRecommendationLiveData.value?.homeRecommendations?.toMutableList() ?: mutableListOf()
        list.add(loadMoreModel)
        _homeRecommendationLiveData.postValue(_homeRecommendationLiveData.value?.copy(
                homeRecommendations = list.copy()
        ))
        recommendationJob = launchCatchError(coroutineContext, block = {
            getHomeRecommendationUseCase.setParams(tabName, recomId, count, page)
            val data = getHomeRecommendationUseCase.executeOnBackground()
            list.remove(loadMoreModel)
            list.addAll(data.homeRecommendations)
            _homeRecommendationNetworkLiveData.postValue(Result.success(data))
            _homeRecommendationLiveData.postValue(data.copy(homeRecommendations = list.copy()))
        }){
            list.remove(loadMoreModel)
            _homeRecommendationLiveData.postValue(_homeRecommendationLiveData.value?.copy(homeRecommendations = list.copy()))
            _homeRecommendationNetworkLiveData.postValue(Result.failure(it))
        }
    }

    fun updateWishlist(id: String, position: Int, isWishlisted: Boolean){
        val list = _homeRecommendationLiveData.value?.homeRecommendations?.toMutableList() ?: mutableListOf()
        var recommendationItem: HomeRecommendationItemDataModel? = null
        var recommendationItemPosition: Int = -1
        if(list.getOrNull(position)?.getUniqueIdentity() == id){
            recommendationItem = list[position] as HomeRecommendationItemDataModel
            recommendationItemPosition = position
        } else {
            list.withIndex().find { it.value.getUniqueIdentity() == id && it.value is HomeRecommendationItemDataModel }?.let {
                recommendationItemPosition = it.index
                recommendationItem = (it.value as HomeRecommendationItemDataModel)
            }
        }
        if(recommendationItemPosition != -1 && recommendationItem != null){
            list[recommendationItemPosition] = recommendationItem!!.copy(
                    product = recommendationItem!!.product.copy(isWishlist = isWishlisted)
            )
            _homeRecommendationLiveData.postValue(_homeRecommendationLiveData.value?.copy(homeRecommendations = list.copy()))
        }
    }
}