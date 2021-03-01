package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view_old.constant.PowerMerchantUrl
import com.tokopedia.power_merchant.subscribe.view_old.fragment.PowerMerchantSubscribeFragment.Companion.MINIMUM_SCORE_ACTIVATE_IDLE
import kotlinx.android.synthetic.main.layout_power_merchant_membership.view.*
import kotlinx.android.synthetic.main.layout_power_merchant_membership.view.btnPerformanceTips
import kotlinx.android.synthetic.main.layout_power_merchant_membership.view.imageMembership
import kotlinx.android.synthetic.main.layout_power_merchant_membership.view.textShopScore
import kotlinx.android.synthetic.main.layout_power_merchant_membership.view.textShopScoreDescription
import kotlinx.android.synthetic.main.layout_power_merchant_membership.view.textShopScoreHint
import kotlinx.android.synthetic.main.layout_power_merchant_membership.view.textStatus
import kotlinx.android.synthetic.main.view_pm_power_merchant_status.view.*

class PowerMerchantStatusView : ConstraintLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.view_pm_power_merchant_status, this)
    }

    fun show(
            powerMerchantStatus: PowerMerchantStatus,
            onClickUpgradeBtn: () -> Unit
    ) {
        imageMembership.loadImageDrawable(R.drawable.ic_pm_membership)
        val shopScore = powerMerchantStatus.shopScore.data.value
        val shopStatus = powerMerchantStatus.goldGetPmOsStatus.result.data

        showShopStatus(shopScore)
        showShopScore(shopScore, 75)
        showShopScoreDescription(shopScore)
        showPerformanceTipsBtn(shopScore)
        showLayout()
    }

    private fun showShopStatus(shopScore: Int) {
        if (shopScore < MINIMUM_SCORE_ACTIVATE_IDLE) {
            val textColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R500)
            textStatus.setTextColor(textColor)
            textStatus.text = context.getString(R.string.power_merchant_inactive)
        } else {
            val textColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)
            textStatus.setTextColor(textColor)
            textStatus.text = context.getString(R.string.power_merchant_active)
        }
    }

    private fun showShopScore(shopScore: Int, threshold: Int) {
        textShopScore.text = shopScore.toString()
        textShopScoreHint.text = context.getString(R.string.pm_shop_score_member_hint, threshold).parseAsHtml()
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
            goToWebViewPage(PowerMerchantUrl.URL_SHOP_PERFORMANCE_TIPS)
        }
    }

    private fun goToWebViewPage(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    private fun showLayout() {
        visibility = View.VISIBLE
    }
}