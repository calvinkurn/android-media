package com.tokopedia.train.seat.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.seat.data.entity.TrainSeatMapEntity;
import com.tokopedia.train.seat.data.entity.TrainSeatsEntity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;

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
        Gson g = new Gson();
        Type dataResponseType = new TypeToken<DataResponse<TrainSeatsEntity>>() {
        }.getType();
        DataResponse<TrainSeatsEntity> dataResponse = g.fromJson(loadJSONFromAsset(), dataResponseType);

        return Observable.just(dataResponse.getData().getSeatMapEntities());
        /*
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
        });*/
    }

    private String getRequestStationPayload(int rawFile) {
        return GraphqlHelper.loadRawString(context.getResources(), rawFile);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("train_seat_maps.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
