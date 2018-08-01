package com.tokopedia.topchat.chatroom.data.network;


import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topchat.attachinvoice.data.model.GetInvoicePostRequest;
import com.tokopedia.topchat.attachinvoice.data.model.GetInvoicesResponseWrapper;
import com.tokopedia.topchat.chatroom.domain.pojo.rating.SendReasonRatingPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.rating.SetChatRatingPojo;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by alvinatin on 27/03/18.
 */

public interface ChatBotApi {

    String PATH_SET_RATING = "/cs/chatbot/v2/post-rating";
    String PATH_INVOICE_LIST = "/cs/chatbot/invoice-list";
    String PATH_SEND_REASON_RATING = "/cs/chatbot/api/rating/post-reason";

    @POST(PATH_SET_RATING)
    @Headers({"Content-Type: application/json"})
    Observable<Response<DataResponse<SetChatRatingPojo>>>
    setChatRating(@Body HashMap<String, Object> requestParams);


    @POST(PATH_INVOICE_LIST)
    Observable<Response<GetInvoicesResponseWrapper>>
    getTXOrderList(@QueryMap Map<String, String> params, @Body GetInvoicePostRequest body);

    @POST(PATH_SEND_REASON_RATING)
    @Headers({"Content-Type: application/json"})
    Observable<Response<DataResponse<SendReasonRatingPojo>>>
    sendReasonRating(@Body HashMap<String, Object> requestParams);
}
