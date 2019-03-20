package com.tokopedia.saldodetails.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter;
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory;
import com.tokopedia.saldodetails.adapter.listener.DataEndLessScrollListener;
import com.tokopedia.saldodetails.presenter.SaldoHistoryPresenter;
import com.tokopedia.saldodetails.response.model.DepositHistoryList;

import static com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.FOR_ALL;
import static com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.FOR_BUYER;
import static com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.FOR_SELLER;
import static com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.TRANSACTION_TYPE;

public class SaldoHistoryListFragment extends BaseListFragment<DepositHistoryList,
        SaldoDetailTransactionFactory> {

    private static final int SELLER_SALDO = 1;
    private static final int BUYER_SALDO = 0;
    private static final int ALL_SALDO = 2;
    private RecyclerView recyclerView;
    private SaldoDepositAdapter adapter;
    private SaldoHistoryPresenter presenter;
    private String transactionType;

    public static SaldoHistoryListFragment createInstance(String type, SaldoHistoryPresenter saldoHistoryPresenter) {
        SaldoHistoryListFragment saldoHistoryListFragment = new SaldoHistoryListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TRANSACTION_TYPE, type);
        saldoHistoryListFragment.setArguments(bundle);
        saldoHistoryListFragment.setPresenter(saldoHistoryPresenter);
        return saldoHistoryListFragment;
    }

    private void setPresenter(SaldoHistoryPresenter saldoHistoryPresenter) {
        this.presenter = saldoHistoryPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saldo_history_list, container, false);
        initViews(view);
        initialVar();
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public SaldoDepositAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    protected BaseListAdapter<DepositHistoryList, SaldoDetailTransactionFactory> createAdapterInstance() {
        adapter = new SaldoDepositAdapter(getAdapterTypeFactory());
        return adapter;
    }

    private void initialVar() {
        transactionType = getArguments().getString(TRANSACTION_TYPE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
    }

    @Override
    protected EndlessRecyclerViewScrollListener createEndlessRecyclerViewListener() {

        return new DataEndLessScrollListener(recyclerView.getLayoutManager(), adapter) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                showLoading();
                switch (transactionType) {
                    case FOR_ALL:
                        presenter.loadMoreAllTransaction(page, ALL_SALDO);
                        break;
                    case FOR_BUYER:
                        presenter.loadMoreBuyerTransaction(page, BUYER_SALDO);
                        break;
                    case FOR_SELLER:
                        presenter.loadMoreSellerTransaction(page, SELLER_SALDO);
                        break;
                }
            }
        };
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected SaldoDetailTransactionFactory getAdapterTypeFactory() {
        return new SaldoDetailTransactionFactory();
    }

    @Override
    public void onItemClicked(DepositHistoryList depositHistoryList) {

    }

    @Override
    protected void initInjector() {

    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        this.recyclerView = super.getRecyclerView(view);
        return super.getRecyclerView(view);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

}
