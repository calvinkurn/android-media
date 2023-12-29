package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.utils.PMCommonUtils
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.databinding.WidgetPmShopGradeBinding
import com.tokopedia.power_merchant.subscribe.view.model.PMProStatusInfoUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetShopGradeUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class ShopGradeWidget(
    itemView: View,
    private val listener: Listener,
    private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<WidgetShopGradeUiModel>(itemView) {

    companion object {
        private const val DATE_FORMAT = "dd MMM yyyy"
        private const val END_OF_TENURE_DAYS = 60L
        val RES_LAYOUT = R.layout.widget_pm_shop_grade
        const val SATURATION_INACTIVE = 0.0f
    }

    private val binding: WidgetPmShopGradeBinding? by viewBinding()

    override fun bind(element: WidgetShopGradeUiModel) {
        setupShopGrade(element)
        setupShopScore(element)
        showTopedIllustration(element)
        setupCurrentGradeStepper(element)
    }

    private fun setupCurrentGradeStepper(element: WidgetShopGradeUiModel) {
        binding?.run {
            val isPm = element.shopGrade == PMConstant.ShopGrade.PM
            val isPmProAdvance = element.shopGrade == PMConstant.ShopGrade.PRO_ADVANCE
            val isPmProExpert = element.shopGrade == PMConstant.ShopGrade.PRO_EXPERT
            val isPmProUltimate = element.shopGrade == PMConstant.ShopGrade.PRO_ULTIMATE

            when {
                isPm || element.pmStatus == PMStatusConst.IDLE -> {
                    stepInActive(badgePmProAdvanced, textPmProAdvanced)
                    stepInActive(badgePmProExpert, textPmProExpert)
                    stepInActive(badgePmProUltimate, textPmProUltimate)
                    separator2.stepSeparatorInActive()
                    separator3.stepSeparatorInActive()
                }
                isPmProAdvance -> {
                    stepInActive(badgePm)
                    stepInActive(badgePmProExpert, textPmProExpert)
                    stepInActive(badgePmProUltimate, textPmProUltimate)
                    separator.stepSeparatorInActive()
                    separator3.stepSeparatorInActive()
                    textPmProAdvanced.setWeight(Typography.BOLD)
                }
                isPmProExpert -> {
                    stepInActive(badgePm)
                    stepInActive(badgePmProAdvanced, textPmProAdvanced)
                    stepInActive(badgePmProUltimate, textPmProUltimate)
                    separator.stepSeparatorInActive()
                    separator2.stepSeparatorInActive()
                    textPmProExpert.setWeight(Typography.BOLD)
                }
                isPmProUltimate -> {
                    stepInActive(badgePm)
                    stepInActive(badgePmProAdvanced, textPmProAdvanced)
                    stepInActive(badgePmProExpert, textPmProExpert)
                    separator.stepSeparatorInActive()
                    separator2.stepSeparatorInActive()
                    separator3.stepSeparatorInActive()
                    textPmProUltimate.setWeight(Typography.BOLD)
                }
            }

            chevronPmGrade.isVisible = element.pmStatus == PMStatusConst.ACTIVE
            chevronPmGrade.setOnClickListener {
                listener.goToMembershipDetail()
                powerMerchantTracking.sendEventClickProgressBar(element.shopGrade)
            }
        }
    }

    private fun stepInActive(stepIcon: IconUnify?, text: Typography? = null) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(SATURATION_INACTIVE)
        val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
        stepIcon?.iconImg?.colorFilter = colorMatrixColorFilter
        stepIcon?.layoutParams?.height =
            itemView.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
        stepIcon?.layoutParams?.width =
            itemView.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)

        text?.setTextColor(
            ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN400
            )
        )
    }

    private fun View?.stepSeparatorInActive() {
        this?.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN200
            )
        )
    }

    private fun showTopedIllustration(element: WidgetShopGradeUiModel) {
        val imgUrl: Triple<Int, Int, String> = getIllustrationUrl(element)
        binding?.imgPmShopGradeIllustration?.loadImageWithoutPlaceholder(imgUrl.third)
        setTopedImageSize(Pair(imgUrl.first, imgUrl.second))
    }

    private fun getIllustrationUrl(element: WidgetShopGradeUiModel): Triple<Int, Int, String> {
        val isPmActive = element.pmStatus == PMStatusConst.ACTIVE
        val isPmPro = element.pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        return when {
            isPmPro && isPmActive && element.isNewSeller -> {
                Triple(
                    com.tokopedia.gm.common.R.dimen.gmc_dimen_128dp,
                    com.tokopedia.gm.common.R.dimen.gmc_dimen_134dp,
                    PMConstant.Images.IMG_TOPED_NEW_SELLER_PM_PRO_ACTIVE
                )
            }
            !isPmPro && isPmActive && element.isNewSeller -> {
                Triple(
                    com.tokopedia.gm.common.R.dimen.gmc_dimen_112dp,
                    com.tokopedia.gm.common.R.dimen.gmc_dimen_122dp,
                    PMConstant.Images.IMG_TOPED_NEW_SELLER_PM_ACTIVE
                )
            }
            isPmPro && isPmActive && !element.isNewSeller -> {
                Triple(
                    com.tokopedia.gm.common.R.dimen.gmc_dimen_128dp,
                    com.tokopedia.gm.common.R.dimen.gmc_dimen_134dp,
                    PMConstant.Images.IMG_TOPED_PM_PRO_ACTIVE
                )
            }
            !isPmPro && isPmActive && !element.isNewSeller -> {
                Triple(
                    com.tokopedia.gm.common.R.dimen.gmc_dimen_112dp,
                    com.tokopedia.gm.common.R.dimen.gmc_dimen_122dp,
                    PMConstant.Images.IMG_TOPED_PM_ACTIVE
                )
            }
            else -> {
                Triple(
                    com.tokopedia.gm.common.R.dimen.gmc_dimen_112dp,
                    com.tokopedia.gm.common.R.dimen.gmc_dimen_114dp,
                    PMConstant.Images.IMG_TOPED_PM_INACTIVE
                )
            }
        }
    }

    private fun setTopedImageSize(illustrationSize: Pair<Int, Int>) =
        binding?.imgPmShopGradeIllustration?.run {
            val imageWidth = context.resources.getDimensionPixelSize(illustrationSize.first)
            val imageHeight = context.resources.getDimensionPixelSize(illustrationSize.second)
            layoutParams.width = imageWidth
            layoutParams.height = imageHeight
            requestLayout()
        }

    private fun setupShopScore(element: WidgetShopGradeUiModel) = binding?.run {
        val isPmActive = element.pmStatus == PMStatusConst.ACTIVE
        val isPmPro = element.pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        val isPmProActive = isPmPro && isPmActive
        val isPmProIdle = isPmPro && element.pmStatus == PMStatusConst.IDLE
        val is30FirstMondayNewSeller = element.isNewSeller && element.is30FirstMonday
        val labelStringId = getLabelStringResId(element)
        val shopScoreFmt = PMCommonUtils.getShopScoreFmt(element.shopScore)

        tvPmShopGradeScore.text =
            root.context.getString(labelStringId, getShopScoreTextColor(element), shopScoreFmt)
                .parseAsHtml()
        tvPmShopGradeScoreTotal.text = root.context.getString(R.string.power_merchant_max_score)

        val shopGradeInfo = getPmShopGradeInfo(element)
        if (isPmProActive) {
            tvPmShopGradeThreshold.showWithCondition(isPmActive && is30FirstMondayNewSeller)
            if (is30FirstMondayNewSeller) {
                tvPmShopGradeThreshold.text = shopGradeInfo.parseAsHtml()
            } else {
                val layoutParams =
                    tvPmShopGradeThreshold.layoutParams as? ConstraintLayout.LayoutParams
                layoutParams?.topMargin =
                    root.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
                tvPmShopGradeThreshold.layoutParams = layoutParams
            }
        } else {
            pmProStatusInfoView.gone()
            tvPmShopGradeThreshold.visible()
            tvPmShopGradeThreshold.text = shopGradeInfo.parseAsHtml()
        }

        when {
            isPmProActive -> {
                pmProStatusInfoView.visible()
                pmProStatusInfoView.showIcon()
                pmProStatusInfoView.setText(R.string.pm_check_pm_pro_status_info)
                pmProStatusInfoView.setOnClickListener {
                    listener.showPmProStatusInfo(getPmProStatusInfo(element))
                }
            }
            isPmActive && !element.isNewSeller -> {
                pmProStatusInfoView.visible()
                pmProStatusInfoView.hideIcon()
                pmProStatusInfoView.setText(R.string.pm_active_cta_if_pm_not_active)
                pmProStatusInfoView.setOnClickListener {
                    listener.showHelpPmNotActive()
                }
            }
            else -> {
                pmProStatusInfoView.gone()
            }
        }

        val isPmShopScoreTipsVisible = element.pmStatus == PMStatusConst.IDLE
        tvPmShopScoreTips.isVisible = isPmShopScoreTipsVisible
        icPmShopScoreTips.isVisible = isPmShopScoreTipsVisible
        tvPmShopScoreTips.setOnClickListener {
            if (isPmProIdle) {
                RouteManager.route(root.context, Constant.Url.PM_PRO_IDLE)
            } else {
                RouteManager.route(root.context, Constant.Url.SHOP_PERFORMANCE_TIPS)
            }
            powerMerchantTracking.sendEventClickTipsToImproveShopScore(element.shopScore.toString())
        }
        wrapperPmShopScore.setOnClickListener {
            RouteManager.route(root.context, ApplinkConst.SHOP_SCORE_DETAIL)
        }
    }

    private fun getPmShopGradeInfo(element: WidgetShopGradeUiModel): String {
        val textColor = PMCommonUtils.getHexColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
        )
        return if (element.pmStatus == PMStatusConst.ACTIVE) {
            if (element.isNewSeller) {
                val endOfTenure = getEndOfTenureDate(element)
                itemView.context.getString(
                    R.string.pm_shop_grade_shop_score_threshold_description_pm_active_new_seller,
                    endOfTenure
                )
            } else {
                itemView.context.getString(
                    R.string.pm_shop_grade_shop_score_threshold_description_pm_active,
                    textColor,
                    element.shopScoreThreshold,
                    getPmTireLabel(element)
                )
            }
        } else {
            itemView.context.getString(
                R.string.pm_shop_grade_shop_score_threshold_description_pm_idle,
                textColor,
                element.shopScoreThreshold,
                getPmTireLabel(element)
            )
        }
    }

    private fun getEndOfTenureDate(element: WidgetShopGradeUiModel): String {
        val shopAge = element.shopAge
        val nowMillis = Date().time
        val remainingDays = END_OF_TENURE_DAYS.minus(shopAge)
        return if (remainingDays < END_OF_TENURE_DAYS) {
            val remainingDaysMillis = TimeUnit.DAYS.toMillis(remainingDays)
            val endOfTenureMillis = nowMillis.plus(remainingDaysMillis)
            DateFormatUtils.getFormattedDate(endOfTenureMillis, DATE_FORMAT)
        } else {
            DateFormatUtils.getFormattedDate(nowMillis, DATE_FORMAT)
        }
    }

    private fun getShopScoreTextColor(element: WidgetShopGradeUiModel): String {
        val minScore = Int.ONE
        return when (element.shopScore) {
            in minScore..element.shopScoreThreshold -> {
                PMCommonUtils.getHexColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_RN500
                )
            }
            else -> PMCommonUtils.getHexColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
            )
        }
    }

    private fun setupShopGrade(element: WidgetShopGradeUiModel) = binding?.run {
        tvPmShopGrade.text = getPmTireLabel(element)
        imgPmShopGradeBackground.loadImage(element.gradeBackgroundUrl)
        showBadgeImageUrl(element)

        val isPmStatusActive = element.pmStatus == PMStatusConst.ACTIVE
        if (isPmStatusActive) {
            tvPmShopGradeStatus.setTextColor(root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            tvPmShopGradeStatus.setBackgroundResource(R.drawable.bg_pm_status_label_active)
        } else {
            tvPmShopGradeStatus.setTextColor(root.context.getResColor(R.color.pm_static_r600_dms))
            tvPmShopGradeStatus.setBackgroundResource(R.drawable.bg_pm_status_label_inactive)
        }
        tvPmShopGradeStatus.text = getPMStatusLabel(element.pmStatus)
        tvPmShopGrade.setTextColor(getPmLabelTextColor(element.pmStatus))
    }

    private fun showBadgeImageUrl(element: WidgetShopGradeUiModel) {
        binding?.run {
            val isPmActive = element.pmStatus == PMStatusConst.ACTIVE
            val isPmPro = element.pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO
            val imageUrl = when {
                isPmPro && isPmActive -> PMConstant.Images.PM_PRO_BADGE
                !isPmPro && isPmActive -> PMConstant.Images.PM_BADGE
                else -> PMConstant.Images.PM_BADGE_INACTIVE
            }
            imgPmShopGrade.loadImageWithoutPlaceholder(imageUrl)
        }
    }

    private fun getPmTireLabel(element: WidgetShopGradeUiModel): String {
        val isPmPro = element.pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        val isPmActive = element.pmStatus == PMStatusConst.ACTIVE
        return if (isPmPro && isPmActive) {
            getString(R.string.pm_power_merchant_pro)
        } else {
            getString(R.string.pm_power_merchant)
        }
    }

    private fun getPmLabelTextColor(pmStatus: String): Int {
        return when (pmStatus) {
            PMStatusConst.ACTIVE -> itemView.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
            else -> itemView.context.getResColor(R.color.pm_static_n700_96_dms)
        }
    }

    private fun getPMStatusLabel(pmStatus: String): String {
        return when (pmStatus) {
            PMStatusConst.ACTIVE -> itemView.context.getString(R.string.pm_active)
            else -> itemView.context.getString(R.string.pm_inactive)
        }
    }

    private fun getPmProStatusInfo(element: WidgetShopGradeUiModel): PMProStatusInfoUiModel {
        return PMProStatusInfoUiModel(
            nextMonthlyRefreshDate = getFormattedRefreshDate(element.nextMonthlyRefreshDate),
            pmActiveShopScoreThreshold = element.shopScoreThreshold,
            pmProActiveShopScoreThreshold = element.pmProShopScoreThreshold,
            itemSoldThreshold = element.itemSoldThreshold,
            netItemValueThreshold = element.netItemValueThreshold
        )
    }

    private fun getFormattedRefreshDate(nextMonthlyRefreshDate: String): String {
        return DateFormatUtils.formatDate(
            DateFormatUtils.FORMAT_YYYY_MM_DD,
            DateFormatUtils.FORMAT_DD_MMMM_YYYY,
            nextMonthlyRefreshDate
        )
    }

    private fun getLabelStringResId(element: WidgetShopGradeUiModel): Int {
        return if (element.isNewSeller) {
            if (element.pmStatus == PMStatusConst.ACTIVE) {
                R.string.pm_shop_performance_sum_new_seller
            } else {
                R.string.pm_shop_performance_sum
            }
        } else {
            R.string.pm_shop_performance_sum
        }
    }

    interface Listener {
        fun showPmProStatusInfo(model: PMProStatusInfoUiModel)
        fun showHelpPmNotActive()
        fun goToMembershipDetail()
    }
}
