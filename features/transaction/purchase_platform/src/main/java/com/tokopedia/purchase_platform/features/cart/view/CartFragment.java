package com.tokopedia.purchase_platform.features.cart.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.chuck.Chuck;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
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
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.checkout.view.common.TickerAnnouncementActionListener;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.TickerAnnouncementHolderData;
import com.tokopedia.common.payment.PaymentConstant;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.dialog.DialogUnify;
import com.tokopedia.merchantvoucher.common.gql.data.request.CartItemDataVoucher;
import com.tokopedia.merchantvoucher.voucherlistbottomsheet.MerchantVoucherListBottomSheetFragment;
import com.tokopedia.navigation_common.listener.CartNotifyListener;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutConstantKt;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.data.ConstantKt;
import com.tokopedia.promocheckout.common.data.entity.request.Order;
import com.tokopedia.promocheckout.common.data.entity.request.ProductDetail;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.TrackingDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData;
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductApplicationDetails;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartDigitalProduct;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartResponse;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartShopItems;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartShops;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.AutoApplyStackData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.MessageData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData;
import com.tokopedia.purchase_platform.common.feature.promo_clashing.ClashBottomSheetFragment;
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoActionListener;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.TickerData;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest;
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.RecentView;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupWithErrorData;
import com.tokopedia.purchase_platform.features.cart.domain.model.voucher.PromoCodeCartListData;
import com.tokopedia.purchase_platform.features.cart.view.adapter.CartAdapter;
import com.tokopedia.purchase_platform.features.cart.view.adapter.CartItemAdapter;
import com.tokopedia.purchase_platform.features.cart.view.compoundview.ToolbarRemoveView;
import com.tokopedia.purchase_platform.features.cart.view.compoundview.ToolbarRemoveWithBackView;
import com.tokopedia.purchase_platform.features.cart.view.di.DaggerNewCartComponent;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartRecommendationViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemTickerErrorHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecommendationItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartSectionHeaderHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistItemHolderData;
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentActivity;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IndexedValue;

