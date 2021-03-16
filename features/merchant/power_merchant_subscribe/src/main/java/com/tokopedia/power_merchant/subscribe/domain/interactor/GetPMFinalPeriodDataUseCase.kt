package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.data.source.local.model.CurrentPMGradeAndBenefitUiModel
import com.tokopedia.gm.common.data.source.local.model.NextPMGradeAndBenefitUiModel
import com.tokopedia.gm.common.data.source.local.model.PMShopStatusUiModel
import com.tokopedia.gm.common.domain.interactor.GetCurrentPMGradeWithBenefitUseCase
import com.tokopedia.gm.common.domain.interactor.GetNextPMGradeWithBenefitUseCase
import com.tokopedia.gm.common.domain.interactor.GetPMShopStatusUseCase
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.view.model.PMFinalPeriodUiModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Created By @ilhamsuaib on 16/03/21
 */

class GetPMFinalPeriodDataUseCase(
        private val getPMShopStatusUseCase: GetPMShopStatusUseCase,
        private val getCurrentPMGradeWithBenefitUseCase: GetCurrentPMGradeWithBenefitUseCase,
        private val getNextPMGradeWithBenefitUseCase: GetNextPMGradeWithBenefitUseCase,
        private val userSession: UserSessionInterface
) : UseCase<PMFinalPeriodUiModel>() {

    override suspend fun executeOnBackground(): PMFinalPeriodUiModel {
        return coroutineScope {
            val shopStatusAsync = async { getPMShopStatus() }
            val currentPMGradeAsync = async { getCurrentPmGrade() }
            val nextPMGradeAsync = async { getNextPmGrade() }
            val shopStatus = shopStatusAsync.await()
            val currentPMGrade = currentPMGradeAsync.await()
            val nextPMGrade = nextPMGradeAsync.await()
            return@coroutineScope PMFinalPeriodUiModel(
                    pmStatus = shopStatus.status,
                    expiredTime = shopStatus.expiredTime,
                    nextMonthlyRefreshDate = currentPMGrade.nextMonthlyRefreshDate,
                    nextQuarterlyCalibrationRefreshDate = currentPMGrade.nextQuarterlyCalibrationRefreshDate,
                    currentPMGrade = currentPMGrade.currentPMGrade,
                    currentPMBenefits = currentPMGrade.currentPMBenefits,
                    nextPMGrade = nextPMGrade.nextPMGrade,
                    nextPMBenefits = nextPMGrade.nextPMBenefits
            )
        }
    }

    private suspend fun getPMShopStatus(): PMShopStatusUiModel {
        getPMShopStatusUseCase.params = GetPMShopStatusUseCase.createParams(userSession.shopId)
        return getPMShopStatusUseCase.executeOnBackground()
    }

    private suspend fun getNextPmGrade(): NextPMGradeAndBenefitUiModel {
        getNextPMGradeWithBenefitUseCase.params = GetCurrentPMGradeWithBenefitUseCase.createParams(
                shopId = userSession.shopId,
                source = Constant.PM_SETTING_INFO_SOURCE
        )
        return getNextPMGradeWithBenefitUseCase.executeOnBackground()
    }

    private suspend fun getCurrentPmGrade(): CurrentPMGradeAndBenefitUiModel {
        getCurrentPMGradeWithBenefitUseCase.params = GetNextPMGradeWithBenefitUseCase.createParams(
                shopId = userSession.shopId,
                source = Constant.PM_SETTING_INFO_SOURCE
        )
        return getCurrentPMGradeWithBenefitUseCase.executeOnBackground()
    }
}