package com.tokopedia.digital.common.data.source;

import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.widget.data.entity.status.StatusEntity;
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper;
import com.tokopedia.digital.widget.view.model.status.Status;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 19/01/18.
 */

public class StatusDataSource {

    private DigitalEndpointService digitalEndpointService;
    private StatusMapper statusMapper;

    public StatusDataSource(DigitalEndpointService digitalEndpointService, StatusMapper statusMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.statusMapper = statusMapper;
    }

    public Observable<Status> getStatus() {
        return digitalEndpointService.getApi()
                .getStatus()
                .map(new Func1<Response<TkpdDigitalResponse>, StatusEntity>() {
                    @Override
                    public StatusEntity call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tkpdDigitalResponseResponse.body().convertDataObj(StatusEntity.class);
                    }
                })
                .map(statusMapper);
    }
}
