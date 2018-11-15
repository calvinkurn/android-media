package com.tokopedia.logisticaddaddress.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressFragment;
import com.tokopedia.logisticaddaddress.features.manage.ManageAddressFragment;
import com.tokopedia.logisticaddaddress.service.ManagePeopleAddressService;

import dagger.Component;

/**
 * Created by Fajar Ulin Nuha on 11/10/18.
 */
@AddressScope
@Component(modules = AddressModule.class, dependencies = BaseAppComponent.class)
public interface AddressComponent {

    void inject(AddAddressFragment addAddressFragment);

    void inject(ManagePeopleAddressService managePeopleAddressService);

}
