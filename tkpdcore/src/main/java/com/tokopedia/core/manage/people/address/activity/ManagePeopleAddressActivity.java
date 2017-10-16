package com.tokopedia.core.manage.people.address.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.manage.people.address.listener.MPAddressActivityListener;
import com.tokopedia.core.manage.people.address.presenter.ManagePeopleAddressImpl;
import com.tokopedia.core.manage.people.address.presenter.ManagePeopleAddressPresenter;
import com.tokopedia.core.manage.people.address.service.ManagePeopleAddressReceiver;
import com.tokopedia.core.manage.people.address.service.ManagePeopleAddressService;

import butterknife.BindView;

public class ManagePeopleAddressActivity extends BasePresenterActivity<ManagePeopleAddressPresenter>
        implements MPAddressActivityListener, ManagePeopleAddressReceiver.Receiver {

    @BindView(R2.id.fab)
    FloatingActionButton fab;

    private Uri uriData;
    private Bundle bundleData;
    private ManagePeopleAddressReceiver mReceiver;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONFIG_P_ADDRESS;
    }

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = data;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.bundleData = extras;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ManagePeopleAddressImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage_people_address2;
    }

    @Override
    protected void initView() {
        presenter.initFragment(uriData, bundleData);
    }

    @Override
    protected void setViewListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.filterAddress(getBaseContext());
            }
        });
    }

    @Override
    protected void initVar() {
        mReceiver = new ManagePeopleAddressReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void inflateFragment(Fragment fragment, String tag) {
        if (getFragmentManager().findFragmentByTag(tag) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, tag)
                    .commit();
        }
    }

    @Override
    public Fragment getInflatedFragment(String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            return getFragmentManager().findFragmentByTag(tag);
        } else {
            throw new RuntimeException("fragment not inflated yet !!!");
        }
    }

    @Override
    public void startServiceSetDefaultAddress(String addressId) {
        ManagePeopleAddressService.startActionSetDefaultAddress(this, addressId, mReceiver);
    }

    @Override
    public void startServiceDeleteAddress(String addressId) {
        ManagePeopleAddressService.startActionDeleteAddress(this, addressId, mReceiver);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        presenter.setOnReceiveResult(resultCode, resultData);
    }

    @Override
    public void setFilterViewVisibility(boolean isAble) {
        if (isAble) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