import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_INSURANCE_RECOMMENDATION;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartFragment extends BaseCheckoutFragment implements ActionListener,
        CartItemAdapter.ActionListener, ICartListView, PromoActionListener,
        RefreshHandler.OnRefreshHandlerListener, ICartListAnalyticsListener, WishListActionListener,
        ToolbarRemoveView.ToolbarCartListener, MerchantVoucherListBottomSheetFragment.ActionListener,
        ClashBottomSheetFragment.ActionListener, InsuranceItemActionListener, TickerAnnouncementActionListener {

    public static final int SHOP_INDEX_PROMO_GLOBAL = -1;

    private static final int HAS_ELEVATION = 8;
    private static final int NO_ELEVATION = 0;
    private static final String CART_TRACE = "mp_cart";
    private static final String CART_ALL_TRACE = "mp_cart_all";
    private static final String CART_PAGE = "cart";
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
    private CheckBox cbSelectAll;
    private Typography btnRemove;
    private CardView cardHeader;
    private CardView cardFooter;
    private LinearLayout llNetworkErrorView;
    private LinearLayout llCartContainer;

    private ProgressDialog progressDialog;

    @Inject
    ICartListPresenter dPresenter;
    @Inject
    RecyclerView.ItemDecoration cartItemDecoration;
    @Inject
    CheckoutAnalyticsCart cartPageAnalytics;
    @Inject
    TrackingPromoCheckoutUtil trackingPromoCheckoutUtil;
    @Inject
    CartTrackingDataGenerator cartTrackingDataGenerator;
    @Inject
    ViewHolderDataMapper viewHolderDataMapper;

    private CartAdapter cartAdapter;
    private RefreshHandler refreshHandler;
    private UserSessionInterface userSession;

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
    private CartSectionHeaderHolderData recommendationSectionHeader;
    private WishListActionListener recommendationWishlistActionListener;
    private boolean hasTriedToLoadWishList;
    private boolean hasTriedToLoadRecentViewList;
    private boolean hasTriedToLoadRecommendation;
    private boolean isInsuranceEnabled = false;

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
            recommendationSectionHeader = saveInstanceCacheManager.get(CartSectionHeaderHolderData.class.getSimpleName(),
                    (new TypeToken<CartSectionHeaderHolderData>() {
                    }).getType(), null);
        } else {
            cartPerformanceMonitoring = PerformanceMonitoring.start(CART_TRACE);
            cartAllPerformanceMonitoring = PerformanceMonitoring.start(CART_ALL_TRACE);
        }

        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getContext());
        isInsuranceEnabled = remoteConfig.getBoolean(APP_ENABLE_INSURANCE_RECOMMENDATION, false);

        dPresenter.attachView(this);
    }

    @Override
    public void onStop() {
        boolean hasChanges = dPresenter.dataHasChanged();

        try {
            Activity activity = getActivity();
            List<CartItemData> cartItemDataList = getSelectedCartDataList();
            if (hasChanges && activity != null && cartItemDataList != null
                    && cartItemDataList.size() > 0 && !FLAG_BEGIN_SHIPMENT_PROCESS) {
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
        if (getActivity() != null) {
            BaseMainApplication baseMainApplication = (BaseMainApplication) getActivity().getApplication();
            DaggerNewCartComponent.builder()
                    .baseAppComponent(baseMainApplication.getBaseAppComponent())
                    .build()
                    .inject(this);
        }
        cartAdapter = new CartAdapter(this, this, this, this, this);
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
        cbSelectAll = view.findViewById(R.id.cb_select_all);
        btnRemove = view.findViewById(R.id.btn_delete_all_cart);
        llCartContainer = view.findViewById(R.id.ll_cart_container);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        progressDialog.setCancelable(false);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            // Remove default cardview margin on Kitkat or lower
            int pixel = Utils.convertDpToPixel(-6, getContext());
            ((ViewGroup.MarginLayoutParams) cardHeader.getLayoutParams()).setMargins(pixel, pixel, pixel, pixel);
        }

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
                    dPresenter.processGetRecommendationData(endlessRecyclerViewScrollListener.getCurrentPage(), cartAdapter.getAllCartItemProductId());
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
        toolbar.setOnClickGoToChuck(this);
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
            if (cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData() != null &&
                    !TextUtils.isEmpty(cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData().getCode()) &&
                    !appliedPromoList.contains(cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData().getCode())) {
                for (CartItemData cartItemData : toBeDeletedCartItemDataList) {
                    if (cartShopHolderData.getShopGroupAvailableData().getCartString().equals(cartItemData.getOriginData().getCartString())) {
                        appliedPromoList.add(cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData().getCode());
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
            final DialogUnify dialog = getMultipleItemsDialogDeleteConfirmation(toBeDeletedCartItemDataList.size());
            dialog.setPrimaryCTAClickListener(() -> {
                if (toBeDeletedCartItemDataList.size() > 0) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, toBeDeletedCartItemDataList, getAppliedPromoCodeList(toBeDeletedCartItemDataList), false, true);
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter.generateCartDataAnalytics(
                                    toBeDeletedCartItemDataList, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
                    );
                }
                dialog.dismiss();
                return Unit.INSTANCE;
            });
            dialog.setSecondaryCTAClickListener(() -> {
                dialog.dismiss();
                return Unit.INSTANCE;
            });
            dialog.show();
        } else {
            showToastMessageRed(getString(R.string.message_delete_empty_selection));
        }
    }

    @Override
    public void onGoToChuck() {
        startActivity(Chuck.getLaunchIntent(getActivity()));
    }

    @Override
    protected void setViewListener() {
        btnToShipment.setOnClickListener(getOnClickButtonToShipmentListener(""));
        cbSelectAll.setOnClickListener(getOnClickCheckboxSelectAll());
        btnRemove.setOnClickListener(v -> {
            if (btnRemove.getVisibility() == View.VISIBLE) {
                onToolbarRemoveAllCart();
            }
        });
    }

    private View.OnClickListener getOnClickCheckboxSelectAll() {
        return v -> onSelectAllClicked();
    }

    @NonNull
    private View.OnClickListener getOnClickButtonToShipmentListener(String message) {
        return view -> {
            if (message == null || message.equals("")) {
                ArrayList<InsuranceCartDigitalProduct> insuranceCartShopsArrayList = cartAdapter.isInsuranceCartProductUnSelected();

                if (!insuranceCartShopsArrayList.isEmpty()) {
                    deleteMacroInsurance(insuranceCartShopsArrayList, false);
                }

                dPresenter.processToUpdateCartData(getSelectedCartDataList());
            } else {
                showToastMessageRed(message);
                sendAnalyticsOnButtonCheckoutClickedFailed();
                sendAnalyticsOnGoToShipmentFailed(message);
            }
        };
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
            if (recommendationSectionHeader != null) {
                saveInstanceCacheManager.put(CartSectionHeaderHolderData.class.getSimpleName(), recommendationSectionHeader);
            }
        }
    }

    @Override
    public void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition) {
        sendAnalyticsOnClickRemoveIconCartItem();
        ArrayList<String> appliedPromoCodes = new ArrayList<>();
        CartShopHolderData cartShopHolderData = cartAdapter.getCartShopHolderDataByIndex(parentPosition);
        if (cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData() != null &&
                !TextUtils.isEmpty(cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData().getCode())) {
            appliedPromoCodes.add(cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData().getCode());
        }
        ArrayList<CartItemData> cartItemDatas = new ArrayList<>(Collections.singletonList(cartItemHolderData.getCartItemData()));
        List<CartItemData> allCartItemDataList = cartAdapter.getAllCartItemData();

        final DialogUnify dialog;

        boolean macroInsurancePresent = !cartAdapter.getInsuranceCartShops().isEmpty();
        boolean removeAllItem = allCartItemDataList.size() == cartItemDatas.size();
        boolean removeMacroInsurance = macroInsurancePresent && removeAllItem;

        if (removeMacroInsurance) {
            dialog = getInsuranceDialogDeleteConfirmation();
            dialog.setPrimaryCTAClickListener(() -> {
                if (cartItemDatas.size() > 0) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, appliedPromoCodes, true, removeMacroInsurance);
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(
                            dPresenter.generateCartDataAnalytics(
                                    cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
                    );
                }
                dialog.dismiss();
                return Unit.INSTANCE;
            });
            dialog.setSecondaryCTAClickListener(() -> {
                if (cartItemDatas.size() > 0) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, appliedPromoCodes, false, removeMacroInsurance);
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter.generateCartDataAnalytics(
                                    cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
                    );
                }
                dialog.dismiss();
                return Unit.INSTANCE;
            });

        } else {
            dialog = getDialogDeleteConfirmation();
            dialog.setPrimaryCTAClickListener(() -> {
                if (cartItemDatas.size() > 0) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, appliedPromoCodes, false, removeMacroInsurance);
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter.generateCartDataAnalytics(
                                    cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
                    );
                }
                dialog.dismiss();
                return Unit.INSTANCE;
            });
            dialog.setSecondaryCTAClickListener(() -> {
                dialog.dismiss();
                return Unit.INSTANCE;
            });
        }

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
        cartPageAnalytics.eventClickAtcCartClickBelanjaSekarangOnEmptyCart();
        RouteManager.route(getActivity(), ApplinkConst.HOME);
    }

    @Override
    public void onShowAllItem(@NotNull String appLink) {
        RouteManager.route(getActivity(), appLink);
    }

    private WishListActionListener getRecommendationWishlistActionListener() {
        if (recommendationWishlistActionListener == null) {
            recommendationWishlistActionListener = new WishListActionListener() {
                @Override
                public void onErrorAddWishList(String errorMessage, String productId) {
                    showToastMessageRed(errorMessage);
                    cartAdapter.notifyByProductId(productId, false);
                    cartAdapter.notifyWishlist(productId, false);
                    cartAdapter.notifyRecentView(productId, false);
                    cartAdapter.notifyRecommendation(productId, false);
                }

                @Override
                public void onSuccessAddWishlist(String productId) {
                    showToastMessageGreen(getString(R.string.toast_message_add_wishlist_success));
                    cartAdapter.notifyByProductId(productId, true);
                    cartAdapter.notifyWishlist(productId, true);
                    cartAdapter.notifyRecentView(productId, true);
                    cartAdapter.notifyRecommendation(productId, true);
                    cartPageAnalytics.eventClickAddWishlistOnPrimaryProduct();
                }

                @Override
                public void onErrorRemoveWishlist(String errorMessage, String productId) {
                    showToastMessageRed(errorMessage);
                    cartAdapter.notifyByProductId(productId, true);
                    cartAdapter.notifyWishlist(productId, true);
                    cartAdapter.notifyRecentView(productId, true);
                    cartAdapter.notifyRecommendation(productId, true);
                }

                @Override
                public void onSuccessRemoveWishlist(String productId) {
                    showToastMessageGreen(getString(R.string.toast_message_remove_wishlist_success));
                    cartAdapter.notifyByProductId(productId, false);
                    cartAdapter.notifyWishlist(productId, false);
                    cartAdapter.notifyRecentView(productId, false);
                    cartAdapter.notifyRecommendation(productId, false);
                    cartPageAnalytics.eventClickRemoveWishlistOnPrimaryProduct();
                }
            };
        }
        return recommendationWishlistActionListener;
    }

    @Override
    public void onAddToWishlist(@NotNull String productId) {
        dPresenter.processAddToWishlist(productId, userSession.getUserId(), this);
    }

    @Override
    public void onAddRecommendationToWishlist(@NotNull String productId) {
        dPresenter.processAddToWishlist(productId, userSession.getUserId(), getRecommendationWishlistActionListener());
    }

    @Override
    public void onRemoveFromWishlist(@NotNull String productId) {
        dPresenter.processRemoveFromWishlist(productId, userSession.getUserId(), this);
    }

    @Override
    public void onRemoveRecommendationFromWishlist(@NotNull String productId) {
        dPresenter.processRemoveFromWishlist(productId, userSession.getUserId(), getRecommendationWishlistActionListener());
    }

    public void onProductClicked(@NotNull String productId) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.PRODUCT_INFO, productId);
        startActivityForResult(intent, NAVIGATION_PDP);
    }

    @Override
    public void onWishlistProductClicked(@NotNull String productId) {
        int position = 0;

        for (CartWishlistItemHolderData wishlist : wishLists) {
            if (wishlist.getId().equalsIgnoreCase(productId)) {
                if (FLAG_IS_CART_EMPTY) {
                    sendAnalyticsOnClickProductWishlistOnEmptyCart(String.valueOf(position),
                            dPresenter.generateWishlistProductClickEmptyCartDataLayer(wishlist, position));
                } else {
                    sendAnalyticsOnClickProductWishlistOnCartList(String.valueOf(position),
                            dPresenter.generateWishlistProductClickDataLayer(wishlist, position));
                }
            }
            position++;
        }

        onProductClicked(productId);
    }

    @Override
    public void onRecentViewProductClicked(@NotNull String productId) {
        int position = 0;

        for (CartRecentViewItemHolderData recentView : recentViewList) {
            if (recentView.getId().equalsIgnoreCase(productId)) {
                if (FLAG_IS_CART_EMPTY) {
                    sendAnalyticsOnClickProductRecentViewOnEmptyCart(String.valueOf(position),
                            dPresenter.generateRecentViewProductClickEmptyCartDataLayer(recentView, position));
                } else {
                    sendAnalyticsOnClickProductRecentViewOnCartList(String.valueOf(position),
                            dPresenter.generateRecentViewProductClickDataLayer(recentView, position));
                }
            }
            position++;
        }

        onProductClicked(productId);
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
            sendAnalyticsOnClickProductRecommendation(String.valueOf(position),
                    dPresenter.generateRecommendationDataOnClickAnalytics(recommendationItemClick, FLAG_IS_CART_EMPTY, position));
        }

        onProductClicked(productId);
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

    @Override
    public void onSelectAllClicked() {
        boolean checked = !dPresenter.getCartListData().isAllSelected();
        if (checked) {
            sendAnalyticsOnButtonSelectAllChecked();
        } else {
            sendAnalyticsOnButtonSelectAllUnchecked();
        }
        dPresenter.getCartListData().setAllSelected(checked);
        cbSelectAll.setChecked(checked);
        cartAdapter.setAllShopSelected(checked);
        dPresenter.setAllInsuranceProductsChecked(cartAdapter.getInsuranceCartShops(), checked);
        cartAdapter.notifyDataSetChanged();
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList(), cartAdapter.getInsuranceCartShops());
    }

    @Override
    public void onSeeErrorProductsClicked() {
        if (cartRecyclerView.getLayoutManager() != null) {
            LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(cartRecyclerView.getContext()) {
                @Override
                protected int getVerticalSnapPreference() {
                    return SNAP_TO_START;
                }
            };
            linearSmoothScroller.setTargetPosition(cartAdapter.getDisabledItemHeaderPosition());
            cartRecyclerView.getLayoutManager().startSmoothScroll(linearSmoothScroller);
        }
    }

    @Override
    public void onTickerDescriptionUrlClicked(@NotNull String url) {
        String finalUrl = url;
        if (!url.startsWith("https://")) {
            if (url.startsWith("http://")) {
                finalUrl = url.replace("http", "https");
            } else {
                finalUrl = "https://" + url;
            }
        }
        Intent view = new Intent();
        view.setAction(Intent.ACTION_VIEW);
        view.setData(Uri.parse(finalUrl));
        startActivity(view);
    }

    @Override
    public void onShowCartTicker(@NotNull String tickerId) {
        cartPageAnalytics.eventViewInformationAndWarningTickerInCart(tickerId);
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
    public void onCartShopNameClicked(@NotNull CartShopHolderData cartShopHolderData) {
        sendAnalyticsOnClickShopNameCartItem(cartShopHolderData.getShopGroupAvailableData().getShopName());

        if (getActivity() != null) {
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.SHOP, cartShopHolderData.getShopGroupAvailableData().getShopId());
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onShopItemCheckChanged(int itemPosition, boolean checked) {
        dPresenter.setHasPerformChecklistChange();
        cartAdapter.setShopSelected(itemPosition, checked);
        cartAdapter.notifyDataSetChanged();
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList(), cartAdapter.getInsuranceCartShops());
        cartAdapter.checkForShipmentForm();
    }

    @Override
    public void onCartItemRemarkEditChange(
            CartItemData cartItemData, String remark, int position, int parentPosition
    ) {

    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestionHolderData data, int position) {
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
        if (object instanceof ShopGroupAvailableData) {
            cartPageAnalytics.eventClickPilihMerchantVoucher();
            dPresenter.processUpdateCartDataPromoMerchant(getSelectedCartDataList(), (ShopGroupAvailableData) object);
        }
    }

    @Override
    public Promo generateCheckPromoFirstStepParam() {
        List<ShopGroupAvailableData> shopGroupAvailableDataList = cartListData.getShopGroupAvailableDataList();
        PromoStackingData promoStackingGlobalData = cartAdapter.getPromoStackingGlobalData();
        ArrayList<Order> orders = new ArrayList<>();
        for (ShopGroupAvailableData shopGroupAvailableData : shopGroupAvailableDataList) {
            Order order = new Order();
            ArrayList<ProductDetail> productDetails = new ArrayList<>();
            for (CartItemHolderData cartItemHolderData : shopGroupAvailableData.getCartItemDataList()) {
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
            if (shopGroupAvailableData.getVoucherOrdersItemData() != null && !TextUtils.isEmpty(shopGroupAvailableData.getVoucherOrdersItemData().getCode())) {
                ArrayList<String> merchantPromoCodes = new ArrayList<>();
                merchantPromoCodes.add(shopGroupAvailableData.getVoucherOrdersItemData().getCode());
                if (merchantPromoCodes.size() > 0) {
                    order.setCodes(merchantPromoCodes);
                }
            }
            order.setProductDetails(productDetails);
            order.setUniqueId(shopGroupAvailableData.getCartString());
            try {
                order.setShopId(Integer.parseInt(shopGroupAvailableData.getShopId()));
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
    public void onPromoGlobalTrackingImpression(PromoStackingData cartPromoGlobal) {
        trackingPromoCheckoutUtil.cartImpressionTicker(cartPromoGlobal.getPromoCodeSafe());
    }

    @Override
    public void onPromoGlobalTrackingCancelled(PromoStackingData cartPromoGlobal, int position) {
        sendAnalyticsOnClickCancelPromoCodeAndCouponBanner();
    }

    @Deprecated
    @Override
    public void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position) {
        List<CartShopHolderData> cartShopHolderDataList = getAllShopDataList();
        List<CartItemData> toBeDeletedCartItem = new ArrayList<>();
        List<CartItemData> allCartItemDataList = cartAdapter.getAllCartItemData();
        ArrayList<String> appliedPromoCodes = new ArrayList<>();

        for (CartShopHolderData cartShopHolderData : cartShopHolderDataList) {
            if (cartShopHolderData.getShopGroupAvailableData().isError()) {
                for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                    toBeDeletedCartItem.add(cartItemHolderData.getCartItemData());
                    if (cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData() != null &&
                            !TextUtils.isEmpty(cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData().getCode())) {
                        String promoCode = cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData().getCode();
                        if (!appliedPromoCodes.contains(promoCode)) {
                            appliedPromoCodes.add(promoCode);
                        }
                    }
                }
            } else {
                for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                    if (cartItemHolderData.getCartItemData().isError()) {
                        toBeDeletedCartItem.add(cartItemHolderData.getCartItemData());
                        if (cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData() != null &&
                                !TextUtils.isEmpty(cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData().getCode())) {
                            String promoCode = cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData().getCode();
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
        final DialogUnify dialog = getMultipleDisabledItemsDialogDeleteConfirmation(toBeDeletedCartItem.size());
        dialog.setPrimaryCTAClickListener(() -> {
            if (toBeDeletedCartItem.size() > 0) {
                dPresenter.processDeleteCartItem(allCartItemDataList, toBeDeletedCartItem, appliedPromoCodes, false, false);
                sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductNoAddToWishList(
                        dPresenter.generateCartDataAnalytics(
                                toBeDeletedCartItem, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }
            dialog.dismiss();
            return Unit.INSTANCE;
        });
        dialog.setSecondaryCTAClickListener(() -> {
            dialog.dismiss();
            return Unit.INSTANCE;
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
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList(), cartAdapter.getInsuranceCartShops());
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
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList(), cartAdapter.getInsuranceCartShops());
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
                if (cartListData.getGlobalCouponAttrData() != null) {
                    if (cartListData.getGlobalCouponAttrData().getDescription() != null) {
                        if (!cartListData.getGlobalCouponAttrData().getDescription().isEmpty()) {
                            builderGlobal.title(cartListData.getGlobalCouponAttrData().getDescription());
                            builderGlobal.titleDefault(cartListData.getGlobalCouponAttrData().getDescription());
                        }
                    }

                    if (cartListData.getGlobalCouponAttrData().getQuantityLabel() != null) {
                        if (!cartListData.getGlobalCouponAttrData().getQuantityLabel().isEmpty()) {
                            builderGlobal.counterLabel(cartListData.getGlobalCouponAttrData().getQuantityLabel());
                            builderGlobal.counterLabelDefault(cartListData.getGlobalCouponAttrData().getQuantityLabel());
                        }
                    }
                }
            }

            PromoStackingData promoStackingData = builderGlobal.build();

            if (cartListData.getShopGroupAvailableDataList().isEmpty() && cartListData.getShopGroupWithErrorDataList().isEmpty()) {
                renderCartEmpty(cartListData, promoStackingData);
            } else {
                renderCartNotEmpty(cartListData, promoStackingData);
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
                dPresenter.processGetRecommendationData(endlessRecyclerViewScrollListener.getCurrentPage(), cartAdapter.getAllCartItemProductId());
            } else {
                renderRecommendation(null);
            }

        }
    }

    private void renderCartNotEmpty(CartListData cartListData, PromoStackingData promoStackingData) {
        cartAdapter.removeCartEmptyData();

        TickerData tickerData = cartListData.getTickerData();
        if (tickerData != null && tickerData.isValid(CART_PAGE)) {
            cartAdapter.addCartTicker(new TickerAnnouncementHolderData(String.valueOf(tickerData.getId()), tickerData.getMessage()));
        }
        cartAdapter.addPromoStackingVoucherData(promoStackingData);
        if (promoStackingData.getState() != TickerPromoStackingCheckoutView.State.FAILED) {
            onPromoGlobalTrackingImpression(promoStackingData);
        }

        if (cartListData.isError()) {
            cartAdapter.addCartTickerError(
                    new CartItemTickerErrorHolderData.Builder()
                            .cartTickerErrorData(cartListData.getCartTickerErrorData())
                            .build()
            );
        }

        cartAdapter.addAvailableDataList(cartListData.getShopGroupAvailableDataList());

        if (cartListData.getShopGroupWithErrorDataList().size() > 0) {
            cartAdapter.addNotAvailableHeader(viewHolderDataMapper.mapDisabledItemHeaderHolderData(
                    cartListData.getCartTickerErrorData().getErrorCount()));
            for (ShopGroupWithErrorData shopGroupWithErrorData : cartListData.getShopGroupWithErrorDataList()) {
                List<CartItemHolderData> cartItemHolderDataList = shopGroupWithErrorData.getCartItemHolderDataList();
                if (cartItemHolderDataList.size() > 0) {
                    cartAdapter.addNotAvailableShop(viewHolderDataMapper.mapDisabledShopHolderData(shopGroupWithErrorData));
                    for (IndexedValue<CartItemHolderData> dataIndexedValue : CollectionsKt.withIndex(cartItemHolderDataList)) {
                        cartAdapter.addNotAvailableProduct(viewHolderDataMapper.mapDisabledItemHolderData(dataIndexedValue.getValue(), dataIndexedValue.getIndex() != cartItemHolderDataList.size() - 1));
                    }
                }
            }
        }

        if (cartListData.getShopGroupAvailableDataList() != null &&
                !cartListData.getShopGroupAvailableDataList().isEmpty() &&
                isInsuranceEnabled) {
            dPresenter.getInsuranceTechCart();
        }

        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList(), cartAdapter.getInsuranceCartShops());
        if (cbSelectAll != null) {
            cbSelectAll.setChecked(cartListData.isAllSelected());
        }

        cartAdapter.checkForShipmentForm();
        onCartNotEmpty();

        cartPageAnalytics.eventViewCartListFinishRender();
        List<CartItemData> cartItemDataList = cartAdapter.getAllCartItemData();
        cartPageAnalytics.enhancedECommerceCartLoadedStep0(
                dPresenter.generateCheckoutDataAnalytics(cartItemDataList, EnhancedECommerceActionField.STEP_0)
        );

        cartAdapter.notifyDataSetChanged();
    }

    private void renderCartEmpty(CartListData cartListData, PromoStackingData promoStackingData) {
        if (promoStackingData.getState() != TickerPromoStackingCheckoutView.State.EMPTY) {
            cartAdapter.addPromoStackingVoucherData(promoStackingData);
            if (promoStackingData.getState() != TickerPromoStackingCheckoutView.State.FAILED) {
                onPromoGlobalTrackingImpression(promoStackingData);
            }
        }
        cartAdapter.addCartEmptyData();

        TickerData tickerData = cartListData.getTickerData();
        if (tickerData != null && tickerData.isValid(CART_PAGE)) {
            cartAdapter.addCartTicker(new TickerAnnouncementHolderData(String.valueOf(tickerData.getId()), tickerData.getMessage()));
        }

        onCartEmpty();
        cartAdapter.notifyDataSetChanged();
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
                                            List<CartItemData> cartItemDataList,
                                            boolean checkoutProductEligibleForCashOnDelivery,
                                            int checklistCondition) {
        Bundle eCommerceBundle = cartTrackingDataGenerator.generateBundleEnhancedEcommerce(cartItemDataList);
        switch (checklistCondition) {
            case CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES:
                cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessDefault(eeCheckoutData, checkoutProductEligibleForCashOnDelivery);
                cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessDefault(eCommerceBundle, checkoutProductEligibleForCashOnDelivery);
                break;
            case CartListPresenter.ITEM_CHECKED_ALL_WITH_CHANGES:
                cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessCheckAll(eeCheckoutData, checkoutProductEligibleForCashOnDelivery);
                cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessCheckAll(eCommerceBundle, checkoutProductEligibleForCashOnDelivery);
                break;
            case CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP:
                cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShop(eeCheckoutData, checkoutProductEligibleForCashOnDelivery);
                cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShop(eCommerceBundle, checkoutProductEligibleForCashOnDelivery);
                break;
            case CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM:
                cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(eeCheckoutData, checkoutProductEligibleForCashOnDelivery);
                cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(eCommerceBundle, checkoutProductEligibleForCashOnDelivery);
                break;
            case CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM:
                cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(eeCheckoutData, checkoutProductEligibleForCashOnDelivery);
                cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(eCommerceBundle, checkoutProductEligibleForCashOnDelivery);
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
                cartListData.getCartPromoSuggestionHolderData(), cartListData.getDefaultPromoDialogTab(),
                isAutoApplyPromoStackCodeApplied
        );
        startActivityForResult(intent, ShipmentActivity.REQUEST_CODE);
    }

    private void clearRecyclerView() {
        cartAdapter.unsubscribeSubscription();
        cartRecyclerView.setAdapter(null);
        cartAdapter = new CartAdapter(null, null, null, null, null);
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

    public List<InsuranceCartShops> getSelectedRecommendedInsuranceList() {
        return cartAdapter.getSelectedRecommendedInsuranceList();
    }

    @Override
    public List<CartItemData> getAllCartDataList() {
        return cartAdapter.getAllCartItemData();
    }

    @Override
    public List<CartItemData> getAllAvailableCartDataList() {
        return cartAdapter.getAllAvailableCartItemData();
    }

    @Override
    public List<CartShopHolderData> getAllShopDataList() {
        return cartAdapter.getAllShopGroupDataList();
    }

    @Override
    public void renderDetailInfoSubTotal(String qty, String subtotalPrice, boolean selectAllCartItem, boolean unselectAllItem, boolean hasAvailableItems) {
        if (dPresenter.getCartListData() != null) {
            dPresenter.getCartListData().setAllSelected(selectAllCartItem);
        }
        if (cbSelectAll.isChecked() != selectAllCartItem) {
            cbSelectAll.setChecked(selectAllCartItem);
        }
        btnRemove.setVisibility(unselectAllItem ? View.INVISIBLE : View.VISIBLE);
        cardHeader.setVisibility(hasAvailableItems ? View.GONE : View.VISIBLE);
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

        Intent intent = getIntentToPromoList(promo);
        startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
    }

    @NotNull
    private Intent getIntentToPromoList(Promo promo) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_LIST_MARKETPLACE);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, cartListData.isPromoCouponActive());
        bundle.putString(ConstantKt.getPROMO_CODE(), "");
        bundle.putBoolean(ConstantKt.getONE_CLICK_SHIPMENT(), false);
        bundle.putInt(ConstantKt.getPAGE_TRACKING(), TrackingPromoCheckoutConstantKt.getFROM_CART());
        bundle.putParcelable(ConstantKt.getCHECK_PROMO_FIRST_STEP_PARAM(), promo);
        intent.putExtras(bundle);
        return intent;
    }

    @NotNull
    private Intent getIntentToPromoDetail(Promo promo, PromoStackingData promoStackingData) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_DETAIL_MARKETPLACE);
        intent.putExtra(ConstantKt.getEXTRA_KUPON_CODE(), promoStackingData.getPromoCodeSafe());
        intent.putExtra(ConstantKt.getEXTRA_IS_USE(), true);
        intent.putExtra(ConstantKt.getONE_CLICK_SHIPMENT(), false);
        intent.putExtra(ConstantKt.getPAGE_TRACKING(), TrackingPromoCheckoutConstantKt.getFROM_CART());
        intent.putExtra(ConstantKt.getCHECK_PROMO_CODE_FIRST_STEP_PARAM(), promo);
        return intent;
    }

    @Override
    public void goToDetailPromoStacking(PromoStackingData promoStackingData) {
        Promo promo = generateCheckPromoFirstStepParam();

        if (promoStackingData.getTypePromo() == PromoStackingData.CREATOR.getTYPE_COUPON()) {
            Intent intent = getIntentToPromoDetail(promo, promoStackingData);
            startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
        } else {
            Intent intent = getIntentToPromoList(promo);
            startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
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
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList(), cartAdapter.getInsuranceCartShops());
        notifyBottomCartParent();
    }

    @NonNull
    private DialogUnify getDialogDeleteConfirmation() {
        DialogUnify dialogUnify = new DialogUnify(getActivity(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE);
        dialogUnify.setTitle(getString(R.string.label_dialog_title_delete_item));
        dialogUnify.setDescription(getString(R.string.label_dialog_message_remove_cart_item));
        dialogUnify.setPrimaryCTAText(getString(R.string.label_dialog_action_delete));
        dialogUnify.setSecondaryCTAText(getString(R.string.label_dialog_action_cancel));
        return dialogUnify;
    }

    @NonNull
    private DialogUnify getMultipleItemsDialogDeleteConfirmation(int count) {
        DialogUnify dialogUnify = new DialogUnify(getActivity(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE);
        dialogUnify.setTitle(getString(R.string.label_dialog_title_delete_multiple_item, count));
        dialogUnify.setDescription(getString(R.string.label_dialog_message_remove_cart_multiple_item));
        dialogUnify.setPrimaryCTAText(getString(R.string.label_dialog_action_delete));
        dialogUnify.setSecondaryCTAText(getString(R.string.label_dialog_action_cancel));
        return dialogUnify;
    }

    @NonNull
    private DialogUnify getDisabledItemDialogDeleteConfirmation() {
        DialogUnify dialogUnify = new DialogUnify(getActivity(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE);
        dialogUnify.setTitle(getString(R.string.label_dialog_title_delete_disabled_item));
        dialogUnify.setDescription(getString(R.string.label_dialog_message_remove_cart_item));
        dialogUnify.setPrimaryCTAText(getString(R.string.label_dialog_action_delete));
        dialogUnify.setSecondaryCTAText(getString(R.string.label_dialog_action_cancel));
        return dialogUnify;
    }

    @NonNull
    private DialogUnify getMultipleDisabledItemsDialogDeleteConfirmation(int count) {
        DialogUnify dialogUnify = new DialogUnify(getActivity(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE);
        dialogUnify.setTitle(getString(R.string.label_dialog_title_delete_disabled_multiple_item, count));
        dialogUnify.setDescription(getString(R.string.label_dialog_message_remove_cart_multiple_disabled_item));
        dialogUnify.setPrimaryCTAText(getString(R.string.label_dialog_action_delete));
        dialogUnify.setSecondaryCTAText(getString(R.string.label_dialog_action_cancel));
        return dialogUnify;
    }

    @NonNull
    private DialogUnify getInsuranceDialogDeleteConfirmation() {
        DialogUnify dialog = new DialogUnify(getActivity(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE);
        dialog.setTitle(getString(R.string.label_dialog_title_delete_item_macro_insurance));
        dialog.setDescription(getString(R.string.label_dialog_message_remove_cart_multiple_item_with_insurance));
        dialog.setPrimaryCTAText(getString(R.string.label_dialog_action_delete_and_add_to_wishlist_macro_insurance));
        dialog.setSecondaryCTAText(getString(R.string.label_dialog_action_delete_macro_insurance));
        return dialog;
    }

    @Override
    public void onRefresh(View view) {
        if (dPresenter.dataHasChanged()) {
            showMainContainer();
            dPresenter.processToUpdateAndReloadCartData();
        } else {
            if (dPresenter.getCartListData() != null && dPresenter.getCartListData().getShopGroupAvailableDataList().size() > 0) {
                showMainContainer();
            }
            dPresenter.processInitialGetCartData(getCartId(), cartListData == null, true);
            String promo = PersistentCacheManager.instance.getString("KEY_CACHE_PROMO_CODE", "");
//            if (!TextUtils.isEmpty(promo)) {
//                dPresenter.processCheckPromoCodeFromSuggestedPromo(promo, true);
//            }
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
            cartAdapter = new CartAdapter(this, this, this, this, this);
            cartRecyclerView.setAdapter(cartAdapter);
        }
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = false;
        if (resultCode == PaymentConstant.PAYMENT_CANCELLED) {
            showToastMessageRed(getString(R.string.alert_payment_canceled_or_failed_transaction_module));
            dPresenter.processInitialGetCartData(getCartId(), false, false);
        } else if (resultCode == PaymentConstant.PAYMENT_SUCCESS) {
            showToastMessageGreen(getString(R.string.message_payment_success));
            refreshHandler.setRefreshing(true);
            dPresenter.processInitialGetCartData(getCartId(), false, false);
        } else if (resultCode == PaymentConstant.PAYMENT_FAILED) {
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
                    if (cartListData.getGlobalCouponAttrData() != null) {
                        if (cartListData.getGlobalCouponAttrData().getDescription() != null) {
                            if (!cartListData.getGlobalCouponAttrData().getDescription().isEmpty()) {
                                promoStackingData.setTitleDefault(cartListData.getGlobalCouponAttrData().getDescription());
                            }
                        }

                        if (cartListData.getGlobalCouponAttrData().getQuantityLabel() != null) {
                            if (!cartListData.getGlobalCouponAttrData().getQuantityLabel().isEmpty()) {
                                promoStackingData.setCounterLabelDefault(cartListData.getGlobalCouponAttrData().getQuantityLabel());
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
        cartAdapter.notifyRecommendation(productId, false);
    }

    @Override
    public void onSuccessAddWishlist(String productId) {
        showToastMessageGreen(getString(R.string.toast_message_add_wishlist_success));
        cartAdapter.notifyByProductId(productId, true);
        cartAdapter.notifyWishlist(productId, true);
        cartAdapter.notifyRecentView(productId, true);
        cartAdapter.notifyRecommendation(productId, true);
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        showToastMessageRed(errorMessage);
        cartAdapter.notifyByProductId(productId, true);
        cartAdapter.notifyWishlist(productId, true);
        cartAdapter.notifyRecentView(productId, true);
        cartAdapter.notifyRecommendation(productId, true);
    }

    @Override
    public void onSuccessRemoveWishlist(String productId) {
        showToastMessageGreen(getString(R.string.toast_message_remove_wishlist_success));
        cartAdapter.notifyByProductId(productId, false);
        cartAdapter.notifyWishlist(productId, false);
        cartAdapter.notifyRecentView(productId, false);
        cartAdapter.notifyRecommendation(productId, false);
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
    public void sendAnalyticsOnViewProductRecommendation(Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedEcommerceViewRecommendationOnCart(eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnClickProductRecommendation(String position, Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedEcommerceClickProductRecommendationOnEmptyCart(position, eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnViewProductWishlist(Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedEcommerceProductViewWishList(eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnClickProductWishlistOnEmptyCart(String position, Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedEcommerceClickProductWishListOnEmptyCart(position, eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnClickProductWishlistOnCartList(String position, Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedEcommerceClickProductWishListOnCartList(position, eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnViewProductRecentView(Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedEcommerceProductViewLastSeen(eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnClickProductRecentViewOnEmptyCart(String position, Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedEcommerceClickProductLastSeenOnEmptyCart(position, eeDataLayerCart);
    }

    @Override
    public void sendAnalyticsOnClickProductRecentViewOnCartList(String position, Map<String, Object> eeDataLayerCart) {
        cartPageAnalytics.enhancedEcommerceClickProductLastSeenOnCartList(position, eeDataLayerCart);
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
    public void showMerchantVoucherListBottomsheet(ShopGroupAvailableData shopGroupAvailableData) {
        Promo promo = generateCheckPromoFirstStepParam();
        if (getFragmentManager() != null) {
            int shopId = 0;
            try {
                shopId = Integer.parseInt(shopGroupAvailableData.getShopId());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            ArrayList<CartItemDataVoucher> cartItemDataVoucherArrayList = new ArrayList<>();
            for (CartItemHolderData cartItemHolderData : shopGroupAvailableData.getCartItemDataList()) {
                if (cartItemHolderData.isSelected()) {
                    try {
                        CartItemDataVoucher cartItemDataVoucher = new CartItemDataVoucher();
                        cartItemDataVoucher.setProductId(Integer.parseInt(cartItemHolderData.getCartItemData().getOriginData().getProductId()));
                        cartItemDataVoucher.setProductName(cartItemHolderData.getCartItemData().getOriginData().getProductName());
                        cartItemDataVoucherArrayList.add(cartItemDataVoucher);
                    } catch (NumberFormatException e) {
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            MerchantVoucherListBottomSheetFragment merchantVoucherListBottomSheetFragment =
                    MerchantVoucherListBottomSheetFragment.newInstance(shopId, shopGroupAvailableData.getCartString(), promo, "cart", cartItemDataVoucherArrayList);
            merchantVoucherListBottomSheetFragment.setActionListener(this);
            merchantVoucherListBottomSheetFragment.show(getFragmentManager(), "");
        }
    }

    @Override
    public void onClashCheckPromo(@NonNull ClashingInfoDetailUiModel clashingInfoDetailUiModel,
                                  @NotNull String type) {
        ClashBottomSheetFragment clashBottomSheetFragment = ClashBottomSheetFragment.newInstance();
        clashBottomSheetFragment.setData(clashingInfoDetailUiModel);
        clashBottomSheetFragment.setActionListener(this);
        clashBottomSheetFragment.setAnalyticsCart(cartPageAnalytics);
        clashBottomSheetFragment.setSource("cart");
        clashBottomSheetFragment.setType(type);
        clashBottomSheetFragment.show(getFragmentManager(), "");
    }

    @Override
    public ArrayList<InsuranceCartDigitalProduct> getInsuranceCartShopData() {
        try {

            ArrayList<InsuranceCartDigitalProduct> insuranceCartDigitalProductArrayList = new ArrayList<>();
            for (InsuranceCartShops insuranceCartShops : cartAdapter.getInsuranceCartShops()) {
                if (insuranceCartShops != null &&
                        !insuranceCartShops.getShopItemsList().isEmpty()) {
                    for (InsuranceCartShopItems insuranceCartShopItems : insuranceCartShops.getShopItemsList()) {
                        if (insuranceCartShopItems.getDigitalProductList() != null &&
                                !insuranceCartShopItems.getDigitalProductList().isEmpty()) {
                            for (InsuranceCartDigitalProduct insuranceCartDigitalProduct : insuranceCartShopItems.getDigitalProductList()) {
                                insuranceCartDigitalProductArrayList.add(insuranceCartDigitalProduct);
                            }
                        }
                    }
                }

            }
            return insuranceCartDigitalProductArrayList;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void removeInsuranceProductItem(List<Long> productId) {
        if (cartAdapter != null) {
            cartAdapter.removeInsuranceDataItem(productId);
        }
    }

    @Override
    public void renderInsuranceCartData(InsuranceCartResponse insuranceCartResponse, boolean isRecommendation) {

        /*
         * render insurance cart data on ui, both micro and macro, if is_product_level == true,
         * then insurance product is of type micro insurance and should be tagged at product level,
         * for micro insurance product add insurance data in shopGroup list*/


        if (insuranceCartResponse != null &&
                insuranceCartResponse.getCartShopsList() != null &&
                !insuranceCartResponse.getCartShopsList().isEmpty()) {
            for (InsuranceCartShops insuranceCartShops : insuranceCartResponse.getCartShopsList()) {
                long shopId = insuranceCartShops.getShopId();
                for (InsuranceCartShopItems insuranceCartShopItems : insuranceCartShops.getShopItemsList()) {
                    for (InsuranceCartDigitalProduct insuranceCartDigitalProduct : insuranceCartShopItems.getDigitalProductList()) {
                        insuranceCartDigitalProduct.setShopId(String.valueOf(shopId));
                        insuranceCartDigitalProduct.setProductId(String.valueOf(insuranceCartShopItems.getProductId()));
                        if (!insuranceCartDigitalProduct.isProductLevel()) {
                            cartAdapter.addInsuranceDataList(insuranceCartShops, isRecommendation);
                        }
                    }
                }
            }
            cartAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onSuccessCheckPromoMerchantFirstStep(@NotNull ResponseGetPromoStackUiModel promoData, @NotNull String promoCode) {
        onSuccessCheckPromoFirstStep(promoData);
    }

    @Override
    public void onSuccessCheckPromoFirstStep(@NonNull ResponseGetPromoStackUiModel
                                                     responseGetPromoStackUiModel) {
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
                    if (voucherOrdersItemUiModel.getUniqueId().equals(cartShopHolderData.getShopGroupAvailableData().getCartString())) {
                        VoucherOrdersItemData voucherOrdersItemData = cartShopHolderData.getShopGroupAvailableData().getVoucherOrdersItemData();
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

                        cartShopHolderData.getShopGroupAvailableData().setVoucherOrdersItemData(voucherOrdersItemData);
                        break;
                    }
                }
                if (responseGetPromoStackUiModel.getData().getTrackingDetailUiModel().size() > 0) {
                    for (TrackingDetailUiModel trackingDetailUiModel : responseGetPromoStackUiModel.getData().getTrackingDetailUiModel()) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
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
            if (cartListData.getShopGroupAvailableDataList().isEmpty()) {
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
                cartShopHolderData.getShopGroupAvailableData().setVoucherOrdersItemData(null);
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
                cartShopHolderData.getShopGroupAvailableData().setVoucherOrdersItemData(null);
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
    public void onSubmitNewPromoAfterClash(@NotNull ArrayList<String> oldPromoList,
                                           @NotNull ArrayList<ClashingVoucherOrderUiModel> newPromoList,
                                           @NotNull String type) {
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
    public void updateInsuranceProductData(InsuranceCartShops insuranceCartShops,
                                           ArrayList<UpdateInsuranceProductApplicationDetails> updateInsuranceProductApplicationDetailsArrayList) {
        dPresenter.updateInsuranceProductData(insuranceCartShops, updateInsuranceProductApplicationDetailsArrayList);
    }

    @Override
    public void deleteMacroInsurance(@NotNull ArrayList<InsuranceCartDigitalProduct> insuranceCartDigitalProductArrayList, boolean showConfirmationDialog) {
        if (showConfirmationDialog) {
            View view = getLayoutInflater().inflate(R.layout.remove_insurance_product, null, false);
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setCancelable(true)
                    .show();

            view.findViewById(R.id.button_positive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dPresenter.processDeleteCartInsurance(insuranceCartDigitalProductArrayList, showConfirmationDialog);
                    alertDialog.dismiss();
                }
            });

            view.findViewById(R.id.button_negative).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } else {
            dPresenter.processDeleteCartInsurance(insuranceCartDigitalProductArrayList, showConfirmationDialog);
        }
    }

    @Override
    public void onInsuranceSelectStateChanges() {
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList(), cartAdapter.getInsuranceCartShops());
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

        sendAnalyticsOnViewProductRecentView(
                dPresenter.generateRecentViewDataImpressionAnalytics(cartRecentViewItemHolderDataList, FLAG_IS_CART_EMPTY)
        );
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

        sendAnalyticsOnViewProductWishlist(
                dPresenter.generateWishlistDataImpressionAnalytics(cartWishlistItemHolderDataList, FLAG_IS_CART_EMPTY)
        );
    }

    @Override
    public void renderRecommendation(@Nullable RecommendationWidget recommendationWidget) {
        List<CartRecommendationItemHolderData> cartRecommendationItemHolderDataList = new ArrayList<>();

        if (recommendationWidget != null) {
            // Render from API
            List<RecommendationItem> recommendationItems = recommendationWidget.getRecommendationItemList();
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
        if ((endlessRecyclerViewScrollListener.getCurrentPage() == 0 && recommendationWidget == null) || (recommendationWidget != null && endlessRecyclerViewScrollListener.getCurrentPage() == 1 && recommendationSectionHeader == null)) {
            if (recommendationSectionHeader != null) {
                cartSectionHeaderHolderData = recommendationSectionHeader;
            } else {
                cartSectionHeaderHolderData = new CartSectionHeaderHolderData();
                if (recommendationWidget != null && !TextUtils.isEmpty(recommendationWidget.getTitle())) {
                    cartSectionHeaderHolderData.setTitle(recommendationWidget.getTitle());
                } else {
                    cartSectionHeaderHolderData.setTitle(getString(R.string.checkout_module_title_recommendation));
                }
                recommendationSectionHeader = cartSectionHeaderHolderData;
            }
        }

        if (cartRecommendationItemHolderDataList.size() > 0) {
            cartAdapter.addCartRecommendationData(cartSectionHeaderHolderData, cartRecommendationItemHolderDataList);
            recommendationList = cartRecommendationItemHolderDataList;

            sendAnalyticsOnViewProductRecommendation(
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
            eventCategory = ConstantTransactionAnalytics.EventCategory.CART;
            eventAction = ConstantTransactionAnalytics.EventAction.CLICK_BELI_ON_WISHLIST;
            eventLabel = "";
            stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer((CartWishlistItemHolderData) productModel, addToCartDataResponseModel, FLAG_IS_CART_EMPTY);
        } else if (productModel instanceof CartRecentViewItemHolderData) {
            eventCategory = ConstantTransactionAnalytics.EventCategory.CART;
            eventAction = ConstantTransactionAnalytics.EventAction.CLICK_BELI_ON_RECENT_VIEW_PAGE;
            eventLabel = "";
            stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer((CartRecentViewItemHolderData) productModel, addToCartDataResponseModel, FLAG_IS_CART_EMPTY);
        } else if (productModel instanceof CartRecommendationItemHolderData) {
            eventCategory = ConstantTransactionAnalytics.EventCategory.CART;
            eventAction = ConstantTransactionAnalytics.EventAction.CLICK_ADD_TO_CART;
            eventLabel = "";
            stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer((CartRecommendationItemHolderData) productModel, addToCartDataResponseModel, FLAG_IS_CART_EMPTY);
        }

        if (stringObjectMap != null) {
            cartPageAnalytics.sendEnhancedECommerceAddToCart(stringObjectMap, eventCategory, eventAction, eventLabel);
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

    @Override
    public void onDeleteAllDisabledProduct() {
        List<CartItemData> allDisabledCartItemDataList = cartAdapter.getAllDisabledCartItemData();

        DialogUnify dialog = getMultipleDisabledItemsDialogDeleteConfirmation(allDisabledCartItemDataList.size());

        sendAnalyticsOnClickRemoveCartConstrainedProduct(dPresenter.generateCartDataAnalytics(
                allDisabledCartItemDataList, EnhancedECommerceCartMapData.REMOVE_ACTION
        ));

        dialog.setPrimaryCTAClickListener(() -> {
            if (allDisabledCartItemDataList.size() > 0) {
                dPresenter.processDeleteCartItem(allDisabledCartItemDataList, allDisabledCartItemDataList, null, false, false);
                sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductNoAddToWishList(
                        dPresenter.generateCartDataAnalytics(
                                allDisabledCartItemDataList, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }
            dialog.dismiss();
            return Unit.INSTANCE;
        });
        dialog.setSecondaryCTAClickListener(() -> {
            dialog.dismiss();
            return Unit.INSTANCE;
        });
        dialog.show();
    }

    @Override
    public void onDeleteDisabledItem(CartItemData cartItemData) {
        sendAnalyticsOnClickRemoveIconCartItem();
        List<CartItemData> cartItemDatas = Collections.singletonList(cartItemData);
        List<CartItemData> allCartItemDataList = cartAdapter.getAllDisabledCartItemData();

        DialogUnify dialog = getDisabledItemDialogDeleteConfirmation();

        dialog.setPrimaryCTAClickListener(() -> {
            dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, null, false, false);
            sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                    dPresenter.generateCartDataAnalytics(
                            cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                    )
            );
            dialog.dismiss();
            return Unit.INSTANCE;
        });
        dialog.setSecondaryCTAClickListener(() -> {
            dialog.dismiss();
            return Unit.INSTANCE;
        });
        dialog.show();
    }
}
