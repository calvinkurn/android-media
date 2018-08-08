package com.tokopedia.core.drawer2.data.factory;

import android.content.Context;

import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.source.CloudProfileSource;
import com.tokopedia.core.drawer2.data.source.LocalProfileSource;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by nisie on 5/5/17.
 */

public class ProfileSourceFactory {

    public static final String KEY_PROFILE_DATA = "KEY_PROFILE_DATA";

    private final Context context;
    private final PeopleService peopleService;
    private final ProfileMapper profileMapper;
    private final GlobalCacheManager peopleCache;
    private final SessionHandler sessionHandler;

    public ProfileSourceFactory(Context context,
                                PeopleService peopleService,
                                ProfileMapper profileMapper,
                                GlobalCacheManager peopleCache,
                                SessionHandler sessionHandler) {
        this.context = context;
        this.peopleService = peopleService;
        this.profileMapper = profileMapper;
        this.peopleCache = peopleCache;
        this.sessionHandler = sessionHandler;
    }

    public CloudProfileSource createCloudPeopleSource() {
        return new CloudProfileSource(context, peopleService,
                profileMapper, peopleCache, sessionHandler);
    }

    public LocalProfileSource createLocalPeopleSource() {
        return new LocalProfileSource(peopleCache);
    }

}
