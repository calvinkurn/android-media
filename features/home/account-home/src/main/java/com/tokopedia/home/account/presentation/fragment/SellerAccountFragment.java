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
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.AccountHomeRouter;
import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.adapter.seller.SellerAccountAdapter;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.InfoCardView;
import com.tokopedia.home.account.presentation.viewmodel.base.AccountViewModel;
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
    private View containerEmpty;
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
        containerEmpty = view.findViewById(R.id.container_empty);
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
    public void loadData(AccountViewModel accountViewModel) {
        if (accountViewModel != null) {
            if (accountViewModel.isSeller()) {
                if(accountViewModel.getSellerViewModel() != null) {
                    adapter.clearAllElements();
                    adapter.setElement(accountViewModel.getSellerViewModel().getItems());
                }
            } else {
                emptyState();
            }
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

    /**
     * Empty seller
     */
    private void emptyState() {
        containerEmpty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        InfoCardView topadsInfo = containerEmpty.findViewById(R.id.topads_info);
        InfoCardView gmInfo = containerEmpty.findViewById(R.id.gm_info);
        InfoCardView sellerCenterInfo = containerEmpty.findViewById(R.id.seller_center_info);
        TextView btnLearnMore = containerEmpty.findViewById(R.id.btn_learn_more);
        Button btnOpenShop = containerEmpty.findViewById(R.id.btn_open_shop);

        topadsInfo.setImage(R.drawable.ic_topads);
        topadsInfo.setMainText(R.string.title_menu_topads);
        topadsInfo.setSecondaryText(R.string.topads_desc);

        gmInfo.setImage(R.drawable.ic_badge_shop_gm);
        gmInfo.setMainText(R.string.gold_merchant);
        gmInfo.setSecondaryText(R.string.gold_merchant_desc);

        sellerCenterInfo.setImage(R.drawable.ic_seller_center);
        sellerCenterInfo.setMainText(R.string.seller_center);
        sellerCenterInfo.setSecondaryText(R.string.seller_center_desc);

        topadsInfo.setOnClickListener(v -> {
            if(getContext().getApplicationContext() instanceof AccountHomeRouter){
                ((AccountHomeRouter) getContext().getApplicationContext()).
                        gotoTopAdsDashboard(getContext());
            }
        });

        gmInfo.setOnClickListener(v -> {
            if(getContext().getApplicationContext() instanceof AccountHomeRouter){
                ((AccountHomeRouter) getContext().getApplicationContext()).
                        goToGMSubscribe(getContext());
            }
        });

        sellerCenterInfo.setOnClickListener(v ->
                RouteManager.route(getActivity(), ApplinkConst.SELLER_CENTER));

        btnOpenShop.setOnClickListener(v -> {
            if(getContext().getApplicationContext() instanceof AccountHomeRouter){
                startActivity(((AccountHomeRouter) getContext().getApplicationContext()).
                        getIntentCreateShop(getContext()));
            }
        });

        btnLearnMore.setOnClickListener(v -> RouteManager.route(getActivity(), String.format("%s?url=%s",
                ApplinkConst.WEBVIEW,
                AccountConstants.Url.MORE_SELLER)));

    }
}
