package com.tokopedia.centralizedpromo.view.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.common.util.CentralizedPromoResourceProvider
import com.tokopedia.centralizedpromo.domain.usecase.CheckNonTopAdsUserUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetChatBlastSellerMetadataUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.SellerHomeGetWhiteListedUserUseCase
import com.tokopedia.centralizedpromo.domain.usecase.VoucherCashbackEligibleUseCase
import com.tokopedia.centralizedpromo.view.FirstVoucherDataSource
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.PromoCreationStaticData
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

class CentralizedPromoViewModel @Inject constructor(
    private val resourceProvider: CentralizedPromoResourceProvider,
    private val userSession: UserSessionInterface,
    private val getOnGoingPromotionUseCase: GetOnGoingPromotionUseCase,
    private val getChatBlastSellerMetadataUseCase: GetChatBlastSellerMetadataUseCase,
    private val voucherCashbackEligibleUseCase: VoucherCashbackEligibleUseCase,
    private val checkNonTopAdsUserUseCase: CheckNonTopAdsUserUseCase,
    private val sellerHomeGetWhiteListedUserUseCase: SellerHomeGetWhiteListedUserUseCase,
    private val remoteConfig: FirebaseRemoteConfigImpl,
    private val sharedPreferences: SharedPreferences,
    private val abTestPlatform: AbTestPlatform,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val UNAVAILABLE_PROMO_TYPE = 0
        private const val BROADCAST_CHAT_PROMO_TYPE = 2

        private const val MVC_PRODUCT_ROLLENCE_KEY = "MVProductEntryPoint"
        private const val MVC_PRODUCT_VARIANT_ON = "MVProductEntryPoint"
    }

    val getLayoutResultLiveData: MutableLiveData<MutableMap<LayoutType, Result<BaseUiModel>>> =
        MutableLiveData()

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

    private suspend fun getPromoCreation(): Result<BaseUiModel> {
        return try {
            val isFreeShippingEnabledDeferred = async {
                !remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
            }
            val broadcastChatPairDeferred = async {
                val chatBlastSellerMetadataUiModel = getChatBlastSellerMetadataUseCase.executeOnBackground()
                val broadcastChatExtra =
                    if (chatBlastSellerMetadataUiModel.promo > UNAVAILABLE_PROMO_TYPE &&
                        chatBlastSellerMetadataUiModel.promoType == BROADCAST_CHAT_PROMO_TYPE) {
                        resourceProvider.composeBroadcastChatFreeQuotaLabel(
                            chatBlastSellerMetadataUiModel.promo
                        )
                    } else ""
                broadcastChatExtra to chatBlastSellerMetadataUiModel.url
            }
            val isVoucherCashbackEligibleDeferred = async {
                voucherCashbackEligibleUseCase.execute(userSession.shopId)
            }
            val isVoucherCashbackFirstTimeDeferred = async {
                sharedPreferences.getBoolean(FirstVoucherDataSource.IS_MVC_FIRST_TIME, true)
            }
            val isProductCouponFirstTimeDeferred = async {
                sharedPreferences.getBoolean(
                    FirstVoucherDataSource.IS_PRODUCT_COUPON_FIRST_TIME,
                    true
                )
            }
            val isProductCouponEnabledDeffered = async {
                getIsProductCouponEnabled()
            }

            val isNonTopAdsUserDeferred = async {
                checkNonTopAdsUserUseCase.execute(userSession.shopId)
            }

            val isNonTopAdsUser = isNonTopAdsUserDeferred.await()
            var isTopAdsOnBoardingEnable = false
            if (isNonTopAdsUser) {
                val isUserWhiteListedDeferred = async {
                    sellerHomeGetWhiteListedUserUseCase.executeQuery()
                }
                isTopAdsOnBoardingEnable = isUserWhiteListedDeferred.await()
            }

            val (broadcastChatExtra, chatBlastSellerUrl) = broadcastChatPairDeferred.await()
            val isFreeShippingEnabled = isFreeShippingEnabledDeferred.await()
            val isVoucherCashbackEligible = isVoucherCashbackEligibleDeferred.await()
            val isVoucherCashbackFirstTime = isVoucherCashbackFirstTimeDeferred.await()
            val isProductCouponFirstTime = isProductCouponFirstTimeDeferred.await()
            val isProductCouponEnabled = isProductCouponEnabledDeffered.await()
            Success(
                PromoCreationStaticData.provideStaticData(
                    resourceProvider,
                    broadcastChatExtra,
                    chatBlastSellerUrl,
                    isFreeShippingEnabled,
                    isVoucherCashbackEligible,
                    isTopAdsOnBoardingEnable,
                    isVoucherCashbackFirstTime,
                    isProductCouponFirstTime,
                    isProductCouponEnabled
                )
            )
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    fun trackFreeShippingImpression() {
        val isTransitionPeriod =
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        CentralizedPromoTracking.sendImpressionFreeShipping(userSession, isTransitionPeriod)
    }

    fun trackFreeShippingClick() {
        val isTransitionPeriod =
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        CentralizedPromoTracking.sendClickFreeShipping(userSession, isTransitionPeriod)
    }

    private fun getIsProductCouponEnabled(): Boolean {
        return try {
            abTestPlatform.getString(MVC_PRODUCT_ROLLENCE_KEY, "") == MVC_PRODUCT_VARIANT_ON
        } catch (ex: Exception) {
            false
        }
    }

}