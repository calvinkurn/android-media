package com.tokopedia.core.tracking.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;

import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.tracking.fragment.TrackingFragment;
import com.tokopedia.core.tracking.presenter.TrackingFragmentPresenterImpl;

/**
 * Created by Alifa on 10/12/2016.
 */

public class TrackingActivity extends BasePresenterActivity {

    private static final String TAG = "TRACKING_FRAGMENT";

    public static Intent createInstance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, TrackingActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_TRACKING_DETAIL;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tracking;
    }

    @Override
    protected void initView() {
        TrackingFragment fragment = TrackingFragment.createInstance(getIntent().getExtras().getString("OrderID"));
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, TAG);
        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TrackingFragmentPresenterImpl.REQUEST_TRACKING_CODE && resultCode == Activity.RESULT_OK){
            ((TrackingFragment)getFragmentManager().findFragmentByTag(TAG)).refresh();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
