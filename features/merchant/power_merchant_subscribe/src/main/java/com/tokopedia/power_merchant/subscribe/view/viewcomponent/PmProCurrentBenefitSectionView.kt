package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gm.common.constant.PMShopGrade
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil.getColorHexString
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil.setTextMakeHyperlink
import com.tokopedia.power_merchant.subscribe.databinding.ItemBenefitPackageStatusPmProBinding
import com.tokopedia.power_merchant.subscribe.view.model.WidgetExpandableUiModel
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

/**
 * Created By @ilhamsuaib on 07/05/21
 */

class PmProCurrentBenefitSectionView : ConstraintLayout {

    private var binding: ItemBenefitPackageStatusPmProBinding? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var updateInfoCtaClickListener: (() -> Unit)? = null

    init {
        binding = ItemBenefitPackageStatusPmProBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun show(data: WidgetExpandableUiModel) {
        showPmGrade(data.grade?.gradeName.orEmpty())
        setupUpdateInfo(data)
        setupDescBenefitSection(data)
        setBenefitPackageClicked()
    }

    private fun setBenefitPackageClicked() {
        binding?.iconPmProStatus?.setOnClickListener {
            updateInfoCtaClickListener?.invoke()
        }
    }

    fun setOnUpdateInfoCtaClickedListener(callback: () -> Unit) {
        this.updateInfoCtaClickListener = callback
    }

    private fun setupUpdateInfo(data: WidgetExpandableUiModel) {
        setupDescUpdateDate(data)
        binding?.iconPmProDowngradeStatus?.showWithCondition(data.isDowngradePeriod())
    }

    private fun setupDescBenefitSection(data: WidgetExpandableUiModel) = binding?.run {
        if (data.pmStatus == PMStatusConst.IDLE) {
            containerDescBenefitPackage.hide()
        } else {
            containerDescBenefitPackage.show()
        }
    }

    private fun setupDescUpdateDate(data: WidgetExpandableUiModel) = binding?.run {
        val blackColor = if (context.isDarkMode()) {
            getColorHexString(context, R.color.pm_static_nn950_night_dms)
        } else {
            getColorHexString(context, R.color.pm_static_nn950_light_dms)
        }

        iconPmProDowngradeStatus.showWithCondition(data.isDowngradePeriod())
        if (data.isDowngradePeriod()) {
            tvNextUpdatePmProStatus.setTextMakeHyperlink(
                context.getString(
                    R.string.pm_next_update_benefit_package_downgrade_status,
                    data.nextMonthlyRefreshDate,
                    blackColor,
                    data.grade?.shopLevel,
                    data.grade?.gradeName?.asCamelCase()
                )
            ) {
                goToShopScorePage()
            }
        } else {
            if (data.nextShopLevel == data.grade?.shopLevel) {
                tvNextUpdatePmProStatus.text = context.getString(
                    R.string.pm_next_update_benefit_package_upgrade_max_status
                )
            } else {
                tvNextUpdatePmProStatus.setTextMakeHyperlink(
                    context.getString(
                        R.string.pm_next_update_benefit_package_upgrade_status,
                        data.nextMonthlyRefreshDate,
                        blackColor,
                        data.nextShopLevel,
                        data.nextGradeName.asCamelCase()
                    )
                ) {
                    goToShopScorePage()
                }
            }
        }
    }

    private fun goToShopScorePage() {
        context?.let {
            RouteManager.route(it, ApplinkConstInternalMarketplace.PM_BENEFIT_PACKAGE)
        }
    }

    private fun showPmGrade(grade: String) = binding?.run {
        labelPmProStatus.unlockFeature = true
        labelPmProStatus.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
        labelPmProStatus.text = grade.asCamelCase()
        when {
            PMShopGrade.EXPERT.equals(grade, true) -> {
                labelPmProStatus.setBackgroundColor(context.getResColor(com.tokopedia.power_merchant.subscribe.R.color.pm_static_tn400_dms))
                bgBenefitPackageStatus.setImageResource(R.drawable.bg_pm_benefit_package_expert)
            }
            PMShopGrade.ULTIMATE.equals(grade, true) -> {
                labelPmProStatus.setBackgroundColor(context.getResColor(com.tokopedia.power_merchant.subscribe.R.color.pm_static_yn400_dms))
                bgBenefitPackageStatus.setImageResource(R.drawable.bg_pm_benefit_package_ultimate)
            }
            else -> { //PMShopGrade.ADVANCED
                labelPmProStatus.setBackgroundColor(context.getResColor(com.tokopedia.power_merchant.subscribe.R.color.pm_static_nn500_dms))
                bgBenefitPackageStatus.setImageResource(R.drawable.bg_pm_benefit_package_advanced)
            }
        }
    }
}