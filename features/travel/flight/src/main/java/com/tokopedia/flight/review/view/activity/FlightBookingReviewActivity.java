package com.tokopedia.flight.review.view.activity;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.bookingV2.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.bookingV2.di.FlightBookingComponent;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.review.view.fragment.FlightBookingReviewFragment;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;

/**
 * Created by zulfikarrahman on 11/9/17.
 */

public class FlightBookingReviewActivity extends BaseFlightActivity implements HasComponent<FlightBookingComponent> {
    public static final String EXTRA_COMBO_KEY = "EXTRA_COMBO_KEY";

    public static Intent createIntent(Context context, FlightBookingReviewModel flightBookingReviewModel, String comboKey){
        Intent intent = new Intent(context, FlightBookingReviewActivity.class);
        intent.putExtra(FlightBookingReviewFragment.EXTRA_DATA_REVIEW, flightBookingReviewModel);
        intent.putExtra(EXTRA_COMBO_KEY, comboKey);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightBookingReviewFragment.createInstance(
                (FlightBookingReviewModel) getIntent().getParcelableExtra(FlightBookingReviewFragment.EXTRA_DATA_REVIEW),
                getIntent().getStringExtra(EXTRA_COMBO_KEY)
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
        if (getFragment() instanceof OnBackActionListener) {
            ((OnBackActionListener) getFragment()).onBackPressed();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public String getScreenName() {
        return FlightAnalytics.Screen.REVIEW;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
