package com.tokopedia.train.seat.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.seat.data.entity.TrainSeatMapEntity;
import com.tokopedia.train.seat.data.entity.TrainSeatsEntity;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class TrainSeatCloudDataStore {
    private static final String QUERY_GQL = "query";
    private static final String VARIABLE_GQL = "query";

    private TrainApi trainApi;
    private Context context;

    public TrainSeatCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        this.trainApi = trainApi;
        this.context = context;
    }

    public Observable<List<TrainSeatMapEntity>> getData(Specification specification) {
        String jsonQuery = getRequestStationPayload(((GqlNetworkSpecification) specification).rawFileNameQuery());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(QUERY_GQL, jsonQuery);
        if (((GqlNetworkSpecification) specification).mapVariable() != null) {
            requestParams.putObject(VARIABLE_GQL, ((GqlNetworkSpecification) specification).mapVariable());
        }

        return trainApi.seats(requestParams.getParameters()).map(new Func1<DataResponse<TrainSeatsEntity>, List<TrainSeatMapEntity>>() {
            @Override
            public List<TrainSeatMapEntity> call(DataResponse<TrainSeatsEntity> trainSeatEntityDataResponse) {
                return trainSeatEntityDataResponse.getData().getSeatMapEntities();
            }
        });
    }

    private String getRequestStationPayload(int rawFile) {
        return GraphqlHelper.loadRawString(context.getResources(), rawFile);
    }
}
