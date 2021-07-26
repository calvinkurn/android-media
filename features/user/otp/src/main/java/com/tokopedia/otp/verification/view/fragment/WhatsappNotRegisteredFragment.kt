package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.viewbinding.WhatsappNotRegisteredViewBinding

open class WhatsappNotRegisteredFragment : BaseOtpToolbarFragment(), IOnBackPressed {

    private var title: String = ""
    private var subtitle: String = ""
    private var imgLink: String = ""

    override val viewBound = WhatsappNotRegisteredViewBinding()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(context)

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
        title = arguments?.getString(OtpConstant.OTP_WA_NOT_REGISTERED_TITLE, "") ?: ""
        subtitle = arguments?.getString(OtpConstant.OTP_WA_NOT_REGISTERED_SUBTITLE, "") ?: ""
        imgLink = arguments?.getString(OtpConstant.OTP_WA_NOT_REGISTERED_IMG_LINK, "") ?: ""
    }

    private fun initView() {
        if (title.isNotEmpty()) viewBound.title?.text = title
        if (subtitle.isNotEmpty()) viewBound.subtitle?.text = subtitle
        if (imgLink.isNotEmpty()) viewBound.mainImage?.setImageUrl(imgLink)
        viewBound.btnMain?.setOnClickListener {
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