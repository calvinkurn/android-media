package com.tokopedia.flight.orderlist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.resend_email.presentation.viewmodel.FlightResendETicketViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 12/11/2020
 */
@Module
abstract class FlightOrderViewModelModule {

    @FlightOrderScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @FlightOrderScope
    @Binds
    @IntoMap
    @ViewModelKey(FlightResendETicketViewModel::class)
    abstract fun flightResendETicketViewModel(viewModel: FlightResendETicketViewModel): ViewModel

}