package com.tokopedia.notifcenter.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.notifcenter.view.NotificationFragment
import com.tokopedia.notifcenter.view.affiliate.NotificationAffiliateFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NotificationFragmentModule {

    @Binds
    @ActivityScope
    internal abstract fun bindFragmentFactory(factory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @ActivityScope
    @IntoMap
    @FragmentKey(NotificationFragment::class)
    internal abstract fun bindNotificationFragment(fragment: NotificationFragment): Fragment

    @Binds
    @ActivityScope
    @IntoMap
    @FragmentKey(NotificationAffiliateFragment::class)
    internal abstract fun bindNotificationAffiliateFragment(
        fragment: NotificationAffiliateFragment
    ): Fragment
}
