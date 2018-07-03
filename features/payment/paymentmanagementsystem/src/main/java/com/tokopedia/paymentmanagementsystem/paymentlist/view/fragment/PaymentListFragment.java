package com.tokopedia.paymentmanagementsystem.paymentlist.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.design.component.Menus;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.paymentmanagementsystem.R;
import com.tokopedia.paymentmanagementsystem.changeclickbca.view.ChangeClickBcaActivity;
import com.tokopedia.paymentmanagementsystem.paymentlist.di.PaymentListModule;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.adapter.PaymentListAdapterTypeFactory;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.adapter.PaymentListViewHolder;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.model.PaymentListModel;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.presenter.PaymentListContract;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.presenter.PaymentListPresenter;
import com.tokopedia.paymentmanagementsystem.paymentlist.di.DaggerPaymentListComponent;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListFragment extends BaseListFragment<PaymentListModel, PaymentListAdapterTypeFactory> implements PaymentListContract.View, PaymentListViewHolder.ListenerPaymentList {

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
        DaggerPaymentListComponent.builder()
                .paymentListModule(new PaymentListModule())
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        paymentListPresenter.attachView(this);
    }

    @Override
    public void showCancelationMessage(String refundMessage, String transactionID, String merchantCode) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(getString(R.string.payment_label_cancel_transaction));
        alertDialog.setMessage(refundMessage);
        alertDialog.setPositiveButton(getString(R.string.payment_title_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                paymentListPresenter.cancelPayment(getResources(), transactionID, merchantCode);
            }
        });
        alertDialog.setNegativeButton(getString(R.string.payment_label_no), null);
    }

    @Override
    public void onResultCancelPayment(boolean isSuccess) {

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
        return new PaymentListAdapterTypeFactory(this);
    }

    public static Fragment createInstance() {
        return new PaymentListFragment();
    }

    @Override
    protected boolean isLoadMoreEnabledByDefault() {
        return false;
    }

    @Override
    public void onClickOverFlow(PaymentListModel paymentListModel) {
        Menus menus = new Menus(getContext());
        menus.setItemMenuList(paymentListModel.getListOfAction().toArray(new String[paymentListModel.getListOfAction().size()]));
        menus.setActionText(getString(R.string.payment_label_back));
        menus.setOnActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menus.dismiss();
            }
        });
        menus.setOnItemMenuClickListener(getListenerOnMenuClick(paymentListModel));
        menus.show();
    }

    private Menus.OnItemMenuClickListener getListenerOnMenuClick(PaymentListModel paymentListModel) {
        return new Menus.OnItemMenuClickListener() {
            @Override
            public void onClick(Menus.ItemMenus itemMenus, int pos) {
                if(itemMenus.title.equals(getString(R.string.payment_label_cancel_transaction))){
                    onClickCancelTransaction(paymentListModel);
                }else if(itemMenus.title.equals(getString(R.string.payment_label_change_bca_user_id))){
                    changeBcaUserId(paymentListModel);
                } else if(itemMenus.title.equals(getString(R.string.payment_label_upload_proof))){
                    uploadProofPayment(paymentListModel);
                } else if(itemMenus.title.equals(getString(R.string.payment_label_change_account_detail))){
                    changeAccountDetail(paymentListModel);
                }
            }
        };
    }

    private void changeAccountDetail(PaymentListModel paymentListModel) {

    }

    private void uploadProofPayment(PaymentListModel paymentListModel) {

    }

    private void changeBcaUserId(PaymentListModel paymentListModel) {
        Intent intent = ChangeClickBcaActivity.createIntent(getActivity(), paymentListModel.getTransactionId(), paymentListModel.getMerchantCode());
        startActivity(intent);
    }

    @Override
    public void onClickHowToPay(PaymentListModel element) {
        paymentListPresenter.getHowToPay();
    }

    @Override
    public void onClickCancelTransaction(PaymentListModel element) {
        paymentListPresenter.getCancelDetail(getResources(), element.getTransactionId(), element.getMerchantCode());
    }

    @Override
    public void onClickSeeDetail(String invoiceUrl) {

    }
}
