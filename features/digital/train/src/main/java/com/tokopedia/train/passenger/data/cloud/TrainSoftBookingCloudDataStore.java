package com.tokopedia.train.passenger.data.cloud;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.passenger.data.cloud.entity.TrainSoftbookEntity;
import com.tokopedia.train.passenger.data.cloud.entity.TrainSoftbookWrapperEntity;
import com.tokopedia.train.passenger.domain.TrainSoftBookingMapper;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 24/07/18.
 */
public class TrainSoftBookingCloudDataStore {

    private TrainApi trainApi;
    private Context context;

    public TrainSoftBookingCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        this.trainApi = trainApi;
        this.context = context;
    }

    public Observable<TrainSoftbook> doSoftBookingTrainTicket(Specification specification) {
        String jsonQuery = getRequestStationPayload(((GqlNetworkSpecification) specification).rawFileNameQuery());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TrainUrl.QUERY_GQL, jsonQuery);
        requestParams.putObject(TrainUrl.VARIABLE_GQL, ((GqlNetworkSpecification) specification).mapVariable());

        return trainApi.doSoftBooking(requestParams.getParameters())
                .map(new Func1<DataResponse<TrainSoftbookWrapperEntity>, TrainSoftbookEntity>() {
                    @Override
                    public TrainSoftbookEntity call(DataResponse<TrainSoftbookWrapperEntity> trainSoftbookWrapperEntityDataResponse) {
                        return trainSoftbookWrapperEntityDataResponse.getData().getSoftbookEntity();
                    }
                })
                .map(new TrainSoftBookingMapper());
    }

    private String getRequestStationPayload(int rawFile) {
        return GraphqlHelper.loadRawString(context.getResources(), rawFile);
    }
}
