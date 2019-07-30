package com.tokopedia.hotel.hoteldetail.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.hoteldetail.presentation.fragment.HotelDetailFragment
import com.tokopedia.hotel.hoteldetail.presentation.fragment.HotelReviewFragment
import dagger.Component

/**
 * @author by furqan on 22/04/19
 */
@HotelDetailScope
@Component(modules = [HotelDetailModule::class, HotelDetailViewModelModule::class], dependencies = [HotelComponent::class])
interface HotelDetailComponent {

    fun inject(hotelDetailFragment: HotelDetailFragment)

    fun inject(hotelReviewFragment: HotelReviewFragment)

}