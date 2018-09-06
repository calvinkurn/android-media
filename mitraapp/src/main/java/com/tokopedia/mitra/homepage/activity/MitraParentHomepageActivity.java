package com.tokopedia.mitra.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.component.BottomNavigation;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.account.fragment.MitraAccountFragment;
import com.tokopedia.mitra.common.MitraComponentInstance;
import com.tokopedia.mitra.homepage.contract.MitraParentHomepageContract;
import com.tokopedia.mitra.homepage.di.DaggerMitraHomepageComponent;
import com.tokopedia.mitra.homepage.di.MitraHomepageComponent;
import com.tokopedia.mitra.homepage.fragment.MitraHelpFragment;
import com.tokopedia.mitra.homepage.fragment.MitraHomepageFragment;
import com.tokopedia.mitra.homepage.presenter.MitraParentHomepagePresenter;
import com.tokopedia.session.login.loginphonenumber.view.activity.LoginPhoneNumberActivity;

import javax.inject.Inject;

public class MitraParentHomepageActivity extends BaseSimpleActivity implements MitraParentHomepageContract.View, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CODE_LOGIN_THEN_ACCOUNT = 1001;

    private BottomNavigation homeNavigation;

    @Inject
    MitraParentHomepagePresenter presenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_mitra_parent_homepage;
    }

    @Override
    protected Fragment getNewFragment() {
        return MitraHomepageFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        setupToolbar();
        homeNavigation = findViewById(R.id.mitra_bottom_nav);
        homeNavigation.setOnNavigationItemSelectedListener(this);
        presenter.attachView(this);
    }

    private void initInjector() {
        MitraHomepageComponent component = DaggerMitraHomepageComponent.builder()
                .mitraComponent(MitraComponentInstance.getComponent(getApplication()))
                .build();
        component.inject(this);
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mitra_home:
                presenter.onHomepageMenuClicked();
                break;
            case R.id.menu_mitra_help:
                presenter.onHelpMenuClicked();
                break;
            case R.id.menu_mitra_account:
                presenter.onAccountMenuClicked();
                break;
        }
        return false;
    }

    private void inflateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent_view, fragment, getTagFragment())
                .commitNow();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MitraParentHomepageActivity.class);
    }

    @Override
    public void inflateHomepageFragment() {
        inflateFragment(MitraHomepageFragment.newInstance());
    }

    @Override
    public void setHomepageMenuSelected() {
        setSelectedNavigationMenu(R.id.menu_mitra_home);
    }

    @Override
    public void inflateAccountFragment() {
        inflateFragment(MitraAccountFragment.newInstance());
    }

    @Override
    public void inflateHelpFragment() {
        inflateFragment(MitraHelpFragment.newInstance());
    }

    @Override
    public void setAccountMenuSelected() {
        setSelectedNavigationMenu(R.id.menu_mitra_account);
    }

    @Override
    public void setHelpMenuSelected() {
        setSelectedNavigationMenu(R.id.menu_mitra_help);
    }

    private void setSelectedNavigationMenu(int itemId) {
        homeNavigation.setOnNavigationItemSelectedListener(null);
        homeNavigation.setCurrentItem(homeNavigation.getMenuItemPosition(itemId));
        homeNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void navigateToLoggedInThenAccountPage() {
        startActivityForResult(LoginPhoneNumberActivity.getCallingIntent(this), REQUEST_CODE_LOGIN_THEN_ACCOUNT);
    }

    @Override
    public void showErrorMessageInSnackbar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(this, getString(resId));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN_THEN_ACCOUNT:
                presenter.onLoginFromAccountResultReceived();
                break;
        }
    }
}
