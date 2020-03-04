package com.tokopedia.centralized_promo.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.centralized_promo.domain.usecase.GetCentralizedPromoPostUseCase
import com.tokopedia.centralized_promo.domain.usecase.GetOnGoingPromoUseCase
import com.tokopedia.centralized_promo.view.LayoutType
import com.tokopedia.centralized_promo.view.OnGoingPromoStaticData
import com.tokopedia.centralized_promo.view.RecommendedPromotionStaticData
import com.tokopedia.centralized_promo.view.model.BaseUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.common.utils.DateTimeUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

class CentralizedPromoViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getOnGoingPromoUseCase: GetOnGoingPromoUseCase,
        private val getCentralizedPromoPostUseCase: GetCentralizedPromoPostUseCase,
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy"
    }

    val getLayoutResultLiveData: MutableLiveData<MutableMap<LayoutType, Result<BaseUiModel>>> = MutableLiveData()

    private val shopId: String by lazy { userSession.shopId }

    private val startDate: String by lazy {
        val timeInMillis = DateTimeUtil.getNPastDaysTimestamp(daysBefore = 7)
        return@lazy DateTimeUtil.format(timeInMillis, DATE_FORMAT)
    }

    private val endDate: String by lazy {
        val timeInMillis = DateTimeUtil.getNPastDaysTimestamp(daysBefore = 1)
        return@lazy DateTimeUtil.format(timeInMillis, DATE_FORMAT)
    }

    fun getLayoutData(vararg layoutTypes: LayoutType) {
        launch(coroutineContext) {
            withContext(Dispatchers.IO) {
                val results = mutableMapOf<LayoutType, Result<BaseUiModel>>()
                layoutTypes.map { type ->
                    async { results[type] = getResult(type) }
                }.awaitAll()

                getLayoutResultLiveData.postValue(results)
            }
        }
    }

    private suspend fun getResult(type: LayoutType) = when (type) {
        LayoutType.ON_GOING_PROMO -> getOnGoingPromotion()
        LayoutType.RECOMMENDED_PROMO -> getRecommendedPromotion()
        LayoutType.POST -> getPostList()
    }

    private suspend fun getOnGoingPromotion(): Result<BaseUiModel> {
        return try {
//            getOnGoingPromoUseCase.params = GetOnGoingPromoUseCase.getRequestParams(false)
//            Success(getOnGoingPromoUseCase.executeOnBackground())
            Success(OnGoingPromoStaticData.provideStaticData())
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    private suspend fun getPostList(): Result<BaseUiModel> {
        return try {
            getCentralizedPromoPostUseCase.params = GetCentralizedPromoPostUseCase.getRequestParams(if (shopId.isBlank()) 0 else shopId.toIntOrZero(), listOf("article"), startDate, endDate)
            Success(getCentralizedPromoPostUseCase.executeOnBackground())
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    private fun getRecommendedPromotion(): Result<BaseUiModel> {
        return try {
            Success(RecommendedPromotionStaticData.provideStaticData())
        } catch (t: Throwable) {
            Fail(t)
        }
    }
}