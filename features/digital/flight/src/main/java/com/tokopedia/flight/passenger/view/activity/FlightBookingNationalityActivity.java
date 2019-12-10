package com.tokopedia.flight.passenger.view.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.bookingV2.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.bookingV2.di.FlightBookingComponent;
import com.tokopedia.flight.passenger.view.fragment.FlightBookingNationalityFragment;

import static com.tokopedia.flight.passenger.view.fragment.FlightBookingNationalityFragment.EXTRA_SEARCH_HINT;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingNationalityActivity extends BaseSimpleActivity implements HasComponent<FlightBookingComponent> {

    public static Intent createIntent(Context context, String searchHint) {
        Intent intent = new Intent(context, FlightBookingNationalityActivity.class);
        intent.putExtra(EXTRA_SEARCH_HINT, searchHint);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightBookingNationalityFragment.createInstance(
                getIntent().getExtras().getString(EXTRA_SEARCH_HINT)
        );
    }

    @Override
    public FlightBookingComponent getComponent() {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }

    @Override
    public void onBackPressed() {
        KeyboardHandler.hideSoftKeyboard(this);
        super.onBackPressed();
    }
}
