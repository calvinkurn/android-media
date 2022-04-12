package com.tokopedia.seller.menu.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.seller.menu.common.domain.usecase.BalanceInfoUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetShopBadgeUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetShopTotalFollowersUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetUserShopInfoUseCase
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
    private val dispatchers: CoroutineDispatchers
) : UseCase<Pair<PartialSettingResponse, PartialSettingResponse>>() {

    override suspend fun executeOnBackground(): Pair<PartialSettingResponse, PartialSettingResponse> {
        return withContext(dispatchers.io) {
            val partialShopInfo = async { getPartialShopInfoData(userSession.shopId.toLongOrZero()) }
            val partialTopAdsInfo = async { getPartialTopAdsData() }

            return@withContext Pair(partialShopInfo.await(), partialTopAdsInfo.await())
        }
    }

    private suspend fun getPartialShopInfoData(shopId: Long): PartialSettingResponse {
        return try {
            getShopTotalFollowersUseCase.params =
                GetShopTotalFollowersUseCase.createRequestParams(shopId)
            getShopBadgeUseCase.params = GetShopBadgeUseCase.createRequestParams(shopId)
            PartialSettingSuccessInfoType.PartialShopSettingSuccessInfo(
                    getUserShopInfoUseCase.executeOnBackground(),
                    getShopTotalFollowersUseCase.executeOnBackground(),
                    getShopBadgeUseCase.executeOnBackground()
            )

        } catch (exception: Exception) {
            getPartialFailResponse(exception)
        }
    }

    private suspend fun getPartialTopAdsData(): PartialSettingResponse {
        return try {
            PartialSettingSuccessInfoType.PartialTopAdsSettingSuccessInfo(
                    balanceInfoUseCase.executeOnBackground())
        } catch (exception: Exception) {
            getPartialFailResponse(exception)
        }
    }

    private fun getPartialFailResponse(exception: Exception): PartialSettingFail {
        SellerMenuErrorHandler.logExceptionToCrashlytics(exception, SellerMenuErrorHandler.ERROR_GET_SETTING_SHOP_INFO)
        return PartialSettingFail
    }
}