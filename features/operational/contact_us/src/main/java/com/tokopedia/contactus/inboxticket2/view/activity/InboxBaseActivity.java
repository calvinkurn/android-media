package com.tokopedia.contactus.inboxticket2.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.di.DaggerInboxComponent;
import com.tokopedia.contactus.inboxticket2.di.InboxComponent;
import com.tokopedia.contactus.inboxticket2.di.InboxModule;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;

import javax.inject.Inject;

import butterknife.ButterKnife;


public abstract class InboxBaseActivity extends BaseSimpleActivity implements InboxBaseContract.InboxBaseView {


    abstract InboxBaseContract.InboxBasePresenter getPresenter();

    abstract void initView();

    abstract int getMenuRes();

    abstract boolean doNeedReattach();

    @Inject
    InboxBaseContract.InboxBasePresenter mPresenter;

    BottomSheetDialogFragment bottomFragment;

    Menu mMenu;

    protected InboxComponent component;

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        getRootView().findViewById(R.id.progress_bar_layout).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        getRootView().findViewById(R.id.progress_bar_layout).setVisibility(View.GONE);
    }

    @Override
    public View getRootView() {
        return findViewById(R.id.root_view);
    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        executeInjector();
        NetworkClient.init(this);
        mPresenter = getPresenter();
        initView();
        mPresenter.attachView(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (doNeedReattach()) {
            mPresenter.reAttachView();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detachView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    private void executeInjector() {
        component = DaggerInboxComponent.builder()
                .inboxModule(new InboxModule(this))
                .build();
    }

    @Override
    public void showBottomFragment() {
        String BOTTOM_FRAGMENT = "Bottom_Sheet_Fragment";
        bottomFragment = (BottomSheetDialogFragment) getSupportFragmentManager().findFragmentByTag(BOTTOM_FRAGMENT);
        if (bottomFragment == null)
            bottomFragment = mPresenter.getBottomFragment();
        bottomFragment.show(getSupportFragmentManager(), BOTTOM_FRAGMENT);
    }

    @Override
    public void hideBottomFragment() {
        if (bottomFragment != null)
            bottomFragment.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuRes() != -1) {
            getMenuInflater().inflate(getMenuRes(), menu);
            mMenu = menu;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mPresenter.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void setSnackBarErrorMessage(String message, boolean clickable) {
        Snackbar snackbar = Snackbar.make(getRootView(), message, Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = layout.findViewById(android.support.design.R.id.snackbar_text);
        LayoutInflater inflater = LayoutInflater.from(this);
        View snackView = inflater.inflate(R.layout.snackbar_error_layout, null);
        TextView tv = snackView.findViewById(R.id.tv_msg);
        tv.setText(message);

        if (clickable) {
            TextView okBtn = snackView.findViewById(R.id.snack_ok);
            okBtn.setVisibility(View.VISIBLE);
            okBtn.setOnClickListener(view -> snackbar.dismiss());
        } else {
            snackbar.setDuration(Snackbar.LENGTH_SHORT);
        }

        layout.addView(snackView, 0);
        layout.setPadding(0, 0, 0, 0);
        textView.setVisibility(View.INVISIBLE);
        snackbar.show();
    }
}
