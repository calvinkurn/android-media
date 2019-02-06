package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.events.R;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.contractor.EventBaseContract;

import butterknife.ButterKnife;

public abstract class EventBaseActivity extends BaseSimpleActivity implements EventBaseContract.EventBaseView {

    abstract void initPresenter();

    abstract View getProgressBar();

    EventBaseContract.EventBasePresenter mPresenter;
    EventComponent eventComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        bindViews();
        mPresenter.attachView(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mPresenter.onClickOptionMenu(item.getItemId());
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void showProgressBar() {
        View progressBar = getProgressBar();
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        View progressBar = getProgressBar();
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSnackBar(String message, boolean clickable) {
        Snackbar snackbar = ToasterError.make(getRootView(), message, BaseToaster.LENGTH_SHORT);
        if (clickable) {
            snackbar.setDuration(BaseToaster.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.title_ok, v -> snackbar.dismiss());
        }
        snackbar.show();
    }

    @Override
    public void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    @Override
    public View getRootView() {
        return findViewById(R.id.main_content);
    }

    @Override
    public Resources getViewResources() {
        return super.getResources();
    }

    protected void initInjector() {
        eventComponent = DaggerEventComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .eventModule(new EventModule(this))
                .build();
    }

    private BaseAppComponent getBaseAppComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }

    private void bindViews() {
        ButterKnife.bind(this);
        bindCustomViews();
    }

    protected void bindCustomViews() {

    }
}
