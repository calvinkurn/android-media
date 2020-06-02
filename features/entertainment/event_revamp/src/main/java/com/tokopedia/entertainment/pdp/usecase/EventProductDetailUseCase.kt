package com.tokopedia.entertainment.pdp.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.entertainment.pdp.data.EventContentByIdEntity
import com.tokopedia.entertainment.pdp.data.EventProductDetailEntity
import com.tokopedia.entertainment.pdp.data.EventPDPContentCombined
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject


class EventProductDetailUseCase @Inject constructor(private val useCase: MultiRequestGraphqlUseCase){

    suspend fun executeUseCase(rawQueryPDP: String, rawQueryContent: String, fromCloud: Boolean = false, urlPdp:String, dummyResponse: String): Result<EventPDPContentCombined> {
        try {
            useCase.clearRequest()
            useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(if (fromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            val pdpRequest = GraphqlRequest(rawQueryPDP, EventProductDetailEntity::class.java, mapOf(URL_PDP to urlPdp))
            useCase.addRequest(pdpRequest)

            //val pdpData = useCase.executeOnBackground().getSuccessData<EventProductDetailEntity>()


            val gson = Gson()
            val pdpData = gson.fromJson(dummyResponse, EventProductDetailEntity::class.java)

            val contentRequest = GraphqlRequest(rawQueryContent, EventContentByIdEntity::class.java, mapOf(TYPE_VALUE to pdpData.eventProductDetail.productDetailData.id))
            useCase.addRequest(contentRequest)
            val contentData = useCase.executeOnBackground().getSuccessData<EventContentByIdEntity>()

            return Success(combineProductandContent(pdpData,contentData))
        }catch (throwable : Throwable){
            return Fail(throwable)
        }
    }

    private fun combineProductandContent(pdp:EventProductDetailEntity, content: EventContentByIdEntity):EventPDPContentCombined{
        return EventPDPContentCombined(content,pdp)
    }

    companion object{
        const val URL_PDP = "urlPDP"
        const val TYPE_VALUE = "typeValue"
    }
}