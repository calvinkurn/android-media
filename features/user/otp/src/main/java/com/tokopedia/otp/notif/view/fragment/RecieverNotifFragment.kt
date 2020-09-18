package com.tokopedia.otp.notif.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.view.activity.ResultNotifActivity
import com.tokopedia.otp.notif.view.viewbinding.RecieverNotifViewBinding

/**
 * Created by Ade Fulki on 14/09/20.
 */

class RecieverNotifFragment : BaseOtpFragment(), IOnBackPressed {

    private var deviceName: String = ""
    private var location: String = ""
    private var time: String = ""
    private var ip: String = ""
    private var challengeCode: String = ""

    override var viewBound = RecieverNotifViewBinding()

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
        arguments?.let {
            deviceName = it.getString(KEY_PARAM_DEVICE_NAME, "")
            location = it.getString(KEY_PARAM_LOCATION, "")
            time = it.getString(KEY_PARAM_TIME, "")
            ip = it.getString(KEY_PARAM_IP, "")
            challengeCode = it.getString(KEY_PARAM_CHALLANGE_CODE, "")
        }
    }

    private fun initView() {
        viewBound.textDevice?.text = deviceName
        viewBound.textTime?.text = time
        viewBound.textLocation?.text = location
        viewBound.btnYes?.setOnClickListener {
            val intent = Intent(context, ResultNotifActivity::class.java)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_RESULT_STATUS, ResultNotifFragment.RESULT_STATUS_APPROVED)
            startActivity(intent)
            activity?.finish()
        }
        viewBound.btnNo?.setOnClickListener {
            val intent = Intent(context, ResultNotifActivity::class.java)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_RESULT_STATUS, ResultNotifFragment.RESULT_STATUS_REJECTED)
            startActivity(intent)
            activity?.finish()
        }
    }

    companion object {

        private const val KEY_PARAM_DEVICE_NAME = "device_name"
        private const val KEY_PARAM_LOCATION = "location"
        private const val KEY_PARAM_TIME = "time"
        private const val KEY_PARAM_IP = "ip"
        private const val KEY_PARAM_CHALLANGE_CODE = "challenge_code"

        fun createInstance(bundle: Bundle): RecieverNotifFragment {
            val fragment = RecieverNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}