package com.tokopedia.otp.notif.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.view.viewbinding.ResultNotifViewBinding
import javax.inject.Inject

/**
 * Created by Ade Fulki on 14/09/20.
 */

class ResultNotifFragment : BaseOtpToolbarFragment(), IOnBackPressed {

    @Inject
    lateinit var analytics: TrackingOtpUtil

    private var imglink: String = ""
    private var messageTitle: String = ""
    private var messageBody: String = ""
    private var ctaType: String = ""
    private var status: String = ""

    override val viewBound = ResultNotifViewBinding()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(context)

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
    }

    override fun onStart() {
        super.onStart()
        trackImpression()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onBackPressed(): Boolean {
        trackClose()
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
        viewBound.mainImage?.setImageUrl(imglink)
        viewBound.title?.text = messageTitle
        viewBound.subtitle?.text = messageBody
        when (ctaType) {
            CTA_TYPE_CLOSE -> {
                viewBound.btnMain?.text = getText(R.string.close_result_push_notif)
                viewBound.btnMain?.setOnClickListener {
                    trackButton()
                    closeResult()
                }
            }
            CTA_TYPE_CHANGE_PIN -> {
                viewBound.btnMain?.text = getText(R.string.change_pin_result_push_notif)
                viewBound.btnMain?.setOnClickListener {
                    trackButton()
                    goToChangePin()
                }
            }
            CTA_TYPE_CHANGE_PASSWORD -> {
                viewBound.btnMain?.text = getText(R.string.change_password_result_push_notif)
                viewBound.btnMain?.setOnClickListener {
                    trackButton()
                    goToChangePassword()
                }
            }
        }
    }

    private fun closeResult() {
        activity?.finish()
    }

    private fun goToChangePin() {
        context?.let {
            RouteManager.route(it, ApplinkConstInternalGlobal.CHANGE_PIN)
            closeResult()
        }
    }

    private fun goToChangePassword() {
        context?.let {
            RouteManager.route(it, ApplinkConstInternalGlobal.HAS_PASSWORD)
            closeResult()
        }
    }

    private fun trackImpression() {
        when (status){
            STATUS_APPROVED -> {
                analytics.trackViewOtpPushNotifSuccessPage()
            }
            STATUS_REJECT_EMAIL, STATUS_REJECT_PHONE -> {
                when (ctaType) {
                    CTA_TYPE_CLOSE -> {
                        analytics.trackViewOtpPushNotifFailedNoPinPage()
                    }
                    CTA_TYPE_CHANGE_PIN -> {
                        analytics.trackViewOtpPushNotifFailedWithPinPage()
                    }
                    CTA_TYPE_CHANGE_PASSWORD -> {
                        analytics.trackViewOtpPushNotifFailedWithPasswordPage()
                    }
                }
            }
            STATUS_EXPIRED -> {
                analytics.trackViewOtpPushNotifFailedOtpExpiredPage()
            }
        }
    }

    private fun trackClose() {
        when (status) {
            STATUS_APPROVED -> {
                analytics.trackClickBackReceiveSuccess()
            }
            STATUS_REJECT_EMAIL, STATUS_REJECT_PHONE -> {
                when (ctaType) {
                    CTA_TYPE_CLOSE -> {
                        analytics.trackClickBackReceiveFailedNoPinButton()
                    }
                    CTA_TYPE_CHANGE_PIN -> {
                        analytics.trackClickCloseReceiveFailedWithPinButton()
                    }
                    CTA_TYPE_CHANGE_PASSWORD -> {
                        analytics.trackClickCloseReceiveFailedWithPasswordButton()
                    }
                }
            }
            STATUS_EXPIRED -> {
                analytics.trackClickCloseReceiveFailedOtpExpiredButton()
            }
        }
    }

    private fun trackButton() {
        when {
            status == STATUS_APPROVED && ctaType == CTA_TYPE_CLOSE -> {
                analytics.trackClickCloseReceiveSuccess()
            }
            (status == STATUS_REJECT_EMAIL || status == STATUS_REJECT_PHONE) && ctaType == CTA_TYPE_CLOSE -> {
                analytics.trackClickCloseReceiveFailedNoPinButton()
            }
            (status == STATUS_REJECT_EMAIL || status == STATUS_REJECT_PHONE) && ctaType == CTA_TYPE_CHANGE_PIN -> {
                analytics.trackClickChangePinReceiveFailedWithPinButton()
            }
            (status == STATUS_REJECT_EMAIL || status == STATUS_REJECT_PHONE) && ctaType == CTA_TYPE_CHANGE_PASSWORD -> {
                analytics.trackClickChangePasswordReceiveFailedWithPasswordButton()
            }
            status == STATUS_EXPIRED && ctaType == CTA_TYPE_CLOSE -> {
                analytics.trackClickBottomCloseReceiveFailedOtpExpiredButton()
            }
        }
    }

    companion object {

        private const val CTA_TYPE_CLOSE = "Close"
        private const val CTA_TYPE_CHANGE_PIN = "ChangePin"
        private const val CTA_TYPE_CHANGE_PASSWORD = "ChangePassword"
        private const val STATUS_APPROVED = "appoved"
        private const val STATUS_REJECT_EMAIL = "reject-email"
        private const val STATUS_REJECT_PHONE = "reject-phone"
        private const val STATUS_EXPIRED = "expire-timeout"

        fun createInstance(bundle: Bundle): ResultNotifFragment {
            val fragment = ResultNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}