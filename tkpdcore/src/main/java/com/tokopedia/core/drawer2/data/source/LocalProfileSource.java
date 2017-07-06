package com.tokopedia.core.drawer2.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 5/5/17.
 */

public class LocalProfileSource {

    private final GlobalCacheManager peopleCache;

    public LocalProfileSource(GlobalCacheManager peopleCache) {
        this.peopleCache = peopleCache;
    }

    public Observable<ProfileModel> getProfile() {

        return Observable.just(true).map(new Func1<Boolean, ProfileModel>() {
            @Override
            public ProfileModel call(Boolean aBoolean) {

                if (getCache() != null) {
                    return CacheUtil.convertStringToModel(getCache(),
                            new TypeToken<ProfileModel>() {
                            }.getType());
                } else
                    throw new RuntimeException("Cache has expired");

            }
        });
    }

    private String getCache() {
        return peopleCache.getValueString(ProfileSourceFactory.KEY_PROFILE_DATA);
    }
}
