package com.tokopedia.pms.payment.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.design.component.Menus;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.pms.R;
import com.tokopedia.pms.bankaccount.view.ChangeBankAccountActivity;
import com.tokopedia.pms.clickbca.view.ChangeClickBcaActivity;
import com.tokopedia.pms.payment.di.PaymentListModule;
import com.tokopedia.pms.payment.view.adapter.PaymentListAdapterTypeFactory;
import com.tokopedia.pms.payment.view.adapter.PaymentListViewHolder;
import com.tokopedia.pms.payment.view.model.PaymentListModel;
import com.tokopedia.pms.payment.view.presenter.PaymentListContract;
import com.tokopedia.pms.payment.view.presenter.PaymentListPresenter;
import com.tokopedia.pms.payment.di.DaggerPaymentListComponent;
import com.tokopedia.pms.proof.view.UploadProofPaymentActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListFragment extends BaseListFragment<PaymentListModel, PaymentListAdapterTypeFactory> implements PaymentListContract.View, PaymentListViewHolder.ListenerPaymentList {

    public static final int REQUEST_CODE_CHANGE_BANK_ACCOUNT = 1;
    public static final int REQUEST_CODE_UPLOAD_PROOF = 2;
    public static final int REQUEST_CODE_CHANGE_BCA_ID = 3;
    @Inject
    PaymentListPresenter paymentListPresenter;
    private ProgressDialog progressDialog;

    String cursor = "";

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
    public void onDestroy() {
        paymentListPresenter.detachView();
        super.onDestroy();
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipe_refresh_layout);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_list, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading));
        return view;
    }

    @Override
    public int getRecyclerViewResourceId() {
        return R.id.recycler_view;
    }

    @Override
    public void showCancelationMessage(String refundMessage, String transactionID, String merchantCode) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(getString(R.string.payment_label_cancel_transaction));
        alertDialog.setMessage(refundMessage);
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                paymentListPresenter.cancelPayment(getResources(), transactionID, merchantCode);
            }
        });
        //alertDialog.setNegativeButton(getString(R.string.payment_label_no), null);
        alertDialog.show();
    }

    @Override
    public void onResultCancelPayment(boolean isSuccess, String message) {
        if(isSuccess){
            NetworkErrorHelper.showGreenCloseSnackbar(getActivity(), message);
            onSwipeRefresh();
        }else{
            NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
        }
    }

    @Override
    public void onErrorGetCancelDetail(Throwable e) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void onErrorCancelPayment(Throwable e) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void showDialogLoading() {
        progressDialog.show();
    }

    @Override
    public void hideDialogLoading() {
        progressDialog.hide();
    }

    @Override
    public void onItemClicked(PaymentListModel paymentListModel) {

    }

    @Override
    public void renderList(List<PaymentListModel> map, boolean hasNextPage, String lastCursor) {
        renderList(map, hasNextPage);
        this.cursor = lastCursor;
    }

    @Override
    public void loadData(int page) {
        paymentListPresenter.getPaymentList(getResources(), getContext(), cursor);
    }

    @Override
    protected void loadInitialData() {
        cursor = "";
        super.loadInitialData();
    }

    @Override
    protected PaymentListAdapterTypeFactory getAdapterTypeFactory() {
        return new PaymentListAdapterTypeFactory(this);
    }

    public static Fragment createInstance() {
        return new PaymentListFragment();
    }

    @Override
    public void onClickOverFlow(PaymentListModel paymentListModel) {
        Menus menus = new Menus(getContext());
        menus.setItemMenuList(paymentListModel.getListOfAction().toArray(new String[paymentListModel.getListOfAction().size()]));
        menus.setActionText(getString(R.string.payment_label_close));
        menus.setOnActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menus.dismiss();
            }
        });
        menus.setOnItemMenuClickListener(new Menus.OnItemMenuClickListener() {
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
                menus.dismiss();
            }
        });
        menus.show();
    }

    private void changeAccountDetail(PaymentListModel paymentListModel) {
        //Intent intent = ChangeBankAccountActivity.createIntent(getActivity(), paymentListModel);
        //startActivityForResult(intent, REQUEST_CODE_CHANGE_BANK_ACCOUNT);
    }

    private void uploadProofPayment(PaymentListModel paymentListModel) {
        //Intent intent = UploadProofPaymentActivity.createIntent(getActivity(), paymentListModel);
        //startActivityForResult(intent, REQUEST_CODE_UPLOAD_PROOF);
    }

    private void changeBcaUserId(PaymentListModel paymentListModel) {
        //Intent intent = ChangeClickBcaActivity.createIntent(getActivity(), paymentListModel.getTransactionId(), paymentListModel.getMerchantCode(), paymentListModel.getValueDynamicViewDetailPayment());
        //startActivityForResult(intent, REQUEST_CODE_CHANGE_BCA_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_CHANGE_BANK_ACCOUNT:
                case REQUEST_CODE_CHANGE_BCA_ID:
                case REQUEST_CODE_UPLOAD_PROOF:
                    onSwipeRefresh();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClickHowToPay(PaymentListModel element) {
        RouteManager.route(getContext(), element.getAppLink());
    }

    @Override
    public void onClickCancelTransaction(PaymentListModel element) {
        paymentListPresenter.getCancelDetail(getResources(), element.getTransactionId(), element.getMerchantCode());
    }

    @Override
    public void onClickSeeDetail(String invoiceUrl) {
        RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, invoiceUrl);
    }
}
