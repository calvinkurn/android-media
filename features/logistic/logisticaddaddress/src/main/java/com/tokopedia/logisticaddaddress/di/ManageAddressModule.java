package com.tokopedia.logisticaddaddress.di;

import android.content.Context;

import com.tokopedia.logisticaddaddress.data.DataSource;
import com.tokopedia.logisticaddaddress.features.manage.ManageAddressContract;
import com.tokopedia.logisticaddaddress.features.manage.ManageAddressPresenter;
import com.tokopedia.logisticaddaddress.data.ManageAddressRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fajar Ulin Nuha on 18/10/18.
 */
@Module
public class ManageAddressModule {

    private final Context context;

    public ManageAddressModule(Context context) {
        this.context = context;
    }

    @Provides
    @AddressScope
    @ActivityContext
    Context provideContext() {
        return this.context;
    }

    @Provides
    @AddressScope
    ManageAddressContract.Presenter providePresenter(ManageAddressPresenter presenter) {
        return presenter;
    }

    @Provides
    @AddressScope
    DataSource provideRepository(ManageAddressRepository repository) {
        return repository;
    }
}
