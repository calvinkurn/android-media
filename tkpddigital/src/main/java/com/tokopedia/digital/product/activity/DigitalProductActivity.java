package com.tokopedia.digital.product.activity;

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
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.fragment.DigitalProductFragment;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class DigitalProductActivity extends BasePresenterActivity {
    public static final String EXTRA_CATEGORY_PASS_DATA = "EXTRA_CATEGORY_PASS_DATA";
    private DigitalCategoryDetailPassData passData;

    public static Intent newInstance(Context context, DigitalCategoryDetailPassData passData) {
        return new Intent(context, DigitalProductActivity.class)
                .putExtra(EXTRA_CATEGORY_PASS_DATA, passData);
    }

    @DeepLink({Constants.Applinks.DIGITAL, Constants.Applinks.DIGITAL_PRODUCT})
    public static TaskStackBuilder getCallingApplinksTaskStask(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();

        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }
        homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                HomeRouter.INIT_STATE_FRAGMENT_HOME);

        Intent destination = new Intent(context, DigitalProductActivity.class)
                .setData(uri.build())
                .putExtras(extras);
        destination.putExtra(Constants.EXTRA_FROM_PUSH, true);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }


    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.passData = extras.getParcelable(EXTRA_CATEGORY_PASS_DATA);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart_digital_module;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof DigitalProductFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    DigitalProductFragment.newInstance(passData.getCategoryId())).commit();
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


}
