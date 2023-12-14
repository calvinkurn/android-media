package com.tokopedia.epharmacy.component.model

import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel

data class EPharmacyDataModel(
    val listOfComponents: MutableList<BaseEPharmacyDataModel> = mutableListOf()
)

class EPharmacyPPGTrackingData(
    private val enablerName: String?,
    private val tConsultationId: String?,
    private val ePharmacyGroupId: String?
) {
    override fun toString(): String {
        return "$enablerName - $tConsultationId - $ePharmacyGroupId"
    }
}
