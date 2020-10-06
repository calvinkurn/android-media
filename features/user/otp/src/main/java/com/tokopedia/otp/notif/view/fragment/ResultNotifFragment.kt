package com.tokopedia.otp.notif.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.view.viewbinding.ResultNotifViewBinding

/**
 * Created by Ade Fulki on 14/09/20.
 */

class ResultNotifFragment : BaseOtpFragment(), IOnBackPressed {

    private var imglink: String = ""
    private var messageTitle: String = ""
    private var messageBody: String = ""
    private var ctaType: String = ""

    override val viewBound = ResultNotifViewBinding()

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
        }
    }

    private fun initView() {
        viewBound.mainImage?.setImageUrl(imglink)
        viewBound.title?.text = messageTitle
        viewBound.subtitle?.text = messageBody
        when (ctaType) {
            CTA_TYPE_CLOSE -> {
                viewBound.btnMain?.text = getText(R.string.close_result_push_notif)
                viewBound.btnMain?.setOnClickListener {
                    closeResult()
                }
            }
            CTA_TYPE_CHANGE_PIN -> {
                viewBound.btnMain?.text = getText(R.string.change_pin_result_push_notif)
                viewBound.btnMain?.setOnClickListener {
                    goToChangePin()
                }
            }
            CTA_TYPE_CHANGE_PASSWORD -> {
                viewBound.btnMain?.text = getText(R.string.change_password_result_push_notif)
                viewBound.btnMain?.setOnClickListener {
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

    companion object {

        private const val CTA_TYPE_CLOSE = "Close"
        private const val CTA_TYPE_CHANGE_PIN = "ChangePin"
        private const val CTA_TYPE_CHANGE_PASSWORD = "ChangePassword"

        fun createInstance(bundle: Bundle): ResultNotifFragment {
            val fragment = ResultNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}