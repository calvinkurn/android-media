package com.tokopedia.power_merchant.subscribe.view.model

import android.os.Parcelable
import com.tokopedia.gm.common.data.source.local.model.PMBenefitItemUiModel
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.kotlin.extensions.view.ZERO
import kotlinx.parcelize.Parcelize

/**
 * Created by @ilhamsuaib on 24/05/22.
 */

@Parcelize
data class MembershipDataUiModel(
    val shopScore: Int = Int.ZERO,
    val shopScoreThreshold: Int = PMShopInfoUiModel.DEFAULT_PM_PRO_SHOP_SCORE_THRESHOLD,
    val totalOrder: Long = Int.ZERO.toLong(),
    val orderThreshold: Long = PMShopInfoUiModel.DEFAULT_ORDER_THRESHOLD,
    val netIncome: Long = Int.ZERO.toLong(),
    val netIncomeThreshold: Long = PMShopInfoUiModel.DEFAULT_NIV_THRESHOLD,
    val gradeBenefit: PMGradeWithBenefitsUiModel
) : Parcelable