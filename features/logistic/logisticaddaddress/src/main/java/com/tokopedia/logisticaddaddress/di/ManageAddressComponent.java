package com.tokopedia.logisticaddaddress.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticaddaddress.features.manage.ManageAddressFragment;
import com.tokopedia.logisticaddaddress.features.manageaddress.ManagePeopleAddressFragment;

import dagger.Component;

/**
 * Created by Fajar Ulin Nuha on 18/10/18.
 */
@AddressScope
@Component(modules = {AddressModule.class, ManageAddressModule.class}, dependencies = BaseAppComponent.class)
public interface ManageAddressComponent {

    void inject(ManagePeopleAddressFragment managePeopleAddressFragment);

    void inject(ManageAddressFragment manageAddressFragment);
}
