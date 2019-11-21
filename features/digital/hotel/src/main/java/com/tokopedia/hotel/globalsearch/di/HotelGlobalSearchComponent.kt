package com.tokopedia.hotel.globalsearch.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.globalsearch.presentation.fragment.HotelGlobalSearchFragment
import dagger.Component

/**
 * @author by furqan on 19/11/2019
 */
@HotelGlobalSearchScope
@Component(modules = [HotelGlobalSearchViewModelModule::class], dependencies = [HotelComponent::class])
interface HotelGlobalSearchComponent {
    fun inject(hotelGlobalSearchFragment: HotelGlobalSearchFragment)
}