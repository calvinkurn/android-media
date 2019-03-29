package com.tokopedia.train.station.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.constant.TrainUrl;
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

    private TrainApi trainApi;
    private Context context;

    public TrainStationCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        this.trainApi = trainApi;
        this.context = context;
    }

    public Observable<List<TrainStationIslandEntity>> getDatas() {
        String jsonQuery = getRequestStationPayload();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TrainUrl.QUERY_GQL, jsonQuery);

        return trainApi.stationsInIsland(requestParams.getParameters()).map(new Func1<DataResponse<StationDataEntity>, List<TrainStationIslandEntity>>() {
            @Override
            public List<TrainStationIslandEntity> call(DataResponse<StationDataEntity> response) {
                return response.getData().getStations();
            }
        });
    }

    private String getRequestStationPayload() {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.kai_station_query);
    }
}
