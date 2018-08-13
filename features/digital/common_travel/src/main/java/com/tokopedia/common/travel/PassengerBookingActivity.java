package com.tokopedia.common.travel;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PassengerBookingActivity extends AppCompatActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, PassengerBookingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_booking);
    }
}
