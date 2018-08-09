package com.tokopedia.train.search.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.search.data.entity.AvailabilityEntity;
import com.tokopedia.train.search.data.entity.ScheduleAvailabilityResponse;
import com.tokopedia.train.search.data.entity.ScheduleEntity;
import com.tokopedia.train.search.data.entity.SearchDataResponse;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainScheduleCloudDataStore {

    private TrainApi trainApi;
    private Context context;

    public TrainScheduleCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        this.trainApi = trainApi;
        this.context = context;
    }

    public Observable<List<ScheduleEntity>> getDatasSchedule(Specification specification) {
        RequestParams requestParams = getRequestParamGql(specification);
        return trainApi.schedulesTrain(requestParams.getParameters())
                .map(new Func1<DataResponse<SearchDataResponse>, List<ScheduleEntity>>() {
                    @Override
                    public List<ScheduleEntity> call(DataResponse<SearchDataResponse> searchDataResponseDataResponse) {
                        return searchDataResponseDataResponse.getData().getSearchSchedule().getSchedules();
                    }
                });
    }

    public Observable<List<AvailabilityEntity>> getDatasAvailability(Specification specification) {
        RequestParams requestParams = getRequestParamGql(specification);
        return trainApi.availabilityTrain(requestParams.getParameters())
                .map(new Func1<DataResponse<ScheduleAvailabilityResponse>, List<AvailabilityEntity>>() {
                    @Override
                    public List<AvailabilityEntity> call(DataResponse<ScheduleAvailabilityResponse> scheduleAvailabilityResponseDataResponse) {
                        return scheduleAvailabilityResponseDataResponse.getData().getSearchAvailability().getAvailabilities();
                    }
                });
    }

    private RequestParams getRequestParamGql(Specification specification) {
        String jsonQuery = getRequestStationPayload(((GqlNetworkSpecification) specification).rawFileNameQuery());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TrainUrl.QUERY_GQL, jsonQuery);
        if (((GqlNetworkSpecification) specification).mapVariable() != null) {
            requestParams.putObject(TrainUrl.VARIABLE_GQL, ((GqlNetworkSpecification) specification).mapVariable());
        }
        return requestParams;
    }

    private String getRequestStationPayload(int rawFile) {
        return GraphqlHelper.loadRawString(context.getResources(), rawFile);
    }
}
