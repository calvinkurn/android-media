package com.scp.auth.verification

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.scp.auth.common.utils.ScpConstants
import com.scp.auth.di.DaggerScpAuthComponent
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
        Log.d("ScpVerificationActivity", "scp verif activity")
        super.onCreate(savedInstanceState)
        val binding = ActivityScpAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        inject()
        getVerifParam()
        startVerification()
        return super.onCreateView(name, context, attrs)
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
            phone = phoneNumber).apply {
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

                },
                onFailed = {

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
            otpType -> VerificationFlowType.REGISTER_EMAIL
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
    REGISTER_EMAIL("register_email")
}
