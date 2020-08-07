package com.tokopedia.core.drawer2.data.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nisie on 5/5/17.
 */

public class CloudProfileSource {

    private static final String KEY_PROFILE_DATA = "KEY_PROFILE_DATA";

    private final Context context;
    private final PeopleService peopleService;
    private final ProfileMapper profileMapper;
    private final GlobalCacheManager peopleCache;
    private final UserSessionInterface userSession;

    public CloudProfileSource(Context context,
                              PeopleService peopleService,
                              ProfileMapper profileMapper,
                              GlobalCacheManager peopleCache,
                              UserSessionInterface userSessionInterface) {
        this.context = context;
        this.peopleService = peopleService;
        this.profileMapper = profileMapper;
        this.peopleCache = peopleCache;
        this.userSession = userSessionInterface;
    }


    public Observable<ProfileModel> getProfile(TKPDMapParam<String, Object> parameters) {
        return peopleService.getApi()
                .getProfile2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(profileMapper)
                .doOnNext(setToCache());
    }

    private Action1<ProfileModel> setToCache() {
        return new Action1<ProfileModel>() {
            @Override
            public void call(ProfileModel profileModel) {
                if (profileModel != null && profileModel.isSuccess()) {
                    peopleCache.setKey(KEY_PROFILE_DATA);
                    peopleCache.setValue(CacheUtil.convertModelToString(profileModel,
                            new TypeToken<ProfileModel>() {
                            }.getType()));
                    peopleCache.setCacheDuration(18000);
                    peopleCache.store();


                    if (profileModel.getProfileData().getShopInfo() != null &&
                            profileModel.getProfileData().getShopInfo().getShopId() != null) {
                        userSession.setShopId(profileModel.getProfileData().getShopInfo().getShopId());
                        userSession.setShopName(profileModel.getProfileData().getShopInfo().getShopName());
                    }
                    if (profileModel.getProfileData().getShopInfo() != null &&
                            profileModel.getProfileData().getShopInfo().getShopDomain() != null) {
                        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString("shopDomain", profileModel.getProfileData().getShopInfo().getShopDomain());
                        editor.apply();
                    }

                    userSession.setProfilePicture(profileModel.getProfileData().getUserInfo().getUserImage());
                }
            }
        };
    }
}
