package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMGradeWithBenefitUseCase
import com.tokopedia.gm.common.domain.interactor.GetPMShopInfoUseCase
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.view.model.PMGradeBenefitAndShopInfoUiModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 11/03/21
 */

class GetPMGradeBenefitAndShopInfoUseCase @Inject constructor(
        private val getPMShopInfoUseCase: GetPMShopInfoUseCase,
        private val getPMGradeWithBenefitUseCase: GetPMGradeWithBenefitUseCase,
        private val userSession: UserSessionInterface
) : UseCase<PMGradeBenefitAndShopInfoUiModel>() {

    override suspend fun executeOnBackground(): PMGradeBenefitAndShopInfoUiModel {
        return coroutineScope {
            val pmGradeBenefitList = async { getPMGradeBenefitList() }
            val shopInfo = async { getShopInfo() }
            return@coroutineScope PMGradeBenefitAndShopInfoUiModel(
                    shopInfo = shopInfo.await(),
                    gradeBenefitList = pmGradeBenefitList.await()
            )
        }
    }

    private suspend fun getShopInfo(): PMShopInfoUiModel {
        getPMShopInfoUseCase.params = GetPMShopInfoUseCase.createParams(
                shopId = userSession.shopId,
                source = Constant.PM_SETTING_INFO_SOURCE
        )
        return getPMShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getPMGradeBenefitList(): List<PMGradeWithBenefitsUiModel> {
        getPMGradeWithBenefitUseCase.params = GetPMGradeWithBenefitUseCase.createParams(
                shopId = userSession.shopId,
                source = Constant.PM_SETTING_INFO_SOURCE
        )
        return getPMGradeWithBenefitUseCase.executeOnBackground()
    }
}