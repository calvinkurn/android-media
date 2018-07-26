package com.tokopedia.checkout.view.view.shipment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.view.addressoptions.CartAddressChoiceActivity;
import com.tokopedia.checkout.view.view.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.view.multipleaddressform.MultipleAddressFormActivity;
import com.tokopedia.checkout.view.view.shipment.converter.RatesDataConverter;
import com.tokopedia.checkout.view.view.shipment.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.view.shipment.di.DaggerShipmentComponent;
import com.tokopedia.checkout.view.view.shipment.di.ShipmentComponent;
import com.tokopedia.checkout.view.view.shipment.di.ShipmentModule;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.checkout.view.view.shippingoptions.CourierBottomsheet;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.transactiondata.entity.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Irfan Khoirul on 23/04/18.
 * Originaly authored by Aghny, Angga, Kris
 */

public class ShipmentFragment extends BaseCheckoutFragment implements ShipmentContract.View,
        ShipmentAdapterActionListener, CourierBottomsheet.ActionListener {

    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;
    private static final int REQUEST_CODE_COURIER_PINPOINT = 13;
    private static final int REQUEST_CODE_SEND_TO_MULTIPLE_ADDRESS = 55;
    public static final String ARG_EXTRA_SHIPMENT_FORM_DATA = "ARG_EXTRA_SHIPMENT_FORM_DATA";
    public static final String ARG_EXTRA_CART_PROMO_SUGGESTION = "ARG_EXTRA_CART_PROMO_SUGGESTION";
    public static final String ARG_EXTRA_PROMO_CODE_APPLIED_DATA = "ARG_EXTRA_PROMO_CODE_APPLIED_DATA";
    public static final String ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO = "ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO";
    private static final String NO_PINPOINT_ETD = "Belum Pinpoint";
    private static final String EXTRA_STATE_SHIPMENT_SELECTION = "EXTRA_STATE_SHIPMENT_SELECTION";
    private static final String DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION = "LAST_CHOOSE_COURIER_ITEM_POSITION";

    private RecyclerView rvShipment;
    private SwipeToRefresh swipeToRefresh;
    private LinearLayout llNetworkErrorView;
    private LinearLayout llCheckoutButtonContainer;
    private TextView tvTotalPayment;
    private TextView tvSelectPaymentMethod;
    private TkpdProgressDialog progressDialogNormal;
    private CourierBottomsheet courierBottomsheet;

    @Inject
    ShipmentAdapter shipmentAdapter;
    @Inject
    ShipmentContract.Presenter shipmentPresenter;
    @Inject
    ShipmentDataConverter shipmentDataConverter;
    @Inject
    RatesDataConverter ratesDataConverter;
    @Inject
    CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection;

    private HashSet<ShipmentSelectionStateData> shipmentSelectionStateDataHashSet = new HashSet<>();

    public static ShipmentFragment newInstance(PromoCodeAppliedData promoCodeAppliedData,
                                               CartPromoSuggestion cartPromoSuggestionData,
                                               String defaultSelectedTabPromo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestionData);
        bundle.putParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA, promoCodeAppliedData);
        bundle.putString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, defaultSelectedTabPromo);
        ShipmentFragment shipmentFragment = new ShipmentFragment();
        shipmentFragment.setArguments(bundle);

        return shipmentFragment;
    }

    @Override
    protected void initInjector() {
        ShipmentComponent component = DaggerShipmentComponent.builder()
                .cartComponent(getComponent(CartComponent.class))
                .shipmentModule(new ShipmentModule(this))
                .trackingAnalyticsModule(new TrackingAnalyticsModule())
                .build();
        component.inject(this);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shipmentPresenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shipmentPresenter.detachView();
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
        if (!shipmentSelectionStateDataHashSet.isEmpty()) {
            state.putParcelableArrayList(
                    EXTRA_STATE_SHIPMENT_SELECTION, new ArrayList<>(shipmentSelectionStateDataHashSet
                    ));
        }
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        ArrayList<ShipmentSelectionStateData> shipmentSelectionStateData =
                savedState.getParcelableArrayList(EXTRA_STATE_SHIPMENT_SELECTION);
        if (shipmentSelectionStateData != null) {
            shipmentSelectionStateDataHashSet = new HashSet<>(shipmentSelectionStateData);
        }
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
        initializePresenterData(arguments);
    }

    private void initializePresenterData(Bundle arguments) {
        PromoCodeAppliedData promoCodeAppliedData = arguments.getParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA);
        shipmentPresenter.setPromoCodeAppliedData(promoCodeAppliedData);
        shipmentPresenter.setCartPromoSuggestion(arguments.getParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION));
        shipmentPresenter.setShipmentCostModel(new ShipmentCostModel());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment;
    }

    @Override
    protected void initView(View view) {
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        rvShipment = view.findViewById(R.id.rv_shipment);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        llCheckoutButtonContainer = view.findViewById(R.id.ll_checkout_button_container);
        tvTotalPayment = view.findViewById(R.id.tv_total_payment);
        tvSelectPaymentMethod = view.findViewById(R.id.tv_select_payment_method);
        progressDialogNormal = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        ((SimpleItemAnimator) rvShipment.getItemAnimator()).setSupportsChangeAnimations(false);

        tvSelectPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipmentAdapter.checkDropshipperValidation();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            shipmentPresenter.processInitialLoadCheckoutPage(false);
        } else {
            swipeToRefresh.setEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PromoCodeAppliedData.class.getSimpleName(), shipmentPresenter.getPromoCodeAppliedData());
        outState.putParcelable(CartPromoSuggestion.class.getSimpleName(), shipmentPresenter.getCartPromoSuggestion());
        outState.putParcelable(RecipientAddressModel.class.getSimpleName(), shipmentPresenter.getRecipientAddressModel());
        outState.putParcelableArrayList(ShipmentCartItemModel.class.getSimpleName(), new ArrayList<>(shipmentPresenter.getShipmentCartItemModelList()));
        outState.putParcelable(ShipmentDonationModel.class.getSimpleName(), shipmentPresenter.getShipmentDonationModel());
        outState.putParcelable(ShipmentCostModel.class.getSimpleName(), shipmentPresenter.getShipmentCostModel());
        outState.putInt(DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION, shipmentAdapter.getLastChooseCourierItemPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            shipmentPresenter.setPromoCodeAppliedData(savedInstanceState.getParcelable(PromoCodeAppliedData.class.getSimpleName()));
            shipmentPresenter.setCartPromoSuggestion(savedInstanceState.getParcelable(CartPromoSuggestion.class.getSimpleName()));
            shipmentPresenter.setRecipientAddressModel(savedInstanceState.getParcelable(RecipientAddressModel.class.getSimpleName()));
            shipmentPresenter.setShipmentCartItemModelList(savedInstanceState.getParcelableArrayList(ShipmentCartItemModel.class.getSimpleName()));
            shipmentPresenter.setShipmentDonationModel(savedInstanceState.getParcelable(ShipmentDonationModel.class.getSimpleName()));
            shipmentPresenter.setShipmentCostModel(savedInstanceState.getParcelable(ShipmentCostModel.class.getSimpleName()));
            shipmentAdapter.setLastChooseCourierItemPosition(savedInstanceState.getInt(DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION));
            renderCheckoutPage(true);
        }
    }

    @Override
    protected void setViewListener() {

    }

    private void initRecyclerViewData(PromoCodeAppliedData promoCodeAppliedData,
                                      CartPromoSuggestion cartPromoSuggestion,
                                      RecipientAddressModel recipientAddressModel,
                                      List<ShipmentCartItemModel> shipmentCartItemModelList,
                                      ShipmentDonationModel shipmentDonationModel,
                                      ShipmentCostModel shipmentCostModel,
                                      boolean isInitialRender) {
        shipmentAdapter.clearData();
        rvShipment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvShipment.setAdapter(shipmentAdapter);
        if (isInitialRender) {
            rvShipment.addItemDecoration(
                    new CartItemDecoration((int) getResources().getDimension(R.dimen.dp_4), false, 0));
        }
        CartItemPromoHolderData cartItemPromoHolderData =
                CartItemPromoHolderData.createInstanceFromAppliedPromo(promoCodeAppliedData);
        if (getArguments() != null) {
            cartItemPromoHolderData.setDefaultSelectedTabString(
                    getArguments().getString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, "")
            );
        }
        shipmentAdapter.addPromoVoucherData(cartItemPromoHolderData);
        shipmentPresenter.setCartItemPromoHolderData(cartItemPromoHolderData);
        if (promoCodeAppliedData != null) {
            cartPromoSuggestion.setVisible(false);
            shipmentAdapter.addPromoSuggestionData(cartPromoSuggestion);
        }
        if (recipientAddressModel != null) {
            shipmentAdapter.addAddressShipmentData(recipientAddressModel);
        }
        shipmentAdapter.addCartItemDataList(shipmentCartItemModelList);
        shipmentAdapter.addShipmentDonationModel(shipmentDonationModel);
        shipmentAdapter.addShipmentCostData(shipmentCostModel);
        shipmentAdapter.updateShipmentSellerCashbackVisibility();
        if (isInitialRender) {
            if (!shipmentSelectionStateDataHashSet.isEmpty()) {
                for (ShipmentSelectionStateData shipmentSelectionStateData : shipmentSelectionStateDataHashSet) {
                    if (!shipmentSelectionStateData.getCourierItemData().getEstimatedTimeDelivery().equalsIgnoreCase(NO_PINPOINT_ETD)
                            && shipmentSelectionStateData.getPosition() < shipmentAdapter.getItemCount()) {
                        shipmentAdapter.setSelectedCourier(shipmentSelectionStateData.getPosition(),
                                shipmentSelectionStateData.getCourierItemData());
                    }
                }
            }
        }
        llCheckoutButtonContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showInitialLoading() {
        swipeToRefresh.setRefreshing(true);
    }

    @Override
    public void hideInitialLoading() {
        swipeToRefresh.setRefreshing(false);
        swipeToRefresh.setEnabled(false);
    }

    @Override
    public void showLoading() {
        if (!progressDialogNormal.isProgress()) progressDialogNormal.showDialog();
    }

    @Override
    public void hideLoading() {
        if (progressDialogNormal.isProgress()) progressDialogNormal.dismiss();
        swipeToRefresh.setEnabled(false);
    }

    @Override
    public void showToastNormal(String message) {
        if (getView() != null && getActivity() != null) {
            ToasterNormal.make(getView(), message, BaseToaster.LENGTH_SHORT)
                    .setAction(getActivity().getString(R.string.label_action_snackbar_close), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
        }
    }

    @Override
    public void showToastError(String message) {
        if (getView() != null && getActivity() != null) {
            if (TextUtils.isEmpty(message)) {
                message = getActivity().getString(R.string.default_request_error_unknown);
            }
            if (shipmentAdapter == null || shipmentAdapter.getItemCount() == 0) {
                renderErrorPage(message);
            } else {
                Snackbar snackbar = Snackbar.make(getView(), message, BaseToaster.LENGTH_SHORT);
                TextView snackbarTextView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                Button snackbarActionButton = snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
                snackbar.getView().setBackground(ContextCompat.getDrawable(getView().getContext(), com.tokopedia.design.R.drawable.bg_snackbar_error));
                snackbarTextView.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.font_black_secondary_54));
                snackbarActionButton.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.font_black_primary_70));
                snackbarTextView.setMaxLines(5);
                snackbar.setAction(getActivity().getString(R.string.label_action_snackbar_close), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
            }
        }
    }

    @Override
    public void renderErrorPage(String message) {
        rvShipment.setVisibility(View.GONE);
        llCheckoutButtonContainer.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        llNetworkErrorView.setVisibility(View.GONE);
                        rvShipment.setVisibility(View.VISIBLE);
                        llCheckoutButtonContainer.setVisibility(View.VISIBLE);
                        shipmentPresenter.processInitialLoadCheckoutPage(false);
                    }
                });

    }

    @Override
    public void renderCheckoutPage(boolean isInitialRender) {
        if (getArguments() != null) {
            initializePresenterData(getArguments());
        }
        PromoCodeAppliedData promoCodeAppliedData = shipmentPresenter.getPromoCodeAppliedData();
        CartPromoSuggestion cartPromoSuggestion = shipmentPresenter.getCartPromoSuggestion();
        RecipientAddressModel recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
        List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentPresenter.getShipmentCartItemModelList();
        ShipmentDonationModel shipmentDonationModel = shipmentPresenter.getShipmentDonationModel();
        ShipmentCostModel shipmentCostModel = shipmentPresenter.getShipmentCostModel();

        initRecyclerViewData(
                promoCodeAppliedData, cartPromoSuggestion, recipientAddressModel,
                shipmentCartItemModelList, shipmentDonationModel, shipmentCostModel, isInitialRender
        );
    }

    @Override
    public void renderCheckShipmentPrepareCheckoutSuccess() {
        shipmentPresenter.processCheckout();
    }

    @Override
    public void renderErrorDataHasChangedCheckShipmentPrepareCheckout(
            CartShipmentAddressFormData cartShipmentAddressFormData, boolean needToRefreshItemList
    ) {
        if (getActivity() != null && needToRefreshItemList) {
            showToastError(getActivity().getString(R.string.error_message_checkout_failed));
        }
        shipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void renderErrorDataHasChangedAfterCheckout(List<ShipmentCartItemModel> oldShipmentCartItemModelList) {
        if (getActivity() != null) {
            showToastError(getActivity().getString(R.string.error_message_checkout_failed));
        }
        initRecyclerViewData(shipmentPresenter.getPromoCodeAppliedData(),
                shipmentPresenter.getCartPromoSuggestion(),
                shipmentPresenter.getRecipientAddressModel(),
                oldShipmentCartItemModelList,
                shipmentPresenter.getShipmentDonationModel(),
                shipmentPresenter.getShipmentCostModel(),
                false
        );
    }

    @Override
    public void renderDataChangedFromMultipleAddress(CartShipmentAddressFormData cartShipmentAddressFormData) {
        if (getArguments() != null) {
            initializePresenterData(getArguments());
        }
        PromoCodeAppliedData promoCodeAppliedData = shipmentPresenter.getPromoCodeAppliedData();
        CartPromoSuggestion cartPromoSuggestion = shipmentPresenter.getCartPromoSuggestion();
        RecipientAddressModel recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
        List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentPresenter.getShipmentCartItemModelList();
        ShipmentDonationModel shipmentDonationModel = shipmentPresenter.getShipmentDonationModel();
        ShipmentCostModel shipmentCostModel = shipmentPresenter.getShipmentCostModel();

        initRecyclerViewData(
                promoCodeAppliedData, cartPromoSuggestion, recipientAddressModel,
                shipmentCartItemModelList, shipmentDonationModel, shipmentCostModel, false
        );
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
    public void renderThanksTopPaySuccess(String message) {
        showToastNormal(getString(R.string.message_payment_succeded_transaction_module));
        startActivity(TransactionPurchaseRouter.createIntentTxSummary(getActivity()));
        if (getActivity() != null) {
            CartBadgeNotificationReceiver.resetBadgeCart(getActivity());
            getActivity().finish();
        }
    }

    @Override
    public void renderCheckoutCartSuccess(CheckoutData checkoutData) {
        shipmentPresenter.setCheckoutData(checkoutData);
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.setRedirectUrl(checkoutData.getRedirectUrl());
        paymentPassData.setTransactionId(checkoutData.getTransactionId());
        paymentPassData.setPaymentId(checkoutData.getPaymentId());
        paymentPassData.setCallbackSuccessUrl(checkoutData.getCallbackSuccessUrl());
        paymentPassData.setCallbackFailedUrl(checkoutData.getCallbackFailedUrl());
        paymentPassData.setQueryString(checkoutData.getQueryString());
        startActivityForResult(
                TopPayActivity.createInstance(getActivity(), paymentPassData),
                TopPayActivity.REQUEST_CODE);
    }

    @Override
    public void renderCheckoutCartError(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void sendAnalyticsChoosePaymentMethodSuccess() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickPilihMetodePembayaranSuccess();
    }

    @Override
    public void sendAnalyticsChoosePaymentMethodFailed() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickPilihMetodePembayaranNotSuccess();
    }

    @Override
    public void sendAnalyticsChoosePaymentMethodCourierNotComplete() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickPilihMetodePembayaranCourierNotComplete();
    }

    @Override
    public void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData
                                                                      promoCodeCartListData) {
        shipmentPresenter.setPromoCodeAppliedData(new PromoCodeAppliedData.Builder()
                .typeVoucher(PromoCodeAppliedData.TYPE_VOUCHER)
                .promoCode(promoCodeCartListData.getDataVoucher().getCode())
                .description(promoCodeCartListData.getDataVoucher().getMessageSuccess())
                .amount(promoCodeCartListData.getDataVoucher().getCashbackAmount())
                .build()
        );
        CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
        if (getArguments() != null) {
            cartItemPromoHolderData.setDefaultSelectedTabString(
                    getArguments().getString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, "")
            );
        }
        PromoCodeAppliedData promoCodeAppliedData = shipmentPresenter.getPromoCodeAppliedData();
        cartItemPromoHolderData.setPromoVoucherType(promoCodeAppliedData.getPromoCode(),
                promoCodeAppliedData.getDescription(), promoCodeAppliedData.getAmount());
        updateAppliedPromo(cartItemPromoHolderData);
    }

    @Override
    public void renderErrorCheckPromoCodeFromSuggestedPromo(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showRedCloseSnackbar(view, message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void renderErrorCheckPromoShipmentData(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
        shipmentAdapter.updatePromo(null);
        shipmentPresenter.setPromoCodeAppliedData(null);
    }

    @Override
    public void renderCheckPromoShipmentDataSuccess(PromoCodeCartShipmentData
                                                            checkPromoCodeCartShipmentResult) {
        shipmentAdapter.updatePromo(checkPromoCodeCartShipmentResult.getDataVoucher());
    }

    @Override
    public void renderEditAddressSuccess(String latitude, String longitude) {
        shipmentAdapter.updateShipmentDestinationPinpoint(Double.parseDouble(latitude),
                Double.parseDouble(longitude));
        courierBottomsheet = null;
        int position = shipmentAdapter.getLastChooseCourierItemPosition();
        ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(position);
        if (shipmentCartItemModel != null) {
            onChooseShipment(position, shipmentCartItemModel, shipmentPresenter.getRecipientAddressModel());
        }
    }

    @Override
    public void renderCancelAutoApplyCouponSuccess() {
        onRemovePromoCode();
        shipmentAdapter.cancelAutoApplyCoupon();
        shipmentAdapter.notifyItemChanged(shipmentAdapter.getShipmentCostPosition());
    }

    @Override
    public void navigateToSetPinpoint(String message, LocationPass locationPass) {
        if (getView() != null) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_rectangle, getView().findViewById(R.id.toast_layout));

            TextView tvMessage = layout.findViewById(R.id.tv_message);
            tvMessage.setText(message);

            Toast toast = new Toast(getActivity());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
        if (getActivity() != null) {
            Intent intent = GeolocationActivity.createInstance(getActivity(), locationPass);
            startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT);
        }
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    @Override
    public void renderChangeAddressSuccess(RecipientAddressModel selectedAddress) {
        shipmentPresenter.setRecipientAddressModel(selectedAddress);
        shipmentAdapter.updateSelectedAddress(selectedAddress);
        courierBottomsheet = null;
        onCartDataDisableToCheckout();
    }

    @Override
    public List<DataCheckoutRequest> generateNewCheckoutRequest
            (List<ShipmentCartItemModel> shipmentCartItemModelList) {
        ShipmentAdapter.RequestData requestData = shipmentAdapter.getRequestData(null, shipmentCartItemModelList);
        return requestData.getCheckoutRequestData();
    }

    @Override
    public ShipmentDataConverter getShipmentDataConverter() {
        return shipmentDataConverter;
    }

    private void updateAppliedPromo(CartItemPromoHolderData cartPromo) {
        shipmentAdapter.updateItemPromoVoucher(cartPromo);
        if (shipmentAdapter.hasSetAllCourier()) {
            ShipmentAdapter.RequestData requestData =
                    shipmentAdapter.getRequestData(null, null);
            shipmentPresenter.setPromoCodeCartShipmentRequestData(requestData.getPromoRequestData());
            shipmentPresenter.checkPromoShipment();

            shipmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TopPayActivity.REQUEST_CODE) {
            onResultFromPayment(resultCode);
        } else if ((requestCode == REQUEST_CHOOSE_PICKUP_POINT)
                && resultCode == Activity.RESULT_OK) {
            onResultFromRequestCodeCourierOptions(requestCode, data);
        } else if (requestCode == CartAddressChoiceActivity.REQUEST_CODE) {
            onResultFromRequestCodeAddressOptions(resultCode, data);
        } else if (requestCode == IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE) {
            onResultFromRequestCodeLoyalty(resultCode, data);
        } else if (requestCode == REQUEST_CODE_COURIER_PINPOINT) {
            onResultFromCourierPinpoint(resultCode, data);
        } else if (requestCode == REQUEST_CODE_SEND_TO_MULTIPLE_ADDRESS) {
            onResultFromMultipleAddress(resultCode, data);
        }
    }

    private void onResultFromMultipleAddress(int resultCode, Intent data) {
        if (resultCode == MultipleAddressFormActivity.RESULT_CODE_RELOAD_CART_PAGE) {
            if (getActivity() != null) {
                getActivity().setResult(ShipmentActivity.RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT);
                getActivity().finish();
            }
        } else if (data != null) {
            CartItemPromoHolderData cartItemPromoHolderData = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_PROMO_DATA);
            CartPromoSuggestion cartPromoSuggestion = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_PROMO_SUGGESTION_DATA);
            RecipientAddressModel recipientAddressModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_RECIPIENT_ADDRESS_DATA);
            ArrayList<ShipmentCartItemModel> shipmentCartItemModels = data.getParcelableArrayListExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_CART_TEM_LIST_DATA);
            ShipmentCostModel shipmentCostModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_COST_SATA);
            ShipmentDonationModel shipmentDonationModel = data.getParcelableExtra(MultipleAddressFormActivity.EXTRA_SHIPMENT_DONATION_DATA);
            shipmentPresenter.processReloadCheckoutPageFromMultipleAddress(
                    cartItemPromoHolderData, cartPromoSuggestion, recipientAddressModel, shipmentCartItemModels,
                    shipmentCostModel, shipmentDonationModel
            );
        } else {
            shipmentSelectionStateDataHashSet.clear();
            shipmentPresenter.processInitialLoadCheckoutPage(true);
        }
    }

    private void onResultFromCourierPinpoint(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null) {
            LocationPass locationPass = data.getExtras().getParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION);
            if (locationPass != null) {
                int index = shipmentAdapter.getLastChooseCourierItemPosition();
                ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(index);
                shipmentPresenter.editAddressPinpoint(locationPass.getLatitude(), locationPass.getLongitude(),
                        shipmentCartItemModel, locationPass);
            }
        }
    }

    private void onResultFromPayment(int resultCode) {
        if (getActivity() != null) {
            getActivity().setResult(resultCode);
            getActivity().finish();
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
                shipmentPresenter.setPromoCodeAppliedData(new PromoCodeAppliedData.Builder()
                        .typeVoucher(PromoCodeAppliedData.TYPE_VOUCHER)
                        .promoCode(voucherCode)
                        .description(voucherMessage)
                        .amount((int) voucherDiscountAmount)
                        .build()
                );
                CartItemPromoHolderData cartPromo = new CartItemPromoHolderData();
                if (getArguments() != null) {
                    cartPromo.setDefaultSelectedTabString(getArguments().getString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, ""));
                }
                cartPromo.setPromoVoucherType(voucherCode, voucherMessage, voucherDiscountAmount);

                updateAppliedPromo(cartPromo);
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
                shipmentPresenter.setPromoCodeAppliedData(new PromoCodeAppliedData.Builder()
                        .typeVoucher(PromoCodeAppliedData.TYPE_COUPON)
                        .promoCode(couponCode)
                        .couponTitle(couponTitle)
                        .description(couponMessage)
                        .amount((int) couponDiscountAmount)
                        .build()
                );
                CartItemPromoHolderData cartPromo = new CartItemPromoHolderData();
                if (getArguments() != null) {
                    cartPromo.setDefaultSelectedTabString(getArguments().getString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, ""));
                }
                cartPromo.setPromoCouponType(
                        couponTitle, couponCode, couponMessage, couponDiscountAmount
                );

                updateAppliedPromo(cartPromo);
            }
        }
    }

    private void onResultFromRequestCodeCourierOptions(int requestCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSE_PICKUP_POINT:
//                Store pickupBooth = data.getParcelableExtra(INTENT_DATA_STORE);
//                shipmentAdapter.setPickupPoint(pickupBooth);
                break;
            default:
                break;
        }
    }

    private void onResultFromRequestCodeAddressOptions(int resultCode, Intent data) {
        switch (resultCode) {
            case CartAddressChoiceActivity.RESULT_CODE_ACTION_SELECT_ADDRESS:
                RecipientAddressModel currentAddress = shipmentAdapter.getAddressShipmentData();
                RecipientAddressModel newAddress = data.getParcelableExtra(
                        CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA);

                if (currentAddress != null && newAddress != null) {
                    if (!currentAddress.getId().equals(newAddress.getId()) ||
                            !currentAddress.getAddressName().equals(newAddress.getAddressName()) ||
                            !currentAddress.getStreet().equals(newAddress.getStreet()) ||
                            !currentAddress.getRecipientName().equals(newAddress.getRecipientName()) ||
                            !currentAddress.getRecipientPhoneNumber().equals(newAddress.getRecipientPhoneNumber()) ||
                            !String.valueOf(currentAddress.getLatitude()).equals(String.valueOf(newAddress.getLatitude())) ||
                            !String.valueOf(currentAddress.getLongitude()).equals(String.valueOf(newAddress.getLongitude())) ||
                            !currentAddress.getPostalCode().equals(newAddress.getPostalCode()) ||
                            !currentAddress.getDestinationDistrictId().equals(newAddress.getDestinationDistrictId()) ||
                            !currentAddress.getCityId().equals(newAddress.getCityId()) ||
                            !currentAddress.getProvinceId().equals(newAddress.getProvinceId())) {
                        shipmentPresenter.setDataChangeAddressRequestList(shipmentAdapter.getRequestData(newAddress, null).getChangeAddressRequestData());
                        shipmentPresenter.changeShippingAddress(newAddress);
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onChangeAddress() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickGantiAlamatAtauKirimKeBeberapaAlamat();
        Intent intent = CartAddressChoiceActivity.createInstance(getActivity(),
                shipmentPresenter.getRecipientAddressModel(),
                CartAddressChoiceActivity.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST);
        startActivityForResult(intent, CartAddressChoiceActivity.REQUEST_CODE);
    }

    @Override
    public void onSendToMultipleAddress(RecipientAddressModel recipientAddressModel) {
        Intent intent = MultipleAddressFormActivity.createInstance(getActivity(),
                shipmentPresenter.getCartItemPromoHolderData(),
                shipmentPresenter.getCartPromoSuggestion(),
                shipmentPresenter.getRecipientAddressModel(),
                shipmentPresenter.getShipmentCartItemModelList(),
                shipmentPresenter.getShipmentCostModel(),
                shipmentPresenter.getShipmentDonationModel()
//                shipmentPresenter.getShipmentCheckoutButtonModel()
        );
        startActivityForResult(intent, REQUEST_CODE_SEND_TO_MULTIPLE_ADDRESS);
    }

    @Override
    public void resetTotalPrice() {
        shipmentAdapter.updateCheckoutButtonData("-");
    }

    @Override
    public void onChooseShipment(int position, ShipmentCartItemModel shipmentCartItemModel,
                                 RecipientAddressModel recipientAddressModel) {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickSelectCourier();
        ShipmentDetailData shipmentDetailData;
        ShipmentDetailData oldShipmentDetailData = null;
        if (recipientAddressModel == null) {
            recipientAddressModel = shipmentCartItemModel.getRecipientAddressModel();
        }
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
            oldShipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        }
        shipmentDetailData = ratesDataConverter.getShipmentDetailData(shipmentCartItemModel,
                recipientAddressModel);
        if (oldShipmentDetailData != null && oldShipmentDetailData.getSelectedCourier() != null) {
            shipmentDetailData.setSelectedCourier(oldShipmentDetailData.getSelectedCourier());
        }
        if (shipmentDetailData != null) {
            showCourierChoiceBottomSheet(shipmentDetailData, shipmentCartItemModel.getShopShipmentList(),
                    recipientAddressModel, position);
        }
    }

    private void showCourierChoiceBottomSheet(ShipmentDetailData shipmentDetailData,
                                              List<ShopShipment> shopShipmentList,
                                              RecipientAddressModel recipientAddressModel,
                                              int position) {
        if (courierBottomsheet == null || position != courierBottomsheet.getLastCartItemPosition() ||
                shipmentDetailData.getSelectedCourier() == null) {
            courierBottomsheet = new CourierBottomsheet(getActivity(), shipmentDetailData,
                    shopShipmentList, recipientAddressModel, position);
        }
        if (shipmentDetailData.getSelectedCourier() != null) {
            courierBottomsheet.setSelectedCourier(shipmentDetailData.getSelectedCourier());
        }
        courierBottomsheet.setListener(this);
        courierBottomsheet.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                checkoutAnalyticsCourierSelection.sendScreenName(
                        getActivity(), ConstantTransactionAnalytics.ScreenName.SELECT_COURIER
                );
                courierBottomsheet.updateHeight();
                checkoutAnalyticsCourierSelection.eventImpressionCourierSelectionImpressionCourierSelection();
            }
        });
        courierBottomsheet.show();
    }

    @Override
    public void onChoosePickupPoint(RecipientAddressModel addressAdapterData) {

    }

    @Override
    public void onClearPickupPoint(RecipientAddressModel addressAdapterData) {

    }

    @Override
    public void onEditPickupPoint(RecipientAddressModel addressAdapterData) {

    }

    @Override
    public void onTotalPaymentChange(String totalPayment) {
        tvTotalPayment.setText(totalPayment);
    }

    @Override
    public void onFinishChoosingShipment
            (List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData,
             List<DataCheckoutRequest> checkoutRequestData) {
        shipmentPresenter.setPromoCodeCartShipmentRequestData(promoRequestData);
        shipmentPresenter.setDataCheckoutRequestList(checkoutRequestData);
        if (shipmentPresenter.getPromoCodeAppliedData() != null &&
                shipmentAdapter.hasAppliedPromoCode()) {
            shipmentPresenter.checkPromoShipment();
        }
    }

    @Override
    public void onRemovePromoCode() {
        shipmentAdapter.updatePromo(null);
        shipmentPresenter.setPromoCodeAppliedData(null);
    }

    @Override
    public void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion,
                                                   int position) {
        shipmentPresenter.processCheckPromoCodeFromSuggestedPromo(cartPromoSuggestion.getPromoCode());
    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion,
                                                        int position) {
        cartPromoSuggestion.setVisible(false);
        shipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartPromo,
                                                  int position) {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickGunakanKodePromoAtauKupon();
        if (getActivity().getApplication() instanceof ICheckoutModuleRouter) {
            startActivityForResult(
                    ((ICheckoutModuleRouter) getActivity().getApplication())
                            .checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
                                    getActivity(), true, "",
                                    cartPromo.getDefaultSelectedTabString()
                            ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
            );
        }
    }

    @Override
    public void onCartPromoCancelVoucherPromoClicked(CartItemPromoHolderData cartPromo,
                                                     int position) {
        shipmentPresenter.cancelAutoApplyCoupon();
    }

    @Override
    public void onCartPromoTrackingSuccess(CartItemPromoHolderData cartPromo, int position) {

    }

    @Override
    public void onCartPromoTrackingCancelled(CartItemPromoHolderData cartPromo, int position) {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickXOnBannerPromoCodeCode();
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickXFromGunakanKodePromoAtauKupon();
    }

    @Override
    public void onCartDataEnableToCheckout() {
        shipmentAdapter.updateCheckoutButtonData(null);
    }

    @Override
    public void onCartDataDisableToCheckout() {
        shipmentAdapter.updateCheckoutButtonData(null);
    }

    @Override
    public void onDropshipperValidationResult(boolean result, ShipmentData shipmentData,
                                              int errorPosition) {
        if (shipmentData == null && result) {
            shipmentPresenter.processCheckShipmentPrepareCheckout();
        } else if (shipmentData != null && !result) {
            if (errorPosition != ShipmentAdapter.DEFAULT_ERROR_POSITION) {
                rvShipment.smoothScrollToPosition(errorPosition);
            }
            showToastError(getActivity().getString(R.string.message_error_courier_not_selected_or_dropshipper_empty));
            onCartDataDisableToCheckout();
            ((ShipmentCartItemModel) shipmentData).setStateDropshipperHasError(true);
            shipmentAdapter.notifyItemChanged(errorPosition);
        } else if (shipmentData == null) {
            showToastError(getActivity().getString(R.string.message_error_courier_not_selected_or_dropshipper_empty));
        }
    }

    @Override
    public void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data,
                                                   int position) {

    }

    @Override
    public void onShipmentItemClick(CourierItemData courierItemData,
                                    RecipientAddressModel recipientAddressModel,
                                    int cartItemPosition) {
        checkoutAnalyticsCourierSelection.eventViewCourierSelectionClickCourierOption(
                courierItemData.getName(),
                courierItemData.getShipmentItemDataType()
        );
        ShipmentSelectionStateData shipmentSelectionStateData = new ShipmentSelectionStateData();
        shipmentSelectionStateData.setPosition(cartItemPosition);
        shipmentSelectionStateData.setCourierItemData(courierItemData);
        shipmentSelectionStateDataHashSet.add(shipmentSelectionStateData);
        if (courierItemData.isUsePinPoint() && (recipientAddressModel.getLatitude() == null ||
                recipientAddressModel.getLatitude() == 0 || recipientAddressModel.getLongitude() == null ||
                recipientAddressModel.getLongitude() == 0)) {
            shipmentAdapter.setLastChooseCourierItemPosition(cartItemPosition);
            LocationPass locationPass = new LocationPass();
            if (shipmentAdapter.getAddressShipmentData() != null) {
                locationPass.setCityName(shipmentAdapter.getAddressShipmentData().getCityName());
                locationPass.setDistrictName(shipmentAdapter.getAddressShipmentData().getDestinationDistrictName());
            } else {
                ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(cartItemPosition);
                if (shipmentCartItemModel != null) {
                    RecipientAddressModel updatedRecipientAddressModel = shipmentCartItemModel.getRecipientAddressModel();
                    if (updatedRecipientAddressModel != null) {
                        locationPass.setCityName(updatedRecipientAddressModel.getCityName());
                        locationPass.setDistrictName(updatedRecipientAddressModel.getDestinationDistrictName());
                    }
                }
            }
            Intent intent = GeolocationActivity.createInstance(getActivity(), locationPass);
            startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT);
        } else {
            shipmentAdapter.setSelectedCourier(cartItemPosition, courierItemData);
        }
    }

    @Override
    public void onInsuranceChecked(final int position) {
        if (rvShipment.isComputingLayout()) {
            rvShipment.post(new Runnable() {
                @Override
                public void run() {
                    shipmentAdapter.updateShipmentCostModel();
                    shipmentAdapter.updateItemAndTotalCost(position);
                    shipmentAdapter.updateInsuranceTncVisibility();
                }
            });
        } else {
            shipmentAdapter.updateShipmentCostModel();
            shipmentAdapter.updateItemAndTotalCost(position);
            shipmentAdapter.updateInsuranceTncVisibility();
        }
    }

