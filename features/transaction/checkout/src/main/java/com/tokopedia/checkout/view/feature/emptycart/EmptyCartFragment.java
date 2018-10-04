package com.tokopedia.checkout.view.feature.emptycart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
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
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsView;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;

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

    public static final String ARG_AUTO_APPLY_MESSAGE = "ARG_AUTO_APPLY_MESSAGE";

    private View toolbar;
    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    private RelativeLayout layoutUsedPromo;
    private TextView tvPromoCodeEmptyCart;
    private AppCompatImageView btnCancelPromoCodeEmptyCart;
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

    @Inject
    UserSession userSession;
    @Inject
    EmptyCartContract.Presenter presenter;
    @Inject
    ICheckoutModuleRouter checkoutModuleRouter;
    @Inject
    CheckoutAnalyticsCart cartPageAnalytics;

    public static EmptyCartFragment newInstance(String autoApplyMessage, String args) {
        EmptyCartFragment emptyCartFragment = new EmptyCartFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EmptyCartFragment.class.getSimpleName(), args);
        bundle.putString(ARG_AUTO_APPLY_MESSAGE, autoApplyMessage);
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
        layoutUsedPromo = view.findViewById(R.id.layout_used_promo);
        tvPromoCodeEmptyCart = view.findViewById(R.id.textview_promo_code);
        btnCancelPromoCodeEmptyCart = view.findViewById(R.id.button_cancel);
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
        rvLastSeen.setNestedScrollingEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.processInitialGetCartData());
        tvWishListSeeAll.setOnClickListener(v -> startActivityForResult(
                checkoutModuleRouter.checkoutModuleRouterGetWhislistIntent(),
                REQUEST_CODE_ROUTE_WISHLIST)
        );
        tvLastSeenSeeAll.setOnClickListener(v -> startActivityForResult(
                checkoutModuleRouter.checkoutModuleRouterGetRecentViewIntent(),
                REQUEST_CODE_ROUTE_RECENT_VIEW));
        tvRecommendationSeeAll.setOnClickListener(v -> navigateToHome());
        tvRecommendationSeeAllBottom.setOnClickListener(v -> navigateToHome());

        String autoApplyMessage = null;
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString(ARG_AUTO_APPLY_MESSAGE))) {
            autoApplyMessage = getArguments().getString(ARG_AUTO_APPLY_MESSAGE);
        }
        renderEmptyCart(autoApplyMessage);
    }

    @Override
    public void renderEmptyCart(String autoApplyMessage) {
        double itemWidth = getItemWidth();

        renderAutoApplyPromo(autoApplyMessage);
        renderTopAds();
        renderWishList((int) itemWidth);
        renderRecentView((int) itemWidth);
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

    private void renderAutoApplyPromo(String autoApplyMessage) {
        if (!TextUtils.isEmpty(autoApplyMessage)) {
            tvPromoCodeEmptyCart.setText(autoApplyMessage);
            btnCancelPromoCodeEmptyCart.setOnClickListener(v -> presenter.processCancelAutoApply());
            layoutUsedPromo.setVisibility(View.VISIBLE);
        } else {
            layoutUsedPromo.setVisibility(View.GONE);
        }
        btnContinueShoppingEmptyCart.setOnClickListener(v -> {
            navigateToHome();
        });
    }

    private void navigateToHome() {
        if (getActivity() != null) {
            cartPageAnalytics.eventClickAtcCartClickBelanjaSekarangOnEmptyCart();
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
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
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
        layoutUsedPromo.setVisibility(View.GONE);
    }

    @Override
    public void navigateToCartFragment(CartListData cartListData) {
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
            cartPageAnalytics.sendScreenName(getActivity(), getScreenName());
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
                String.valueOf(position), presenter.generateEmptyCartAnalyticProductClickDataLayer(product, position + 1));
        startActivity(checkoutModuleRouter.checkoutModuleRouterGetProductDetailIntentForTopAds(product));
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        startActivity(checkoutModuleRouter.checkoutModuleRouterGetShopInfoIntent(shop.getId()));
    }

    @Override
    public void onAddFavorite(int position, Data data) {

    }

    @Override
    public void onAddWishList(int position, Data data) {

    }

    @Override
    public void onItemWishListClicked(Wishlist wishlist, int position) {
        cartPageAnalytics.enhancedEcommerceClickProductWishListOnEmptyCart(
                String.valueOf(position), presenter.generateEmptyCartAnalyticProductClickDataLayer(wishlist, position));
        startActivityForResult(checkoutModuleRouter.checkoutModuleRouterGetProductDetailIntent(
                wishlist.getId()
        ), REQUEST_CODE_ROUTE_WISHLIST);
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

        startActivityForResult(checkoutModuleRouter.checkoutModuleRouterGetProductDetailIntent(
                recentView.getProductId()
        ), REQUEST_CODE_ROUTE_WISHLIST);
    }

    @Override
    public void onTopAdsLoaded() {
        cvRecommendation.setVisibility(View.VISIBLE);
        tvRecommendationSeeAllBottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        cvRecommendation.setVisibility(View.GONE);
        tvRecommendationSeeAllBottom.setVisibility(View.GONE);
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
