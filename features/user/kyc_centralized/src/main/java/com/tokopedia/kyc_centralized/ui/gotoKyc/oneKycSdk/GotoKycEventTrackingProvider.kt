package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.content.Context
import com.gojek.kyc.sdk.config.KycSdkAnalyticsConfig
import com.gojek.kyc.sdk.core.analytics.IKycSdkEventTrackingProvider
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kyc_centralized.ui.gotoKyc.analytics.GotoKycAnalytics
import com.tokopedia.kyc_centralized.ui.gotoKyc.worker.GotoKycCleanupStorageWorker
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

class GotoKycEventTrackingProvider @Inject constructor(
    private val kycSdkAnalyticsConfig: KycSdkAnalyticsConfig,
    private val kycSharedPreference: KycSharedPreference,
    private val remoteConfigImpl: FirebaseRemoteConfigImpl,
    @ApplicationContext private val context: Context
) : IKycSdkEventTrackingProvider {
    override fun getAnalyticsConfigForClickStream(): KycSdkAnalyticsConfig? {
        return if (remoteConfigImpl.getBoolean(RemoteConfigKey.GOTO_ONE_KYC_CLICKSTREAM)) {
            kycSdkAnalyticsConfig
        } else {
            null
        }
    }

    override fun sendPeopleProperty(customerId: String, propertyName: String, propertyValue: Any?): Boolean {
        return true
    }

    override fun sendPeopleProperty(map: MutableMap<String, Any>): Boolean {
        return true
    }

    override fun track(
        eventName: String,
        eventProperties: Map<String, Any?>,
        productName: String?
    ) {
        sendTracker(
            eventName = eventName,
            eventProperties = eventProperties
        )
    }

    private fun sendTracker(eventName: String, eventProperties: Map<String, Any?>) {
        val projectId = kycSharedPreference.getProjectId()
        when (eventName) {
            CAMERA_OPENED -> {
                when (eventProperties[ACTUAL_USAGE]) {
                    DEEPLEARN_AUTO_CAPTURE -> {
                        when (eventProperties[TYPE]) {
                            KTP -> {
                                GotoKycAnalytics.sendViewKtpPage(
                                    isManual = false,
                                    projectId = projectId
                                )
                            }
                            SELFIE -> {
                                when (eventProperties[FLOW_TYPE]) {
                                    FLOW_TYPE_AURORA -> {
                                        GotoKycAnalytics.sendViewSelfiePage(
                                            kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                                            detectionType = VALUE_DETECTION_TYPE_AURORA,
                                            projectId = projectId
                                        )
                                    }
                                    else -> {
                                        GotoKycAnalytics.sendViewSelfiePage(
                                            kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                                            detectionType = VALUE_DETECTION_TYPE_AUTO,
                                            projectId = projectId
                                        )
                                    }
                                }
                            }
                        }
                    }
                    CUSTOM -> {
                        when (eventProperties[TYPE]) {
                            KTP -> {
                                GotoKycAnalytics.sendViewKtpPage(
                                    isManual = true,
                                    projectId = projectId
                                )
                            }
                            SELFIE -> {
                                GotoKycAnalytics.sendViewSelfiePage(
                                    kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                                    detectionType = VALUE_DETECTION_TYPE_MANUAL,
                                    projectId = projectId
                                )
                            }
                        }
                    }
                }
            }
            IMAGE_CAPTURE_MODE_CHANGE_VIEWED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendViewManualFotoKtpQuestion(projectId)
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendViewManualFotoSelfie(projectId)
                    }
                }
            }
            IMAGE_CAPTURE_MODE_AUTO_CAPTURE -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickOnButtonFotoOtomatisKtpPage(projectId)
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickOnButtonFotoOtomatisSelfiePage(projectId)
                    }
                }
            }
            IMAGE_CAPTURE_MODE_CHANGED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickButtonFotoManualKtpPage(projectId)
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickButtonFotoManualSelfiePage(projectId)
                    }
                }
            }
            APP_CLOSE_BUTTON_CLICKED -> {
                when (eventProperties[SCREEN_TYPE]) {
                    SCREEN_TYPE_CAMERA -> {
                        when (eventProperties[TYPE]) {
                            KTP -> {
                                when (eventProperties[CAPTURE_MODE]) {
                                    AUTO_CAPTURE -> {
                                        GotoKycAnalytics.sendClickOnButtonBackFromKtpPage(projectId)
                                    }
                                    MANUAL_CAPTURE -> {
                                        GotoKycAnalytics.sendClickOnButtonBackManualKtp(projectId)
                                    }
                                }
                            }
                            SELFIE -> {
                                when (eventProperties[CAPTURE_MODE]) {
                                    AUTO_CAPTURE -> {
                                        when (eventProperties[FLOW_TYPE]) {
                                            FLOW_TYPE_AURORA -> {
                                                GotoKycAnalytics.sendClickOnButtonBackSelfiePage(
                                                    detectionType = VALUE_DETECTION_TYPE_AURORA,
                                                    projectId = projectId
                                                )
                                            }
                                            else -> {
                                                GotoKycAnalytics.sendClickOnButtonBackSelfiePage(
                                                    detectionType = VALUE_DETECTION_TYPE_AUTO,
                                                    projectId = projectId
                                                )
                                            }
                                        }
                                    }
                                    MANUAL_CAPTURE -> {
                                        GotoKycAnalytics.sendClickOnButtonBackManualSelfie(projectId)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            CLOSE_BUTTON_CLICKED -> {
                when (eventProperties[SCREEN_TYPE]) {
                    SCREEN_TYPE_ALL_PREVIEW -> {
                        GotoKycAnalytics.sendClickOnButtonBackFromReviewPage(projectId)
                    }
                    SCREEN_TYPE_DOCUMENT_INFO -> {
                        when (eventProperties[TYPE]) {
                            KTP -> {
                                GotoKycAnalytics.sendClickOnButtonBackFromReviewKtpPage(projectId)
                            }
                            SELFIE -> {
                                GotoKycAnalytics.sendClickOnButtonBackFromReviewSelfiePage(projectId)
                            }
                        }
                    }
                }
            }
            IMAGE_CAPTURE_MODE_BACK_PRESSED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickOnFotoManualCloseButtonKtpPage(projectId)
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickOnFotoManualCloseButtonSelfiePage(projectId)
                    }
                }
            }
            SELFIE_PREPARE_SCREEN_VIEWED -> {
                when (eventProperties[FLOW_TYPE]) {
                    FLOW_TYPE_AURORA -> {
                        GotoKycAnalytics.sendViewGuideSelfiePage(
                            kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                            detectionType = VALUE_DETECTION_TYPE_AURORA,
                            projectId = projectId
                        )
                    }
                    else -> {
                        GotoKycAnalytics.sendViewGuideSelfiePage(
                            kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                            detectionType = VALUE_DETECTION_TYPE_AUTO,
                            projectId = projectId
                        )
                    }
                }
            }
            SELFIE_TIMER_EXHAUSTED_VIEW -> {
                GotoKycAnalytics.sendViewOnAuroraConfirmationEvent(
                    kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                    projectId = projectId
                )
            }
            SELFIE_TIMER_EXHAUSTED_CLICK_READY -> {
                GotoKycAnalytics.sendClickOnButtonOkAuroraConfirmationEvent(
                    kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                    projectId = projectId
                )
            }
            SELFIE_TIMER_EXHAUSTED_CLICK_LATER -> {
                GotoKycAnalytics.sendClickOnButtonLaterAuroraConfirmationEvent(
                    kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                    projectId = projectId
                )
            }
            CLICK_SELFIE_NEED_TIME -> {
                when (eventProperties[FLOW_TYPE]) {
                    FLOW_TYPE_AURORA -> {
                        GotoKycAnalytics.sendClickNeedTimeGuideSelfiePage(
                            kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                            detectionType = VALUE_DETECTION_TYPE_AURORA,
                            projectId = projectId
                        )
                    }
                    else -> {
                        GotoKycAnalytics.sendClickNeedTimeGuideSelfiePage(
                            kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                            detectionType = VALUE_DETECTION_TYPE_AUTO,
                            projectId = projectId
                        )
                    }
                }
            }
            SELFIE_PREPARE_READY_CLICKED -> {
                when (eventProperties[FLOW_TYPE]) {
                    FLOW_TYPE_AURORA -> {
                        GotoKycAnalytics.sendClickOnStartSelfie(
                            kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                            detectionType = VALUE_DETECTION_TYPE_AURORA,
                            projectId = projectId
                        )
                    }
                    else -> {
                        GotoKycAnalytics.sendClickOnStartSelfie(
                            kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                            detectionType = VALUE_DETECTION_TYPE_AUTO,
                            projectId = projectId
                        )
                    }
                }
            }
            PREVIEW_ALL_DOCUMENT_VIEWED -> {
                GotoKycAnalytics.sendViewReviewPage(projectId)
            }
            PREVIEW_ALL_DOCUMENT_CTA_CLICKED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickOnKtpImageReviewPage(projectId)
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickSelfieImage(projectId)
                    }
                }
            }
            DOCUMENT_RETAKE -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickButtonRetakeKtp(projectId)
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickButtonRetakeSelfie(projectId)
                    }
                }
            }
            DOCUMENT_RETAKE_CTA -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickOnButtonFotoUlangReviewKtpPage(projectId)
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickOnButtonFotoUlangReviewSelfiePage(projectId)
                    }
                }
            }
            PREVIEW_ALL_DOCUMENT_SUBMITTED -> {
                GotoKycAnalytics.sendClickButtonKirimDocument(projectId)
            }
            VIEW_DOCUMENT_LOOKS_GOOD_CLICKED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickOnButtonPakaiFotoIniReviewKtpPage(projectId)
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickOnButtonPakaiFotoIniReviewSelfiePage(projectId)
                    }
                }
            }
            IMAGE_QUALITY_ERROR -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendScanKtpImage(
                            statusScan = VALUE_IMAGE_DETECTED_ERROR,
                            projectId = projectId
                        )
                    }
                    SELFIE -> {
                        when (eventProperties[FLOW_TYPE]) {
                            FLOW_TYPE_AURORA -> {
                                GotoKycAnalytics.sendScanSelfieImage(
                                    statusScan = VALUE_IMAGE_DETECTED_ERROR,
                                    kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                                    detectionType = VALUE_DETECTION_TYPE_AURORA,
                                    projectId = projectId
                                )
                            }
                            else -> {
                                GotoKycAnalytics.sendScanSelfieImage(
                                    statusScan = VALUE_IMAGE_DETECTED_ERROR,
                                    kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                                    detectionType = VALUE_DETECTION_TYPE_AUTO,
                                    projectId = projectId
                                )
                            }
                        }
                    }
                }
            }
            IMAGE_DELETED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickDeleteKtp(projectId)
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickDeleteSelfie(projectId)
                    }
                }
            }
            IMAGE_DETECTED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendScanKtpImage(
                            statusScan = VALUE_IMAGE_DETECTED,
                            projectId = projectId
                        )
                    }
                    SELFIE -> {
                        when (eventProperties[FLOW_TYPE]) {
                            FLOW_TYPE_AURORA -> {
                                GotoKycAnalytics.sendScanSelfieImage(
                                    statusScan = VALUE_IMAGE_DETECTED,
                                    kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                                    detectionType = VALUE_DETECTION_TYPE_AURORA,
                                    projectId = projectId
                                )
                            }
                            else -> {
                                GotoKycAnalytics.sendScanSelfieImage(
                                    statusScan = VALUE_IMAGE_DETECTED,
                                    kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                                    detectionType = VALUE_DETECTION_TYPE_AUTO,
                                    projectId = projectId
                                )
                            }
                        }
                    }
                }
            }
            IMAGE_QUALITY_GOOD_DETECTED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendScanKtpImage(
                            statusScan = VALUE_IMAGE_DETECTED_GOOD,
                            projectId = projectId
                        )
                    }
                    SELFIE -> {
                        when (eventProperties[FLOW_TYPE]) {
                            FLOW_TYPE_AURORA -> {
                                GotoKycAnalytics.sendScanSelfieImage(
                                    statusScan = VALUE_IMAGE_DETECTED_GOOD,
                                    kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                                    detectionType = VALUE_DETECTION_TYPE_AURORA,
                                    projectId = projectId
                                )
                            }
                            else -> {
                                GotoKycAnalytics.sendScanSelfieImage(
                                    statusScan = VALUE_IMAGE_DETECTED_GOOD,
                                    kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                                    detectionType = VALUE_DETECTION_TYPE_AUTO,
                                    projectId = projectId
                                )
                            }
                        }
                    }
                }
            }
            MANUAL_IMAGE_CAPTURE_CLICKED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickOnButtonCaptureKtpPage(projectId)
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickOnButtonCaptureSelfiePage(projectId)
                    }
                }
            }
            IMAGE_CAPTURED -> {
                GotoKycCleanupStorageWorker.scheduleWorker(context)

                when (eventProperties[CAPTURE_MODE]) {
                    AUTO_CAPTURE -> {
                        when (eventProperties[TYPE]) {
                            KTP -> {
                                GotoKycAnalytics.sendScanKtpImage(
                                    statusScan = VALUE_IMAGE_CAPTURED,
                                    projectId = projectId
                                )
                            }
                            SELFIE -> {
                                when (eventProperties[FLOW_TYPE]) {
                                    FLOW_TYPE_AURORA -> {
                                        GotoKycAnalytics.sendScanSelfieImage(
                                            statusScan = VALUE_IMAGE_CAPTURED,
                                            kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                                            detectionType = VALUE_DETECTION_TYPE_AURORA,
                                            projectId = projectId
                                        )
                                    }
                                    else -> {
                                        GotoKycAnalytics.sendScanSelfieImage(
                                            statusScan = VALUE_IMAGE_CAPTURED,
                                            kycFlowType = VALUE_KYC_TYPE_NON_PROGRESSIVE,
                                            detectionType = VALUE_DETECTION_TYPE_AUTO,
                                            projectId = projectId
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val SCREEN_TYPE = "ScreenType"
        private const val SCREEN_TYPE_CAMERA = "Camera"
        private const val SCREEN_TYPE_ALL_PREVIEW = "All Document Preview"
        private const val SCREEN_TYPE_DOCUMENT_INFO = "Captured Document Info"
        private const val CAPTURE_MODE = "CaptureMode"
        private const val ACTUAL_USAGE = "ActualUsage"
        private const val FLOW_TYPE = "FlowType"
        private const val FLOW_TYPE_AURORA = "Zero Click Aurora"
        private const val TYPE = "Type"
        private const val KTP = "KTP"
        private const val SELFIE = "Selfie"
        private const val AUTO_CAPTURE = "Auto-Capture"
        private const val MANUAL_CAPTURE = "Manual"
        private const val DEEPLEARN_AUTO_CAPTURE = "DeeplearnAutoCapture"
        private const val CUSTOM = "Custom"
        private const val CAMERA_OPENED = "GP KYC Camera Opened"
        private const val IMAGE_CAPTURE_MODE_AUTO_CAPTURE  = "GP KYC Image Capture Mode Autocapture Clicked"
        private const val IMAGE_CAPTURE_MODE_CHANGE_VIEWED = "GP KYC Image Capture Mode Change Viewed"
        private const val IMAGE_CAPTURE_MODE_CHANGED = "GP KYC Image Capture Mode Changed"
        private const val APP_CLOSE_BUTTON_CLICKED = "GP KYC In App Close Button Clicked"
        private const val CLOSE_BUTTON_CLICKED = "GP KYC Close Button Clicked"
        private const val IMAGE_CAPTURE_MODE_BACK_PRESSED = "GP KYC Image Capture Mode Back Button Pressed"
        private const val SELFIE_PREPARE_SCREEN_VIEWED = "GP KYC Selfie Prepare Screen Viewed"
        private const val SELFIE_TIMER_EXHAUSTED_VIEW = "GP KYC Selfie Timer Exhausted"
        private const val SELFIE_TIMER_EXHAUSTED_CLICK_READY = "GP KYC Selfie Timer Exhausted Ready CTA Clicked"
        private const val SELFIE_TIMER_EXHAUSTED_CLICK_LATER = "GP KYC Selfie Timer Exhausted Close CTA Clicked"
        private const val CLICK_SELFIE_NEED_TIME = "GP KYC Selfie Prepare Need Time CTA Clicked"
        private const val SELFIE_PREPARE_READY_CLICKED = "GP KYC Selfie Prepare Ready CTA Clicked"
        private const val PREVIEW_ALL_DOCUMENT_VIEWED = "GP KYC All Documents Viewed"
        private const val PREVIEW_ALL_DOCUMENT_CTA_CLICKED = "GP KYC View Document CTA Clicked"
        private const val DOCUMENT_RETAKE = "GP KYC Document Retake"
        private const val DOCUMENT_RETAKE_CTA = "GP KYC View Document Retake CTA Clicked"
        private const val PREVIEW_ALL_DOCUMENT_SUBMITTED = "GP KYC All Documents Submitted"
        private const val VIEW_DOCUMENT_LOOKS_GOOD_CLICKED = "GP KYC View Document Looks Good CTA Clicked"
        private const val IMAGE_DELETED = "GP KYC Document Deleted"
        private const val IMAGE_QUALITY_ERROR = "GP KYC Image Quality Error"
        private const val IMAGE_DETECTED = "GP KYC Image Detected"
        private const val IMAGE_QUALITY_GOOD_DETECTED = "GP KYC Good Quality Image Detected"
        private const val MANUAL_IMAGE_CAPTURE_CLICKED = "GP KYC Image Capture Clicked"
        private const val VALUE_IMAGE_DETECTED = "image detected"
        private const val VALUE_IMAGE_DETECTED_GOOD = "good image detected"
        private const val VALUE_IMAGE_DETECTED_ERROR = "error image detected"
        private const val VALUE_IMAGE_CAPTURED = "image captured"
        private const val VALUE_DETECTION_TYPE_AURORA = "aurora"
        private const val VALUE_DETECTION_TYPE_AUTO = "auto"
        private const val VALUE_DETECTION_TYPE_MANUAL = "manual"
        private const val VALUE_KYC_TYPE_NON_PROGRESSIVE = "non progressive"
        private const val IMAGE_CAPTURED = "GP KYC Image Captured"
    }
}
