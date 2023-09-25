package com.scp.auth.verification

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.gojek.pin.Cancelled
import com.gojek.pin.CtaClicked
import com.gojek.pin.Failed
import com.gojek.pin.PinManager
import com.gojek.pin.PinParam
import com.gojek.pin.Success
import com.gojek.pin.viewmodel.state.PinFlowType
import com.scp.auth.TkpdAdditionalHeaders
import com.scp.verification.core.data.network.entities.CVError
import com.scp.verification.core.domain.common.entities.VerificationData
import com.scp.verification.core.domain.common.entities.config.VerificationUiConfig
import com.scp.verification.core.domain.common.listener.OnSuccessValidation
import com.scp.verification.core.domain.common.listener.PinListener
import com.scp.verification.core.domain.common.listener.VerificationListener
import com.tokopedia.keys.R as keysR

class ScpVerificationManager(private val pinManager: PinManager) {

    val cvSdkInstance = VerificationSdk.CVSDKINSTANCE

    fun startVerification(
        activity: AppCompatActivity,
        source: String,
        verificationData: VerificationData,
        onSuccess: (token: String) -> Unit,
        onFailed: (CVError) -> Unit
    ) {
        cvSdkInstance?.initiateVerification(
            activity = activity,
            verificationData = verificationData,
            verificationUIConfig = VerificationUiConfig(
                isHelpCenterVisible = true,
                shouldAutoCloseOnCompletion = true
            ),
            verificationListener = object : VerificationListener {
                override fun verificationFailed(error: CVError) {
                    onFailed.invoke(error)
                }

                override fun verificationSuccessful(activity: Activity, token: String, transactionId: String, isFromGoToPin: Boolean) {
                    onSuccess.invoke(token)
                }
            },
            pinListener = object : PinListener {
                override fun openPinFlow(challengeId: String, onSuccessValidation: OnSuccessValidation, failure: (CVError) -> Unit, onCtaClicked: (activity: FragmentActivity, type: String, value: String, ctaClickHandledByClient: (deeplink: String) -> Unit) -> Unit, mapEvent: (String, Map<String, Any?>) -> Unit) {
                    pinManager.manage(
                        launchContext = activity,
                        param = PinParam(
                            source = source,
                            clientId = activity.getString(keysR.string.cvsdk_client_id),
                            pinFlowType = PinFlowType.VERIFY,
                            challengeId = challengeId
                        ),
                        clientAnalyticsDelegate = {
                            mapEvent(it.eventName, it.properties)
                        },
                        resultHandler = { result ->
                            when (result) {
                                is Success -> {
                                    onSuccessValidation(
                                        activity,
                                        result.token,
                                        result.onClose
                                    )
                                }

                                is Failed -> {
                                    failure(
                                        CVError.ApiErrorType.GOTO_PIN_API_ERROR(
                                            message = result.errorReason.toString()
                                        )
                                    )
                                }

                                is Cancelled -> {
                                    failure(CVError.UserCancelled)
                                }

                                is CtaClicked -> {
                                    onCtaClicked(result.pinContext, result.type, result.value) {
                                        // TODO: handle Help Centre Click
                                    }
                                }
                            }
                        }
                    )
                }
            },
            additionalHeaders = TkpdAdditionalHeaders(activity)

        )
    }
}
