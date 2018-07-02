package com.tokopedia.instantloan.domain.interactor;

import com.tokopedia.common.network.data.model.CacheType;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestCacheStrategy;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity;
import com.tokopedia.instantloan.data.repository.InstantLoanDataRepository;
import com.tokopedia.instantloan.network.InstantLoanUrl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by lavekush on 21/03/18.
 */

public class GetLoanProfileStatusUseCase extends RestRequestUseCase/*UseCase<UserProfileLoanEntity>*/ {
    private final InstantLoanDataRepository mRepository;

    @Inject
    public GetLoanProfileStatusUseCase(InstantLoanDataRepository repository) {
        this.mRepository = repository;
    }

    /*@Override
    public Observable<UserProfileLoanEntity> createObservable(RequestParams requestParams) {
        return mRepository.getLoanProfileStatus();
    }*/

    @Override
    protected List<RestRequest> buildRequest() {

        List<RestRequest> restRequestList = new ArrayList<>();
        RestCacheStrategy restCacheStrategy = new RestCacheStrategy.Builder(CacheType.CACHE_FIRST).build();
        RestRequest restRequest1 = new RestRequest.Builder(InstantLoanUrl.PATH_USER_PROFILE_STATUS, UserProfileLoanEntity.class)
                .setRequestType(RequestType.GET)
                .setCacheStrategy(restCacheStrategy)
                .build();
        restRequestList.add(restRequest1);

        return restRequestList;
    }
}
