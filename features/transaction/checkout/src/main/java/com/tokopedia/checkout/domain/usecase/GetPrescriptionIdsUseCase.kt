package com.tokopedia.checkout.domain.usecase

import com.tokopedia.checkout.data.model.response.prescription.ConsultationData
import com.tokopedia.checkout.data.model.response.prescription.EpharmacyGroup
import com.tokopedia.checkout.data.model.response.prescription.GetPrescriptionIdsResponse
import com.tokopedia.checkout.data.model.response.prescription.PrepareProductGroupData
import com.tokopedia.checkout.data.model.response.prescription.PrepareProductGroupResponse
import com.tokopedia.checkout.data.model.response.prescription.Product
import com.tokopedia.checkout.data.model.response.prescription.ProductInfo
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetPrescriptionIdsUseCase @Inject constructor(private val gql: GraphqlUseCase) {

    fun execute(checkoutId: String): Observable<GetPrescriptionIdsResponse> {
        val gqlRequest = GraphqlRequest(query, GetPrescriptionIdsResponse::class.java, getRequestParams(checkoutId))
        gql.clearCache()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
            .map {
                val response: GetPrescriptionIdsResponse? =
                    it.getData<GetPrescriptionIdsResponse>(GetPrescriptionIdsResponse::class.java)
                response
                    ?: throw MessageErrorException(it.getError(GetPrescriptionIdsResponse::class.java)[0].message)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun execute(): Observable<PrepareProductGroupResponse> {
        var response = PrepareProductGroupResponse(
            data = PrepareProductGroupData(
                epharmacyGroups = listOf(
                    EpharmacyGroup(
                        epharmacyGroupId = "123",
                        productsInfo = listOf(
                            ProductInfo(
                                shopId = 14005189,
                                products = listOf(
                                    Product(productId = 5232131732)
                                )
                            )
                        ),
                        numberPrescriptionImages = 0,
                        prescriptionImages = listOf(),
                        consultationData = ConsultationData(
                            tokoConsultationId = 1,
                            partnerConsultationId = "asdf",
                            consultationStatus = 4
                        )
                    )
                )
            )
        )
        return Observable.just(response)
    }

    private fun getRequestParams(checkoutId: String): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PARAM_CHECKOUT_ID] = checkoutId
        return requestMap
    }

    companion object {
        const val PARAM_CHECKOUT_ID = "checkout_id"
    }

}

private val query = """
    query GetEpharmacyCheckoutData(${'$'}checkout_id: String!) {
    getEpharmacyCheckoutData(checkout_id: ${'$'}checkout_id) {
      data {
        checkout_id
        prescription_images {
          prescription_id
        }
      }
    }
}
""".trimIndent()
