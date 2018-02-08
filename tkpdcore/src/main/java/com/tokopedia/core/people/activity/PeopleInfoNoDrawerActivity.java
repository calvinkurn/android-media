package com.tokopedia.core.people.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.people.listener.PeopleInfoView;
import com.tokopedia.core.people.presenter.PeopleInfoImpl;
import com.tokopedia.core.people.presenter.PeopleInfoPresenter;

/**
 * Created on 6/7/16.
 */
public class PeopleInfoNoDrawerActivity extends BasePresenterActivity<PeopleInfoPresenter>
        implements PeopleInfoView {

    private static final String EXTRA_PARAM_USER_ID = "user_id";

    private Uri uriData;
    private Bundle bundleData;

    public static Intent createInstance(@NonNull Context context, @NonNull String userID) {
        Intent intent = new Intent(context, PeopleInfoNoDrawerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_USER_ID, userID);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PEOPLE;
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
        presenter = new PeopleInfoImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_people_info;
    }

    @Override
    protected void initView() {
        presenter.initFragment(uriData, bundleData);
    }

    @Override
    public void inflateFragment(Fragment fragment, String tag) {
        if (getFragmentManager().findFragmentByTag(tag) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, tag)
                    .commit();
        } else {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
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

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
