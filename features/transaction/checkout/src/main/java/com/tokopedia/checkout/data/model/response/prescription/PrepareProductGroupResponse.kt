package com.tokopedia.checkout.data.model.response.prescription

class PrepareProductGroupResponse(
    var data: PrepareProductGroupData = PrepareProductGroupData()
)

class PrepareProductGroupData(
    var epharmacyGroups: List<EpharmacyGroup> = emptyList()
)

class EpharmacyGroup(
    var epharmacyGroupId: String = "",
    var productsInfo: List<ProductInfo> = emptyList(),
    var numberPrescriptionImages: Int = 0,
    var prescriptionImages: List<Prescription> = emptyList(),
    var consultationData: ConsultationData = ConsultationData()
)

class ProductInfo(
    var shopId: Long = 0,
    var products: List<Product> = emptyList()
)

class Product(
    var productId: Long = 0
)

class Prescription(
    val prescriptionId: String = ""
)

class ConsultationData(
    var tokoConsultationId: Long = 0,
    var partnerConsultationId: String = "",
    var consultationStatus: Int = 0
)
