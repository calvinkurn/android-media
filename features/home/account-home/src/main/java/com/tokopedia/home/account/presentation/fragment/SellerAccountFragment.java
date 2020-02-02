package com.tokopedia.home.account.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.home.account.AccountConstants;
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
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.track.TrackApp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * @author okasurya on 7/16/18.
 */
public class SellerAccountFragment extends BaseAccountFragment implements AccountItemListener,
        SellerAccount.View, FragmentListener {

    public static final String TAG = SellerAccountFragment.class.getSimpleName();
    public static final String SELLER_DATA = "seller_data";
    private static final String FPM_SELLER = "mp_account_seller";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SellerAccountAdapter adapter;
    private PerformanceMonitoring fpmSeller;

    @Inject
    SellerAccount.Presenter presenter;
    private boolean isLoaded = false;

    public static SellerAccountFragment newInstance() {
        SellerAccountFragment fragment = new SellerAccountFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fpmSeller = PerformanceMonitoring.start(FPM_SELLER);
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
    public void onResume() {
        super.onResume();
        if (isOpenShop) {
            isLoaded = false;
            getData();
            isOpenShop = false;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SellerAccountAdapter(new AccountTypeFactory(this), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        getData();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            isLoaded = false;
            getData();
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getData();
            TrackApp.getInstance().getGTM().sendScreenAuthenticated(getScreenName());
        }
    }

    private void getData() {
        if (!isLoaded && getContext() != null) {
            String saldoQuery = GraphqlHelper.loadRawString(
                    getContext().getResources(),
                    R.raw.new_query_saldo_balance);
            presenter.getSellerData(GraphqlHelper.loadRawString(getContext().getResources(), R.raw.query_seller_account_home),
                    GraphqlHelper.loadRawString(getContext().getResources(), R.raw.gql_get_deposit), saldoQuery);
            isLoaded = !isLoaded;
        }
    }

    @Override
    public void loadSellerData(SellerViewModel model) {
        if (model.getItems() != null) {
            adapter.clearAllElements();
            adapter.setElement(model.getItems());
        }
        fpmSeller.stopTrace();
    }

    @Override
    protected String getScreenName() {
        return String.format("/%s/%s",
                AccountConstants.Analytics.USER,
                AccountConstants.Analytics.JUAL);
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
        if (getView() != null && getUserVisibleHint()) {
            ToasterError.make(getView(), message)
                    .setAction(getString(R.string.title_try_again), view -> getData())
                    .show();
        }

        fpmSeller.stopTrace();
    }

    @Override
    public void showError(Throwable e) {
        if (getView() != null && getContext() != null && getUserVisibleHint()) {
            ToasterError.make(getView(), ErrorHandler.getErrorMessage(getContext(), e))
                    .setAction(getString(R.string.title_try_again), view -> getData())
                    .show();
        }

        fpmSeller.stopTrace();
    }

    @Override
    public void showErrorNoConnection() {
        showError(getString(R.string.error_no_internet_connection));
    }

    @Override
    public void onScrollToTop() {
        if (recyclerView != null)
            recyclerView.scrollToPosition(0);
    }

    @Override
    public boolean isLightThemeStatusBar() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == OPEN_SHOP_SUCCESS) {
            isLoaded = false;
            getData();
        } else if (resultCode == Activity.RESULT_OK && requestCode == BaseAccountFragment.REQUEST_PHONE_VERIFICATION) {
            userSession.setIsMSISDNVerified(true);
            moveToCreateShop();
        }
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

    @Override
    public void onProductRecommendationClicked(@NotNull RecommendationItem product, int adapterPosition, String widgetTitle) {

    }

    @Override
    public void onProductRecommendationImpression(@NotNull RecommendationItem product, int adapterPosition) {

    }

    @Override
    public void onProductRecommendationWishlistClicked(@NotNull RecommendationItem product, boolean wishlistStatus, @NotNull Function2<? super Boolean, ? super Throwable, Unit> callback) {

    }
}
