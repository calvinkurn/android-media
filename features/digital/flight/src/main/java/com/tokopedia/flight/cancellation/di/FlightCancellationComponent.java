package com.tokopedia.flight.cancellation.di;

import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationChooseReasonFragment;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationDetailFragment;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationFragment;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationListFragment;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationReasonAndProofFragment;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationRefundDetailFragment;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationReviewFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;

/**
 * @author by furqan on 21/03/18.
 */

@FlightCancellationScope
@Component(modules = FlightCancellationModule.class, dependencies = FlightComponent.class)
public interface FlightCancellationComponent {
    FlightModuleRouter flightModuleRouter();

    UserSessionInterface userSessionInterface();

    void inject(FlightCancellationFragment flightCancellationFragment);

    void inject(FlightCancellationReasonAndProofFragment flightCancellationReasonAndProofFragment);

    void inject(FlightCancellationReviewFragment flightCancellationReviewFragment);

    void inject(FlightCancellationRefundDetailFragment flightCancellationRefundDetailFragment);

    void inject(FlightCancellationListFragment flightCancellationListFragment);

    void inject(FlightCancellationDetailFragment flightCancellationDetailFragment);

    void inject(FlightCancellationChooseReasonFragment flightCancellationChooseReasonFragment);
}
