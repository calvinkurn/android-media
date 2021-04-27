package com.tokopedia.otp.qrcode.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.qrcode.view.viewbinding.LoginByQrResultViewBinding
import com.tokopedia.otp.qrcode.viewmodel.LoginByQrViewModel
import javax.inject.Inject

class LoginByQrResultFragment: BaseOtpToolbarFragment(), IOnBackPressed {

    private var imglink: String = ""
    private var messageTitle: String = ""
    private var messageBody: String = ""
    private var ctaType: String = ""
    private var status: String = ""

    override val viewBound = LoginByQrResultViewBinding()

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

    override fun onBackPressed(): Boolean = true

    private fun initVar() {
        arguments?.run {
            imglink = getString(ApplinkConstInternalGlobal.PARAM_IMG_LINK, "")
            messageTitle = getString(ApplinkConstInternalGlobal.PARAM_MESSAGE_TITLE, "")
            messageBody = getString(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY, "")
            ctaType = getString(ApplinkConstInternalGlobal.PARAM_CTA_TYPE, "")
            status = getString(ApplinkConstInternalGlobal.PARAM_STATUS, "")
        }
    }

    private fun initView() {
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_cancel_grey_otp)
        viewBound.title?.text = messageTitle
        viewBound.subtitle?.text = messageBody
        viewBound.btnMain?.text = getText(R.string.close_result_push_notif)
        viewBound.btnMain?.setOnClickListener {
            activity?.finish()
        }
    }

    companion object {

        fun createInstance(bundle: Bundle): LoginByQrResultFragment {
            val fragment = LoginByQrResultFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}