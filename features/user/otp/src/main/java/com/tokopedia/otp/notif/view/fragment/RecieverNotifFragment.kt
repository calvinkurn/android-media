package com.tokopedia.otp.notif.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.common.SignaturePref
import com.tokopedia.otp.notif.domain.pojo.VerifyPushNotifData
import com.tokopedia.otp.notif.view.activity.ResultNotifActivity
import com.tokopedia.otp.notif.view.viewbinding.RecieverNotifViewBinding
import com.tokopedia.otp.notif.viewmodel.NotifViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Ade Fulki on 14/09/20.
 */

class RecieverNotifFragment : BaseDaggerFragment(), IOnBackPressed {

    @Inject
    lateinit var viewBound: RecieverNotifViewBinding

    @Inject
    lateinit var signaturePref: SignaturePref

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var deviceName: String = ""
    private var location: String = ""
    private var time: String = ""
    private var ip: String = ""
    private var challengeCode: String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NotifViewModel::class.java)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
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

    private fun initObserver() {
        viewModel.verifyPushNotifResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessVerifyPushNotif().invoke(it.data)
                is Fail -> onFailedVerifyPushNotif().invoke(it.throwable)
            }
        })
    }

    private fun onSuccessVerifyPushNotif(): (VerifyPushNotifData) -> Unit {
        return { verifyPushNotifData ->
            if (verifyPushNotifData.ctaType.isNotEmpty()) {
                goToResultNotif(
                        verifyPushNotifData.imglink,
                        verifyPushNotifData.messageTitle,
                        verifyPushNotifData.messageBody,
                        verifyPushNotifData.ctaType
                )
            } else {
                onFailedVerifyPushNotif().invoke(Throwable())
            }
        }
    }

    private fun onFailedVerifyPushNotif(): (Throwable) -> Unit {
        return { throwable ->
            throwable.printStackTrace()
        }
    }

    private fun goToResultNotif(imglink: String, messageTitle: String, messageBody: String, ctaType: String) {
        val intent = Intent(context, ResultNotifActivity::class.java)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IMG_LINK, imglink)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MESSAGE_TITLE, messageTitle)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY, messageBody)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CTA_TYPE, ctaType)
        startActivity(intent)
        activity?.finish()
    }

    private fun initView() {
        viewBound.textDevice?.text = deviceName
        viewBound.textTime?.text = time
        viewBound.textLocation?.text = location
        viewBound.btnYes?.setOnClickListener {
            viewModel.verifyPushNotif(challengeCode, signaturePref.signature ?: "", STATUS_APPROVE)
        }
        viewBound.btnNo?.setOnClickListener {
            viewModel.verifyPushNotif(challengeCode, signaturePref.signature ?: "", STATUS_REJECT)
        }
    }

    companion object {

        const val STATUS_APPROVE = "approve"
        const val STATUS_REJECT = "reject"

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