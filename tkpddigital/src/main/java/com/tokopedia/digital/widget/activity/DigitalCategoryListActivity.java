package com.tokopedia.digital.widget.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.widget.fragment.DigitalCategoryListFragment;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListActivity extends BasePresenterActivity {

    @Override
    public String getScreenName() {
        return DigitalCategoryListActivity.class.getSimpleName();
    }

    @SuppressWarnings("unused")
    @DeepLink({Constants.Applinks.DIGITAL_CATEGORY, Constants.Applinks.DIGITAL})
    public static TaskStackBuilder getCallingApplinksTaskStask(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();

        if (extras != null && extras.getBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, false)) {
            Intent homeIntent;
            if (GlobalConfig.isSellerApp()) {
                homeIntent = SellerAppRouter.getSellerHomeActivity(context);
            } else {
                homeIntent = HomeRouter.getHomeActivity(context);
            }
            homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                    HomeRouter.INIT_STATE_FRAGMENT_HOME);
            taskStackBuilder.addNextIntent(homeIntent);
        }

        Intent destination = DigitalCategoryListActivity.newInstance(context);
        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, DigitalCategoryListActivity.class);
    }

    public static Intent newInstance(Context context, boolean isFromSeller) {
        Intent intent = new Intent(context, DigitalCategoryListActivity.class);
        return intent;
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
        return R.layout.activity_digital_category_list_digital_module;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof DigitalCategoryListFragment)) {
            DigitalCategoryListFragment digitalCategoryListFragment
                    = DigitalCategoryListFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.container,
                    digitalCategoryListFragment).commit();
        }
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
