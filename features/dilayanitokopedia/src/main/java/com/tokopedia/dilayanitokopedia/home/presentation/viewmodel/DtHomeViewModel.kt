package com.tokopedia.dilayanitokopedia.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutState
import com.tokopedia.dilayanitokopedia.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.home.constant.HomeStaticLayoutId
import com.tokopedia.dilayanitokopedia.home.domain.mapper.widgets.AnchorTabMapper.mapMenuList
import com.tokopedia.dilayanitokopedia.home.domain.mapper.widgets.HomeLayoutMapper.addLoadingIntoList
import com.tokopedia.dilayanitokopedia.home.domain.mapper.widgets.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.domain.usecase.GetHomeAnchorTabUseCase
import com.tokopedia.dilayanitokopedia.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.HomeRecommendationFeedDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.AnchorTabUiModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutListUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class DtHomeViewModel @Inject constructor(
    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
    private val getHomeAnchorTabUseCase: GetHomeAnchorTabUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val homeLayoutItemList = mutableListOf<HomeLayoutItemUiModel>()

    val homeLayoutList: LiveData<Result<HomeLayoutListUiModel>>
        get() = _homeLayoutList
    private val _homeLayoutList = MutableLiveData<Result<HomeLayoutListUiModel>>()

    val menuList: LiveData<List<AnchorTabUiModel>>
        get() = _menuList
    private val _menuList = MutableLiveData<List<AnchorTabUiModel>>()

    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress

    private val homeRecommendationDataModel = HomeRecommendationFeedDataModel()

    fun getEmptyState(@HomeStaticLayoutId id: String, serviceType: String) {
        // no op yet
    }

    fun getHomeVisitableList(): List<Visitable<*>> {
        return homeLayoutItemList.mapNotNull { it.layout }
    }

    fun getPositionUsingGroupId(groupId: String): HomeLayoutItemUiModel? {
        return homeLayoutItemList.find { groupId == it.groupId }
    }

    fun getAnchorTabByVisitablePosition(indexVisitable: Int): AnchorTabUiModel? {
        val getGroupId = homeLayoutItemList.get(indexVisitable).groupId
        return _menuList.value?.find { getGroupId == it.groupId }
    }

    fun getHomeLayout(localCacheModel: LocalCacheModel) {
        launchCatchError(block = {
            homeLayoutItemList.clear()

            val homeLayoutResponse = getHomeLayoutDataUseCase.execute(localCacheModel = localCacheModel)

            homeLayoutItemList.mapHomeLayoutList(homeLayoutResponse)

            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = DtLayoutState.SHOW
            )

            _homeLayoutList.postValue(Success(data))

            getAnchorTabMenu(homeLayoutResponse)
            getRecommendationForYouNew()
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    /**
     * anchor tab contain info and visitable of click to scroll
     */
    private fun getAnchorTabMenu(homeLayoutResponse: List<HomeLayoutResponse>) {
        launchCatchError(block = {
            val anchorTabResponse = getHomeAnchorTabUseCase.execute()
            val menuList = anchorTabResponse.mapMenuList(homeLayoutResponse, getHomeVisitableList())
            _menuList.postValue(menuList)
        }) {}
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

    fun switchService() {
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
