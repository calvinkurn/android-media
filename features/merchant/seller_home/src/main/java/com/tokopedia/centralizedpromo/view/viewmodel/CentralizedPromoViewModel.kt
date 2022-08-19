package com.tokopedia.centralizedpromo.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetPromotionUseCase
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.PromoCreationMapper
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

class CentralizedPromoViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val getOnGoingPromotionUseCase: GetOnGoingPromotionUseCase,
    private val getPromotionUseCase: GetPromotionUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    val getLayoutResultLiveData: MutableLiveData<MutableMap<LayoutType, Result<BaseUiModel>>> =
        MutableLiveData()

    fun getLayoutData(vararg layoutTypes: LayoutType, tabId: String) {
        launch(coroutineContext) {
            withContext(dispatcher.io) {
                val results = mutableMapOf<LayoutType, Result<BaseUiModel>>()
                layoutTypes.map { type ->
                    async { results[type] = getResult(type, tabId) }
                }.awaitAll()

                getLayoutResultLiveData.postValue(results)
            }
        }
    }

    private suspend fun getResult(type: LayoutType, tabId: String) = when (type) {
        LayoutType.ON_GOING_PROMO -> getOnGoingPromotion()
        LayoutType.PROMO_CREATION -> getPromoCreation(tabId)
    }

    private suspend fun getOnGoingPromotion(): Result<BaseUiModel> {
        return try {
            getOnGoingPromotionUseCase.params = GetOnGoingPromotionUseCase.getRequestParams(false)
            Success(getOnGoingPromotionUseCase.executeOnBackground())
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    private suspend fun getPromoCreation(tabId: String): Result<BaseUiModel> {
        return try {
            val response = getPromotionUseCase.execute(userSession.shopId, tabId)
            val promotionListUiModel = PromoCreationMapper.mapperToPromoCreationUiModel(response)
            Success(promotionListUiModel)
        } catch (t: Throwable) {
            Fail(t)
        }
    }
}