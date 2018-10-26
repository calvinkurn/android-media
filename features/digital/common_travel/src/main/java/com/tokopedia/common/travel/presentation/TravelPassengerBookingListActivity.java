package com.tokopedia.common.travel.presentation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.io.Serializable;
import java.util.List;

public class TravelPassengerBookingListActivity extends BaseSimpleActivity implements TravelPassengerBookingListFragment.ActionListener {

    public static final String PASSENGER_DATA = "passenger_data";
    public static final String PASSENGER_LIST = "passenger_list";
    private TravelPassenger travelPassenger;
    private List<String> currentPassengerList;

    public static Intent callingIntent(Context context,  TravelPassenger trainPassengerViewModel,
                                       List<String> currentPassengerList) {
        Intent intent = new Intent(context, TravelPassengerBookingListActivity.class);
        intent.putExtra(PASSENGER_DATA, trainPassengerViewModel);
        intent.putExtra(PASSENGER_LIST, (Serializable) currentPassengerList);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        travelPassenger = getIntent().getParcelableExtra(PASSENGER_DATA);
        currentPassengerList = getIntent().getStringArrayListExtra(PASSENGER_LIST);
        return TravelPassengerBookingListFragment.newInstance(travelPassenger, currentPassengerList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateTitle("Daftar Penumpang");
    }

    @Override
    public void onBackPressed() {
        KeyboardHandler.hideSoftKeyboard(this);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_passenger_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_passenger_list_edit) {
            Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();
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
}
