package com.tokopedia.logisticaddaddress.di;

import com.tokopedia.logisticaddaddress.data.AddAddressRetrofitInteractorImpl;
import com.tokopedia.logisticaddaddress.data.AddressRepository;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressContract;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressPresenterImpl;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.module.LogisticNetworkModule;
import com.tokopedia.logisticdata.data.module.qualifier.AddressScope;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticPeopleActApiQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticUserSessionQualifier;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fajar Ulin Nuha on 11/10/18.
 */
@AddressScope
@Module(includes = LogisticNetworkModule.class)
public class AddressModule {

    @Provides
    @AddressScope
    AddressRepository provideAddressRepository(
            @LogisticPeopleActApiQualifier PeopleActApi peopleActApi
    ){
        return new AddAddressRetrofitInteractorImpl(peopleActApi);
    }

    @Provides
    @AddressScope
    AddAddressContract.Presenter provideAddAddressPresenter(
            @LogisticUserSessionQualifier UserSessionInterface userSessionInterface,
            AddressRepository addressRepository) {
        return new AddAddressPresenterImpl(userSessionInterface, addressRepository);
    }

}
