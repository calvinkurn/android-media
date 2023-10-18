package com.tokopedia.home.beranda.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.GetHomeRecommendationCardUseCase
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import com.tokopedia.topads.sdk.utils.VALUE_ITEM
import com.tokopedia.topads.sdk.utils.VALUE_TEMPLATE_ID
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import javax.inject.Inject

class HomeRecommendationViewModel @Inject constructor(
    private val getHomeRecommendationUseCase: Lazy<GetHomeRecommendationUseCase>,
    private val getHomeRecommendationCardUseCase: Lazy<GetHomeRecommendationCardUseCase>,
    private val topAdsImageViewUseCase: Lazy<TopAdsImageViewUseCase>,
    private val getTopAdsHeadlineUseCase: Lazy<GetTopAdsHeadlineUseCase>,
    private val userSessionInterface: Lazy<UserSessionInterface>,
    private val topAdsAddressHelper: Lazy<TopAdsAddressHelper>,
    homeDispatcher: Lazy<CoroutineDispatchers>
) : BaseViewModel(homeDispatcher.get().io) {

    companion object {
        private const val TOPADS_TDN_RECOM_SOURCE = "1"
        private const val TOPADS_TDN_RECOM_DIMEN = 3
        private const val TOPADS_PAGE_DEFAULT = "1"
        private const val SRC_HEADLINE_TOPADS = "homepage_foryou"
        private const val HEADLINE_PRODUCT_COUNT = "2"
    }
    val homeRecommendationLiveData get() = _homeRecommendationLiveData
    private val _homeRecommendationLiveData: MutableLiveData<HomeRecommendationDataModel> = MutableLiveData()
    val homeRecommendationNetworkLiveData get() = _homeRecommendationNetworkLiveData
    private val _homeRecommendationNetworkLiveData: MutableLiveData<Result<HomeRecommendationDataModel>> = MutableLiveData()
    private val loadingModel = HomeRecommendationLoading()
    private val loadMoreModel = HomeRecommendationLoadMore()

    var topAdsBannerNextPage = TOPADS_PAGE_DEFAULT

    fun loadInitialPage(
        tabName: String,
        recommendationId: Int,
        count: Int,
        locationParam: String = "",
        tabIndex: Int = 0,
        sourceType: String
    ) {
        _homeRecommendationLiveData.postValue(HomeRecommendationDataModel(homeRecommendations = listOf(loadingModel)))
        launchCatchError(coroutineContext, block = {
            // todo will remove
//            getHomeRecommendationUseCase.get().setParams(tabName, recommendationId, count, 1, locationParam, sourceType)
            val data = getHomeRecommendationCardUseCase.get().execute(Int.ONE, sourceType, locationParam)
            if (data.homeRecommendations.isEmpty()) {
                _homeRecommendationLiveData.postValue(data.copy(homeRecommendations = listOf(HomeRecommendationEmpty())))
            } else {
                try {
                    val headlineAds = fetchHeadlineAds(tabIndex)
                    val homeBannerTopAds = data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsDataModel>()
                    var topAdsBanner = arrayListOf<TopAdsImageViewModel>()
                    if (homeBannerTopAds.isNotEmpty()) {
                        topAdsBanner = topAdsImageViewUseCase.get().getImageData(
                            topAdsImageViewUseCase.get().getQueryMap(
                                query = "",
                                source = TOPADS_TDN_RECOM_SOURCE,
                                pageToken = "",
                                adsCount = homeBannerTopAds.size,
                                dimenId = TOPADS_TDN_RECOM_DIMEN,
                                depId = "",
                                page = topAdsBannerNextPage
                            )
                        )
                    }
                    handleTopAdsWidgets(data, topAdsBanner, homeBannerTopAds, headlineAds)
                } catch (e: Exception) {
                    _homeRecommendationLiveData.postValue(
                        data.copy(
                            homeRecommendations = data.homeRecommendations.filter { it !is HomeRecommendationBannerTopAdsDataModel }
                        )
                    )
                }
            }
            _homeRecommendationNetworkLiveData.postValue(Result.success(data))
        }) {
            _homeRecommendationLiveData.postValue(HomeRecommendationDataModel(homeRecommendations = listOf(HomeRecommendationError())))
            _homeRecommendationNetworkLiveData.postValue(Result.failure(it))
        }
    }

    private fun handleTopAdsWidgets(
        data: HomeRecommendationDataModel,
        topAdsBanner: ArrayList<TopAdsImageViewModel>,
        homeBannerTopAds: List<HomeRecommendationBannerTopAdsDataModel>,
        headlineAds: TopAdsHeadlineResponse
    ) {
        incrementTopadsPage()
        val newList = data.homeRecommendations.toMutableList()
        val headlineData = headlineAds.displayAds.data
        var position: Int? = null
        if (!headlineData.isNullOrEmpty()) {
            position = headlineData.first().cpm.position
        }
        if (topAdsBanner.isEmpty()) {
            homeBannerTopAds.firstOrNull()?.let { newList.remove(it) }
            position?.let {
                if (newList.size >= position) {
                    newList.add(
                        it,
                        HomeRecommendationHeadlineTopAdsDataModel(headlineAds.displayAds)
                    )
                }
            }
        } else {
            topAdsBanner.forEachIndexed { index, topAdsImageViewModel ->
                val visitableBanner = homeBannerTopAds[index]
                if (position == Int.ZERO) {
                    newList.add(position, HomeRecommendationHeadlineTopAdsDataModel(headlineAds.displayAds))
                    if (newList.size > visitableBanner.position + Int.ONE) {
                        newList[visitableBanner.position + Int.ONE] =
                            HomeRecommendationBannerTopAdsDataModel(topAdsImageViewModel)
                    }
                } else {
                    if (newList.size > visitableBanner.position) {
                        newList[visitableBanner.position] =
                            HomeRecommendationBannerTopAdsDataModel(topAdsImageViewModel)
                    }

                    position?.let {
                        if (newList.size >= position + Int.ONE) {
                            newList.add(
                                it + Int.ONE,
                                HomeRecommendationHeadlineTopAdsDataModel(headlineAds.displayAds)
                            )
                        }
                    }
                }
            }
        }
        _homeRecommendationLiveData.postValue(data.copy(homeRecommendations = newList))
    }

    private suspend fun fetchHeadlineAds(tabIndex: Int): TopAdsHeadlineResponse {
        return if (tabIndex == Int.ZERO) {
            val params = getTopAdsHeadlineUseCase.get().createParams(
                userId = userSessionInterface.get().userId,
                page = TOPADS_PAGE_DEFAULT,
                src = SRC_HEADLINE_TOPADS,
                templateId = VALUE_TEMPLATE_ID,
                headlineProductCount = HEADLINE_PRODUCT_COUNT,
                item = VALUE_ITEM,
                seenAds = null
            )
            getTopAdsHeadlineUseCase.get().setParams(params, topAdsAddressHelper.get().getAddressData())
            getTopAdsHeadlineUseCase.get().executeOnBackground()
        } else {
            TopAdsHeadlineResponse()
        }
    }

    fun loadNextData(tabName: String, recomId: Int, count: Int, page: Int, locationParam: String = "", sourceType: String) {
        val list = _homeRecommendationLiveData.value?.homeRecommendations?.toMutableList() ?: mutableListOf()
        list.add(loadMoreModel)
        _homeRecommendationLiveData.postValue(
            _homeRecommendationLiveData.value?.copy(
                homeRecommendations = list.toList().copy()
            )
        )
        launchCatchError(coroutineContext, block = {
            // todo will remove
//            getHomeRecommendationUseCase.get().setParams(tabName, recomId, count, page, locationParam, sourceType)
            val data = getHomeRecommendationCardUseCase.get().execute(page, sourceType, locationParam)
            list.remove(loadMoreModel)
            try {
                val homeBannerTopAds = data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsDataModel>()
                val topAdsBanner = topAdsImageViewUseCase.get().getImageData(
                    topAdsImageViewUseCase.get().getQueryMap(
                        query = "",
                        source = TOPADS_TDN_RECOM_SOURCE,
                        pageToken = "",
                        adsCount = homeBannerTopAds.size,
                        dimenId = TOPADS_TDN_RECOM_DIMEN,
                        depId = "",
                        page = topAdsBannerNextPage
                    )
                )
                incrementTopadsPage()
                if (topAdsBanner.isEmpty()) {
                    list.addAll(data.homeRecommendations.filter { it !is HomeRecommendationBannerTopAdsDataModel })
                } else {
                    val newList = data.homeRecommendations.toMutableList()
                    topAdsBanner.forEachIndexed { index, topAdsImageViewModel ->
                        val visitableBanner = homeBannerTopAds[index]
                        newList[visitableBanner.position] = HomeRecommendationBannerTopAdsDataModel(topAdsImageViewModel)
                    }
                    list.addAll(newList)
                }
            } catch (e: Exception) {
                list.addAll(data.homeRecommendations.filter { it !is HomeRecommendationBannerTopAdsDataModel })
            }
            _homeRecommendationLiveData.postValue(data.copy(homeRecommendations = list.copy()))
            _homeRecommendationNetworkLiveData.postValue(Result.success(data))
        }) {
            list.remove(loadMoreModel)
            _homeRecommendationLiveData.postValue(_homeRecommendationLiveData.value?.copy(homeRecommendations = list.copy()))
            _homeRecommendationNetworkLiveData.postValue(Result.failure(it))
        }
    }

    fun updateWishlist(id: String, position: Int, isWishlisted: Boolean) {
        val homeRecomendationList = _homeRecommendationLiveData.value?.homeRecommendations?.toMutableList() ?: mutableListOf()
        var recommendationItem: HomeRecommendationItemDataModel? = null
        var recommendationItemPosition: Int = -1
        if (homeRecomendationList.getOrNull(position)?.getUniqueIdentity() == id) {
            recommendationItem = homeRecomendationList[position] as HomeRecommendationItemDataModel
            recommendationItemPosition = position
        } else {
            homeRecomendationList.withIndex().find { it.value.getUniqueIdentity() == id && it.value is HomeRecommendationItemDataModel }?.let {
                recommendationItemPosition = it.index
                recommendationItem = (it.value as? HomeRecommendationItemDataModel)
            }
        }
        if (recommendationItemPosition != -1) {
            recommendationItem?.let {
                homeRecomendationList[recommendationItemPosition] = it.copy(
                    recommendationCard = it.recommendationCard.copy(isWishlist = isWishlisted)
                )
                _homeRecommendationLiveData.postValue(
                    _homeRecommendationLiveData.value?.copy(
                        homeRecommendations = homeRecomendationList.toList().copy()
                    )
                )
            }
        }
    }

    private fun incrementTopadsPage() {
        topAdsBannerNextPage = try {
            val currentPage = topAdsBannerNextPage.toIntOrZero()
            (currentPage + 1).toString()
        } catch (e: Exception) {
            TOPADS_PAGE_DEFAULT
        }
    }
}
