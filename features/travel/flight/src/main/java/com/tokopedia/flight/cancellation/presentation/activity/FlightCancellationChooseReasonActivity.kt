package com.tokopedia.flight.cancellation.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent
import com.tokopedia.flight.cancellation.presentation.fragment.FlightCancellationChooseReasonFragment

class FlightCancellationChooseReasonActivity : BaseSimpleActivity(),
        HasComponent<FlightCancellationComponent> {

    private var selectedReason: FlightCancellationPassengerEntity.Reason? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        selectedReason = intent.getParcelableExtra(FlightCancellationChooseReasonFragment.EXTRA_SELECTED_REASON)
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    override fun getNewFragment(): Fragment =
            FlightCancellationChooseReasonFragment.createInstance(selectedReason)

    override fun getComponent(): FlightCancellationComponent =
            DaggerFlightCancellationComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(com.tokopedia.common.travel.R.anim.travel_anim_stay, com.tokopedia.common.travel.R.anim.travel_slide_out_up)
    }

    private fun setupToolbar() {
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this,
                com.tokopedia.abstraction.R.drawable.ic_close_default))
    }

    companion object {
        fun getCallingIntent(context: Context,
                             selectedReason: FlightCancellationPassengerEntity.Reason?): Intent =
                Intent(context, FlightCancellationChooseReasonActivity::class.java)
                        .putExtra(FlightCancellationChooseReasonFragment.EXTRA_SELECTED_REASON, selectedReason)
    }
}
