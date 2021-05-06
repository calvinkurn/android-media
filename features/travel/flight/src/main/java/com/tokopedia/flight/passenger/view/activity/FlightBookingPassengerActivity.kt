package com.tokopedia.flight.passenger.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.flight.common.constant.FlightUrl
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.passenger.di.DaggerFlightPassengerComponent
import com.tokopedia.flight.passenger.di.FlightPassengerComponent
import com.tokopedia.flight.passenger.view.fragment.FlightBookingPassengerFragment
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityMetaModel
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel
import java.util.*

/**
 * @author by jessica on 2019-09-04
 */

class FlightBookingPassengerActivity : BaseFlightActivity(), HasComponent<FlightPassengerComponent> {

    var passengerModel: FlightBookingPassengerModel? = null
    var selectedPassengerId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = savedInstanceState ?: intent.extras ?: Bundle()
        passengerModel = extras.getParcelable(EXTRA_PASSENGER)
        selectedPassengerId = extras.getString(EXTRA_SELECTED_PASSENGER_ID)

        super.onCreate(savedInstanceState)
    }

    fun updateActivityTitle(title: String) {
        updateTitle(title)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(EXTRA_PASSENGER, passengerModel)
        outState?.putString(EXTRA_SELECTED_PASSENGER_ID, selectedPassengerId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean = true

    override fun getNewFragment(): Fragment {
        return FlightBookingPassengerFragment.newInstance(
                depatureId = intent.getStringExtra(EXTRA_DEPATURE),
                passengerModel = passengerModel ?: FlightBookingPassengerModel(),
                luggageModels = intent.getParcelableArrayListExtra(EXTRA_LUGGAGES),
                mealModels = intent.getParcelableArrayListExtra(EXTRA_MEALS),
                isAirAsiaAirlines = intent.getBooleanExtra(EXTRA_IS_AIRASIA, false),
                depatureDate = intent.getStringExtra(EXTRA_DEPARTURE_DATE),
                requestId = intent.getStringExtra(EXTRA_REQUEST_ID),
                isDomestic = intent.getBooleanExtra(EXTRA_IS_DOMESTIC, false),
                returnId = intent.getStringExtra(EXTRA_RETURN),
                autofillName = intent.getStringExtra(EXTRA_AUTOFILL_NAME))
    }

    override fun getComponent(): FlightPassengerComponent {
        return DaggerFlightPassengerComponent.builder()
                .flightComponent(flightComponent)
                .build()
    }

    override fun navigateToHelpPage() {
        RouteManager.route(this, FlightUrl.FLIGHT_PASSENGER_HELP_URL)
    }

    companion object {
        const val EXTRA_PASSENGER = "EXTRA_PASSENGER"
        const val EXTRA_LUGGAGES = "EXTRA_LUGGAGES"
        const val EXTRA_MEALS = "EXTRA_MEALS"
        const val EXTRA_DEPATURE = "EXTRA_DEPATURE"
        const val EXTRA_RETURN = "EXTRA_RETURN"
        const val EXTRA_IS_AIRASIA = "EXTRA_IS_AIRASIA"
        const val EXTRA_DEPARTURE_DATE = "EXTRA_DEPATURE_DATE"
        const val EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID"
        const val EXTRA_SELECTED_PASSENGER_ID = "EXTRA_SELECTED_PASSENGER_ID"
        const val EXTRA_IS_DOMESTIC = "EXTRA_IS_DOMESTIC"
        const val EXTRA_AUTOFILL_NAME = "EXTRA_AUTOFILL_NAME"

        fun getCallingIntent(activity: Activity,
                             depatureId: String,
                             returnId: String,
                             bookingPassengerModel: FlightBookingPassengerModel,
                             luggageModels: List<FlightBookingAmenityMetaModel>,
                             mealModels: List<FlightBookingAmenityMetaModel>,
                             isAirAsiaAirlines: Boolean,
                             depatureDate: String,
                             requestId: String,
                             isDomestic: Boolean,
                             autofillName: String = ""): Intent {
            val intent = Intent(activity, FlightBookingPassengerActivity::class.java)
            intent.putExtra(EXTRA_DEPATURE, depatureId)
            intent.putExtra(EXTRA_RETURN, returnId)
            intent.putExtra(EXTRA_PASSENGER, bookingPassengerModel)
            intent.putExtra(EXTRA_DEPARTURE_DATE, depatureDate)
            intent.putParcelableArrayListExtra(EXTRA_LUGGAGES, luggageModels as ArrayList<out Parcelable>)
            intent.putParcelableArrayListExtra(EXTRA_MEALS, mealModels as ArrayList<out Parcelable>)
            intent.putExtra(EXTRA_IS_AIRASIA, isAirAsiaAirlines)
            intent.putExtra(EXTRA_REQUEST_ID, requestId)
            intent.putExtra(EXTRA_SELECTED_PASSENGER_ID, bookingPassengerModel.passengerId)
            intent.putExtra(EXTRA_IS_DOMESTIC, isDomestic)
            intent.putExtra(EXTRA_AUTOFILL_NAME, autofillName)
            return intent
        }

        fun getCallingIntent(activity: Activity,
                             depatureId: String,
                             bookingPassengerModel: FlightBookingPassengerModel,
                             luggageModels: List<FlightBookingAmenityMetaModel>,
                             mealModels: List<FlightBookingAmenityMetaModel>,
                             requestId: String,
                             isDomestic: Boolean,
                             autofillName: String = ""): Intent {
            val intent = Intent(activity, FlightBookingPassengerActivity::class.java)
            intent.putExtra(EXTRA_DEPATURE, depatureId)
            intent.putExtra(EXTRA_PASSENGER, bookingPassengerModel)
            intent.putParcelableArrayListExtra(EXTRA_LUGGAGES, luggageModels as ArrayList<out Parcelable>)
            intent.putParcelableArrayListExtra(EXTRA_MEALS, mealModels as ArrayList<out Parcelable>)
            intent.putExtra(EXTRA_REQUEST_ID, requestId)
            intent.putExtra(EXTRA_SELECTED_PASSENGER_ID, bookingPassengerModel.passengerId)
            intent.putExtra(EXTRA_IS_DOMESTIC, isDomestic)
            intent.putExtra(EXTRA_AUTOFILL_NAME, autofillName)
            return intent
        }
    }

}