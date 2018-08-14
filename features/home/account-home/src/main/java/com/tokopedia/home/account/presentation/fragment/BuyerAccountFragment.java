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

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.di.component.BuyerAccountComponent;
import com.tokopedia.home.account.di.component.DaggerBuyerAccountComponent;
import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.adapter.buyer.BuyerAccountAdapter;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author okasurya on 7/16/18.
 */
public class BuyerAccountFragment extends BaseAccountFragment implements BuyerAccount.View {

    public static final String TAG = BuyerAccountFragment.class.getSimpleName();
    private static final String BUYER_DATA = "buyer_data";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BuyerAccountAdapter adapter;

    @Inject
    BuyerAccount.Presenter presenter;

    public static Fragment newInstance() {
        Fragment fragment = new BuyerAccountFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_account, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_buyer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                .VERTICAL, false));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BuyerAccountAdapter(new AccountTypeFactory(this), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        if (getContext() != null) {
            GraphqlClient.init(getContext());
            getData();
            swipeRefreshLayout.setOnRefreshListener(this::getData);
        }
    }

    private void getData() {
        presenter.getBuyerData(GraphqlHelper.loadRawString(getContext().getResources(), R.raw
                .query_buyer_account_home));
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    @Override
    public void loadBuyerData(BuyerViewModel model) {
        if (model.getItems() != null) {
            adapter.clearAllElements();
            adapter.setElement(model.getItems());
        }
    }

    private void initInjector() {
        BuyerAccountComponent component = DaggerBuyerAccountComponent.builder()
                .baseAppComponent(
                        ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()
                ).build();

        component.inject(this);
        presenter.attachView(this);
    }

    @Override
    public void showLoading() {
        if (adapter != null)
            adapter.showLoading();
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
