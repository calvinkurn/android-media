package com.tokopedia.core.drawer2.data.source;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.factory.TokoCashSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nisie on 5/5/17.
 */

public class CloudProfileSource {

    private Context context;
    private PeopleService peopleService;
    private ProfileMapper profileMapper;
    private GlobalCacheManager peopleCache;

    public CloudProfileSource(Context context,
                              PeopleService peopleService,
                              ProfileMapper profileMapper,
                              GlobalCacheManager peopleCache) {
        this.context = context;
        this.peopleService = peopleService;
        this.profileMapper = profileMapper;
        this.peopleCache = peopleCache;
    }

    public Observable<ProfileModel> getProfile(TKPDMapParam<String, Object> parameters) {
        return peopleService.getApi()
                .getProfile2(parameters)
                .map(profileMapper)
                .doOnNext(setToCache());
    }

    private Action1<ProfileModel> setToCache() {
        return new Action1<ProfileModel>() {
            @Override
            public void call(ProfileModel profileModel) {
                if (profileModel != null) {
                    peopleCache.setKey(ProfileSourceFactory.KEY_PROFILE_DATA);
                    peopleCache.setValue(CacheUtil.convertModelToString(profileModel,
                            new TypeToken<ProfileModel>() {
                            }.getType()));
                    peopleCache.setCacheDuration(18000);
                    peopleCache.store();
                }
            }
        };
    }
}
