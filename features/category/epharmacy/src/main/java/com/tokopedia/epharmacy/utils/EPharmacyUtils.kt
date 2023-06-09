package com.tokopedia.epharmacy.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyTickerDataModel
import com.tokopedia.usecase.BuildConfig
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    fun getConsultationIds(response: EPharmacyPrepareProductsGroupResponse?): ArrayList<String> {
        val ids = arrayListOf<String>()
        response?.detailData?.groupsData?.epharmacyGroups?.forEach { gp ->
            ids.add(gp?.consultationSource?.id.toString())
        }
        return ids
    }

    fun getPrescriptionIds(response: EPharmacyPrepareProductsGroupResponse?): ArrayList<String?> {
        val ids = arrayListOf<String?>()
        response?.detailData?.groupsData?.epharmacyGroups?.forEach { gp ->
            gp?.prescriptionImages?.forEach { pres ->
                if (!pres?.prescriptionId.isNullOrBlank()) {
                    ids.add(pres?.prescriptionId)
                }
            }
        }
        return ids
    }

    fun getShopIds(ePharmacyGroupId: String?, ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse?): List<String> {
        val ids = mutableListOf<String>()
        val groups = ePharmacyPrepareProductsGroupResponse?.detailData?.groupsData?.epharmacyGroups ?: return emptyList()
        for (gp in groups) {
            if (gp?.epharmacyGroupId == ePharmacyGroupId) {
                gp?.shopInfo?.forEach { si ->
                    si?.shopId?.takeIf { it.isNotBlank() }?.let { ids.add(it) }
                }
            }
        }
        return ids
    }

    fun getShopIds(ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse?): List<String> {
        val ids = arrayListOf<String>()
        val groups = ePharmacyPrepareProductsGroupResponse?.detailData?.groupsData?.epharmacyGroups ?: return arrayListOf()
        groups.forEach { gp ->
            gp?.shopInfo?.forEach { si ->
                if (!si?.shopId.isNullOrBlank()) {
                    ids.add(si?.shopId ?: "")
                }
            }
        }
        return ids
    }

    fun getEnablers(ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse?): ArrayList<String> {
        val ids = arrayListOf<String>()
        ePharmacyPrepareProductsGroupResponse?.detailData?.groupsData?.epharmacyGroups?.forEach { gp ->
            if (!gp?.consultationSource?.enablerName.isNullOrBlank()) {
                ids.add(gp?.consultationSource?.enablerName ?: "")
            }
        }
        return ids
    }

    fun getGroupIds(ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse?): ArrayList<String> {
        val ids = arrayListOf<String>()
        val groups = ePharmacyPrepareProductsGroupResponse?.detailData?.groupsData?.epharmacyGroups ?: return arrayListOf()
        groups.forEach { gp ->
            if (!gp?.epharmacyGroupId.isNullOrBlank()) {
                ids.add(gp?.epharmacyGroupId ?: "")
            }
        }
        return ids
    }

    fun findGroup(ePharmacyGroupId: String?, ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse?): EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup? {
        return ePharmacyPrepareProductsGroupResponse?.detailData?.groupsData?.epharmacyGroups?.find { group -> group?.epharmacyGroupId == ePharmacyGroupId }
    }

    fun getMiniConsultationModel(group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup?): EPharmacyMiniConsultationResult {
        return EPharmacyMiniConsultationResult(
            group?.epharmacyGroupId,
            group?.shopInfo,
            group?.consultationData?.consultationStatus,
            group?.consultationData?.consultationString,
            group?.consultationData?.prescription,
            group?.consultationData?.partnerConsultationId,
            group?.consultationData?.tokoConsultationId.toString(),
            group?.prescriptionImages
        )
    }

    fun mapGroupsDataIntoDataModel(data: EPharmacyPrepareProductsGroupResponse): EPharmacyDataModel {
        val listOfComponents = arrayListOf<BaseEPharmacyDataModel>()
        if (data.detailData?.groupsData?.attachmentPageTickerText?.isNotBlank() == true) {
            listOfComponents.add(
                EPharmacyTickerDataModel(
                    TICKER_COMPONENT,
                    TICKER_COMPONENT,
                    data.detailData?.groupsData?.attachmentPageTickerText,
                    data.detailData?.groupsData?.attachmentPageTickerLogoUrl,
                    EPHARMACY_TICKER_BACKGROUND
                )
            )
        }

        data.detailData?.groupsData?.epharmacyGroups?.forEachIndexed { indexGroup, group ->
            if (!group?.shopInfo.isNullOrEmpty()) {
                group?.shopInfo?.forEachIndexed { shopIndex, info ->
                    if (info?.products?.isEmpty() != true) {
                        listOfComponents.add(
                            getGroupComponent(
                                group,
                                info,
                                shopIndex,
                                EPharmacyMapper.isLastIndex(data.detailData?.groupsData?.epharmacyGroups, indexGroup)
                            )
                        )
                    }
                }
            }
        }
        return EPharmacyDataModel(listOfComponents)
    }

    private fun getGroupComponent(
        group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup,
        info: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
        shopIndex: Int,
        isLastGroup: Boolean
    ): BaseEPharmacyDataModel {
        return EPharmacyMapper.mapGroupsToAttachmentComponents(group, info, shopIndex, isLastGroup)
    }
}

enum class PrescriptionActionType(val type: String) {
    REDIRECT_PWA("REDIRECT_CONSULTATION_PWA"),
    REDIRECT_OPTION("SHOW_PRESCRIPTION_ATTACHMENT_OPTION"),
    REDIRECT_UPLOAD("REDIRECT_UPLOAD_PRESC_PAGE"),
    REDIRECT_PRESCRIPTION("REDIRECT_CONSULTATION_PRESCRIPTION"),
    REDIRECT_CHECK_PRESCRIPTION("REDIRECT_CEK_PRESCRIPTION_PAGE")
}
