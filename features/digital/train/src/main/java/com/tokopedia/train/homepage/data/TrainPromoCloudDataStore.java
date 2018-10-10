package com.tokopedia.train.homepage.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.homepage.data.entity.BannerDetail;
import com.tokopedia.train.homepage.data.entity.TrainBannerEntity;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class TrainPromoCloudDataStore {
    private TrainApi trainApi;
    private Context context;

    public TrainPromoCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        this.trainApi = trainApi;
        this.context = context;
    }

    public Observable<List<BannerDetail>> getData(Specification specification) {
        String jsonQuery = getRequestStationPayload(((GqlNetworkSpecification) specification).rawFileNameQuery());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TrainUrl.QUERY_GQL, jsonQuery);
        if (((GqlNetworkSpecification) specification).mapVariable() != null) {
            requestParams.putObject(TrainUrl.VARIABLE_GQL, ((GqlNetworkSpecification) specification).mapVariable());
        }

        return trainApi
                .banners(requestParams.getParameters())
                .map(new Func1<DataResponse<TrainBannerEntity>, List<BannerDetail>>() {
                    @Override
                    public List<BannerDetail> call(DataResponse<TrainBannerEntity> trainBannerEntityDataResponse) {
                        return trainBannerEntityDataResponse.getData().getBanners();
                    }
                });

    }

    private String getRequestStationPayload(int rawFile) {
        return GraphqlHelper.loadRawString(context.getResources(), rawFile);
    }
}
