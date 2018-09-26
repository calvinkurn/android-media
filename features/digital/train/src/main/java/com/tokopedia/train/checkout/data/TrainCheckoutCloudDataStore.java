package com.tokopedia.train.checkout.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.train.checkout.data.entity.TrainCheckoutEntity;
import com.tokopedia.train.checkout.data.entity.TrainCheckoutWrapperEntity;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 26/07/18.
 */
public class TrainCheckoutCloudDataStore {

    private TrainApi trainApi;
    private Context context;

    public TrainCheckoutCloudDataStore(TrainApi trainApi, Context context) {
        this.trainApi = trainApi;
        this.context = context;
    }

    public Observable<TrainCheckoutEntity> checkout(Specification specification) {
        String jsonQuery = getRequestStationPayload(((GqlNetworkSpecification) specification).rawFileNameQuery());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TrainUrl.QUERY_GQL, jsonQuery);
        if (((GqlNetworkSpecification) specification).mapVariable() != null) {
            requestParams.putObject(TrainUrl.VARIABLE_GQL, ((GqlNetworkSpecification) specification).mapVariable());
        }

        return trainApi.checkout(requestParams.getParameters())
                .map(new Func1<DataResponse<TrainCheckoutWrapperEntity>, TrainCheckoutEntity>() {
                    @Override
                    public TrainCheckoutEntity call(DataResponse<TrainCheckoutWrapperEntity> trainCheckoutEntityDataResponse) {
                        return trainCheckoutEntityDataResponse.getData().getTrainCheckoutEntity();
                    }
                });
    }

    private String getRequestStationPayload(int rawFile) {
        return GraphqlHelper.loadRawString(context.getResources(), rawFile);
    }

}
