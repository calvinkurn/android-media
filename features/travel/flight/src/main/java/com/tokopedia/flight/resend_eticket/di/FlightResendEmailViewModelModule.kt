package com.tokopedia.flight.resend_eticket.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.resend_eticket.presentation.viewmodel.FlightResendETicketViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FlightResendEmailViewModelModule {

    @FlightResendEmailScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @FlightResendEmailScope
    @Binds
    @IntoMap
    @ViewModelKey(FlightResendETicketViewModel::class)
    abstract fun flightResendETicketViewModel(viewModel: FlightResendETicketViewModel): ViewModel

}