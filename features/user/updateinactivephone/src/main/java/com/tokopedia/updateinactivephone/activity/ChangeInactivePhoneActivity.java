package com.tokopedia.updateinactivephone.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.fragment.ChangeInactivePhoneFragment;

public class ChangeInactivePhoneActivity extends BaseSimpleActivity implements HasComponent<AppComponent> {

    private final static String TITLE = "title";

    @DeepLink({ApplinkConst.CHANGE_INACTIVE_PHONE})
    public static Intent getCallingApplinkIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        Intent intent = getChangeInactivePhoneIntent(context);
        return intent.setData(uri.build());
    }

    public static Intent getChangeInactivePhoneIntent(Context context) {
        return new Intent(context, ChangeInactivePhoneActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return ChangeInactivePhoneFragment.getInstance();
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        setupToolbar();
        initView();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.change_inactive_phone_layout;
    }

    private void initView() {

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (ChangeInactivePhoneFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = ChangeInactivePhoneFragment.getInstance();
        }
        fragmentTransaction.replace(R.id.parent_view, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }


    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_icon_back_black);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }
}
