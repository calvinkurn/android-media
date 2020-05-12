package com.tokopedia.salam.umrah.travel.di

import com.tokopedia.salam.umrah.common.di.UmrahComponent
import com.tokopedia.salam.umrah.travel.presentation.fragment.*
import dagger.Component

/**
 * @author by Firman on 22/1/20
 */

@UmrahTravelScope
@Component(modules = [UmrahTravelModule::class, UmrahTravelViewModelModule::class],
        dependencies = [UmrahComponent::class])
interface UmrahTravelComponent {
    fun inject(umrahTravelFragment: UmrahTravelFragment)
    fun inject(umrahTravelAgentProductsFragment: UmrahTravelAgentProductsFragment)
    fun inject(umrahTravelAgentInfoFragment: UmrahTravelAgentInfoFragment)
    fun inject(umrahTravelAgentGalleryFragment: UmrahTravelAgentGalleryFragment)
    fun inject(umrahTravelListFragment: UmrahTravelListFragment)
}