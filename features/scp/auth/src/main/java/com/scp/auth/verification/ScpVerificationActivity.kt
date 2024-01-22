package com.scp.auth.verification

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.scp.auth.common.utils.ScpConstants
import com.scp.auth.di.DaggerScpAuthComponent
import com.scp.verification.core.data.network.entities.CVError
import com.scp.verification.core.domain.common.entities.VerificationCredential
import com.scp.verification.core.domain.common.entities.VerificationData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.scp.auth.databinding.ActivityScpAuthBinding
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ScpVerificationActivity : BaseActivity() {

    private var cvSdkParam: CvSdkParam? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityScpAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inject()
        getVerifParam()
        startVerification()
    }

    private fun inject() {
        val appComponent = (application as BaseMainApplication)
            .baseAppComponent
        DaggerScpAuthComponent.builder()
            .baseAppComponent(appComponent)
            .build()
            .inject(this)
    }

    private fun getVerifParam(): CvSdkParam {
        val otpType = intent?.extras?.getInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, 0) ?: 0
        val email = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_EMAIL, "").toEmptyStringIfNull()
        val phoneNumber = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "").toEmptyStringIfNull()
        return CvSdkParam(
            otpType = otpType,
            email = email,
            phone = phoneNumber
        ).apply {
            cvSdkParam = this
        }
    }

    private fun startVerification() {
        cvSdkParam?.let {
            val flow = flowFactory(it.otpType)
            ScpVerificationManager.startVerification(
                this,
                flow.value,
                createVerificationData(flow, it),
                onSuccess = {
                    val bundle = Bundle().apply {
                        putString(ApplinkConstInternalGlobal.PARAM_UUID, it)
                        putString(ApplinkConstInternalGlobal.PARAM_TOKEN, it)
                        putString(ApplinkConstInternalGlobal.PARAM_MSISDN, cvSdkParam?.phone.toEmptyStringIfNull())
                        putString(ApplinkConstInternalGlobal.PARAM_EMAIL, cvSdkParam?.email.toEmptyStringIfNull())
                        putString(
                            ApplinkConstInternalGlobal.PARAM_SOURCE,
                            getString(ApplinkConstInternalGlobal.PARAM_SOURCE, "")
                        )
                        putBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_SCP, true)
                    }
                    setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
                    finish()
                },
                onFailed = { error ->
                    if ((error is CVError.UserCancelled).not()) {
                        Toast.makeText(this, "Terjadi Kesalahan ${error.message} ${error.errorCode}", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        finish()
                    }
                }
            )
        }
    }

    private fun createVerificationData(flowType: VerificationFlowType, param: CvSdkParam): VerificationData {
        return VerificationData(
            flow = flowType.value,
            credential = getCredential(param)
        )
    }

    private fun getCredential(credential: CvSdkParam): VerificationCredential {
        return if (userSession.accessToken.isNotEmpty()) {
            VerificationCredential.Token(userSession.accessToken)
        } else if (credential.phone.isNotEmpty()) {
            VerificationCredential.PhoneNumber(
                countryCode = "",
                number = credential.phone
            )
        } else {
            VerificationCredential.Email(credential.email)
        }
    }

    private fun flowFactory(otpType: Int): VerificationFlowType {
        return when (otpType) {
            ScpConstants.OtpType.REGISTER_EMAIL -> VerificationFlowType.REGISTER_EMAIL
            ScpConstants.OtpType.REGISTER_PHONE_NUMBER -> VerificationFlowType.REGISTER_PHONE
            ScpConstants.OtpType.SQCP -> VerificationFlowType.SQCP
            else -> VerificationFlowType.REGISTER_EMAIL
        }
    }
}

data class CvSdkParam(
    val otpType: Int,
    val email: String,
    val phone: String
)

enum class VerificationFlowType(val value: String) {
    REGISTER_EMAIL("register_email"),
    REGISTER_PHONE("register_phone"),
    RESET_PASSWORD("reset_password"),
    RESET_TOKOPEDIA_PIN("reset_tokopedia_pin"),
    SQCP("sqcp")
}
