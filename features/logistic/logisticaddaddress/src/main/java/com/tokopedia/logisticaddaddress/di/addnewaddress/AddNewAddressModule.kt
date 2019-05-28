package com.tokopedia.logisticaddaddress.di.addnewaddress

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.logisticaddaddress.data.AddressRepository
import com.tokopedia.logisticaddaddress.domain.mapper.AddAddressMapper
import com.tokopedia.logisticaddaddress.domain.mapper.AutofillMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AddAddressUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.AutofillUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
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
    @AddNewAddressScope
    fun providePintpointMapPresenter(
            @ApplicationContext context: Context,
            getDistrictUseCase: GetDistrictUseCase,
            getDistrictMapper: GetDistrictMapper,
            autofillUseCase: AutofillUseCase,
            autofillMapper: AutofillMapper): PinpointMapPresenter {
        return PinpointMapPresenter(context, getDistrictUseCase, getDistrictMapper, autofillUseCase, autofillMapper)
    }

    @Provides
    @AddNewAddressScope
    fun provideAddEditAddressPresenter(
            @ApplicationContext context: Context,
            addAddressUseCase: AddAddressUseCase,
            addAddressMapper: AddAddressMapper): AddEditAddressPresenter {
        return AddEditAddressPresenter(context, addAddressUseCase, addAddressMapper)
    }

    @Provides
    @AddNewAddressScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)
}