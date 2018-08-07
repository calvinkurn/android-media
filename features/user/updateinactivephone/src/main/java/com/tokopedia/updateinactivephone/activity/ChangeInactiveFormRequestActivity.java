package com.tokopedia.updateinactivephone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.fragment.SelectImageNewPhoneFragment;
import com.tokopedia.updateinactivephone.presenter.ChangeInactiveFormRequestPresenter;
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest;

import javax.inject.Inject;

public class ChangeInactiveFormRequestActivity extends BaseSimpleActivity implements
        HasComponent<AppComponent>, ChangeInactiveFormRequest.View, SelectImageNewPhoneFragment.SelectImageInterface {

    @Inject
    ChangeInactiveFormRequestPresenter presenter;

    private String userId;
    private String oldPhoneNumber;


    public static Intent getChangeInactivePhoneIntent(Context context) {
        return new Intent(context, ChangeInactiveFormRequestActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return SelectImageNewPhoneFragment.getInstance();
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        setupToolbar();
        initView();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.change_inactive_form_layout;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        presenter.attachView(this);
        return super.onCreateView(name, context, attrs);
    }

    private void initView() {

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (SelectImageNewPhoneFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = SelectImageNewPhoneFragment.getInstance();
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

        ImageView infoIcon = toolbar.findViewById(R.id.info_icon);
        infoIcon.setOnClickListener(view -> Toast.makeText(view.getContext(), "icon clicked", Toast.LENGTH_SHORT).show());
    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }

    @Override
    public void onContinueButtonClick() {
        // TODO: 8/7/18 move to input new phone fragment

    }

    @Override
    public boolean isValidPhotoIdPath() {
        return presenter.isValidPhotoIdPath();
    }

    @Override
    public void setAccountPhotoImagePath(String imagePath) {
        presenter.setAccountPhotoImagePath(imagePath);
    }

    @Override
    public void setPhotoIdImagePath(String imagePath) {
        presenter.setPhotoIdImagePath(imagePath);
    }

    @Override
    public void uploadPhotoIdImage() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onForbidden() {

    }
}
