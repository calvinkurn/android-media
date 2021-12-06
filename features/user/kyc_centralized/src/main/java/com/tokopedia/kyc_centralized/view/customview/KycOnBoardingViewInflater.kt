package com.tokopedia.kyc_centralized.view.customview

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kyc_centralized.KycUrl
import com.tokopedia.kyc_centralized.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton

object KycOnBoardingViewInflater {

    fun setupKycBenefitToolbar(activity: Activity?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity?.window ?: return
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = MethodChecker.getColor(activity,
                com.tokopedia.unifyprinciples.R.color.Unify_T200)
        }

        if(activity != null && activity is AppCompatActivity) {
            activity.supportActionBar?.hide()
        }
    }

    fun setupKycBenefitView(view: View, mainAction: () -> Unit, closeButtonAction: () -> Unit) {
        val kycBenefitImage = view.findViewById<ImageUnify>(R.id.image_banner)
        kycBenefitImage?.cornerRadius = 0
        kycBenefitImage.loadImage(KycUrl.KYC_BENEFIT_BANNER)

        val kycBenefitPowerMerchant = view.findViewById<ImageUnify>(R.id.image_power_merchant)
        kycBenefitPowerMerchant.loadImage(KycUrl.KYC_BENEFIT_POWER_MERCHANT)

        val kycBenefitFintech = view.findViewById<ImageUnify>(R.id.image_fintech)
        kycBenefitFintech.loadImage(KycUrl.KYC_BENEFIT_FINTECH)

        val kycBenefitShield = view.findViewById<ImageUnify>(R.id.image_shiled_star)
        kycBenefitShield.loadImage(KycUrl.KYC_BENEFIT_SHIELD)

        val benefitButton: UnifyButton? = view.findViewById(R.id.kyc_benefit_btn)
        benefitButton?.setOnClickListener {
            mainAction()
        }

        val kycBenefitCloseButton: UnifyImageButton? = view.findViewById(R.id.close_button)
        kycBenefitCloseButton?.setOnClickListener {
            closeButtonAction()
        }
    }

    fun restoreStatusBar(activity: Activity?, defaultStatusBarColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity?.window ?: return
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = if(defaultStatusBarColor.isZero()) {
                MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_Background)
            } else {
                defaultStatusBarColor
            }
        }

        if(activity != null && activity is AppCompatActivity) {
            activity.supportActionBar?.show()
        }
    }
}