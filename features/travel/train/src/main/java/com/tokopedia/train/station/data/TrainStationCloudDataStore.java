package com.tokopedia.train.station.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.station.data.entity.StationDataEntity;
import com.tokopedia.train.station.data.entity.TrainStationIslandEntity;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationCloudDataStore {
    private static final String QUERY_GQL = "query";
    private static final String VARIABLE_GQL = "query";

    private TrainApi trainApi;
    private Context context;

    public TrainStationCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        this.trainApi = trainApi;
        this.context = context;
    }

    public Observable<List<TrainStationIslandEntity>> getDatas(Specification specification) {
        String jsonQuery = getRequestStationPayload(((GqlNetworkSpecification) specification).rawFileNameQuery());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(QUERY_GQL, jsonQuery);
        if (((GqlNetworkSpecification) specification).mapVariable() != null) {
            requestParams.putObject(VARIABLE_GQL, ((GqlNetworkSpecification) specification).mapVariable());
        }

        return trainApi.stationsInIsland(requestParams.getParameters()).map(new Func1<DataResponse<StationDataEntity>, List<TrainStationIslandEntity>>() {
            @Override
            public List<TrainStationIslandEntity> call(DataResponse<StationDataEntity> response) {
                return response.getData().getStations();
            }
        });
    }

    public Observable<TrainStationIslandEntity> getData(Specification specification) {
        return null;
    }

    private String getRequestStationPayload(int rawFile) {
        return GraphqlHelper.loadRawString(context.getResources(), rawFile);
    }
}
