package com.tokopedia.core.manage.people.profile.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.fragment.EmailVerificationDialog;
import com.tokopedia.core.manage.people.profile.fragment.ManagePeopleProfileFragment;
import com.tokopedia.core.manage.people.profile.intentservice.ManagePeopleProfileIntentService;
import com.tokopedia.core.manage.people.profile.intentservice.ManagePeopleProfileResultReceiver;
import com.tokopedia.core.manage.people.profile.listener.ManagePeopleProfileView;
import com.tokopedia.core.manage.people.profile.model.PeopleProfilePass;
import com.tokopedia.core.manage.people.profile.presenter.ManagePeopleProfileImpl;
import com.tokopedia.core.manage.people.profile.presenter.ManagePeopleProfilePresenter;

public class ManagePeopleProfileActivity extends BasePresenterActivity<ManagePeopleProfilePresenter>
        implements ManagePeopleProfileView, EmailVerificationDialog.EmailChangeConfirmation, ManagePeopleProfileResultReceiver.Receiver {

    private Bundle bundleData;
    private Uri uriData;
    private ManagePeopleProfileResultReceiver mReceiver;

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
        presenter = new ManagePeopleProfileImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage_people_profile2;
    }

    @Override
    protected void initView() {
        presenter.initFragment(uriData, bundleData);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONFIG_P_PROFILE;
    }

    @Override
    protected void initVar() {
        mReceiver = new ManagePeopleProfileResultReceiver(new Handler());
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
    public void onEmailChanged() {
        if (getFragmentManager().findFragmentByTag(ManagePeopleProfileFragment.class.getSimpleName()) != null) {
            ManagePeopleProfileFragment fragment = (ManagePeopleProfileFragment) getFragmentManager().findFragmentByTag(ManagePeopleProfileFragment.class.getSimpleName());
            fragment.setOnNotifiedEmailChanged();
        }
    }

    @Override
    public void callServiceToSaveProfile(PeopleProfilePass paramPass) {
        ManagePeopleProfileIntentService.saveProfile(this, paramPass, mReceiver);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (getFragmentManager().findFragmentByTag(ManagePeopleProfileFragment.class.getSimpleName()) != null) {
            ManagePeopleProfileFragment fragment = (ManagePeopleProfileFragment) getFragmentManager().findFragmentByTag(ManagePeopleProfileFragment.class.getSimpleName());
            fragment.setReceiveResult(resultCode, resultData);
        }
    }
}
