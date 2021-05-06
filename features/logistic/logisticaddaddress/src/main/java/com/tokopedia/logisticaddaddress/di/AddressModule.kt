package com.tokopedia.logisticaddaddress.di

import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.logisticCommon.data.apiservice.PeopleActApi
import com.tokopedia.logisticCommon.data.module.LogisticNetworkModule
import com.tokopedia.logisticCommon.data.module.qualifier.AddressScope
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticPeopleActApiQualifier
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticUserSessionQualifier
import com.tokopedia.logisticCommon.data.repository.AddressRepositoryImpl
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import com.tokopedia.logisticaddaddress.data.AddAddressRetrofitInteractorImpl
import com.tokopedia.logisticaddaddress.data.AddressRepository
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressContract
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressPresenterImpl
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by Fajar Ulin Nuha on 11/10/18.
 */
@Module(includes = [LogisticNetworkModule::class])
class AddressModule {

    @Provides
    @AddressScope
    fun provideAddressRepository(
            @LogisticPeopleActApiQualifier peopleActApi: PeopleActApi): AddressRepository {
        return AddAddressRetrofitInteractorImpl(peopleActApi)
    }

    @Provides
    @AddressScope
    fun providesPeopleApi(@LogisticPeopleActApiQualifier retrofit: Retrofit) : PeopleActApi {
        return retrofit.create(PeopleActApi::class.java)
    }

    @Provides
    @AddressScope
    fun provideEditPinpointRepository(
            @LogisticPeopleActApiQualifier peopleActApi: PeopleActApi): com.tokopedia.logisticCommon.data.repository.AddressRepository {
        return AddressRepositoryImpl(peopleActApi)
    }

    @Provides
    @AddressScope
    fun provideAddAddressPresenter(
            @LogisticUserSessionQualifier userSessionInterface: UserSessionInterface?,
            addressRepository: AddressRepository, revGeocodeUseCase: RevGeocodeUseCase?): AddAddressContract.Presenter {
        return AddAddressPresenterImpl(userSessionInterface, addressRepository, revGeocodeUseCase)
    }

    @Provides
    @AddressScope
    fun providePerformanceMonitoring(): PerformanceMonitoring {
        return PerformanceMonitoring()
    }
}