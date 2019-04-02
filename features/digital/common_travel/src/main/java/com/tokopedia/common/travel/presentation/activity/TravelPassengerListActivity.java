package com.tokopedia.common.travel.presentation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.presentation.fragment.TravelPassengerListFragment;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.presentation.model.TravelTrip;

public class TravelPassengerListActivity extends BaseSimpleActivity implements TravelPassengerListFragment.ActionListener {

    public static final String PASSENGER_DATA = "passenger_data";
    public static final String TRAVEL_TRIP = "travel_trip";
    public static final String RESET_PASSENGER_LIST_SELECTED = "reset_passenger_list";
    private TravelTrip travelTrip;
    private TravelPassenger travelPassenger;

    public static Intent callingIntent(Context context,
                                       TravelTrip travelTrip,
                                       boolean resetPassengerListSelected) {
        Intent intent = new Intent(context, TravelPassengerListActivity.class);
        intent.putExtra(TRAVEL_TRIP, travelTrip);
        intent.putExtra(RESET_PASSENGER_LIST_SELECTED, resetPassengerListSelected);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        boolean resetPassengerList = getIntent().getBooleanExtra(RESET_PASSENGER_LIST_SELECTED, false);
        return TravelPassengerListFragment.newInstance((TravelTrip) getIntent().getParcelableExtra(TRAVEL_TRIP), resetPassengerList);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TRAVEL_TRIP, travelTrip);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            travelTrip = savedInstanceState.getParcelable(TRAVEL_TRIP);
        } else {
            travelTrip = (TravelTrip) getIntent().getParcelableExtra(TRAVEL_TRIP);
        }

        updateTitle(getString(R.string.travel_title_list_passenger_page));
    }

    @Override
    public void onBackPressed() {
        KeyboardHandler.hideSoftKeyboard(this);
        if (this.travelPassenger != null) {
            onClickPassenger(this.travelPassenger);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_passenger_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_menu_passenger_list_edit);
        SpannableString s = new SpannableString(item.getTitle());
        ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(),
                R.color.tkpd_main_green));
        s.setSpan(span, 0, s.length(), 0);
        item.setTitle(s);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_passenger_list_edit) {
            supportInvalidateOptionsMenu();
            startActivity(TravelPassengerEditActivity.callingIntent(getApplicationContext(), travelTrip));
            overridePendingTransition(R.anim.travel_slide_up_in, R.anim.travel_anim_stay);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickPassenger(TravelPassenger travelPassenger) {
        Intent intent = new Intent();
        intent.putExtra(PASSENGER_DATA, travelPassenger);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressedActivity(TravelPassenger travelPassenger) {
        this.travelPassenger = travelPassenger;
    }
}
