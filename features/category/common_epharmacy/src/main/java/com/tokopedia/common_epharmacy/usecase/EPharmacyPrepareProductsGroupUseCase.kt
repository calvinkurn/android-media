package com.tokopedia.common_epharmacy.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyPrepareProductsGroupUseCase @Inject constructor(@ApplicationContext graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyPrepareProductsGroupResponse>(graphqlRepository) {

    fun getEPharmacyPrepareProductsGroup(
        onSuccess: (EPharmacyPrepareProductsGroupResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        onSuccess(
            EPharmacyPrepareProductsGroupResponse(
                detailData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                    groupsData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
                        epharmacyGroups = listOf(
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(
                                epharmacyGroupId = "123",
                                productsInfo = listOf(
                                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                                        shopId = "14005189",
                                        products = listOf(
                                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                productId = 5232131732,
                                                isEthicalDrug = true,
                                                itemWeight = 0.0,
                                                name = "",
                                                productImage = "",
                                                productTotalWeightFmt = "",
                                                quantity = 1
                                            )
                                        ),
                                        partnerLogoUrl = "",
                                        shopLocation = "",
                                        shopLogoUrl = "",
                                        shopName = "",
                                        shopType = ""
                                    )
                                ),
                                numberPrescriptionImages = 0,
                                prescriptionImages = listOf(
                                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionImage(
                                        expiredAt = null,
                                        prescriptionId = "1",
                                        rejectReason = null,
                                        status = null
                                    )
                                ),
                                consultationData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData(
                                    tokoConsultationId = "0",
                                    partnerConsultationId = "0",
                                    consultationStatus = 0,
                                    doctorDetails = null,
                                    endTime = null,
                                    medicalRecommendation = null,
                                    prescription = null,
                                    startTime = ""
                                ),
                                consultationSource = null,
                                prescriptionSource = null
                            )
                        ),
                        attachmentPageTickerText = null
                    ),
                    header = null
                )
            )
        )
//        try {
//            this.setTypeClass(EPharmacyPrepareProductsGroupResponse::class.java)
//            setGraphqlQuery(GetEPharmacyPrepareProductsGroupQuery)
//            this.execute(
//                { result ->
//                    onSuccess(result)
//                }, { error ->
//                    onError(error)
//                }
//            )
//        } catch (throwable: Throwable) {
//            onError(throwable)
//        }
    }
}
