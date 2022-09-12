package com.tokopedia.dilayanitokopedia.home.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutState
import com.tokopedia.dilayanitokopedia.home.constant.HomeStaticLayoutId
import com.tokopedia.dilayanitokopedia.home.domain.mapper.HomeLayoutMapper.addEmptyStateIntoList
import com.tokopedia.dilayanitokopedia.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutListUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class DtHomeViewModel @Inject constructor(
//    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
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

//            val homeLayoutResponse = getHomeLayoutDataUseCase.execute(
//                localCacheModel = localCacheModel
//            )
//            channelToken = homeLayoutResponse.first().token

            homeLayoutItemList.mapHomeLayoutList(
//                homeLayoutResponse,
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
        }) {
            _homeLayoutList.postValue(Fail(it))
        }.let {
//            getHomeLayoutJob = it
        }
    }


}