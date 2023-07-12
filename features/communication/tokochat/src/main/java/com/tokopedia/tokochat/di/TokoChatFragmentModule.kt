package com.tokopedia.tokochat.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.view.chatlist.TokoChatListFragment
import com.tokopedia.tokochat.view.chatroom.TokoChatFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoChatFragmentModule {
    @Binds
    @ActivityScope
    internal abstract fun bindFragmentFactory(factory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @ActivityScope
    @IntoMap
    @FragmentKey(TokoChatFragment::class)
    internal abstract fun bindTokoChatFragment(fragment: TokoChatFragment): Fragment

    @Binds
    @ActivityScope
    @IntoMap
    @FragmentKey(TokoChatListFragment::class)
    internal abstract fun bindTokoChatListFragment(fragment: TokoChatListFragment): Fragment
}
