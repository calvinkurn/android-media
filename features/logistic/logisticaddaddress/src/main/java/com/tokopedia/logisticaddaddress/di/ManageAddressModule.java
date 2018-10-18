package com.tokopedia.logisticaddaddress.di;

import android.content.Context;

import com.tokopedia.logisticaddaddress.adapter.AddressTypeFactory;
import com.tokopedia.logisticaddaddress.adapter.AddressViewHolder;
import com.tokopedia.logisticaddaddress.adapter.ManageAddressAdapter;
import com.tokopedia.logisticaddaddress.features.manageaddress.MPAddressActivityListener;
import com.tokopedia.logisticaddaddress.features.manageaddress.ManagePeopleAddressFragmentPresenter;
import com.tokopedia.logisticaddaddress.features.manageaddress.ManagePeopleAddressPresenter;

import java.util.ArrayList;

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
    Context provideConthext() {
        return this.context;
    }

    @Provides
    @AddressScope
    MPAddressActivityListener provideManageAddressListener(@ActivityContext Context context) {
        return (MPAddressActivityListener) context;
    }

    @Provides
    @AddressScope
    ManagePeopleAddressFragmentPresenter provideManageAddressPresenter(ManagePeopleAddressPresenter presenter) {
        return presenter;
    }

    @Provides
    @AddressScope
    AddressViewHolder.ManageAddressListener provideAddressViewHolderListener(ManagePeopleAddressPresenter presenter) {
        return presenter;
    }

    @Provides
    @AddressScope
    ManageAddressAdapter provideAddressAdapter(AddressTypeFactory addressTypeFactory) {
        return new ManageAddressAdapter(addressTypeFactory, new ArrayList<>());
    }

}
