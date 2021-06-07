package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMCurrentAndNextShopGradeUiModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMCurrentAndNextShopGradeUseCase
import com.tokopedia.gm.common.domain.interactor.GetPMStatusUseCase
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.view.model.PMActiveDataUiModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 16/03/21
 */

class GetPMActiveDataUseCase @Inject constructor(
        private val getPMCurrentAndNextShopGradeUseCase: GetPMCurrentAndNextShopGradeUseCase,
        private val userSession: UserSessionInterface
) : UseCase<PMActiveDataUiModel>() {

    override suspend fun executeOnBackground(): PMActiveDataUiModel {
        return coroutineScope {
            val currentAndNextPMGradeAsync = async { getCurrentAndNextPMGrade() }
            val currentAndNextPMGrade = currentAndNextPMGradeAsync.await()
            return@coroutineScope PMActiveDataUiModel(
                    nextMonthlyRefreshDate = currentAndNextPMGrade.nextMonthlyRefreshDate,
                    nextQuarterlyCalibrationRefreshDate = currentAndNextPMGrade.nextQuarterlyCalibrationRefreshDate,
                    currentPMGrade = currentAndNextPMGrade.currentPMGrade,
                    currentPMBenefits = currentAndNextPMGrade.currentPMBenefits,
                    nextPMGrade = currentAndNextPMGrade.nextPMGrade,
                    nextPMBenefits = currentAndNextPMGrade.nextPMBenefits
            )
        }
    }

    private suspend fun getCurrentAndNextPMGrade(): PMCurrentAndNextShopGradeUiModel {
        getPMCurrentAndNextShopGradeUseCase.params = GetPMCurrentAndNextShopGradeUseCase.createParams(
                shopId = userSession.shopId,
                source = PMConstant.PM_SETTING_INFO_SOURCE
        )
        return getPMCurrentAndNextShopGradeUseCase.executeOnBackground()
    }
}