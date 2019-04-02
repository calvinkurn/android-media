package com.tokopedia.train.seat.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.seat.data.entity.TrainChangeSeatEntity;
import com.tokopedia.train.seat.data.entity.TrainChangeSeatsDataEntity;
import com.tokopedia.train.seat.data.entity.TrainKaiSeatMapEntity;
import com.tokopedia.train.seat.data.entity.TrainSeatMapEntity;
import com.tokopedia.train.seat.data.specification.TrainChangeSeatSpecification;
import com.tokopedia.train.seat.domain.model.TrainPassengerSeat;
import com.tokopedia.usecase.RequestParams;

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

    public Observable<List<TrainChangeSeatEntity>> changeSeats(TrainChangeSeatSpecification specification) {
        String jsonQuery = getRequestStationPayload(((GqlNetworkSpecification) specification).rawFileNameQuery());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TrainUrl.QUERY_GQL, jsonQuery);
        if (((GqlNetworkSpecification) specification).mapVariable() != null) {
            requestParams.putObject(TrainUrl.VARIABLE_GQL, ((GqlNetworkSpecification) specification).mapVariable());
        }

        return trainApi
                .changeSeats(requestParams.getParameters())
                .map(new Func1<DataResponse<TrainChangeSeatsDataEntity>, List<TrainChangeSeatEntity>>() {
                    @Override
                    public List<TrainChangeSeatEntity> call(DataResponse<TrainChangeSeatsDataEntity> stringDataResponse) {
                        return stringDataResponse.getData().getSeats().getSeats();
                    }
                });
    }
}
