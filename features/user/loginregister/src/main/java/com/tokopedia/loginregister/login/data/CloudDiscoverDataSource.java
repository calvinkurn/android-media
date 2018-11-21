package com.tokopedia.loginregister.login.data;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase;
import com.tokopedia.loginregister.discover.mapper.DiscoverMapper;
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 10/10/17.
 */

public class CloudDiscoverDataSource {

    private final CacheManager globalCacheManager;
    private LoginRegisterApi loginRegisterApi;
    private DiscoverMapper discoverMapper;

    @Inject
    public CloudDiscoverDataSource(CacheManager globalCacheManager,
                                   LoginRegisterApi loginRegisterApi,
                                   DiscoverMapper discoverMapper) {
        this.globalCacheManager = globalCacheManager;
        this.loginRegisterApi = loginRegisterApi;
        this.discoverMapper = discoverMapper;
    }

    public Observable<DiscoverViewModel> getDiscover(RequestParams params) {
        return loginRegisterApi
                .discoverLogin(params.getParameters())
                .map(discoverMapper)
                .doOnNext(saveToCache(params.getString(DiscoverUseCase.PARAM_TYPE, "")));
    }

    private Action1<DiscoverViewModel> saveToCache(final String source) {
        return discoverViewModel -> {
            if (discoverViewModel != null) {
                globalCacheManager.save(LocalDiscoverDataSource.KEY_DISCOVER + source,
                        CacheUtil.convertModelToString(discoverViewModel,
                                new TypeToken<DiscoverViewModel>() {
                                }.getType()), LocalDiscoverDataSource.CACHE_DURATION);
            }
        };
    }
}
