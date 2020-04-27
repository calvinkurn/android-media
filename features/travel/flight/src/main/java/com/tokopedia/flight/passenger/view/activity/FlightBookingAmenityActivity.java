package com.tokopedia.flight.passenger.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.bookingV2.presentation.adapter.viewholder.FlightBookingAmenityViewHolder;
import com.tokopedia.flight.bookingV2.presentation.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.bookingV2.presentation.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.passenger.view.fragment.FlightBookingAmenityFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingAmenityActivity extends BaseSimpleActivity {
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    public static Intent createIntent(Context context, String title, List<FlightBookingAmenityViewModel> flightBookingAmenityViewModels,
                                      FlightBookingAmenityMetaViewModel selectedLuggage) {
        Intent intent = new Intent(context, FlightBookingAmenityActivity.class);
        intent.putParcelableArrayListExtra(FlightBookingAmenityFragment.EXTRA_LIST_AMENITIES, (ArrayList<? extends Parcelable>) flightBookingAmenityViewModels);
        intent.putExtra(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES, selectedLuggage);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        FlightBookingAmenityMetaViewModel metaViewModel = getIntent().getParcelableExtra(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES);
        return FlightBookingAmenityFragment.createInstance(getIntent().<FlightBookingAmenityMetaViewModel>getParcelableArrayListExtra(FlightBookingAmenityFragment.EXTRA_LIST_AMENITIES),
                metaViewModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getIntent().getStringExtra(EXTRA_TITLE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.tokopedia.flight.R.menu.menu_flight_amenity_info_reset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.tokopedia.flight.R.id.action_reset) {
            onResetClicked();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void onResetClicked() {
        Fragment f = getCurrentFragment();
        if (f != null && f instanceof FlightBookingAmenityViewHolder.ListenerCheckedLuggage) {
            ((FlightBookingAmenityViewHolder.ListenerCheckedLuggage) f).resetItemCheck();
        }
    }

    private Fragment getCurrentFragment() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (int i = 0, sizei = fragmentList.size(); i < sizei; i++) {
            Fragment fragment = fragmentList.get(i);
            if (fragment.isAdded() && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }
}
