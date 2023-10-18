package com.tokopedia.autocompletecomponent.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.autocompletecomponent.util.CoachMarkLocalCache
import com.tokopedia.autocompletecomponent.util.SharedPrefsCoachMarkLocalCache
import com.tokopedia.discovery.common.utils.MpsLocalCache
import com.tokopedia.discovery.common.utils.SharedPrefsMpsLocalCache
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object LocalCacheModule {
    @Provides
    @Reusable
    @JvmStatic
    fun bindCoachMarkLocalCache(
        instance: SharedPrefsCoachMarkLocalCache
    ) : CoachMarkLocalCache = instance

    @Provides
    @Reusable
    @JvmStatic
    fun bindMpsLocalCache(
        @ApplicationContext context: Context
    ) : MpsLocalCache {
        return SharedPrefsMpsLocalCache(context)
    }
}
