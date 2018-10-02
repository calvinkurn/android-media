package com.tokopedia.loginregister.login.domain;

import com.tokopedia.loginregister.login.view.model.DiscoverViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by nisie on 10/10/17.
 */

public class DiscoverUseCase extends UseCase<DiscoverViewModel> {
    @Override
    public Observable<DiscoverViewModel> createObservable(RequestParams requestParams) {
        return null;
    }

//    public static final String PARAM_TYPE = "type";
//    private static final String TYPE_REGISTER = "register";
//    private final CloudDiscoverDataSource cloudDiscoverDataSource;
//    private final LocalDiscoverDataSource localDiscoverDataSource;
//
//    @Inject
//    public DiscoverUseCase(CloudDiscoverDataSource cloudDiscoverDataSource,
//                           LocalDiscoverDataSource localDiscoverDataSource) {
//        this.cloudDiscoverDataSource = cloudDiscoverDataSource;
//        this.localDiscoverDataSource = localDiscoverDataSource;
//
//    }
//
//    public static RequestParams getParamRegister() {
//        RequestParams params = RequestParams.create();
//        params.putString(PARAM_TYPE,TYPE_REGISTER);
//        return params;
//    }
//
//    @Override
//    public Observable<DiscoverViewModel> createObservable(final RequestParams requestParams) {
//        return localDiscoverDataSource.getDiscover(requestParams.getString(PARAM_TYPE,""))
//                .onErrorResumeNext(new Func1<Throwable, Observable<DiscoverViewModel>>() {
//                    @Override
//                    public Observable<DiscoverViewModel> call(Throwable throwable) {
//                        return cloudDiscoverDataSource.getDiscover(requestParams);
//                    }
//                });
//    }
}
