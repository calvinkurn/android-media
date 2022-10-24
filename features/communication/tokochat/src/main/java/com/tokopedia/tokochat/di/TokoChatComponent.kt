package com.tokopedia.tokochat.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokochat.view.fragment.TokoChatFragment
import com.tokopedia.tokochat.view.fragment.experiment.TokoChatFragmentExp
import com.tokopedia.tokochat.view.fragment.experiment.TokoChatListFragmentExp
import dagger.Component

@TokoChatScope
@Component(
    modules = [
        TokoChatModule::class,
        TokoChatUseCaseModule::class,
        TokoChatContextModule::class,
        TokoChatViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface TokoChatComponent {
    fun inject(fragment: TokoChatFragment)

    //TODO: Remove this after experiment
    fun inject(fragment: TokoChatFragmentExp)
    fun inject(fragment: TokoChatListFragmentExp)
}
