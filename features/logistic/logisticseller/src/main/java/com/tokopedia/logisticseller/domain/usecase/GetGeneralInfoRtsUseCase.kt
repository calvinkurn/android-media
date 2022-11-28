package com.tokopedia.logisticseller.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticseller.data.param.GetGeneralInfoRtsParam
import com.tokopedia.logisticseller.data.response.GetGeneralInfoRtsResponse
import javax.inject.Inject

class GetGeneralInfoRtsUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetGeneralInfoRtsParam, GetGeneralInfoRtsResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return RETRY_AVAILABILITY_QUERY
    }

    override suspend fun execute(params: GetGeneralInfoRtsParam): GetGeneralInfoRtsResponse {
//        return repository.request(graphqlQuery(), params)
        return mockResponse()
    }

    private fun mockResponse(): GetGeneralInfoRtsResponse{
        return GetGeneralInfoRtsResponse(
            status = 200,
            data = GetGeneralInfoRtsResponse.GeneralInfoRtsData(
                title = "Konfirmasi Pengembalian Barang",
                description = "Pesanan INV/20220101/ABC/1234567890 berhasil dikembalikan & diterima penjual. Silakan konfirmasi maks. 2x24 jam, ya.",
                articleUrl = "https://www.tokopedia.com/help/seller/article/cara-menanggapi-komplain-retur-dan-pengembalian-dana",
                image = GetGeneralInfoRtsResponse.Image(imageId = "https://sarjanaekonomi.co.id/wp-content/uploads/2020/12/Barang.jpg"),
            )
        )
    }

    companion object {
        const val RETRY_AVAILABILITY_QUERY = """
          query getGeneralInformation(${'$'}input:GetGeneralInfoRtsParam!){
            getGeneralInformation(input: ${'$'}input) {
              data {
                title
                description
                invoice
                image {
                  image_id
                }
                article_url
              }
              status
              message_error
            }
          }  
        """
    }
}
