package com.tokopedia.search.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class SearchTrackingModule {

    @SearchScope
    @Provides
    SearchTracking provideSearchTracking(@ApplicationContext Context context,
                                         UserSessionInterface userSessionInterface) {
        return new SearchTracking(context, userSessionInterface);
    }
}
