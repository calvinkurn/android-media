package com.tokopedia.salam.umrah.homepage.di

import com.tokopedia.salam.umrah.common.di.UmrahComponent
import com.tokopedia.salam.umrah.homepage.presentation.fragment.UmrahHomepageFragment
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by furqan on 14/10/2019
 */
@UmrahHomepageScope
@Component(modules = [UmrahHomepageModule::class, UmrahHomepageViewModelModule::class],
        dependencies = [UmrahComponent::class])
interface UmrahHomepageComponent {

    fun inject(umrahOrderFragment: UmrahHomepageFragment)

}