package com.tokopedia.inbox.universalinbox.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.universalinbox.view.UniversalInboxFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UniversalInboxFragmentModule {

    @Binds
    @ActivityScope
    internal abstract fun bindFragmentFactory(factory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @ActivityScope
    @IntoMap
    @FragmentKey(UniversalInboxFragment::class)
    internal abstract fun bindUniversalInboxFragment(fragment: UniversalInboxFragment): Fragment
}
