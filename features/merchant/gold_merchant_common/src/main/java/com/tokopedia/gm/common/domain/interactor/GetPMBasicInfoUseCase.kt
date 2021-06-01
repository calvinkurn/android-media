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
        private val userSession: UserSessionInterface
) : BaseGqlUseCase<PowerMerchantBasicInfoUiModel>() {

    override suspend fun executeOnBackground(): PowerMerchantBasicInfoUiModel {
        return coroutineScope {
            val pmStatusInfoAsync = async { getPmStatusInfo() }
            val pmShopInfoAsync = async { getPmShopInfo() }
            val pmSettingInfoAsync = async { getPmSettingInfo() }
            return@coroutineScope PowerMerchantBasicInfoUiModel(
                    pmStatus = pmStatusInfoAsync.await(),
                    shopInfo = pmShopInfoAsync.await(),
                    tickers = pmSettingInfoAsync.await()
            )
        }
    }

    private suspend fun getPmSettingInfo(): List<TickerUiModel> {
        return try {
            getPmSettingInfoUseCase.params = GetPMSettingInfoUseCase.createParams(userSession.shopId, PMConstant.PM_SETTING_INFO_SOURCE)
            val settingInfo = getPmSettingInfoUseCase.executeOnBackground()
            settingInfo.tickers
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun getPmShopInfo(): PMShopInfoUiModel {
        getPMShopInfoUseCase.params = GetPMShopInfoUseCase.createParams(userSession.shopId, PMConstant.PM_SETTING_INFO_SOURCE)
        return getPMShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getPmStatusInfo(): PMStatusUiModel {
        getPmStatusInfo.params = GetPMStatusUseCase.createParams(userSession.shopId)
        return getPmStatusInfo.executeOnBackground()
    }
}