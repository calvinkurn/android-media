package com.tokopedia.tkpd.home;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.react.ReactConst;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.fragment.ReactNativeOfficialStoreFragment;

public class ReactNativeOfficialStoresActivity extends BasePresenterActivity {
    public static final String USER_ID = "User_ID";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    @DeepLink({Constants.Applinks.OFFICIAL_STORES})
    public static Intent getOfficialStoresApplinkCallingIntent(Context context, Bundle bundle) {
        return ReactNativeOfficialStoresActivity.createReactNativeActivity(
                context, ReactConst.Screen.OFFICIAL_STORE,
                SessionHandler.getLoginID(context),
                context.getString(com.tokopedia.tkpd.R.string.react_native_banner_official_title)
        ).putExtras(bundle);
    }

    public static Intent createReactNativeActivity(Context context,
                                                   String reactScreenName,
                                                   String userId,
                                                   String pageTitle) {
        Intent intent = new Intent(context, ReactNativeOfficialStoresActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(USER_ID, userId);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
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
        return R.layout.activity_react_native_official_stores;
    }

    @Override
    protected void initView() {
        setToolbar();
        Bundle initialProps = getIntent().getExtras();
        ReactNativeOfficialStoreFragment fragment = ReactNativeOfficialStoreFragment.createInstance(initialProps);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    private void setToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            int flags = view.getSystemUiVisibility();

            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_700));
        if (getIntent() != null && getIntent().getExtras() != null) {
            String title = getIntent().getExtras().getString(EXTRA_TITLE);
            if (!TextUtils.isEmpty(title)) {
                toolbar.setTitle(title);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
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
