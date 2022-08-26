package com.tokopedia.centralizedpromoold.view.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.PromoCreationStaticDataOld
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.centralizedpromoold.analytic.CentralizedPromoTrackingOld
import com.tokopedia.centralizedpromoold.common.util.CentralizedPromoResourceProviderOld
import com.tokopedia.centralizedpromoold.domain.usecase.CheckNonTopAdsUserUseCase
import com.tokopedia.centralizedpromoold.domain.usecase.GetChatBlastSellerMetadataUseCase
import com.tokopedia.centralizedpromoold.domain.usecase.GetOnGoingPromotionUseCaseOld
import com.tokopedia.centralizedpromoold.domain.usecase.SellerHomeGetWhiteListedUserUseCase
import com.tokopedia.centralizedpromoold.domain.usecase.SlashPriceEligibleUseCase
import com.tokopedia.centralizedpromoold.domain.usecase.VoucherCashbackEligibleUseCase
import com.tokopedia.centralizedpromoold.view.FirstPromoDataSource
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CentralizedPromoViewModelOld @Inject constructor(
    private val resourceProvider: CentralizedPromoResourceProviderOld,
    private val userSession: UserSessionInterface,
    private val getOnGoingPromotionUseCaseOld: GetOnGoingPromotionUseCaseOld,
    private val getChatBlastSellerMetadataUseCase: GetChatBlastSellerMetadataUseCase,
    private val voucherCashbackEligibleUseCase: VoucherCashbackEligibleUseCase,
    private val slashPriceEligibleUseCase: SlashPriceEligibleUseCase,
    private val checkNonTopAdsUserUseCase: CheckNonTopAdsUserUseCase,
    private val sellerHomeGetWhiteListedUserUseCase: SellerHomeGetWhiteListedUserUseCase,
    private val remoteConfig: FirebaseRemoteConfigImpl,
    private val sharedPreferences: SharedPreferences,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val UNAVAILABLE_PROMO_TYPE = 0
        private const val BROADCAST_CHAT_PROMO_TYPE = 2
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
            getOnGoingPromotionUseCaseOld.params = GetOnGoingPromotionUseCaseOld.getRequestParams(false)
            Success(getOnGoingPromotionUseCaseOld.executeOnBackground())
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    private suspend fun getPromoCreation(): Result<BaseUiModel> {
        return try {
            val isEnableFlashSaleDeferred = async {
                remoteConfig.getBoolean(
                    RemoteConfigKey.ENABLE_FLASH_SALE_ENTRY_SELLER, true)
            }
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
                sharedPreferences.getBoolean(FirstPromoDataSource.IS_MVC_FIRST_TIME, true)
            }
            val isProductCouponFirstTimeDeferred = async {
                sharedPreferences.getBoolean(
                    FirstPromoDataSource.IS_PRODUCT_COUPON_FIRST_TIME,
                    true
                )
            }
            val isTokopediaPlayFirstTimeDeferred = async {
                sharedPreferences.getBoolean(
                    FirstPromoDataSource.IS_TOKOPEDIA_PLAY_FIRST_TIME,
                    true
                )
            }
            val isProductCouponEnabledDeffered = async {
                getIsProductCouponEnabled()
            }
            val isSlashPriceEnabledDeffered = async {
                getIsSlashPriceEnabled()
            }
            val isSlashPriceEligibleDeffered = async {
                slashPriceEligibleUseCase.execute(userSession.shopId)
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
            val isTokopediaPlayFirstTime = isTokopediaPlayFirstTimeDeferred.await()
            val isProductCouponEnabled = isProductCouponEnabledDeffered.await()
            val isSlashPriceEnabled = isSlashPriceEnabledDeffered.await()
            val isSlashPriceEligible = isSlashPriceEligibleDeffered.await()
            val isEnableFlashSale = isEnableFlashSaleDeferred.await()

            Success(
                PromoCreationStaticDataOld.provideStaticData(
                    resourceProvider,
                    broadcastChatExtra,
                    chatBlastSellerUrl,
                    isFreeShippingEnabled,
                    isVoucherCashbackEligible,
                    isTopAdsOnBoardingEnable,
                    isVoucherCashbackFirstTime,
                    isProductCouponFirstTime,
                    isTokopediaPlayFirstTime,
                    isProductCouponEnabled,
                    isSlashPriceEnabled,
                    isSlashPriceEligible,
                    isEnableFlashSale
                )
            )
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    fun trackFreeShippingImpression() {
        val isTransitionPeriod =
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        CentralizedPromoTrackingOld.sendImpressionFreeShipping(userSession, isTransitionPeriod)
    }

    fun trackFreeShippingClick() {
        val isTransitionPeriod =
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        CentralizedPromoTrackingOld.sendClickFreeShipping(userSession, isTransitionPeriod)
    }

    private fun getIsProductCouponEnabled(): Boolean {
        return try {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_MVC_PRODUCT, true)
        } catch (ex: Exception) {
            false
        }
    }

    private fun getIsSlashPriceEnabled(): Boolean {
        return try {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } catch (ex: Exception) {
            false
        }
    }

}