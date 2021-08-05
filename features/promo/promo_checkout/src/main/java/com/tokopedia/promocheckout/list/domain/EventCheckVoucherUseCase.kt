package com.tokopedia.promocheckout.list.domain

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.util.PromoCheckoutUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.promocheckout.list.domain.mapper.EventCheckVoucherMapper.mapDataEvent
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * @author: astidhiyaa on 02/08/21.
 */
class EventCheckVoucherUseCase @Inject constructor(val restRepository: RestRepository): RestRequestUseCase(restRepository) {
    val url = PromoCheckoutUrl.BASE_URL_EVENT_DEALS + PromoCheckoutUrl.EVENT_DEALS_POST_VERIFY_PATH
    var book :  HashMap<String, Boolean> = hashMapOf()
    var requestBody: EventVerifyBody = EventVerifyBody()

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, EventVerifyResponse::class.java)
            .setRequestType(RequestType.POST)
            .setQueryParams(book)
            .setBody(requestBody)
            .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return restRepository.getResponses(restRequestList)
    }

    suspend fun execute(book: HashMap<String, Boolean>, body: EventVerifyBody): Result<DataUiModel>{
        return try {
            val data = executeOnBackground()
            val values = data[EventVerifyResponse::class.java]
            val convertedData = convertResponse(data)
            if(values?.code == SUCCESS_CODE && !values.isError){
                Success(mapDataEvent(convertedData))
            }else{
                Fail(MessageErrorException(convertedData.message_error[0]))
            }
        }catch (throws: Throwable){
            Fail(throws)
        }
    }

    fun createMapParam(book: Boolean): HashMap<String, Boolean> {
        val mapParam = HashMap<String, Boolean>()
        mapParam[BOOK] = book
        this.book = mapParam
        return mapParam
    }

    fun setEventVerifyBody(body: EventVerifyBody) : EventVerifyBody{
        requestBody = body
        return requestBody
    }

    companion object {
        const val BOOK = "book"
        private const val SUCCESS_CODE = 200

        private fun convertResponse(typeRestResponseMap: Map<Type, RestResponse?>): EventVerifyResponse {
            return typeRestResponseMap[EventVerifyResponse::class.java]?.getData() as EventVerifyResponse
        }
    }
}