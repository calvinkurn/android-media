package com.tokopedia.dilayanitokopedia.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutState
import com.tokopedia.dilayanitokopedia.common.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.domain.mapper.widgets.AnchorTabMapper.mapMenuList
import com.tokopedia.dilayanitokopedia.domain.mapper.widgets.HomeLayoutMapper.addLoadingIntoList
import com.tokopedia.dilayanitokopedia.domain.mapper.widgets.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.dilayanitokopedia.domain.usecase.GetAnchorTabUseCase
import com.tokopedia.dilayanitokopedia.domain.usecase.GetLayoutDataUseCase
import com.tokopedia.dilayanitokopedia.ui.home.adapter.uimodel.HomeRecommendationFeedDataModel
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.AnchorTabUiModel
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.HomeLayoutListUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class DtHomeViewModel @Inject constructor(
    private val getLayoutDataUseCase: GetLayoutDataUseCase,
    private val getHomeAnchorTabUseCase: GetAnchorTabUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val homeLayoutItemList = mutableListOf<HomeLayoutItemUiModel>()

    val homeLayoutList: LiveData<Result<HomeLayoutListUiModel>>
        get() = _homeLayoutList
    private val _homeLayoutList = MutableLiveData<Result<HomeLayoutListUiModel>>()

    var menuList: List<AnchorTabUiModel>? = null

    private val _anchorTabState = MutableLiveData<Result<List<AnchorTabUiModel>>>()
    val anchorTabState: LiveData<Result<List<AnchorTabUiModel>>>
        get() = _anchorTabState

    private val homeRecommendationDataModel = HomeRecommendationFeedDataModel()

    val isOnLoading: Boolean
        get() = homeLayoutList.value.let {
            if (it is Success) {
                it.data.state == DtLayoutState.LOADING
            } else {
                false
            }
        }

    fun getHomeVisitableList(): List<Visitable<*>> {
        return homeLayoutItemList.mapNotNull { it.layout }
    }

    fun getPositionUsingGroupId(groupId: String): HomeLayoutItemUiModel? {
        return homeLayoutItemList.find { groupId == it.groupId }
    }

    fun getAnchorTabByVisitablePosition(indexVisitable: Int): AnchorTabUiModel? {
        val getGroupId = homeLayoutItemList.getOrNull(indexVisitable)?.groupId
        return menuList?.find { getGroupId == it.groupId }
    }

    fun getHomeLayout(localCacheModel: LocalCacheModel) {
        launchCatchError(block = {
            homeLayoutItemList.clear()

            val homeLayoutResponse = getLayoutDataUseCase(GetLayoutDataUseCase.getParam(location = localCacheModel))

            homeLayoutItemList.mapHomeLayoutList(homeLayoutResponse.response.data)

            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = DtLayoutState.SHOW
            )

            _homeLayoutList.postValue(Success(data))
            getRecommendationForYouNew()
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    /**
     * anchor tab contain info and visitable of click to scroll
     */
    fun getAnchorTabMenu(localCacheModel: LocalCacheModel) {
        launchCatchError(
            block = {
                val anchorTabResponse = getHomeAnchorTabUseCase(GetAnchorTabUseCase.getParam(localCacheModel))
                anchorTabResponse.response.mapMenuList().apply {
                    menuList = this
                    _anchorTabState.postValue(Success(this))
                }
            }
        ) {
            _anchorTabState.postValue(Fail(it))
        }
    }

    private fun getRecommendationForYouNew() {
        val newVisitable = getHomeVisitableList().toMutableList()
        newVisitable.add(homeRecommendationDataModel)

        val data = HomeLayoutListUiModel(items = newVisitable, state = DtLayoutState.SHOW)

        homeLayoutItemList.add(
            HomeLayoutItemUiModel(
                HomeRecommendationFeedDataModel(),
                state = HomeLayoutItemState.LOADING,
                null
            )
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun refreshLayout() {
        getLoadingState()
    }

    fun loadLayout() {
        getLoadingState()
    }

    private fun getLoadingState() {
        homeLayoutItemList.clear()
        homeLayoutItemList.addLoadingIntoList()
        val data = HomeLayoutListUiModel(
            items = getHomeVisitableList(),
            state = DtLayoutState.LOADING
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun isLastWidgetIsRecommendationForYou(): Boolean? {
        if (getHomeVisitableList().isEmpty()) return null
        return getHomeVisitableList().last() == homeRecommendationDataModel
    }
}
