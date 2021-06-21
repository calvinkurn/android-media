package com.tokopedia.loginregister.login.data;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase;
import com.tokopedia.loginregister.discover.mapper.DiscoverMapper;
import com.tokopedia.loginregister.login.view.model.DiscoverDataModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 10/10/17.
 */

public class CloudDiscoverDataSource {

    private LoginRegisterApi loginRegisterApi;
    private DiscoverMapper discoverMapper;

    @Inject
    public CloudDiscoverDataSource(LoginRegisterApi loginRegisterApi,
                                   DiscoverMapper discoverMapper) {
        this.loginRegisterApi = loginRegisterApi;
        this.discoverMapper = discoverMapper;
    }

    public Observable<DiscoverDataModel> getDiscover(RequestParams params) {
        return loginRegisterApi
                .discoverLogin(params.getParameters())
                .map(discoverMapper)
                .doOnNext(saveToCache(params.getString(DiscoverUseCase.PARAM_TYPE, "")));
    }

    private Action1<DiscoverDataModel> saveToCache(final String source) {
        return discoverDataModel -> {
            if (discoverDataModel != null) {
            }
        };
    }
}
