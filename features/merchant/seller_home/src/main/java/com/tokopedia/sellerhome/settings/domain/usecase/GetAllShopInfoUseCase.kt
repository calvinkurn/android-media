package com.tokopedia.sellerhome.settings.domain.usecase

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.common.errorhandler.SellerHomeErrorHandler
import com.tokopedia.sellerhome.settings.view.fragment.OtherMenuFragment
import com.tokopedia.sellerhome.settings.view.uimodel.base.partialresponse.PartialSettingFail
import com.tokopedia.sellerhome.settings.view.uimodel.base.partialresponse.PartialSettingResponse
import com.tokopedia.sellerhome.settings.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllShopInfoUseCase @Inject constructor(
        private val userSession: UserSessionInterface,
        private val balanceInfoUseCase: BalanceInfoUseCase,
        private val getShopBadgeUseCase: GetShopBadgeUseCase,
        private val getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase,
        private val shopStatusTypeUseCase: ShopStatusTypeUseCase,
        private val topAdsAutoTopupUseCase: TopAdsAutoTopupUseCase,
        private val topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase)
    : UseCase<Pair<PartialSettingResponse, PartialSettingResponse>>(){

    override suspend fun executeOnBackground(): Pair<PartialSettingResponse, PartialSettingResponse> = coroutineScope {
        with(userSession) {
            val partialShopInfo = async { getPartialShopInfoData(shopId.toIntOrZero()) }
            val partialTopAdsInfo = async { getPartialTopAdsData(shopId) }
            return@coroutineScope Pair(partialShopInfo.await(), partialTopAdsInfo.await())
        }
    }

    private suspend fun getPartialShopInfoData(shopId: Int): PartialSettingResponse {
        return withContext(Dispatchers.IO) {
            try {
                shopStatusTypeUseCase.params = ShopStatusTypeUseCase.createRequestParams(shopId)
                getShopTotalFollowersUseCase.params = GetShopTotalFollowersUseCase.createRequestParams(shopId)
                getShopBadgeUseCase.params = GetShopBadgeUseCase.createRequestParams(shopId)
                PartialSettingSuccessInfoType.PartialShopSettingSuccessInfo(
                        shopStatusTypeUseCase.executeOnBackground(),
                        getShopTotalFollowersUseCase.executeOnBackground(),
                        getShopBadgeUseCase.executeOnBackground()
                )
            } catch (exception: Exception) {
                getPartialFailResponse(exception)
            }
        }
    }

    private suspend fun getPartialTopAdsData(shopId: String): PartialSettingResponse {
        return withContext(Dispatchers.IO) {
            try {
                topAdsDashboardDepositUseCase.params = TopAdsDashboardDepositUseCase.createRequestParams(shopId.toInt())
                topAdsAutoTopupUseCase.params = TopAdsAutoTopupUseCase.createRequestParams(shopId)
                PartialSettingSuccessInfoType.PartialTopAdsSettingSuccessInfo(
                        balanceInfoUseCase.executeOnBackground(),
                        topAdsDashboardDepositUseCase.executeOnBackground(),
                        topAdsAutoTopupUseCase.executeOnBackground())
            } catch (exception: Exception) {
                getPartialFailResponse(exception)
            }
        }
    }

    private fun getPartialFailResponse(exception: Exception): PartialSettingFail {
        SellerHomeErrorHandler.logExceptionToCrashlytics(exception, OtherMenuFragment.ERROR_GET_SETTING_SHOP_INFO)
        return PartialSettingFail
    }

}