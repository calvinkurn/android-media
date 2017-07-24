package com.tokopedia.core.manage.people.bank.activity;

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

import com.tokopedia.core.R;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.manage.people.bank.listener.ListBankTypeActivityView;
import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;
import com.tokopedia.core.manage.people.bank.presenter.ListBankTypePresenter;
import com.tokopedia.core.manage.people.bank.presenter.ListBankTypePresenterImpl;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;

import rx.Subscriber;

/**
 * Created by kris on 7/20/17. Tokopedia
 */

public class ListBankTypeActivity extends BasePresenterActivity<ListBankTypePresenter>
        implements ListBankTypeActivityView {

    private RecyclerView mainRecyclerView;

    private BankListRecyclerAdapter adapter;

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
        return R.layout.recycler_view_layout;
    }

    @Override
    protected void initView() {
        adapter = new BankListRecyclerAdapter();
        mainRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
    public void onClickBcaGetAccessToken(String accessToken) {

    }

    private class BankListRecyclerAdapter extends RecyclerView.Adapter<BankListViewHolder> {

        BankListRecyclerAdapter() {

        }

        @Override
        public BankListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.plain_adapter_layout, parent, false);
            return new BankListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BankListViewHolder holder, int position) {
            switch (position) {
                case 0:
                    holder.titleText.setText("Akun Bank");
                    holder.mainView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ListBankTypeActivity.this,
                                    ManagePeopleBankActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    holder.titleText.setText("BCA One Click");
                    holder.mainView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            presenter.onOneClickBcaChosen(new Subscriber<BcaOneClickData>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(BcaOneClickData bcaOneClickData) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("access_token",
                                            bcaOneClickData.getAccessToken());
                                    if ((getApplication() instanceof TransactionRouter)) {
                                        ((TransactionRouter) getApplication())
                                                .goToBcaOneClick(ListBankTypeActivity.this, bundle);
                                    }
                                }
                            });
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    private class BankListViewHolder extends RecyclerView.ViewHolder {

        private TextView titleText;

        private LinearLayout mainView;

        BankListViewHolder(View itemView) {
            super(itemView);
            mainView = (LinearLayout) itemView.findViewById(R.id.plain_adapter_main_view);
            titleText = (TextView) itemView.findViewById(R.id.plain_adapter_text_view);
        }
    }
}
