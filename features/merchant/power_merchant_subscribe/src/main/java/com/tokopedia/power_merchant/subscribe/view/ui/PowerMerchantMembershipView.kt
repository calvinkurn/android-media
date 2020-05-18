package com.tokopedia.power_merchant.subscribe.view.ui

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.constant.PowerMerchantUrl
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment.Companion.MINIMUM_SCORE_ACTIVATE_REGULAR
import com.tokopedia.power_merchant.subscribe.view.util.PowerMerchantSpannableUtil.createSpannableString
import kotlinx.android.synthetic.main.layout_power_merchant_membership.view.*

class PowerMerchantMembershipView: ConstraintLayout {

    constructor (context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    fun show(powerMerchantStatus: PowerMerchantStatus) {
        inflate(context, R.layout.layout_power_merchant_membership, this)
        val shopScore = powerMerchantStatus.shopScore.data.value
        val shopStatus = powerMerchantStatus.goldGetPmOsStatus.result.data

        showShopStatus(shopStatus)
        showShopScore(shopScore)
        showShopScoreDescription(shopScore)
        showPerformanceTipsBtn(shopScore)
        showPremiumAccountView()
    }

    private fun showShopStatus(shopStatus: ShopStatusModel) {
        if(shopStatus.isPowerMerchantActive()) {
            val textColor = ContextCompat.getColor(context, R.color.light_N700)
            textStatus.setTextColor(textColor)
            textStatus.text = context.getString(R.string.power_merchant_active)
        } else {
            val textColor = ContextCompat.getColor(context, R.color.light_R500)
            textStatus.setTextColor(textColor)
            textStatus.text = context.getString(R.string.power_merchant_inactive)
        }
    }

    private fun showShopScore(shopScore: Int) {
        textShopScore.text = shopScore.toString()
        textShopScoreHint.text = context.getString(R.string.power_merchant_shop_score_member_hint)
    }

    private fun showShopScoreDescription(shopScore: Int) {
        textShopScoreDescription.text = if (shopScore >= MINIMUM_SCORE_ACTIVATE_REGULAR) {
            context.getString(R.string.power_merchant_shop_description_good)
        } else {
            context.getString(R.string.power_merchant_shop_score_description_not_eligible)
        }
    }

    private fun showPerformanceTipsBtn(shopScore: Int) {
        val shouldShow = shopScore < MINIMUM_SCORE_ACTIVATE_REGULAR
        btnPerformanceTips.showWithCondition(shouldShow)
        btnPerformanceTips.setOnClickListener {
            goToWebViewPage(PowerMerchantUrl.URL_SHOP_PERFORMANCE_TIPS)
        }
    }

    private fun showPremiumAccountView() {
        val description = context.getString(R.string.power_merchant_premium_account_description)
        val clickableText = context.getString(R.string.power_merchant_premium_account_clickable_text)
        val clickableTextColor = ContextCompat.getColor(context, R.color.pm_main_color)

        textPremiumAccountDescription.text = createSpannableString(description, clickableText, clickableTextColor) {
            // TO-DO
        }
    }

    private fun goToWebViewPage(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }
}