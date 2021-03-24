package com.tokopedia.gm.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.constant.PMShopGrade
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.utils.PMCommonUtils
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.view_gmc_power_merchant_grade.view.*

/**
 * Created By @ilhamsuaib on 20/03/21
 */

class PowerMerchantGradeView : RelativeLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_gmc_power_merchant_grade, this)
    }

    fun show(data: PowerMerchantInterruptUiModel) {
        when (data.pmStatus) {
            PMStatusConst.ACTIVE, PMStatusConst.IDLE -> {
                if (data.hasReputation) {
                    setupCardForPmWithReputation(data)
                } else {
                    setupCardForPmWithNoReputation(data)
                }
            }
            else -> {
                when {
                    data.isEligiblePm -> setupCardRmWithEligiblePm(data)
                    else -> setupCardRmWithNoEligiblePm(data)
                }
            }
        }
        containerCardPm.setBackgroundResource(R.drawable.bg_gmc_power_merchant_card)
    }

    private fun setupCardRmWithEligiblePm(data: PowerMerchantInterruptUiModel) {
        if (data.hasReputation) {
            imgSahPmGrade.gone()
            icSahPmReputationBadge.visible()
            imgSahPMPotentialGradeArrow.visible()
            icSahPmGradeBadge.visible()
            icSahPmGradeBadge.loadImageWithoutPlaceholder(data.pmGradeBadge)
            icSahPmReputationBadge.loadImageDrawable(getOldPmBadgeByGrade(data.pmGrade))
        } else {
            imgSahPmGrade.visible()
            icSahPmReputationBadge.gone()
            imgSahPMPotentialGradeArrow.gone()
            icSahPmGradeBadge.gone()
            imgSahPmGrade.loadImageWithoutPlaceholder(data.pmGradeBadge)
        }

        var cardTitle = context.getString(R.string.gmc_pm_potential_grade_title, data.potentialPmGrade)
        var cardDescription = context.getString(R.string.gmc_pm_potential_grade_description, data.potentialPmGrade)
        if (!data.hasReputation) {
            cardTitle = context.getString(R.string.gmc_pm_potential_grade_no_reputation_title, data.potentialPmGrade)
            cardDescription = context.getString(R.string.gmc_pm_potential_grade_no_reputation_description, data.potentialPmGrade)
        }
        setCardDescriptionText(cardDescription)
        tvSahPmGradeTitle.text = cardTitle
    }

    private fun setupCardRmWithNoEligiblePm(data: PowerMerchantInterruptUiModel) {

    }

    private fun setupCardForPmWithReputation(data: PowerMerchantInterruptUiModel) {
        tvSahPmGradeTitle.text = context.getString(R.string.gmc_pm_grade_title, data.pmGrade)

        val descriptionText: String = if (data.shopScore >= data.shopScoreThreshold) {
            context.getString(R.string.gmc_pm_grade_description)
        } else {
            val boldTextColor = PMCommonUtils.getHexColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)
            context.getString(R.string.gmc_pm_grade_description_risk_tobe_inactive, boldTextColor, data.shopScoreThreshold)
        }
        setCardDescriptionText(descriptionText)

        imgSahPmGrade.gone()
        icSahPmReputationBadge.visible()
        imgSahPMPotentialGradeArrow.visible()
        icSahPmGradeBadge.visible()
        icSahPmGradeBadge.loadImageWithoutPlaceholder(data.pmGradeBadge)
        icSahPmReputationBadge.loadImageDrawable(getOldPmBadgeByGrade(data.pmGrade))
    }

    private fun setupCardForPmWithNoReputation(data: PowerMerchantInterruptUiModel) {
        tvSahPmGradeTitle.text = context.getString(R.string.gmc_pm_grade_title, data.pmGrade)

        val descriptionText: String = if (data.shopScore >= data.shopScoreThreshold) {
            context.getString(R.string.gmc_pm_grade_no_reputation_description)
        } else {
            val boldTextColor = PMCommonUtils.getHexColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)
            context.getString(R.string.gmc_pm_grade_no_reputation_description_at_risk, boldTextColor, data.shopScoreThreshold)
        }
        setCardDescriptionText(descriptionText)

        imgSahPmGrade.visible()
        icSahPmReputationBadge.gone()
        imgSahPMPotentialGradeArrow.gone()
        icSahPmGradeBadge.gone()
        imgSahPmGrade.loadImageWithoutPlaceholder(data.pmGradeBadge)
    }

    private fun getOldPmBadgeByGrade(pmGrade: String): Int {
        return when (pmGrade) {
            PMShopGrade.DIAMOND -> R.drawable.ic_gmc_old_pm_badge_diamond
            PMShopGrade.GOLD -> R.drawable.ic_gmc_old_pm_badge_gold
            PMShopGrade.SILVER -> R.drawable.ic_gmc_old_pm_badge_silver
            else -> R.drawable.ic_gmc_old_pm_badge_bronze
        }
    }

    private fun setCardDescriptionText(text: String) {
        tvSahPmGradeDescription.text = text.parseAsHtml()
    }
}