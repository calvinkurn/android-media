package com.tokopedia.home.beranda.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper.Companion.TYPE_BANNER_ADS
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper.Companion.TYPE_VERTICAL_BANNER_ADS
import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.GetHomeRecommendationCardUseCase
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.view.helper.HomeRecommendationController
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationCardState
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
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
        private const val TOPADS_TDN_RECOM_VERTICAL_SOURCE = "29"
        private const val TOPADS_TDN_RECOM_DIMEN = 3
        private const val TOPADS_TDN_RECOM_VERTICAL_DIMEN = 10
        private const val TOPADS_PAGE_DEFAULT = "1"
        private const val SRC_HEADLINE_TOPADS = "homepage_foryou"
        private const val HEADLINE_PRODUCT_COUNT = "2"
    }

    private val loadingModel = HomeRecommendationLoading()
    private val loadMoreModel = HomeRecommendationLoadMore()
    val buttonRetryUiModel = HomeRecommendationButtonRetryUiModel()
    val emptyModel = HomeRecommendationEmpty()
    val homeRecommendationLiveData get() = _homeRecommendationLiveData
    private val _homeRecommendationLiveData: MutableLiveData<HomeRecommendationDataModel> =
        MutableLiveData()
    val homeRecommendationNetworkLiveData get() = _homeRecommendationNetworkLiveData
    private val _homeRecommendationNetworkLiveData: MutableLiveData<Result<HomeRecommendationDataModel>> =
        MutableLiveData()

    private val _homeRecommendationCardState =
        MutableStateFlow<HomeRecommendationCardState<HomeRecommendationDataModel>>(
            HomeRecommendationCardState.Loading(HomeRecommendationDataModel(listOf(loadingModel)))
        )
    val homeRecommendationCardState: StateFlow<HomeRecommendationCardState<HomeRecommendationDataModel>>
        get() = _homeRecommendationCardState.asStateFlow()

    var topAdsBannerNextPage = TOPADS_PAGE_DEFAULT

    fun fetchHomeRecommendation(
        tabName: String,
        recommendationId: Int,
        count: Int,
        locationParam: String = "",
        tabIndex: Int = 0,
        sourceType: String
    ) {
        if (HomeRecommendationController.isUsingRecommendationCard()) {
            fetchHomeRecommendationCard(tabName, locationParam, sourceType)
        } else {
            loadInitialPage(tabName, recommendationId, count, locationParam, tabIndex, sourceType)
        }
    }

    fun fetchNextHomeRecommendation(
        tabName: String,
        recommendationId: Int,
        count: Int,
        page: Int,
        locationParam: String = "",
        sourceType: String,
        existingRecommendationData: List<BaseHomeRecommendationVisitable>
    ) {
        if (HomeRecommendationController.isUsingRecommendationCard()) {
            fetchNextHomeRecommendationCard(tabName, page, locationParam, sourceType, existingRecommendationData)
        } else {
            loadNextData(tabName, recommendationId, count, page, locationParam, sourceType)
        }
    }

    private fun fetchHomeRecommendationCard(
        tabName: String,
        locationParam: String,
        sourceType: String
    ) {
        launchCatchError(coroutineContext, block = {
            val result = getHomeRecommendationCardUseCase.get().execute(
                Int.ONE,
                tabName,
                sourceType,
                locationParam
            )
            if (result.homeRecommendations.isEmpty()) {
                _homeRecommendationCardState.emit(
                    HomeRecommendationCardState.EmptyData(
                        HomeRecommendationDataModel(
                            listOf(emptyModel)
                        )
                    )
                )
            } else {
                _homeRecommendationCardState.emit(HomeRecommendationCardState.Success(result))
            }
        }, onError = {
                _homeRecommendationCardState.emit(
                    HomeRecommendationCardState.Fail(
                        HomeRecommendationDataModel(listOf(HomeRecommendationError(it))),
                        throwable = it
                    )
                )
            })
    }

    private fun fetchNextHomeRecommendationCard(
        tabName: String,
        page: Int,
        locationParam: String,
        sourceType: String,
        existingRecommendationData: List<BaseHomeRecommendationVisitable>
    ) {
        val existingRecommendationDataMutableList = existingRecommendationData.toMutableList()

        existingRecommendationDataMutableList.removeAll { it is HomeRecommendationLoadMore || it is HomeRecommendationButtonRetryUiModel }

        existingRecommendationDataMutableList.add(loadMoreModel)

        launchCatchError(coroutineContext, block = {
            _homeRecommendationCardState.emit(
                HomeRecommendationCardState.LoadingMore(
                    HomeRecommendationDataModel(
                        homeRecommendations = existingRecommendationDataMutableList.toList()
                    )
                )
            )

            val result = getHomeRecommendationCardUseCase.get().execute(
                page,
                tabName,
                sourceType,
                locationParam
            )

            existingRecommendationDataMutableList.removeAll { it is HomeRecommendationLoadMore }

            existingRecommendationDataMutableList.addAll(result.homeRecommendations)

            val newHomeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = existingRecommendationDataMutableList.toList(),
                isHasNextPage = result.isHasNextPage
            )

            _homeRecommendationCardState.emit(
                HomeRecommendationCardState.Success(newHomeRecommendationDataModel)
            )
        }, onError = {
                existingRecommendationDataMutableList.removeAll {
                        existingRecommendationData ->
                    existingRecommendationData is HomeRecommendationLoadMore
                }
                existingRecommendationDataMutableList.add(buttonRetryUiModel)

                _homeRecommendationCardState.emit(
                    HomeRecommendationCardState.FailNextPage(
                        HomeRecommendationDataModel(existingRecommendationDataMutableList.toList()),
                        throwable = it
                    )
                )
            })
    }

    private fun loadInitialPage(
        tabName: String,
        recommendationId: Int,
        count: Int,
        locationParam: String = "",
        tabIndex: Int = 0,
        sourceType: String
    ) {
        _homeRecommendationLiveData.postValue(
            HomeRecommendationDataModel(
                homeRecommendations = listOf(
                    loadingModel
                )
            )
        )
        launchCatchError(coroutineContext, block = {
            getHomeRecommendationUseCase.get()
                .setParams(tabName, recommendationId, count, 1, locationParam, sourceType)
            val data = getHomeRecommendationUseCase.get().executeOnBackground()
            if (data.homeRecommendations.isEmpty()) {
                _homeRecommendationLiveData.postValue(
                    data.copy(
                        homeRecommendations = listOf(
                            HomeRecommendationEmpty()
                        )
                    )
                )
            } else {
                try {
                    val headlineAds = fetchHeadlineAds(tabIndex)
                    val homeBannerTopAds =
                        data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsOldDataModel>()
                    val homeBannerTopAdsMutable =
                        data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsOldDataModel>()
                            .toMutableList()
                    val newList = data.homeRecommendations.toMutableList()
                    val topAdsBanner =
                        arrayListOf<Pair<String, ArrayList<TopAdsImageViewModel>>>()
                    homeBannerTopAds.forEach {
                        if (it.bannerType == TYPE_BANNER_ADS) {
                            val bannerData = topAdsImageViewUseCase.get().getImageData(
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
                            if (bannerData.isNotEmpty()) {
                                topAdsBanner.add(Pair(TYPE_BANNER_ADS, bannerData))
                            } else {
                                homeBannerTopAdsMutable.remove(it)
                                newList.remove(it)
                            }
                        } else if (it.bannerType == TYPE_VERTICAL_BANNER_ADS) {
                            val bannerData = topAdsImageViewUseCase.get().getImageData(
                                topAdsImageViewUseCase.get().getQueryMap(
                                    query = "",
                                    source = TOPADS_TDN_RECOM_VERTICAL_SOURCE,
                                    pageToken = "",
                                    adsCount = homeBannerTopAds.size,
                                    dimenId = TOPADS_TDN_RECOM_VERTICAL_DIMEN,
                                    depId = "",
                                    page = topAdsBannerNextPage
                                )
                            )
                            if (bannerData.isNotEmpty()) {
                                topAdsBanner.add(Pair(TYPE_VERTICAL_BANNER_ADS, bannerData))
                            } else {
                                homeBannerTopAdsMutable.remove(it)
                                newList.remove(it)
                            }
                        }
                    }
                    handleTopAdsWidgets(
                        data,
                        topAdsBanner,
                        homeBannerTopAdsMutable,
                        headlineAds,
                        newList
                    )
                } catch (e: Exception) {
                    _homeRecommendationLiveData.postValue(
                        data.copy(
                            homeRecommendations = data.homeRecommendations.filter { it !is HomeRecommendationBannerTopAdsOldDataModel }
                        )
                    )
                }
            }
            _homeRecommendationNetworkLiveData.postValue(Result.success(data))
        }) {
            _homeRecommendationLiveData.postValue(
                HomeRecommendationDataModel(
                    homeRecommendations = listOf(
                        HomeRecommendationError(it)
                    )
                )
            )
            _homeRecommendationNetworkLiveData.postValue(Result.failure(it))
        }
    }

    private fun handleTopAdsWidgets(
        data: HomeRecommendationDataModel,
        topAdsBanner: ArrayList<Pair<String, ArrayList<TopAdsImageViewModel>>>,
        homeBannerTopAds: List<HomeRecommendationBannerTopAdsOldDataModel>,
        headlineAds: TopAdsHeadlineResponse,
        newList: MutableList<BaseHomeRecommendationVisitable>
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
                newList.add(
                    position,
                    HomeRecommendationHeadlineTopAdsDataModel(headlineAds.displayAds)
                )
                topAdsBanner.forEachIndexed { index, pair ->
                    val visitableBanner = homeBannerTopAds[index]
                    if (newList.size > visitableBanner.position + Int.ONE) {
                        newList[visitableBanner.position + Int.ONE] =
                            HomeRecommendationBannerTopAdsOldDataModel(
                                pair.second.firstOrNull(),
                                bannerType = pair.first
                            )
                    }
                }
            } else {
                topAdsBanner.forEachIndexed { index, pair ->
                    val visitableBanner = homeBannerTopAds[index]
                    if (newList.size > visitableBanner.position) {
                        newList[visitableBanner.position] =
                            HomeRecommendationBannerTopAdsOldDataModel(
                                pair.second.firstOrNull(),
                                bannerType = pair.first
                            )
                    }
                }
                position?.let {
                    if (newList.size >= position + topAdsBanner.size) {
                        newList.add(
                            it + Int.ONE,
                            HomeRecommendationHeadlineTopAdsDataModel(headlineAds.displayAds)
                        )
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
            getTopAdsHeadlineUseCase.get()
                .setParams(params, topAdsAddressHelper.get().getAddressData())
            getTopAdsHeadlineUseCase.get().executeOnBackground()
        } else {
            TopAdsHeadlineResponse()
        }
    }

    private fun loadNextData(
        tabName: String,
        recomId: Int,
        count: Int,
        page: Int,
        locationParam: String = "",
        sourceType: String
    ) {
        val list = _homeRecommendationLiveData.value?.homeRecommendations?.toMutableList()
            ?: mutableListOf()
        list.add(loadMoreModel)
        _homeRecommendationLiveData.postValue(
            _homeRecommendationLiveData.value?.copy(
                homeRecommendations = list.toList().copy()
            )
        )
        launchCatchError(coroutineContext, block = {
            getHomeRecommendationUseCase.get()
                .setParams(tabName, recomId, count, page, locationParam, sourceType)
            val data = getHomeRecommendationUseCase.get().executeOnBackground()
            list.remove(loadMoreModel)

            try {
                val homeBannerTopAds =
                    data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsOldDataModel>()
                val homeBannerTopAdsMutable =
                    data.homeRecommendations.filterIsInstance<HomeRecommendationBannerTopAdsOldDataModel>()
                        .toMutableList()
                val newList = data.homeRecommendations.toMutableList()
                val topAdsBanner2 = arrayListOf<Pair<String, ArrayList<TopAdsImageViewModel>>>()
                homeBannerTopAds.forEachIndexed { index, it ->
                    if (it.bannerType == TYPE_BANNER_ADS) {
                        val bannerData = topAdsImageViewUseCase.get().getImageData(
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
                        if (bannerData.isNotEmpty()) {
                            topAdsBanner2.add(Pair(TYPE_BANNER_ADS, bannerData))
                        } else {
                            homeBannerTopAdsMutable.remove(it)
                            newList.remove(it)
                        }
                    } else if (it.bannerType == TYPE_VERTICAL_BANNER_ADS) {
                        val bannerData = topAdsImageViewUseCase.get().getImageData(
                            topAdsImageViewUseCase.get().getQueryMap(
                                query = "",
                                source = TOPADS_TDN_RECOM_VERTICAL_SOURCE,
                                pageToken = "",
                                adsCount = homeBannerTopAds.size,
                                dimenId = TOPADS_TDN_RECOM_VERTICAL_DIMEN,
                                depId = "",
                                page = topAdsBannerNextPage
                            )
                        )
                        if (bannerData.isNotEmpty()) {
                            topAdsBanner2.add(Pair(TYPE_VERTICAL_BANNER_ADS, bannerData))
                        } else {
                            homeBannerTopAdsMutable.remove(it)
                            newList.remove(it)
                        }
                    }
                }

                incrementTopadsPage()
                topAdsBanner2.forEachIndexed { index, pair ->
                    val visitableBanner = homeBannerTopAds[index]
                    if (newList.size > visitableBanner.position) {
                        newList[visitableBanner.position] =
                            HomeRecommendationBannerTopAdsOldDataModel(
                                pair.second.firstOrNull(),
                                bannerType = pair.first
                            )
                    }
                }
                list.addAll(newList)
            } catch (e: Exception) {
                list.addAll(data.homeRecommendations.filter { it !is HomeRecommendationBannerTopAdsOldDataModel })
            }
            _homeRecommendationLiveData.postValue(data.copy(homeRecommendations = list.copy()))
            _homeRecommendationNetworkLiveData.postValue(Result.success(data))
        }) {
            list.remove(loadMoreModel)
            _homeRecommendationLiveData.postValue(
                _homeRecommendationLiveData.value?.copy(
                    homeRecommendations = list.copy()
                )
            )
            _homeRecommendationNetworkLiveData.postValue(Result.failure(it))
        }
    }

    fun updateWhistlist(id: String, position: Int, isWishlisted: Boolean) {
        if (HomeRecommendationController.isUsingRecommendationCard()) {
            updateWishlistNewQuery(id, position, isWishlisted)
        } else {
            updateWishlistOldQuery(id, position, isWishlisted)
        }
    }

    private fun updateWishlistOldQuery(id: String, position: Int, isWishlisted: Boolean) {
        val homeRecomendationList =
            _homeRecommendationLiveData.value?.homeRecommendations?.filterIsInstance<HomeRecommendationItemDataModel>()
                ?.toMutableList()
                ?: mutableListOf()
        var recommendationItem: HomeRecommendationItemDataModel? = null
        var recommendationItemPosition: Int = -1
        if (homeRecomendationList.getOrNull(position)?.recommendationProductItem?.id == id) {
            recommendationItem = homeRecomendationList[position]
            recommendationItemPosition = position
        } else {
            homeRecomendationList.withIndex()
                .find { it.value.recommendationProductItem.id == id }
                ?.let {
                    recommendationItemPosition = it.index
                    recommendationItem = it.value
                }
        }
        if (recommendationItemPosition != -1) {
            recommendationItem?.let {
                homeRecomendationList[recommendationItemPosition] = it.copy(
                    recommendationProductItem = it.recommendationProductItem.copy(isWishlist = isWishlisted)
                )
                _homeRecommendationLiveData.postValue(
                    _homeRecommendationLiveData.value?.copy(
                        homeRecommendations = homeRecomendationList.toList().copy()
                    )
                )
            }
        }
    }

    private fun updateWishlistNewQuery(id: String, position: Int, isWishlisted: Boolean) {
        val homeRecommendationCardStateValue = homeRecommendationCardState.value
        if (homeRecommendationCardStateValue is HomeRecommendationCardState.Success) {
            val homeRecommendationList =
                homeRecommendationCardStateValue.data.homeRecommendations.toMutableList()
            val homeRecommendationItemList =
                homeRecommendationList.filterIsInstance<HomeRecommendationItemDataModel>()

            val recommendationItem =
                homeRecommendationItemList.find { it.recommendationProductItem.id == id }

            recommendationItem?.let {
                launch(coroutineContext) {
                    homeRecommendationList[position] = it.copy(
                        recommendationProductItem = it.recommendationProductItem.copy(isWishlist = isWishlisted)
                    )

                    _homeRecommendationCardState.emit(
                        HomeRecommendationCardState.Success(
                            homeRecommendationCardStateValue.data.copy(
                                homeRecommendations = homeRecommendationList.copy().toList()
                            )
                        )
                    )
                }
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
