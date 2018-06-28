package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.SelectLocationFragment;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;


public class DealsLocationActivity extends BaseSimpleActivity {

    private LocationViewModel location;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location= Utils.getSingletonInstance().getLocation(this);
        if(location!=null) {
            toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_close_deals));
        }

    }

    @Override
    protected Fragment getNewFragment() {
        return SelectLocationFragment.createInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(location!=null) {

            overridePendingTransition(R.anim.hold, R.anim.slide_out_up);
        }

    }
}
