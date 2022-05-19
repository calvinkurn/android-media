package com.tokopedia.flight.resend_eticket.di

import com.tokopedia.flight.resend_eticket.domain.FlightOrderResendEmailUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import dagger.Module
import dagger.Provides

@Module
class FlightResendEmailModule {
    @FlightResendEmailScope
    @Provides
    fun provideFlightOrderResendEmailUseCase(multiRequestUseCase: MultiRequestGraphqlUseCase): FlightOrderResendEmailUseCase =
        FlightOrderResendEmailUseCase(multiRequestUseCase)

}