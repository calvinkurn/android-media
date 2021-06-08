package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.*
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 25/03/21
 */

class GetPMBasicInfoUseCase @Inject constructor(
        private val getPmStatusInfo: GetPMStatusUseCase,
        private val getPmSettingInfoUseCase: GetPMSettingInfoUseCase,
        private val getPMShopInfoUseCase: GetPMShopInfoUseCase,
        private val getShopScoreUseCase: GetShopScoreLevelUseCase,
        private val userSession: UserSessionInterface
) {

    suspend fun executeOnBackground(isFirstLoad: Boolean): PowerMerchantBasicInfoUiModel {
        return coroutineScope {
            val pmStatusInfoAsync = async { getPmStatusInfo(isFirstLoad) }
            val pmShopInfoAsync = async { getPmShopInfo() }
            val pmSettingInfoAsync = async { getPmSettingInfo(isFirstLoad) }
            val shopScoreAsync = async { getShopScore() }

            val pmSettingInfo = pmSettingInfoAsync.await()
            val pmShopInfo = pmShopInfoAsync.await()
            val shopScoreResult = shopScoreAsync.await()
            return@coroutineScope PowerMerchantBasicInfoUiModel(
                    pmStatus = pmStatusInfoAsync.await(),
                    shopInfo = pmShopInfo.copy(shopScore = shopScoreResult.shopScore.toInt()),
                    tickers = pmSettingInfo.tickers,
                    periodTypePmPro = pmSettingInfo.periodeTypePmPro
            )
        }
    }

    private suspend fun getPmSettingInfo(isFirstLoad: Boolean): PowerMerchantSettingInfoUiModel {
        return try {
            getPmSettingInfoUseCase.setCacheStrategy(GetPMSettingInfoUseCase.getCacheStrategy(isFirstLoad))
            getPmSettingInfoUseCase.params = GetPMSettingInfoUseCase.createParams(userSession.shopId, PMConstant.PM_SETTING_INFO_SOURCE)
            getPmSettingInfoUseCase.executeOnBackground()
        } catch (e: Exception) {
            PowerMerchantSettingInfoUiModel()
        }
    }

    private suspend fun getPmShopInfo(): PMShopInfoUiModel {
        getPMShopInfoUseCase.params = GetPMShopInfoUseCase.createParams(userSession.shopId, PMConstant.PM_SETTING_INFO_SOURCE)
        return getPMShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getPmStatusInfo(shouldFromCache: Boolean): PMStatusUiModel {
        getPmStatusInfo.setCacheStrategy(GetPMStatusUseCase.getCacheStrategy(shouldFromCache))
        getPmStatusInfo.params = GetPMStatusUseCase.createParams(userSession.shopId)
        return getPmStatusInfo.executeOnBackground()
    }

    private suspend fun getShopScore(): ShopScoreResultUiModel {
        getShopScoreUseCase.params = GetShopScoreLevelUseCase.getRequestParams(userSession.shopId, PMConstant.PM_SETTING_INFO_SOURCE)
        return getShopScoreUseCase.executeOnBackground()
    }
}