package com.tokopedia.power_merchant.subscribe.view.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.constant.PowerMerchantUrl
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment.Companion.MINIMUM_SCORE_ACTIVATE_IDLE
import com.tokopedia.power_merchant.subscribe.view.util.PowerMerchantDateFormatter.formatCancellationDate
import kotlinx.android.synthetic.main.layout_power_merchant_membership.view.*

class PowerMerchantMembershipView: ConstraintLayout {

    private var tracker: PowerMerchantTracking? = null

    constructor (context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.layout_power_merchant_membership, this)
    }

    fun show(
        powerMerchantStatus: PowerMerchantStatus,
        tracker: PowerMerchantTracking,
        onClickUpgradeBtn: () -> Unit
    ) {
        val shopScore = powerMerchantStatus.shopScore.data.value
        val shopStatus = powerMerchantStatus.goldGetPmOsStatus.result.data

        setTracker(tracker)
        showTextWarning(shopStatus)
        showShopStatus(shopScore)
        showShopScore(shopScore)
        showShopScoreDescription(shopScore)
        showPerformanceTipsBtn(shopScore)
        setUpgradeBtnListener(onClickUpgradeBtn)
        showLayout()
    }

    private fun setTracker(tracker: PowerMerchantTracking) {
        this.tracker = tracker
    }

    private fun showTextWarning(shopStatus: ShopStatusModel) {
        if(shopStatus.isAutoExtend()) {
            containerWarning.hide()
        } else {
            val expiredDate = shopStatus.powerMerchant.expiredTime
            textWarning.text = formatCancellationDate(context, R.string.expired_label, expiredDate)
            containerWarning.show()
        }
    }

    private fun setUpgradeBtnListener(onClickUpgradeBtn: () -> Unit) {
        btnUpgrade.setOnClickListener {
            onClickUpgradeBtn.invoke()
        }
    }

    private fun showShopStatus(shopScore: Int) {
        if(shopScore < MINIMUM_SCORE_ACTIVATE_IDLE) {
            val textColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R500)
            textStatus.setTextColor(textColor)
            textStatus.text = context.getString(R.string.power_merchant_inactive)
        } else {
            val textColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)
            textStatus.setTextColor(textColor)
            textStatus.text = context.getString(R.string.power_merchant_active)
        }
    }

    private fun showShopScore(shopScore: Int) {
        textShopScore.text = shopScore.toString()
        textShopScoreHint.text = context.getString(R.string.power_merchant_shop_score_member_hint)
    }

    private fun showShopScoreDescription(shopScore: Int) {
        textShopScoreDescription.text = if (shopScore < MINIMUM_SCORE_ACTIVATE_IDLE) {
            context.getString(R.string.power_merchant_shop_score_description_not_eligible)
        } else {
            context.getString(R.string.power_merchant_shop_score_description_good)
        }
    }

    private fun showPerformanceTipsBtn(shopScore: Int) {
        val shouldShow = shopScore < MINIMUM_SCORE_ACTIVATE_IDLE
        btnPerformanceTips.showWithCondition(shouldShow)
        btnPerformanceTips.setOnClickListener {
            trackClickPerformanceTipsBtn()
            goToWebViewPage(PowerMerchantUrl.URL_SHOP_PERFORMANCE_TIPS)
        }
    }

    private fun trackClickPerformanceTipsBtn() {
        tracker?.eventClickPerformanceTipsBtn()
    }

    private fun goToWebViewPage(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    private fun showLayout() {
        visibility = View.VISIBLE
    }
}