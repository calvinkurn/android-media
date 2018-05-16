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
                    public TrackingViewModel call(Response<TkpdResponse> tkpdResponseResponse) {
                        if(tkpdResponseResponse.body() == null) {
                            throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        } else if (tkpdResponseResponse.body().isError()) {
                            throw new RuntimeException(
                                    tkpdResponseResponse.body().getErrorMessageJoined()
                            );
                        }
                        return mapper.trackingViewModel(
                                tkpdResponseResponse.body()
                                        .convertDataObj(TrackingResponse.class)
                        );
                    }
                });
    }
}
