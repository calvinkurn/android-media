package com.tokopedia.epharmacy.utils

import com.tokopedia.common_epharmacy.network.response.EPharmacyItemButtonData
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.network.response.PrescriptionStatusCount

object EPharmacyMapper {

    fun mapGroupsToAttachmentComponents(group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup,
                              info: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
                              index: Int, isLastShopOfGroup : Boolean, isLastGroup : Boolean) : EPharmacyAttachmentDataModel{
        return EPharmacyAttachmentDataModel(
            getUniqueModelName(group.epharmacyGroupId,index,isLastShopOfGroup),GROUP_COMPONENT,
            group.epharmacyGroupId,
            group.consultationSource?.enablerName,
            group.consultationSource?.enablerLogoUrl,
            info,
            group.consultationData?.consultationStatus,
            group.consultationData?.consultationString,
            group.consultationData?.prescription,
            group.consultationData?.partnerConsultationId,
            group.consultationData?.tokoConsultationId,
            group.prescriptionImages,
            group.prescriptionSource,
            group.consultationSource,
            false,
            prepareCtaData(group.prescriptionSource,group.consultationData?.tokoConsultationId,group.prescriptionImages),
            isLastIndex(group.shopInfo,index),
            (isLastIndex(group.shopInfo,index) && isLastGroup).not()
        )
    }

    fun getUniqueModelName(ePharmacyGroupId : String?, index : Int, isLastShopOfGroup : Boolean): String {
        return if(isLastShopOfGroup) {
            "${ePharmacyGroupId}_${index}"
        }else {
            "${ePharmacyGroupId}_N"
        }
    }

    fun prepareCtaData(
        prescriptionSource: List<String?>?,
        tokoConsultationId : String?,
        prescriptionImages: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionImage?>?
    ) : EPharmacyItemButtonData {
        var buttonText = "Chat Dokter Buat Dapat Resep"
        var buttonSubText = ""
        var buttonSource = ""
        prescriptionSource?.let { sources ->
            if(sources.size > 1){
                buttonText = "Upload Resep atau Chat Dokter"
                buttonSource = PrescriptionSourceType.MULTI.type
            }else {
                sources.firstOrNull()?.let { source ->
                    buttonText = when(source){
                        PrescriptionSourceType.MINI_CONSULT.type ->{
                            buttonSource = PrescriptionSourceType.MINI_CONSULT.type
                            "Chat Dokter Buat Dapat Resep"
                        }
                        PrescriptionSourceType.UPLOAD.type ->{
                            buttonSource = PrescriptionSourceType.UPLOAD.type
                            "Upload Resep"
                        }
                        else -> {
                            ""
                        }
                    }
                }
            }
        }
        if(((prescriptionImages?.size ?: 0) > 1) ){
            buttonText = "Resep Terlampir"
            buttonSubText = "Kamu punya ${prescriptionImages?.size} foto resep"
        }else if(!tokoConsultationId.isNullOrBlank()){
            buttonText = "Resep Digital Terlampir"
            buttonSubText = "Resep dari dokter"
        }
        return EPharmacyItemButtonData(EPHARMACY_RX_IMAGE,buttonText,buttonSubText,"",buttonSource)
    }

    fun isLastIndex(list : List<Any?>?, index : Int ) : Boolean{
        return index == ((list?.size ?: 0) - 1)
    }

    fun getPrescriptionCount(consultationStatus: ArrayList<Int>) : PrescriptionStatusCount{
        val statusCount = PrescriptionStatusCount(0,0,0)
        consultationStatus.forEach { status ->
            when (status) {
                EPharmacyConsultationStatus.REJECTED.status, EPharmacyConsultationStatus.EXPIRED.status -> {
                    statusCount.rejected += 1
                }
                EPharmacyConsultationStatus.APPROVED.status -> {
                    statusCount.approved += 1
                }
                EPharmacyConsultationStatus.ACTIVE.status -> {
                    statusCount.active += 1
                }
            }
        }
        return statusCount
    }
}
