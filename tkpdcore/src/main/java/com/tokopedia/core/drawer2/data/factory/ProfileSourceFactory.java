package com.tokopedia.core.drawer2.data.factory;

import android.content.Context;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.source.CloudProfileSource;
import com.tokopedia.core.drawer2.data.source.LocalProfileSource;
import com.tokopedia.core.network.apiservices.user.PeopleService;

/**
 * Created by nisie on 5/5/17.
 */

public class ProfileSourceFactory {

    public static final String KEY_PROFILE_DATA = "KEY_PROFILE_DATA";

    private Context context;
    private PeopleService peopleService;
    private ProfileMapper profileMapper;
    private GlobalCacheManager peopleCache;

    public ProfileSourceFactory(Context context,
                                PeopleService peopleService,
                                ProfileMapper profileMapper,
                                GlobalCacheManager peopleCache) {
        this.context = context;
        this.peopleService = peopleService;
        this.profileMapper = profileMapper;
        this.peopleCache = peopleCache;
    }

    public CloudProfileSource createCloudPeopleSource() {
        return new CloudProfileSource(context, peopleService,
                profileMapper, peopleCache);
    }

    public LocalProfileSource createLocalPeopleSource() {
        return new LocalProfileSource(peopleCache);
    }

}
