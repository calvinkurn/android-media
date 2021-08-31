package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.common.constant.PMShopGrade
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil.setTextMakeHyperlink
import com.tokopedia.power_merchant.subscribe.view.model.WidgetExpandableUiModel
import kotlinx.android.synthetic.main.item_benefit_package_status_pm_pro.view.*
import kotlinx.android.synthetic.main.view_pm_pro_current_benefit_section.view.*

/**
 * Created By @ilhamsuaib on 07/05/21
 */

class PmProCurrentBenefitSectionView : ConstraintLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var updateInfoCtaClickListener: (() -> Unit)? = null

    init {
        View.inflate(context, R.layout.item_benefit_package_status_pm_pro, this)
    }

    fun show(data: WidgetExpandableUiModel) {
        showPmGrade(data.grade?.gradeName.orEmpty())
        setupUpdateInfo(data)
    }

    fun setOnUpdateInfoCtaClickedListener(callback: () -> Unit) {
        this.updateInfoCtaClickListener = callback
    }

    private fun setupUpdateInfo(data: WidgetExpandableUiModel) {
        tvNextUpdatePmProStatus.setTextMakeHyperlink(
            context.getString(
                R.string.pm_next_update_benefit_package_status, data.nextMonthlyRefreshDate,
                data.nextShopLevel, data.grade
            )
        ) {
            updateInfoCtaClickListener?.invoke()
        }
        iconPmProDowngradeStatus?.showWithCondition(data.isDowngradePeriod())

        tvPmUpdateDate.text = context.getString(
            R.string.pm_label_next_three_months_pm_grade_update,
            data.nextMonthlyRefreshDate
        )
    }

    private fun showPmGrade(grade: String) {
        labelPmProStatus?.text = grade.asCamelCase()
        when {
            PMShopGrade.EXPERT.equals(grade, true) -> {
                labelPmProStatus?.setBackgroundColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_TN400))
                bgBenefitPackageStatus?.setImageResource(R.drawable.bg_pm_benefit_package_expert)
            }
            PMShopGrade.ULTIMATE.equals(grade, true) -> {
                labelPmProStatus?.setBackgroundColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_YN400))
                bgBenefitPackageStatus?.setImageResource(R.drawable.bg_pm_benefit_package_ultimate)
            }
            else -> { //PMShopGrade.ADVANCED
                labelPmProStatus?.setBackgroundColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN500))
                bgBenefitPackageStatus?.setImageResource(R.drawable.bg_pm_benefit_package_advanced)
            }
        }
    }
}