package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.gm.common.constant.PMShopGrade
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.power_merchant.subscribe.view.model.WidgetExpandableUiModel
import kotlinx.android.synthetic.main.view_pm_pro_current_benefit_section.view.*

/**
 * Created By @ilhamsuaib on 07/05/21
 */

class PmProCurrentBenefitSectionView : ConstraintLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var updateInfoCtaClickListener: (() -> Unit)? = null

    init {
        View.inflate(context, R.layout.view_pm_pro_current_benefit_section, this)
    }

    fun show(data: WidgetExpandableUiModel) {
        showPmGrade(data.grade?.gradeName.orEmpty())
        setupUpdateInfo(data)
    }

    fun setOnExpandedChanged(shouldExpanded: Boolean) {
        if (shouldExpanded) {
            icPmProExpandable.setImage(IconUnify.CHEVRON_DOWN)
        } else {
            icPmProExpandable.setImage(IconUnify.CHEVRON_UP)
        }
    }

    fun setOnUpdateInfoCtaClickedListener(callback: () -> Unit) {
        this.updateInfoCtaClickListener = callback
    }

    private fun setupUpdateInfo(data: WidgetExpandableUiModel) {
        val isUpdateInfoVisible = data.pmStatus == PMStatusConst.ACTIVE && data.isAutoExtendEnabled
        viewPmNextUpdateInfo.isVisible = isUpdateInfoVisible

        if (!isUpdateInfoVisible) return

        tvPmUpdateDate.text = context.getString(R.string.pm_label_next_three_months_pm_grade_update, data.nextMonthlyRefreshDate)
        val ctaTextColor = com.tokopedia.unifyprinciples.R.color.Unify_G500
        val updateInfoDescription = if (data.isDowngradePeriod()) {
            context.getString(R.string.pm_update_info_pm_pro_transition_period_description_text)
        } else {
            context.getString(R.string.pm_update_info_pm_pro_description_text)
        }
        val clickableText = context.getString(R.string.pm_update_info_description_clickable_text)
        val termDescription = PowerMerchantSpannableUtil.createSpannableString(
                text = updateInfoDescription.parseAsHtml(),
                highlightText = clickableText,
                colorId = context.getResColor(ctaTextColor),
                isBold = true
        ) {
            updateInfoCtaClickListener?.invoke()
        }
        tvPmUpdateDescription.movementMethod = LinkMovementMethod.getInstance()
        tvPmUpdateDescription.text = termDescription
    }

    private fun showPmGrade(grade: String) {
        tvPmCurrentGrade.text = grade.asCamelCase()
        when {
            PMShopGrade.EXPERT.equals(grade, true) -> {
                tvPmCurrentGrade.setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_T500))
                imgPmGradeBg.loadImageDrawable(R.drawable.bg_pm_benefit_package_expert)
            }
            PMShopGrade.ULTIMATE.equals(grade, true) -> {
                tvPmCurrentGrade.setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_Y400))
                imgPmGradeBg.loadImageDrawable(R.drawable.bg_pm_benefit_package_ultimate)
            }
            else -> { //PMShopGrade.ADVANCED
                tvPmCurrentGrade.setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                imgPmGradeBg.loadImageDrawable(R.drawable.bg_pm_benefit_package_advanced)
            }
        }
    }
}