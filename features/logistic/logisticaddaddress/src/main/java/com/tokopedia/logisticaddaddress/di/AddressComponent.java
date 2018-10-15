package com.tokopedia.logisticaddaddress.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticaddaddress.addaddress.AddAddressFragment;
import com.tokopedia.logisticaddaddress.manageaddress.ManagePeopleAddressFragment;
import com.tokopedia.logisticaddaddress.service.ManagePeopleAddressService;

import dagger.Component;

/**
 * Created by Fajar Ulin Nuha on 11/10/18.
 */
@AddressScope
@Component(modules = AddressModule.class, dependencies = BaseAppComponent.class)
public interface AddressComponent {

    void inject(AddAddressFragment addAddressFragment);

    void inject(ManagePeopleAddressFragment managePeopleAddressFragment);

    void inject(ManagePeopleAddressService managePeopleAddressService);

}
