package com.tokopedia.chatbot.data.network

import com.tokopedia.chatbot.attachinvoice.data.model.GetInvoicePostRequest
import com.tokopedia.chatbot.attachinvoice.data.model.GetInvoicesResponseWrapper
import com.tokopedia.chatbot.attachinvoice.domain.SendReasonRatingPojo
import com.tokopedia.chatbot.attachinvoice.domain.SetChatRatingPojo
import com.tokopedia.chatbot.data.network.ChatbotUrl.Companion.PATH_INVOICE_LIST
import com.tokopedia.chatbot.data.network.ChatbotUrl.Companion.PATH_SEND_REASON_RATING
import com.tokopedia.chatbot.data.network.ChatbotUrl.Companion.PATH_SET_RATING
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * @author : Steven 29/11/18
 */


interface ChatBotApi {

    @POST(PATH_SET_RATING)
    @Headers("Content-Type: application/json")
    fun setChatRating(@Body parameters: HashMap<String, Any>)
            : Observable<Response<DataResponse<SetChatRatingPojo>>>

    @POST(PATH_INVOICE_LIST)
    fun getTXOrderList(@QueryMap params : Map<String, String>, @Body body : GetInvoicePostRequest)
            : Observable<Response<GetInvoicesResponseWrapper>>

    @POST(PATH_SEND_REASON_RATING)
    @Headers( "Content-Type: application/json" )
    fun sendReasonRating(@Body requestParams : HashMap<String, Any>)
            : Observable<Response<DataResponse<SendReasonRatingPojo>>>

}

