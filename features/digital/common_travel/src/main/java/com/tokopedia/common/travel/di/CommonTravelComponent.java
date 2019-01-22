package com.tokopedia.common.travel.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.common.travel.presentation.fragment.TravelPassengerEditFragment;
import com.tokopedia.common.travel.presentation.fragment.TravelPassengerUpdateFragment;
import com.tokopedia.common.travel.presentation.fragment.TravelPassengerListFragment;

import dagger.Component;

/**
 * Created by nabillasabbaha on 13/08/18.
 */
@CommonTravelScope
@Component(modules = CommonTravelModule.class, dependencies = BaseAppComponent.class)
public interface CommonTravelComponent {

    void inject(TravelPassengerUpdateFragment travelPassengerUpdateFragment);

    void inject(TravelPassengerListFragment travelPassengerListFragment);

    void inject(TravelPassengerEditFragment travelPassengerEditFragment);
}
