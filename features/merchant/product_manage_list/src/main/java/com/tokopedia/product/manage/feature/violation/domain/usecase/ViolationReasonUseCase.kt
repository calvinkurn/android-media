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
        private val QUERY = """
            query {
              visionGetPendingReasonDetail {
                product_id
                title
                content {
                  description {
                    info
                    detail
                  }
                  resolution {
                    info
                    steps
                  }
                  cta {
                    text
                    link
                  }
                }
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
                        title = "Pelanggaran atribut",
                        content = PendingReasonContent(
                                description = PendingReasonDescription(
                                        descDetail = "Berupa organ tubuh manusia",
                                        descInfo = "Produk diduga melanggar hukum karena:"
                                ),
                                resolution = PendingReasonResolution(
                                        resolutionInfo = "Lakukan langkah berikut untuk menyelesaikan:",
                                        resolutionSteps = listOf(
                                                "Pelajari tentang <a href=\"https://www.tokopedia.com/login\">produk yang dilarang</a>",
                                                "Tambah produk sesuai <a href=\"https://www.tokopedia.com/login\">Syarat & Ketentuan</a> yang berlaku di Tokopedia"
                                        )
                                ),
                                ctaList = listOf(
                                        ViolationCTA(
                                                buttonText = "Bantuan",
                                                buttonLink = "sellerapp://home"
                                        )
                                )
                        )
                )
        )
        delay(1000)
        return mapper.mapViolationResponseToUiModel(dummyResponse)
    }

}