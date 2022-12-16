package com.tokopedia.dilayanitokopedia.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutState
import com.tokopedia.dilayanitokopedia.home.constant.AnchorTabData
import com.tokopedia.dilayanitokopedia.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.home.constant.HomeStaticLayoutId
import com.tokopedia.dilayanitokopedia.home.domain.mapper.AnchorTabMapper.mapMenuList
import com.tokopedia.dilayanitokopedia.home.domain.mapper.HomeLayoutMapper.addLoadingIntoList
import com.tokopedia.dilayanitokopedia.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.dilayanitokopedia.home.domain.usecase.DtGetHomeLayoutDataUseCase
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.HomeRecommendationFeedDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.AnchorTabUiModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutListUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class DtHomeViewModel @Inject constructor(
    private val getHomeLayoutDataUseCase: DtGetHomeLayoutDataUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
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

    fun getEmptyState(@HomeStaticLayoutId id: String, serviceType: String) {
        launchCatchError(block = {
            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = DtLayoutState.HIDE
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getHomeVisitableList(): List<Visitable<*>> {
        return homeLayoutItemList.mapNotNull { it.layout }
    }

    fun getHomeLayout(localCacheModel: LocalCacheModel) {
        launchCatchError(block = {
            homeLayoutItemList.clear()

            val homeLayoutResponse = getHomeLayoutDataUseCase.execute(
                localCacheModel = localCacheModel
            )

            homeLayoutItemList.mapHomeLayoutList(homeLayoutResponse)

            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = DtLayoutState.SHOW
            )

            _homeLayoutList.postValue(Success(data))
            _menuList.postValue(dataMenuList().mapMenuList(homeLayoutResponse, getHomeVisitableList()))

            getRecommendationForYouNew()
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    private fun getRecommendationForYouNew() {
        val newVisitable = getHomeVisitableList().toMutableList()
        newVisitable.add(HomeRecommendationFeedDataModel())

        val data = HomeLayoutListUiModel(items = newVisitable, state = DtLayoutState.SHOW)

        homeLayoutItemList.add(HomeLayoutItemUiModel(HomeRecommendationFeedDataModel(), state = HomeLayoutItemState.LOADING))
        _homeLayoutList.postValue(Success(data))
    }

    /**
     * still dummy, need to update
     */
    private fun dataMenuList(): MutableList<AnchorTabUiModel> {
        return AnchorTabData.getListAnchorTab()
    }

    fun switchServiceOrLoadLayout() {
        getLoadingState()
    }

    fun getLoadingState() {
        homeLayoutItemList.clear()
        homeLayoutItemList.addLoadingIntoList()
        val data = HomeLayoutListUiModel(
            items = getHomeVisitableList(),
            state = DtLayoutState.LOADING
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun getChooseAddress(source: String) {
        getChooseAddressWarehouseLocUseCase.getStateChosenAddress({
            _chooseAddress.postValue(Success(it))
        }, {
            _chooseAddress.postValue(Fail(it))
        }, source)
    }

    fun getPositionWidget(listComponent: MutableList<AnchorTabUiModel>?, pinnedComponentId: AnchorTabUiModel): Int {
        listComponent?.forEachIndexed { index, componentsItem ->
            if (componentsItem == pinnedComponentId) {
                return index
            }
        }
        return -1
    }
}
