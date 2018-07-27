package com.tokopedia.train.reviewdetail.data;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.reviewdetail.data.entity.TrainCheckVoucherEntity;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by Rizky on 23/07/18.
 */
public class TrainCheckVoucherCloudDataStore {

    private TrainApi trainApi;
    private Context context;

    public TrainCheckVoucherCloudDataStore(TrainApi trainApi, Context context) {
        this.trainApi = trainApi;
        this.context = context;
    }

    public Observable<TrainCheckVoucherEntity> getData(Specification specification) {
        String jsonQuery = getRequestStationPayload(((GqlNetworkSpecification) specification).rawFileNameQuery());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TrainUrl.QUERY_GQL, jsonQuery);
        if (((GqlNetworkSpecification) specification).mapVariable() != null) {
            requestParams.putObject(TrainUrl.VARIABLE_GQL, ((GqlNetworkSpecification) specification).mapVariable());
        }

        return trainApi.checkVoucher(requestParams.getParameters())
                .map(trainCheckVoucherWrapperEntityDataResponse -> trainCheckVoucherWrapperEntityDataResponse
                        .getData().getTrainCheckVoucherEntity());
    }

    private String getRequestStationPayload(int rawFile) {
        return GraphqlHelper.loadRawString(context.getResources(), rawFile);
    }

}