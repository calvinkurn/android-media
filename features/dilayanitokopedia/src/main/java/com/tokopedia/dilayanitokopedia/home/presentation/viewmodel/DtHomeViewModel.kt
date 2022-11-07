package com.tokopedia.dilayanitokopedia.home.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutState
import com.tokopedia.dilayanitokopedia.common.util.PageInfo
import com.tokopedia.dilayanitokopedia.home.constant.HomeStaticLayoutId
import com.tokopedia.dilayanitokopedia.home.domain.mapper.AnchorTabMapper.mapMenuList
import com.tokopedia.dilayanitokopedia.home.domain.mapper.HomeLayoutMapper.addEmptyStateIntoList
import com.tokopedia.dilayanitokopedia.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.dilayanitokopedia.home.domain.usecase.DtGetHomeLayoutDataUseCase
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
//    private val getCategoryListUseCase: GetCategoryListUseCase,
//    private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
//    private val getTickerUseCase: GetTickerUseCase,
//    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
//    private val addToCartUseCase: AddToCartUseCase,
//    private val updateCartUseCase: UpdateCartUseCase,
//    private val deleteCartUseCase: DeleteCartUseCase,
//    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
//    private val getRepurchaseWidgetUseCase: GetRepurchaseWidgetUseCase,
//    private val getQuestWidgetListUseCase: GetQuestWidgetListUseCase,
//    private val setUserPreferenceUseCase: SetUserPreferenceUseCase,
//    private val getHomeReferralUseCase: GetHomeReferralUseCase,
//    private val playWidgetTools: PlayWidgetTools,
//    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
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
            homeLayoutItemList.clear()
            homeLayoutItemList.addEmptyStateIntoList(id, serviceType)
            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = DtLayoutState.HIDE
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    private fun getHomeVisitableList(): List<Visitable<*>> {
        return homeLayoutItemList.mapNotNull { it.layout }
    }

    fun getHomeLayout(
        localCacheModel: LocalCacheModel,
//                      removeAbleWidgets: List<HomeRemoveAbleWidget>
    ) {
//        getHomeLayoutJob?.cancel()
        launchCatchError(block = {
            homeLayoutItemList.clear()

            val homeLayoutResponse = getHomeLayoutDataUseCase.execute(
                localCacheModel = localCacheModel
            )
//            channelToken = homeLayoutResponse.first().token

            homeLayoutItemList.mapHomeLayoutList(
                homeLayoutResponse,
//                hasTickerBeenRemoved,
//                removeAbleWidgets,
//                miniCartSimplifiedData,
                localCacheModel,
//                userSession.isLoggedIn
            )

            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = DtLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))


            _menuList.postValue(dataMenuList().mapMenuList(homeLayoutResponse))
        }) {
            _homeLayoutList.postValue(Fail(it))
        }.let {
//            getHomeLayoutJob = it
        }
    }


    /**
     * still dummy, need to change
     */
    private fun dataMenuList(): MutableList<AnchorTabUiModel> {
        val anchorTabList = mutableListOf<AnchorTabUiModel>()
        anchorTabList.add(AnchorTabUiModel(0, "title 1", "", "360"))
        anchorTabList.add(AnchorTabUiModel(0, "title 2", "", "360"))
        anchorTabList.add(AnchorTabUiModel(0, "title 3", "", "360"))
        anchorTabList.add(AnchorTabUiModel(0, "title 4", "", "363"))
        anchorTabList.add(AnchorTabUiModel(0, "title 5", "", ""))

        return anchorTabList
    }

    /***
     *
     * @param localCacheModel local data from choose address
     * @param serviceType which is the intended type of service
     */
    fun switchService(localCacheModel: LocalCacheModel, serviceType: String) {
//        launchCatchError(block = {
//            val userPreference = setUserPreferenceUseCase.execute(localCacheModel, serviceType)
//            _setUserPreference.postValue(Success(userPreference))
//        }) {
//            _setUserPreference.postValue(Fail(it))
//        }
    }

    fun getLoadingState() {
//        channelToken = ""
        homeLayoutItemList.clear()
//        homeLayoutItemList.addLoadingIntoList()
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

    fun getShareUTM(data: PageInfo): String {
        var campaignCode = if (data.campaignCode.isNullOrEmpty()) "0" else data.campaignCode
        if (data.campaignCode != null && data.campaignCode.length > 11) {
            campaignCode = data.campaignCode.substring(0, 11)
        }
        return "${data.identifier}-${campaignCode}"
    }
}
