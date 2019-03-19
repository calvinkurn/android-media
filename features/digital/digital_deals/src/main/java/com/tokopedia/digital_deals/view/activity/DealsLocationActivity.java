package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;


public class DealsLocationActivity extends BaseSimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        toolbar.setVisibility(View.GONE);
        return null;
    }
}
