package com.tokopedia.flight.passenger.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.passenger.di.DaggerFlightPassengerComponent;
import com.tokopedia.flight.passenger.di.FlightPassengerComponent;
import com.tokopedia.flight.passenger.view.fragment.FlightPassengerUpdateFragment;

import static com.tokopedia.flight.passenger.view.fragment.FlightPassengerUpdateFragment.EXTRA_DEPARTURE_DATE;
import static com.tokopedia.flight.passenger.view.fragment.FlightPassengerUpdateFragment.EXTRA_IS_DOMESTIC;
import static com.tokopedia.flight.passenger.view.fragment.FlightPassengerUpdateFragment.EXTRA_PASSENGER_VIEW_MODEL;
import static com.tokopedia.flight.passenger.view.fragment.FlightPassengerUpdateFragment.EXTRA_REQUEST_ID;

public class FlightPassengerUpdateActivity extends BaseSimpleActivity
        implements HasComponent<FlightPassengerComponent> {

    public static Intent getCallingIntent(Activity activity,
                                          FlightBookingPassengerViewModel passengerViewModel,
                                          String departureDate, String requestId, boolean isDomestic) {
        Intent intent = new Intent(activity, FlightPassengerUpdateActivity.class);
        intent.putExtra(EXTRA_PASSENGER_VIEW_MODEL, passengerViewModel);
        intent.putExtra(EXTRA_DEPARTURE_DATE, departureDate);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_IS_DOMESTIC, isDomestic);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getIntent().getExtras()
                .getParcelable(EXTRA_PASSENGER_VIEW_MODEL);
        return FlightPassengerUpdateFragment.newInstance(
                flightBookingPassengerViewModel,
                getIntent().getExtras().getString(EXTRA_DEPARTURE_DATE),
                getIntent().getExtras().getString(EXTRA_REQUEST_ID),
                getIntent().getExtras().getBoolean(EXTRA_IS_DOMESTIC)
        );
    }

    @Override
    public FlightPassengerComponent getComponent() {
        return DaggerFlightPassengerComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }

    @Override
    public void onBackPressed() {
        showCancelUpdateDialog();
    }

    private void showCancelUpdateDialog() {
        final Dialog dialog = new Dialog(this, Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(com.tokopedia.flight.R.string.flight_passenger_update_abort_dialog_title));
        dialog.setDesc(getString(com.tokopedia.flight.R.string.flight_passenger_update_abort_dialog_description));
        dialog.setBtnOk(getString(com.tokopedia.flight.R.string.flight_passenger_update_ok_button));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setBtnCancel(getString(com.tokopedia.flight.R.string.flight_passenger_update_cancel_button));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }
}
