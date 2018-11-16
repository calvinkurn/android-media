package com.tokopedia.checkout.view.feature.cartlist;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
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
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartTickerErrorData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ShopGroupData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.common.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveView;
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveWithBackView;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.di.component.CartListComponent;
import com.tokopedia.checkout.view.di.component.DaggerCartListComponent;
import com.tokopedia.checkout.view.di.module.CartListModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.feature.addressoptions.CartAddressChoiceActivity;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartItemAdapter;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartTopAdsModel;
import com.tokopedia.checkout.view.feature.shipment.ShipmentActivity;
import com.tokopedia.checkout.view.feature.shipment.ShipmentData;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.navigation_common.listener.CartNotifyListener;
import com.tokopedia.navigation_common.listener.EmptyCartListener;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCartMapData;
import com.tokopedia.transactiondata.entity.request.UpdateCartRequest;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartFragment extends BaseCheckoutFragment implements CartAdapter.ActionListener,
        CartItemAdapter.ActionListener, ICartListView, TopAdsItemClickListener,
        RefreshHandler.OnRefreshHandlerListener, ICartListAnalyticsListener, WishListActionListener,
        ToolbarRemoveView.OnToolbarRemoveAllCartListener {

    private static final int HAS_ELEVATION = 8;
    private static final int NO_ELEVATION = 0;

    private View toolbar;
    private AppBarLayout appBarLayout;
    private RecyclerView cartRecyclerView;
    private TextView btnToShipment;
    private TextView tvTotalPrice;
    private TextView tvItemCount;
    private TkpdProgressDialog progressDialogNormal;
    private RelativeLayout layoutUsedPromoEmptyCart;
    private RelativeLayout rlContent;
    private LinearLayout llHeader;
    private CheckBox cbSelectAll;
    private CardView cardHeader;
    private CardView cardFooter;
    private LinearLayout llNetworkErrorView;
    private LinearLayout emptyCartContainer;
    private TopAdsCarouselView topAdsCarouselView;

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
    UserSession userSession;

    private RefreshHandler refreshHandler;

    private boolean mIsMenuVisible = false;
    private boolean isToolbarWithBackButton = true;
    private EmptyCartListener emptyCartListener;

    private CartListData cartListData;
    private PromoCodeAppliedData promoCodeAppliedData;

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
    }

    @Override
    public void onDestroy() {
        cartAdapter.unsubscribeSubscription();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        boolean hasChanges = dPresenter.dataHasChanged();

        if (hasChanges && getActivity() != null && getSelectedCartDataList() != null && getSelectedCartDataList().size() > 0) {
            Intent service = new Intent(getActivity(), UpdateCartIntentService.class);
            service.putParcelableArrayListExtra(
                    UpdateCartIntentService.EXTRA_CART_ITEM_DATA_LIST, new ArrayList<>(getSelectedCartDataList())
            );
            getActivity().startService(service);
        }
        super.onStop();
    }

    @Override
    protected void initInjector() {
        CartListComponent cartListComponent = DaggerCartListComponent.builder()
                .cartComponent(CartComponentInjector.newInstance(getActivity().getApplication()).getCartApiServiceComponent())
                .cartListModule(new CartListModule(this))
                .trackingAnalyticsModule(new TrackingAnalyticsModule())
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
            if (cartListData != null) {
                renderInitialGetCartListDataSuccess(cartListData);
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
        topAdsCarouselView = view.findViewById(R.id.topads);
        progressDialogNormal = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
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

    @Override
    public void onToolbarRemoveAllCart() {
        sendAnalyticsOnClickRemoveButtonHeader();
        List<CartItemData> toBeDeletedCartItemDataList = cartAdapter.getSelectedCartItemData();
        List<CartItemData> allCartItemDataList = cartAdapter.getAllCartItemData();
        final boolean deleteAllCartData = toBeDeletedCartItemDataList.size() == allCartItemDataList.size();
        if (toBeDeletedCartItemDataList.size() > 0) {
            final com.tokopedia.design.component.Dialog dialog = getDialogDeleteConfirmation(toBeDeletedCartItemDataList.size());
            dialog.setOnOkClickListener(v -> {
                if (toBeDeletedCartItemDataList.size() > 0) {
                    dPresenter.processDeleteAndRefreshCart(toBeDeletedCartItemDataList, true, deleteAllCartData);
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
                    dPresenter.processDeleteAndRefreshCart(toBeDeletedCartItemDataList, false, deleteAllCartData);
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
        btnToShipment.setOnClickListener(getOnClickButtonToShipmentListener(null));
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
            dPresenter.setCheckedCartItemState(cartAdapter.getAllCartItemHolderData());
            cartAdapter.notifyDataSetChanged();
            dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList());
        };
    }

    @NonNull
    private View.OnClickListener getOnClickButtonToShipmentListener(String message) {
        return view -> {
            if (message == null) {
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
        if (getActivity() != null) {
            setHasOptionsMenu(true);
            getActivity().setTitle(getActivity().getString(R.string.title_activity_cart));
            if (cartListData == null) {
                refreshHandler.startRefresh();
            }
        }
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition) {
        sendAnalyticsOnClickRemoveIconCartItem();
        ArrayList<CartItemData> cartItemDatas =
                new ArrayList<>(Collections.singletonList(cartItemHolderData.getCartItemData()));
        List<CartItemData> allCartItemDataList = cartAdapter.getAllCartItemData();
        final boolean deleteAllCartData = cartItemDatas.size() == allCartItemDataList.size();
        final com.tokopedia.design.component.Dialog dialog = getDialogDeleteConfirmation(1);
        dialog.setOnOkClickListener(view -> {
            if (cartItemDatas.size() > 0) {
                dPresenter.processDeleteAndRefreshCart(cartItemDatas, true, deleteAllCartData);
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
                dPresenter.processDeleteAndRefreshCart(cartItemDatas, false, deleteAllCartData);
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
    public void onCartItemQuantityFormEdited(int position, int parentPosition, boolean needRefreshItemView) {
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList());
    }

    @Override
    public void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition) {
        sendAnalyticsOnClickProductNameCartItem(cartItemHolderData.getCartItemData().getOriginData().getProductName());
        navigateToActivity(
                checkoutModuleRouter.checkoutModuleRouterGetProductDetailIntent(
                        cartItemHolderData.getCartItemData().getOriginData().getProductId()
                ));
    }

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
        dPresenter.setCheckedCartItemState(cartAdapter.getAllCartItemHolderData());
        dPresenter.setHasPerformChecklistChange();
        cartAdapter.setShopSelected(itemPosition, checked);
        cartAdapter.notifyDataSetChanged();
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList());
        cartAdapter.checkForShipmentForm();
    }

    @Override
    public void onCartItemRemarkEditChange(
            CartItemData cartItemData, String remark, int position, int parentPosition
    ) {

    }

    @Override
    public void onCartPromoSuggestionActionClicked(CartPromoSuggestion data, int position) {
        dPresenter.processCheckPromoCodeFromSuggestedPromo(data.getPromoCode(), false);
    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion data, int position) {
        data.setVisible(false);
        cartAdapter.notifyItemChanged(position);
        cartAdapter.checkForShipmentForm();
    }

    @Override
    public void onCartItemListIsEmpty(int shopPosition) {
        renderEmptyCartData(null);
    }

    @Override
    public void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartItemPromoHolderData, int position) {
        sendAnalyticsOnClickUsePromoCodeAndCoupon();
        List<CartItemData> cartItemDataList = getSelectedCartDataList();
        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();
        for (CartItemData data : cartItemDataList) {
            updateCartRequestList.add(new UpdateCartRequest.Builder()
                    .cartId(data.getOriginData().getCartId())
                    .notes(data.getUpdatedData().getRemark())
                    .quantity(data.getUpdatedData().getQuantity())
                    .build());
        }
        startActivityForResult(
                checkoutModuleRouter
                        .checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartListIntent(
                                cartListData.isPromoCouponActive(),
                                new Gson().toJson(updateCartRequestList),
                                cartItemPromoHolderData.getDefaultSelectedTabString()
                        ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
        );
    }

    @Override
    public void onCartPromoCancelVoucherPromoClicked(CartItemPromoHolderData cartItemPromoHolderData, int position) {
        dPresenter.processCancelAutoApply();
    }

    @Override
    public void onCartPromoTrackingSuccess(CartItemPromoHolderData cartItemPromoHolderData, int position) {

    }

    @Override
    public void onCartPromoTrackingCancelled(CartItemPromoHolderData cartItemPromoHolderData, int position) {
        sendAnalyticsOnClickCancelPromoCodeAndCouponBanner();
    }

    @Override
    public void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position) {
        List<CartShopHolderData> cartShopHolderDataList = getAllShopDataList();
        List<CartItemData> toBeDeletedCartItem = new ArrayList<>();
        List<CartItemData> allCartItemDataList = cartAdapter.getAllCartItemData();

        for (CartShopHolderData cartShopHolderData : cartShopHolderDataList) {
            if (cartShopHolderData.getShopGroupData().isError()) {
                for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                    toBeDeletedCartItem.add(cartItemHolderData.getCartItemData());
                }
            } else {
                for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                    if (cartItemHolderData.getCartItemData().isError()) {
                        toBeDeletedCartItem.add(cartItemHolderData.getCartItemData());
                    }
                }
            }
        }
        final boolean deleteAllCartData = toBeDeletedCartItem.size() == allCartItemDataList.size();

        sendAnalyticsOnClickRemoveCartConstrainedProduct(dPresenter.generateCartDataAnalytics(
                toBeDeletedCartItem, EnhancedECommerceCartMapData.REMOVE_ACTION
        ));
        final com.tokopedia.design.component.Dialog dialog = getDialogDeleteConfirmation(toBeDeletedCartItem.size());
        dialog.setOnOkClickListener(view -> {
            if (toBeDeletedCartItem.size() > 0) {
                dPresenter.processDeleteAndRefreshCart(toBeDeletedCartItem, true, deleteAllCartData);
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
                dPresenter.processDeleteAndRefreshCart(toBeDeletedCartItem, false, deleteAllCartData);
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
    public void onDropshipperValidationResult(boolean result, ShipmentData shipmentData, int position) {

    }

    @Override
    public void onCartDataEnableToCheckout() {
        if (isAdded()) {
            btnToShipment.setOnClickListener(getOnClickButtonToShipmentListener(null));
        }
    }

    @Override
    public void onCartDataDisableToCheckout(String message) {
        if (isAdded()) {
            btnToShipment.setOnClickListener(getOnClickButtonToShipmentListener(getString(R.string.message_checkout_empty_selection)));
        }
    }

    @Override
    public void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel) {

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
    public void onQuantityChanged() {

    }

    @Override
    public boolean onCartItemCheckChanged(int position, int parentPosition, boolean checked) {
        dPresenter.setCheckedCartItemState(cartAdapter.getAllCartItemHolderData());
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
        progressDialogNormal.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialogNormal.dismiss();
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
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
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
    public void closeView() {

    }

    @Override
    public void renderInitialGetCartListDataSuccess(CartListData cartListData) {
        refreshHandler.finishRefresh();
        this.cartListData = cartListData;
        cartAdapter.resetData();

        CartItemPromoHolderData cartItemPromoHolderData;
        if (cartListData.getAutoApplyData() != null && cartListData.getAutoApplyData().isSuccess()) {
            cartItemPromoHolderData = CartItemPromoHolderData.createInstanceFromAutoApply(
                    cartListData.getAutoApplyData());
            if (cartItemPromoHolderData.getTypePromo() == PromoCodeAppliedData.TYPE_COUPON) {
                promoCodeAppliedData = new PromoCodeAppliedData.Builder()
                        .typeVoucher(PromoCodeAppliedData.TYPE_COUPON)
                        .promoCode(cartItemPromoHolderData.getCouponCode())
                        .couponTitle(cartItemPromoHolderData.getCouponTitle())
                        .description(cartItemPromoHolderData.getCouponMessage())
                        .amount((int) cartItemPromoHolderData.getCouponDiscountAmount())
                        .fromAutoApply(true)
                        .build();
            } else {
                promoCodeAppliedData = new PromoCodeAppliedData.Builder()
                        .typeVoucher(PromoCodeAppliedData.TYPE_VOUCHER)
                        .promoCode(cartItemPromoHolderData.getVoucherCode())
                        .description(cartItemPromoHolderData.getVoucherMessage())
                        .amount((int) cartItemPromoHolderData.getVoucherDiscountAmount())
                        .fromAutoApply(true)
                        .build();
            }
            sendAnalyticsOnViewPromoAutoApply();
        } else {
            cartItemPromoHolderData = new CartItemPromoHolderData();
            cartItemPromoHolderData.setPromoNotActive();
            promoCodeAppliedData = null;
        }
        cartItemPromoHolderData.setDefaultSelectedTabString(cartListData.getDefaultPromoDialogTab());
        cartAdapter.addPromoVoucherData(cartItemPromoHolderData);

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

        cartAdapter.setCheckedItemState(dPresenter.getCheckedCartItemState());
        cartAdapter.addDataList(cartListData.getShopGroupDataList());
        cartAdapter.addTopAdsSection(cartListData.getShopGroupDataList());
        dPresenter.reCalculateSubTotal(cartAdapter.getAllShopGroupDataList());
        cbSelectAll.setChecked(cartListData.isAllSelected());

        cartAdapter.checkForShipmentForm();

        if (toolbar != null) {
            setVisibilityRemoveButton(true);
        } else {
            if (getActivity() != null && !mIsMenuVisible && !cartListData.getShopGroupDataList().isEmpty()) {
                mIsMenuVisible = true;
                getActivity().invalidateOptionsMenu();
            }
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
        rlContent.setVisibility(View.GONE);
        emptyCartContainer.setVisibility(View.VISIBLE);
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
    public void renderErrorHttpInitialGetCartListData(String message) {
        if (cartAdapter.getItemCount() > 0) {
            showSnackbarRetry(message);
        } else {
            showErrorLayout(message);
        }
    }

    @Override
    public void renderErrorNoConnectionInitialGetCartListData(String message) {
        if (cartAdapter.getItemCount() > 0) {
            showSnackbarRetry(message);
        } else {
            showErrorLayout(message);
        }
    }

    @Override
    public void renderErrorTimeoutConnectionInitialGetCartListData(String message) {
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
    public void renderErrorActionDeleteCartData(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorHttpActionDeleteCartData(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorNoConnectionActionDeleteCartData(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorTimeoutConnectionActionDeleteCartData(String message) {
        showToastMessageRed(message);
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
    public void renderToShipmentFormSuccess(Map<String, Object> eeCheckoutData, int checklistCondition) {
        switch (checklistCondition) {
            case CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES:
                sendAnalyticsOnSuccessToCheckoutDefault(eeCheckoutData);
                break;
            case CartListPresenter.ITEM_CHECKED_ALL_WITH_CHANGES:
                sendAnalyticsOnSuccessToCheckoutCheckAll(eeCheckoutData);
                break;
            case CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP:
                sendAnalyticsOnSuccessToCheckoutPartialShop(eeCheckoutData);
                break;
            case CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM:
                sendAnalyticsOnSuccessToCheckoutPartialProduct(eeCheckoutData);
                break;
            case CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM:
                sendAnalyticsOnSuccessToCheckoutPartialShopAndProduct(eeCheckoutData);
                break;
        }
        renderToAddressChoice();
    }

    @Override
    public void renderToAddressChoice() {
        boolean isAutoApplyPromoCodeApplied = dPresenter.getCartListData() != null &&
                dPresenter.getCartListData().getAutoApplyData() != null &&
                dPresenter.getCartListData().getAutoApplyData().isSuccess();
        Intent intent = ShipmentActivity.createInstance(getActivity(), promoCodeAppliedData,
                cartListData.getCartPromoSuggestion(), cartListData.getDefaultPromoDialogTab(),
                isAutoApplyPromoCodeApplied
        );
        startActivityForResult(intent, ShipmentActivity.REQUEST_CODE);
    }

    @Override
    public void renderErrorToShipmentForm(String message) {
        sendAnalyticsOnButtonCheckoutClickedFailed();
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorHttpToShipmentForm(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorNoConnectionToShipmentForm(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorTimeoutConnectionToShipmentForm(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorToShipmentMultipleAddress(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorHttpToShipmentMultipleAddress(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorNoConnectionToShipmentMultipleAddress(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorTimeoutConnectionToShipmentMultipleAddress(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData) {
        this.promoCodeAppliedData = new PromoCodeAppliedData.Builder()
                .typeVoucher(PromoCodeAppliedData.TYPE_VOUCHER)
                .promoCode(promoCodeCartListData.getDataVoucher().getCode())
                .description(promoCodeCartListData.getDataVoucher().getMessageSuccess())
                .amount(promoCodeCartListData.getDataVoucher().getCashbackAmount())
                .build();
        CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
        cartItemPromoHolderData.setPromoVoucherType(promoCodeAppliedData.getPromoCode(),
                promoCodeAppliedData.getDescription(), promoCodeAppliedData.getAmount());
        cartAdapter.updateItemPromoVoucher(cartItemPromoHolderData);
    }

    @Override
    public void renderErrorCheckPromoCodeFromSuggestedPromo(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorHttpCheckPromoCodeFromSuggestedPromo(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorNoConnectionCheckPromoCodeFromSuggestedPromo(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorTimeoutConnectionCheckPromoCodeFromSuggestedPromo(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderEmptyCartData(CartListData cartListData) {
        enableSwipeRefresh();
        sendAnalyticsOnDataCartIsEmpty();
        refreshHandler.finishRefresh();
        mIsMenuVisible = false;
        getActivity().invalidateOptionsMenu();
        checkoutModuleRouter.checkoutModuleRouterResetBadgeCart();

        String autoApplyMessage = null;
        if (cartListData != null && cartListData.getAutoApplyData() != null &&
                cartListData.getAutoApplyData().isSuccess()) {
            autoApplyMessage = cartListData.getAutoApplyData().getTitleDescription();
        }
        if (emptyCartListener != null) {
            emptyCartListener.onCartEmpty(autoApplyMessage);
        } else {
            if (getActivity() instanceof EmptyCartListener) {
                ((EmptyCartListener) getActivity()).onCartEmpty(autoApplyMessage);
            }
        }
        showEmptyCartContainer();
        notifyBottomCartParent();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            sendAnalyticsScreenName(getScreenName());
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
                    }
                }
            }
        } else {
            dPresenter.setCheckedCartItemState(cartAdapter.getAllCartItemHolderData());
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
        dPresenter.getCartListData().setAllSelected(selectAllCartItem);
        cbSelectAll.setChecked(selectAllCartItem);
        tvTotalPrice.setText(subtotalPrice);
        btnToShipment.setText(String.format(getString(R.string.cart_item_button_checkout_count_format), qty));
    }

    @Override
    public void updateCashback(double cashback) {
        cartAdapter.updateShipmentSellerCashback(cashback);
    }

    @Override
    public void renderPromoVoucher() {
        CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
        cartItemPromoHolderData.setPromoNotActive();
        cartAdapter.addPromoVoucherData(cartItemPromoHolderData);
    }

    @Override
    public void showToastMessageRed(String message) {
        View view = getView();
        if (view != null) {
            NetworkErrorHelper.showRedCloseSnackbar(view, message);
        } else if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showToastMessageGreen(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showGreenCloseSnackbar(view, message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
        promoCodeAppliedData = null;
        if (layoutUsedPromoEmptyCart != null) {
            layoutUsedPromoEmptyCart.setVisibility(View.GONE);
        } else {
            cartAdapter.cancelAutoApplyCoupon();
            cartAdapter.checkForShipmentForm();
        }
    }

    @Override
    public void renderCancelAutoApplyCouponError() {
        NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.default_request_error_unknown));
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
        navigateToActivity(checkoutModuleRouter.checkoutModuleRouterGetProductDetailIntentForTopAds(product));
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        navigateToActivity(checkoutModuleRouter.checkoutModuleRouterGetShopInfoIntent(shop.getId()));
    }

    @Override
    public void onAddFavorite(int position, Data shopData) {
    }

    @Override
    public void onAddWishList(int position, Data data) {
        //TODO: next implement wishlist action
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
            NetworkErrorHelper.showSnackbar(
                    getActivity(),
                    getString(R.string.alert_payment_canceled_or_failed_transaction_module)
            );
            dPresenter.processResetAndRefreshCartData();
        } else if (resultCode == TopPayActivity.PAYMENT_SUCCESS) {
            showToastMessage(getString(R.string.message_payment_success));
            navigateToActivity(checkoutModuleRouter.checkoutModuleRouterGetTransactionSummaryIntent());
            checkoutModuleRouter.checkoutModuleRouterResetBadgeCart();
            getActivity().finish();
        } else if (resultCode == TopPayActivity.PAYMENT_FAILED) {
            showToastMessage(getString(R.string.default_request_error_unknown));
        }
    }

    private void onResultFromRequestCodeLoyalty(int resultCode, Intent data) {
        if (resultCode == IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String voucherCode = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE, "");
                String voucherMessage = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE, "");
                long voucherDiscountAmount = bundle.getLong(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT);
                this.promoCodeAppliedData = new PromoCodeAppliedData.Builder()
                        .typeVoucher(PromoCodeAppliedData.TYPE_VOUCHER)
                        .promoCode(voucherCode)
                        .description(voucherMessage)
                        .amount((int) voucherDiscountAmount)
                        .build();
                CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
                cartItemPromoHolderData.setPromoVoucherType(voucherCode, voucherMessage, voucherDiscountAmount);

                cartAdapter.updateItemPromoVoucher(cartItemPromoHolderData);
                sendAnalyticsOnViewPromoManualApply("voucher");
            }
        } else if (resultCode == IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String couponTitle = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TITLE, "");
                String couponMessage = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE, "");
                String couponCode = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE, "");
                long couponDiscountAmount = bundle.getLong(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_DISCOUNT_AMOUNT);
                this.promoCodeAppliedData = new PromoCodeAppliedData.Builder()
                        .typeVoucher(PromoCodeAppliedData.TYPE_COUPON)
                        .promoCode(couponCode)
                        .couponTitle(couponTitle)
                        .description(couponMessage)
                        .amount((int) couponDiscountAmount)
                        .build();
                CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
                cartItemPromoHolderData.setPromoCouponType(
                        couponTitle, couponCode, couponMessage, couponDiscountAmount
                );

                cartAdapter.updateItemPromoVoucher(cartItemPromoHolderData);
                sendAnalyticsOnViewPromoManualApply("coupon");
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
}
