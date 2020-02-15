package com.tokopedia.flight.cancellation.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationTermsAndConditionsFragment;

public class FlightCancellationTermsAndConditionsActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, FlightCancellationTermsAndConditionsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightCancellationTermsAndConditionsFragment.createInstance();
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
