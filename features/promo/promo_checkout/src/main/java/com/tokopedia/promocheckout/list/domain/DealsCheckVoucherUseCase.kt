package com.tokopedia.promocheckout.list.domain

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.list.domain.mapper.DealsCheckoutMapper
import com.tokopedia.promocheckout.common.domain.model.deals.DealsErrorResponse
import com.tokopedia.promocheckout.common.domain.model.deals.DealsVerifyResponse
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.util.PromoCheckoutUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import retrofit2.HttpException
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * @author: astidhiyaa on 03/08/21.
 */
class DealsCheckVoucherUseCase @Inject constructor(val restRepository: RestRepository): RestRequestUseCase(restRepository) {
    val url = PromoCheckoutUrl.BASE_URL_EVENT_DEALS + PromoCheckoutUrl.EVENT_DEALS_POST_VERIFY_PATH
    var book :  HashMap<String, Boolean> = hashMapOf()
    var requestBody: JsonObject = JsonObject()

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, DealsVerifyResponse::class.java)
            .setRequestType(RequestType.POST)
            .setQueryParams(book)
            .setBody(requestBody)
            .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return restRepository.getResponses(restRequestList)
    }

    suspend fun execute(book: HashMap<String, Boolean>, body: JsonObject): Result<DataUiModel> {
        return try {
            val data = executeOnBackground()
            val convertedData = convertResponse(data)
            if(convertedData.message_error.isNotEmpty()){
                Fail(MessageErrorException(convertedData.data.message))
            }else{
                Success(DealsCheckoutMapper.mapData(convertedData))
            }
        }catch (throws: Throwable){
            val body = (throws as HttpException).response()?.errorBody().toString()
            if(!body.isNullOrEmpty()){
                val gson = Gson()
                val testModel = gson.fromJson(body, DealsErrorResponse::class.java)
                if(testModel.data.message.isNotEmpty()){
                    Fail(MessageErrorException(testModel.data.message))
                }else{
                    Fail(throws)
                }
            }else{
                Fail(throws)
            }
        }
    }

    fun createMapParam(book: Boolean): HashMap<String, Boolean> {
        val mapParam = HashMap<String, Boolean>()
        mapParam[BOOK] = book
        this.book = mapParam
        return mapParam
    }

    fun setDealsVerifyBody(body: JsonObject) : JsonObject {
        requestBody = body
        return requestBody
    }

    companion object {
        const val BOOK = "book"

        private fun convertResponse(typeRestResponseMap: Map<Type, RestResponse?>): DealsVerifyResponse {
            return typeRestResponseMap[DealsVerifyResponse::class.java]?.getData() as DealsVerifyResponse
        }
    }
}