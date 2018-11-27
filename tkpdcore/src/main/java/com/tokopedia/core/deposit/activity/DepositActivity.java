package com.tokopedia.core.deposit.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.deposit.fragment.DepositFragment;
import com.tokopedia.core.deposit.presenter.DepositFragmentPresenterImpl;
import com.tokopedia.core.deposit.presenter.DepositPresenter;
import com.tokopedia.core.deposit.presenter.DepositPresenterImpl;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;
import com.tokopedia.saldodetails.view.fragment.MerchantSaldoPriorityFragment;

/**
 * Created by Nisie on 3/30/16.
 */
@DeepLink(Constants.Applinks.DEPOSIT)
public class DepositActivity extends BasePresenterActivity<DepositPresenter> implements
        DepositFragment.DepositScreenListener, MerchantSaldoPriorityFragment.InteractionListener {

    private static final String TAG = "DEPOSIT_FRAGMENT";

    private TkpdProgressDialog tkpdProgressDialog;

    public static Intent createInstance(Context context) {
        return new Intent(context, DepositActivity.class);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_DEPOSIT;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new DepositPresenterImpl();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit;
    }

    @Override
    protected void initView() {
        DepositFragment fragment = DepositFragment.createInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, TAG);
        fragmentTransaction.commit();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DepositFragmentPresenterImpl.REQUEST_WITHDRAW_CODE && resultCode == Activity.RESULT_OK) {
            ((DepositFragment) getFragmentManager().findFragmentByTag(TAG)).refresh();
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void showSaldoFragment(int resId, GqlDetailsResponse sellerDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("seller_details", sellerDetails);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(resId, MerchantSaldoPriorityFragment.newInstance(bundle))
                .commit();
    }

    @Override
    public void showLoading() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        }
        tkpdProgressDialog.setCancelable(false);
        tkpdProgressDialog.showDialog("", "Updating Status...");
        tkpdProgressDialog.showDialog();
    }

    @Override
    public void dismissLoading() {
        if (tkpdProgressDialog == null) {
            return;
        }
        tkpdProgressDialog.dismiss();
    }
}
