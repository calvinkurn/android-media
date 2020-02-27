package com.tokopedia.logisticaddaddress.di.addnewaddress

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.logisticaddaddress.domain.executor.MainSchedulerProvider
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AddAddressUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictBoundaryUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetZipCodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressPresenter
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapPresenter
import com.tokopedia.logisticdata.domain.usecase.RevGeocodeUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by fwidjaja on 2019-05-09.
 */

@Module
class AddNewAddressModule {

    @Provides
    @AddNewAddressScope
    fun providePinpointMapPresenter(
            getDistrictUseCase: GetDistrictUseCase,
            getDistrictMapper: GetDistrictMapper,
            revGeocodeUseCase: RevGeocodeUseCase,
            districtBoundaryUseCase: DistrictBoundaryUseCase,
            districtBoundaryMapper: DistrictBoundaryMapper): PinpointMapPresenter {
        return PinpointMapPresenter(getDistrictUseCase, getDistrictMapper, revGeocodeUseCase,
                districtBoundaryUseCase, districtBoundaryMapper)
    }

    @Provides
    @AddNewAddressScope
    fun provideAddEditAddressPresenter(
            addAddressUseCase: AddAddressUseCase,
            zipCodeUseCase: GetZipCodeUseCase): AddEditAddressPresenter {
        return AddEditAddressPresenter(addAddressUseCase, zipCodeUseCase)
    }

    @Provides
    @AddNewAddressScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

    @Provides
    fun provideScedulerProvider(): SchedulerProvider = MainSchedulerProvider()

}