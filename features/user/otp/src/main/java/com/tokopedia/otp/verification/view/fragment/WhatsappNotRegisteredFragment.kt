package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.verification.data.OtpConstant
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.viewbinding.WhatsappNotRegisteredViewBinding

open class WhatsappNotRegisteredFragment : BaseOtpToolbarFragment(), IOnBackPressed {

    private var title: String = ""
    private var subtitle: String = ""
    private var imgLink: String = ""

    override val viewBound = WhatsappNotRegisteredViewBinding()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(requireContext())

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onBackPressed(): Boolean {
        goToVerificationMethodPage()
        return false
    }

    private fun initVar() {
        title = arguments?.getString(OtpConstant.OTP_WA_NOT_REGISTERED_TITLE, "")
            ?: getString(R.string.wa_not_registered_title)
        subtitle = arguments?.getString(OtpConstant.OTP_WA_NOT_REGISTERED_SUBTITLE, "")
            ?: getString(R.string.wa_not_registered_subtitle)
        imgLink = arguments?.getString(OtpConstant.OTP_WA_NOT_REGISTERED_IMG_LINK, "") ?: ""
    }

    private fun initView() {
        if (title.isNotEmpty()) viewBound.emptyState?.setTitle(title)
        if (subtitle.isNotEmpty()) viewBound.emptyState?.setDescription(subtitle)
        if (imgLink.isNotEmpty()) viewBound.emptyState?.setImageUrl(imgLink)
        viewBound.emptyState?.setPrimaryCTAText(getString(R.string.wa_not_registered_button_text))
        viewBound.emptyState?.setPrimaryCTAClickListener {
            goToVerificationMethodPage()
        }
    }

    private fun goToVerificationMethodPage() {
        (activity as VerificationActivity).goToVerificationMethodPage()
    }

    companion object {
        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = WhatsappNotRegisteredFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}
