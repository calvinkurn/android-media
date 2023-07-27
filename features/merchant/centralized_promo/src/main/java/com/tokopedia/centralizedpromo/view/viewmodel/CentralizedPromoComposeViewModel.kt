package com.tokopedia.centralizedpromo.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.PromoPlayAuthorConfigUseCase
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.PromoCreationMapper
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoUiState
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CentralizedPromoComposeViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val getOnGoingPromotionUseCase: GetOnGoingPromotionUseCase,
    private val getPromotionUseCase: GetPromotionUseCase,
    private val promoPlayAuthorConfigUseCase: PromoPlayAuthorConfigUseCase,
    private val dispatcher: CoroutineDispatchers
) : ViewModel() {

    private val _layoutList = MutableStateFlow(CentralizedPromoUiState(isLoading = true))
    val layoutList = _layoutList.asStateFlow()

    init {
        // Initial load tabId = "", means no filter is selected
        viewModelScope.launch {
            getOnGoingOrPromoCreation(
                LayoutType.ON_GOING_PROMO,
                LayoutType.PROMO_CREATION,
                tabId = ""
            )
        }
    }

    private suspend fun getOnGoingOrPromoCreation(
        vararg layoutTypes: LayoutType,
        tabId: String = ""
    ) {
        viewModelScope.launch(dispatcher.io) {
            val results = mutableListOf<BaseUiModel>()
            layoutTypes.map { type ->
                async {
                    results.add(getResult(type, tabId))
                }
            }.awaitAll()
            _layoutList.update {
                CentralizedPromoUiState(data = results)
            }
        }
    }

    private suspend fun getResult(type: LayoutType, tabId: String) = when (type) {
        LayoutType.ON_GOING_PROMO -> getOnGoingPromotion()
        LayoutType.PROMO_CREATION -> getPromoCreation(tabId)
    }

    private suspend fun getOnGoingPromotion(): BaseUiModel {
        return try {
            getOnGoingPromotionUseCase.params =
                GetOnGoingPromotionUseCase.getRequestParams(false)
            getOnGoingPromotionUseCase.executeOnBackground()
        } catch (e: Throwable) {
            OnGoingPromoListUiModel(
                errorMessage = e.message ?: "",
                title = "",
                items = listOf()
            )
        }
    }

    private suspend fun getPromoCreation(tabId: String): BaseUiModel {
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
            promotionListUiModel
        } catch (e: Throwable) {
            PromoCreationListUiModel(
                errorMessage = e.message ?: "",
                filterItems = listOf(),
                items = listOf()
            )
        }

    }
}