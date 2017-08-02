package com.tokopedia.core.manage.people.bank.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.R;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.manage.people.bank.adapter.BcaOneClickRecyclerAdapter;
import com.tokopedia.core.manage.people.bank.dialog.BcaOneClickDeleteDialog;
import com.tokopedia.core.manage.people.bank.listener.BcaOneClickDeleteListener;
import com.tokopedia.core.manage.people.bank.listener.ListBankTypeActivityView;
import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;
import com.tokopedia.core.manage.people.bank.model.PaymentListModel;
import com.tokopedia.core.manage.people.bank.presenter.ListBankTypePresenter;
import com.tokopedia.core.manage.people.bank.presenter.ListBankTypePresenterImpl;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;

import rx.Subscriber;

/**
 * Created by kris on 7/20/17. Tokopedia
 */

public class ListBankTypeActivity extends BasePresenterActivity<ListBankTypePresenter>
        implements ListBankTypeActivityView, BcaOneClickDeleteListener {

    private static final int REGISTER_BCA_ONE_CLICK_REQUEST_CODE = 1;
    public static final int EDIT_BCA_ONE_CLICK_REQUEST_CODE = 2;

    private RecyclerView bcaOneClickRecyclerView;

    private BcaOneClickRecyclerAdapter bcaOneClickRecyclerAdapter;

    private LinearLayout bcaOneClickRegisterLayout;

    private TextView bcaOneClickRegistrationButton;

    private TkpdProgressDialog progressDialog;

    private PaymentListModel paymentModels;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new ListBankTypePresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.payment_list_layout;
    }

    @Override
    protected void initView() {
        bcaOneClickRecyclerView = (RecyclerView) findViewById(R.id.bca_one_click_recycler_view);
        bcaOneClickRegisterLayout = (LinearLayout) findViewById(R.id.bca_one_click_register_layout);
        bcaOneClickRegistrationButton = (TextView) findViewById(R.id.bca_one_click_register_button);
        bcaOneClickRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bcaOneClickRecyclerView.setAdapter(bcaOneClickRecyclerAdapter);
        progressDialog = new TkpdProgressDialog(ListBankTypeActivity.this,
                TkpdProgressDialog.NORMAL_PROGRESS);
        presenter.onGetPaymentList(paymentListModelSubscriber());
        bcaOneClickRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.showDialog();
                presenter.onOneClickBcaChosen(new Subscriber<BcaOneClickData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(BcaOneClickData bcaOneClickData) {
                        Bundle bundle = new Bundle();
                        bundle.putString("access_token",
                                bcaOneClickData.getToken().getAccessToken());
                        if ((getApplication() instanceof TransactionRouter)) {
                            ((TransactionRouter) getApplication())
                                    .goToBcaOneClick(ListBankTypeActivity.this, bundle,
                                            REGISTER_BCA_ONE_CLICK_REQUEST_CODE);
                            finish();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });
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
    public void onGetPaymentListData(PaymentListModel paymentListModel) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onDestroyed();
    }

    private Subscriber<PaymentListModel> paymentListModelSubscriber() {
        return new Subscriber<PaymentListModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(PaymentListModel paymentListModel) {
                paymentModels = paymentListModel;
                bcaOneClickRecyclerAdapter = new BcaOneClickRecyclerAdapter(
                        paymentListModel.getBcaOneClickUserModels(),
                        actionListener()
                );
                bcaOneClickRecyclerView.setAdapter(bcaOneClickRecyclerAdapter);
                bcaOneClickRecyclerAdapter.notifyDataSetChanged();
                if(paymentListModel.getBcaOneClickUserModels().size() < 3) {
                    bcaOneClickRegisterLayout.setVisibility(View.VISIBLE);
                } else bcaOneClickRegisterLayout.setVisibility(View.GONE);
            }
        };
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REGISTER_BCA_ONE_CLICK_REQUEST_CODE:
                presenter.onGetPaymentList(paymentListModelSubscriber());
                break;
            case EDIT_BCA_ONE_CLICK_REQUEST_CODE:
                presenter.onGetPaymentList(paymentListModelSubscriber());
                break;
        }
    }

    private BcaOneClickRecyclerAdapter.ActionListener actionListener() {
        return new BcaOneClickRecyclerAdapter.ActionListener() {
            @Override
            public void onDelete(String name, String credentialNumber) {
                BcaOneClickDeleteDialog bcaOneClickDeleteDialog =
                        BcaOneClickDeleteDialog.createDialog(name, credentialNumber);
                bcaOneClickDeleteDialog.show(getFragmentManager(), "delete_dialog");
            }

            @Override
            public void onEdit() {

            }
        };
    }

    @Override
    public void onDelete(String tokenId) {
        presenter.onDeletePaymentList(paymentListModelSubscriber(), tokenId);
    }
}
