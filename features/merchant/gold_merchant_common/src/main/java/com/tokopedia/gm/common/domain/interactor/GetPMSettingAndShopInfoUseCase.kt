package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMSettingAndShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 25/03/21
 */

class GetPMSettingAndShopInfoUseCase @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getPMSettingInfoUseCase: GetPMSettingInfoUseCase,
        private val getPMShopInfoUseCase: GetPMShopInfoUseCase
) : BaseGqlUseCase<PMSettingAndShopInfoUiModel>() {

    override suspend fun executeOnBackground(): PMSettingAndShopInfoUiModel {
        return coroutineScope {
            val pmSettingInfo = async { getPmSettingInfo() }
            val pmShopInfo = async { getPmShopInfo() }
            return@coroutineScope PMSettingAndShopInfoUiModel(pmSettingInfo.await(), pmShopInfo.await())
        }
    }

    private suspend fun getPmShopInfo(): PMShopInfoUiModel {
        getPMShopInfoUseCase.params = GetPMShopInfoUseCase.createParams(
                userSession.shopId,
                PMConstant.PM_SETTING_INFO_SOURCE
        )
        return getPMShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getPmSettingInfo(): PowerMerchantSettingInfoUiModel {
        getPMSettingInfoUseCase.params = GetPMSettingInfoUseCase.createParams(
                userSession.shopId,
                PMConstant.PM_SETTING_INFO_SOURCE
        )
        return getPMSettingInfoUseCase.executeOnBackground()
    }
}