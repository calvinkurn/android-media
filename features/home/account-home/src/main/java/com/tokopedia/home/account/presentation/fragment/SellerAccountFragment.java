package com.tokopedia.home.account.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.di.component.DaggerSellerAccountComponent;
import com.tokopedia.home.account.di.component.SellerAccountComponent;
import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.adapter.seller.SellerAccountAdapter;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.util.AccountHomeErrorHandler;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TickerViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking;
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants;
import com.tokopedia.seller_migration_common.presentation.widget.SellerMigrationAccountBottomSheet;
import com.tokopedia.seller_migration_common.presentation.widget.SellerMigrationVoucherTokoBottomSheet;
import com.tokopedia.track.TrackApp;
import com.tokopedia.unifycomponents.BottomSheetUnify;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.unifycomponents.ticker.TickerCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import static com.tokopedia.seller_migration_common.SellerMigrationRemoteConfigKt.getSellerMigrationDate;
import static com.tokopedia.seller_migration_common.SellerMigrationRemoteConfigKt.isSellerMigrationEnabled;

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
    private Ticker migrationTicker;
    private SellerMigrationVoucherTokoBottomSheet sellerMigrationVoucherTokoBottomSheet;

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
        migrationTicker = view.findViewById(R.id.account_seller_migration_ticker);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                .VERTICAL, false));
        swipeRefreshLayout.setColorSchemeResources(R.color.tkpd_main_green);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoaded = false;
        getData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SellerAccountAdapter(new AccountTypeFactory(this), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        setupSellerMigrationTicker();
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
            presenter.getSellerData(
                    GraphqlHelper.loadRawString(getContext().getResources(), R.raw.query_seller_account_home),
                    GraphqlHelper.loadRawString(getContext().getResources(), R.raw.gql_get_deposit),
                    saldoQuery,
                    GraphqlHelper.loadRawString(getContext().getResources(), R.raw.query_shop_location)
            );
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
            Toaster.INSTANCE.make(getView(), message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(R.string.title_try_again), v -> getData());
        }

        fpmSeller.stopTrace();
    }

    @Override
    public void showError(Throwable e, String errorCode) {
        if (getView() != null && getContext() != null && getUserVisibleHint()) {
            String message = String.format("%s (%s)", ErrorHandler.getErrorMessage(getActivity(), e), errorCode);
            Toaster.INSTANCE.make(getView(), message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(R.string.title_try_again), v -> getData());
        }
        AccountHomeErrorHandler.logExceptionToCrashlytics(e, userSession.getUserId(), userSession.getEmail(), errorCode);
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
    public void onMenuListClicked(MenuListViewModel item) {
        if (item.getMenu().equalsIgnoreCase(getString(R.string.title_menu_voucher_toko))) {
            sendTracking(item.getTitleTrack(), item.getSectionTrack(), item.getMenu());
            showSellerMigrationVoucherTokoBottomSheet();
        } else {
            super.onMenuListClicked(item);
        }
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

    @Override
    public void onProductRecommendationThreeDotsClicked(@NotNull RecommendationItem product, int adapterPosition) {

    }

    private void setupSellerMigrationTicker() {
        if (isSellerMigrationEnabled(this.getContext())) {
            migrationTicker.setTickerTitle(getString(com.tokopedia.seller_migration_common.R.string.seller_migration_account_home_ticker_title));
            String remoteConfigDate = getSellerMigrationDate(this.getContext());
            if (remoteConfigDate.isEmpty()) {
                migrationTicker.setHtmlDescription(getString(com.tokopedia.seller_migration_common.R.string.seller_migration_generic_ticker_content));
            } else {
                migrationTicker.setHtmlDescription(getString(com.tokopedia.seller_migration_common.R.string.seller_migration_account_home_ticker_content, remoteConfigDate));
            }
            migrationTicker.setDescriptionClickEvent(new TickerCallback() {
                @Override
                public void onDescriptionViewClick(@NotNull CharSequence charSequence) {
                    SellerMigrationTracking.INSTANCE.eventOnClickAccountTicker(userSession.getUserId());
                    openSellerMigrationBottomSheet();
                }

                @Override
                public void onDismiss() {
                    // No op
                }
            });
        } else {
            migrationTicker.setVisibility(View.GONE);
        }
    }

    private void openSellerMigrationBottomSheet() {
        if (getContext() != null) {
            BottomSheetUnify sellerMigrationBottomSheet = SellerMigrationAccountBottomSheet.Companion.createNewInstance(getContext());
            sellerMigrationBottomSheet.show(getChildFragmentManager(), SellerMigrationConstants.TAG_SELLER_MIGRATION_BOTTOM_SHEET);
        }
    }

    private void showSellerMigrationVoucherTokoBottomSheet() {
        if (getContext() != null) {
            if (sellerMigrationVoucherTokoBottomSheet == null) {
                sellerMigrationVoucherTokoBottomSheet = SellerMigrationVoucherTokoBottomSheet.Companion.createNewInstance(getContext());
            }
            sellerMigrationVoucherTokoBottomSheet.show(getChildFragmentManager(), SellerMigrationConstants.TAG_SELLER_MIGRATION_BOTTOM_SHEET);
        }
    }
}
