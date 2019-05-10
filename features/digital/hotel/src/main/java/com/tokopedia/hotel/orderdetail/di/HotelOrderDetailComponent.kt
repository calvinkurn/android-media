package com.tokopedia.hotel.orderdetail.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import dagger.Component

/**
 * @author by jessica on 09/05/19
 */

@HotelOrderDetailScope
@Component(modules = [HotelOrderDetailModule::class, HotelOrderDetailViewModelModule::class], dependencies = [HotelComponent::class])
interface HotelHomepageComponent {

//    fun inject(hotelHomepageFragment: HotelHomepageFragment)

}