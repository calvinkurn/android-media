package com.tokopedia.events.data;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
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
import com.tokopedia.events.data.source.EventsApi;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public class CloudEventsDataStore implements EventDataStore {

    private final EventsApi eventsApi;

    public CloudEventsDataStore(EventsApi eventsApi) {
        this.eventsApi = eventsApi;
    }


    @Override
    public Observable<EventResponseEntity> getEvents(HashMap<String, Object> params) {
        return eventsApi.getEvents().map(new DataResponseMapper<>());
    }

    @Override
    public Observable<SearchResponse> getSearchEvents(HashMap<String, Object> params) {
        return eventsApi.getSearchEvents(params).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<SearchResponse> getSearchNext(String nextUrl) {
        return eventsApi.getSearchNext(nextUrl).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<EventLocationEntity> getEventsLocationList(HashMap<String, Object> params) {
        return eventsApi.getEventsLocationList().map(new DataResponseMapper<>());
    }

    @Override
    public Observable<EventResponseEntity> getEventsListByLocation(String location) {
        return eventsApi.getEventsByLocation(location).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<EventsDetailsEntity> getEventDetails(String url) {
        return eventsApi.getEventDetails(url).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<ValidateResponse> validateShow(JsonObject requestBody) {
        return eventsApi.validateShow(requestBody).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<VerifyCartResponse> verifyCart(JsonObject requestBody, boolean flag) {
        return eventsApi.postCartVerify(requestBody, flag).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<VerifyCartResponse> postCouponInit(JsonObject requestBody) {
        return eventsApi.postCouponInit(requestBody).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<CheckoutResponse> checkoutCart(JsonObject requestBody) {
        return eventsApi.checkoutCart(requestBody).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<SeatLayoutResponse> getSeatLayout(int category_id,
                                                        int product_id,
                                                        int schedule_id,
                                                        int group_id,
                                                        int package_id) {
        return eventsApi.getSeatLayout(category_id, product_id, schedule_id, group_id, package_id).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<List<SeatLayoutItem>> getEventSeatLayout(String url) {
        return eventsApi.getEventSeatLayout(url).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<LikeUpdateResponse> updateLikes(JsonObject requestBody) {
        return eventsApi.updateLikes(requestBody).map(new DataResponseMapper<>());
    }

    @Override
    public Observable<List<UserLikesResponse>> getUserLikes() {
        return eventsApi.getUserLikesProduct().map(new DataResponseMapper<>());
    }

    @Override
    public Observable<ProductRatingResponse> getProductRating(int id) {
        return eventsApi.getProductLike(id).map(new DataResponseMapper<>());
    }


}
