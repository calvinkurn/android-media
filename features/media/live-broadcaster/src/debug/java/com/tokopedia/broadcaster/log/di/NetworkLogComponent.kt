package com.tokopedia.broadcaster.log.di

import com.tokopedia.broadcaster.log.di.module.NetworkLogModule
import com.tokopedia.broadcaster.log.di.module.NetworkLogViewModelModule
import com.tokopedia.broadcaster.log.di.scope.NetworkLogScope
import com.tokopedia.broadcaster.log.ui.fragment.main.NetworkLogFragment
import dagger.Component

@NetworkLogScope
@Component(modules = [
    NetworkLogModule::class,
    NetworkLogViewModelModule::class
])
interface NetworkLogComponent {
    fun inject(fragment: NetworkLogFragment)
}