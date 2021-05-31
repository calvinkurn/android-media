package com.tokopedia.otp.notif.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.LoadingDialog
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.analytics.TrackingOtpConstant
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.data.SignResult
import com.tokopedia.otp.notif.domain.pojo.VerifyPushNotifData
import com.tokopedia.otp.notif.domain.pojo.VerifyPushNotifExpData
import com.tokopedia.otp.notif.view.activity.ResultNotifActivity
import com.tokopedia.otp.notif.view.viewbinding.ReceiverNotifViewBinding
import com.tokopedia.otp.notif.viewmodel.NotifViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import javax.inject.Inject

/**
 * Created by Ade Fulki on 14/09/20.
 */

class ReceiverNotifFragment : BaseOtpToolbarFragment(), IOnBackPressed {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var loadingDialog: LoadingDialog
    @Inject
    lateinit var analytics: TrackingOtpUtil
    @Inject
    lateinit var userSession: UserSessionInterface

    private var deviceName: String = ""
    private var location: String = ""
    private var time: String = ""
    private var ip: String = ""
    private var challengeCode: String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NotifViewModel::class.java)
    }

    override val viewBound = ReceiverNotifViewBinding()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(context)

    override fun getScreenName(): String = TrackingOtpConstant.Screen.SCREEN_PUSH_NOTIF_RECEIVE

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
    }

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(screenName)
        analytics.trackViewOtpPushNotifReceivePage()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
        verifyPushNotifExp()
    }

    override fun onBackPressed(): Boolean {
        analytics.trackClickBackReceiver()
        return true
    }

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
        viewModel.verifyPushNotifExpResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessVerifyPushNotifExp().invoke(it.data)
                is Fail -> onFailedVerifyPushNotifExp().invoke(it.throwable)
            }
        })
    }

    private fun onSuccessVerifyPushNotif(): (VerifyPushNotifData) -> Unit {
        return { verifyPushNotifData ->
            dismissLoading()
            goToResultNotif(
                    verifyPushNotifData.imglink,
                    verifyPushNotifData.messageTitle,
                    verifyPushNotifData.messageBody,
                    verifyPushNotifData.ctaType,
                    verifyPushNotifData.status
            )
        }
    }

    private fun onFailedVerifyPushNotif(): (Throwable) -> Unit {
        return { throwable ->
            dismissLoading()
            throwable.printStackTrace()
            LetUtil.ifLet(throwable.message, viewBound.containerView) { (message, containerView) ->
                Toaster.make(containerView as View, message as String, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun onSuccessVerifyPushNotifExp(): (VerifyPushNotifExpData) -> Unit {
        return { verifyPushNotifExpData ->
            dismissLoading()
            if(verifyPushNotifExpData.imglink.isNotEmpty() &&
                    verifyPushNotifExpData.messageTitle.isNotEmpty() &&
                    verifyPushNotifExpData.messageBody.isNotEmpty() &&
                    verifyPushNotifExpData.ctaType.isNotEmpty()) {
                goToResultNotif(
                        verifyPushNotifExpData.imglink,
                        verifyPushNotifExpData.messageTitle,
                        verifyPushNotifExpData.messageBody,
                        verifyPushNotifExpData.ctaType,
                        verifyPushNotifExpData.status
                )
            }
        }
    }

    private fun onFailedVerifyPushNotifExp(): (Throwable) -> Unit {
        return { throwable ->
            dismissLoading()
            throwable.printStackTrace()
            LetUtil.ifLet(throwable.message, viewBound.containerView) { (message, containerView) ->
                Toaster.make(containerView as View, message as String, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun verifyPushNotifExp() {
        showLoading()
        var signResult = SignResult()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            signResult = signDataVerifyPushNotif(challengeCode, STATUS_CHECK)
        }
        viewModel.verifyPushNotifExp(challengeCode, signResult.signature, STATUS_CHECK)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun signDataVerifyPushNotif(challangeCode: String, status: String): SignResult {
        val datetime = (System.currentTimeMillis() / 1000).toString()
        val data = challangeCode + status
        return signData(data, datetime)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun signData(data: String, datetime: String): SignResult {
        val signResult = SignResult()
        try {
            signResult.datetime = datetime

            val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
                load(null)
            }

            val privateKey: PrivateKey = keyStore.getKey(PUSH_NOTIF_ALIAS, null) as PrivateKey

            val publicKey: PublicKey = keyStore.getCertificate(PUSH_NOTIF_ALIAS).publicKey
            signResult.publicKey = publicKeyToString(publicKey.encoded)

            val signature: ByteArray? = Signature.getInstance(SHA_256_WITH_RSA).run {
                initSign(privateKey)
                update(data.toByteArray())
                sign()
            }

            if (signature != null) {
                signResult.signature = Base64.encodeToString(signature, Base64.DEFAULT)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return signResult
    }

    private fun publicKeyToString(input: ByteArray): String {
        val encoded = Base64.encodeToString(input, Base64.NO_WRAP)
        return "$PUBLIC_KEY_PREFIX$encoded$PUBLIC_KEY_SUFFIX"
    }

    private fun goToResultNotif(imglink: String, messageTitle: String, messageBody: String, ctaType: String, status: String) {
        val intent = Intent(context, ResultNotifActivity::class.java)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IMG_LINK, imglink)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MESSAGE_TITLE, messageTitle)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY, messageBody)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CTA_TYPE, ctaType)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_STATUS, status)
        startActivity(intent)
        activity?.finish()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_cancel_grey_otp)
        viewBound.imagePhoneBell?.setImage(R.drawable.ic_phone_bell, 0f)
        viewBound.imagePhone?.setImage(R.drawable.ic_phone_otp, 0f)
        viewBound.imageTime?.setImage(R.drawable.ic_time_otp, 0f)
        viewBound.imageLocation?.setImage(R.drawable.ic_location_otp, 0f)
        viewBound.textDevice?.text = deviceName
        viewBound.textTime?.text = time
        viewBound.textLocation?.text = "$location • $ip"
        viewBound.btnYes?.setOnClickListener {
            analytics.trackClickAcceptAccesssButton()
            showLoading()
            var signResult = SignResult()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                signResult = signDataVerifyPushNotif(challengeCode, STATUS_APPROVE)
            }
            viewModel.verifyPushNotif(challengeCode, signResult.signature, STATUS_APPROVE)
        }
        viewBound.btnNo?.setOnClickListener {
            analytics.trackClickRejectAccesssButton()
            showLoading()
            var signResult = SignResult()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                signResult = signDataVerifyPushNotif(challengeCode, STATUS_REJECT)
            }
            viewModel.verifyPushNotif(challengeCode, signResult.signature, STATUS_REJECT)
        }
    }

    private fun showLoading() {
        if (activity != null) {
            loadingDialog.show()
        }
    }

    private fun dismissLoading() {
        if (activity != null) {
            loadingDialog.dismiss()
        }
    }

    companion object {

        const val STATUS_APPROVE = "approve"
        const val STATUS_REJECT = "reject"
        const val STATUS_CHECK = "check"

        private const val KEY_PARAM_DEVICE_NAME = "device_name"
        private const val KEY_PARAM_LOCATION = "location"
        private const val KEY_PARAM_TIME = "time"
        private const val KEY_PARAM_IP = "ip"
        private const val KEY_PARAM_CHALLANGE_CODE = "challenge_code"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val PUSH_NOTIF_ALIAS = "PushNotif"
        private const val SHA_256_WITH_RSA = "SHA256withRSA"
        private const val PUBLIC_KEY_PREFIX = "-----BEGIN PUBLIC KEY-----\n"
        private const val PUBLIC_KEY_SUFFIX = "\n-----END PUBLIC KEY-----"

        fun createInstance(bundle: Bundle): ReceiverNotifFragment {
            val fragment = ReceiverNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}