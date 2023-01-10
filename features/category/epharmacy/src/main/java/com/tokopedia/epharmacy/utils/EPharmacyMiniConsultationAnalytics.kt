package com.tokopedia.epharmacy.utils

import com.tokopedia.track.builder.Tracker

object EPharmacyMiniConsultationAnalytics {

    fun userViewAttachPrescriptionPage(isLoggedIn: Boolean, userId: String, totalShop: String, epGroupId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.OPEN_SCREEN)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.OPEN_SCREEN_ID_ATTACH)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.IS_LOGGED_IN, isLoggedIn.toString())
            .setCustomProperty(EventKeys.PAGE_PATH, "")
            .setCustomProperty(EventKeys.SCREEN_NAME, "view attach prescription page - $totalShop - $epGroupId")
            .setUserId(userId)
            .build()
            .send()
    }

    fun clickAttachPrescriptionButton(
        buttonText: String,
        enablers: String,
        buttonPosition: String,
        totalShop: String,
        shopId: String?,
        ePharmacyGroupId: String?
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_PG)
            .setEventAction(ActionKeys.CLICK_ATTACH_PRESCRIPTION_BUTTON)
            .setEventCategory(CategoryKeys.ATTACH_PRESCRIPTION_PAGE)
            .setEventLabel("$buttonText - $enablers - $buttonPosition - $totalShop - $shopId - $ePharmacyGroupId")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.ATTACH_PRESCRIPTION_BUTTON)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    fun viewMiniConsultationPage(
        isLoggedIn: Boolean,
        userId: String,
        enablerName: String,
        ePharmacyGroupId: String,
        consultationId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_GROCERIES_IRIS)
            .setEventAction(ActionKeys.VIEW_CONSULTATION_WEB_VIEW_URL_GENERATED)
            .setEventCategory(CategoryKeys.ATTACH_PRESCRIPTION_PAGE)
            .setEventLabel("$enablerName - $ePharmacyGroupId - $consultationId")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.VIEW_MINI_CONSULTATION)
            .setCustomProperty(EventKeys.IS_LOGGED_IN, isLoggedIn.toString())
            .setUserId(userId)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    fun viewAttachPrescriptionResult(
        consultationId: Long,
        enablerName: String,
        successFailed: String,
        groupId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_GROCERIES_IRIS)
            .setEventAction(ActionKeys.VIEW_ATTACH_PRESCRIPTION_RESULT)
            .setEventCategory(CategoryKeys.ATTACH_PRESCRIPTION_PAGE)
            .setEventLabel("$consultationId - $enablerName - $successFailed - $groupId")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.ATTACH_PRESCRIPTION_RESULT)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    fun viewAttachPrescriptionsOptionsPage(enablerName: String?, ePharmacyGroupId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_PG_IRIS)
            .setEventAction(ActionKeys.VIEW_ATTACH_PRESCRIPTION_OPTION_PAGE)
            .setEventCategory(CategoryKeys.ATTACH_PRESCRIPTION_PAGE)
            .setEventLabel("$enablerName - $ePharmacyGroupId")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.ATTACH_PRESCRIPTION_OPTIONS)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    fun clickUploadResepDokter(enablerName: String?, ePharmacyGroupId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_PG)
            .setEventAction(ActionKeys.CLICK_UPLOAD_RESEP_DOKTER)
            .setEventCategory(CategoryKeys.ATTACH_PRESCRIPTION_PAGE)
            .setEventLabel("$enablerName - $ePharmacyGroupId")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_UPLOAD_RESEP_DOKTER)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    fun clickChatDokter(enablerName: String?, ePharmacyGroupId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_PG_IRIS)
            .setEventAction(ActionKeys.CLICK_CHAT_DOKTER)
            .setEventCategory(CategoryKeys.ATTACH_PRESCRIPTION_PAGE)
            .setEventLabel("$enablerName - $ePharmacyGroupId")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_CHAT_DOKTER)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    fun clickLanjutButton(
        consultationIds: String,
        enablerNames: String,
        approvalReason: String,
        ePharmacyGroupId: String,
        prescriptionIds: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_PG)
            .setEventAction(ActionKeys.CLICK_LANJUT_KE_PENGIRIMAN)
            .setEventCategory(CategoryKeys.ATTACH_PRESCRIPTION_PAGE)
            .setEventLabel("$consultationIds - $enablerNames - $approvalReason - $ePharmacyGroupId - $prescriptionIds")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.LANJUT_BUTTON_CLICK)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    fun viewNoDoctorScreen(
        groupId: String,
        enablerName: String,
        insideOrOutside: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_PG_IRIS)
            .setEventAction(ActionKeys.VIEW_NO_DOCTOR_PAGE)
            .setEventCategory(CategoryKeys.ATTACH_PRESCRIPTION_PAGE)
            .setEventLabel("$groupId - $enablerName - $insideOrOutside")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.VIEW_NO_DOCTOR)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    fun clickIngatkanSaya(
        groupId: String,
        enablerName: String,
        insideOrOutside: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_PG)
            .setEventAction(ActionKeys.CLICK_INGATKAN_SAYA)
            .setEventCategory(CategoryKeys.ATTACH_PRESCRIPTION_PAGE)
            .setEventLabel("$groupId - $enablerName - $insideOrOutside")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_INGATKAN_SAYA)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }
}
