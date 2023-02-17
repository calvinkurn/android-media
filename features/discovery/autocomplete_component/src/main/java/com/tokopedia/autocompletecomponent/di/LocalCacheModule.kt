package com.tokopedia.autocompletecomponent.di

import com.tokopedia.autocompletecomponent.util.CoachMarkLocalCache
import com.tokopedia.autocompletecomponent.util.SharedPrefsCoachMarkLocalCache
import dagger.Binds
import dagger.Module

@Module
abstract class LocalCacheModule {
    @Binds
    @AutoCompleteScope
    abstract fun bindCoachMarkLocalCache(
        instance: SharedPrefsCoachMarkLocalCache
    ) : CoachMarkLocalCache
}
