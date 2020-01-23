package com.tokopedia.salam.umrah.travel.di

import com.tokopedia.salam.umrah.common.di.UmrahComponent
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelFragment
import com.tokopedia.salam.umrah.travel.presentation.viewmodel.UmrahTravelViewModel
import dagger.Component

/**
 * @author by Firman on 22/1/20
 */

@UmrahTravelScope
@Component(modules = [UmrahTravelModule::class, UmrahTravelViewModelModule::class],
        dependencies = [UmrahComponent::class])
interface UmrahTravelComponent {
    fun inject(umrahTravelFragment: UmrahTravelFragment)
}