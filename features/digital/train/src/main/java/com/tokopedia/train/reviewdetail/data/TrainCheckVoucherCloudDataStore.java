package com.tokopedia.train.reviewdetail.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Func1;

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

    public Observable<TrainPromoEntity> getData(Specification specification) {
        String jsonQuery = getRequestStationPayload(((GqlNetworkSpecification) specification).rawFileNameQuery());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TrainUrl.QUERY_GQL, jsonQuery);
        if (((GqlNetworkSpecification) specification).mapVariable() != null) {
            requestParams.putObject(TrainUrl.VARIABLE_GQL, ((GqlNetworkSpecification) specification).mapVariable());
        }

        return trainApi.checkVoucher(requestParams.getParameters())
                .map(trainKaiSeatMapEntityDataResponse -> trainKaiSeatMapEntityDataResponse
                        .getData().getTrainCheckGalaEntity().getTrainPromoEntity());
    }

    private String getRequestStationPayload(int rawFile) {
        return GraphqlHelper.loadRawString(context.getResources(), rawFile);
    }

}