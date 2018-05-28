package com.tokopedia.contactus.orderquery.source.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.contactus.common.api.ContactUsURL;
import com.tokopedia.contactus.orderquery.data.Solutions;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public interface  OrderQueryApi {
    @FormUrlEncoded
    @POST(ContactUsURL.TICKET_OPTION_LIST)
    Observable<Response<DataResponse<Solutions>>> getTicketOptions(@FieldMap Map<String, Object> param);
}
