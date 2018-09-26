package com.tokopedia.saldodetails.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.di.SaldoDetailsComponent;
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance;
import com.tokopedia.saldodetails.presenter.SaldoDetailsPresenter;
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse;
import com.tokopedia.saldodetails.view.fragment.MerchantSaldoPriorityFragment;
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment;

@DeepLink(ApplinkConst.DEPOSIT)
public class SaldoDepositActivity extends BaseSimpleActivity implements
        HasComponent<SaldoDetailsComponent>, SaldoDepositFragment.DepositScreenListener {

    private static final String TAG = "DEPOSIT_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        SaldoDepositFragment fragment = SaldoDepositFragment.createInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, TAG);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SaldoDetailsPresenter.REQUEST_WITHDRAW_CODE && resultCode == Activity.RESULT_OK) {
//            ((SaldoDepositFragment) getSupportFragmentManager().findFragmentByTag(TAG)).refresh();
        }
    }

    @Override
    public SaldoDetailsComponent getComponent() {
        return SaldoDetailsComponentInstance.getComponent(getApplication());
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_saldo_deposit;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void showSaldoFragment(int resId, GqlMerchantSaldoDetailsResponse.Details sellerDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("seller_details", sellerDetails);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(resId, MerchantSaldoPriorityFragment.newInstance(bundle))
                .commit();
    }

}
