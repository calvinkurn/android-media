package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.utils.PMCommonUtils
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.databinding.WidgetPmShopGradeBinding
import com.tokopedia.power_merchant.subscribe.view.model.PMProStatusInfoUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetShopGradeUiModel
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
        val RES_LAYOUT = R.layout.widget_pm_shop_grade
    }

    private val binding: WidgetPmShopGradeBinding? by viewBinding()

    override fun bind(element: WidgetShopGradeUiModel) {
        setupShopGrade(element)
        setupShopScore(element)
        showTopedIllustration(element)
    }

    private fun showTopedIllustration(element: WidgetShopGradeUiModel) {
        val isPmActive = element.pmStatus == PMStatusConst.ACTIVE
        val isPmPro = element.pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        val imgUrl: Triple<Int, Int, String> =
            if (element.isNewSeller) {
                when {
                    isPmPro && isPmActive -> {
                        Triple(
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_128dp,
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_134dp,
                            PMConstant.Images.IMG_TOPED_NEW_SELLER_PM_PRO_ACTIVE
                        )
                    }
                    isPmPro && !isPmActive -> {
                        Triple(
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_136dp,
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_132dp,
                            PMConstant.Images.IMG_TOPED_NEW_SELLER_PM_PRO_INACTIVE

                        )
                    }
                    !isPmPro && isPmActive -> {
                        Triple(
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_112dp,
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_122dp,
                            PMConstant.Images.IMG_TOPED_NEW_SELLER_PM_INACTIVE
                        )
                    }
                    else -> {
                        Triple(
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_112dp,
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_114dp,
                            PMConstant.Images.IMG_TOPED_NEW_SELLER_PM_ACTIVE
                        )
                    }
                }
            } else {
                when {
                    isPmPro && isPmActive -> {
                        Triple(
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_128dp,
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_134dp,
                            PMConstant.Images.IMG_TOPED_PM_PRO_ACTIVE
                        )
                    }
                    isPmPro && !isPmActive -> {
                        Triple(
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_136dp,
                            com.tokopedia.gm.common.R.dimen.gmc_dimen_132dp,
                            PMConstant.Images.IMG_TOPED_PM_PRO_INACTIVE
                        )
                    }
                    !isPmPro && isPmActive -> {
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
        binding?.imgPmShopGradeIllustration?.loadImageWithoutPlaceholder(imgUrl.third)
        setTopedImageSize(Pair(imgUrl.first, imgUrl.second))
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
        val labelStringId = if (element.isNewSeller) {
            R.string.pm_shop_performance_sum_new_seller
        } else {
            R.string.pm_shop_performance_sum
        }
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
            pmProStatusInfoView.visible()
            pmProStatusInfoView.setOnClickListener {
                listener.showPmProStatusInfo(getPmProStatusInfo(element))
            }
        } else {
            pmProStatusInfoView.gone()
            tvPmShopGradeThreshold.visible()
            tvPmShopGradeThreshold.text = shopGradeInfo.parseAsHtml()
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
            com.tokopedia.unifyprinciples.R.color.Unify_N700_96
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
                    getPmTireLabel(element.pmTierType)
                )
            }
        } else {
            itemView.context.getString(
                R.string.pm_shop_grade_shop_score_threshold_description_pm_idle,
                textColor,
                element.shopScoreThreshold,
                getPmTireLabel(element.pmTierType)
            )
        }
    }

    private fun getEndOfTenureDate(element: WidgetShopGradeUiModel): String {
        val endOfTenureDays = 90L
        val shopAge = element.shopAge
        val nowMillis = Date().time
        val remainingDays = endOfTenureDays.minus(shopAge)
        return if (remainingDays < endOfTenureDays) {
            val remainingDaysMillis = TimeUnit.DAYS.toMillis(remainingDays)
            val endOfTenureMillis = nowMillis.plus(remainingDaysMillis)
            DateFormatUtils.getFormattedDate(endOfTenureMillis, DATE_FORMAT)
        } else {
            DateFormatUtils.getFormattedDate(nowMillis, DATE_FORMAT)
        }
    }

    private fun getShopScoreTextColor(element: WidgetShopGradeUiModel): String {
        val minScore = 1
        return when (element.shopScore) {
            in minScore..element.shopScoreThreshold -> {
                PMCommonUtils.getHexColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_R600
                )
            }
            else -> PMCommonUtils.getHexColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_96
            )
        }
    }

    private fun setupShopGrade(element: WidgetShopGradeUiModel) = binding?.run {
        tvPmShopGrade.text = getPmTireLabel(element.pmTierType)
        imgPmShopGradeBackground.loadImage(element.gradeBackgroundUrl)
        imgPmShopGrade.loadImageWithoutPlaceholder(element.gradeBadgeImgUrl)

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

    private fun getPmTireLabel(pmTierType: Int): String {
        return if (pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO) {
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
            autoExtendDateFmt = element.autoExtendDateStr,
            pmActiveShopScoreThreshold = element.shopScoreThreshold,
            pmProActiveShopScoreThreshold = element.pmProShopScoreThreshold,
            itemSoldThreshold = element.itemSoldThreshold,
            netItemValueThreshold = element.netItemValueThreshold
        )
    }

    interface Listener {
        fun showPmProStatusInfo(model: PMProStatusInfoUiModel)

    }
}