package com.tokopedia.flight.cancellationV2.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.cancellationV2.di.DaggerFlightCancellationComponent
import com.tokopedia.flight.cancellationV2.di.FlightCancellationComponent

class FlightCancellationPassengerActivity : BaseSimpleActivity(), HasComponent<FlightCancellationComponent> {

    override fun getNewFragment(): Fragment? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getComponent(): FlightCancellationComponent =
            DaggerFlightCancellationComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()
}
