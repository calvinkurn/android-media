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
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.di.component.DaggerSellerAccountComponent;
import com.tokopedia.home.account.di.component.SellerAccountComponent;
import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.adapter.seller.SellerAccountAdapter;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewmodel.TickerViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;
import com.tokopedia.navigation_common.listener.FragmentListener;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author okasurya on 7/16/18.
 */
public class SellerAccountFragment extends BaseAccountFragment implements AccountItemListener,
        SellerAccount.View, FragmentListener {

    public static final String TAG = SellerAccountFragment.class.getSimpleName();
    public static final String SELLER_DATA = "seller_data";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SellerAccountAdapter adapter;

    @Inject
    SellerAccount.Presenter presenter;
    private boolean isLoaded = false;

    public static Fragment newInstance() {
        Fragment fragment = new SellerAccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_seller_account, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_seller);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                .VERTICAL, false));
        swipeRefreshLayout.setColorSchemeResources(R.color.tkpd_main_green);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SellerAccountAdapter(new AccountTypeFactory(this), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        if (getContext() != null) {
            GraphqlClient.init(getContext());
            getData();
            swipeRefreshLayout.setOnRefreshListener(this::getData);
        }
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
        presenter.getSellerData(GraphqlHelper.loadRawString(getContext().getResources(), R.raw.query_seller_account_home),
                GraphqlHelper.loadRawString(getContext().getResources(), R.raw.gql_get_deposit));
    }

    @Override
    public void loadSellerData(SellerViewModel model) {
        if (model.getItems() != null) {
            adapter.clearAllElements();
            adapter.setElement(model.getItems());
        }

        // TODO: 23/1/19 show showcase dialog for saldo toko

    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    private void initInjector() {
        SellerAccountComponent component = DaggerSellerAccountComponent.builder()
                .baseAppComponent(
                        ((BaseMainApplication) getActivity().getApplication())
                                .getBaseAppComponent()
                ).build();

        component.inject(this);
        presenter.attachView(this);
    }

    @Override
    public void showLoading() {
        if (adapter != null) adapter.showLoading();
    }

    @Override
    public void hideLoading() {
        if (adapter != null) adapter.hideLoading();

        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        if (getView() != null) {
            ToasterError.make(getView(), message)
                    .setAction(getString(R.string.title_try_again), view -> getData())
                    .show();
        }
    }

    @Override
    public void showError(Throwable e) {
        if (getView() != null && getContext() != null) {
            ToasterError.make(getView(), ErrorHandler.getErrorMessage(getContext(), e))
                    .setAction(getString(R.string.title_try_again), view -> getData())
                    .show();
        }
    }

    @Override
    public void showErroNoConnection() {
        showError(getString(R.string.error_no_internet_connection));
    }

    @Override
    public void onScrollToTop() {
        if (recyclerView != null)
            recyclerView.scrollToPosition(0);
    }

    @Override
    public void onTickerClosed() {
        if (adapter.getItemCount() > 0 &&
                adapter.getItemAt(0) instanceof TickerViewModel) {
            adapter.removeElementAt(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }
}
