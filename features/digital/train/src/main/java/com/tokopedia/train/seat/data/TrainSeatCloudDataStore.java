package com.tokopedia.train.seat.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.seat.data.entity.TrainKaiSeatMapEntity;
import com.tokopedia.train.seat.data.entity.TrainSeatMapEntity;
import com.tokopedia.usecase.RequestParams;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class TrainSeatCloudDataStore {

    private TrainApi trainApi;
    private Context context;

    public TrainSeatCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        this.trainApi = trainApi;
        this.context = context;
    }

    public Observable<List<TrainSeatMapEntity>> getData(Specification specification) {
        String jsonQuery = getRequestStationPayload(((GqlNetworkSpecification) specification).rawFileNameQuery());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TrainUrl.QUERY_GQL, jsonQuery);
        if (((GqlNetworkSpecification) specification).mapVariable() != null) {
            requestParams.putObject(TrainUrl.VARIABLE_GQL, ((GqlNetworkSpecification) specification).mapVariable());
        }

        return trainApi
                .seats(requestParams.getParameters())
                .map(new Func1<DataResponse<TrainKaiSeatMapEntity>, List<TrainSeatMapEntity>>() {
                    @Override
                    public List<TrainSeatMapEntity> call(DataResponse<TrainKaiSeatMapEntity> trainKaiSeatMapEntityDataResponse) {
                        return trainKaiSeatMapEntityDataResponse.getData().getKaiSeatMap().getSeatMapEntities();
                    }
                });
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
