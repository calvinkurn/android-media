package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryQuery
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRechargeInquiryUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<TopupBillsEnquiryData>(graphqlRepository) {

    private var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): TopupBillsEnquiryData {
        val gqlRequest = GraphqlRequest(CommonTopupBillsGqlQuery.rechargeInquiry, TopupBillsEnquiryData::class.java, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        val error = gqlResponse.getError(TopupBillsEnquiryData::class.java)
        if (error == null || error.isEmpty()){
            return (gqlResponse.getData(TopupBillsEnquiryData::class.java) as TopupBillsEnquiryData)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun createInquiryParams(productId: String, clientNumber: String, inputData: Map<String, String>){
        val enquiryParams = mutableListOf<TopupBillsEnquiryQuery>()
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_SOURCE_TYPE, ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE))
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_DEVICE_ID, ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE))
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_PRODUCT_ID, productId))
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_CLIENT_NUMBER, clientNumber))
        inputData.forEach { (key, value) ->
            enquiryParams.add(TopupBillsEnquiryQuery(key, value))
        }
        params = RequestParams.create().apply {
            putObject(PARAM_FIELDS, enquiryParams)
        }
    }

    companion object{
        const val PARAM_FIELDS = "fields"
        const val ENQUIRY_PARAM_SOURCE_TYPE = "source_type"
        const val ENQUIRY_PARAM_DEVICE_ID = "device_id"
        const val ENQUIRY_PARAM_PRODUCT_ID = "product_id"
        const val ENQUIRY_PARAM_CLIENT_NUMBER = "client_number"
        const val ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE = "c20ad4d76fe977"
        const val ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE = "5"
    }
}