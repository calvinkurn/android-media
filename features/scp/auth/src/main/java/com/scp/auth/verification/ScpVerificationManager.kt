package com.scp.auth.verification

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.gojek.pin.Cancelled
import com.gojek.pin.CtaClicked
import com.gojek.pin.Failed
import com.gojek.pin.PinParam
import com.gojek.pin.Success
import com.gojek.pin.viewmodel.state.PinFlowType
import com.scp.auth.GotoSdk
import com.scp.auth.common.utils.ScpConstants
import com.scp.auth.common.utils.TkpdAdditionalHeaders
import com.scp.auth.common.utils.goToChangePIN
import com.scp.auth.common.utils.goToForgotGotoPin
import com.scp.auth.common.utils.goToForgotPassword
import com.scp.auth.common.utils.goToHelpGotoPIN
import com.scp.verification.core.data.network.entities.CVError
import com.scp.verification.core.domain.common.entities.VerificationData
import com.scp.verification.core.domain.common.entities.config.VerificationUiConfig
import com.scp.verification.core.domain.common.listener.ForgetContext
import com.scp.verification.core.domain.common.listener.ForgetListener
import com.scp.verification.core.domain.common.listener.OnSuccessValidation
import com.scp.verification.core.domain.common.listener.PinListener
import com.scp.verification.core.domain.common.listener.VerificationListener
import com.scp.verification.features.gotopin.CVPinManager

object ScpVerificationManager {

    private val pinManager = GotoSdk.getGotopinInstance()

    fun startVerification(
        activity: AppCompatActivity,
        source: String,
        verificationData: VerificationData,
        onSuccess: (token: String) -> Unit,
        onFailed: (CVError) -> Unit
    ) {
        val cvSdkInstance = VerificationSdk.getInstance(activity.application)
        cvSdkInstance?.initiateVerification(
            activity = activity,
            verificationData = verificationData,
            verificationUIConfig = VerificationUiConfig(
                isHelpCenterVisible = true,
                shouldAutoCloseOnCompletion = true
            ),
            verificationListener = object : VerificationListener {
                override fun verificationFailed(error: CVError) {
                    cvSdkInstance.closeScreenAndExit()
                    onFailed.invoke(error)
                }

                override fun verificationSuccessful(
                    activity: Activity,
                    token: String,
                    transactionId: String,
                    isFromGoToPin: Boolean,
                    onClose: () -> Unit
                ) {
                    onSuccess.invoke(token)
                }
            },
            pinListener = object : PinListener {
                override fun openPinFlow(challengeId: String, onSuccessValidation: OnSuccessValidation, failure: (CVError) -> Unit, onCtaClicked: (activity: FragmentActivity, type: String, value: String, ctaClickHandledByClient: (deeplink: String) -> Unit) -> Unit, mapEvent: (String, Map<String, Any?>) -> Unit) {
                    pinManager?.manage(
                        launchContext = activity,
                        param = PinParam(
                            source = source,
                            clientId = ScpConstants.TOKOPEDIA_CLIENT_ID,
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
                                        when (result.type) {
                                            CVPinManager.CTA_TYPE_HELP -> {
                                                goToHelpGotoPIN(result.pinContext)
                                            }
                                            CVPinManager.FORGOT_PIN_HELP_ARTICLE -> {
                                                goToForgotGotoPin(result.pinContext)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            },
            additionalHeaders = TkpdAdditionalHeaders(activity),
            forgetListener = getForgetListener()
        )
    }

    private fun getForgetListener() = object : ForgetListener {
        override fun openForgetFlow(type: ForgetContext, activity: FragmentActivity) {
            when (type) {
                ForgetContext.FORGET_PASSWORD -> {
                    goToForgotPassword(activity)
                }
                ForgetContext.FORGET_TOKO_PIN -> {
                    goToChangePIN(activity)
                }
            }
        }
    }
}
