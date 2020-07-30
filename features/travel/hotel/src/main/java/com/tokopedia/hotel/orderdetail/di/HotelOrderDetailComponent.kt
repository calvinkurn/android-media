package com.tokopedia.hotel.orderdetail.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.orderdetail.presentation.fragment.HotelOrderDetailFragment
import dagger.Component

/**
 * @author by jessica on 09/05/19
 */

@HotelOrderDetailScope
@Component(modules = [HotelOrderDetailModule::class, HotelOrderDetailViewModelModule::class], dependencies = [HotelComponent::class])
interface HotelOrderDetailComponent {

    fun inject(hotelOrderDetailFragment: HotelOrderDetailFragment)

}