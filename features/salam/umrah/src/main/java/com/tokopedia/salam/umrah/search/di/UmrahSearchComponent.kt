package com.tokopedia.salam.umrah.search.di

import com.tokopedia.salam.umrah.common.di.UmrahComponent
import com.tokopedia.salam.umrah.search.presentation.fragment.UmrahSearchFilterFragment
import com.tokopedia.salam.umrah.search.presentation.fragment.UmrahSearchFragment
import dagger.Component

/**
 * @author by furqan on 18/10/2019
 */
@UmrahSearchScope
@Component(modules = [UmrahSearchModule::class, UmrahSearchViewModelModule::class],
        dependencies = [UmrahComponent::class])
interface UmrahSearchComponent {
    fun inject(umrahSearchFragment: UmrahSearchFragment)
    fun inject(umrahSearchFilterFragment: UmrahSearchFilterFragment)
}