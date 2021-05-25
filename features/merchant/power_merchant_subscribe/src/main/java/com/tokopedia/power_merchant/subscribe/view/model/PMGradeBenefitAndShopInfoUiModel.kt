package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel

/**
 * Created By @ilhamsuaib on 11/03/21
 */

data class PMGradeBenefitAndShopInfoUiModel(
        val shopInfo: PMShopInfoUiModel,
        val gradeBenefitList: List<PMGradeWithBenefitsUiModel>
)