package com.tokopedia.kyc_centralized.ui.customview

import android.app.Activity
import android.os.Build
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kyc_centralized.common.KycUrl
import com.tokopedia.kyc_centralized.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

object KycOnBoardingViewInflater {

    const val URL_TNC = "https://www.tokopedia.com/help/article/syarat-dan-ketentuan-verifikasi-pengguna"

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

    fun setupKycBenefitView(activity: FragmentActivity?, view: View, mainAction: () -> Unit, closeButtonAction: () -> Unit, onCheckedChanged: (Boolean) -> Unit, onTncClicked: () -> Unit) {
        val kycBenefitImage = view.findViewById<ImageUnify>(R.id.image_banner)
        kycBenefitImage?.cornerRadius = 0
        kycBenefitImage.loadImage(KycUrl.KYC_BENEFIT_BANNER)

        with(view) {
            findViewById<KycBenefitItemView>(R.id.benefit_complete_shopping_feature).apply {
                iconUrl = KycUrl.KYC_BENEFIT_CART
            }
            findViewById<KycBenefitItemView>(R.id.benefit_exclusive_sales_feature).apply {
                iconUrl = KycUrl.KYC_BENEFIT_POWER_MERCHANT
            }
            findViewById<KycBenefitItemView>(R.id.benefit_account_more_safer).apply {
                iconUrl = KycUrl.KYC_BENEFIT_SHIELD
            }
            findViewById<Typography>(R.id.see_more_benefit_button).setOnClickListener {
                activity?.supportFragmentManager?.let { fragmentManager ->
                    KycBenefitDetailBottomSheet.createInstance()
                        .show(fragmentManager, KycBenefitDetailBottomSheet.TAG)
                }
            }
        }

        val benefitButton: UnifyButton? = view.findViewById(R.id.kyc_benefit_btn)
        benefitButton?.isEnabled = false
        benefitButton?.setOnClickListener {
            mainAction()
        }

        val kycBenefitCloseButton: UnifyImageButton? = view.findViewById(R.id.close_button)
        kycBenefitCloseButton?.setOnClickListener {
            closeButtonAction()
        }
        val chkBox: CheckboxUnify? = view.findViewById(R.id.kyc_benefit_checkbox)

        if(activity != null){
            val spannableString = setupTncText(activity, onTncClicked)
            chkBox?.movementMethod = LinkMovementMethod.getInstance()
            chkBox?.setText(spannableString, TextView.BufferType.SPANNABLE)
            chkBox?.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChanged(isChecked)
                benefitButton?.isEnabled = isChecked
            }
        }
    }


    fun setupTncText(activity: FragmentActivity, onTncClicked: () -> Unit): SpannableString {
        val sourceString = activity.getString(R.string.kyc_consent_text)
        val spannable = SpannableString(sourceString)
        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                onTncClicked()
                RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=$URL_TNC")
            }
            override fun updateDrawState(ds: TextPaint) {
                ds.color = MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_G400)
            }
        }, sourceString.indexOf("Syarat & Ketentuan."), sourceString.length, 0)
        return spannable
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
