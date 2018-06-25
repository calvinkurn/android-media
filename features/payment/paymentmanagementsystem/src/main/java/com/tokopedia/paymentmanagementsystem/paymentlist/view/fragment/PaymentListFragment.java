package com.tokopedia.paymentmanagementsystem.paymentlist.view.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.adapter.PaymentListAdapterTypeFactory;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.model.PaymentListModel;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.presenter.PaymentListPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListFragment extends BaseListFragment<PaymentListModel, PaymentListAdapterTypeFactory> {

    @Inject
    PaymentListPresenter paymentListPresenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onItemClicked(PaymentListModel paymentListModel) {

    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected PaymentListAdapterTypeFactory getAdapterTypeFactory() {
        return new PaymentListAdapterTypeFactory();
    }

    public static Fragment createInstance() {
        return new PaymentListFragment();
    }
}
