package com.tokopedia.logisticaddaddress.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.logisticaddaddress.data.AddAddressRetrofitInteractorImpl;
import com.tokopedia.logisticaddaddress.data.AddressRepository;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressPresenter;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressPresenterImpl;
import com.tokopedia.logisticdata.data.module.LogisticNetworkModule;
import com.tokopedia.logisticdata.data.module.qualifier.AddressScope;
import com.tokopedia.user.session.UserSession;

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
    AddAddressPresenter provideAddAddressPresenter(AddAddressPresenterImpl addAddressPresenter) {
        return addAddressPresenter;
    }

    @Provides
    @AddressScope
    AddressRepository provideAddressRepo(AddAddressRetrofitInteractorImpl addAddressRetrofitInteractor) {
        return addAddressRetrofitInteractor;
    }

    @Provides
    @AddressScope
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

}
