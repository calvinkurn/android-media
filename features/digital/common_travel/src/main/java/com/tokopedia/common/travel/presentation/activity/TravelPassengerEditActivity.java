package com.tokopedia.common.travel.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.common.travel.presentation.fragment.TravelPassengerEditFragment;

/**
 * Created by nabillasabbaha on 13/11/18.
 */
public class TravelPassengerEditActivity extends BaseSimpleActivity {

    public static Intent callingIntent(Context context) {
        Intent intent = new Intent(context, TravelPassengerEditActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TravelPassengerEditFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateTitle("Edit Penumpang");
    }
}
