package com.tokopedia.hotel.cancellation.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import dagger.Component

/**
 * @author by jessica on 27/04/20
 */

@HotelCancellationScope
@Component(modules = [HotelCancellationViewModelModule::class], dependencies = [HotelComponent::class])
interface HotelCancellationComponent {

}