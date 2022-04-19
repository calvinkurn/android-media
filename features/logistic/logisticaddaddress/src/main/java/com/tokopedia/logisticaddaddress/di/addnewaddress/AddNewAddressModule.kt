package com.tokopedia.logisticaddaddress.di.addnewaddress

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import com.tokopedia.logisticaddaddress.domain.executor.MainSchedulerProvider
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.usecase.*
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressPresenter
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapPresenter
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by fwidjaja on 2019-05-09.
 */

@Module
class AddNewAddressModule {

    @Provides
    @ActivityScope
    fun providePinpointMapPresenter(
        getDistrictUseCase: GetDistrictUseCase,
        revGeocodeUseCase: RevGeocodeUseCase,
        districtBoundaryUseCase: DistrictBoundaryUseCase,
        districtBoundaryMapper: DistrictBoundaryMapper
    ): PinpointMapPresenter {
        return PinpointMapPresenter(getDistrictUseCase, revGeocodeUseCase,
                districtBoundaryUseCase, districtBoundaryMapper)
    }

    @Provides
    @ActivityScope
    fun provideAddEditAddressPresenter(
        addAddressUseCase: AddAddressUseCase,
        zipCodeUseCase: GetZipCodeUseCase,
        getDistrictUseCase: GetDistrictUseCase,
        autoCompleteUseCase: AutoCompleteUseCase
    ): AddEditAddressPresenter {
        return AddEditAddressPresenter(addAddressUseCase, zipCodeUseCase, getDistrictUseCase, autoCompleteUseCase)
    }

    @Provides
    @ActivityScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

    @Provides
    fun provideScedulerProvider(): SchedulerProvider = MainSchedulerProvider()

}