package com.tokopedia.checkout.view.feature.emptycart;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.AutoApplyData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.recentview.RecentView;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.common.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveView;
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveWithBackView;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.feature.emptycart.adapter.RecentViewAdapter;
import com.tokopedia.checkout.view.feature.emptycart.adapter.WishlistAdapter;
import com.tokopedia.checkout.view.feature.emptycart.di.DaggerEmptyCartComponent;
import com.tokopedia.checkout.view.feature.emptycart.di.EmptyCartComponent;
import com.tokopedia.checkout.view.feature.emptycart.di.EmptyCartModule;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.navigation_common.listener.EmptyCartListener;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsView;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.transaction.common.constant.CartConstant.TOPADS_CART_SRC;

/**
 * Created by Irfan Khoirul on 14/09/18.
 */

public class EmptyCartFragment extends BaseCheckoutFragment
        implements EmptyCartContract.View, TopAdsItemClickListener, TopAdsListener,
        WishlistAdapter.ActionListener, RecentViewAdapter.ActionListener {

    private static final int TOP_ADS_COUNT = 4;
    private static final int REQUEST_CODE_ROUTE_WISHLIST = 123;
    private static final int REQUEST_CODE_ROUTE_RECENT_VIEW = 321;
    private static final String EMPTY_CART_TRACE = "mp_empty_cart";
    private static final String EMPTY_CART_ALL_TRACE = "mp_empty_cart_all";

    public static final String ARG_AUTO_APPLY_MESSAGE = "ARG_AUTO_APPLY_MESSAGE";
    private static final String ARG_AUTO_APPLY_STATE = "ARG_AUTO_APPLY_STATE";
    private static final String ARG_AUTO_APPLY_TITLE = "ARG_AUTO_APPLY_TITLE";

    private View toolbar;
    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    private TickerCheckoutView tickerCheckoutView;
    private TextView btnContinueShoppingEmptyCart;
    private CardView cvRecommendation;
    private TopAdsView topAdsView;
    private TextViewCompat tvRecommendationSeeAll;
    private TextViewCompat tvRecommendationSeeAllBottom;
    private CardView cvWishList;
    private RelativeLayout rlWishList;
    private TextViewCompat tvWishListSeeAll;
    private RecyclerView rvWishList;
    private CardView cvLastSeen;
    private RelativeLayout rlLastSeen;
    private TextView tvLastSeenSeeAll;
    private RecyclerView rvLastSeen;

    private boolean isToolbarWithBackButton = true;
    private WishlistAdapter wishlistAdapter;
    private RecentViewAdapter recentViewAdapter;

    private PerformanceMonitoring cartPerformanceMonitoring;
    private boolean isCartTraceStopped;

    private PerformanceMonitoring allPerformanceMonitoring;
    private boolean isAllTraceStopped;
    private UserSessionInterface userSession;

    @Inject
    EmptyCartContract.Presenter presenter;
    @Inject
    ICheckoutModuleRouter checkoutModuleRouter;
    @Inject
    CheckoutAnalyticsCart cartPageAnalytics;

    public static EmptyCartFragment newInstance(String autoApplyMessage, String args, String state, String titleDesc) {
        EmptyCartFragment emptyCartFragment = new EmptyCartFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EmptyCartFragment.class.getSimpleName(), args);
        bundle.putString(ARG_AUTO_APPLY_MESSAGE, autoApplyMessage);
        bundle.putString(ARG_AUTO_APPLY_STATE, state);
        bundle.putString(ARG_AUTO_APPLY_TITLE, titleDesc);
        emptyCartFragment.setArguments(bundle);

        return emptyCartFragment;
    }

    @Override
    protected void initInjector() {
        EmptyCartComponent component = DaggerEmptyCartComponent.builder()
                .cartComponent(CartComponentInjector.newInstance(getActivity().getApplication()).getCartApiServiceComponent())
                .emptyCartModule(new EmptyCartModule(this))
                .trackingAnalyticsModule(new TrackingAnalyticsModule())
                .build();
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getActivity());
        cartPerformanceMonitoring = PerformanceMonitoring.start(EMPTY_CART_TRACE);
        allPerformanceMonitoring = PerformanceMonitoring.start(EMPTY_CART_ALL_TRACE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        String args = arguments.getString(EmptyCartFragment.class.getSimpleName());
        if (args != null && !args.isEmpty()) {
            isToolbarWithBackButton = false;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_empty_cart;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected void initView(View view) {
        setupToolbar(view);
        presenter.attachView(this);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        tickerCheckoutView = view.findViewById(R.id.ticker_checkout_view);
        btnContinueShoppingEmptyCart = view.findViewById(R.id.btn_shopping_now);
        cvRecommendation = view.findViewById(R.id.cv_recommendation);
        topAdsView = view.findViewById(R.id.topads);
        tvRecommendationSeeAll = view.findViewById(R.id.tv_recommendation_see_all);
        tvRecommendationSeeAllBottom = view.findViewById(R.id.tv_recommendation_see_all_bottom);
        cvWishList = view.findViewById(R.id.cv_wish_list);
        rlWishList = view.findViewById(R.id.rl_wish_list);
        tvWishListSeeAll = view.findViewById(R.id.tv_wish_list_see_all);
        rvWishList = view.findViewById(R.id.rv_wish_list);
        cvLastSeen = view.findViewById(R.id.cv_last_seen);
        rlLastSeen = view.findViewById(R.id.rl_last_seen);
        tvLastSeenSeeAll = view.findViewById(R.id.tv_last_seen_see_all);
        rvLastSeen = view.findViewById(R.id.rv_last_seen);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (cvRecommendation.getVisibility() == View.VISIBLE) {
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        showSeeOtherRecommendationText();
                    } else {
                        hideSeeOtherRecommendationText();
                    }
                } else {
                    hideSeeOtherRecommendationText();
                }
            }
        });

        rvWishList.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) rvWishList.getItemAnimator()).setSupportsChangeAnimations(false);
        rvLastSeen.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) rvLastSeen.getItemAnimator()).setSupportsChangeAnimations(false);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.processInitialGetCartData());
        tvWishListSeeAll.setOnClickListener(v -> {
                    cartPageAnalytics.eventClickLihatSemuaWishlist();
                    startActivityForResult(
                            checkoutModuleRouter.checkoutModuleRouterGetWhislistIntent(),
                            REQUEST_CODE_ROUTE_WISHLIST);
                }
        );
        tvLastSeenSeeAll.setOnClickListener(v -> {
            cartPageAnalytics.eventClickLihatSemuaLastSeen();
            startActivityForResult(
                    checkoutModuleRouter.checkoutModuleRouterGetRecentViewIntent(),
                    REQUEST_CODE_ROUTE_RECENT_VIEW);
        });
        tvRecommendationSeeAll.setOnClickListener(v -> {
            cartPageAnalytics.eventClickAtcCartClickBelanjaSekarangOnEmptyCart();
            navigateToHome();
        });
        tvRecommendationSeeAllBottom.setOnClickListener(v -> {
            cartPageAnalytics.eventClickLihatLainnya();
            navigateToHome();
        });

        AutoApplyData autoApplyData = new AutoApplyData();
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString(ARG_AUTO_APPLY_MESSAGE))) {
            autoApplyData.setMessageSuccess(getArguments().getString(ARG_AUTO_APPLY_MESSAGE));
            autoApplyData.setState(getArguments().getString(ARG_AUTO_APPLY_STATE));
            autoApplyData.setTitleDescription(getArguments().getString(ARG_AUTO_APPLY_TITLE));
        }
        renderEmptyCart(autoApplyData);
        stopCartTrace();
    }

    @Override
    public void renderEmptyCart(AutoApplyData autoApplyData) {
        double itemWidth = getItemWidth();

        renderAutoApplyPromo(autoApplyData);
        renderTopAds();
        renderWishList((int) itemWidth);
        renderRecentView((int) itemWidth);
        cartPageAnalytics.sendScreenName(getActivity(), getScreenName());
    }

    @Override
    public void stopCartTrace() {
        if (!isCartTraceStopped) {
            cartPerformanceMonitoring.stopTrace();
            isCartTraceStopped = true;
        }
    }

    @Override
    public void stopAllTrace() {
        if (!isAllTraceStopped && presenter.hasLoadAllApi()) {
            allPerformanceMonitoring.stopTrace();
            isAllTraceStopped = true;
        }
    }

    @Override
    public boolean isCartTraceStopped() {
        return isCartTraceStopped;
    }

    @Override
    public boolean isAllTraceStopped() {
        return isAllTraceStopped;
    }

    private double getItemWidth() {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        return (double) (deviceWidth / 2.0f);
    }

    private void renderWishList(int imageWidth) {
        presenter.processGetWishlistData();
        wishlistAdapter = new WishlistAdapter(presenter, this, imageWidth);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvWishList.setLayoutManager(gridLayoutManager);
        rvWishList.setAdapter(wishlistAdapter);
    }

    @Override
    public void renderHasWishList(boolean hasMoreItem) {
        cartPageAnalytics.enhancedEcommerceProductViewWishListOnEmptyCart(
                presenter.generateEmptyCartAnalyticViewProductWishlistDataLayer());
        cvWishList.setVisibility(View.VISIBLE);
        wishlistAdapter.notifyDataSetChanged();
        if (hasMoreItem) {
            tvWishListSeeAll.setVisibility(View.VISIBLE);
        } else {
            tvWishListSeeAll.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderHasNoWishList() {
        cvWishList.setVisibility(View.GONE);
    }

    private void renderRecentView(int imageWidth) {
        presenter.processGetRecentViewData(Integer.parseInt(userSession.getUserId()));
        recentViewAdapter = new RecentViewAdapter(presenter, this, imageWidth);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvLastSeen.setLayoutManager(gridLayoutManager);
        rvLastSeen.setAdapter(recentViewAdapter);
    }

    @Override
    public void renderHasRecentView(boolean hasMoreItem) {
        cartPageAnalytics.enhancedEcommerceProductViewLastSeenOnEmptyCart(
                presenter.generateEmptyCartAnalyticViewProductRecentViewDataLayer());
        cvLastSeen.setVisibility(View.VISIBLE);
        recentViewAdapter.notifyDataSetChanged();
        if (hasMoreItem) {
            tvLastSeenSeeAll.setVisibility(View.VISIBLE);
        } else {
            tvLastSeenSeeAll.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderHasNoRecentView() {
        cvLastSeen.setVisibility(View.GONE);
    }

    private void renderAutoApplyPromo(AutoApplyData autoApplyData) {
        if (autoApplyData!= null && TickerCheckoutUtilKt.mapToStatePromoCheckout(autoApplyData.getState()) != TickerCheckoutView.State.EMPTY) {
            tickerCheckoutView.setVisibility(View.VISIBLE);
            tickerCheckoutView.setState(TickerCheckoutUtilKt.mapToStatePromoCheckout(autoApplyData.getState()));
            tickerCheckoutView.setTitle(autoApplyData.getTitleDescription());
            tickerCheckoutView.setDesc(autoApplyData.getMessageSuccess());
            tickerCheckoutView.setActionListener(new TickerCheckoutView.ActionListener() {
                @Override
                public void onClickUsePromo() {
                    //do nothing
                }

                @Override
                public void onDisablePromoDiscount() {
                    presenter.processCancelAutoApply();
                }

                @Override
                public void onClickDetailPromo() {
                    //do nothing
                }
            });
        } else {
            tickerCheckoutView.setVisibility(View.GONE);
        }
        btnContinueShoppingEmptyCart.setOnClickListener(v -> {
            cartPageAnalytics.eventClickAtcCartClickBelanjaSekarangOnEmptyCart();
            navigateToHome();
        });
    }

    private void navigateToHome() {
        if (getActivity() != null) {
            startActivity(
                    checkoutModuleRouter.checkoutModuleRouterGetHomeIntent(getActivity())
            );
            getActivity().finish();
        }
    }

    private void renderTopAds() {
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, TOPADS_CART_SRC);

        Config config = new Config.Builder()
                .setSessionId(userSession.getDeviceId())
                .setUserId(userSession.getUserId())
                .withPreferedCategory()
                .setEndpoint(Endpoint.PRODUCT)
                .displayMode(DisplayMode.FEED)
                .topAdsParams(params)
                .build();

        topAdsView.setConfig(config);
        topAdsView.setDisplayMode(DisplayMode.FEED);
        topAdsView.setMaxItems(TOP_ADS_COUNT);
        topAdsView.setAdsItemClickListener(this);
        topAdsView.loadTopAds();
        topAdsView.setAdsListener(this);
        topAdsView.setAdsImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionProductAdsItem(int position, Product product) {
                TopAdsGtmTracker.eventCartEmptyProductView(getContext(), product, position);
            }
        });
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showLoading() {
        nestedScrollView.scrollTo(0, 0);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
        nestedScrollView.scrollTo(0, 0);
    }

    @Override
    public void showErrorToast(String message) {
        if (TextUtils.isEmpty(message)) {
            message = getActivity().getString(R.string.default_request_error_unknown);
        }
        NetworkErrorHelper.showRedSnackbar(getActivity(), message);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return originParams == null
                ? AuthUtil.generateParamsNetwork(
                getActivity(), userSession.getUserId(),
                userSession.getDeviceId()
        )
                : AuthUtil.generateParamsNetwork(
                getActivity(), originParams,
                userSession.getUserId(),
                userSession.getDeviceId()
        );
    }

    @Override
    public void renderCancelAutoApplyCouponSuccess() {
        tickerCheckoutView.setVisibility(View.GONE);
    }

    @Override
    public void navigateToCartFragment(CartListData cartListData) {
        cartPageAnalytics.sendScreenName(getActivity(), getScreenName());
        if (getActivity() instanceof EmptyCartListener) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(EmptyCartListener.ARG_CART_LIST_DATA, cartListData);
            ((EmptyCartListener) getActivity()).onCartNotEmpty(bundle);
        }
    }

    @Override
    protected String getScreenName() {
        return ConstantTransactionAnalytics.ScreenName.CART;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            presenter.processInitialGetCartData();
        }
    }

    private void setupToolbar(View view) {
        Toolbar appbar = view.findViewById(R.id.toolbar);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        if (isToolbarWithBackButton) {
            toolbar = toolbarRemoveWithBackView();
        } else {
            toolbar = toolbarRemoveView();
                
            // add padding programmatically
            int padding = (int) (24*getResources().getDisplayMetrics().density + 0.5f);
            view.setPadding(0,padding,0,0);
        }
        appbar.addView(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(appbar);
        setVisibilityRemoveButton(false);
    }

    private ToolbarRemoveWithBackView toolbarRemoveWithBackView() {
        ToolbarRemoveWithBackView toolbar = new ToolbarRemoveWithBackView(getActivity());
        toolbar.navigateUp(getActivity());
        toolbar.setTitle(getString(R.string.cart));
        return toolbar;
    }

    private ToolbarRemoveView toolbarRemoveView() {
        ToolbarRemoveView toolbar = new ToolbarRemoveView(getActivity());
        toolbar.setTitle(getString(R.string.cart));
        return toolbar;
    }

    private void setVisibilityRemoveButton(boolean state) {
        if (toolbar != null) {
            if (toolbar instanceof ToolbarRemoveView) {
                ((ToolbarRemoveView) toolbar).setVisibilityRemove(state);
            } else if (toolbar instanceof ToolbarRemoveWithBackView) {
                ((ToolbarRemoveWithBackView) toolbar).setVisibilityRemove(state);
            }
        }
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        cartPageAnalytics.enhancedEcommerceClickProductRecommendationOnEmptyCart(
                String.valueOf(position + 1), presenter.generateEmptyCartAnalyticProductClickDataLayer(product, position + 1));
        startActivity(getProductIntent(product.getId()));
        TopAdsGtmTracker.eventCartEmptyProductClick(getContext(), product, position);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        startActivity(checkoutModuleRouter.checkoutModuleRouterGetShopInfoIntent(shop.getId()));
    }

    @Override
    public void onAddFavorite(int position, Data data) {

    }

    @Override
    public void onItemWishListClicked(Wishlist wishlist, int position) {
        cartPageAnalytics.enhancedEcommerceClickProductWishListOnEmptyCart(
                String.valueOf(position), presenter.generateEmptyCartAnalyticProductClickDataLayer(wishlist, position));
        startActivityForResult(getProductIntent(wishlist.getId()), REQUEST_CODE_ROUTE_WISHLIST);
    }

    private Intent getProductIntent(String productId){
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(),ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ROUTE_WISHLIST || requestCode == REQUEST_CODE_ROUTE_RECENT_VIEW) {
            presenter.processInitialGetCartData();
        }
    }

    @Override
    public void onItemRecentViewClicked(RecentView recentView, int position) {
        cartPageAnalytics.enhancedEcommerceClickProductLastSeenOnEmptyCart(
                String.valueOf(position), presenter.generateEmptyCartAnalyticProductClickDataLayer(recentView, position));

        startActivityForResult(getProductIntent(recentView.getProductId()), REQUEST_CODE_ROUTE_WISHLIST);
    }

    @Override
    public void onTopAdsLoaded(List<Item> list) {
        presenter.setRecommendationList(list);
        cartPageAnalytics.enhancedEcommerceProductViewRecommendationOnEmptyCart(
                presenter.generateEmptyCartAnalyticViewProductRecommendationDataLayer());
        cvRecommendation.setVisibility(View.VISIBLE);
        tvRecommendationSeeAllBottom.setVisibility(View.VISIBLE);
        if (!isAllTraceStopped) {
            presenter.setLoadApiStatus(EmptyCartApi.SUGGESTION, true);
            stopAllTrace();
        }
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        cvRecommendation.setVisibility(View.GONE);
        tvRecommendationSeeAllBottom.setVisibility(View.GONE);
        if (!isAllTraceStopped) {
            presenter.setLoadApiStatus(EmptyCartApi.SUGGESTION, true);
            stopAllTrace();
        }
    }

    private void hideSeeOtherRecommendationText() {
        tvRecommendationSeeAllBottom.animate()
                .scaleX(0)
                .scaleY(0);
    }

    private void showSeeOtherRecommendationText() {
        tvRecommendationSeeAllBottom.animate()
                .scaleY(1)
                .scaleX(1);
    }
}
