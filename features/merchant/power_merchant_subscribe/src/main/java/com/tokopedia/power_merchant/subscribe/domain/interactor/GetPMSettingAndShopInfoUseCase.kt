package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.gm.common.domain.interactor.BaseGqlUseCase
import com.tokopedia.gm.common.domain.interactor.GetPMSettingInfoUseCase
import com.tokopedia.gm.common.domain.interactor.GetPMShopInfoUseCase
import com.tokopedia.power_merchant.subscribe.view_old.model.PMSettingAndShopInfoUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 27/03/21
 */

class GetPMSettingAndShopInfoUseCase @Inject constructor(
        private val getPMSettingInfoUseCase: GetPMSettingInfoUseCase,
        private val getPMShopInfoUseCase: GetPMShopInfoUseCase,
        private val userSession: UserSessionInterface
) : BaseGqlUseCase<PMSettingAndShopInfoUiModel>() {

    override suspend fun executeOnBackground(): PMSettingAndShopInfoUiModel {
        return coroutineScope {
            val shopInfo = async { getShopInfo() }
            val settingInfo = async { getPmSettingInfo() }
            return@coroutineScope PMSettingAndShopInfoUiModel(shopInfo.await(), settingInfo.await())
        }
    }

    private suspend fun getShopInfo(): PMShopInfoUiModel {
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