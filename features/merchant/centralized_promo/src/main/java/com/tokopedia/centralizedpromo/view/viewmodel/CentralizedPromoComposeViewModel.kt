package com.tokopedia.centralizedpromo.view.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.CENTRALIZED_PROMO_COACHMARK_KEY
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.CENTRALIZED_PROMO_PREF
import com.tokopedia.centralizedpromo.common.errorhandler.CentralizedPromoErrorHandler
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.PromoPlayAuthorConfigUseCase
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.LayoutType.ON_GOING_PROMO
import com.tokopedia.centralizedpromo.view.LayoutType.PROMO_CREATION
import com.tokopedia.centralizedpromo.view.PromoCreationMapper
import com.tokopedia.centralizedpromo.view.fragment.CentralizedPromoFragment
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent.CoachMarkShown
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent.FilterUpdate
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent.LoadOnGoingPromo
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent.LoadPromoCreation
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent.UpdateRbacBottomSheet
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoResult
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoResult.Fail
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoResult.Loading
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoUiState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _layoutList = MutableStateFlow(CentralizedPromoUiState())
    val layoutList = _layoutList.asStateFlow()

    private val _toasterState = MutableSharedFlow<Boolean>()
    val toasterState = _toasterState.asSharedFlow()

    init {
        // Initial load tabId = "", means no filter is selected
        getOnGoingOrPromoCreation(
            PROMO_CREATION,
            ON_GOING_PROMO,
            tabId = ""
        )
    }

    fun sendEvent(event: CentralizedPromoEvent) {
        when (event) {
            is UpdateRbacBottomSheet -> {
                setKeyRBAC(event.key)
            }
            is FilterUpdate -> {
                updateFilter(event.selectedTabFilterData)
            }
            is CoachMarkShown -> {
                setKeyRBAC(event.key + CENTRALIZED_PROMO_COACHMARK_KEY)
            }
            is LoadPromoCreation -> {
                loadPromoCreation()
            }
            is LoadOnGoingPromo -> {
                loadOnGoingPromo()
            }
            else -> {
                swipeToRefresh()
            }
        }
    }

    //Ensure this only called once in the compose, either remember it once or put in lambda
    fun getKeyRBAC(key: String): Boolean {
        return pref.getBoolean(key, false)
    }

    //Ensure this only called once in the compose, either remember it once or put in lambda
    fun getCoachmarkSharedPref(pageId: String): Boolean {
        return pref.getBoolean(pageId + CENTRALIZED_PROMO_COACHMARK_KEY, false)
    }

    private fun setKeyRBAC(key: String) {
        pref.edit().putBoolean(key, true).apply()
    }

    private fun updateFilter(selectedTabFilterData: Pair<String, String>) {
        if (selectedTabFilterData.first == _layoutList.value.selectedTabId()) {
            return
        }

        _layoutList.update {
            it.copy(
                selectedTabFilterData = selectedTabFilterData,
                promoCreationData = Loading
            )
        }
        getOnGoingOrPromoCreation(
            PROMO_CREATION,
            tabId = selectedTabFilterData.first
        )
    }

    private fun loadOnGoingPromo() {
        _layoutList.update {
            if (it.onGoingData as? Fail == null) return@update it
            it.copy(
                onGoingData = (it.onGoingData).copy(isLoading = true)
            )
        }

        getOnGoingOrPromoCreation(
            ON_GOING_PROMO,
            tabId = _layoutList.value.selectedTabId()
        )
    }

    private fun loadPromoCreation() {
        _layoutList.update {
            if (it.promoCreationData as? Fail == null) return@update it
            it.copy(
                promoCreationData = (it.promoCreationData).copy(isLoading = true)
            )
        }

        getOnGoingOrPromoCreation(
            PROMO_CREATION,
            tabId = _layoutList.value.selectedTabId()
        )
    }

    private fun swipeToRefresh() {
        _layoutList.update {
            it.copy(
                isSwipeRefresh = true,
                promoCreationData = Loading,
                onGoingData = Loading
            )
        }

        getOnGoingOrPromoCreation(
            ON_GOING_PROMO,
            PROMO_CREATION,
            tabId = _layoutList.value.selectedTabId()
        )
    }

    private fun getOnGoingOrPromoCreation(
        vararg layoutTypes: LayoutType,
        tabId: String
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

                if (updatedState.onGoingData is Fail
                    || updatedState.promoCreationData is Fail
                ) {
                    showToasterState()
                }

                updatedState.copy(isSwipeRefresh = false)
            }
        }
    }

    private fun showToasterState() {
        viewModelScope.launch {
            _toasterState.emit(true)
        }
    }

    private suspend fun getResult(type: LayoutType, tabId: String) = when (type) {
        ON_GOING_PROMO -> getOnGoingPromotion()
        PROMO_CREATION -> getPromoCreation(tabId)
    }

    private suspend fun getOnGoingPromotion(): CentralizedPromoResult<BaseUiModel> {
        return try {
            getOnGoingPromotionUseCase.params =
                GetOnGoingPromotionUseCase.getRequestParams(false)
            val result = getOnGoingPromotionUseCase.executeOnBackground()
            if (result.items.isEmpty()) {
                CentralizedPromoResult.Empty
            } else {
                CentralizedPromoResult.Success(getOnGoingPromotionUseCase.executeOnBackground())
            }
        } catch (e: Throwable) {
            CentralizedPromoErrorHandler.logException(
                e,
                String.format(CentralizedPromoFragment.ERROR_GET_LAYOUT_DATA, ON_GOING_PROMO)
            )
            Fail(e, e.message.toString(), false)
        }
    }

    private suspend fun getPromoCreation(tabId: String): CentralizedPromoResult<BaseUiModel> {
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
            if (promotionListUiModel.items.isEmpty()) {
                CentralizedPromoResult.Empty
            } else {
                CentralizedPromoResult.Success(promotionListUiModel)
            }
        } catch (e: Throwable) {
            CentralizedPromoErrorHandler.logException(
                e,
                String.format(CentralizedPromoFragment.ERROR_GET_LAYOUT_DATA, PROMO_CREATION)
            )
            Fail(e, e.message.toString(), false)
        }

    }
}