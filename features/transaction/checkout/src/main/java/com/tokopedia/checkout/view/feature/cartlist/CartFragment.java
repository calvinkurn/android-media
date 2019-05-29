package com.tokopedia.checkout.view.feature.cartlist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartTickerErrorData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ShopGroupData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.promostacking.AutoApplyStackData;
import com.tokopedia.checkout.domain.datamodel.promostacking.MessageData;
import com.tokopedia.checkout.domain.datamodel.promostacking.VoucherOrdersItemData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.datamodel.voucher.promostacking.ResponseFirstStep;
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
import com.tokopedia.checkout.view.feature.addressoptions.CartAddressChoiceActivity;
import com.tokopedia.checkout.view.feature.bottomsheetpromostacking.ClashBottomSheetFragment;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartItemAdapter;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentActivity;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.merchantvoucher.voucherlistbottomsheet.MerchantVoucherListBottomSheetFragment;
import com.tokopedia.navigation_common.listener.CartNotifyListener;
import com.tokopedia.navigation_common.listener.EmptyCartListener;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutConstantKt;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.data.entity.request.Order;
import com.tokopedia.promocheckout.common.data.entity.request.ProductDetail;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel;
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCartMapData;
import com.tokopedia.transactiondata.entity.request.UpdateCartRequest;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public static final int GO_TO_DETAIL = 2;
    public static final int GO_TO_LIST = 1;
    private boolean FLAG_BEGIN_SHIPMENT_PROCESS = false;

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
    private LinearLayout emptyCartContainer;

    private ProgressDialog progressDialog;

    @Inject
    ICartListPresenter dPresenter;
    @Inject
    CartAdapter cartAdapter;
    @Inject
    RecyclerView.ItemDecoration cartItemDecoration;
    @Inject
    CheckoutAnalyticsCart cartPageAnalytics;
    @Inject
    CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection;
    @Inject
    ICheckoutModuleRouter checkoutModuleRouter;
    @Inject
    Context context;
    @Inject
    TrackingPromoCheckoutUtil trackingPromoCheckoutUtil;

    private RefreshHandler refreshHandler;
    private UserSessionInterface userSession;

    private boolean mIsMenuVisible = false;
    private boolean isToolbarWithBackButton = true;
    private EmptyCartListener emptyCartListener;

    private CartListData cartListData;

    private PerformanceMonitoring performanceMonitoring;
    private boolean isTraceStopped;

    private SaveInstanceCacheManager saveInstanceCacheManager;

    public static CartFragment newInstance(Bundle bundle, String args) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(CartFragment.class.getSimpleName(), args);
        CartFragment fragment = new CartFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setEmptyCartListener(EmptyCartListener emptyCartListener) {
        this.emptyCartListener = emptyCartListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        userSession = new UserSession(getActivity());
        performanceMonitoring = PerformanceMonitoring.start(CART_TRACE);

        if (getActivity() != null) {
            saveInstanceCacheManager = new SaveInstanceCacheManager(getActivity(), savedInstanceState);
        }

        if (savedInstanceState != null && saveInstanceCacheManager != null) {
            cartListData = saveInstanceCacheManager.get(CartListData.class.getSimpleName(), CartListData.class);
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

            cartListData = arguments.getParcelable(EmptyCartListener.ARG_CART_LIST_DATA);
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
        return R.layout.fragment_cart_new;
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
        emptyCartContainer = view.findViewById(R.id.container_empty_cart);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        progressDialog.setCancelable(false);

        refreshHandler = new RefreshHandler(getActivity(), view, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                    dPresenter.processDeleteAndRefreshCart(allCartItemDataList, toBeDeletedCartItemDataList, getAppliedPromoCodeList(toBeDeletedCartItemDataList), true);
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
                    dPresenter.processDeleteAndRefreshCart(allCartItemDataList, toBeDeletedCartItemDataList, getAppliedPromoCodeList(toBeDeletedCartItemDataList), false);
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
                dPresenter.processToUpdateCartData(getSelectedCartDataList());
            } else {
                showToastMessageRed(message);
                sendAnalyticsOnButtonCheckoutClickedFailed();
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
                cartListData = saveInstanceCacheManager.get(CartListData.class.getSimpleName(), CartListData.class);
                renderInitialGetCartListDataSuccess(cartListData);
                stopTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (saveInstanceCacheManager != null) {
            saveInstanceCacheManager.onSave(outState);
            saveInstanceCacheManager.put(CartListData.class.getSimpleName(), cartListData);
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
                dPresenter.processDeleteAndRefreshCart(allCartItemDataList, cartItemDatas, appliedPromoCodes, true);
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
                dPresenter.processDeleteAndRefreshCart(allCartItemDataList, cartItemDatas, appliedPromoCodes, false);
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
        navigateToActivity(getProductIntent(cartItemHolderData.getCartItemData().getOriginData().getProductId()));
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
        PromoStackingData promoStackingGlobalData = cartAdapter.getPromoStackingGlobaldata();
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
                dPresenter.processDeleteAndRefreshCart(allCartItemDataList, toBeDeletedCartItem, appliedPromoCodes, true);
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
                dPresenter.processDeleteAndRefreshCart(allCartItemDataList, toBeDeletedCartItem, appliedPromoCodes, false);
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
        dPresenter.processUpdateCartDataPromoStacking(getSelectedCartDataList(), dataGlobal, GO_TO_DETAIL);
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
                renderEmptyCartData();
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
            }

            if (toolbar != null) {
                setVisibilityRemoveButton(!cartListData.getShopGroupDataList().isEmpty());
            } else {
                if (getActivity() != null && !mIsMenuVisible && !cartListData.getShopGroupDataList().isEmpty()) {
                    mIsMenuVisible = true;
                    getActivity().invalidateOptionsMenu();
                }
            }

            cartPageAnalytics.eventViewCartListFinishRender();
        }
    }

    @Override
    public void stopTrace() {
        if (!isTraceStopped) {
            performanceMonitoring.stopTrace();
            isTraceStopped = true;
        }
    }

    @Override
    public void renderAppliedPromoStacking() {

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
                        dPresenter.processInitialGetCartData(dPresenter.getCartListData() == null);
                    });
        }
    }

    public void showMainContainerLoadingInitData() {
        emptyCartContainer.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.GONE);
        rlContent.setVisibility(View.VISIBLE);
        cardFooter.setVisibility(View.GONE);
        cardHeader.setVisibility(View.GONE);
    }

    public void showMainContainer() {
        emptyCartContainer.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.GONE);
        rlContent.setVisibility(View.VISIBLE);
        cardFooter.setVisibility(View.VISIBLE);
        cardHeader.setVisibility(View.VISIBLE);
    }

    public void showErrorContainer() {
        rlContent.setVisibility(View.GONE);
        emptyCartContainer.setVisibility(View.GONE);
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
                -> dPresenter.processInitialGetCartData(dPresenter.getCartListData() == null))
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
    public void renderActionDeleteCartDataSuccess(CartItemData cartItemData, String message, boolean addWishList) {

    }

    @Override
    public void renderNoRecipientAddressShipmentForm(CartShipmentAddressFormData shipmentAddressFormData) {
        Intent intent;
        if (shipmentAddressFormData.getKeroDiscomToken() != null &&
                shipmentAddressFormData.getKeroUnixTime() != 0) {
            Token token = new Token();
            token.setUt(shipmentAddressFormData.getKeroUnixTime());
            token.setDistrictRecommendation(shipmentAddressFormData.getKeroDiscomToken());

            intent = CartAddressChoiceActivity.createInstance(getActivity(),
                    CartAddressChoiceActivity.TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS, token);
        } else {
            intent = CartAddressChoiceActivity.createInstance(getActivity(),
                    CartAddressChoiceActivity.TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS);
        }

        startActivityForResult(intent, CartAddressChoiceActivity.REQUEST_CODE);
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
        boolean isAutoApplyPromoStackCodeApplied = dPresenter.getCartListData() != null &&
                dPresenter.getCartListData().getAutoApplyStackData() != null &&
                dPresenter.getCartListData().getAutoApplyStackData().isSuccess();
        Intent intent = ShipmentActivity.createInstance(getActivity(), cartAdapter.getPromoStackingGlobaldata(),
                cartListData.getCartPromoSuggestion(), cartListData.getDefaultPromoDialogTab(),
                isAutoApplyPromoStackCodeApplied
        );
        startActivityForResult(intent, ShipmentActivity.REQUEST_CODE);
    }

    @Override
    public void renderErrorToShipmentForm(String message) {
        sendAnalyticsOnButtonCheckoutClickedFailed();
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

    @Override
    public void renderCheckPromoStackingCodeFromSuggestedPromoSuccess(ResponseFirstStep responseFirstStep) {
        PromoStackingData promoStackingData = new PromoStackingData.Builder()
                .typePromo(PromoStackingData.CREATOR.getTYPE_VOUCHER())
                .promoCode(responseFirstStep.getData().getInvoiceDescription())
                .description(responseFirstStep.getData().getMessage().getText())
                .amount(responseFirstStep.getData().getCashbackWalletAmount())
                .state(TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(responseFirstStep.getData().getMessage().getState()))
                .title(responseFirstStep.getData().getTitleDescription())
                .build();
        cartAdapter.updateItemPromoStackVoucher(promoStackingData);
    }

    private void renderEmptyCartData() {
        enableSwipeRefresh();
        sendAnalyticsOnDataCartIsEmpty();
        refreshHandler.finishRefresh();
        mIsMenuVisible = false;
        if (getActivity() != null) getActivity().invalidateOptionsMenu();
        checkoutModuleRouter.checkoutModuleRouterResetBadgeCart();
        showEmptyCartContainer();
        notifyBottomCartParent();

/*
        try {
            if (emptyCartListener != null) {
                emptyCartListener.onCartEmpty(
                        cartListData.getAutoApplyStackData().getMessageSuccess(),
                        cartListData.getAutoApplyStackData().getState(),
                        cartListData.getAutoApplyStackData().getTitleDescription(),
                        cartListData.getAutoApplyStackData().getCode());
            } else {
                if (getActivity() instanceof EmptyCartListener) {
                    ((EmptyCartListener) getActivity()).onCartEmpty(
                            cartListData.getAutoApplyStackData().getMessageSuccess(),
                            cartListData.getAutoApplyStackData().getState(),
                            cartListData.getAutoApplyStackData().getTitleDescription(),
                            cartListData.getAutoApplyStackData().getCode());
                }
            }
            showEmptyCartContainer();
            notifyBottomCartParent();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
*/

        // Todo : Insert empty cart placeholder to adapter
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            refreshHandler.setRefreshing(true);
            if (dPresenter.getCartListData() == null) {
                if (getArguments() == null || getArguments().getParcelable(EmptyCartListener.ARG_CART_LIST_DATA) == null) {
                    dPresenter.processInitialGetCartData(true);
                }
            } else {
                if (dPresenter.dataHasChanged()) {
                    dPresenter.processToUpdateAndReloadCartData();
                } else {
                    if (getArguments() == null || getArguments().getParcelable(EmptyCartListener.ARG_CART_LIST_DATA) == null) {
                        dPresenter.processInitialGetCartData(false);
                    } else {
                        CartListData cartListData = getArguments().getParcelable(EmptyCartListener.ARG_CART_LIST_DATA);
                        dPresenter.setCartListData(cartListData);
                        renderLoadGetCartDataFinish();
                        renderInitialGetCartListDataSuccess(cartListData);
                        stopTrace();
                    }
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

    @Deprecated
    @Override
    public void renderPromoVoucher() {
        PromoData promoData = new PromoData.Builder()
                .state(TickerCheckoutView.State.EMPTY)
                .build();
        cartAdapter.addPromoVoucherData(promoData);
    }

    @Override
    public void renderPromoGlobalVoucher() {
        PromoStackingData promoStackingData = new PromoStackingData.Builder()
                .state(TickerPromoStackingCheckoutView.State.EMPTY)
                .build();
        cartAdapter.addPromoStackingVoucherData(promoStackingData);
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
    public void goToDetail(PromoData promoData) {
        Promo promo = generateCheckPromoFirstStepParam();

        if (promoData.getTypePromo() == PromoData.CREATOR.getTYPE_COUPON()) {
            startActivityForResult(checkoutModuleRouter.getPromoCheckoutDetailIntentWithCode(promoData.getPromoCodeSafe(),
                    cartListData.isPromoCouponActive(), false, TrackingPromoCheckoutConstantKt.getFROM_CART(), promo), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
        } else {
            startActivityForResult(checkoutModuleRouter.getPromoCheckoutListIntentWithCode(promoData.getPromoCodeSafe(),
                    cartListData.isPromoCouponActive(), false, TrackingPromoCheckoutConstantKt.getFROM_CART(), promo), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
        }
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
        cartAdapter.resetData();
        showMainContainer();
        onContentAvailabilityChanged(true);
    }

    @Override
    public void renderCartTickerError(CartTickerErrorData cartTickerErrorData) {
        cartAdapter.addCartTickerError(new CartItemTickerErrorHolderData.Builder()
                .cartTickerErrorData(cartTickerErrorData)
                .build());
    }

    @Override
    public void renderCancelAutoApplyCouponSuccess() {
        if (layoutUsedPromoEmptyCart != null) {
            layoutUsedPromoEmptyCart.setVisibility(View.GONE);
        } else {
            cartAdapter.cancelAutoApplyCoupon();
            cartAdapter.checkForShipmentForm();
        }
    }

    @Override
    public void renderCancelAutoApplyCouponStackSuccess(int position) {
        if (layoutUsedPromoEmptyCart != null) {
            layoutUsedPromoEmptyCart.setVisibility(View.GONE);
        } else {
            cartAdapter.cancelAutoApplyStackCoupon();
            cartAdapter.checkForShipmentForm();
        }
    }

    @Override
    public void renderCancelAutoApplyCouponError() {
        if (getActivity() != null) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onDeleteCartDataSuccess() {
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
        navigateToActivity(getProductIntent(product.getId()));
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
            dPresenter.processInitialGetCartData(dPresenter.getCartListData() == null);
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
        }
    }

    private void onResultFromRequestCodeCartShipment(int resultCode, Intent data) {
        if (resultCode == TopPayActivity.PAYMENT_CANCELLED) {
            showToastMessageRed(getString(R.string.alert_payment_canceled_or_failed_transaction_module));
            dPresenter.processResetAndRefreshCartData();
        } else if (resultCode == TopPayActivity.PAYMENT_SUCCESS) {
            showToastMessageGreen(getString(R.string.message_payment_success));
            checkoutModuleRouter.checkoutModuleRouterResetBadgeCart();
            refreshHandler.setRefreshing(true);
            dPresenter.processInitialGetCartData(false);
        } else if (resultCode == TopPayActivity.PAYMENT_FAILED) {
            showToastMessage(getString(R.string.default_request_error_unknown));
            sendAnalyticsScreenName(getScreenName());
        } else if (resultCode == Activity.RESULT_CANCELED) {
            sendAnalyticsScreenName(getScreenName());
        } else if (resultCode == ShipmentActivity.RESULT_CODE_COUPON_STATE_CHANGED) {
            refreshHandler.setRefreshing(true);
            dPresenter.processInitialGetCartData(false);
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
        hideProgressLoading();
        showToastMessageRed(errorMessage);
        cartAdapter.notifyByProductId(productId, false);
    }

    @Override
    public void onSuccessAddWishlist(String productId) {
        hideProgressLoading();
        showToastMessageGreen(getString(R.string.toast_message_add_wishlist_success));
        cartAdapter.notifyByProductId(productId, true);
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        hideProgressLoading();
        showToastMessageRed(errorMessage);
        cartAdapter.notifyByProductId(productId, true);
    }

    @Override
    public void onSuccessRemoveWishlist(String productId) {
        hideProgressLoading();
        showToastMessageGreen(getString(R.string.toast_message_remove_wishlist_success));
        cartAdapter.notifyByProductId(productId, false);
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
    public void sendAnalyticsOnViewPromoAutoApply() {
        cartPageAnalytics.eventViewPromoAutoApply();
    }

    private void notifyBottomCartParent() {
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
    public void onSuccessCheckPromoFirstStep(@NonNull ResponseGetPromoStackUiModel responseGetPromoStackUiModel) {
        // Update global promo state
        if (responseGetPromoStackUiModel.getData().getCodes().size() > 0) {
            PromoStackingData promoStackingGlobalData = cartAdapter.getPromoStackingGlobaldata();
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
            }
        }

        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessClearPromoStack(int shopIndex) {
        if (shopIndex == SHOP_INDEX_PROMO_GLOBAL) {
            PromoStackingData promoStackingData = cartAdapter.getPromoStackingGlobaldata();
            promoStackingData.setState(TickerPromoStackingCheckoutView.State.EMPTY);
            promoStackingData.setAmount(0);
            promoStackingData.setPromoCode("");
            promoStackingData.setDescription("");
            promoStackingData.setTitle(promoStackingData.getTitleDefault());
            promoStackingData.setCounterLabel(promoStackingData.getCounterLabelDefault());
            cartAdapter.updateItemPromoStackVoucher(promoStackingData);
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
        PromoStackingData promoStackingData = cartAdapter.getPromoStackingGlobaldata();
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
}
