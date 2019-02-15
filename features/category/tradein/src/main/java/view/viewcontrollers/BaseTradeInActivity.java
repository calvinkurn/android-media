package view.viewcontrollers;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;

import com.example.tradein.R;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterNormal;

public abstract class BaseTradeInActivity extends BaseSimpleActivity implements TradeInBaseView {

    abstract void initView();

    abstract int getMenuRes();

    abstract int getBottomSheetLayoutRes();

    abstract boolean doNeedReattach();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //executeInjector();
        NetworkClient.init(this);
        //mPresenter = getPresenter(); //Todo:add code for getting default viewmodel
        initView();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon_back_black);
        //mPresenter.attachView(this);
    }

    @Override
    public void showMessage(String message) {
        Snackbar snackbar = ToasterNormal.make(getRootView(), message, BaseToaster.LENGTH_SHORT);
        snackbar.show();
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
    protected void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.abstraction.R.color.white));
        }
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
    public void showBottomFragment() {

    }

    @Override
    public void hideBottomFragment() {

    }

    @Override
    public void updateDataSet() {

    }

    @Override
    public void setSnackBarErrorMessage(String message, boolean clickable) {

    }

    @Override
    public void clearSearch() {

    }

    @Override
    public boolean isSearchMode() {
        return false;
    }

    @Override
    public void toggleSearch(int visibility) {

    }
}
