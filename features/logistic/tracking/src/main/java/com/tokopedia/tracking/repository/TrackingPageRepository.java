package com.tokopedia.tracking.repository;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.logisticdata.data.apiservice.TrackingOrderApi;
import com.tokopedia.tracking.entity.TrackingResponse;
import com.tokopedia.tracking.mapper.ITrackingPageMapper;
import com.tokopedia.tracking.viewmodel.TrackingViewModel;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

public class TrackingPageRepository implements ITrackingPageRepository {

    private TrackingOrderApi trackingOrderApi;

    private ITrackingPageMapper mapper;

    public TrackingPageRepository(TrackingOrderApi trackingOrderApi, ITrackingPageMapper mapper) {
        this.trackingOrderApi = trackingOrderApi;
        this.mapper = mapper;
    }

    @Override
    public Observable<TrackingViewModel> getRates(Map<String, String> trackParameters) {
        return trackingOrderApi.trackOrder(trackParameters).map(
                new Func1<Response<TkpdResponse>, TrackingViewModel>() {
                    @Override
                    public TrackingViewModel call(Response<TkpdResponse> tkpdResponse) {
                        if(tkpdResponse.body() == null)
                            throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);

                        return mapper.trackingViewModel(
                                tkpdResponse.body()
                                        .convertDataObj(TrackingResponse.class)
                        );
                    }
                });
    }
}
