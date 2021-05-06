package com.tokopedia.otp.qrcode.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.Screen.SCREEN_LOGIN_BY_QR_EXPIRED_PAGE
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.Screen.SCREEN_LOGIN_BY_QR_SUCCESS_PAGE
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.qrcode.view.viewbinding.LoginByQrResultViewBinding
import javax.inject.Inject

class LoginByQrResultFragment : BaseOtpToolbarFragment(), IOnBackPressed {

    @Inject
    lateinit var analytics: TrackingOtpUtil

    private var imglink: String = ""
    private var messageTitle: String = ""
    private var messageBody: String = ""
    private var ctaType: String = ""
    private var status: String = ""

    override val viewBound = LoginByQrResultViewBinding()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(context)

    override fun getScreenName(): String = if (status == SUCCESS_STATUS) SCREEN_LOGIN_BY_QR_SUCCESS_PAGE else SCREEN_LOGIN_BY_QR_EXPIRED_PAGE

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(screenName)
        if (status == SUCCESS_STATUS) {
            analytics.trackViewApprovalApprovedPage()
        } else {
            analytics.trackViewApprovalExpiredPage()
        }
    }

    override fun onBackPressed(): Boolean {
        if (status == SUCCESS_STATUS) {
            analytics.trackClickCloseApprovalApprovedPage()
        } else {
            analytics.trackClickCloseApprovalExpiredPage()
        }
        return true
    }

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
        viewBound.mainImage?.setImageUrl(imglink)
        viewBound.btnMain?.text = ctaType
        viewBound.btnMain?.setOnClickListener {
            if (status == SUCCESS_STATUS) {
                analytics.trackClickTutupApprovalApprovedPage()
            } else {
                analytics.trackClickScanApprovalExpiredPage()
            }
            activity?.finish()
        }
    }

    companion object {

        private const val SUCCESS_STATUS = "success2"

        fun createInstance(bundle: Bundle): LoginByQrResultFragment {
            val fragment = LoginByQrResultFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}