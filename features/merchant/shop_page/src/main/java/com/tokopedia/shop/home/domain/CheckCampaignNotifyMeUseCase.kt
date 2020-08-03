package com.tokopedia.shop.home.domain

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.*
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.home.GqlQueryConstant.GQL_CHECK_CAMPAIGN_NOTIFY_ME
import com.tokopedia.shop.home.GqlQueryConstant.GQL_GET_CAMPAIGN_NOTIFY_ME
import com.tokopedia.shop.home.data.model.CheckCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.CheckCampaignNotifyMeRequest
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeRequest
import com.tokopedia.usecase.coroutines.UseCase
import java.io.File
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named

class CheckCampaignNotifyMeUseCase @Inject constructor(
        @Named(GQL_CHECK_CAMPAIGN_NOTIFY_ME)
        val gqlQuery: String,
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<CheckCampaignNotifyMeModel>() {

    companion object {
        private const val KEY_PARAMS = "params"
        private const val DEFAULT_SOURCE_VALUE = "campaign"
        private const val DEFAULT_REQUEST_TYPE_VALUE = "CAMPAIGN"

        @JvmStatic
        fun createParams(
                campaignId: String = "",
                action: String = "",
                source: String = DEFAULT_SOURCE_VALUE
        ): Map<String, Any> {
            val paramsGetCampaignNotifyMe = CheckCampaignNotifyMeRequest(
                    campaignId.toIntOrZero(),
                    action,
                    source,
                    DEFAULT_REQUEST_TYPE_VALUE
            )
            return mapOf<String, Any>(
                    KEY_PARAMS to paramsGetCampaignNotifyMe
            )
        }
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): CheckCampaignNotifyMeModel {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CLOUD_THEN_CACHE).build())
        val gqlRequest = GraphqlRequest(gqlQuery, CheckCampaignNotifyMeModel.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
//        val gqlResponse = createMockGraphQlSuccessResponse()

        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<CheckCampaignNotifyMeModel.Response>(CheckCampaignNotifyMeModel.Response::class.java)
                    .checkCampaignNotifyMeModel
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }


    private fun createMockGraphQlSuccessResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                asdasd,
                JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = CheckCampaignNotifyMeModel.Response::class.java
        val obj: Any = Gson().fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }

    val asdasd = """
        {
  "data": {
    "checkCampaignNotifyMe": {
      "product_id": 15157113,
      "campaign_id": 441,
      "success": true,
      "message": "Kamu berhasil menambah produk ke Diskon Yang Dikejar",
      "error_message": ""
    }
  }
}
    """.trimIndent()


    private fun String.getJsonFromFile(): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(this)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }

}