//    private void checkRecyclerViewFillScreen() {
//        boolean isPageFilledWithItems = rvShipment.computeVerticalScrollRange() > rvShipment.getHeight();
//        if (shipmentPresenter.getShipmentCheckoutButtonModel() != null) {
//            shipmentPresenter.getShipmentCheckoutButtonModel().setHideBottomShadow(isPageFilledWithItems);
//        }
//    }

    @Override
    public void onNeedUpdateViewItem(final int position) {
        if (rvShipment.isComputingLayout()) {
            rvShipment.post(new Runnable() {
                @Override
                public void run() {
                    shipmentAdapter.notifyItemChanged(position);
//                    checkRecyclerViewFillScreen();
                }
            });
        } else {
            shipmentAdapter.notifyItemChanged(position);
//            checkRecyclerViewFillScreen();
        }
    }

    @Override
    public void onSubTotalCartItemClicked(int position) {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickSubtotal();
    }

    @Override
    public void onInsuranceTncClicked() {
        startActivity(((ICheckoutModuleRouter) getActivity().getApplication()).checkoutModuleRouterGetInsuranceTncActivityIntent());
    }

    @Override
    public void onNeedUpdateRequestData() {
        shipmentAdapter.checkHasSelectAllCourier();
    }

    @Override
    public void onDropshipCheckedForTrackingAnalytics() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickDropship();
    }

    @Override
    public void onInsuranceCheckedForTrackingAnalytics() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickAsuransiPengiriman();
    }

    @Override
    public void onChoosePaymentMethodButtonClicked() {
        shipmentAdapter.checkDropshipperValidation();
    }

    @Override
    public void onDonationChecked(boolean checked) {
        shipmentAdapter.updateDonation(checked);
    }

    @Override
    protected String getScreenName() {
        return ConstantTransactionAnalytics.ScreenName.CHECKOUT;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkoutAnalyticsCourierSelection.sendScreenName(getActivity(), getScreenName());
    }
}
