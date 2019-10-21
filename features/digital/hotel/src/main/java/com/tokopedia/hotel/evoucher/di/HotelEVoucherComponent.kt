package com.tokopedia.hotel.evoucher.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.evoucher.presentation.fragment.HotelEVoucherFragment
import dagger.Component

/**
 * @author by furqan on 14/05/19
 */

@HotelEVoucherScope
@Component(modules = [HotelEVoucherModule::class, HotelEVoucherViewModelModule::class],
        dependencies = [HotelComponent::class])
interface HotelEVoucherComponent {

    fun inject(hotelEVoucherFragment: HotelEVoucherFragment)

}