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
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface ChatApi {
    @Headers("Content-Type: application/json")
    @POST(TkpdBaseURL.Chat.DELETE)
    Observable<Response<DataResponse<DeleteChatListUiModel>>> deleteMessage(@Body JsonObject
                                                                                    parameters);
}
