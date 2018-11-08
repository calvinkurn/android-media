package com.tokopedia.flight.booking.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.fragment.FlightBookingFragment;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.passenger.domain.FlightPassengerDeleteAllListUseCase;
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 11/6/17.
 */

public class FlightBookingActivity extends BaseFlightActivity implements HasComponent<FlightBookingComponent> {
    private static final String EXTRA_PASS_SEARCH_DATA = "EXTRA_PASS_SEARCH_DATA";
    private static final String EXTRA_FLIGHT_DEPARTURE_ID = "EXTRA_FLIGHT_DEPARTURE_ID";
    private static final String EXTRA_FLIGHT_ARRIVAL_ID = "EXTRA_FLIGHT_ARRIVAL_ID";
    private static final String EXTRA_PRICE = "EXTRA_PRICE";

    private FlightBookingFragment flightBookingFragment;

    @Inject
    FlightPassengerDeleteAllListUseCase flightPassengerDeleteAllListUseCase;

    @Deprecated
    public static Intent getCallingIntent(Activity activity, FlightSearchPassDataViewModel passDataViewModel, String departureId) {
        Intent intent = new Intent(activity, FlightBookingActivity.class);
        intent.putExtra(EXTRA_FLIGHT_DEPARTURE_ID, departureId);
        intent.putExtra(EXTRA_PASS_SEARCH_DATA, passDataViewModel);
        return intent;
    }

    public static Intent getCallingIntent(Activity activity, FlightSearchPassDataViewModel passDataViewModel, String departureId, FlightPriceViewModel priceViewModel) {
        Intent intent = new Intent(activity, FlightBookingActivity.class);
        intent.putExtra(EXTRA_FLIGHT_DEPARTURE_ID, departureId);
        intent.putExtra(EXTRA_PASS_SEARCH_DATA, passDataViewModel);
        intent.putExtra(EXTRA_PRICE, priceViewModel);
        return intent;
    }

    @Deprecated
    public static Intent getCallingIntent(Activity activity, FlightSearchPassDataViewModel passDataViewModel, String departureId, String returnId) {
        Intent intent = new Intent(activity, FlightBookingActivity.class);
        intent.putExtra(EXTRA_FLIGHT_DEPARTURE_ID, departureId);
        intent.putExtra(EXTRA_FLIGHT_ARRIVAL_ID, returnId);
        intent.putExtra(EXTRA_PASS_SEARCH_DATA, passDataViewModel);
        return intent;
    }

    public static Intent getCallingIntent(Activity activity, FlightSearchPassDataViewModel passDataViewModel, String departureId, String returnId, FlightPriceViewModel priceViewModel) {
        Intent intent = new Intent(activity, FlightBookingActivity.class);
        intent.putExtra(EXTRA_FLIGHT_DEPARTURE_ID, departureId);
        intent.putExtra(EXTRA_PASS_SEARCH_DATA, passDataViewModel);
        intent.putExtra(EXTRA_FLIGHT_ARRIVAL_ID, returnId);
        intent.putExtra(EXTRA_PRICE, priceViewModel);
        return intent;
    }

    @Override
    public String getScreenName() {
        return FlightAnalytics.Screen.BOOKING;
    }

    @Override
    protected Fragment getNewFragment() {
        String departureId = getIntent().getStringExtra(EXTRA_FLIGHT_DEPARTURE_ID);
        String arrivalId = getIntent().getStringExtra(EXTRA_FLIGHT_ARRIVAL_ID);
        FlightSearchPassDataViewModel searchPassDataViewModel = getIntent().getParcelableExtra(EXTRA_PASS_SEARCH_DATA);
        FlightPriceViewModel priceViewModel = getIntent().getParcelableExtra(EXTRA_PRICE);
        flightBookingFragment = FlightBookingFragment.newInstance(searchPassDataViewModel, departureId, arrivalId, priceViewModel);
        return flightBookingFragment;
    }

    @Override
    public FlightBookingComponent getComponent() {
        if (getApplication() instanceof FlightModuleRouter) {
            return DaggerFlightBookingComponent.builder()
                    .flightComponent(getFlightComponent())
                    .build();
        }
        throw new RuntimeException("Application must implement FlightModuleRouter");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public void onBackPressed() {
        deleteAllPassengerList();
    }

    private void deleteAllPassengerList() {
        flightPassengerDeleteAllListUseCase.execute(
                flightPassengerDeleteAllListUseCase.createEmptyRequestParams(),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        FlightBookingActivity.super.onBackPressed();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        FlightBookingActivity.super.onBackPressed();
                    }
                }
        );
    }
}
