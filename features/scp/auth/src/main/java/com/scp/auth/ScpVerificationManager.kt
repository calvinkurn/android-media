package com.scp.auth

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.scp.verification.core.data.network.entities.CVError
import com.scp.verification.core.data.network.header.AdditionalHeaders
import com.scp.verification.core.data.network.header.AkamaiHeaderData
import com.scp.verification.core.domain.common.entities.VerificationData
import com.scp.verification.core.domain.common.entities.config.VerificationUiConfig
import com.scp.verification.core.domain.common.listener.OnSuccessValidation
import com.scp.verification.core.domain.common.listener.PinListener
import com.scp.verification.core.domain.common.listener.VerificationListener

class ScpVerificationManager {

    val cvSdkInstance = GotoSdk.CVSDKINSTANCE

    fun startVerification(activity: AppCompatActivity, verificationData: VerificationData, onSuccess: (token: String) -> Unit) {
        cvSdkInstance?.initiateVerification(
            activity = activity,
            verificationData = verificationData,
            verificationUIConfig = VerificationUiConfig(
                isHelpCenterVisible = true,
                shouldAutoCloseOnCompletion = true,
            ),
            verificationListener = object : VerificationListener {
                override fun verificationFailed(error: CVError) {
                    TODO("Not yet implemented")
                }

                override fun verificationSuccessful(activity: Activity, token: String, transactionId: String) {
                    onSuccess.invoke(token)
                }
            },
            pinListener = object : PinListener {
                override fun openPinFlow(challengeId: String, onSuccessValidation: OnSuccessValidation, failure: (CVError) -> Unit, onCtaClicked: (activity: FragmentActivity, type: String, value: String, ctaClickHandledByClient: (deeplink: String) -> Unit) -> Unit, mapEvent: (String, Map<String, Any>) -> Unit) {
                    TODO("Not yet implemented")
                }

            },
            additionalHeaders = object : AdditionalHeaders {
                override fun getAdditionalHeaders(): HashMap<String, String>? {
                    TODO("Not yet implemented")
                }

                override fun getBotProtectionHeaders(): AkamaiHeaderData {
                    TODO("Not yet implemented")
                }

            }
        )
    }
}
