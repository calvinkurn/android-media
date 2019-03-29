package com.tokopedia.tokocash.autosweepmf.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.tokocash.autosweepmf.data.mapper.AutoSweepLimitMapperEntity;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.data.source.AutoSweepLimitDataStore;
import com.tokopedia.tokocash.autosweepmf.data.source.cloud.api.AutoSweepApi;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class AutoSweepLimitDataCloud implements AutoSweepLimitDataStore {
    private AutoSweepApi mApi;
    private AutoSweepLimitMapperEntity mMapper;

    @Inject
    public AutoSweepLimitDataCloud(AutoSweepApi api, AutoSweepLimitMapperEntity mapper) {
        this.mApi = api;
        this.mMapper = mapper;
    }


    @Override
    public Observable<AutoSweepLimitDomain> autoSweepLimit(RequestParams requestParams) {
        return mApi.postAutoSweepLimit(requestParams.getParameters()).map(new Func1<Response<GraphqlResponse<ResponseAutoSweepLimit>>, AutoSweepLimitDomain>() {
            @Override
            public AutoSweepLimitDomain call(Response<GraphqlResponse<ResponseAutoSweepLimit>> response) {
                if (response.isSuccessful()) {
                    return mMapper.transform(response.body().getData().getMfAutoSweepUpdate());
                } else {
                    throw new RuntimeException(
                            ErrorNetMessage.MESSAGE_ERROR_DEFAULT
                    );
                }
            }
        });
    }
}
