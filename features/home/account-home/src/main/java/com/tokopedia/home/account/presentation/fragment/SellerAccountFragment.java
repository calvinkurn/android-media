package com.tokopedia.home.account.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.adapter.seller.SellerAccountAdapter;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/16/18.
 */
public class SellerAccountFragment extends BaseAccountFragment implements AccountItemListener,
        SellerAccount.View {

    public static final String TAG = SellerAccountFragment.class.getSimpleName();
    public static final String SELLER_DATA = "seller_data";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SellerAccountAdapter adapter;

    private boolean isLoaded = false;

    public static Fragment newInstance(SellerViewModel sellerViewModel) {
        Fragment fragment = new SellerAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SELLER_DATA, sellerViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_account, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_seller);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SellerAccountAdapter(new AccountTypeFactory(this), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::getData);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLoaded) {
            getData();
            isLoaded = !isLoaded;
        }
    }

    private void getData() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof AccountHomeFragment) {
            ((AccountHomeFragment)parentFragment).loadData();
        }
    }

    @Override
    public void loadData(List<? extends Visitable> visitables) {
        if(visitables != null) {
            adapter.clearAllElements();
            adapter.setElement(visitables);
        }
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }


    @Override
    public void showLoading() {
        if (adapter != null)
            adapter.showLoading();

        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (adapter != null)
            adapter.hideLoading();

        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        if (getView() != null) {
            ToasterError.make(getView(), message, ToasterError.LENGTH_SHORT).show();
        }
    }
}
