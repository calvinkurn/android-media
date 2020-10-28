package com.tokopedia.logisticaddaddress.di

import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.logisticaddaddress.data.AddAddressRetrofitInteractorImpl
import com.tokopedia.logisticaddaddress.data.AddressRepository
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressContract
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressPresenterImpl
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi
import com.tokopedia.logisticdata.data.module.LogisticNetworkModule
import com.tokopedia.logisticdata.data.module.qualifier.AddressScope
import com.tokopedia.logisticdata.data.module.qualifier.LogisticPeopleActApiQualifier
import com.tokopedia.logisticdata.data.module.qualifier.LogisticUserSessionQualifier
import com.tokopedia.logisticdata.data.repository.AddressRepositoryImpl
import com.tokopedia.logisticdata.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticdata.domain.usecase.RevGeocodeUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by Fajar Ulin Nuha on 11/10/18.
 */
@AddressScope
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
            @LogisticPeopleActApiQualifier peopleActApi: PeopleActApi): com.tokopedia.logisticdata.data.repository.AddressRepository {
        return AddressRepositoryImpl(peopleActApi)
    }

    @Provides
    @AddressScope
    fun provideAddAddressPresenter(
            @LogisticUserSessionQualifier userSessionInterface: UserSessionInterface?,
            addressRepository: AddressRepository, revGeocodeUseCase: RevGeocodeUseCase?, editAddressUseCase: EditAddressUseCase): AddAddressContract.Presenter {
        return AddAddressPresenterImpl(userSessionInterface, addressRepository, revGeocodeUseCase, editAddressUseCase)
    }

    @Provides
    @AddressScope
    fun providePerformanceMonitoring(): PerformanceMonitoring {
        return PerformanceMonitoring()
    }
}