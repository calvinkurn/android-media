package com.tokopedia.logisticCommon.data.repository;

import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.logisticCommon.data.apiservice.TrackingOrderApi;
import com.tokopedia.logisticCommon.data.entity.trackingshipment.TrackingResponse;

import java.util.Map;

import rx.Observable;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

public class TrackingPageRepository implements ITrackingPageRepository {

    private TrackingOrderApi trackingOrderApi;

    public TrackingPageRepository(TrackingOrderApi trackingOrderApi) {
        this.trackingOrderApi = trackingOrderApi;
    }

    @Override
    public Observable<TrackingResponse> getRates(Map<String, String> trackParameters) {
        return trackingOrderApi.trackOrder(trackParameters)
                .map(tokopediaWsV4ResponseResponse -> {
                    if (tokopediaWsV4ResponseResponse == null)
                        throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    return tokopediaWsV4ResponseResponse.body()
                            .convertDataObj(TrackingResponse.class);
                });

    }
}
