package com.tokopedia.checkout.view.view.cartlist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartTickerErrorData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.adapter.CartListAdapter;
import com.tokopedia.checkout.view.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartListComponent;
import com.tokopedia.checkout.view.di.component.DaggerCartListComponent;
import com.tokopedia.checkout.view.di.module.CartListModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.holderitemdata.CartItemHolderData;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.view.addressoptions.CartAddressChoiceActivity;
import com.tokopedia.checkout.view.view.shipment.ShipmentActivity;
import com.tokopedia.checkout.view.view.shipment.ShipmentData;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.payment.activity.TopPayActivity;
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
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCartMapData;
import com.tokopedia.transactiondata.entity.request.UpdateCartRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.transaction.common.constant.CartConstant.TOPADS_CART_SRC;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartFragment extends BaseCheckoutFragment implements CartListAdapter.ActionListener,
        ICartListView, TopAdsItemClickListener, RefreshHandler.OnRefreshHandlerListener {
    private static final int REQUEST_CODE_ROUTE_WISHLIST = 123;

    private RecyclerView cartRecyclerView;
    private TextView btnToShipment;
    private TextView tvTotalPrice;
    private View bottomLayout;
    private TextView tvItemCount;
    private TkpdProgressDialog progressDialogNormal;
    private RelativeLayout layoutUsedPromoEmptyCart;
    private RelativeLayout rlContent;
    private LinearLayout llNetworkErrorView;
    private LinearLayout emptyCartContainer;

    @Inject
    ICartListPresenter dPresenter;
    @Inject
    CartListAdapter cartListAdapter;
    @Inject
    RecyclerView.ItemDecoration cartItemDecoration;
    @Inject
    CheckoutAnalyticsCart cartPageAnalytics;
    @Inject
    CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection;
    @Inject
    ICheckoutModuleRouter checkoutModuleRouter;

    private RefreshHandler refreshHandler;

    private boolean mIsMenuVisible = false;

    private ActionListener mDataPasserListener;
    private CartListData cartListData;
    private PromoCodeAppliedData promoCodeAppliedData;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDataPasserListener = (ActionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    " must implement OnPassingCartDataListener");
        }
    }

    @Override
    public void onDetach() {
        if (getActivity() != null && getCartDataList() != null && getCartDataList().size() > 0) {
            Intent service = new Intent(getActivity(), UpdateCartIntentService.class);
            service.putParcelableArrayListExtra(UpdateCartIntentService.EXTRA_CART_ITEM_DATA_LIST, new ArrayList<>(getCartDataList()));
            getActivity().startService(service);
        }
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        cartListAdapter.unsubscribeSubscription();
        dPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void initInjector() {
        CartListComponent cartListComponent = DaggerCartListComponent.builder()
                .cartComponent(getComponent(CartComponent.class))
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mIsMenuVisible) {
            inflater.inflate(R.menu.menu_checkout_cart_remove, menu);
            MenuItem item = menu.getItem(0);
            item.setActionView(R.layout.layout_menu_delete);
            TextView deleteTextView = (TextView) item.getActionView();
            deleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartPageAnalytics.eventClickAtcCartClickHapusOnTopRightCorner();
                    mDataPasserListener.onRemoveAllCartMenuClicked(cartListAdapter.getCartItemDataList());
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_new;
    }

    @Override
    protected void initView(View view) {
        cartRecyclerView = view.findViewById(R.id.rv_cart);
        btnToShipment = view.findViewById(R.id.go_to_courier_page_button);
        tvTotalPrice = view.findViewById(R.id.tv_total_prices);
        bottomLayout = view.findViewById(R.id.bottom_layout);
        tvItemCount = view.findViewById(R.id.tv_item_count);
        rlContent = view.findViewById(R.id.rl_content);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        emptyCartContainer = view.findViewById(R.id.container_empty_cart);

        progressDialogNormal = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        refreshHandler = new RefreshHandler(getActivity(), view, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartRecyclerView.setAdapter(cartListAdapter);
        cartRecyclerView.addItemDecoration(cartItemDecoration);
        ((SimpleItemAnimator) cartRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    protected void setViewListener() {
        btnToShipment.setOnClickListener(getOnClickButtonToShipmentListener());
    }

    @NonNull
    private View.OnClickListener getOnClickButtonToShipmentListener() {
        return view -> {
            dPresenter.processToUpdateCartData();
            sendAnalyticsOnButtonCheckoutClicked();
        };
    }

    private void sendAnalyticsOnButtonCheckoutClicked() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickSelectCourierOnCart();
    }

    @Override
    protected void initialVar() {
        setHasOptionsMenu(true);
        getActivity().setTitle(getActivity().getString(R.string.title_activity_cart));
        refreshHandler.startRefresh();
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position) {
        cartPageAnalytics.eventClickAtcCartClickTrashBin();
        ArrayList<CartItemData> cartItemDatas =
                new ArrayList<>(Collections.singletonList(cartItemHolderData.getCartItemData()));
        final com.tokopedia.design.component.Dialog dialog = getDialogDeleteConfirmation(1);
        dialog.setOnOkClickListener(view -> {
            if (cartItemDatas.size() > 0) {
                dPresenter.processDeleteAndRefreshCart(cartItemDatas, true);
                cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusDanTambahWishlistFromTrashBin(
                        dPresenter.generateCartDataAnalytics(
                                cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }
            dialog.dismiss();
        });
        dialog.setOnCancelClickListener(view -> {
            if (cartItemDatas.size() > 0) {
                dPresenter.processDeleteAndRefreshCart(cartItemDatas, false);
                cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromTrashBin(
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
    public void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position) {
        cartPageAnalytics.eventClickAtcCartClickButtonPlus();
        cartListAdapter.increaseQuantity(position);
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());
    }

    @Override
    public void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position) {
        cartPageAnalytics.eventClickAtcCartClickButtonMinus();
        cartListAdapter.decreaseQuantity(position);
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());
    }

    @Override
    public void onCartItemQuantityReseted(int position, boolean needRefreshItemView) {
        cartListAdapter.resetQuantity(position);
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());
        if (needRefreshItemView) {
            cartListAdapter.notifyItems(position);
        }
    }

    @Override
    public void onCartItemQuantityFormEdited(int position, boolean needRefreshItemView) {
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());
        if (needRefreshItemView) {
            cartListAdapter.notifyItems(position);
        }
    }

    @Override
    public void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position) {
        cartPageAnalytics.eventClickAtcCartClickProductName(cartItemHolderData.getCartItemData().getOriginData().getProductName());
        if (getActivity().getApplication() instanceof ICheckoutModuleRouter) {
            startActivity(((ICheckoutModuleRouter) getActivity().getApplication()).checkoutModuleRouterGetProductDetailIntent(
                    getActivity(),
                    ProductPass.Builder.aProductPass()
                            .setProductId(cartItemHolderData.getCartItemData().getOriginData().getProductId())
                            .setProductImage(cartItemHolderData.getCartItemData().getOriginData().getProductImage())
                            .setProductName(cartItemHolderData.getCartItemData().getOriginData().getProductName())
                            .setProductPrice(cartItemHolderData.getCartItemData().getOriginData().getPriceFormatted())
                            .build()
            ));
        }
    }

    @Override
    public void onCartItemShopNameClicked(CartItemHolderData cartItemHolderData, int position) {
        cartPageAnalytics.eventClickAtcCartClickShopName(cartItemHolderData.getCartItemData().getOriginData().getShopName());
        if (getActivity().getApplication() instanceof ICheckoutModuleRouter) {
            startActivity(((ICheckoutModuleRouter) getActivity().getApplication()).checkoutModuleRouterGetShopInfoIntent(
                    getActivity(),
                    cartItemHolderData.getCartItemData().getOriginData().getShopId()
            ));
        }
    }

    @Override
    public void onCartItemRemarkEditChange(CartItemData cartItemData, int position, String remark) {

    }

    @Override
    public void onCartPromoSuggestionActionClicked(CartPromoSuggestion data, int position) {
        dPresenter.processCheckPromoCodeFromSuggestedPromo(data.getPromoCode(), false);
    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion data, int position) {
        data.setVisible(false);
        cartListAdapter.notifyItemChanged(position);
        cartListAdapter.checkForShipmentForm();
    }

    @Override
    public void onCartItemListIsEmpty() {
        renderEmptyCartData(null);
    }

    @Override
    public void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartItemPromoHolderData, int position) {
        cartPageAnalytics.eventClickAtcCartClickGunakanKodePromoAatauKupon();
        List<CartItemData> cartItemDataList = getCartDataList();
        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();
        for (CartItemData data : cartItemDataList) {
            updateCartRequestList.add(new UpdateCartRequest.Builder()
                    .cartId(data.getOriginData().getCartId())
                    .notes(data.getUpdatedData().getRemark())
                    .quantity(data.getUpdatedData().getQuantity())
                    .build());
        }
        if (getActivity().getApplication() instanceof ICheckoutModuleRouter) {
            startActivityForResult(
                    ((ICheckoutModuleRouter) getActivity().getApplication())
                            .checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartListIntent(
                                    getActivity(), cartListData.isPromoCouponActive(),
                                    new Gson().toJson(updateCartRequestList),
                                    cartItemPromoHolderData.getDefaultSelectedTabString()
                            ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
            );
        }
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
        cartPageAnalytics.eventClickAtcCartClickXOnBannerPromoCode();
        cartPageAnalytics.eventClickAtcCartClickXFromGunakanKodePromoAtauKupon();
    }

    @Override
    public void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position) {
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusProdukBerkendala(
                dPresenter.generateCartDataAnalytics(
                        getCartDataList(), EnhancedECommerceCartMapData.REMOVE_ACTION
                )
        );
        List<CartItemData> toBeDeletedCartItem = new ArrayList<>();
        for (CartItemData cartItemData : getCartDataList()) {
            if (cartItemData.isError()) {
                toBeDeletedCartItem.add(cartItemData);
            }
        }
        final com.tokopedia.design.component.Dialog dialog = getDialogDeleteConfirmation(toBeDeletedCartItem.size());
        dialog.setOnOkClickListener(view -> {
            if (toBeDeletedCartItem.size() > 0) {
                dPresenter.processDeleteAndRefreshCart(toBeDeletedCartItem, true);
                cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusDanTambahWishlistFromHapusProdukBerkendala(
                        dPresenter.generateCartDataAnalytics(
                                toBeDeletedCartItem, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }
            dialog.dismiss();
        });
        dialog.setOnCancelClickListener(view -> {
            if (toBeDeletedCartItem.size() > 0) {
                dPresenter.processDeleteAndRefreshCart(toBeDeletedCartItem, false);
                cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromHapusProdukBerkendala(
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
            btnToShipment.setBackgroundResource(R.drawable.orange_button_rounded);
            btnToShipment.setTextColor(getResources().getColor(R.color.white));
            btnToShipment.setOnClickListener(getOnClickButtonToShipmentListener());
        }
    }

    @Override
    public void onCartDataDisableToCheckout() {
        if (isAdded()) {
            btnToShipment.setBackgroundResource(R.drawable.bg_grey_button_rounded_checkout_module);
            btnToShipment.setTextColor(getResources().getColor(R.color.grey_500));
            btnToShipment.setOnClickListener(null);
        }
    }

    @Override
    public void onCartItemAfterErrorChecked() {
        cartListAdapter.checkForShipmentForm();
    }

    @Override
    public void onCartItemQuantityInputFormClicked(String qty) {
        cartPageAnalytics.eventClickAtcCartClickInputQuantity(qty);
    }

    @Override
    public void onCartItemLabelInputRemarkClicked() {
        cartPageAnalytics.eventClickAtcCartClickTulisCatatan();
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
                getActivity(), SessionHandler.getLoginID(getActivity()),
                GCMHandler.getRegistrationId(getActivity())
        )
                : AuthUtil.generateParamsNetwork(
                getActivity(), originParams,
                SessionHandler.getLoginID(getActivity()),
                GCMHandler.getRegistrationId(getActivity()
                )
        );
    }

    @Override
    public void closeView() {

    }

    @Override
    public void renderInitialGetCartListDataSuccess(CartListData cartListData) {
        refreshHandler.finishRefresh();
        this.cartListData = cartListData;
        cartListAdapter.resetData();

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
        } else {
            cartItemPromoHolderData = new CartItemPromoHolderData();
            cartItemPromoHolderData.setPromoNotActive();
            promoCodeAppliedData = null;
        }
        cartItemPromoHolderData.setDefaultSelectedTabString(cartListData.getDefaultPromoDialogTab());
        cartListAdapter.addPromoVoucherData(cartItemPromoHolderData);

        if (cartListData.getCartPromoSuggestion().isVisible()) {
            cartListAdapter.addPromoSuggestion(cartListData.getCartPromoSuggestion());
        }

        if (cartListData.isError()) {
            cartListAdapter.addCartTickerError(
                    new CartItemTickerErrorHolderData.Builder()
                            .cartTickerErrorData(cartListData.getCartTickerErrorData())
                            .build()
            );
        }

        cartListAdapter.addDataList(cartListData.getCartItemDataList());
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());

        if (!mIsMenuVisible && !cartListData.getCartItemDataList().isEmpty()) {
            mIsMenuVisible = true;
            getActivity().invalidateOptionsMenu();
        }
        cartListAdapter.checkForShipmentForm();
    }

    private void showErrorLayout(String message) {
        mIsMenuVisible = false;
        getActivity().invalidateOptionsMenu();
        refreshHandler.finishRefresh();
        showErrorContainer();
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                () -> {
                    showMainContainer();
                    refreshHandler.setPullEnabled(true);
                    refreshHandler.setRefreshing(true);
                    cartListAdapter.resetData();
                    dPresenter.processInitialGetCartData();
                });
    }

    public void showMainContainerLoadingInitData() {
        emptyCartContainer.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.GONE);
        rlContent.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.GONE);
    }

    public void showMainContainer() {
        emptyCartContainer.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.GONE);
        rlContent.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);
    }

    public void showErrorContainer() {
        rlContent.setVisibility(View.GONE);
        emptyCartContainer.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.GONE);
    }

    public void showEmptyCartContainer() {
        rlContent.setVisibility(View.GONE);
        emptyCartContainer.setVisibility(View.VISIBLE);
        llNetworkErrorView.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
    }

    private void showSnackbarRetry(String message) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                dPresenter.processInitialGetCartData();
            }
        }).showRetrySnackbar();
    }

    @Override
    public void renderErrorInitialGetCartListData(String message) {
        if (cartListAdapter.getItemCount() > 0) {
            showSnackbarRetry(message);
        } else {
            showErrorLayout(message);
        }
    }

    @Override
    public void renderErrorHttpInitialGetCartListData(String message) {
        if (cartListAdapter.getItemCount() > 0) {
            showSnackbarRetry(message);
        } else {
            showErrorLayout(message);
        }
    }

    @Override
    public void renderErrorNoConnectionInitialGetCartListData(String message) {
        if (cartListAdapter.getItemCount() > 0) {
            showSnackbarRetry(message);
        } else {
            showErrorLayout(message);
        }
    }

    @Override
    public void renderErrorTimeoutConnectionInitialGetCartListData(String message) {
        if (cartListAdapter.getItemCount() > 0) {
            showSnackbarRetry(message);
        } else {
            showErrorLayout(message);
        }
    }

    @Override
    public void renderActionDeleteCartDataSuccess(CartItemData cartItemData, String message, boolean addWishList) {
        cartListAdapter.deleteItem(cartItemData);
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());
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
    public void renderToShipmentFormSuccess() {
        Intent intent = ShipmentActivity.createInstance(getActivity(), promoCodeAppliedData,
                cartListData.getCartPromoSuggestion(), cartListData.getDefaultPromoDialogTab()
        );
        startActivityForResult(intent, ShipmentActivity.REQUEST_CODE);
    }

    @Override
    public void renderToAddressChoice() {
        Intent intent = ShipmentActivity.createInstance(getActivity(), promoCodeAppliedData,
                cartListData.getCartPromoSuggestion(), cartListData.getDefaultPromoDialogTab()
        );
        startActivityForResult(intent, ShipmentActivity.REQUEST_CODE);
    }

    @Override
    public void renderErrorToShipmentForm(String message) {
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
        cartListAdapter.updateItemPromoVoucher(cartItemPromoHolderData);
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
        cartPageAnalytics.eventViewAtcCartImpressionCartEmpty();
        refreshHandler.finishRefresh();
        mIsMenuVisible = false;
        getActivity().invalidateOptionsMenu();
        CartBadgeNotificationReceiver.resetBadgeCart(getActivity());
        showEmptyCartContainer();
        emptyCartContainer.removeAllViews();


        @SuppressLint("InflateParams") View emptyState = LayoutInflater.from(getActivity()).
                inflate(R.layout.layout_empty_shopping_cart_new, null);

        NestedScrollView scrollViewEmptyCart = emptyState.findViewById(R.id.scrollview_empty_cart);
        layoutUsedPromoEmptyCart = emptyState.findViewById(R.id.layout_used_promo);
        TextView tvPromoCodeEmptyCart = emptyState.findViewById(R.id.textview_promo_code);
        View btnCancelPromoCodeEmptyCart = emptyState.findViewById(R.id.button_cancel);
        View btnContinueShoppingEmptyCart = emptyState.findViewById(R.id.btn_shopping_now);
        View btnAddFromWishListEmptyCart = emptyState.findViewById(R.id.btn_add_from_whislist);
        TopAdsView topAdsViewEmptyCart = emptyState.findViewById(R.id.topads);
        emptyCartContainer.addView(emptyState);

        if (cartListData != null && cartListData.getAutoApplyData() != null &&
                cartListData.getAutoApplyData().isSuccess()) {
            layoutUsedPromoEmptyCart.setVisibility(View.VISIBLE);
            tvPromoCodeEmptyCart.setText(cartListData.getAutoApplyData().getTitleDescription());
            btnCancelPromoCodeEmptyCart.setOnClickListener(view -> dPresenter.processCancelAutoApply());
        } else {
            layoutUsedPromoEmptyCart.setVisibility(View.GONE);
        }
        btnContinueShoppingEmptyCart.setOnClickListener(view -> {
            cartPageAnalytics.eventClickAtcCartClickBelanjaSekarangOnEmptyCart();
            navigateToActivity(
                    checkoutModuleRouter.getHomeFeedIntent(getActivity())
            );
            getActivity().finish();
        });
        btnAddFromWishListEmptyCart.setOnClickListener(v -> {
            cartPageAnalytics.eventClickAtcCartClickAddFromWishlistOnEmptyCart();
            navigateToActivityRequest(
                    checkoutModuleRouter.checkoutModuleRouterGetWhislistIntent(),
                    REQUEST_CODE_ROUTE_WISHLIST
            );
        });
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, TOPADS_CART_SRC);

        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .withPreferedCategory()
                .setEndpoint(Endpoint.PRODUCT)
                .displayMode(DisplayMode.FEED)
                .topAdsParams(params)
                .build();

        topAdsViewEmptyCart.setConfig(config);
        topAdsViewEmptyCart.setDisplayMode(DisplayMode.FEED);
        topAdsViewEmptyCart.setMaxItems(4);
        topAdsViewEmptyCart.setAdsItemClickListener(this);
        topAdsViewEmptyCart.loadTopAds();

        int scrollTo = ((View) tvPromoCodeEmptyCart.getParent().getParent()).getTop() + tvPromoCodeEmptyCart.getTop();
        scrollViewEmptyCart.smoothScrollTo(0, scrollTo);

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
    public List<CartItemData> getCartDataList() {
        return cartListAdapter.getCartItemDataList();
    }

    @Override
    public void renderDetailInfoSubTotal(String qty, String subtotalPrice) {
        tvTotalPrice.setText(subtotalPrice);
        tvItemCount.setText(String.format(getActivity().getString(R.string.cart_item_count_format), qty));
    }

    @Override
    public void updateCashback(double cashback) {
        cartListAdapter.updateShipmentSellerCashback(cashback);
    }

    @Override
    public void renderPromoVoucher() {
        CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
        cartItemPromoHolderData.setPromoNotActive();
        cartListAdapter.addPromoVoucherData(cartItemPromoHolderData);
    }

    @Override
    public void showToastMessageRed(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showRedCloseSnackbar(view, message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void renderLoadGetCartData() {
        showMainContainerLoadingInitData();
    }

    @Override
    public void renderLoadGetCartDataFinish() {
        cartListAdapter.resetData();
        showMainContainer();
    }

    @Override
    public void renderCartTickerError(CartTickerErrorData cartTickerErrorData) {
        cartListAdapter.addCartTickerError(new CartItemTickerErrorHolderData.Builder()
                .cartTickerErrorData(cartTickerErrorData)
                .build());
    }

    @Override
    public void renderCancelAutoApplyCouponSuccess() {
        promoCodeAppliedData = null;
        if (layoutUsedPromoEmptyCart != null) {
            layoutUsedPromoEmptyCart.setVisibility(View.GONE);
        } else {
            cartListAdapter.cancelAutoApplyCoupon();
            cartListAdapter.checkForShipmentForm();
        }
    }

    @Override
    public void renderCancelAutoApplyCouponError() {
        NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.default_request_error_unknown));
    }

    @Override
    public void sendAnalyticsOnSuccessToShipment(Map<String, Object> stringObjectMap) {
        cartPageAnalytics.enhancedECommerceGoToCheckoutStep1(stringObjectMap);
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

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_url());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        Intent intent = ((TransactionRouter) getActivity().getApplication()).getShopPageIntent(
                getActivity(), shop.getId());
        startActivity(intent);
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
        showMainContainer();
        cartListAdapter.resetData();
        dPresenter.processInitialGetCartData();
        String promo = BranchSdkUtils.getAutoApplyCouponIfAvailable(getActivity());
        if (!TextUtils.isEmpty(promo)) {
            dPresenter.processCheckPromoCodeFromSuggestedPromo(promo, true);
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
            case REQUEST_CODE_ROUTE_WISHLIST:
                onResultFromRequestCodeWishlist();
                break;
        }
    }

    private void onResultFromRequestCodeWishlist() {
        refreshHandler.startRefresh();
    }

    public CheckoutAnalyticsCart getCartPageAnalytics() {
        return cartPageAnalytics;
    }

    private void onResultFromRequestCodeCartShipment(int resultCode, Intent data) {
        if (resultCode == ShipmentActivity.RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT ||
                resultCode == ShipmentActivity.RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT) {
            dPresenter.processResetAndRefreshCartData();
        } else if (resultCode == TopPayActivity.PAYMENT_CANCELLED) {
            NetworkErrorHelper.showSnackbar(
                    getActivity(),
                    getString(R.string.alert_payment_canceled_or_failed_transaction_module)
            );
            dPresenter.processResetAndRefreshCartData();
        } else if (resultCode == TopPayActivity.PAYMENT_SUCCESS) {
            showToastMessage(getString(R.string.message_payment_success));
            startActivity(TransactionPurchaseRouter.createIntentTxSummary(getActivity()));
            CartBadgeNotificationReceiver.resetBadgeCart(getActivity());
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

                cartListAdapter.updateItemPromoVoucher(cartItemPromoHolderData);
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

                cartListAdapter.updateItemPromoVoucher(cartItemPromoHolderData);
            }
        }
    }

    public interface ActionListener {

        void onRemoveAllCartMenuClicked(List<CartItemData> cartItemData);

    }

    @Override
    protected String getScreenName() {
        return ConstantTransactionAnalytics.ScreenName.CART;
    }

    @Override
    public void onStart() {
        super.onStart();
        cartPageAnalytics.sendScreenName(getActivity(), getScreenName());
    }
}
