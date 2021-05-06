package com.tokopedia.power_merchant.subscribe.view.ui

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.URL_LEARN_MORE_BENEFIT
import com.tokopedia.power_merchant.subscribe.view.activity.PowerMerchantTermsActivity
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment.Companion.MINIMUM_SCORE_ACTIVATE_REGULAR
import com.tokopedia.power_merchant.subscribe.view.util.PowerMerchantSpannableUtil.createSpannableString
import com.tokopedia.user_identification_common.KYCConstant
import kotlinx.android.synthetic.main.layout_power_merchant_registration.view.*

class PowerMerchantRegistrationView : ConstraintLayout {

    private var tracker: PowerMerchantTracking? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.layout_power_merchant_registration, this)
    }

    fun show(powerMerchantStatus: PowerMerchantStatus, tracker: PowerMerchantTracking) {
        val shopStatus = powerMerchantStatus.kycUserProjectInfoPojo.kycProjectInfo
        val shopScore = powerMerchantStatus.shopScore.data.value

        val kycVerified = shopStatus.status == KYCConstant.STATUS_VERIFIED
        val shopScoreEligible = shopScore >= MINIMUM_SCORE_ACTIVATE_REGULAR

        setTracker(tracker)
        showRegistrationCheckList()
        showVerificationCheckList(kycVerified)
        showShopScoreCheckList(shopScoreEligible)
        showDescription(kycVerified, shopScoreEligible, shopScore)
        showLayout()
    }

    private fun setTracker(tracker: PowerMerchantTracking) {
        this.tracker = tracker
    }

    private fun showDescription(kycVerified: Boolean, shopScoreEligible: Boolean, shopScore: Int) {
        val description = when {
            kycVerified && shopScoreEligible -> {
                val text = context.getString(R.string.power_merchant_full_eligibility_description)
                val clickableText = context.getString(R.string.power_merchant_register_text)
                val clickableTextColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)

                createSpannableString(text, clickableText, clickableTextColor, true) {
                    goToTermsAndConditionPage(shopScore)
                }
            }
            shopScoreEligible -> {
                context.getString(R.string.power_merchant_kyc_verified_description)
            }
            else -> {
                val text = context.getString(R.string.power_merchant_shop_score_description)
                val clickableText = context.getString(R.string.power_merchant_see_tips)
                val clickableTextColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)

                createSpannableString(text, clickableText, clickableTextColor) { goToLearMorePage() }
            }
        }

        textDescription.movementMethod = LinkMovementMethod.getInstance()
        textDescription.text = description
    }

    private fun showRegistrationCheckList() {
        val checkListIcon = R.drawable.ic_pm_steps_not_active
        textRegister.setCompoundDrawablesWithIntrinsicBounds(checkListIcon, 0, 0, 0)
    }

    private fun showVerificationCheckList(kycVerified: Boolean) {
        val checkListIcon = if (kycVerified) {
            R.drawable.ic_pm_steps_active
        } else {
            R.drawable.ic_pm_steps_not_active
        }
        textVerification.setCompoundDrawablesWithIntrinsicBounds(checkListIcon, 0, 0, 0)
    }

    private fun showShopScoreCheckList(shopScoreEligible: Boolean) {
        val checkListIcon = if (shopScoreEligible) {
            R.drawable.ic_pm_steps_active
        } else {
            R.drawable.ic_pm_steps_not_active
        }
        textShopScore.setCompoundDrawablesWithIntrinsicBounds(checkListIcon, 0, 0, 0)
    }

    private fun goToTermsAndConditionPage(shopScore: Int) {
        val intent = PowerMerchantTermsActivity.createIntent(context, shopScore)
        context.startActivity(intent)
    }

    private fun goToLearMorePage() {
        tracker?.eventLearnMorePm()
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, URL_LEARN_MORE_BENEFIT)
    }

    private fun showLayout() {
        visibility = View.VISIBLE
    }
}