package com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model

data class EpharmacyPrescriptionDataModel(
    val checkoutId: String = "",
    val prescriptionIds: List<String>? = emptyList()
)
