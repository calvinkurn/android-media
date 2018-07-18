package com.tokopedia.checkout.view.view.shipment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
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
import com.tokopedia.checkout.view.view.shipment.converter.RatesDataConverter;
import com.tokopedia.checkout.view.view.shipment.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.view.shipment.di.DaggerShipmentComponent;
import com.tokopedia.checkout.view.view.shipment.di.ShipmentComponent;
import com.tokopedia.checkout.view.view.shipment.di.ShipmentModule;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCheckoutButtonModel;
import com.tokopedia.checkout.view.view.shippingoptions.CourierBottomsheet;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
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
    public static final String ARG_EXTRA_SHIPMENT_FORM_DATA = "ARG_EXTRA_SHIPMENT_FORM_DATA";
    public static final String ARG_EXTRA_CART_PROMO_SUGGESTION = "ARG_EXTRA_CART_PROMO_SUGGESTION";
    public static final String ARG_EXTRA_PROMO_CODE_APPLIED_DATA = "ARG_EXTRA_PROMO_CODE_APPLIED_DATA";
    public static final String ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO = "ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO";
    public static final String ARG_EXTRA_NAVIGATE_TO_ADDRESS_CHOICE = "ARG_EXTRA_NAVIGATE_TO_ADDRESS_CHOICE";
    private static final String NO_PINPOINT_ETD = "Belum Pinpoint";
    private static final int TOASTER_DURATION = 2500;
    private static final String EXTRA_STATE_SHIPMENT_SELECTION = "EXTRA_STATE_SHIPMENT_SELECTION";

    private RecyclerView rvShipment;
    private TkpdProgressDialog progressDialogNormal;
    private CourierBottomsheet courierBottomsheet;

    private CartShipmentAddressFormData cartShipmentAddressFormData;

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

    public static ShipmentFragment newInstance(CartShipmentAddressFormData cartShipmentAddressFormData,
                                               PromoCodeAppliedData promoCodeAppliedData,
                                               CartPromoSuggestion cartPromoSuggestionData,
                                               String defaultSelectedTabPromo,
                                               boolean navigateToAddressChoice) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        bundle.putParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestionData);
        bundle.putParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA, promoCodeAppliedData);
        bundle.putString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, defaultSelectedTabPromo);
        bundle.putBoolean(ARG_EXTRA_NAVIGATE_TO_ADDRESS_CHOICE, navigateToAddressChoice);
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
        cartShipmentAddressFormData = arguments.getParcelable(ARG_EXTRA_SHIPMENT_FORM_DATA);
        if (cartShipmentAddressFormData != null) {
            if (!cartShipmentAddressFormData.isMultiple()) {
                shipmentPresenter.setRecipientAddressModel(
                        shipmentDataConverter.getRecipientAddressModel(cartShipmentAddressFormData));
            }

            shipmentPresenter.setShipmentCartItemModelList(shipmentDataConverter.getShipmentItems(
                    cartShipmentAddressFormData));

            Token token = new Token();
            token.setDistrictRecommendation(cartShipmentAddressFormData.getKeroDiscomToken());
            token.setUt(cartShipmentAddressFormData.getKeroUnixTime());
        }

        PromoCodeAppliedData promoCodeAppliedData = arguments.getParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA);
        shipmentPresenter.setPromoCodeAppliedData(promoCodeAppliedData);
        shipmentPresenter.setCartPromoSuggestion(arguments.getParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION));
        shipmentPresenter.setShipmentCostModel(new ShipmentCostModel());
        shipmentPresenter.setShipmentCheckoutButtonModel(new ShipmentCheckoutButtonModel());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment;
    }

    @Override
    protected void initView(View view) {
        rvShipment = view.findViewById(R.id.rv_shipment);
        progressDialogNormal = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        ((SimpleItemAnimator) rvShipment.getItemAnimator()).setSupportsChangeAnimations(false);
        if (getArguments().getBoolean(ARG_EXTRA_NAVIGATE_TO_ADDRESS_CHOICE, false)) {
            onAddOrChangeAddress();
        }
    }

    @Override
    protected void setViewListener() {
        PromoCodeAppliedData promoCodeAppliedData = shipmentPresenter.getPromoCodeAppliedData();
        CartPromoSuggestion cartPromoSuggestion = shipmentPresenter.getCartPromoSuggestion();
        RecipientAddressModel recipientAddressModel = shipmentPresenter.getRecipientAddressModel();
        List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentPresenter.getShipmentCartItemModelList();
        ShipmentCostModel shipmentCostModel = shipmentPresenter.getShipmentCostModel();
        ShipmentCheckoutButtonModel shipmentCheckoutButtonModel = shipmentPresenter.getShipmentCheckoutButtonModel();

        initRecyclerViewData(promoCodeAppliedData, cartPromoSuggestion, recipientAddressModel,
                shipmentCartItemModelList, shipmentCostModel, shipmentCheckoutButtonModel, true);
    }

    private void initRecyclerViewData(PromoCodeAppliedData promoCodeAppliedData,
                                      CartPromoSuggestion cartPromoSuggestion,
                                      RecipientAddressModel recipientAddressModel,
                                      List<ShipmentCartItemModel> shipmentCartItemModelList,
                                      ShipmentCostModel shipmentCostModel,
                                      ShipmentCheckoutButtonModel shipmentCheckoutButtonModel,
                                      boolean isInitialRender) {

        shipmentAdapter.clearData();
        rvShipment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvShipment.setAdapter(shipmentAdapter);
        if (isInitialRender) {
            rvShipment.addItemDecoration(
                    new CartItemDecoration((int) getResources().getDimension(R.dimen.dp_12), false, 0));
        }
        CartItemPromoHolderData cartItemPromoHolderData =
                CartItemPromoHolderData.createInstanceFromAppliedPromo(promoCodeAppliedData);
        cartItemPromoHolderData.setDefaultSelectedTabString(
                getArguments().getString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, "")
        );
        shipmentAdapter.addPromoVoucherData(cartItemPromoHolderData);
        if (promoCodeAppliedData != null) {
            cartPromoSuggestion.setVisible(false);
            shipmentAdapter.addPromoSuggestionData(cartPromoSuggestion);
        }
        if (recipientAddressModel != null) {
            shipmentAdapter.addAddressShipmentData(recipientAddressModel);
        }
        shipmentAdapter.addCartItemDataList(shipmentCartItemModelList);
        shipmentAdapter.addShipmentCostData(shipmentCostModel);
        shipmentAdapter.addShipmentCheckoutButtonModel(shipmentCheckoutButtonModel);
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
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showLoading() {
        if (!progressDialogNormal.isProgress()) progressDialogNormal.showDialog();
    }

    @Override
    public void hideLoading() {
        if (progressDialogNormal.isProgress()) progressDialogNormal.dismiss();
    }

    @Override
    public void showToastNormal(String message) {
        if (getView() != null) {
            ToasterNormal.make(getView(), message, TOASTER_DURATION)
                    .setAction(getActivity().getString(R.string.label_action_snackbar_close), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
        }
    }

    @Override
    public void showToastError(String message) {
        if (getView() != null) {
            Snackbar snackbar = Snackbar.make(getView(), message, TOASTER_DURATION);
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

    @Override
    public void renderCheckShipmentPrepareCheckoutSuccess() {
        shipmentPresenter.processCheckout();
    }

    @Override
    public void renderErrorDataHasChangedCheckShipmentPrepareCheckout(
            CartShipmentAddressFormData cartShipmentAddressFormData, boolean needToRefreshItemList
    ) {
        shipmentAdapter.disableShipmentCheckoutButtonModel();
        if (needToRefreshItemList) {
            showToastError(getActivity().getString(R.string.error_message_checkout_failed));
            shipmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void renderErrorDataHasChangedAfterCheckout(List<ShipmentCartItemModel> oldShipmentCartItemModelList) {
        showToastError(getActivity().getString(R.string.error_message_checkout_failed));
        initRecyclerViewData(shipmentPresenter.getPromoCodeAppliedData(),
                shipmentPresenter.getCartPromoSuggestion(),
                shipmentPresenter.getRecipientAddressModel(),
                oldShipmentCartItemModelList,
                shipmentPresenter.getShipmentCostModel(),
                shipmentPresenter.getShipmentCheckoutButtonModel(),
                false);
    }

    @Override
    public void renderThanksTopPaySuccess(String message) {
        showToastNormal(getString(R.string.message_payment_succeded_transaction_module));
        startActivity(TransactionPurchaseRouter.createIntentTxSummary(getActivity()));
        CartBadgeNotificationReceiver.resetBadgeCart(getActivity());
        getActivity().finish();
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
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickPilihMetodePembayaranSuccess();
    }

    @Override
    public void sendAnalyticsChoosePaymentMethodFailed() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess();
    }

    @Override
    public void sendAnalyticsChoosePaymentMethodCourierNotComplete() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickPilihMetodePembayaranCourierNotComplete();
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
        cartItemPromoHolderData.setDefaultSelectedTabString(
                getArguments().getString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, "")
        );
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
        Intent intent = GeolocationActivity.createInstance(getActivity(), locationPass);
        startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT);
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
        }
    }

    private void onResultFromCourierPinpoint(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null) {
            LocationPass locationPass = data.getExtras().getParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION);
            if (locationPass != null) {
                shipmentPresenter.editAddressPinpoint(locationPass.getLatitude(), locationPass.getLongitude(),
                        shipmentAdapter.getShipmentCartItemModelByIndex(shipmentAdapter.getLastChooseCourierItemPosition()),
                        locationPass);
            }
        }
    }

    private void onResultFromPayment(int resultCode) {
        getActivity().setResult(resultCode);
        getActivity().finish();
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
                cartPromo.setDefaultSelectedTabString(getArguments().getString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, ""));
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
                cartPromo.setDefaultSelectedTabString(getArguments().getString(ARG_EXTRA_DEFAULT_SELECTED_TAB_PROMO, ""));
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

                if (!currentAddress.getId().equals(newAddress.getId()) ||
                        !currentAddress.getAddressName().equals(newAddress.getAddressName()) ||
                        !currentAddress.getAddressStreet().equals(newAddress.getAddressStreet()) ||
                        !currentAddress.getRecipientName().equals(newAddress.getRecipientName()) ||
                        !currentAddress.getRecipientPhoneNumber().equals(newAddress.getRecipientPhoneNumber()) ||
                        !String.valueOf(currentAddress.getLatitude()).equals(String.valueOf(newAddress.getLatitude())) ||
                        !String.valueOf(currentAddress.getLongitude()).equals(String.valueOf(newAddress.getLongitude())) ||
                        !currentAddress.getAddressPostalCode().equals(newAddress.getAddressPostalCode()) ||
                        !currentAddress.getDestinationDistrictId().equals(newAddress.getDestinationDistrictId()) ||
                        !currentAddress.getCityId().equals(newAddress.getCityId()) ||
                        !currentAddress.getProvinceId().equals(newAddress.getProvinceId())) {
                    shipmentPresenter.setDataChangeAddressRequestList(shipmentAdapter.getRequestData(newAddress, null).getChangeAddressRequestData());
                    shipmentPresenter.changeShippingAddress(newAddress);
                }
                break;
            case CartAddressChoiceActivity.RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM:
                Intent intent = new Intent();
                intent.putExtra(ShipmentActivity.EXTRA_SELECTED_ADDRESS_RECIPIENT_DATA,
                        shipmentAdapter.getAddressShipmentData());
                Token token = new Token();
                token.setDistrictRecommendation(cartShipmentAddressFormData.getKeroDiscomToken());
                token.setUt(cartShipmentAddressFormData.getKeroUnixTime());
                intent.putExtra(ShipmentActivity.EXTRA_DISTRICT_RECOMMENDATION_TOKEN, token);
                getActivity().setResult(ShipmentActivity.RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM, intent);
                getActivity().finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onAddOrChangeAddress() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickGantiAlamatAtauKirimKeBeberapaAlamat();
        Intent intent = CartAddressChoiceActivity.createInstance(getActivity(),
                shipmentPresenter.getRecipientAddressModel(),
                CartAddressChoiceActivity.TYPE_REQUEST_SELECT_ADDRESS_FROM_SHORT_LIST);
        startActivityForResult(intent, CartAddressChoiceActivity.REQUEST_CODE);
    }

    @Override
    public void resetTotalPrice() {
        shipmentAdapter.updateCheckoutButtonData(true, "-");
    }

    @Override
    public void onChooseShipment(int position, ShipmentCartItemModel shipmentCartItemModel,
                                 RecipientAddressModel recipientAddressModel) {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickSelectCourier();
        ShipmentDetailData shipmentDetailData;
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
            shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        } else {
            if (recipientAddressModel == null) {
                recipientAddressModel = shipmentCartItemModel.getRecipientAddressModel();
            }
            shipmentDetailData = ratesDataConverter.getShipmentDetailData(shipmentCartItemModel,
                    recipientAddressModel);
        }
        if (shipmentDetailData != null) {
            showCourierChoiceBottomSheet(shipmentDetailData, shipmentCartItemModel.getShopShipmentList(), position);
        }
    }

    private void showCourierChoiceBottomSheet(ShipmentDetailData shipmentDetailData,
                                              List<ShopShipment> shopShipmentList,
                                              int position) {
        if (courierBottomsheet == null || position != courierBottomsheet.getLastCartItemPosition() ||
                shipmentDetailData.getSelectedCourier() == null) {
            courierBottomsheet = new CourierBottomsheet(getActivity(), shipmentDetailData, shopShipmentList, position);
        }
        courierBottomsheet.setListener(this);
        courierBottomsheet.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                checkoutAnalyticsCourierSelection.sendScreenName(
                        getActivity(), ConstantTransactionAnalytics.ScreenName.SELECT_COURIER
                );
                courierBottomsheet.updateHeight();
                checkoutAnalyticsCourierSelection.eventViewAtcCourierSelectionImpressionCourierSelection();
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
    public void onTotalPaymentChange(ShipmentCostModel shipmentCostModel) {
        shipmentAdapter.updateCheckoutButtonData(false, null);
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
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickGunakanKodePromoAtauKupon();
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
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickXOnBannerPromoCodeCode();
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickXFromGunakanKodePromoAtauKupon();
    }

    @Override
    public void onCartDataEnableToCheckout() {
        shipmentAdapter.updateCheckoutButtonData(false, null);
    }

    @Override
    public void onCartDataDisableToCheckout() {
        shipmentAdapter.updateCheckoutButtonData(true, null);
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
    public void onShipmentItemClick(CourierItemData courierItemData, int cartItemPosition) {
        checkoutAnalyticsCourierSelection.eventViewCourierSelectionClickCourierOption(
                courierItemData.getName(),
                courierItemData.getShipmentItemDataType()
        );
        ShipmentSelectionStateData shipmentSelectionStateData = new ShipmentSelectionStateData();
        shipmentSelectionStateData.setPosition(cartItemPosition);
        shipmentSelectionStateData.setCourierItemData(courierItemData);
        shipmentSelectionStateDataHashSet.add(shipmentSelectionStateData);
        if (courierItemData.getEstimatedTimeDelivery().equalsIgnoreCase(NO_PINPOINT_ETD)) {
            shipmentAdapter.setLastChooseCourierItemPosition(cartItemPosition);
            LocationPass locationPass = new LocationPass();
            if (shipmentAdapter.getAddressShipmentData() != null) {
                locationPass.setCityName(shipmentAdapter.getAddressShipmentData().getAddressCityName());
                locationPass.setDistrictName(shipmentAdapter.getAddressShipmentData().getDestinationDistrictName());
            } else {
                ShipmentCartItemModel shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(cartItemPosition);
                if (shipmentCartItemModel != null) {
                    RecipientAddressModel recipientAddressModel = shipmentCartItemModel.getRecipientAddressModel();
                    if (recipientAddressModel != null) {
                        locationPass.setCityName(recipientAddressModel.getAddressCityName());
                        locationPass.setDistrictName(recipientAddressModel.getDestinationDistrictName());
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
                    shipmentAdapter.updateItemAndTotalCost(position);
                    shipmentAdapter.updateInsuranceTncVisibility();
                }
            });
        } else {
            shipmentAdapter.updateItemAndTotalCost(position);
            shipmentAdapter.updateInsuranceTncVisibility();
        }
        shipmentAdapter.updateShipmentCostModel();
    }

    @Override
    public void onNeedUpdateViewItem(final int position) {
        if (rvShipment.isComputingLayout()) {
            rvShipment.post(new Runnable() {
                @Override
                public void run() {
                    shipmentAdapter.notifyItemChanged(position);
                }
            });
        } else {
            shipmentAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onSubTotalCartItemClicked(int position) {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickSubtotal();
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
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickDropship();
    }

    @Override
    public void onInsuranceCheckedForTrackingAnalytics() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickAsuransiPengiriman();
    }

    @Override
    public void onChoosePaymentMethodButtonClicked(boolean ableToCheckout) {
        shipmentAdapter.checkDropshipperValidation();
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
