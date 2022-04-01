package com.tokopedia.flight.resend_eticket.di

import com.tokopedia.flight.common.di.component.FlightComponent
import com.tokopedia.flight.resend_eticket.presentation.bottomsheet.FlightOrderResendEmailBottomSheet
import dagger.Component

@FlightResendEmailScope
@Component(
    modules = [FlightResendEmailModule::class, FlightResendEmailViewModelModule::class],
    dependencies = [FlightComponent::class]
)
interface FlightResendEmailComponent {
    fun inject(resendEmail: FlightOrderResendEmailBottomSheet)
}