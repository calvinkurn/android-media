package com.tokopedia.recentview.domain.usecase;

import com.tokopedia.recentview.data.source.CloudRecentProductDataSource;
import com.tokopedia.recentview.domain.model.RecentViewProductDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Kulomady on 12/8/16.
 */

public class GetRecentViewUseCase extends UseCase<List<RecentViewProductDomain>> {
    public static final String PARAM_USER_ID = "user_id";

    private final CloudRecentProductDataSource cloudRecentProductDataSource;

    @Inject
    GetRecentViewUseCase(CloudRecentProductDataSource cloudRecentProductDataSource) {
        this.cloudRecentProductDataSource = cloudRecentProductDataSource;
    }


    @Override
    public Observable<List<RecentViewProductDomain>> createObservable(RequestParams requestParams) {
        return cloudRecentProductDataSource.getRecentProduct(requestParams);
    }

    public RequestParams getParam(String loginID) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, loginID);
        return params;
    }
}
