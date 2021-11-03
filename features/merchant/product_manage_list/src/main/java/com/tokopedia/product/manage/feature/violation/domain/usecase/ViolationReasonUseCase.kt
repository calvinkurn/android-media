package com.tokopedia.product.manage.feature.violation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.violation.data.*
import com.tokopedia.product.manage.feature.violation.domain.mapper.ViolationReasonMapper
import com.tokopedia.product.manage.feature.violation.view.uimodel.ViolationReasonUiModel
import kotlinx.coroutines.delay
import javax.inject.Inject

class ViolationReasonUseCase @Inject constructor(graphqlRepository: GraphqlRepository,
                                                 private val mapper: ViolationReasonMapper) :
    GraphqlUseCase<ViolationReasonDetailResponse>(graphqlRepository) {

    companion object {
        // TODO: Update this query
        private val QUERY = """
            query {
              visionGetPendingReasonDetail {
                product_id,
                platform_id
              }
            }
        """.trimIndent()
    }

    init {
        setTypeClass(ViolationReasonDetailResponse::class.java)
        setGraphqlQuery(QUERY)
    }

    suspend fun execute(): ViolationReasonUiModel {
        // TODO: Remove Dummy
        val dummyResponse = ViolationReasonDetailResponse(
            detail = VisionGetPendingReasonDetail(
                productId = "123",
                mainTitle = "Pelanggaran atribut",
                description = PendingReasonDescription(
                    descText = "Berupa organ tubuh manusia",
                    preDescText = "Produk diduga melanggar hukum karena:"
                ),
                resolutionSteps = listOf(
                    PendingReasonResolutionSteps("Pelajari tentang <a href=\"https://www.tokopedia.com/login\">produk yang dilarang</a>"),
                    PendingReasonResolutionSteps("Tambah produk sesuai <a href=\"https://www.tokopedia.com/login\">Syarat & Ketentuan</a> yang berlaku di Tokopedia")
                ),
                callToActionInfo = CallToActionInfo(
                    buttonText = "Bantuan",
                    buttonLink = "sellerapp://home"
                )
            )
        )
        delay(1000)
        return mapper.mapViolationResponseToUiModel(dummyResponse)
    }

}