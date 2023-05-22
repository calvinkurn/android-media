package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.util.Log
import com.gojek.kyc.sdk.config.KycSdkAnalyticsConfig
import com.gojek.kyc.sdk.core.analytics.IKycSdkEventTrackingProvider
import com.tokopedia.kyc_centralized.ui.gotoKyc.analytics.GotoKycAnalytics
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import javax.inject.Inject

class GotoKycEventTrackingProvider @Inject constructor(
    private val kycSdkAnalyticsConfig: KycSdkAnalyticsConfig,
    private val kycSharedPreference: KycSharedPreference
) : IKycSdkEventTrackingProvider {
    override fun getAnalyticsConfigForClickStream(): KycSdkAnalyticsConfig {
        return kycSdkAnalyticsConfig
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
        Log.i("Tracker", "track: $eventName ,,,,,,,,, eventProperties: $eventProperties ,,,,,,,,, productName: $productName")
    }

    private fun sendTracker(eventName: String, eventProperties: Map<String, Any?>) {
        when (eventName) {
            CAMERA_OPENED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        //GotoKycAnalytics.sendViewKtpPage(kycSharedPreference.getProjectId())
                    }
                    SELFIE -> {
                        //GotoKycAnalytics.sendViewSelfiePage(kycSharedPreference.getProjectId())
                    }
                }
            }
            IMAGE_CAPTURE_MODE_CHANGE_VIEWED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendViewManualFotoKtpQuestion(kycSharedPreference.getProjectId())
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendViewManualFotoSelfie(kycSharedPreference.getProjectId())
                    }
                }
            }
            IMAGE_CAPTURE_MODE_CHANGED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickButtonFotoManualKtpPage(kycSharedPreference.getProjectId())
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickButtonFotoManualSelfiePage(kycSharedPreference.getProjectId())
                    }
                }
            }
            IMAGE_CAPTURE_INITIATED -> {
                when (eventProperties[CAPTURE_MODE]) {
                    AUTO_CAPTURE -> {
                        when (eventProperties[TYPE]) {
                            KTP -> {
                                GotoKycAnalytics.sendScanKtpImage(
                                    statusScan = VALUE_IMAGE_CAPTURED,
                                    projectId = kycSharedPreference.getProjectId()
                                )
                            }
                        }
                    }
                    MANUAL_CAPTURE -> {
                        when (eventProperties[TYPE]) {
                            KTP -> {
                                GotoKycAnalytics.sendClickOnButtonCaptureKtpPage(kycSharedPreference.getProjectId())
                            }
                            SELFIE -> {
                                GotoKycAnalytics.sendClickOnButtonCaptureSelfiePage(kycSharedPreference.getProjectId())
                            }
                        }
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
                                        GotoKycAnalytics.sendClickOnButtonBackFromKtpPage(kycSharedPreference.getProjectId())
                                    }
                                    MANUAL_CAPTURE -> {
                                        GotoKycAnalytics.sendClickOnButtonBackManualKtp(kycSharedPreference.getProjectId())
                                    }
                                }
                            }
                            SELFIE -> {
                                when (eventProperties[CAPTURE_MODE]) {
                                    AUTO_CAPTURE -> {
                                        GotoKycAnalytics.sendClickOnButtonBackSelfiePage(kycSharedPreference.getProjectId())
                                    }
                                    MANUAL_CAPTURE -> {
                                        GotoKycAnalytics.sendClickOnButtonBackManualSelfie(kycSharedPreference.getProjectId())
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
                        GotoKycAnalytics.sendClickOnButtonBackFromReviewPage(kycSharedPreference.getProjectId())
                    }
                    SCREEN_TYPE_DOCUMENT_INFO -> {
                        when (eventProperties[TYPE]) {
                            KTP -> {
                                GotoKycAnalytics.sendClickOnButtonBackFromReviewKtpPage(kycSharedPreference.getProjectId())
                            }
                            SELFIE -> {
                                GotoKycAnalytics.sendClickOnButtonBackFromReviewSelfiePage(kycSharedPreference.getProjectId())
                            }
                        }
                    }
                }
            }
            //TODO: remove this when unused (need confirm with PO)
            GUIDELINE_CLICKED -> {
                when (eventProperties[SCREEN_TYPE]) {
                    SCREEN_TYPE_CAMERA -> {
                        when (eventProperties[TYPE]) {
                            KTP -> {
                            }
                            SELFIE -> {
                            }
                        }
                    }
                    SCREEN_TYPE_ALL_PREVIEW -> {
                        when (eventProperties[TYPE]) {
                            KTP -> {
                            }
                            SELFIE -> {
                            }
                        }
                    }
                    SCREEN_TYPE_DOCUMENT_INFO -> {
                        when (eventProperties[TYPE]) {
                            KTP -> {
                            }
                            SELFIE -> {
                            }
                        }
                    }
                }
            }
            IMAGE_CAPTURE_MODE_BACK_PRESSED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickOnFotoManualCloseButtonKtpPage(kycSharedPreference.getProjectId())
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickOnFotoManualCloseButtonSelfiePage(kycSharedPreference.getProjectId())
                    }
                }
            }
            SELFIE_PREPARE_SCREEN_VIEWED -> {
                GotoKycAnalytics.sendViewGuideSelfiePage(kycSharedPreference.getProjectId())
            }
            SELFIE_PREPARE_READY_CLICKED -> {
                GotoKycAnalytics.sendClickOnStartSelfie(kycSharedPreference.getProjectId())
            }
            PREVIEW_ALL_DOCUMENT_VIEWED -> {
                GotoKycAnalytics.sendViewReviewPage(kycSharedPreference.getProjectId())
            }
            PREVIEW_ALL_DOCUMENT_CTA_CLICKED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickOnKtpImageReviewPage(kycSharedPreference.getProjectId())
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickSelfieImage(kycSharedPreference.getProjectId())
                    }
                }
            }
            DOCUMENT_RETAKE -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickButtonRetakeKtp(kycSharedPreference.getProjectId())
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickButtonRetakeSelfie(kycSharedPreference.getProjectId())
                    }
                }
            }
            PREVIEW_ALL_DOCUMENT_SUBMITTED -> {
                GotoKycAnalytics.sendClickButtonKirimDocument(kycSharedPreference.getProjectId())
            }
            VIEW_DOCUMENT_LOOKS_GOOD_CLICKED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendClickOnButtonPakaiFotoIniReviewKtpPage(kycSharedPreference.getProjectId())
                    }
                    SELFIE -> {
                        GotoKycAnalytics.sendClickOnButtonPakaiFotoIniReviewSelfiePage(kycSharedPreference.getProjectId())
                    }
                }
            }
            IMAGE_QUALITY_ERROR -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendScanKtpImage(
                            statusScan = VALUE_IMAGE_DETECTED_ERROR,
                            projectId = kycSharedPreference.getProjectId()
                        )
                    }
                }
            }
            IMAGE_DETECTED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendScanKtpImage(
                            statusScan = VALUE_IMAGE_DETECTED,
                            projectId = kycSharedPreference.getProjectId()
                        )
                    }
                }
            }
            IMAGE_QUALITY_GOOD_DETECTED -> {
                when (eventProperties[TYPE]) {
                    KTP -> {
                        GotoKycAnalytics.sendScanKtpImage(
                            statusScan = VALUE_IMAGE_DETECTED_GOOD,
                            projectId = kycSharedPreference.getProjectId()
                        )
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
        private const val TYPE = "Type"
        private const val KTP = "KTP"
        private const val SELFIE = "Selfie"
        private const val AUTO_CAPTURE = "Auto-Capture"
        private const val MANUAL_CAPTURE = "Manual"
        private const val CAMERA_OPENED = "GP KYC Camera Opened"
        private const val IMAGE_CAPTURE_MODE_CHANGE_VIEWED = "GP KYC Image Capture Mode Change Viewed"
        private const val IMAGE_CAPTURE_MODE_CHANGED = "GP KYC Image Capture Mode Changed"
        private const val IMAGE_CAPTURE_INITIATED = "GP KYC Image Capture Initiated"
        private const val APP_CLOSE_BUTTON_CLICKED = "GP KYC In App Close Button Clicked"
        private const val CLOSE_BUTTON_CLICKED = "GP KYC Close Button Clicked"
        private const val GUIDELINE_CLICKED = "GP KYC Guideline Clicked"
        private const val IMAGE_CAPTURE_MODE_BACK_PRESSED = "GP KYC Image Capture Mode Back Button Pressed"
        private const val SELFIE_PREPARE_SCREEN_VIEWED = "GP KYC Selfie Prepare Screen Viewed"
        private const val SELFIE_PREPARE_READY_CLICKED = "GP KYC Selfie Prepare Ready CTA Clicked"
        private const val PREVIEW_ALL_DOCUMENT_VIEWED = "GP KYC All Documents Viewed"
        private const val PREVIEW_ALL_DOCUMENT_CTA_CLICKED = "GP KYC View Document CTA Clicked"
        private const val DOCUMENT_RETAKE = "GP KYC Document Retake"
        private const val PREVIEW_ALL_DOCUMENT_SUBMITTED = "GP KYC All Documents Submitted"
        private const val VIEW_DOCUMENT_LOOKS_GOOD_CLICKED = "GP KYC View Document Looks Good CTA Clicked"
        private const val IMAGE_QUALITY_ERROR = "GP KYC Image Quality Error"
        private const val IMAGE_DETECTED = "GP KYC Image Detected"
        private const val IMAGE_QUALITY_GOOD_DETECTED = "GP KYC Good Quality Image Detected"
        private const val VALUE_IMAGE_DETECTED = "image detected"
        private const val VALUE_IMAGE_DETECTED_GOOD = "good image detected"
        private const val VALUE_IMAGE_DETECTED_ERROR = "error image detected"
        private const val VALUE_IMAGE_CAPTURED = "image captured"
    }
}
