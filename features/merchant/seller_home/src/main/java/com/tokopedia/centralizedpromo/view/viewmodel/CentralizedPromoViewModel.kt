package com.tokopedia.centralizedpromo.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetPostUseCase
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.PromoCreationStaticData
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

class CentralizedPromoViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getOnGoingPromotionUseCase: GetOnGoingPromotionUseCase,
        private val getPostUseCase: GetPostUseCase,
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
        LayoutType.PROMO_CREATION -> getPromoCreation()
        LayoutType.POST -> getPostList()
    }

    private suspend fun getOnGoingPromotion(): Result<BaseUiModel> {
        return try {
            getOnGoingPromotionUseCase.params = GetOnGoingPromotionUseCase.getRequestParams(false)
            Success(getOnGoingPromotionUseCase.executeOnBackground())
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    private suspend fun getPostList(): Result<BaseUiModel> {
        return try {
            getPostUseCase.params = GetPostUseCase.getRequestParams(shopId.toIntOrZero(), emptyList(), startDate, endDate)
            Success(getPostUseCase.executeOnBackground())
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    private fun getPromoCreation(): Result<BaseUiModel> {
        return try {
            Success(PromoCreationStaticData.provideStaticData())
        } catch (t: Throwable) {
            Fail(t)
        }
    }
}