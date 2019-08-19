package com.tokopedia.checkout.view.feature.cartlist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartTickerData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ShopGroupData;
import com.tokopedia.checkout.domain.datamodel.promostacking.AutoApplyStackData;
import com.tokopedia.checkout.domain.datamodel.promostacking.MessageData;
import com.tokopedia.checkout.domain.datamodel.promostacking.VoucherOrdersItemData;
import com.tokopedia.checkout.domain.datamodel.recentview.RecentView;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.common.PromoActionListener;
import com.tokopedia.checkout.view.common.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveView;
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveWithBackView;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.di.component.CartListComponent;
import com.tokopedia.checkout.view.di.component.DaggerCartListComponent;
import com.tokopedia.checkout.view.di.module.CartListModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.feature.bottomsheetpromostacking.ClashBottomSheetFragment;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartItemAdapter;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartRecommendationViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecentViewHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecentViewItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecommendationItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartSectionHeaderHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartWishlistHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartWishlistItemHolderData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentActivity;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.merchantvoucher.voucherlistbottomsheet.MerchantVoucherListBottomSheetFragment;
import com.tokopedia.navigation_common.listener.CartNotifyListener;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutConstantKt;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.data.entity.request.Order;
import com.tokopedia.promocheckout.common.data.entity.request.ProductDetail;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.TrackingDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCartMapData;
import com.tokopedia.transactiondata.entity.request.UpdateCartRequest;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartFragment extends BaseCheckoutFragment implements ActionListener,
        CartItemAdapter.ActionListener, ICartListView, TopAdsItemClickListener, PromoActionListener,
        RefreshHandler.OnRefreshHandlerListener, ICartListAnalyticsListener, WishListActionListener,
        ToolbarRemoveView.OnToolbarRemoveAllCartListener, MerchantVoucherListBottomSheetFragment.ActionListener,
        ClashBottomSheetFragment.ActionListener {

    public static final int SHOP_INDEX_PROMO_GLOBAL = -1;

    private static final int HAS_ELEVATION = 8;
    private static final int NO_ELEVATION = 0;
    private static final String CART_TRACE = "mp_cart";
    private static final String CART_ALL_TRACE = "mp_cart_all";
    private static final int NAVIGATION_PDP = 64728;
    public static final int GO_TO_DETAIL = 2;
    public static final int GO_TO_LIST = 1;
    private boolean FLAG_BEGIN_SHIPMENT_PROCESS = false;
    private boolean FLAG_SHOULD_CLEAR_RECYCLERVIEW = false;
    private boolean FLAG_IS_CART_EMPTY = false;

    private View toolbar;
    private AppBarLayout appBarLayout;
    private RecyclerView cartRecyclerView;
    private TextView btnToShipment;
    private TextView tvTotalPrice;
    private TextView tvItemCount;
    private RelativeLayout layoutUsedPromoEmptyCart;
    private RelativeLayout rlContent;
    private LinearLayout llHeader;
    private CheckBox cbSelectAll;
    private CardView cardHeader;
    private CardView cardFooter;
    private LinearLayout llNetworkErrorView;
    private LinearLayout llCartContainer;
    private Ticker cartTicker;

    private ProgressDialog progressDialog;

    @Inject
    ICartListPresenter dPresenter;
    @Inject
    RecyclerView.ItemDecoration cartItemDecoration;
    @Inject
    CheckoutAnalyticsCart cartPageAnalytics;
    @Inject
    CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection;
    @Inject
    ICheckoutModuleRouter checkoutModuleRouter;
    @Inject
    TrackingPromoCheckoutUtil trackingPromoCheckoutUtil;

    private CartAdapter cartAdapter;
    private RefreshHandler refreshHandler;
    private UserSessionInterface userSession;

    private boolean mIsMenuVisible = false;
    private boolean isToolbarWithBackButton = true;

    private PerformanceMonitoring cartPerformanceMonitoring;
    private boolean isTraceCartStopped;
    private PerformanceMonitoring cartAllPerformanceMonitoring;
    private boolean isTraceCartAllStopped;

    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private boolean hasLoadRecommendation;

    private SaveInstanceCacheManager saveInstanceCacheManager;
    private CartListData cartListData;
    private List<CartWishlistItemHolderData> wishLists;
    private List<CartRecentViewItemHolderData> recentViewList;
    private List<CartRecommendationItemHolderData> recommendationList;
    private boolean hasTriedToLoadWishList;
    private boolean hasTriedToLoadRecentViewList;
    private boolean hasTriedToLoadRecommendation;

    public static CartFragment newInstance(Bundle bundle, String args) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(CartFragment.class.getSimpleName(), args);
        CartFragment fragment = new CartFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        userSession = new UserSession(getActivity());

        if (getActivity() != null) {
            saveInstanceCacheManager = new SaveInstanceCacheManager(getActivity(), savedInstanceState);
        }

        if (savedInstanceState != null && saveInstanceCacheManager != null) {
            cartListData = saveInstanceCacheManager.get(CartListData.class.getSimpleName(), CartListData.class);
            wishLists = saveInstanceCacheManager.get(CartWishlistItemHolderData.class.getSimpleName(),
                    (new TypeToken<ArrayList<CartWishlistItemHolderData>>() {
                    }).getType(), null);
            recentViewList = saveInstanceCacheManager.get(CartRecentViewItemHolderData.class.getSimpleName(),
                    (new TypeToken<ArrayList<CartRecentViewItemHolderData>>() {
                    }).getType(), null);
            recommendationList = saveInstanceCacheManager.get(CartRecommendationItemHolderData.class.getSimpleName(),
                    (new TypeToken<ArrayList<CartRecommendationItemHolderData>>() {
                    }).getType(), null);
        } else {
            cartPerformanceMonitoring = PerformanceMonitoring.start(CART_TRACE);
            cartAllPerformanceMonitoring = PerformanceMonitoring.start(CART_ALL_TRACE);
        }

        dPresenter.attachView(this);
    }

    @Override
    public void onStop() {
        boolean hasChanges = dPresenter.dataHasChanged();

        try {
            if (hasChanges && getActivity() != null && getSelectedCartDataList() != null
                    && getSelectedCartDataList().size() > 0 && !FLAG_BEGIN_SHIPMENT_PROCESS) {
                Intent service = new Intent(getActivity(), UpdateCartIntentService.class);
                service.putParcelableArrayListExtra(
                        UpdateCartIntentService.EXTRA_CART_ITEM_DATA_LIST, new ArrayList<>(getSelectedCartDataList())
                );
                getActivity().startService(service);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        if (FLAG_SHOULD_CLEAR_RECYCLERVIEW) {
            clearRecyclerView();
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {
        cartAdapter.unsubscribeSubscription();
        dPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void initInjector() {
        CartListComponent cartListComponent = DaggerCartListComponent.builder()
                .cartComponent(CartComponentInjector.newInstance(getActivity().getApplication()).getCartApiServiceComponent())
                .cartListModule(new CartListModule(this))
                .trackingAnalyticsModule(new TrackingAnalyticsModule())
                .promoCheckoutModule(new PromoCheckoutModule())
                .build();
        cartListComponent.inject(this);
        cartAdapter = new CartAdapter(this, this, this);
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
        return true;
    }

    @Override
    protected void initialListener(Activity activity) {
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        if (arguments != null) {
            String args = arguments.getString(CartFragment.class.getSimpleName());
            if (args != null && !args.isEmpty()) {
                isToolbarWithBackButton = false;
            }
        }
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

    private void onContentAvailabilityChanged(boolean available) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (available) {
                appBarLayout.setElevation(NO_ELEVATION);
            } else {
                appBarLayout.setElevation(HAS_ELEVATION);
            }
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void initView(View view) {
        setupToolbar(view);
        cartRecyclerView = view.findViewById(R.id.rv_cart);
        btnToShipment = view.findViewById(R.id.go_to_courier_page_button);
        tvTotalPrice = view.findViewById(R.id.tv_total_prices);
        tvItemCount = view.findViewById(R.id.tv_item_count);
        rlContent = view.findViewById(R.id.rl_content);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        cardHeader = view.findViewById(R.id.card_header);
        cardFooter = view.findViewById(R.id.card_footer);
        llHeader = view.findViewById(R.id.ll_header);
        cbSelectAll = view.findViewById(R.id.cb_select_all);
        llCartContainer = view.findViewById(R.id.ll_cart_container);
        cartTicker = view.findViewById(R.id.cart_ticker);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        progressDialog.setCancelable(false);

        refreshHandler = new RefreshHandler(getActivity(), view, this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        cartRecyclerView.setLayoutManager(layoutManager);
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.addItemDecoration(cartItemDecoration);
        cartRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.canScrollVertically(-1)) {
                    disableSwipeRefresh();
                } else {
                    enableSwipeRefresh();
                }
            }
        });

        ((SimpleItemAnimator) cartRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position != RecyclerView.NO_POSITION) {
                    if (position < cartAdapter.getItemCount() && cartAdapter.getItemViewType(position) == CartRecommendationViewHolder.getLAYOUT()) {
                        return 1;
                    }
                    return 2;
                }
                return 0;
            }
        });
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (hasLoadRecommendation) {
                    dPresenter.processGetRecommendationData(endlessRecyclerViewScrollListener.getCurrentPage());
                }
            }
        };
        cartRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    private void setupToolbar(View view) {
        Toolbar appbar = view.findViewById(R.id.toolbar);
        View statusBarBackground = view.findViewById(R.id.status_bar_bg);
        statusBarBackground.getLayoutParams().height =
                DisplayMetricUtils.getStatusBarHeight(getActivity());

        appBarLayout = view.findViewById(R.id.app_bar_layout);
        if (isToolbarWithBackButton) {
            toolbar = toolbarRemoveWithBackView();
            statusBarBackground.setVisibility(View.GONE);
        } else {
            toolbar = toolbarRemoveView();
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarBackground.setVisibility(View.INVISIBLE);
            } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                statusBarBackground.setVisibility(View.VISIBLE);
            } else {
                statusBarBackground.setVisibility(View.GONE);
            }
        }
        appbar.addView(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(appbar);
        setVisibilityRemoveButton(false);
    }

    private ToolbarRemoveWithBackView toolbarRemoveWithBackView() {
        ToolbarRemoveWithBackView toolbar = new ToolbarRemoveWithBackView(getActivity());
        toolbar.navigateUp(getActivity());
        toolbar.setOnClickRemove(this);
        toolbar.setTitle(getString(R.string.cart));
        return toolbar;
    }

    private ToolbarRemoveView toolbarRemoveView() {
        ToolbarRemoveView toolbar = new ToolbarRemoveView(getActivity());
        toolbar.setOnClickRemove(this);
        toolbar.setTitle(getString(R.string.cart));
        return toolbar;
    }

    private ArrayList<String> getAppliedPromoCodeList(List<CartItemData> toBeDeletedCartItemDataList) {
        ArrayList<String> appliedPromoList = new ArrayList<>();
        List<CartShopHolderData> cartShopHolderDataList = cartAdapter.getAllShopGroupDataList();
        for (CartShopHolderData cartShopHolderData : cartShopHolderDataList) {
            if (cartShopHolderData.getShopGroupData().getVoucherOrdersItemData() != null &&
                    !TextUtils.isEmpty(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode()) &&
                    !appliedPromoList.contains(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode())) {
                for (CartItemData cartItemData : toBeDeletedCartItemDataList) {
                    if (cartShopHolderData.getShopGroupData().getCartString().equals(cartItemData.getOriginData().getCartString())) {
                        appliedPromoList.add(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode());
                        break;
                    }
                }
            }
        }

        return appliedPromoList;
    }

    @Override
    public void onToolbarRemoveAllCart() {
        sendAnalyticsOnClickRemoveButtonHeader();
        List<CartItemData> toBeDeletedCartItemDataList = cartAdapter.getSelectedCartItemData();
        List<CartItemData> allCartItemDataList = cartAdapter.getAllCartItemData();
        if (toBeDeletedCartItemDataList.size() > 0) {
            final com.tokopedia.design.component.Dialog dialog = getDialogDeleteConfirmation(toBeDeletedCartItemDataList.size());
            dialog.setOnOkClickListener(v -> {
                if (toBeDeletedCartItemDataList.size() > 0) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, toBeDeletedCartItemDataList, getAppliedPromoCodeList(toBeDeletedCartItemDataList), true);
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(
                            dPresenter.generateCartDataAnalytics(
                                    toBeDeletedCartItemDataList, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
                    );
                }
                dialog.dismiss();
            });
            dialog.setOnCancelClickListener(v -> {
                if (toBeDeletedCartItemDataList.size() > 0) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, toBeDeletedCartItemDataList, getAppliedPromoCodeList(toBeDeletedCartItemDataList), false);
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter.generateCartDataAnalytics(
                                    toBeDeletedCartItemDataList, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
                    );
                }
                dialog.dismiss();
            });
            dialog.show();
        } else {
            showToastMessageRed(getString(R.string.message_delete_empty_selection));
        }
    }

    @Override
    protected void setViewListener() {
        btnToShipment.setOnClickListener(getOnClickButtonToShipmentListener(""));
        llHeader.setOnClickListener(getOnClickCheckboxSelectAll());
        cbSelectAll.setOnClickListener(getOnClickCheckboxSelectAll());
    }

    private View.OnClickListener getOnClickCheckboxSelectAll() {
        return v -> {
            boolean checked = !dPresenter.getCartListData().isAllSelected();
            if (checked) {
                sendAnalyticsOnButtonSelectAllChecked();
            } else {
                sendAnalyticsOnButtonSelectAllUnchecked();
            }
            dPresenter.getCartListData().setAllSelected(checked);
            cbSelectAll.setChecked(checked);
            cartAdapter.setAllShopSelected(checked);
            cartAdapter.notifyDataSetChanged();
            dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList());
        };
    }

    @NonNull
    private View.OnClickListener getOnClickButtonToShipmentListener(String message) {
        return view -> {
            if (message == null || message.equals("")) {
                dPresenter.processToUpdateCartData(getSelectedCartDataList(), cartAdapter.getSelectedCartShopHolderData());
            } else {
                showToastMessageRed(message);
                sendAnalyticsOnButtonCheckoutClickedFailed();
                sendAnalyticsOnGoToShipmentFailed(message);
            }
        };
    }

    @Override
    public void sendAnalyticsOnButtonCheckoutClicked() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickSelectCourierOnCart();
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            setHasOptionsMenu(true);
            getActivity().setTitle(getActivity().getString(R.string.title_activity_cart));
            if (savedInstanceState == null) {
                refreshHandler.startRefresh();
            } else {
                if (cartListData != null) {
                    dPresenter.setCartListData(cartListData);
                    renderLoadGetCartDataFinish();
                    renderInitialGetCartListDataSuccess(cartListData);
                    stopCartPerformanceTrace();
                } else {
                    refreshHandler.startRefresh();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (saveInstanceCacheManager != null) {
            saveInstanceCacheManager.onSave(outState);
            saveInstanceCacheManager.put(CartListData.class.getSimpleName(), cartListData);
            if (wishLists != null) {
                saveInstanceCacheManager.put(CartWishlistItemHolderData.class.getSimpleName(), new ArrayList<>(wishLists));
            }
            if (recentViewList != null) {
                saveInstanceCacheManager.put(CartRecentViewItemHolderData.class.getSimpleName(), new ArrayList<>(recentViewList));
            }
            if (recommendationList != null) {
                saveInstanceCacheManager.put(CartRecommendationItemHolderData.class.getSimpleName(), new ArrayList<>(recommendationList));
            }
        }
    }

    @Override
    public void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition) {
        sendAnalyticsOnClickRemoveIconCartItem();
        ArrayList<String> appliedPromoCodes = new ArrayList<>();
        CartShopHolderData cartShopHolderData = cartAdapter.getCartShopHolderDataByIndex(parentPosition);
        if (cartShopHolderData.getShopGroupData().getVoucherOrdersItemData() != null &&
                !TextUtils.isEmpty(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode())) {
            appliedPromoCodes.add(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode());
        }
        ArrayList<CartItemData> cartItemDatas = new ArrayList<>(Collections.singletonList(cartItemHolderData.getCartItemData()));
        List<CartItemData> allCartItemDataList = cartAdapter.getAllCartItemData();
        final com.tokopedia.design.component.Dialog dialog = getDialogDeleteConfirmation(1);
        dialog.setOnOkClickListener(view -> {
            if (cartItemDatas.size() > 0) {
                dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, appliedPromoCodes, true);
                sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(
                        dPresenter.generateCartDataAnalytics(
                                cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }
            dialog.dismiss();
        });
        dialog.setOnCancelClickListener(view -> {
            if (cartItemDatas.size() > 0) {
                dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, appliedPromoCodes, false);
                sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                        dPresenter.generateCartDataAnalytics(
                                cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition) {
        sendAnalyticsOnClickButtonPlusCartItem();
        cartAdapter.increaseQuantity(position, parentPosition);
    }

    @Override
    public void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition) {
        sendAnalyticsOnClickButtonMinusCartItem();
        cartAdapter.decreaseQuantity(position, parentPosition);
    }

    @Override
    public void onCartItemQuantityReseted(int position, int parentPosition, boolean needRefreshItemView) {
        cartAdapter.resetQuantity(position, parentPosition);
    }

    @Override
    public void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition) {
        sendAnalyticsOnClickProductNameCartItem(cartItemHolderData.getCartItemData().getOriginData().getProductName());
        navigateToActivityRequest(getProductIntent(cartItemHolderData.getCartItemData().getOriginData().getProductId()), NAVIGATION_PDP);
    }

    private Intent getProductIntent(String productId) {
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public void onClickShopNow() {
        RouteManager.route(getActivity(), ApplinkConst.HOME);
    }

    @Override
    public void onShowAllItem(@NotNull String appLink) {
        RouteManager.route(getActivity(), appLink);
    }

    @Override
    public void onAddToWishlist(@NotNull String productId) {
        dPresenter.processAddToWishlist(productId, userSession.getUserId(), this);
    }

    @Override
    public void onRemoveFromWishlist(@NotNull String productId) {
        dPresenter.processRemoveFromWishlist(productId, userSession.getUserId(), this);
    }

    @Override
    public void onProductClicked(@NotNull String productId) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.PRODUCT_INFO, productId);
        startActivityForResult(intent, NAVIGATION_PDP);
    }

    @Override
    public void onRecommendationProductClicked(@NotNull String productId) {
        int index = 0, position = 0;
        RecommendationItem recommendationItemClick = null;
        for (CartRecommendationItemHolderData recommendation : recommendationList) {
            if (String.valueOf(recommendation.getRecommendationItem().getProductId()).equalsIgnoreCase(productId)) {
                position = index;
                recommendationItemClick = recommendation.getRecommendationItem();
                break;
            }
            index++;
        }

        if (recommendationItemClick != null) {
            sendAnalyticsOnClickProductRecommendationOnEmptyCart(String.valueOf(position),
                    dPresenter.generateRecommendationDataOnClickAnalytics(recommendationItemClick, FLAG_IS_CART_EMPTY, position));
        }

        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.PRODUCT_INFO, productId);
        startActivityForResult(intent, NAVIGATION_PDP);
    }

    @Override
    public void onButtonAddToCartClicked(@NotNull Object productModel) {
        dPresenter.processAddToCart(productModel);
    }

    @Override
    public void onShowTickerOutOfStock(@NotNull String productId) {
        cartPageAnalytics.eventViewTickerOutOfStock(productId);
    }

    @Override
    public void onSimilarProductUrlClicked(@NotNull String similarProductUrl) {
        RouteManager.route(getContext(), similarProductUrl);
        cartPageAnalytics.eventClickMoreLikeThis();
    }

    @NonNull
    @Override
    public String getDefaultCartErrorMessage() {
        if (isAdded()) {
            return getString(R.string.cart_error_message_no_count);
        } else {
            return "";
        }
    }

    @Override
    public void onCartShopNameClicked(CartShopHolderData cartShopHolderData) {
        sendAnalyticsOnClickShopNameCartItem(cartShopHolderData.getShopGroupData().getShopName());
        navigateToActivity(checkoutModuleRouter.checkoutModuleRouterGetShopInfoIntent(
                cartShopHolderData.getShopGroupData().getShopId()
        ));
    }

    @Override
    public void onShopItemCheckChanged(int itemPosition, boolean checked) {
        dPresenter.setHasPerformChecklistChange();
        cartAdapter.setShopSelected(itemPosition, checked);
        cartAdapter.notifyDataSetChanged();
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList());
        cartAdapter.checkForShipmentForm();
    }

    @Override
    public void onTopAdsItemClicked(Product product) {
        if (getActivity() != null) {
            Intent intent = getProductIntent(product.getId());
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onCartItemRemarkEditChange(
            CartItemData cartItemData, String remark, int position, int parentPosition
    ) {

    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion data, int position) {
        data.setVisible(false);
        cartAdapter.notifyItemChanged(position);
        cartAdapter.checkForShipmentForm();
    }

    @Override
    public void onCartPromoUseVoucherGlobalPromoClicked(PromoStackingData cartPromoGlobal, int position) {
        List<CartItemData> cartItemData = getSelectedCartDataList();
        if (cartItemData != null && cartItemData.size() > 0) {
            trackingPromoCheckoutUtil.cartClickUseTickerPromoOrCoupon();
            dPresenter.processUpdateCartDataPromoStacking(cartItemData, cartPromoGlobal, GO_TO_LIST);
        } else {
            showToastMessageRed(getString(R.string.checkout_module_label_promo_no_item_checked));
        }
    }

    @Override
    public void onVoucherMerchantPromoClicked(Object object) {
        if (object instanceof ShopGroupData) {
            cartPageAnalytics.eventClickPilihMerchantVoucher();
            dPresenter.processUpdateCartDataPromoMerchant(getSelectedCartDataList(), (ShopGroupData) object);
        }
    }

    @Override
    public Promo generateCheckPromoFirstStepParam() {
        List<ShopGroupData> shopGroupDataList = cartListData.getShopGroupDataList();
        PromoStackingData promoStackingGlobalData = cartAdapter.getPromoStackingGlobalData();
        ArrayList<Order> orders = new ArrayList<>();
        for (ShopGroupData shopGroupData : shopGroupDataList) {
            Order order = new Order();
            ArrayList<ProductDetail> productDetails = new ArrayList<>();
            for (CartItemHolderData cartItemHolderData : shopGroupData.getCartItemDataList()) {
                ProductDetail productDetail = new ProductDetail();
                try {
                    productDetail.setProductId(Integer.parseInt(cartItemHolderData.getCartItemData().getOriginData().getProductId()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    productDetail.setProductId(0);
                }
                productDetail.setQuantity(cartItemHolderData.getCartItemData().getUpdatedData().getQuantity());
                productDetails.add(productDetail);
            }
            if (shopGroupData.getVoucherOrdersItemData() != null && !TextUtils.isEmpty(shopGroupData.getVoucherOrdersItemData().getCode())) {
                ArrayList<String> merchantPromoCodes = new ArrayList<>();
                merchantPromoCodes.add(shopGroupData.getVoucherOrdersItemData().getCode());
                if (merchantPromoCodes.size() > 0) {
                    order.setCodes(merchantPromoCodes);
                }
            }
            order.setProductDetails(productDetails);
            order.setUniqueId(shopGroupData.getCartString());
            try {
                order.setShopId(Integer.parseInt(shopGroupData.getShopId()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                order.setShopId(0);
            }
            orders.add(order);
        }
        Promo promo = new Promo();
        promo.setState(Promo.CREATOR.getSTATE_CART());
        promo.setCartType(Promo.CREATOR.getCART_TYPE_DEFAULT());
        if (promoStackingGlobalData != null) {
            ArrayList<String> globalPromoCodes = new ArrayList<>();
            globalPromoCodes.add(promoStackingGlobalData.getPromoCode());
            promo.setCodes(globalPromoCodes);
        }
        promo.setOrders(orders);
        promo.setSkipApply(0);
        promo.setSuggested(0);
        return promo;
    }

    @Override
    public void onCartPromoCancelVoucherPromoGlobalClicked(PromoStackingData cartPromoGlobal, int position) {
        ArrayList<String> promoCodes = new ArrayList<>();
        promoCodes.add(cartPromoGlobal.getPromoCode());
        dPresenter.processCancelAutoApplyPromoStack(SHOP_INDEX_PROMO_GLOBAL, promoCodes, false);
    }

    @Override
    public void onCancelVoucherMerchantClicked(String promoMerchantCode, int shopIndex, boolean ignoreAPIResponse) {
        cartPageAnalytics.eventClickHapusPromoXOnTicker(promoMerchantCode);
        ArrayList<String> promoMerchantCodes = new ArrayList<>();
        promoMerchantCodes.add(promoMerchantCode);
        dPresenter.processCancelAutoApplyPromoStack(shopIndex, promoMerchantCodes, ignoreAPIResponse);
    }

    @Override
    public void onCartPromoGlobalTrackingImpression(PromoStackingData cartPromoGlobal, int position) {
        trackingPromoCheckoutUtil.cartImpressionTicker(cartPromoGlobal.getPromoCodeSafe());
    }

    @Override
    public void onCartPromoGlobalTrackingCancelled(PromoStackingData cartPromoGlobal, int position) {
        sendAnalyticsOnClickCancelPromoCodeAndCouponBanner();
    }

    @Override
    public void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position) {
        List<CartShopHolderData> cartShopHolderDataList = getAllShopDataList();
        List<CartItemData> toBeDeletedCartItem = new ArrayList<>();
        List<CartItemData> allCartItemDataList = cartAdapter.getAllCartItemData();
        ArrayList<String> appliedPromoCodes = new ArrayList<>();

        for (CartShopHolderData cartShopHolderData : cartShopHolderDataList) {
            if (cartShopHolderData.getShopGroupData().isError()) {
                for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                    toBeDeletedCartItem.add(cartItemHolderData.getCartItemData());
                    if (cartShopHolderData.getShopGroupData().getVoucherOrdersItemData() != null &&
                            !TextUtils.isEmpty(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode())) {
                        String promoCode = cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode();
                        if (!appliedPromoCodes.contains(promoCode)) {
                            appliedPromoCodes.add(promoCode);
                        }
                    }
                }
            } else {
                for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                    if (cartItemHolderData.getCartItemData().isError()) {
                        toBeDeletedCartItem.add(cartItemHolderData.getCartItemData());
                        if (cartShopHolderData.getShopGroupData().getVoucherOrdersItemData() != null &&
                                !TextUtils.isEmpty(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode())) {
                            String promoCode = cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode();
                            if (!appliedPromoCodes.contains(promoCode)) {
                                appliedPromoCodes.add(promoCode);
                            }
                        }
                    }
                }
            }
        }

        sendAnalyticsOnClickRemoveCartConstrainedProduct(dPresenter.generateCartDataAnalytics(
                toBeDeletedCartItem, EnhancedECommerceCartMapData.REMOVE_ACTION
        ));
        final com.tokopedia.design.component.Dialog dialog = getDialogDeleteConfirmation(toBeDeletedCartItem.size());
        dialog.setOnOkClickListener(view -> {
            if (toBeDeletedCartItem.size() > 0) {
                dPresenter.processDeleteCartItem(allCartItemDataList, toBeDeletedCartItem, appliedPromoCodes, true);
                sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductWithAddToWishList(
                        dPresenter.generateCartDataAnalytics(
                                toBeDeletedCartItem, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }
            dialog.dismiss();
        });
        dialog.setOnCancelClickListener(view -> {
            if (toBeDeletedCartItem.size() > 0) {
                dPresenter.processDeleteCartItem(allCartItemDataList, toBeDeletedCartItem, appliedPromoCodes, false);
                sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductNoAddToWishList(
                        dPresenter.generateCartDataAnalytics(
                                toBeDeletedCartItem, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onClickDetailPromoGlobal(PromoStackingData dataGlobal, int position) {
        List<CartItemData> cartItemData = getSelectedCartDataList();
        if (cartItemData != null && cartItemData.size() > 0) {
            trackingPromoCheckoutUtil.cartClickUseTickerPromoOrCoupon();
            dPresenter.processUpdateCartDataPromoStacking(cartItemData, dataGlobal, GO_TO_DETAIL);
        }
    }

    @Override
    public void onCartDataEnableToCheckout() {
        if (isAdded() && btnToShipment != null) {
            btnToShipment.setOnClickListener(getOnClickButtonToShipmentListener(""));
        }
    }

    @Override
    public void onCartDataDisableToCheckout(String message) {
        if (isAdded() && btnToShipment != null) {
            btnToShipment.setOnClickListener(getOnClickButtonToShipmentListener(getString(R.string.message_checkout_empty_selection)));
        }
    }

    @Override
    public void onCartItemAfterErrorChecked() {
        cartAdapter.checkForShipmentForm();
    }

    @Override
    public void onCartItemQuantityInputFormClicked(String qty) {
        sendAnalyticsOnClickQuantityCartItemInput(qty);
    }

    @Override
    public void onCartItemLabelInputRemarkClicked() {
        sendAnalyticsOnClickCreateNoteCartItem();
    }

    @Override
    public boolean onCartItemCheckChanged(int position, int parentPosition, boolean checked) {
        dPresenter.setHasPerformChecklistChange();
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList());
        cartAdapter.checkForShipmentForm();
        return cartAdapter.setItemSelected(position, parentPosition, checked);
    }

    @Override
    public void onWishlistCheckChanged(String productId, boolean isChecked) {
        if (getActivity() != null) {
            if (isChecked) {
                dPresenter.processAddToWishlist(productId, userSession.getUserId(), this);
            } else {
                dPresenter.processRemoveFromWishlist(productId, userSession.getUserId(), this);
            }
        }
    }

    @Override
    public void onNeedToRefreshSingleShop(int parentPosition) {
        cartAdapter.notifyItemChanged(parentPosition);
    }

    @Override
    public void onNeedToRefreshMultipleShop() {
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNeedToRecalculate() {
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList());
    }

    @Override
    public void onCartItemShowTickerPriceDecrease(String productId) {
        cartPageAnalytics.eventViewTickerPriceDecrease(productId);
    }

    @Override
    public void onCartItemShowTickerStockDecreaseAndAlreadyAtcByOtherUser(String productId) {
        cartPageAnalytics.eventViewTickerStockDecreaseAndAlreadyAtcByOtherUser(productId);
    }

    @Override
    public void onCartItemShowTickerOutOfStock(String productId) {
        cartPageAnalytics.eventViewTickerOutOfStock(productId);
    }

    @Override
    public void onCartItemSimilarProductUrlClicked(String similarProductUrl) {
        RouteManager.route(getContext(), similarProductUrl);
        cartPageAnalytics.eventClickMoreLikeThis();
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) progressDialog.show();
    }

    @Override
    public void hideProgressLoading() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        if (refreshHandler.isRefreshing()) {
            refreshHandler.finishRefresh();
        }
    }

    @Override
    public void showToastMessage(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
    public void renderInitialGetCartListDataSuccess(CartListData cartListData) {
        if (cartListData != null) {
            endlessRecyclerViewScrollListener.resetState();
            sendAnalyticsScreenName(getScreenName());
            if (refreshHandler != null) {
                refreshHandler.finishRefresh();
            }
            this.cartListData = cartListData;
            cartAdapter.resetData();

            boolean flagAutoApplyStack = false;
            PromoStackingData.Builder builderGlobal = new PromoStackingData.Builder();
            if (cartListData.getAutoApplyStackData() != null && cartListData.getAutoApplyStackData().isSuccess()
                    && !TextUtils.isEmpty(cartListData.getAutoApplyStackData().getCode())) {
                AutoApplyStackData autoApplyStackData = cartListData.getAutoApplyStackData();
                if (autoApplyStackData != null) {
                    if (autoApplyStackData.getMessageSuccess() != null && autoApplyStackData.getCode() != null
                            && autoApplyStackData.getState() != null && autoApplyStackData.getTitleDescription() != null) {
                        builderGlobal.typePromo(autoApplyStackData.getIsCoupon() == PromoStackingData.CREATOR.getVALUE_COUPON() ?
                                PromoStackingData.CREATOR.getTYPE_COUPON() : PromoStackingData.CREATOR.getTYPE_VOUCHER())
                                .description(autoApplyStackData.getMessageSuccess())
                                .amount(autoApplyStackData.getDiscountAmount())
                                .promoCode(autoApplyStackData.getCode())
                                .state(TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(autoApplyStackData.getState()))
                                .title(autoApplyStackData.getTitleDescription())
                                .build();
                        sendAnalyticsOnViewPromoAutoApply();
                        flagAutoApplyStack = true;
                    }
                } else {
                    builderGlobal.state(TickerPromoStackingCheckoutView.State.EMPTY);
                }
            } else {
                builderGlobal.state(TickerPromoStackingCheckoutView.State.EMPTY);
            }

            if (!flagAutoApplyStack) {
                if (cartListData.getGlobalCouponAttr() != null) {
                    if (cartListData.getGlobalCouponAttr().getDescription() != null) {
                        if (!cartListData.getGlobalCouponAttr().getDescription().isEmpty()) {
                            builderGlobal.title(cartListData.getGlobalCouponAttr().getDescription());
                            builderGlobal.titleDefault(cartListData.getGlobalCouponAttr().getDescription());
                        }
                    }

                    if (cartListData.getGlobalCouponAttr().getQuantityLabel() != null) {
                        if (!cartListData.getGlobalCouponAttr().getQuantityLabel().isEmpty()) {
                            builderGlobal.counterLabel(cartListData.getGlobalCouponAttr().getQuantityLabel());
                            builderGlobal.counterLabelDefault(cartListData.getGlobalCouponAttr().getQuantityLabel());
                        }
                    }
                }
            }

            PromoStackingData promoStackingData = builderGlobal.build();

            if (cartListData.getShopGroupDataList().isEmpty()) {
                if (promoStackingData.getState() != TickerPromoStackingCheckoutView.State.EMPTY) {
                    cartAdapter.addPromoStackingVoucherData(promoStackingData);
                }
                cartAdapter.addCartEmptyData();
                cartAdapter.notifyDataSetChanged();
                onCartEmpty();
            } else {
                cartAdapter.removeCartEmptyData();
                cartAdapter.addPromoStackingVoucherData(promoStackingData);

                if (cartListData.getCartPromoSuggestion().isVisible()) {
                    cartAdapter.addPromoSuggestion(cartListData.getCartPromoSuggestion());
                }

                if (cartListData.isError()) {
                    cartAdapter.addCartTickerError(
                            new CartItemTickerErrorHolderData.Builder()
                                    .cartTickerErrorData(cartListData.getCartTickerErrorData())
                                    .build()
                    );
                }

                cartAdapter.addDataList(cartListData.getShopGroupDataList());
                if (cartListData.getAdsModel() != null) {
                    cartAdapter.mappingTopAdsModel(cartListData.getAdsModel());
                }
                dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList());
                if (cbSelectAll != null) {
                    cbSelectAll.setChecked(cartListData.isAllSelected());
                }

                cartAdapter.checkForShipmentForm();
                onCartNotEmpty();
                cartPageAnalytics.eventViewCartListFinishRender();
            }

            if (recentViewList == null) {
                dPresenter.processGetRecentViewData();
            } else {
                renderRecentView(null);
            }

            if (wishLists == null) {
                dPresenter.processGetWishlistData();
            } else {
                renderWishlist(null);
            }

            if (recommendationList == null) {
                dPresenter.processGetRecommendationData(endlessRecyclerViewScrollListener.getCurrentPage());
            } else {
                renderRecommendation(null);
            }

            if (toolbar != null) {
                setVisibilityRemoveButton(!cartListData.getShopGroupDataList().isEmpty());
            } else {
                if (getActivity() != null && !mIsMenuVisible && !cartListData.getShopGroupDataList().isEmpty()) {
                    mIsMenuVisible = true;
                    getActivity().invalidateOptionsMenu();
                }
            }

            CartTickerData tickerData = cartListData.getTicker();
            if (tickerData != null) {
                if (cartTicker != null) {
                    cartTicker.setTickerType(Ticker.TYPE_ANNOUNCEMENT);
                    cartTicker.setTickerShape(Ticker.SHAPE_FULL);
                    cartTicker.setCloseButtonVisibility(View.GONE);
                    cartTicker.setHtmlDescription(tickerData.getMessage());
                    cartTicker.setVisibility(View.VISIBLE);

                    // Ticker Wrap_Content workaround - remove TickerViewPager
                    View view = cartTicker.findViewById(com.tokopedia.unifycomponents.R.id.ticker_content_multiple);
                    if (view != null) {
                        view.setVisibility(View.GONE);
                    }

                    cartTicker.requestLayout();
                    cartPageAnalytics.eventViewInformationAndWarningTickerInCart(String.valueOf(tickerData.getId()));
                }
            } else if (cartTicker != null) {
                cartTicker.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void stopCartPerformanceTrace() {
        if (cartPerformanceMonitoring != null && !isTraceCartStopped) {
            cartPerformanceMonitoring.stopTrace();
            isTraceCartStopped = true;
        }
    }

    @Override
    public void stopAllCartPerformanceTrace() {
        if (cartAllPerformanceMonitoring != null && !isTraceCartAllStopped && hasTriedToLoadRecentViewList && hasTriedToLoadWishList && hasTriedToLoadRecommendation) {
            cartAllPerformanceMonitoring.stopTrace();
            isTraceCartAllStopped = true;
        }
    }

    private void showErrorLayout(String message) {
        if (getActivity() != null) {
            enableSwipeRefresh();
            mIsMenuVisible = false;
            getActivity().invalidateOptionsMenu();
            refreshHandler.finishRefresh();
            showErrorContainer();
            onContentAvailabilityChanged(false);
            NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                    () -> {
                        llNetworkErrorView.setVisibility(View.GONE);
                        rlContent.setVisibility(View.VISIBLE);
                        refreshHandler.setRefreshing(true);
                        cartAdapter.resetData();
                        dPresenter.processInitialGetCartData(getCartId(), dPresenter.getCartListData() == null, false);
                    });
        }
    }

    public void showMainContainerLoadingInitData() {
        llNetworkErrorView.setVisibility(View.GONE);
        rlContent.setVisibility(View.VISIBLE);
        cardFooter.setVisibility(View.GONE);
        cardHeader.setVisibility(View.GONE);
    }

    public void showMainContainer() {
        llNetworkErrorView.setVisibility(View.GONE);
        rlContent.setVisibility(View.VISIBLE);
        cardFooter.setVisibility(View.VISIBLE);
        cardHeader.setVisibility(View.VISIBLE);
    }

    public void showErrorContainer() {
        rlContent.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        cardFooter.setVisibility(View.GONE);
        cardHeader.setVisibility(View.GONE);
    }

    public void showEmptyCartContainer() {
        llNetworkErrorView.setVisibility(View.GONE);
        cardFooter.setVisibility(View.GONE);
        cardHeader.setVisibility(View.GONE);
        onContentAvailabilityChanged(false);
    }

    private void showSnackbarRetry(String message) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, ()
                -> dPresenter.processInitialGetCartData(getCartId(), dPresenter.getCartListData() == null, false))
                .showRetrySnackbar();
    }

    @Override
    public void renderErrorInitialGetCartListData(String message) {
        if (cartAdapter.getItemCount() > 0) {
            showSnackbarRetry(message);
        } else {
            showErrorLayout(message);
        }
    }

    @Override
    public void renderToShipmentFormSuccess(Map<String, Object> eeCheckoutData,
                                            boolean checkoutProductEligibleForCashOnDelivery,
                                            int checklistCondition) {
        switch (checklistCondition) {
            case CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES:
                if (checkoutProductEligibleForCashOnDelivery) {
                    sendAnalyticsOnSuccessToCheckoutDefaultEligibleCod(eeCheckoutData);
                } else {
                    sendAnalyticsOnSuccessToCheckoutDefault(eeCheckoutData);
                }
                break;
            case CartListPresenter.ITEM_CHECKED_ALL_WITH_CHANGES:
                if (checkoutProductEligibleForCashOnDelivery) {
                    sendAnalyticsOnSuccessToCheckoutCheckAllEligibleCod(eeCheckoutData);
                } else {
                    sendAnalyticsOnSuccessToCheckoutCheckAll(eeCheckoutData);
                }
                break;
            case CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP:
                if (checkoutProductEligibleForCashOnDelivery) {
                    sendAnalyticsOnSuccessToCheckoutPartialShopEligibleCod(eeCheckoutData);
                } else {
                    sendAnalyticsOnSuccessToCheckoutPartialShop(eeCheckoutData);
                }
                break;
            case CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM:
                if (checkoutProductEligibleForCashOnDelivery) {
                    sendAnalyticsOnSuccessToCheckoutPartialProductEligibleCod(eeCheckoutData);
                } else {
                    sendAnalyticsOnSuccessToCheckoutPartialProduct(eeCheckoutData);
                }
                break;
            case CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM:
                if (checkoutProductEligibleForCashOnDelivery) {
                    sendAnalyticsOnSuccessToCheckoutPartialShopAndProductEligibleCod(eeCheckoutData);
                } else {
                    sendAnalyticsOnSuccessToCheckoutPartialShopAndProduct(eeCheckoutData);
                }
                break;
        }
        renderToAddressChoice();
    }

    @Override
    public void renderToAddressChoice() {
        FLAG_BEGIN_SHIPMENT_PROCESS = true;
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = true;
        boolean isAutoApplyPromoStackCodeApplied = dPresenter.getCartListData() != null &&
                dPresenter.getCartListData().getAutoApplyStackData() != null &&
                dPresenter.getCartListData().getAutoApplyStackData().isSuccess();
        Intent intent = ShipmentActivity.createInstance(getActivity(), cartAdapter.getPromoStackingGlobalData(),
                cartListData.getCartPromoSuggestion(), cartListData.getDefaultPromoDialogTab(),
                isAutoApplyPromoStackCodeApplied
        );
        startActivityForResult(intent, ShipmentActivity.REQUEST_CODE);
    }

    private void clearRecyclerView() {
        cartAdapter.unsubscribeSubscription();
        cartRecyclerView.setAdapter(null);
        cartAdapter = new CartAdapter(null, null, null);
        cartRecyclerView.removeAllViews();
        cartRecyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void renderErrorToShipmentForm(String message) {
        sendAnalyticsOnButtonCheckoutClickedFailed();
        sendAnalyticsOnGoToShipmentFailed(message);
        showToastMessageRed(message);
    }

    @Override
    public void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData) {
        PromoStackingData promoStackingData = new PromoStackingData.Builder()
                .typePromo(PromoStackingData.CREATOR.getTYPE_VOUCHER())
                .promoCode(promoCodeCartListData.getDataVoucher().getCode())
                .description(promoCodeCartListData.getDataVoucher().getMessageSuccess())
                .amount(promoCodeCartListData.getDataVoucher().getCashbackAmount())
                .state(TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(promoCodeCartListData.getDataVoucher().getState()))
                .title(promoCodeCartListData.getDataVoucher().getTitleDescription())
                .build();
        cartAdapter.updateItemPromoStackVoucher(promoStackingData);
    }

    private void onCartEmpty() {
        FLAG_IS_CART_EMPTY = true;
        enableSwipeRefresh();
        sendAnalyticsOnDataCartIsEmpty();
        checkoutModuleRouter.checkoutModuleRouterResetBadgeCart();
        showEmptyCartContainer();
        notifyBottomCartParent();
        if (cartRecyclerView.getItemDecorationCount() > 0) {
            cartRecyclerView.removeItemDecoration(cartItemDecoration);
        }
        if (getActivity() != null) {
            llCartContainer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        }
    }

    private void onCartNotEmpty() {
        FLAG_IS_CART_EMPTY = false;
        if (cartRecyclerView.getItemDecorationCount() == 0) {
            cartRecyclerView.addItemDecoration(cartItemDecoration);
        }
        if (getActivity() != null) {
            llCartContainer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.checkout_module_color_background));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            refreshHandler.setRefreshing(true);
            if (dPresenter.getCartListData() == null) {
                dPresenter.processInitialGetCartData(getCartId(), true, false);
            } else {
                if (dPresenter.dataHasChanged()) {
                    dPresenter.processToUpdateAndReloadCartData();
                } else {
                    dPresenter.processInitialGetCartData(getCartId(), false, true);
                }
            }
        }
    }

    @Override
    public void disableSwipeRefresh() {
        refreshHandler.setPullEnabled(false);
    }

    @Override
    public void enableSwipeRefresh() {
        refreshHandler.setPullEnabled(true);
    }

    @Override
    public List<CartItemData> getSelectedCartDataList() {
        return cartAdapter.getSelectedCartItemData();
    }

    @Override
    public List<CartItemData> getAllCartDataList() {
        return cartAdapter.getAllCartItemData();
    }

    @Override
    public List<CartShopHolderData> getAllShopDataList() {
        return cartAdapter.getAllShopGroupDataList();
    }

    @Override
    public void renderDetailInfoSubTotal(String qty, String subtotalPrice, boolean selectAllCartItem) {
        if (dPresenter.getCartListData() != null) {
            dPresenter.getCartListData().setAllSelected(selectAllCartItem);
        }
        cbSelectAll.setChecked(selectAllCartItem);
        tvTotalPrice.setText(subtotalPrice);
        btnToShipment.setText(String.format(getString(R.string.cart_item_button_checkout_count_format), qty));
    }

    @Override
    public void updateCashback(double cashback) {
        cartAdapter.updateShipmentSellerCashback(cashback);
    }

    @Override
    public void goToCouponList() {
        List<CartItemData> cartItemDataList = getSelectedCartDataList();
        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();
        for (CartItemData data : cartItemDataList) {
            updateCartRequestList.add(new UpdateCartRequest.Builder()
                    .cartId(data.getOriginData().getCartId())
                    .notes(data.getUpdatedData().getRemark())
                    .quantity(data.getUpdatedData().getQuantity())
                    .build());
        }

        Promo promo = generateCheckPromoFirstStepParam();

        startActivityForResult(
                checkoutModuleRouter
                        .checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartListIntent(
                                cartListData.isPromoCouponActive(),
                                new Gson().toJson(updateCartRequestList), TrackingPromoCheckoutConstantKt.getFROM_CART(),
                                "", promo
                        ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
        );
    }

    @Override
    public void goToDetailPromoStacking(PromoStackingData promoStackingData) {
        Promo promo = generateCheckPromoFirstStepParam();

        if (promoStackingData.getTypePromo() == PromoStackingData.CREATOR.getTYPE_COUPON()) {
            startActivityForResult(checkoutModuleRouter.getPromoCheckoutDetailIntentWithCode(promoStackingData.getPromoCodeSafe(),
                    cartListData.isPromoCouponActive(), false, TrackingPromoCheckoutConstantKt.getFROM_CART(), promo), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
        } else {
            startActivityForResult(checkoutModuleRouter.getPromoCheckoutListIntentWithCode(promoStackingData.getPromoCodeSafe(),
                    cartListData.isPromoCouponActive(), false, TrackingPromoCheckoutConstantKt.getFROM_CART(), promo), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void showToastMessageRed(String message) {
        if (TextUtils.isEmpty(message)) {
            message = "Terjadi kesalahan. Ulangi beberapa saat lagi";
        }
        View view = getView();
        if (view != null) {
            NetworkErrorHelper.showRedCloseSnackbar(view, message);
        } else if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showToastMessageGreen(String message) {
        View view = getView();
        if (view != null) {
            NetworkErrorHelper.showGreenCloseSnackbar(view, message);
        } else if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void renderLoadGetCartData() {
        showMainContainerLoadingInitData();
        onContentAvailabilityChanged(false);
    }

    @Override
    public void renderLoadGetCartDataFinish() {
        if (refreshHandler.isRefreshing()) {
            refreshHandler.setRefreshing(false);
        }
        showMainContainer();
        onContentAvailabilityChanged(true);
    }

    @Override
    public void onDeleteCartDataSuccess(List<Integer> deletedCartIds) {
        cartAdapter.removeCartItemById(deletedCartIds, getContext());
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList());
        notifyBottomCartParent();
    }

    @NonNull
    private com.tokopedia.design.component.Dialog getDialogDeleteConfirmation(int count) {
        final com.tokopedia.design.component.Dialog dialog =
                new com.tokopedia.design.component.Dialog(getActivity(),
                        com.tokopedia.design.component.Dialog.Type.LONG_PROMINANCE);
        dialog.setTitle(getString(R.string.label_dialog_title_delete_item));
        dialog.setDesc(String.format(getString(R.string.label_dialog_message_remove_cart_multiple_item),
                String.valueOf(count)));
        dialog.setBtnOk(getString(R.string.label_dialog_action_delete_and_add_to_wishlist));
        dialog.setBtnCancel(getString(R.string.label_dialog_action_delete));
        dialog.getAlertDialog().setCancelable(true);
        dialog.getAlertDialog().setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        navigateToActivityRequest(getProductIntent(product.getId()), NAVIGATION_PDP);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        navigateToActivity(checkoutModuleRouter.checkoutModuleRouterGetShopInfoIntent(shop.getId()));
    }

    @Override
    public void onAddFavorite(int position, Data shopData) {
    }

    @Override
    public void onRefresh(View view) {
        if (dPresenter.dataHasChanged()) {
            showMainContainer();
            dPresenter.processToUpdateAndReloadCartData();
        } else {
            if (dPresenter.getCartListData() != null && dPresenter.getCartListData().getShopGroupDataList().size() > 0) {
                showMainContainer();
            }
            dPresenter.processInitialGetCartData(getCartId(), cartListData == null, true);
            String promo = checkoutModuleRouter.checkoutModuleRouterGetAutoApplyCouponBranchUtil();
            if (!TextUtils.isEmpty(promo)) {
                dPresenter.processCheckPromoCodeFromSuggestedPromo(promo, true);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE:
                onResultFromRequestCodeLoyalty(resultCode, data);
                break;
            case ShipmentActivity.REQUEST_CODE:
                onResultFromRequestCodeCartShipment(resultCode, data);
                break;
            case NAVIGATION_PDP:
                refreshHandler.setRefreshing(true);
                dPresenter.processInitialGetCartData(getCartId(), cartListData == null, true);
        }
    }

    private void onResultFromRequestCodeCartShipment(int resultCode, Intent data) {
        if (cartRecyclerView.getAdapter() == null) {
            cartAdapter = new CartAdapter(this, this, this);
            cartRecyclerView.setAdapter(cartAdapter);
        }
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = false;
        if (resultCode == TopPayActivity.PAYMENT_CANCELLED) {
            showToastMessageRed(getString(R.string.alert_payment_canceled_or_failed_transaction_module));
            dPresenter.processResetAndRefreshCartData();
        } else if (resultCode == TopPayActivity.PAYMENT_SUCCESS) {
            showToastMessageGreen(getString(R.string.message_payment_success));
            checkoutModuleRouter.checkoutModuleRouterResetBadgeCart();
            refreshHandler.setRefreshing(true);
            dPresenter.processInitialGetCartData(getCartId(), false, false);
        } else if (resultCode == TopPayActivity.PAYMENT_FAILED) {
            showToastMessage(getString(R.string.default_request_error_unknown));
            sendAnalyticsScreenName(getScreenName());
            refreshHandler.setRefreshing(true);
            if (cartListData != null) {
                renderInitialGetCartListDataSuccess(cartListData);
            } else {
                dPresenter.processInitialGetCartData(getCartId(), false, false);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            sendAnalyticsScreenName(getScreenName());
            refreshHandler.setRefreshing(true);
            if (cartListData != null) {
                renderInitialGetCartListDataSuccess(cartListData);
            } else {
                dPresenter.processInitialGetCartData(getCartId(), false, false);
            }
        } else if (resultCode == ShipmentActivity.RESULT_CODE_COUPON_STATE_CHANGED) {
            refreshHandler.setRefreshing(true);
            dPresenter.processInitialGetCartData(getCartId(), false, false);
        }
    }

    private void onResultFromRequestCodeLoyalty(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            sendAnalyticsScreenName(getScreenName());
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                PromoStackingData promoStackingData = bundle.getParcelable(TickerCheckoutUtilKt.getEXTRA_PROMO_DATA());

                if (cartListData != null && promoStackingData != null) {
                    if (cartListData.getGlobalCouponAttr() != null) {
                        if (cartListData.getGlobalCouponAttr().getDescription() != null) {
                            if (!cartListData.getGlobalCouponAttr().getDescription().isEmpty()) {
                                promoStackingData.setTitleDefault(cartListData.getGlobalCouponAttr().getDescription());
                            }
                        }

                        if (cartListData.getGlobalCouponAttr().getQuantityLabel() != null) {
                            if (!cartListData.getGlobalCouponAttr().getQuantityLabel().isEmpty()) {
                                promoStackingData.setCounterLabelDefault(cartListData.getGlobalCouponAttr().getQuantityLabel());
                            }
                        }
                    }
                }

                for (CartItemData cartItemData : cartAdapter.getAllCartItemData()) {
                    if (promoStackingData != null && promoStackingData.getTrackingDetailUiModels().size() > 0) {
                        for (TrackingDetailUiModel trackingDetailUiModel : promoStackingData.getTrackingDetailUiModels()) {
                            if (String.valueOf(trackingDetailUiModel.getProductId()).equalsIgnoreCase(cartItemData.getOriginData().getProductId())) {
                                cartItemData.getOriginData().setPromoCodes(trackingDetailUiModel.getPromoCodesTracking());
                                cartItemData.getOriginData().setPromoDetails(trackingDetailUiModel.getPromoDetailsTracking());
                            }
                        }
                    }
                }

                if (promoStackingData != null) {
                    cartAdapter.updateItemPromoStackVoucher(promoStackingData);
                    if (promoStackingData.getTypePromo() == PromoStackingData.CREATOR.getTYPE_VOUCHER()) {
                        sendAnalyticsOnViewPromoManualApply("voucher");
                    } else {
                        sendAnalyticsOnViewPromoManualApply("coupon");
                    }
                }
            }
        } else if (resultCode == TickerCheckoutUtilKt.getRESULT_CLASHING()) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                ClashingInfoDetailUiModel clashingInfoDetailUiModel = bundle.getParcelable(TickerCheckoutUtilKt.getEXTRA_CLASHING_DATA());
                if (clashingInfoDetailUiModel != null) {
                    String type = bundle.getString(TickerCheckoutUtilKt.getEXTRA_TYPE());
                    if (type == null) type = "";
                    onClashCheckPromo(clashingInfoDetailUiModel, type);
                }
            }
        }
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        showToastMessageRed(errorMessage);
        cartAdapter.notifyByProductId(productId, false);

        cartAdapter.notifyWishlist(productId, false);
        cartAdapter.notifyRecentView(productId, false);
    }

    @Override
    public void onSuccessAddWishlist(String productId) {
        showToastMessageGreen(getString(R.string.toast_message_add_wishlist_success));
        cartAdapter.notifyByProductId(productId, true);
        cartAdapter.notifyWishlist(productId, true);
        cartAdapter.notifyRecentView(productId, true);
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        showToastMessageRed(errorMessage);
        cartAdapter.notifyByProductId(productId, true);
        cartAdapter.notifyWishlist(productId, true);
        cartAdapter.notifyRecentView(productId, true);
    }

    @Override
    public void onSuccessRemoveWishlist(String productId) {
        showToastMessageGreen(getString(R.string.toast_message_remove_wishlist_success));
        cartAdapter.notifyByProductId(productId, false);
        cartAdapter.notifyWishlist(productId, false);
        cartAdapter.notifyRecentView(productId, false);
    }

    @Override
    public void sendAnalyticsOnSuccessToCheckoutDefault(Map<String, Object> eeData) {
        cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessDefault(eeData);
    }

    @Override
    public void sendAnalyticsOnSuccessToCheckoutCheckAll(Map<String, Object> eeData) {
        cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessCheckAll(eeData);
    }

    @Override
    public void sendAnalyticsOnSuccessToCheckoutPartialShop(Map<String, Object> eeData) {
        cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShop(eeData);
    }

    @Override
    public void sendAnalyticsOnSuccessToCheckoutPartialProduct(Map<String, Object> eeData) {
        cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(eeData);
    }

    @Override
    public void sendAnalyticsOnSuccessToCheckoutPartialShopAndProduct(Map<String, Object> eeData) {
        cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(eeData);
    }

    @Override
    public void sendAnalyticsOnSuccessToCheckoutDefaultEligibleCod(Map<String, Object> eeData) {
        cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessDefaultEligibleCod(eeData);
    }

    @Override
    public void sendAnalyticsOnSuccessToCheckoutCheckAllEligibleCod(Map<String, Object> eeData) {
        cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessCheckAllEligibleCod(eeData);
    }

    @Override
    public void sendAnalyticsOnSuccessToCheckoutPartialShopEligibleCod(Map<String, Object> eeData) {
        cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShopEligibleCod(eeData);
    }

    @Override
    public void sendAnalyticsOnSuccessToCheckoutPartialProductEligibleCod(Map<String, Object> eeData) {
        cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialProductEligibleCod(eeData);
    }

    @Override
    public void sendAnalyticsOnSuccessToCheckoutPartialShopAndProductEligibleCod(Map<String, Object> eeData) {
        cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProductEligibleCod(eeData);
    }

    @Override
    public void sendAnalyticsOnClickBackArrow() {
        cartPageAnalytics.eventClickAtcCartClickArrowBack();
    }

    @Override
    public void sendAnalyticsOnClickRemoveButtonHeader() {
        cartPageAnalytics.eventClickAtcCartClickHapusOnTopRightCorner();
    }

    @Override
    public void sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusDanTambahWishlistFromTrashBin(eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromTrashBin(eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnClickRemoveIconCartItem() {
        cartPageAnalytics.eventClickAtcCartClickTrashBin();
    }

    @Override
    public void sendAnalyticsOnClickButtonPlusCartItem() {
        cartPageAnalytics.eventClickAtcCartClickButtonPlus();
    }

    @Override
    public void sendAnalyticsOnClickButtonMinusCartItem() {
        cartPageAnalytics.eventClickAtcCartClickButtonMinus();
    }

    @Override
    public void sendAnalyticsOnClickProductNameCartItem(String productName) {
        cartPageAnalytics.eventClickAtcCartClickProductName(productName);
    }

    @Override
    public void sendAnalyticsOnClickShopNameCartItem(String shopName) {
        cartPageAnalytics.eventClickAtcCartClickShopName(shopName);
    }

    @Override
    public void sendAnalyticsOnClickUsePromoCodeAndCoupon() {
        cartPageAnalytics.eventClickAtcCartClickGunakanKodePromoAatauKupon();
    }

    @Override
    public void sendAnalyticsOnClickCancelPromoCodeAndCouponBanner() {
        cartPageAnalytics.eventClickAtcCartClickXOnBannerPromoCode();
    }

    @Override
    public void sendAnalyticsOnClickRemoveCartConstrainedProduct(Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusProdukBerkendala(eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductWithAddToWishList(Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusDanTambahWishlistFromHapusProdukBerkendala(eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductNoAddToWishList(Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromHapusProdukBerkendala(eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnClickQuantityCartItemInput(String quantity) {
        cartPageAnalytics.eventClickAtcCartClickInputQuantity(quantity);
    }

    @Override
    public void sendAnalyticsOnClickCreateNoteCartItem() {
        cartPageAnalytics.eventClickAtcCartClickTulisCatatan();
    }

    @Override
    public void sendAnalyticsOnDataCartIsEmpty() {
        cartPageAnalytics.eventViewAtcCartImpressionCartEmpty();
    }

    @Override
    public void sendAnalyticsOnClickShoppingNowCartEmptyState() {
        cartPageAnalytics.eventClickAtcCartClickBelanjaSekarangOnEmptyCart();
    }

    @Override
    public void sendAnalyticsOnClickAddFromWishListCartEmptyState() {
        cartPageAnalytics.eventClickAtcCartClickAddFromWishlistOnEmptyCart();
    }

    @Override
    public void sendAnalyticsScreenName(String screenName) {
        cartPageAnalytics.sendScreenName(getActivity(), screenName);
    }

    @Override
    protected String getScreenName() {
        return ConstantTransactionAnalytics.ScreenName.CART;
    }

    @Override
    public void sendAnalyticsOnButtonCheckoutClickedFailed() {
        cartPageAnalytics.eventClickCheckoutCartClickCheckoutFailed();
    }

    @Override
    public void sendAnalyticsOnGoToShipmentFailed(String errorMessage) {
        cartPageAnalytics.eventViewErrorWhenCheckout(errorMessage);
    }

    @Override
    public void sendAnalyticsOnButtonSelectAllChecked() {
        cartPageAnalytics.eventClickCheckoutCartClickPilihSemuaProdukChecklist();
    }

    @Override
    public void sendAnalyticsOnButtonSelectAllUnchecked() {
        cartPageAnalytics.eventClickCheckoutCartClickPilihSemuaProdukUnChecklist();
    }

    @Override
    public void sendAnalyticsOnViewPromoManualApply(String type) {
        cartPageAnalytics.eventViewPromoManualApply(type);
    }

    @Override
    public void sendAnalyticsOnViewProductRecommendationOnCart(Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedEcommerceViewRecommendationOnCart(eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnClickProductRecommendationOnEmptyCart(String position, Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedEcommerceClickProductRecommendationOnEmptyCart(position, eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnViewPromoAutoApply() {
        cartPageAnalytics.eventViewPromoAutoApply();
    }

    @Override
    public void notifyBottomCartParent() {
        if (getActivity() instanceof CartNotifyListener) {
            ((CartNotifyListener) getActivity()).onNotifyCart();
        }
    }

    @Override
    public void showMerchantVoucherListBottomsheet(ShopGroupData shopGroupData) {
        Promo promo = generateCheckPromoFirstStepParam();
        if (getFragmentManager() != null) {
            int shopId = 0;
            try {
                shopId = Integer.parseInt(shopGroupData.getShopId());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            MerchantVoucherListBottomSheetFragment merchantVoucherListBottomSheetFragment =
                    MerchantVoucherListBottomSheetFragment.newInstance(shopId, shopGroupData.getCartString(), promo, "cart");
            merchantVoucherListBottomSheetFragment.setActionListener(this);
            merchantVoucherListBottomSheetFragment.show(getFragmentManager(), "");
        }
    }

    @Override
    public void onClashCheckPromo(@NonNull ClashingInfoDetailUiModel clashingInfoDetailUiModel, @NotNull String type) {
        ClashBottomSheetFragment clashBottomSheetFragment = ClashBottomSheetFragment.newInstance();
        clashBottomSheetFragment.setData(clashingInfoDetailUiModel);
        clashBottomSheetFragment.setActionListener(this);
        clashBottomSheetFragment.setAnalyticsCart(cartPageAnalytics);
        clashBottomSheetFragment.setSource("cart");
        clashBottomSheetFragment.setType(type);
        clashBottomSheetFragment.show(getFragmentManager(), "");
    }

    @Override
    public void onSuccessCheckPromoMerchantFirstStep(@NotNull ResponseGetPromoStackUiModel promoData, @NotNull String promoCode) {
        onSuccessCheckPromoFirstStep(promoData);
    }

    @Override
    public void onSuccessCheckPromoFirstStep(@NonNull ResponseGetPromoStackUiModel responseGetPromoStackUiModel) {
        // Update global promo state
        if (responseGetPromoStackUiModel.getData().getCodes().size() > 0) {
            PromoStackingData promoStackingGlobalData = cartAdapter.getPromoStackingGlobalData();
            int typePromo;
            if (responseGetPromoStackUiModel.getData().isCoupon() == PromoStackingData.CREATOR.getVALUE_COUPON()) {
                typePromo = PromoStackingData.CREATOR.getTYPE_COUPON();
            } else {
                typePromo = PromoStackingData.CREATOR.getTYPE_VOUCHER();
            }
            promoStackingGlobalData.setTypePromo(typePromo);
            promoStackingGlobalData.setPromoCode(responseGetPromoStackUiModel.getData().getCodes().get(0));
            promoStackingGlobalData.setDescription(responseGetPromoStackUiModel.getData().getMessage().getText());
            promoStackingGlobalData.setState(TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(responseGetPromoStackUiModel.getData().getMessage().getState()));
            promoStackingGlobalData.setTitle(responseGetPromoStackUiModel.getData().getTitleDescription());
            promoStackingGlobalData.setAmount(responseGetPromoStackUiModel.getData().getCashbackWalletAmount());
            promoStackingGlobalData.setVariant(TickerPromoStackingCheckoutView.Variant.GLOBAL);
        }

        // Update merchant voucher state
        List<CartShopHolderData> cartShopHolderDataList = cartAdapter.getAllShopGroupDataList();
        if (cartShopHolderDataList != null) {
            for (CartShopHolderData cartShopHolderData : cartShopHolderDataList) {
                for (VoucherOrdersItemUiModel voucherOrdersItemUiModel : responseGetPromoStackUiModel.getData().getVoucherOrders()) {
                    if (voucherOrdersItemUiModel.getUniqueId().equals(cartShopHolderData.getShopGroupData().getCartString())) {
                        VoucherOrdersItemData voucherOrdersItemData = cartShopHolderData.getShopGroupData().getVoucherOrdersItemData();
                        if (voucherOrdersItemData == null) {
                            voucherOrdersItemData = new VoucherOrdersItemData();
                        }
                        voucherOrdersItemData.setCode(voucherOrdersItemUiModel.getCode());
                        voucherOrdersItemData.setSuccess(voucherOrdersItemUiModel.getSuccess());
                        voucherOrdersItemData.setUniqueId(voucherOrdersItemUiModel.getUniqueId());
                        voucherOrdersItemData.setCartId(voucherOrdersItemUiModel.getCartId());
                        voucherOrdersItemData.setType(voucherOrdersItemUiModel.getType());
                        voucherOrdersItemData.setCashbackWalletAmount(voucherOrdersItemUiModel.getCashbackWalletAmount());
                        voucherOrdersItemData.setDiscountAmount(voucherOrdersItemUiModel.getDiscountAmount());
                        voucherOrdersItemData.setInvoiceDescription(voucherOrdersItemUiModel.getInvoiceDescription());

                        MessageData messageData = new MessageData();
                        messageData.setColor(voucherOrdersItemUiModel.getMessage().getColor());
                        messageData.setState(voucherOrdersItemUiModel.getMessage().getState());
                        messageData.setText(voucherOrdersItemUiModel.getMessage().getText());

                        voucherOrdersItemData.setMessageData(messageData);

                        cartShopHolderData.getShopGroupData().setVoucherOrdersItemData(voucherOrdersItemData);
                        break;
                    }
                }
                if (responseGetPromoStackUiModel.getData().getTrackingDetailUiModel().size() > 0) {
                    for (TrackingDetailUiModel trackingDetailUiModel : responseGetPromoStackUiModel.getData().getTrackingDetailUiModel()) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                            if (String.valueOf(trackingDetailUiModel.getProductId()).equalsIgnoreCase(cartItemHolderData.getCartItemData().getOriginData().getProductId())) {
                                cartItemHolderData.getCartItemData().getOriginData().setPromoCodes(trackingDetailUiModel.getPromoCodesTracking());
                                cartItemHolderData.getCartItemData().getOriginData().setPromoDetails(trackingDetailUiModel.getPromoDetailsTracking());
                            }
                        }
                    }
                }

            }
        }

        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessClearPromoStack(int shopIndex) {
        if (shopIndex == SHOP_INDEX_PROMO_GLOBAL) {
            if (cartListData.getShopGroupDataList().isEmpty()) {
                cartAdapter.removePromoStackingVoucherData();
            } else {
                PromoStackingData promoStackingData = cartAdapter.getPromoStackingGlobalData();
                promoStackingData.setState(TickerPromoStackingCheckoutView.State.EMPTY);
                promoStackingData.setAmount(0);
                promoStackingData.setPromoCode("");
                promoStackingData.setDescription("");
                promoStackingData.setTitle(promoStackingData.getTitleDefault());
                promoStackingData.setCounterLabel(promoStackingData.getCounterLabelDefault());
                cartAdapter.updateItemPromoStackVoucher(promoStackingData);
            }
        } else {
            CartShopHolderData cartShopHolderData = cartAdapter.getCartShopHolderDataByIndex(shopIndex);
            if (cartShopHolderData != null) {
                cartShopHolderData.getShopGroupData().setVoucherOrdersItemData(null);
                cartAdapter.notifyItemChanged(shopIndex);
            }
        }
    }

    @Override
    public void onSuccessClearPromoStackAfterClash() {
        // Reset global promo
        PromoStackingData promoStackingData = cartAdapter.getPromoStackingGlobalData();
        promoStackingData.setState(TickerPromoStackingCheckoutView.State.EMPTY);
        promoStackingData.setAmount(0);
        promoStackingData.setPromoCode("");
        promoStackingData.setDescription("");
        promoStackingData.setTitle(promoStackingData.getTitleDefault());
        promoStackingData.setCounterLabel(promoStackingData.getCounterLabelDefault());

        // Reset merchant promo
        List<CartShopHolderData> cartShopHolderDataList = cartAdapter.getAllCartShopHolderData();
        for (CartShopHolderData cartShopHolderData : cartShopHolderDataList) {
            if (cartShopHolderData != null) {
                cartShopHolderData.getShopGroupData().setVoucherOrdersItemData(null);
            }
        }

        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailedClearPromoStack(boolean ignoreAPIResponse) {
        if (!ignoreAPIResponse) {
            ToasterError.make(getView(), "Terjadi kesalahan. Ulangi beberapa saat lagi", ToasterError.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSubmitNewPromoAfterClash(@NotNull ArrayList<String> oldPromoList, @NotNull ArrayList<ClashingVoucherOrderUiModel> newPromoList, @NotNull String type) {
        dPresenter.processCancelAutoApplyPromoStackAfterClash(oldPromoList, newPromoList, type);
    }

    // get newly added cart id if open cart after ATC on PDP
    @Override
    public String getCartId() {
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString(CartActivity.EXTRA_CART_ID))) {
            return getArguments().getString(CartActivity.EXTRA_CART_ID);
        }
        return "0";
    }

    @Override
    public PromoStackingData getPromoStackingGlobalData() {
        return cartAdapter.getPromoStackingGlobalData();
    }

    @Override
    public void renderRecentView(@Nullable List<RecentView> recentViewList) {
        List<CartRecentViewItemHolderData> cartRecentViewItemHolderDataList = new ArrayList<>();
        if (this.recentViewList != null) {
            cartRecentViewItemHolderDataList.addAll(this.recentViewList);
        } else if (recentViewList != null) {
            for (RecentView recentView : recentViewList) {
                CartRecentViewItemHolderData cartRecentViewItemHolderData = new CartRecentViewItemHolderData();
                cartRecentViewItemHolderData.setId(recentView.getProductId());
                cartRecentViewItemHolderData.setName(recentView.getProductName());
                cartRecentViewItemHolderData.setPrice(recentView.getProductPrice());
                cartRecentViewItemHolderData.setImageUrl(recentView.getProductImage());
                cartRecentViewItemHolderData.setWishlist(recentView.isWishlist());
                cartRecentViewItemHolderData.setRating(recentView.getProductRating());
                cartRecentViewItemHolderData.setReviewCount(recentView.getProductReviewCount());
                cartRecentViewItemHolderData.setShopLocation(recentView.getShopLocation());
                cartRecentViewItemHolderData.setShopId(recentView.getShopId());
                cartRecentViewItemHolderData.setShopName(recentView.getShopName());
                cartRecentViewItemHolderData.setMinOrder(1);
                if (recentView.getBadges().size() > 0) {
                    cartRecentViewItemHolderData.setBadgeUrl(recentView.getBadges().get(0).getImageUrl());
                    if (recentView.getBadges().get(0).getTitle().equalsIgnoreCase("Official Store")) {
                        cartRecentViewItemHolderData.setShopType("official_store");
                    } else if (recentView.getBadges().get(0).getTitle().equalsIgnoreCase("Power Badge")) {
                        cartRecentViewItemHolderData.setShopType("power_badge");
                    }
                }
                cartRecentViewItemHolderDataList.add(cartRecentViewItemHolderData);
            }
        }
        CartSectionHeaderHolderData cartSectionHeaderHolderData = new CartSectionHeaderHolderData();
        cartSectionHeaderHolderData.setTitle(getString(R.string.checkout_module_title_recent_view));

        CartRecentViewHolderData cartRecentViewHolderData = new CartRecentViewHolderData();
        cartRecentViewHolderData.setRecentViewList(cartRecentViewItemHolderDataList);
        cartAdapter.addCartRecentViewData(cartSectionHeaderHolderData, cartRecentViewHolderData);
        this.recentViewList = cartRecentViewItemHolderDataList;
    }

    @Override
    public void renderWishlist(@Nullable List<Wishlist> wishlist) {
        List<CartWishlistItemHolderData> cartWishlistItemHolderDataList = new ArrayList<>();
        if (this.wishLists != null) {
            cartWishlistItemHolderDataList.addAll(this.wishLists);
        } else if (wishlist != null) {
            for (Wishlist item : wishlist) {
                CartWishlistItemHolderData cartWishlistItemHolderData = new CartWishlistItemHolderData();
                cartWishlistItemHolderData.setId(item.getId());
                cartWishlistItemHolderData.setName(item.getName());
                cartWishlistItemHolderData.setRawPrice(String.valueOf(item.getPrice()));
                cartWishlistItemHolderData.setPrice(item.getPriceFmt());
                cartWishlistItemHolderData.setImageUrl(item.getImageUrl());
                cartWishlistItemHolderData.setUrl(item.getUrl());
                cartWishlistItemHolderData.setWishlist(true);
                cartWishlistItemHolderData.setRating(item.rating);
                cartWishlistItemHolderData.setReviewCount(item.reviewCount);
                cartWishlistItemHolderData.setMinOrder(item.getMinimumOrder());
                cartWishlistItemHolderData.setCategory(item.getCategoryBreadcrumb());
                if (item.getShop() != null) {
                    cartWishlistItemHolderData.setShopId(item.getShop().getId());
                    cartWishlistItemHolderData.setShopName(item.getShop().getName());
                    String shopType = "";
                    if (item.getShop().isOfficial()) {
                        shopType = "official_store";
                    } else if (item.getShop().isGoldMerchant()) {
                        shopType = "gold_merchant";
                    }
                    cartWishlistItemHolderData.setShopType(shopType);
                    cartWishlistItemHolderData.setShopLocation(item.getShop().getLocation());
                }
                if (item.getBadges().size() > 0) {
                    cartWishlistItemHolderData.setBadgeUrl(item.getBadges().get(0).getImageUrl());
                }
                cartWishlistItemHolderDataList.add(cartWishlistItemHolderData);
            }
        }
        CartSectionHeaderHolderData cartSectionHeaderHolderData = new CartSectionHeaderHolderData();
        cartSectionHeaderHolderData.setTitle(getString(R.string.checkout_module_title_wishlist));
        cartSectionHeaderHolderData.setShowAllAppLink(ApplinkConst.WISHLIST);

        CartWishlistHolderData cartRecentViewHolderData = new CartWishlistHolderData();
        cartRecentViewHolderData.setWishList(cartWishlistItemHolderDataList);
        cartAdapter.addCartWishlistData(cartSectionHeaderHolderData, cartRecentViewHolderData);
        this.wishLists = cartWishlistItemHolderDataList;
    }

    @Override
    public void renderRecommendation(@Nullable List<RecommendationItem> recommendationItems) {
        List<CartRecommendationItemHolderData> cartRecommendationItemHolderDataList = new ArrayList<>();

        if (recommendationItems != null) {
            // Render from API
            for (RecommendationItem recommendationItem : recommendationItems) {
                CartRecommendationItemHolderData cartRecommendationItemHolderData =
                        new CartRecommendationItemHolderData(recommendationItem);
                cartRecommendationItemHolderDataList.add(cartRecommendationItemHolderData);
            }
        } else {
            // Render from Cache
            if (recommendationList != null && recommendationList.size() != 0) {
                cartRecommendationItemHolderDataList.addAll(this.recommendationList);
            }
        }

        CartSectionHeaderHolderData cartSectionHeaderHolderData = null;
        if (endlessRecyclerViewScrollListener.getCurrentPage() == 0) {
            cartSectionHeaderHolderData = new CartSectionHeaderHolderData();
            cartSectionHeaderHolderData.setTitle(getString(R.string.checkout_module_title_recommendation));
        }

        if (cartRecommendationItemHolderDataList.size() > 0) {
            cartAdapter.addCartRecommendationData(cartSectionHeaderHolderData, cartRecommendationItemHolderDataList);
            recommendationList = cartRecommendationItemHolderDataList;

            sendAnalyticsOnViewProductRecommendationOnCart(
                    dPresenter.generateRecommendationDataAnalytics(recommendationList, FLAG_IS_CART_EMPTY)
            );
        }
    }

    @Override
    public void showItemLoading() {
        cartAdapter.addCartLoadingData();
    }

    @Override
    public void hideItemLoading() {
        cartAdapter.removeCartLoadingData();
        endlessRecyclerViewScrollListener.updateStateAfterGetData();
        hasLoadRecommendation = true;
    }

    @Override
    public void triggerSendEnhancedEcommerceAddToCartSuccess(AddToCartDataModel addToCartDataResponseModel, Object productModel) {
        Map<String, Object> stringObjectMap = null;
        String eventCategory = "";
        String eventAction = "";
        String eventLabel = "";
        if (productModel instanceof CartWishlistItemHolderData) {
            eventCategory = ConstantTransactionAnalytics.EventCategory.WISHLIST_PAGE;
            eventAction = ConstantTransactionAnalytics.EventAction.CLICK_BELI_ON_WISHLIST;
            eventLabel = "";
            stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer((CartWishlistItemHolderData) productModel, addToCartDataResponseModel);
        } else if (productModel instanceof CartRecentViewItemHolderData) {
            eventCategory = ConstantTransactionAnalytics.EventCategory.RECENT_VIEW;
            eventAction = ConstantTransactionAnalytics.EventAction.CLICK_BELI_ON_RECENT_VIEW_PAGE;
            eventLabel = "";
            stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer((CartRecentViewItemHolderData) productModel, addToCartDataResponseModel);
        } else if (productModel instanceof CartRecommendationItemHolderData) {
            eventCategory = ConstantTransactionAnalytics.EventCategory.CART;
            eventAction = ConstantTransactionAnalytics.EventAction.CLICK_ADD_TO_CART;
            eventLabel = "";
            stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer((CartRecommendationItemHolderData) productModel, addToCartDataResponseModel, FLAG_IS_CART_EMPTY);
        }

        if (stringObjectMap != null) {
            checkoutAnalyticsCourierSelection.sendEnhancedECommerceAddToCart(stringObjectMap, eventCategory, eventAction, eventLabel);
        }
    }

    @Override
    public void setHasTriedToLoadRecentView() {
        hasTriedToLoadRecentViewList = true;
    }

    @Override
    public void setHasTriedToLoadWishList() {
        hasTriedToLoadWishList = true;
    }

    @Override
    public void setHasTriedToLoadRecommendation() {
        hasTriedToLoadRecommendation = true;
    }
}
