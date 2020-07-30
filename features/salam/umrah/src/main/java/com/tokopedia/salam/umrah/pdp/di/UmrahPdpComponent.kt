package com.tokopedia.salam.umrah.pdp.di

import com.tokopedia.salam.umrah.common.di.UmrahComponent
import com.tokopedia.salam.umrah.pdp.presentation.fragment.UmrahPdpDetailFragment
import com.tokopedia.salam.umrah.pdp.presentation.fragment.UmrahPdpFragment
import dagger.Component
/**
 * @author by M on 30/10/19
 */
@UmrahPdpScope
@Component(modules = [UmrahPdpModule::class, UmrahPdpViewModelModule::class],
        dependencies = [UmrahComponent::class])
interface UmrahPdpComponent {
    fun inject(umrahPdpFragment: UmrahPdpFragment)
    fun inject(umrahPdpDetailFragment: UmrahPdpDetailFragment)
}