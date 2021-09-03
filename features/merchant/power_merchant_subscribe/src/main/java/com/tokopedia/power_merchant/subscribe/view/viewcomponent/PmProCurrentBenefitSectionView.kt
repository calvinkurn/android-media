package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gm.common.constant.PMShopGrade
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil.setTextMakeHyperlink
import com.tokopedia.power_merchant.subscribe.view.model.WidgetExpandableUiModel
import kotlinx.android.synthetic.main.item_benefit_package_status_pm_pro.view.*

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
        setBenefitPackageClicked()
    }

    private fun setBenefitPackageClicked() {
        cardPmProBenefitPackage?.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalMarketplace.PM_BENEFIT_PACKAGE)
        }
    }

    fun setOnUpdateInfoCtaClickedListener(callback: () -> Unit) {
        this.updateInfoCtaClickListener = callback
    }

    private fun setupUpdateInfo(data: WidgetExpandableUiModel) {
        setupDescUpdateDate(data)
        iconPmProDowngradeStatus?.showWithCondition(data.isDowngradePeriod())
    }

    private fun setupDescUpdateDate(data: WidgetExpandableUiModel) {
        if (data.isDowngradePeriod()) {
            tvNextUpdatePmProStatus.setTextMakeHyperlink(
                MethodChecker.fromHtml(
                    context.getString(
                        R.string.pm_next_update_benefit_package_downgrade_status,
                        data.nextMonthlyRefreshDate,
                        data.grade?.shopLevel,
                        data.grade?.gradeName?.asCamelCase()
                    )
                ).toString()
            ) {
                updateInfoCtaClickListener?.invoke()
            }
        } else {
            if (data.nextShopLevel == data.grade?.shopLevel) {
                tvNextUpdatePmProStatus.text = context.getString(
                    R.string.pm_next_update_benefit_package_upgrade_max_status
                )
            } else {
                tvNextUpdatePmProStatus.setTextMakeHyperlink(
                    MethodChecker.fromHtml(
                        context.getString(
                            R.string.pm_next_update_benefit_package_upgrade_status,
                            data.nextMonthlyRefreshDate,
                            data.nextShopLevel,
                            data.nextGradeName.asCamelCase()
                        )
                    ).toString()
                ) {
                    updateInfoCtaClickListener?.invoke()
                }
            }
        }
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