package com.tokopedia.contactus.home.source.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.contactus.common.api.ContactUsURL;
import com.tokopedia.contactus.home.data.BuyerPurchaseData;
import com.tokopedia.contactus.home.data.ContactUsArticleResponse;
import com.tokopedia.contactus.home.data.TopBotStatus;
import com.tokopedia.contactus.orderquery.data.CreateTicketResult;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface ContactUsAPI {

    @GET(ContactUsURL.POPULAR_ARTICLE)
    Observable<Response<List<ContactUsArticleResponse>>> getPopularArticle();

    @GET(ContactUsURL.BUYER_LIST)
    Observable<Response<DataResponse<BuyerPurchaseData>>> getBuyerPurchaseList();

    @GET(ContactUsURL.SELLER_LIST)
    Observable<Response<DataResponse<BuyerPurchaseData>>> getSellerPurchaseList();

    @GET(ContactUsURL.TOPBOT_STATUS)
    Observable<Response<DataResponse<TopBotStatus>>> getTopBotStatus();


    @GET(ContactUsURL.PATH_GET_SOLUTION)
    Observable<Response<TkpdResponse>> getSolution(@Path("id") String id);

    @FormUrlEncoded
    @POST(ContactUsURL.PATH_CREATE_STEP_2)
    Observable<Response<DataResponse<CreateTicketResult>>> createTicket(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ContactUsURL.PATH_CREATE_STEP_1)
    Observable<Response<DataResponse<CreateTicketResult>>> createTicketValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ContactUsURL.PATH_COMMENT_RATING)
    Observable<Response<TkpdResponse>> commentRating(@FieldMap Map<String, String> params);
}