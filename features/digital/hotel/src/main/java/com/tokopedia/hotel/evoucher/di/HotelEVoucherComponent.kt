package com.tokopedia.hotel.evoucher.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import dagger.Component

/**
 * @author by furqan on 14/05/19
 */

@HotelEVoucherScope
@Component(modules = [HotelEVoucherModule::class, HotelEVoucherViewModelModule::class],
        dependencies = [HotelComponent::class])
interface HotelEVoucherComponent {


}