package com.tokopedia.topchat.common.chat.api;

import com.google.gson.JsonObject;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListUiModel;
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface ChatApi {
    @Headers("Content-Type: application/json")
    @POST(TkpdBaseURL.Chat.DELETE)
    Observable<Response<DataResponse<DeleteChatListUiModel>>> deleteMessage(@Body JsonObject
                                                                                    parameters);

    @GET(TkpdBaseURL.Chat.GET_TEMPLATE)
    Observable<Response<DataResponse<TemplateData>>> getTemplate(@QueryMap Map<String, Object>
                                                                         parameters);

    @FormUrlEncoded
    @PUT(TkpdBaseURL.Chat.UPDATE_TEMPLATE)
    Observable<Response<DataResponse<TemplateData>>> editTemplate(@Path("index") int index,
                                                                  @FieldMap Map<String, Object> jsonObject,
                                                                  @Query("is_seller") boolean isSeller);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Chat.CREATE_TEMPLATE)
    Observable<Response<DataResponse<TemplateData>>> createTemplate(@FieldMap Map<String, Object>
                                                                            parameters);

    @Headers("Content-Type: application/json")
    @PUT(TkpdBaseURL.Chat.SET_TEMPLATE)
    Observable<Response<DataResponse<TemplateData>>> setTemplate(@Body JsonObject parameters, @Query("is_seller") boolean isSeller);

    @HTTP(method = "DELETE", path = TkpdBaseURL.Chat.DELETE_TEMPLATE + "/{is_seller}", hasBody = true)
    Observable<Response<DataResponse<TemplateData>>> deleteTemplate(@Path("index") int index
            , @Path("is_seller") boolean isSeller
            , @Body JsonObject object);
}
