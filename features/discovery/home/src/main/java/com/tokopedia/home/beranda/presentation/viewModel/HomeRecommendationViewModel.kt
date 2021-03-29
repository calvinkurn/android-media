package com.tokopedia.home.beranda.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.convertToLocationParams
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import javax.inject.Inject

class HomeRecommendationViewModel @Inject constructor(
        private val getHomeRecommendationUseCase: GetHomeRecommendationUseCase,
        private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
        homeDispatcher: CoroutineDispatchers
) : BaseViewModel(homeDispatcher.io){
    val homeRecommendationLiveData get() = _homeRecommendationLiveData
    private val _homeRecommendationLiveData: MutableLiveData<HomeRecommendationDataModel> = MutableLiveData()
    val homeRecommendationNetworkLiveData get() = _homeRecommendationNetworkLiveData
    private val _homeRecommendationNetworkLiveData: MutableLiveData<Result<HomeRecommendationDataModel>> = MutableLiveData()
    private val loadingModel = HomeRecommendationLoading()
    private val loadMoreModel = HomeRecommendationLoadMore()

    var topAdsBannerNextPageToken = ""

    fun loadInitialPage(tabName: String, recommendationId: Int,count: Int, locationParam: String = ""){
        _homeRecommendationLiveData.postValue(HomeRecommendationDataModel(homeRecommendations = listOf(loadingModel)))
        launchCatchError(coroutineContext, block = {
            getHomeRecommendationUseCase.setParams(tabName, recommendationId, count, 1, locationParam)
            val data = getHomeRecommendationUseCase.executeOnBackground()
            if(data.homeRecommendations.isEmpty()){
                _homeRecommendationLiveData.postValue(data.copy(homeRecommendations = listOf(HomeRecommendationEmpty())))
            } else {
                try{
                    val homeBannerTopAds = data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsDataModel>()
                    var topAdsBanner = arrayListOf<TopAdsImageViewModel>()
                    if (homeBannerTopAds.isNotEmpty()) {
                        topAdsBanner = topAdsImageViewUseCase.getImageData(
                                topAdsImageViewUseCase.getQueryMap(
                                        "",
                                        "1",
                                        topAdsBannerNextPageToken,
                                        homeBannerTopAds.size,
                                        3,
                                        ""
                                )
                        )
                    }
                    if(topAdsBanner.isEmpty()){
                        _homeRecommendationLiveData.postValue(data.copy(
                                homeRecommendations = data.homeRecommendations.filter { it !is HomeRecommendationBannerTopAdsDataModel}
                        ))
                    } else {
                        val newList = data.homeRecommendations.toMutableList()
                        topAdsBanner.forEachIndexed { index, topAdsImageViewModel ->
                            val visitableBanner = homeBannerTopAds[index]
                            newList[visitableBanner.position] = HomeRecommendationBannerTopAdsDataModel(topAdsImageViewModel)
                            topAdsBannerNextPageToken = topAdsImageViewModel.nextPageToken ?: ""
                        }
                        _homeRecommendationLiveData.postValue(data.copy(homeRecommendations = newList))
                    }
                } catch (e: Exception){
                    _homeRecommendationLiveData.postValue(data.copy(
                            homeRecommendations = data.homeRecommendations.filter { it !is HomeRecommendationBannerTopAdsDataModel}
                    ))
                }
            }
            _homeRecommendationNetworkLiveData.postValue(Result.success(data))
        }){
            _homeRecommendationLiveData.postValue(HomeRecommendationDataModel(homeRecommendations = listOf(HomeRecommendationError())))
            _homeRecommendationNetworkLiveData.postValue(Result.failure(it))
        }
    }

    fun loadNextData(tabName: String, recomId: Int, count: Int, page: Int, locationParam: String = "") {
        val list = _homeRecommendationLiveData.value?.homeRecommendations?.toMutableList() ?: mutableListOf()
        list.add(loadMoreModel)
        _homeRecommendationLiveData.postValue(_homeRecommendationLiveData.value?.copy(
                homeRecommendations = list.copy()
        ))
        launchCatchError(coroutineContext, block = {
            getHomeRecommendationUseCase.setParams(tabName, recomId, count, page, locationParam)
            val data = getHomeRecommendationUseCase.executeOnBackground()
            list.remove(loadMoreModel)
            try{
                val homeBannerTopAds = data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsDataModel>()
                val topAdsBanner = topAdsImageViewUseCase.getImageData(
                        topAdsImageViewUseCase.getQueryMap(
                                "",
                                "1",
                                topAdsBannerNextPageToken,
                                homeBannerTopAds.size,
                                3,
                                ""
                        )
                )
                if(topAdsBanner.isEmpty()){
                    list.addAll(data.homeRecommendations.filter { it !is HomeRecommendationBannerTopAdsDataModel})
                } else {
                    val newList = data.homeRecommendations.toMutableList()
                    topAdsBanner.forEachIndexed { index, topAdsImageViewModel ->
                        val visitableBanner = homeBannerTopAds[index]
                        newList[visitableBanner.position] = HomeRecommendationBannerTopAdsDataModel(topAdsImageViewModel)
                        topAdsBannerNextPageToken = topAdsImageViewModel.nextPageToken ?: ""
                    }
                    list.addAll(newList)
                }
            }catch (e: Exception){
                list.addAll(data.homeRecommendations.filter { it !is HomeRecommendationBannerTopAdsDataModel})
            }
            _homeRecommendationLiveData.postValue(data.copy(homeRecommendations = list.copy()))
            _homeRecommendationNetworkLiveData.postValue(Result.success(data))
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