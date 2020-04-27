package com.tokopedia.events.data;

import com.google.gson.JsonObject;
import com.tokopedia.events.data.entity.response.EventLocationEntity;
import com.tokopedia.events.data.entity.response.EventResponseEntity;
import com.tokopedia.events.data.entity.response.EventsDetailsEntity;
import com.tokopedia.events.data.entity.response.LikeUpdateResponse;
import com.tokopedia.events.data.entity.response.ProductRatingResponse;
import com.tokopedia.events.data.entity.response.SeatLayoutItem;
import com.tokopedia.events.data.entity.response.UserLikesResponse;
import com.tokopedia.events.data.entity.response.ValidateResponse;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.events.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.events.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by ashwanityagi on 07/11/17.
 */

public interface EventDataStore {

    Observable<EventResponseEntity> getEvents(HashMap<String, Object> params);

    Observable<SearchResponse> getSearchEvents(HashMap<String, Object> params);

    Observable<SearchResponse> getSearchNext(String nextUrl);

    Observable<EventLocationEntity> getEventsLocationList(HashMap<String, Object> params);

    Observable<EventResponseEntity> getEventsListByLocation(String location);

    Observable<EventsDetailsEntity> getEventDetails(String url);

    Observable<ValidateResponse> validateShow(JsonObject requestBody);

    Observable<VerifyCartResponse> verifyCart(JsonObject requestBody, boolean flag);

    Observable<VerifyCartResponse> postCouponInit(JsonObject requestBody);

    Observable<CheckoutResponse> checkoutCart(JsonObject requestBody);

    Observable<SeatLayoutResponse> getSeatLayout(int category_id,
                                                 int product_id,
                                                 int schedule_id,
                                                 int group_id,
                                                 int package_id);

    Observable<List<SeatLayoutItem>> getEventSeatLayout(String url);

    Observable<LikeUpdateResponse> updateLikes(JsonObject requestBody);

    Observable<List<UserLikesResponse>> getUserLikes();

    Observable<ProductRatingResponse> getProductRating(int id);
}
