package com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model

data class EpharmacyPrescriptionDataModel(
    val checkoutId: String? = "",
    val prescriptions: List<Prescription?>? = emptyList()
) {
    data class Prescription(
        val prescriptionId: String? = ""
    )
}
