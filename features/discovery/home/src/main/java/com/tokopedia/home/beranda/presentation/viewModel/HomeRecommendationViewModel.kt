package com.tokopedia.home.beranda.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper.Companion.TYPE_BANNER_ADS
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper.Companion.TYPE_VERTICAL_BANNER_ADS
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
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
import javax.inject.Inject

class HomeRecommendationViewModel @Inject constructor(
    private val getHomeRecommendationUseCase: GetHomeRecommendationUseCase,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val getTopAdsHeadlineUseCase: GetTopAdsHeadlineUseCase,
    private val userSessionInterface: UserSessionInterface,
    private val topAdsAddressHelper: TopAdsAddressHelper,
    homeDispatcher: CoroutineDispatchers
) : BaseViewModel(homeDispatcher.io) {

    companion object {
        private const val TOPADS_TDN_RECOM_SOURCE = "1"
        private const val TOPADS_TDN_RECOM_VERTICAL_SOURCE = "29"
        private const val TOPADS_TDN_RECOM_DIMEN = 3
        private const val TOPADS_TDN_RECOM_VERTICAL_DIMEN = 10
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
            getHomeRecommendationUseCase.setParams(tabName, recommendationId, count, 1, locationParam, sourceType)
            val data = getHomeRecommendationUseCase.executeOnBackground()
            if (data.homeRecommendations.isEmpty()) {
                _homeRecommendationLiveData.postValue(data.copy(homeRecommendations = listOf(HomeRecommendationEmpty())))
            } else {
                try {
                    val headlineAds = fetchHeadlineAds(tabIndex)
                    val homeBannerTopAds = data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsDataModel>()
                    val homeBannerTopAdsMutable = data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsDataModel>().toMutableList()
                    val newList = data.homeRecommendations.toMutableList()
                    val topAdsBanner = arrayListOf<Pair<String, ArrayList<TopAdsImageViewModel>>>()
                    homeBannerTopAds.forEach {
                        if (it.bannerType == TYPE_BANNER_ADS) {
                            val bannerData = topAdsImageViewUseCase.getImageData(
                                topAdsImageViewUseCase.getQueryMap(
                                    query = "",
                                    source = TOPADS_TDN_RECOM_SOURCE,
                                    pageToken = "",
                                    adsCount = homeBannerTopAds.size,
                                    dimenId = TOPADS_TDN_RECOM_DIMEN,
                                    depId = "",
                                    page = topAdsBannerNextPage
                                ))
                            if (bannerData.isNotEmpty()) {
                                topAdsBanner.add(Pair(TYPE_BANNER_ADS, bannerData))
                            } else {
                                homeBannerTopAdsMutable.remove(it)
                                newList.remove(it)
                            }

                        } else if (it.bannerType == TYPE_VERTICAL_BANNER_ADS) {
                            val bannerData = topAdsImageViewUseCase.getImageData(
                                topAdsImageViewUseCase.getQueryMap(
                                    query = "",
                                    source = TOPADS_TDN_RECOM_VERTICAL_SOURCE,
                                    pageToken = "",
                                    adsCount = homeBannerTopAds.size,
                                    dimenId = TOPADS_TDN_RECOM_VERTICAL_DIMEN,
                                    depId = "",
                                    page = topAdsBannerNextPage
                                ))
                            if (bannerData.isNotEmpty()) {
                                topAdsBanner.add(Pair(TYPE_VERTICAL_BANNER_ADS, bannerData))
                            } else {
                                homeBannerTopAdsMutable.remove(it)
                                newList.remove(it)
                            }
                        }

                    }
                    handleTopAdsWidgets(data, topAdsBanner, homeBannerTopAdsMutable, headlineAds, newList)
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
        topAdsBanner: ArrayList<Pair<String, ArrayList<TopAdsImageViewModel>>>,
        homeBannerTopAds: List<HomeRecommendationBannerTopAdsDataModel>,
        headlineAds: TopAdsHeadlineResponse,
        newList: MutableList<HomeRecommendationVisitable>
    ) {
        incrementTopadsPage()
        val headlineData = headlineAds.displayAds.data
        var position: Int? = null
        if (!headlineData.isNullOrEmpty()) {
            position = headlineData.first().cpm.position
        }
        if (topAdsBanner.isEmpty()) {
            position?.let {
                if (newList.size >= position) {
                    newList.add(
                        it,
                        HomeRecommendationHeadlineTopAdsDataModel(headlineAds.displayAds)
                    )
                }
            }
        } else {
            if (position == Int.ZERO) {
                newList.add(position, HomeRecommendationHeadlineTopAdsDataModel(headlineAds.displayAds))
                topAdsBanner.forEachIndexed { index, pair ->
                    val visitableBanner = homeBannerTopAds[index]
                    if (newList.size > visitableBanner.position + Int.ONE) {
                        newList[visitableBanner.position + Int.ONE] =
                            HomeRecommendationBannerTopAdsDataModel(pair.second.firstOrNull(), bannerType = pair.first)
                    }
                }

            } else {
                topAdsBanner.forEachIndexed { index, pair ->
                    val visitableBanner = homeBannerTopAds[index]
                    if (newList.size > visitableBanner.position) {
                        newList[visitableBanner.position] =
                            HomeRecommendationBannerTopAdsDataModel(pair.second.firstOrNull(), bannerType = pair.first)
                    }
                }
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
        _homeRecommendationLiveData.postValue(data.copy(homeRecommendations = newList))
    }

    private suspend fun fetchHeadlineAds(tabIndex: Int): TopAdsHeadlineResponse {
        return if (tabIndex == Int.ZERO) {
            val params = getTopAdsHeadlineUseCase.createParams(
                userId = userSessionInterface.userId,
                page = TOPADS_PAGE_DEFAULT,
                src = SRC_HEADLINE_TOPADS,
                templateId = VALUE_TEMPLATE_ID,
                headlineProductCount = HEADLINE_PRODUCT_COUNT,
                item = VALUE_ITEM,
                seenAds = null
            )
            getTopAdsHeadlineUseCase.setParams(params, topAdsAddressHelper.getAddressData())
            getTopAdsHeadlineUseCase.executeOnBackground()
        } else {
            TopAdsHeadlineResponse()
        }
    }

    fun loadNextData(tabName: String, recomId: Int, count: Int, page: Int, locationParam: String = "", sourceType: String) {
        val list = _homeRecommendationLiveData.value?.homeRecommendations?.toMutableList()
            ?: mutableListOf()
        list.add(loadMoreModel)
        _homeRecommendationLiveData.postValue(
            _homeRecommendationLiveData.value?.copy(
                homeRecommendations = list.copy()
            )
        )
        launchCatchError(coroutineContext, block = {
            getHomeRecommendationUseCase.setParams(tabName, recomId, count, page, locationParam, sourceType)
            val data = getHomeRecommendationUseCase.executeOnBackground()
            list.remove(loadMoreModel)
            try {
                val homeBannerTopAds = data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsDataModel>()
                val homeBannerTopAdsMutable = data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsDataModel>().toMutableList()
                val newList = data.homeRecommendations.toMutableList()
                val topAdsBanner2 = arrayListOf<Pair<String, ArrayList<TopAdsImageViewModel>>>()
                homeBannerTopAds.forEachIndexed { index, it->
                    if (it.bannerType == TYPE_BANNER_ADS) {
                        val bannerData = topAdsImageViewUseCase.getImageData(
                            topAdsImageViewUseCase.getQueryMap(
                                query = "",
                                source = TOPADS_TDN_RECOM_SOURCE,
                                pageToken = "",
                                adsCount = homeBannerTopAds.size,
                                dimenId = TOPADS_TDN_RECOM_DIMEN,
                                depId = "",
                                page = topAdsBannerNextPage
                            ))
                        if (bannerData.isNotEmpty()) {
                            topAdsBanner2.add(Pair(TYPE_BANNER_ADS, bannerData))
                        } else {
                            homeBannerTopAdsMutable.remove(it)
                            list.remove(it)
                        }

                    } else if (it.bannerType == TYPE_VERTICAL_BANNER_ADS) {
                        val bannerData = topAdsImageViewUseCase.getImageData(
                            topAdsImageViewUseCase.getQueryMap(
                                query = "",
                                source = TOPADS_TDN_RECOM_VERTICAL_SOURCE,
                                pageToken = "",
                                adsCount = homeBannerTopAds.size,
                                dimenId = TOPADS_TDN_RECOM_VERTICAL_DIMEN,
                                depId = "",
                                page = topAdsBannerNextPage
                            ))
                        if (bannerData.isNotEmpty()) {
                            topAdsBanner2.add(Pair(TYPE_VERTICAL_BANNER_ADS, bannerData))
                        } else {
                            homeBannerTopAdsMutable.remove(it)
                            list.remove(it)
                        }
                    }
                }

                incrementTopadsPage()
                topAdsBanner2.forEachIndexed { index, pair ->
                    val visitableBanner = homeBannerTopAds[index]
                    if (newList.size > visitableBanner.position) {
                        newList[visitableBanner.position] =
                            HomeRecommendationBannerTopAdsDataModel(pair.second.firstOrNull(), bannerType = pair.first)
                    }
                }
                list.addAll(newList)
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
        val list = _homeRecommendationLiveData.value?.homeRecommendations?.toMutableList()
            ?: mutableListOf()
        var recommendationItem: HomeRecommendationItemDataModel? = null
        var recommendationItemPosition: Int = -1
        if (list.getOrNull(position)?.getUniqueIdentity() == id) {
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
            _homeRecommendationLiveData.postValue(_homeRecommendationLiveData.value?.copy(homeRecommendations = list.copy()))
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
