package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantBasicInfoUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 25/03/21
 */

class GetPMStatusAndShopInfoUseCase @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getPMStatusInfo: GetPMStatusUseCase,
        private val getPMShopInfoUseCase: GetPMShopInfoUseCase
) : BaseGqlUseCase<PowerMerchantBasicInfoUiModel>() {

    override suspend fun executeOnBackground(): PowerMerchantBasicInfoUiModel {
        return coroutineScope {
            val pmStatusInfoAsync = async { getPmStatusInfo() }
            val pmShopInfoAsync = async { getPmShopInfo() }
            return@coroutineScope PowerMerchantBasicInfoUiModel(
                    pmStatusInfoAsync.await(),
                    pmShopInfoAsync.await()
            )
        }
    }

    private suspend fun getPmShopInfo(): PMShopInfoUiModel {
        getPMShopInfoUseCase.params = GetPMShopInfoUseCase.createParams(userSession.shopId, PMConstant.PM_SETTING_INFO_SOURCE)
        return getPMShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getPmStatusInfo(): PMStatusUiModel {
        getPMStatusInfo.params = GetPMStatusUseCase.createParams(userSession.shopId)
        return getPMStatusInfo.executeOnBackground()
    }
}