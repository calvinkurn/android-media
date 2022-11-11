package com.tokopedia.epharmacy.utils

import com.tokopedia.common_epharmacy.network.response.EPharmacyItemButtonData
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel

object EPharmacyMapper {

    fun mapGroupsToAttachmentComponents(group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup,
                              info: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
                              index: Int, isLastGroup : Boolean) : EPharmacyAttachmentDataModel{
        return EPharmacyAttachmentDataModel(
            "${group.epharmacyGroupId},${info?.shopId  ?: ""}",GROUP_COMPONENT,
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
            prepareCtaData(group.prescriptionSource,group.consultationData,group.prescriptionImages),
            isLastIndex(group.shopInfo,index),
            (isLastIndex(group.shopInfo,index) && isLastGroup).not()
        )
    }

    private fun prepareCtaData(
        prescriptionSource: List<String?>?,
        consultationData: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData?,
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
        }else if(consultationData != null){
            buttonText = "Resep Digital Terlampir"
            buttonSubText = "Resep dari dokter"
        }
        return EPharmacyItemButtonData(EPHARMACY_RX_IMAGE,buttonText,buttonSubText,"",buttonSource)
    }

    fun isLastIndex(list : List<Any?>?, index : Int ) : Boolean{
        return index == ((list?.size ?: 0) - 1)
    }
}
