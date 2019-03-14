package com.tokopedia.digital_deals.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.SelectLocationFragment;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.model.Location;


public class DealsLocationActivity extends BaseSimpleActivity {

    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        location= Utils.getSingletonInstance().getLocation(this);
//        if(location!=null) {
//            toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_close_deals));
//        }

        SelectLocationFragment selectLocationFragment = new SelectLocationFragment();
        selectLocationFragment.show(getSupportFragmentManager(), "");

    }

    @Override
    protected Fragment getNewFragment() {
        toolbar.setVisibility(View.GONE);
        return null;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if(location!=null) {
//            overridePendingTransition(R.anim.hold, R.anim.slide_out_up);
//        }
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        if(location!=null) {
//            overridePendingTransition(R.anim.hold, R.anim.slide_out_up);
//        }
//
//    }
}
