package com.tokopedia.checkout.view.feature.emptycart;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.common.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveView;
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveWithBackView;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.feature.emptycart.di.DaggerEmptyCartComponent;
import com.tokopedia.checkout.view.feature.emptycart.di.EmptyCartComponent;
import com.tokopedia.checkout.view.feature.emptycart.di.EmptyCartModule;
import com.tokopedia.navigation_common.listener.EmptyCartListener;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsView;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.transaction.common.constant.CartConstant.TOPADS_CART_SRC;

/**
 * Created by Irfan Khoirul on 14/09/18.
 */

public class EmptyCartFragment extends BaseCheckoutFragment
        implements EmptyCartContract.View, TopAdsItemClickListener {

    public static final String ARG_AUTO_APPLY_MESSAGE = "ARG_AUTO_APPLY_MESSAGE";
    private static final int TOP_ADS_COUNT = 4;

    private View toolbar;
    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout layoutUsedPromo;
    private TextView tvPromoCodeEmptyCart;
    private AppCompatImageView btnCancelPromoCodeEmptyCart;
    private TextView btnContinueShoppingEmptyCart;
    private TopAdsView topAdsView;

    private boolean isToolbarWithBackButton = true;

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
    protected void initView(View view) {
        setupToolbar(view);
        presenter.attachView(this);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        layoutUsedPromo = view.findViewById(R.id.layout_used_promo);
        tvPromoCodeEmptyCart = view.findViewById(R.id.textview_promo_code);
        btnCancelPromoCodeEmptyCart = view.findViewById(R.id.button_cancel);
        btnContinueShoppingEmptyCart = view.findViewById(R.id.btn_shopping_now);
        topAdsView = view.findViewById(R.id.topads);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.processInitialGetCartData();
            }
        });

        String autoApplyMessage = null;
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString(ARG_AUTO_APPLY_MESSAGE))) {
            autoApplyMessage = getArguments().getString(ARG_AUTO_APPLY_MESSAGE);
        }
        renderEmptyCart(autoApplyMessage);
    }

    @Override
    public void renderEmptyCart(String autoApplyMessage) {
        renderAutoApplyPromo(autoApplyMessage);
        renderTopAds();
    }

    @Override
    public void renderHasWishlist(List<Wishlist> wishlistData) {
        // Todo : bind to RV
    }

    @Override
    public void renderHasNoWishlist() {

    }

    private void renderAutoApplyPromo(String autoApplyMessage) {
        if (!TextUtils.isEmpty(autoApplyMessage)) {
            tvPromoCodeEmptyCart.setText(autoApplyMessage);
            btnCancelPromoCodeEmptyCart.setOnClickListener(v -> presenter.processCancelAutoApply());
            layoutUsedPromo.setVisibility(View.VISIBLE);
        } else {
            layoutUsedPromo.setVisibility(View.GONE);
        }
        btnContinueShoppingEmptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartPageAnalytics.eventClickAtcCartClickBelanjaSekarangOnEmptyCart();
                startActivity(
                        checkoutModuleRouter.checkoutModuleRouterGetHomeFeedIntent(getActivity())
                );
                getActivity().finish();
            }
        });
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
}
