package com.tokopedia.tkpd.tkpdcontactus.orderquery.source.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.tkpdcontactus.common.api.ContactUsURL;
import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.Solutions;
import com.tokopedia.usecase.RequestParams;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public interface  OrderQueryApi {
    @FormUrlEncoded
    @POST(ContactUsURL.TICKET_OPTION_LIST)
    Observable<Response<DataResponse<Solutions>>> getTicketOptions(@FieldMap Map<String, Object> param);
}
