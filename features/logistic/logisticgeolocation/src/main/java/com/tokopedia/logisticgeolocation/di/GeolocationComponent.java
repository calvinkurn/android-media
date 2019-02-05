package com.tokopedia.logisticgeolocation.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticgeolocation.pinpoint.GeolocationActivity;
import com.tokopedia.logisticgeolocation.pinpoint.GoogleMapFragment;

import dagger.Component;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
 */
@GeolocationScope
@Component(modules = GeolocationModule.class, dependencies = BaseAppComponent.class)
public interface GeolocationComponent {

    void inject(GoogleMapFragment googleMapFragment);

    void inject(GeolocationActivity geolocationActivity);

}
