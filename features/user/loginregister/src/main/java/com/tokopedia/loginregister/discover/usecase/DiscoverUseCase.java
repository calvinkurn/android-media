package com.tokopedia.loginregister.discover.usecase;

import com.tokopedia.loginregister.login.data.CloudDiscoverDataSource;
import com.tokopedia.loginregister.login.data.LocalDiscoverDataSource;
import com.tokopedia.loginregister.login.view.model.DiscoverDataModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/10/17.
 */

public class DiscoverUseCase extends UseCase<DiscoverDataModel> {

    public static final String PARAM_TYPE = "type";
    private static final String TYPE_REGISTER = "register";
    private final CloudDiscoverDataSource cloudDiscoverDataSource;
    private final LocalDiscoverDataSource localDiscoverDataSource;

    @Inject
    public DiscoverUseCase(CloudDiscoverDataSource cloudDiscoverDataSource,
                           LocalDiscoverDataSource localDiscoverDataSource) {
        this.cloudDiscoverDataSource = cloudDiscoverDataSource;
        this.localDiscoverDataSource = localDiscoverDataSource;

    }

    public static RequestParams getParamRegister() {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_TYPE,TYPE_REGISTER);
        return params;
    }

    @Override
    public Observable<DiscoverDataModel> createObservable(final RequestParams requestParams) {
        return localDiscoverDataSource.getDiscover(requestParams.getString(PARAM_TYPE,""))
                .onErrorResumeNext(new Func1<Throwable, Observable<DiscoverDataModel>>() {
                    @Override
                    public Observable<DiscoverDataModel> call(Throwable throwable) {
                        return cloudDiscoverDataSource.getDiscover(requestParams);
                    }
                });
    }
}
