package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.data.source.local.model.PMCurrentAndNextShopGradeUiModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMCurrentAndNextShopGradeUseCase
import com.tokopedia.gm.common.domain.interactor.GetPMStatusUseCase
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.view.model.PMFinalPeriodUiModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 16/03/21
 */

class GetPMFinalPeriodDataUseCase @Inject constructor(
        private val getPMStatusUseCase: GetPMStatusUseCase,
        private val getPMCurrentAndNextShopGradeUseCase: GetPMCurrentAndNextShopGradeUseCase,
        private val userSession: UserSessionInterface
) : UseCase<PMFinalPeriodUiModel>() {

    override suspend fun executeOnBackground(): PMFinalPeriodUiModel {
        return coroutineScope {
            val shopStatusAsync = async { getPMShopStatus() }
            val currentAndNextPMGradeAsync = async { getCurrentAndNextPMGrade() }
            val shopStatus = shopStatusAsync.await()
            val currentAndNextPMGrade = currentAndNextPMGradeAsync.await()
            return@coroutineScope PMFinalPeriodUiModel(
                    pmStatus = shopStatus.status,
                    expiredTime = shopStatus.expiredTime,
                    nextMonthlyRefreshDate = currentAndNextPMGrade.nextMonthlyRefreshDate,
                    nextQuarterlyCalibrationRefreshDate = currentAndNextPMGrade.nextQuarterlyCalibrationRefreshDate,
                    currentPMGrade = currentAndNextPMGrade.currentPMGrade,
                    currentPMBenefits = currentAndNextPMGrade.currentPMBenefits,
                    nextPMGrade = currentAndNextPMGrade.nextPMGrade,
                    nextPMBenefits = currentAndNextPMGrade.nextPMBenefits
            )
        }
    }

    private suspend fun getPMShopStatus(): PMStatusUiModel {
        getPMStatusUseCase.params = GetPMStatusUseCase.createParams(userSession.shopId)
        return getPMStatusUseCase.executeOnBackground()
    }

    private suspend fun getCurrentAndNextPMGrade(): PMCurrentAndNextShopGradeUiModel {
        getPMCurrentAndNextShopGradeUseCase.params = GetPMCurrentAndNextShopGradeUseCase.createParams(
                shopId = userSession.shopId,
                source = Constant.PM_SETTING_INFO_SOURCE
        )
        return getPMCurrentAndNextShopGradeUseCase.executeOnBackground()
    }
}