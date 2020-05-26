package com.tokopedia.power_merchant.subscribe.view.ui

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.constant.PowerMerchantUrl
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment.Companion.MINIMUM_SCORE_ACTIVATE_IDLE
import com.tokopedia.power_merchant.subscribe.view.util.PowerMerchantSpannableUtil.createSpannableString
import kotlinx.android.synthetic.main.layout_power_merchant_membership.view.*

class PowerMerchantMembershipView: ConstraintLayout {

    constructor (context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.layout_power_merchant_membership, this)
    }

    fun show(powerMerchantStatus: PowerMerchantStatus, onClickUpgradeBtn: () -> Unit) {
        val shopScore = powerMerchantStatus.shopScore.data.value
        val shopStatus = powerMerchantStatus.goldGetPmOsStatus.result.data

        showTextWarning(shopStatus)
        showShopStatus(shopScore)
        showShopScore(shopScore)
        showShopScoreDescription(shopScore)
        showPerformanceTipsBtn(shopScore)
        setUpgradeBtnListener(onClickUpgradeBtn)
        showPremiumAccountView()
        showLayout()
    }

    private fun showTextWarning(shopStatus: ShopStatusModel) {
        if(shopStatus.isPowerMerchantIdle()) {
            containerWarning.show()

            val cancellationDate = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD,
                DateFormatUtils.FORMAT_D_MMMM_YYYY, shopStatus.powerMerchant.expiredTime)
            val warningText = context.getString(R.string.expired_label, cancellationDate)
            val highlightTextColor = ContextCompat.getColor(context, R.color.light_N700)

            textWarning.text = createSpannableString(warningText, cancellationDate, highlightTextColor, true)
        } else {
            containerWarning.hide()
        }
    }

    private fun setUpgradeBtnListener(onClickUpgradeBtn: () -> Unit) {
        btnUpgrade.setOnClickListener {
            onClickUpgradeBtn.invoke()
        }
    }

    private fun showShopStatus(shopScore: Int) {
        if(shopScore < MINIMUM_SCORE_ACTIVATE_IDLE) {
            val textColor = ContextCompat.getColor(context, R.color.light_R500)
            textStatus.setTextColor(textColor)
            textStatus.text = context.getString(R.string.power_merchant_inactive)
        } else {
            val textColor = ContextCompat.getColor(context, R.color.light_N700)
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
            goToWebViewPage(PowerMerchantUrl.URL_SHOP_PERFORMANCE_TIPS)
        }
    }

    private fun showPremiumAccountView() {
        val description = context.getString(R.string.power_merchant_premium_account_description)
        val clickableText = context.getString(R.string.power_merchant_premium_account_clickable_text)
        val clickableTextColor = ContextCompat.getColor(context, R.color.pm_main_color)

        textPremiumAccountDescription.movementMethod = LinkMovementMethod.getInstance()
        textPremiumAccountDescription.text = createSpannableString(description, clickableText, clickableTextColor) {
            goToWebViewPage(PowerMerchantUrl.URL_PREMIUM_ACCOUNT)
        }
    }

    private fun goToWebViewPage(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    private fun showLayout() {
        visibility = View.VISIBLE
    }
}