package com.tokopedia.centralizedpromo.view.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.CENTRALIZED_PROMO_PREF
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.PromoPlayAuthorConfigUseCase
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.LayoutType.ON_GOING_PROMO
import com.tokopedia.centralizedpromo.view.LayoutType.PROMO_CREATION
import com.tokopedia.centralizedpromo.view.LoadingType
import com.tokopedia.centralizedpromo.view.PromoCreationMapper
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent.FilterUpdate
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent.SwipeRefresh
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent.UpdateRbacBottomSheet
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoUiState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class CentralizedPromoComposeViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val getOnGoingPromotionUseCase: GetOnGoingPromotionUseCase,
    private val getPromotionUseCase: GetPromotionUseCase,
    private val promoPlayAuthorConfigUseCase: PromoPlayAuthorConfigUseCase,
    @Named(CENTRALIZED_PROMO_PREF) private val pref: SharedPreferences,
    private val dispatcher: CoroutineDispatchers
) : ViewModel() {

    private val _layoutList = MutableStateFlow(CentralizedPromoUiState(isLoading = LoadingType.ALL))
    val layoutList = _layoutList.asStateFlow()

    private val _isSwipeRefresh = MutableStateFlow(false)
    val isSwipeRefresh = _isSwipeRefresh.asStateFlow()

    init {
        // Initial load tabId = "", means no filter is selected
        viewModelScope.launch {
            getOnGoingOrPromoCreation(
                ON_GOING_PROMO,
                PROMO_CREATION,
                tabId = ""
            )
        }
    }

    fun sendEvent(event: CentralizedPromoEvent) {
        when (event) {
            is FilterUpdate -> {
                updateFilter(event.selectedTabFilterData)
            }
            is SwipeRefresh -> {
                swipeToRefresh()
            }
            is UpdateRbacBottomSheet -> {
                setKeyRBAC(event.key)
            }
        }
    }

    fun getKeyRBAC(key: String): Boolean {
        return pref.getBoolean(key, false)
    }

    private fun setKeyRBAC(key: String) {
        pref.edit().putBoolean(key, true).apply()
    }

    private fun updateFilter(selectedTabFilterData: Pair<String, String>) {
        _layoutList.update {
            it.copy(selectedTabFilterData = selectedTabFilterData)
        }
        getOnGoingOrPromoCreation(
            PROMO_CREATION,
            tabId = selectedTabFilterData.first
        )
    }

    private fun swipeToRefresh() {
        _isSwipeRefresh.update {
            true
        }

        getOnGoingOrPromoCreation(
            ON_GOING_PROMO,
            PROMO_CREATION,
            tabId = _layoutList.value.selectedTabId()
        )
    }

    private fun getOnGoingOrPromoCreation(
        vararg layoutTypes: LayoutType,
        tabId: String = ""
    ) {
        viewModelScope.launch(dispatcher.io) {
            val results = layoutTypes.map { type ->
                async { type to getResult(type, tabId) }
            }.awaitAll()

            _layoutList.update { currentState ->
                val updatedState = results.fold(currentState) { state, (type, result) ->
                    when (type) {
                        ON_GOING_PROMO -> state.copy(onGoingData = result)
                        PROMO_CREATION -> state.copy(promoCreationData = result)
                    }
                }
                updatedState.copy(isLoading = LoadingType.NONE)
            }
            _isSwipeRefresh.update { false }
        }
    }

    private suspend fun getResult(type: LayoutType, tabId: String) = when (type) {
        ON_GOING_PROMO -> getOnGoingPromotion()
        PROMO_CREATION -> getPromoCreation(tabId)
    }

    private suspend fun getOnGoingPromotion(): Result<BaseUiModel> {
        return try {
            getOnGoingPromotionUseCase.params =
                GetOnGoingPromotionUseCase.getRequestParams(false)
            Success(getOnGoingPromotionUseCase.executeOnBackground())
        } catch (e: Throwable) {
            Fail(e)
        }
    }

    private suspend fun getPromoCreation(tabId: String): Result<BaseUiModel> {
        return try {
            val responseDeferred =
                viewModelScope.async {
                    getPromotionUseCase.execute(userSession.shopId, tabId)
                }
            val hasPlayContentDeferred =
                viewModelScope.async { promoPlayAuthorConfigUseCase.execute(userSession.shopId) }

            val promotionListUiModel = PromoCreationMapper.mapperToPromoCreationUiModel(
                responseDeferred.await(),
                hasPlayContentDeferred.await()
            )
            Success(promotionListUiModel)
        } catch (e: Throwable) {
            Fail(e)
        }

    }
}