package com.tokopedia.paymentmanagementsystem.paymentlist.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.adapter.PaymentListAdapterTypeFactory;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.model.PaymentListModel;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.presenter.PaymentListContract;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.presenter.PaymentListPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListFragment extends BaseListFragment<PaymentListModel, PaymentListAdapterTypeFactory> implements PaymentListContract.View {

    @Inject
    PaymentListPresenter paymentListPresenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        GraphqlClient.init(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void showCancelationMessage(String refundMessage) {

    }

    @Override
    public void onResultCancelPayment(boolean success) {

    }

    @Override
    public void onErrorGetCancelDetail(Throwable e) {

    }

    @Override
    public void onErrorCancelPayment(Throwable e) {

    }

    @Override
    public void onItemClicked(PaymentListModel paymentListModel) {

    }

    @Override
    public void loadData(int page) {
        paymentListPresenter.getPaymentList(getResources(), getContext());
    }

    @Override
    protected PaymentListAdapterTypeFactory getAdapterTypeFactory() {
        return new PaymentListAdapterTypeFactory();
    }

    public static Fragment createInstance() {
        return new PaymentListFragment();
    }

    @Override
    protected boolean isLoadMoreEnabledByDefault() {
        return false;
    }
}
