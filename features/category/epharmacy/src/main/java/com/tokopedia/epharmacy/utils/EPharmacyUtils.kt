package com.tokopedia.epharmacy.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.usecase.BuildConfig
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object EPharmacyUtils {

    fun logException(e: Exception) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(e)
        } else {
            e.printStackTrace()
        }
    }

    fun checkIsError(ePharmacyAttachmentDataModel: EPharmacyAttachmentDataModel?): Boolean {
        return (
            (
                ePharmacyAttachmentDataModel != null &&
                    checkIsErrorForConsultationData(ePharmacyAttachmentDataModel) &&
                    checkIsErrorForPrescriptionImages(ePharmacyAttachmentDataModel)
                )
            )
    }

    private fun checkIsErrorForConsultationData(ePharmacyAttachmentDataModel: EPharmacyAttachmentDataModel): Boolean {
        return (
            ePharmacyAttachmentDataModel.consultationData == null ||
                !arrayListOf(
                    EPharmacyConsultationStatus.APPROVED.status,
                    EPharmacyConsultationStatus.REJECTED.status
                )
                    .contains(ePharmacyAttachmentDataModel.consultationStatus)
            )
    }

    private fun checkIsErrorForPrescriptionImages(ePharmacyAttachmentDataModel: EPharmacyAttachmentDataModel): Boolean {
        return ePharmacyAttachmentDataModel.prescriptionImages?.isEmpty() == true
    }

    fun formatDateToLocal(currentFormat: String = YYYY_MM_DD_T_HH_MM_SS_Z, newFormat: String = NEW_DATE_FORMAT, dateString: String): Date? {
        return try {
            val fromFormat: DateFormat = SimpleDateFormat(currentFormat, Locale.ENGLISH)
            fromFormat.isLenient = false
            fromFormat.timeZone = TimeZone.getTimeZone(UTC)
            val toFormat: DateFormat = SimpleDateFormat(newFormat, Locale.ENGLISH)
            toFormat.isLenient = false
            toFormat.timeZone = TimeZone.getDefault()

            fromFormat.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun getTimeFromDate(date: Date?): String {
        date?.let {
            val fromFormat: DateFormat = SimpleDateFormat(HH_MM, Locale.ENGLISH)
            return fromFormat.format(date).toString()
        } ?: kotlin.run {
            return ""
        }
    }
}

enum class PrescriptionActionType(val type: String) {
    REDIRECT_PWA("REDIRECT_CONSULTATION_PWA"),
    REDIRECT_OPTION("SHOW_PRESCRIPTION_ATTACHMENT_OPTION"),
    REDIRECT_UPLOAD("REDIRECT_UPLOAD_PRESC_PAGE"),
    REDIRECT_PRESCRIPTION("REDIRECT_CONSULTATION_PRESCRIPTION"),
    REDIRECT_CHECK_PRESCRIPTION("REDIRECT_CEK_PRESCRIPTION_PAGE")
}
