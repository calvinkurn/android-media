package com.tokopedia.oneclickcheckout.order.view.mapper

import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.EpharmacyPrescriptionDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.GetPrescriptionIdsResponse

object PrescriptionMapper {

    fun mapPrescriptionResponse(prescriptionIdsResponse: GetPrescriptionIdsResponse): EpharmacyPrescriptionDataModel {
        return EpharmacyPrescriptionDataModel(
            checkoutId = prescriptionIdsResponse.detailData?.prescriptionData?.checkoutId ?: "",
            prescriptionIds = prescriptionIdsResponse.detailData?.prescriptionData?.prescriptions?.map { prescription ->
                prescription?.prescriptionId.toString()
            } as ArrayList<String>
        )
    }
}
