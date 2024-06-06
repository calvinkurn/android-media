package com.tokopedia.home.beranda.presentation.viewModel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.domain.interactor.usecase.GetGlobalHomeRecommendationCardUseCase
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeGlobalRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationCardState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.recommendation_widget_common.byteio.RefreshType
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.EmptyStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ErrorStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.LoadMoreStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.RetryButtonStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ShimmeringStateModel
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeGlobalRecommendationViewModel @Inject constructor(
    private val getHomeRecommendationCardUseCase: Lazy<GetGlobalHomeRecommendationCardUseCase>,
    homeDispatcher: Lazy<CoroutineDispatchers>
) : BaseViewModel(homeDispatcher.get().io) {

    companion object {
        private const val TOPADS_PAGE_DEFAULT = "1"
    }

    private val loadingModel = ShimmeringStateModel()
    private val loadMoreModel = LoadMoreStateModel()
    val buttonRetryUiModel = RetryButtonStateModel()
    val emptyModel = EmptyStateModel()

    private val _homeRecommendationCardState =
        MutableStateFlow<HomeRecommendationCardState<HomeGlobalRecommendationDataModel>>(
            HomeRecommendationCardState.Loading(HomeGlobalRecommendationDataModel(listOf(loadingModel)))
        )
    val homeRecommendationCardState: StateFlow<HomeRecommendationCardState<HomeGlobalRecommendationDataModel>>
        get() = _homeRecommendationCardState.asStateFlow()

    var topAdsBannerNextPage = TOPADS_PAGE_DEFAULT

    private var recSessionId: String = ""

    fun fetchHomeRecommendation(
        tabName: String,
        locationParam: String = "",
        tabIndex: Int = 0,
        sourceType: String,
        refreshType: RefreshType,
    ) {
        recSessionId = ""
        fetchHomeRecommendationCard(tabIndex, tabName, locationParam, sourceType, refreshType)
    }

    fun fetchNextHomeRecommendation(
        tabIndex: Int,
        tabName: String,
        page: Int,
        locationParam: String = "",
        sourceType: String,
        existingRecommendationData: List<ForYouRecommendationVisitable>
    ) {
        fetchNextHomeRecommendationCard(tabIndex, tabName, page, locationParam, sourceType, existingRecommendationData)
    }

    private fun fetchHomeRecommendationCard(
        tabIndex: Int,
        tabName: String,
        locationParam: String,
        sourceType: String,
        refreshType: RefreshType
    ) {
        launchCatchError(coroutineContext, block = {
            val result = getHomeRecommendationCardUseCase.get().execute(
                Int.ONE,
                tabIndex,
                tabName,
                sourceType,
                locationParam,
                refreshType = refreshType,
                bytedanceSessionId = recSessionId
            )

            recSessionId = result.appLog.sessionId

            if (result.homeRecommendations.isEmpty()) {
                _homeRecommendationCardState.emit(
                    HomeRecommendationCardState.EmptyData(
                        HomeGlobalRecommendationDataModel(
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
                    HomeGlobalRecommendationDataModel(listOf(ErrorStateModel(it))),
                    throwable = it
                )
            )
        })
    }

    private fun fetchNextHomeRecommendationCard(
        tabIndex: Int,
        tabName: String,
        page: Int,
        locationParam: String,
        sourceType: String,
        existingRecommendationData: List<ForYouRecommendationVisitable>
    ) {
        val existingRecommendationDataMutableList = existingRecommendationData.toMutableList()

        existingRecommendationDataMutableList.removeAll { it is LoadMoreStateModel || it is RetryButtonStateModel }

        existingRecommendationDataMutableList.add(loadMoreModel)

        launchCatchError(coroutineContext, block = {
            _homeRecommendationCardState.emit(
                HomeRecommendationCardState.LoadingMore(
                    HomeGlobalRecommendationDataModel(
                        homeRecommendations = existingRecommendationDataMutableList.toList()
                    )
                )
            )

            val result = getHomeRecommendationCardUseCase.get().execute(
                page,
                tabIndex,
                tabName,
                sourceType,
                locationParam,
                refreshType = RefreshType.LOAD_MORE,
                bytedanceSessionId = recSessionId,
                currentTotalData = existingRecommendationData.size
            )

            recSessionId = result.appLog.sessionId

            existingRecommendationDataMutableList.removeAll { it is LoadMoreStateModel }

            existingRecommendationDataMutableList.addAll(result.homeRecommendations)

            val newHomeRecommendationDataModel = HomeGlobalRecommendationDataModel(
                homeRecommendations = existingRecommendationDataMutableList.toList(),
                isHasNextPage = result.isHasNextPage
            )

            _homeRecommendationCardState.emit(
                HomeRecommendationCardState.Success(newHomeRecommendationDataModel)
            )
        }, onError = {
            existingRecommendationDataMutableList.removeAll {
                    existingRecommendationData ->
                existingRecommendationData is LoadMoreStateModel
            }
            existingRecommendationDataMutableList.add(buttonRetryUiModel)

            _homeRecommendationCardState.emit(
                HomeRecommendationCardState.FailNextPage(
                    HomeGlobalRecommendationDataModel(existingRecommendationDataMutableList.toList()),
                    throwable = it
                )
            )
        })
    }

    fun updateWhistlist(id: String, position: Int, isWishlisted: Boolean) {
        val homeRecommendationCardStateValue = homeRecommendationCardState.value
        if (homeRecommendationCardStateValue is HomeRecommendationCardState.Success) {
            val homeRecommendationList =
                homeRecommendationCardStateValue.data.homeRecommendations.toMutableList()
            val homeRecommendationItemList =
                homeRecommendationList.filterIsInstance<RecommendationCardModel>()

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
}
