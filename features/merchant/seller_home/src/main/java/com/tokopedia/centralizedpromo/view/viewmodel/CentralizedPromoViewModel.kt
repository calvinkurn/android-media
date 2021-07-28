package com.tokopedia.centralizedpromo.view.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.domain.usecase.GetChatBlastSellerMetadataUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.PromoCreationStaticData
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sellerhome.R
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
        private val getChatBlastSellerMetadataUseCase: GetChatBlastSellerMetadataUseCase,
        private val remoteConfig: FirebaseRemoteConfigImpl,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    val getLayoutResultLiveData: MutableLiveData<MutableMap<LayoutType, Result<BaseUiModel>>> = MutableLiveData()

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
    }

    private suspend fun getOnGoingPromotion(): Result<BaseUiModel> {
        return try {
            getOnGoingPromotionUseCase.params = GetOnGoingPromotionUseCase.getRequestParams(false)
            Success(getOnGoingPromotionUseCase.executeOnBackground())
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