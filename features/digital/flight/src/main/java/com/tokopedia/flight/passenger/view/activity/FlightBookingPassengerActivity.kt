package com.tokopedia.flight.passenger.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent
import com.tokopedia.flight.booking.di.FlightBookingComponent
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel
import com.tokopedia.flight.passenger.view.fragment.FlightBookingPassengerFragment
import java.util.ArrayList

/**
 * @author by jessica on 2019-09-04
 */

class FlightBookingPassengerActivity: BaseSimpleActivity(), HasComponent<FlightBookingComponent>,
        FlightBookingPassengerFragment.OnFragmentInteractionListener {

    var passengerModel: FlightBookingPassengerViewModel? = null
    var selectedPassengerId: String? = null

//    @Inject lateinit var flightPassengerUpdateSelectedUseCase: FlightPassengerUpdateSelectedUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = savedInstanceState ?: intent.extras
        passengerModel = extras.getParcelable(EXTRA_PASSENGER)
        selectedPassengerId = extras.getString(EXTRA_SELECTED_PASSENGER_ID)

        super.onCreate(savedInstanceState)
//        component.inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(EXTRA_PASSENGER, passengerModel)
        outState?.putString(EXTRA_SELECTED_PASSENGER_ID, selectedPassengerId)
    }

    override fun getNewFragment(): Fragment {
        return FlightBookingPassengerFragment.newInstance(
                    depatureId = intent.getStringExtra(EXTRA_DEPATURE),
                    passengerModel = passengerModel ?: FlightBookingPassengerViewModel(),
                    luggageModels = intent.getParcelableArrayListExtra(EXTRA_LUGGAGES),
                    mealModels = intent.getParcelableArrayListExtra(EXTRA_MEALS),
                    isAirAsiaAirlines = intent.getBooleanExtra(EXTRA_IS_AIRASIA, false),
                    depatureDate = intent.getStringExtra(EXTRA_DEPARTURE_DATE),
                    requestId = intent.getStringExtra(EXTRA_REQUEST_ID),
                    isDomestic = intent.getBooleanExtra(EXTRA_IS_DOMESTIC, false),
                    returnId = intent.getStringExtra(EXTRA_RETURN))
    }

    override fun getComponent(): FlightBookingComponent {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(application))
                .build()
    }

    override fun actionSuccessUpdatePassengerData(flightBookingPassengerViewModel: FlightBookingPassengerViewModel) {
        val intent = intent
        intent.putExtra(EXTRA_PASSENGER, flightBookingPassengerViewModel)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

//    override fun onBackPressed() {
//        if (selectedPassengerId == null && passengerModel?.passengerId != null) {
//            unselectPassenger()
//        } else if (passengerModel?.passengerId != null && selectedPassengerId != null &&
//                !passengerModel?.getPassengerId().equals(selectedPassengerId)) {
//            unselectPassenger()
//        } else super.onBackPressed()
//    }

    override fun updatePassengerViewModel(flightBookingPassengerViewModel: FlightBookingPassengerViewModel) {
        passengerModel = flightBookingPassengerViewModel
    }

//    private fun unselectPassenger() {
//        flightPassengerUpdateSelectedUseCase.execute(
//                flightPassengerUpdateSelectedUseCase.createRequestParams(
//                        passengerModel?.getPassengerId(),
//                        FlightPassengerListFragment.IS_NOT_SELECTING
//                ),
//                object : Subscriber<Boolean>() {
//                    override fun onCompleted() {
//
//                    }
//
//                    override fun onError(throwable: Throwable) {
//                        throwable.printStackTrace()
//                    }
//
//                    override fun onNext(aBoolean: Boolean?) {
//                        super@FlightBookingPassengerActivity.onBackPressed()
//                    }
//                }
//        )
//    }

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

        fun getCallingIntent(activity: Activity,
                             depatureId: String,
                             returnId: String,
                             bookingPassengerModel: FlightBookingPassengerViewModel,
                             luggageModels: List<FlightBookingAmenityMetaViewModel>,
                             mealModels: List<FlightBookingAmenityMetaViewModel>,
                             isAirAsiaAirlines: Boolean,
                             depatureDate: String,
                             requestId: String,
                             isDomestic: Boolean): Intent {
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
            return intent
        }

        fun getCallingIntent(activity: Activity,
                             depatureId: String,
                             bookingPassengerModel: FlightBookingPassengerViewModel,
                             luggageModels: List<FlightBookingAmenityMetaViewModel>,
                             mealModels: List<FlightBookingAmenityMetaViewModel>,
                             requestId: String,
                             isDomestic: Boolean): Intent {
            val intent = Intent(activity, FlightBookingPassengerActivity::class.java)
            intent.putExtra(EXTRA_DEPATURE, depatureId)
            intent.putExtra(EXTRA_PASSENGER, bookingPassengerModel)
            intent.putParcelableArrayListExtra(EXTRA_LUGGAGES, luggageModels as ArrayList<out Parcelable>)
            intent.putParcelableArrayListExtra(EXTRA_MEALS, mealModels as ArrayList<out Parcelable>)
            intent.putExtra(EXTRA_REQUEST_ID, requestId)
            intent.putExtra(EXTRA_SELECTED_PASSENGER_ID, bookingPassengerModel.passengerId)
            intent.putExtra(EXTRA_IS_DOMESTIC, isDomestic)
            return intent
        }
    }

}