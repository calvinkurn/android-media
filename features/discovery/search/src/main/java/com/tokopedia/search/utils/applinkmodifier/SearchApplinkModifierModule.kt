package com.tokopedia.search.utils.applinkmodifier

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class SearchApplinkModifierModule {

    @Binds
    @SearchScope
    abstract fun provideApplinkModifier(searchApplinkModifier: SearchApplinkModifier): ApplinkModifier
}
