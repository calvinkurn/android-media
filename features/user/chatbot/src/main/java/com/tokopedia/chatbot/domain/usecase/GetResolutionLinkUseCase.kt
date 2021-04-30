package com.tokopedia.chatbot.domain.usecase

import com.google.gson.JsonObject
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.chatbot.domain.resolink.ResoLinkResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val GET_RESO_LINK_QUERY = """
mutation getResolutionlink(${'$'}input :GetResolutionLinkInput!) {
  get_resolution_link(input:${'$'}input) {
    data{
      orderList{
        dynamicLink
        resoList{
          id
          resoTypeString
          statusString
          url
        }
      }
    }
    messageError
  }
}
"""

const val INVOICE_REF_NUMBER = "invoice_ref_num"
const val QUERY_REFERENCE_KEY = "ref"
const val QUERY_REFERENCE_VALUE = "chatbot"
const val QUERY_INPUT = "input"

@GqlQuery("GetResoLinkQuery", GET_RESO_LINK_QUERY)
class GetResolutionLinkUseCase @Inject constructor() {

    @Inject
    lateinit var baseRepository: BaseRepository

    suspend fun getResoLinkResponse(params: RequestParams): ResoLinkResponse {
        return baseRepository.getGQLData(GetResoLinkQuery.GQL_QUERY, ResoLinkResponse::class.java, params.parameters)
    }

    fun createRequestParams(invoiceRefNumber: String): RequestParams {
        val requestParams = RequestParams.create()
        val obj = JsonObject()
        obj.addProperty(INVOICE_REF_NUMBER, invoiceRefNumber)
        obj.addProperty(QUERY_REFERENCE_KEY, QUERY_REFERENCE_VALUE)
        requestParams.putObject(QUERY_INPUT, obj)
        return requestParams
    }
}