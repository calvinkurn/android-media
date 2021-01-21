package com.tokopedia.centralizedpromo.view.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.domain.usecase.GetChatBlastSellerMetadataUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetPostUseCase
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.PromoCreationStaticData
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sellerhome.R
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

class CentralizedPromoViewModel @Inject constructor(
        @ApplicationContext private val context: Context,
        private val userSession: UserSessionInterface,
        private val getOnGoingPromotionUseCase: GetOnGoingPromotionUseCase,
        private val getPostUseCase: GetPostUseCase,
        private val getChatBlastSellerMetadataUseCase: GetChatBlastSellerMetadataUseCase,
        private val remoteConfig: FirebaseRemoteConfigImpl,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

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
            withContext(dispatcher.io) {
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

    private suspend fun getPromoCreation(): Result<BaseUiModel> = runBlocking {
        try {
            val isFreeShippingEnabled = !remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
            val chatBlastSellerMetadataUiModel = getChatBlastSellerMetadataUseCase.executeOnBackground()
            val broadcastChatExtra = if (chatBlastSellerMetadataUiModel.promo > 0 && chatBlastSellerMetadataUiModel.promoType == 2){
                context.getString(R.string.centralized_promo_broadcast_chat_extra_free_quota, chatBlastSellerMetadataUiModel.promo)
            } else ""
            Success(PromoCreationStaticData.provideStaticData(context, broadcastChatExtra, chatBlastSellerMetadataUiModel.url, isFreeShippingEnabled))
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    fun trackFreeShippingImpression() {
        val isTransitionPeriod = remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        CentralizedPromoTracking.sendImpressionFreeShipping(userSession, isTransitionPeriod)
    }

    fun trackFreeShippingClick() {
        val isTransitionPeriod = remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        CentralizedPromoTracking.sendClickFreeShipping(userSession, isTransitionPeriod)
    }
}