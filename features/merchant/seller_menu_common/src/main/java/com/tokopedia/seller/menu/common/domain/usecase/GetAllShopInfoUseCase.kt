package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.seller.menu.common.errorhandler.SellerMenuErrorHandler
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingFail
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingResponse
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class GetAllShopInfoUseCase constructor(
        private val userSession: UserSessionInterface,
        private val balanceInfoUseCase: BalanceInfoUseCase,
        private val getShopBadgeUseCase: GetShopBadgeUseCase,
        private val getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase,
        private val getUserShopInfoUseCase: GetUserShopInfoUseCase,
        private val topAdsAutoTopupUseCase: TopAdsAutoTopupUseCase,
        private val topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase,
        private val dispatchers: CoroutineDispatchers
) : UseCase<Pair<PartialSettingResponse, PartialSettingResponse>>() {

    override suspend fun executeOnBackground(): Pair<PartialSettingResponse, PartialSettingResponse> {
        return withContext(dispatchers.io) {
            val partialShopInfo = async { getPartialShopInfoData(userSession.shopId.toIntOrZero()) }
            val partialTopAdsInfo = async { getPartialTopAdsData(userSession.shopId) }

            return@withContext Pair(partialShopInfo.await(), partialTopAdsInfo.await())
        }
    }

    private suspend fun getPartialShopInfoData(shopId: Int): PartialSettingResponse {
        return try {
            getShopTotalFollowersUseCase.params = GetShopTotalFollowersUseCase.createRequestParams(shopId)
            getShopBadgeUseCase.params = GetShopBadgeUseCase.createRequestParams(shopId)
            getUserShopInfoUseCase.params = GetUserShopInfoUseCase.createRequestParams(shopId)
            PartialSettingSuccessInfoType.PartialShopSettingSuccessInfo(
                    getUserShopInfoUseCase.executeOnBackground(),
                    getShopTotalFollowersUseCase.executeOnBackground(),
                    getShopBadgeUseCase.executeOnBackground()
            )

        } catch (exception: Exception) {
            getPartialFailResponse(exception)
        }
    }

    private suspend fun getPartialTopAdsData(shopId: String): PartialSettingResponse {
        return try {
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

    private fun getPartialFailResponse(exception: Exception): PartialSettingFail {
        SellerMenuErrorHandler.logExceptionToCrashlytics(exception, SellerMenuErrorHandler.ERROR_GET_SETTING_SHOP_INFO)
        return PartialSettingFail
    }
}