package com.tokopedia.pms.bankdestination.view.fragment;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.pms.bankdestination.di.BankDestinationModule;
import com.tokopedia.pms.bankdestination.view.presenter.BankDestinationContract;
import com.tokopedia.pms.bankdestination.view.presenter.BankDestinationPresenter;
import com.tokopedia.pms.bankdestination.view.adapter.BankListAdapterTypeFactory;
import com.tokopedia.pms.bankdestination.view.model.BankListModel;
import com.tokopedia.pms.bankdestination.di.DaggerBankDestinationComponent;
import com.tokopedia.pms.common.Constant;

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
                .bankDestinationModule(new BankDestinationModule(getContext()))
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
