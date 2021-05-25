package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.utils.PMCommonUtils
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.view.model.WidgetShopGradeUiModel
import kotlinx.android.synthetic.main.widget_pm_shop_grade.view.*

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class ShopGradeWidget(
        itemView: View,
        private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<WidgetShopGradeUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_shop_grade
    }

    override fun bind(element: WidgetShopGradeUiModel) {
        setupShopGrade(element)
        setupShopScore(element)
        showTopedIllustration(element)
    }

    private fun showTopedIllustration(element: WidgetShopGradeUiModel) {
        val isPmActive = element.pmStatus == PMStatusConst.ACTIVE
        val isPmPro = element.pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        val illustrationSize: Pair<Int, Int>
        val imgUrl = when {
            isPmPro && isPmActive -> {
                illustrationSize = Pair(com.tokopedia.gm.common.R.dimen.gmc_dimen_128dp, com.tokopedia.gm.common.R.dimen.gmc_dimen_134dp)
                PMConstant.Images.IMG_TOPED_PM_PRO_ACTIVE
            }
            isPmPro && !isPmActive -> {
                illustrationSize = Pair(com.tokopedia.gm.common.R.dimen.gmc_dimen_136dp, com.tokopedia.gm.common.R.dimen.gmc_dimen_132dp)
                PMConstant.Images.IMG_TOPED_PM_PRO_INACTIVE
            }
            !isPmPro && isPmActive -> {
                illustrationSize = Pair(com.tokopedia.gm.common.R.dimen.gmc_dimen_112dp, com.tokopedia.gm.common.R.dimen.gmc_dimen_122dp)
                PMConstant.Images.IMG_TOPED_PM_ACTIVE
            }
            else -> {
                illustrationSize = Pair(com.tokopedia.gm.common.R.dimen.gmc_dimen_112dp, com.tokopedia.gm.common.R.dimen.gmc_dimen_114dp)
                PMConstant.Images.IMG_TOPED_PM_INACTIVE
            }
        }
        itemView.imgPmShopGradeIllustration.loadImageWithoutPlaceholder(imgUrl)
        setTopedImageSize(illustrationSize)
    }

    private fun setTopedImageSize(illustrationSize: Pair<Int, Int>) = with(itemView.imgPmShopGradeIllustration) {
        val imageWidth = context.resources.getDimensionPixelSize(illustrationSize.first)
        val imageHeight = context.resources.getDimensionPixelSize(illustrationSize.second)
        layoutParams.width = imageWidth
        layoutParams.height = imageHeight
        requestLayout()
    }

    private fun setupShopScore(element: WidgetShopGradeUiModel) = with(itemView) {
        val labelStringId = if (element.isNewSeller) {
            R.string.pm_shop_performance_sum_new_seller
        } else {
            R.string.pm_shop_performance_sum
        }

        tvPmShopGradeScore.text = context.getString(labelStringId, getShopScoreTextColor(element), getShopScoreFmt(element.shopScore)).parseAsHtml()
        tvPmShopGradeScoreTotal.text = context.getString(R.string.power_merchant_max_score)
        val textColor = PMCommonUtils.getHexColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        val thresholdInfo = context.getString(R.string.pm_shop_score_threshold_description_final_period, textColor, element.threshold, getPmTireLabel(element.pmTierType))
        tvPmShopGradeThreshold.text = thresholdInfo.parseAsHtml()

        val isPmShopScoreTipsVisible = element.pmStatus == PMStatusConst.IDLE
        tvPmShopScoreTips.isVisible = isPmShopScoreTipsVisible
        icPmShopScoreTips.isVisible = isPmShopScoreTipsVisible
        tvPmShopScoreTips.setOnClickListener {
            RouteManager.route(context, Constant.Url.URL_SHOP_PERFORMANCE_TIPS)
            powerMerchantTracking.sendEventClickTipsToImproveShopScore()
        }
    }

    private fun getPmTireLabel(pmTierType: Int): String {
        return if (pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO) {
            getString(R.string.pm_power_merchant_pro)
        } else {
            getString(R.string.pm_power_merchant)
        }
    }

    private fun getShopScoreTextColor(element: WidgetShopGradeUiModel): String {
        return when (element.shopScore) {
            in 1..element.threshold -> {
                PMCommonUtils.getHexColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_R600)
            }
            else -> PMCommonUtils.getHexColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        }
    }

    private fun getShopScoreFmt(shopScore: Int): String {
        return when {
            (shopScore <= 0) -> "-"
            else -> shopScore.toString()
        }
    }

    private fun setupShopGrade(element: WidgetShopGradeUiModel) = with(itemView) {
        val isPmPro = element.pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        tvPmShopGrade.text = if (isPmPro) {
            getString(R.string.pm_power_merchant_pro)
        } else {
            getString(R.string.pm_power_merchant)
        }
        imgPmShopGradeBackground.loadImage(element.gradeBackgroundUrl, R.drawable.bg_pm_registration_header)
        imgPmShopGrade.loadImageWithoutPlaceholder(element.gradeBadgeImgUrl)
        val isPmStatusActive = element.pmStatus == PMStatusConst.ACTIVE
        if (isPmStatusActive) {
            tvPmShopGradeStatus.setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
            tvPmShopGradeStatus.setBackgroundResource(R.drawable.bg_pm_status_label_active)
        } else {
            tvPmShopGradeStatus.setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_R600))
            tvPmShopGradeStatus.setBackgroundResource(R.drawable.bg_pm_status_label_inactive)
        }
        tvPmShopGradeStatus.text = getPMStatusLabel(element.pmStatus)
    }

    private fun getPMStatusLabel(pmStatus: String): String {
        return when (pmStatus) {
            PMStatusConst.ACTIVE -> itemView.context.getString(R.string.pm_active)
            else -> itemView.context.getString(R.string.pm_inactive)
        }
    }
}