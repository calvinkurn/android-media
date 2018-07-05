package com.tokopedia.paymentmanagementsystem.bankdestinationlist.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.di.BankDestinationModule;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.view.presenter.BankDestinationContract;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.view.presenter.BankDestinationPresenter;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.view.adapter.BankListAdapterTypeFactory;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.view.model.BankListModel;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.di.DaggerBankDestinationComponent;
import com.tokopedia.paymentmanagementsystem.common.Constant;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class BankDestinationFragment extends BaseListFragment<BankListModel,BankListAdapterTypeFactory> implements BankDestinationContract.View {

    @Inject
    BankDestinationPresenter bankDestinationPresenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerBankDestinationComponent.builder()
                .bankDestinationModule(new BankDestinationModule())
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        bankDestinationPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        bankDestinationPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onItemClicked(BankListModel bankListModel) {
        Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_BANK_LIST_MODEL, bankListModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void loadData(int page) {
        bankDestinationPresenter.getListBank();
    }

    @Override
    protected BankListAdapterTypeFactory getAdapterTypeFactory() {
        return new BankListAdapterTypeFactory();
    }

    public static Fragment createInstance() {
        BankDestinationFragment bankDestinationFragment = new BankDestinationFragment();
        return bankDestinationFragment;
    }
}
