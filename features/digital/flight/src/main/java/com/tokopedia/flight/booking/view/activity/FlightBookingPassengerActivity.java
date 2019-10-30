package com.tokopedia.flight.booking.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.fragment.FlightBookingPassengerFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.passenger.domain.FlightPassengerUpdateSelectedUseCase;
import com.tokopedia.flight.passenger.view.fragment.FlightPassengerListFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.flight.booking.view.fragment.FlightBookingPassengerFragment.EXTRA_IS_DOMESTIC;

public class FlightBookingPassengerActivity extends BaseSimpleActivity implements HasComponent<FlightBookingComponent>, FlightBookingPassengerFragment.OnFragmentInteractionListener {
    public static final String EXTRA_PASSENGER = "EXTRA_PASSENGER";
    public static final String EXTRA_LUGGAGES = "EXTRA_LUGGAGES";
    public static final String EXTRA_MEALS = "EXTRA_MEALS";
    public static final String EXTRA_DEPARTURE = "EXTRA_DEPARTURE";
    public static final String EXTRA_RETURN = "EXTRA_RETURN";
    public static final String EXTRA_IS_AIRASIA = "EXTRA_IS_AIRASIA";
    public static final String EXTRA_DEPARTURE_DATE = "EXTRA_DEPARTURE_DATE";
    public static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    public static final String EXTRA_SELECTED_PASSENGER_ID = "EXTRA_SELECTED_PASSENGER_ID";
    private FlightBookingPassengerViewModel viewModel;
    private String selectedPassengerId;
    FlightBookingPassengerFragment flightBookingPassengerFragment;

    @Inject
    FlightPassengerUpdateSelectedUseCase flightPassengerUpdateSelectedUseCase;

    public static Intent getCallingIntent(Activity activity,
                                          String departureId,
                                          String returnId,
                                          FlightBookingPassengerViewModel viewModel,
                                          List<FlightBookingAmenityMetaViewModel> luggageViewModels,
                                          List<FlightBookingAmenityMetaViewModel> mealViewModels,
                                          boolean isAirAsiaAirlines,
                                          String departureDate,
                                          String requestId,
                                          boolean isDomestic) {
        Intent intent = new Intent(activity, FlightBookingPassengerActivity.class);
        intent.putExtra(EXTRA_DEPARTURE, departureId);
        intent.putExtra(EXTRA_RETURN, returnId);
        intent.putExtra(EXTRA_PASSENGER, viewModel);
        intent.putExtra(EXTRA_DEPARTURE_DATE, departureDate);
        intent.putParcelableArrayListExtra(EXTRA_LUGGAGES, (ArrayList<? extends Parcelable>) luggageViewModels);
        intent.putParcelableArrayListExtra(EXTRA_MEALS, (ArrayList<? extends Parcelable>) mealViewModels);
        intent.putExtra(EXTRA_IS_AIRASIA, isAirAsiaAirlines);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_SELECTED_PASSENGER_ID, viewModel.getPassengerId());
        intent.putExtra(EXTRA_IS_DOMESTIC, isDomestic);
        return intent;
    }

    public static Intent getCallingIntent(Activity activity,
                                          String departureId,
                                          FlightBookingPassengerViewModel viewModel,
                                          List<FlightBookingAmenityMetaViewModel> luggageViewModels,
                                          List<FlightBookingAmenityMetaViewModel> mealViewModels,
                                          String requestId,
                                          boolean isDomestic) {
        Intent intent = new Intent(activity, FlightBookingPassengerActivity.class);
        intent.putExtra(EXTRA_DEPARTURE, departureId);
        intent.putExtra(EXTRA_PASSENGER, viewModel);
        intent.putParcelableArrayListExtra(EXTRA_LUGGAGES, (ArrayList<? extends Parcelable>) luggageViewModels);
        intent.putParcelableArrayListExtra(EXTRA_MEALS, (ArrayList<? extends Parcelable>) mealViewModels);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_SELECTED_PASSENGER_ID, viewModel.getPassengerId());
        intent.putExtra(EXTRA_IS_DOMESTIC, isDomestic);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = (savedInstanceState != null) ? savedInstanceState : getIntent().getExtras();
        viewModel = extras.getParcelable(EXTRA_PASSENGER);
        selectedPassengerId = extras.getString(EXTRA_SELECTED_PASSENGER_ID);

        super.onCreate(savedInstanceState);
        getComponent().inject(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PASSENGER, viewModel);
        outState.putString(EXTRA_SELECTED_PASSENGER_ID, selectedPassengerId);
    }

    @Override
    protected Fragment getNewFragment() {
        List<FlightBookingAmenityMetaViewModel> luggageViewModels = getIntent().getParcelableArrayListExtra(EXTRA_LUGGAGES);
        List<FlightBookingAmenityMetaViewModel> mealViewModels = getIntent().getParcelableArrayListExtra(EXTRA_MEALS);
        if (getIntent().getStringExtra(EXTRA_RETURN) != null) {
            flightBookingPassengerFragment = FlightBookingPassengerFragment.newInstance(
                    getIntent().getStringExtra(EXTRA_DEPARTURE),
                    getIntent().getStringExtra(EXTRA_RETURN),
                    viewModel, luggageViewModels, mealViewModels,
                    getIntent().getBooleanExtra(EXTRA_IS_AIRASIA, false),
                    getIntent().getStringExtra(EXTRA_DEPARTURE_DATE),
                    getIntent().getStringExtra(EXTRA_REQUEST_ID),
                    getIntent().getBooleanExtra(EXTRA_IS_DOMESTIC, false)
            );
        } else {
            flightBookingPassengerFragment =  FlightBookingPassengerFragment.newInstance(
                    getIntent().getStringExtra(EXTRA_DEPARTURE),
                    viewModel,
                    luggageViewModels,
                    mealViewModels,
                    getIntent().getBooleanExtra(EXTRA_IS_AIRASIA, false),
                    getIntent().getStringExtra(EXTRA_DEPARTURE_DATE),
                    getIntent().getStringExtra(EXTRA_REQUEST_ID),
                    getIntent().getBooleanExtra(EXTRA_IS_DOMESTIC, false)
            );
        }
        return flightBookingPassengerFragment;
    }

    @Override
    public FlightBookingComponent getComponent() {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }

    @Override
    public void actionSuccessUpdatePassengerData(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_PASSENGER, flightBookingPassengerViewModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void updatePassengerViewModel(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        viewModel = flightBookingPassengerViewModel;
    }

    @Override
    public void onBackPressed() {
        if (selectedPassengerId == null && viewModel.getPassengerId() != null) {
            unselectPassenger();
        } else if (viewModel.getPassengerId() != null && selectedPassengerId != null &&
                !viewModel.getPassengerId().equals(selectedPassengerId)) {
            unselectPassenger();
        } else {
            super.onBackPressed();
        }
    }

    private void unselectPassenger() {
        flightPassengerUpdateSelectedUseCase.execute(
                flightPassengerUpdateSelectedUseCase.createRequestParams(
                        viewModel.getPassengerId(),
                        FlightPassengerListFragment.IS_NOT_SELECTING
                ),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        FlightBookingPassengerActivity.super.onBackPressed();
                    }
                }
        );
    }
}
