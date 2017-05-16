package com.tokopedia.tkpd.feedplus;

import com.tokopedia.core.database.manager.GlobalCacheManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 5/15/17.
 */

@Module
public class FeedPlusModule {

    @FeedPlusScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }
}
