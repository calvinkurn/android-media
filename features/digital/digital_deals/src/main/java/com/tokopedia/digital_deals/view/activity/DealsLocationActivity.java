package com.tokopedia.digital_deals.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.transition.Fade;
import android.transition.Transition;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.SelectLocationFragment;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.model.Location;


public class DealsLocationActivity extends DealsBaseActivity {

    private Location location;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fade = new Fade();
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setExitTransition(fade);
            getWindow().setEnterTransition(fade);
        }


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

    @Override
    public void finish() {
        super.finish();
        if(location!=null) {
            overridePendingTransition(R.anim.hold, R.anim.slide_out_up);
        }

    }
}
