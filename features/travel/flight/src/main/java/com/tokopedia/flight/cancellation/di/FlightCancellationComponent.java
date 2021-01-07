package com.tokopedia.flight.cancellation.di;

import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationDetailFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;

/**
 * @author by furqan on 21/03/18.
 */

@FlightCancellationScope
@Component(modules = FlightCancellationModule.class, dependencies = FlightComponent.class)
public interface FlightCancellationComponent {

    UserSessionInterface userSessionInterface();

    void inject(FlightCancellationDetailFragment flightCancellationDetailFragment);
}
