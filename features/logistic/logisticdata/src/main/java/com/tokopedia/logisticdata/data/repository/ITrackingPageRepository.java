package com.tokopedia.logisticdata.data.repository;

import com.tokopedia.logisticdata.data.entity.trackingshipment.TrackingResponse;

import java.util.Map;

import rx.Observable;


/**
 * Created by kris on 5/14/18. Tokopedia
 */

public interface ITrackingPageRepository {

    Observable<TrackingResponse> getRates(Map<String, String> trackParameters);

}
