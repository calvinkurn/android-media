package com.tokopedia.checkout.view.view.shipment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.CartComponent;
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
import com.tokopedia.checkout.view.view.shipmentform.CartShipmentActivity;
import com.tokopedia.checkout.view.view.shippingoptions.CourierBottomsheet;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;

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
    public static final int RESULT_CODE_CANCEL_SHIPMENT_PAYMENT = 4;
    public static final String ARG_EXTRA_SHIPMENT_FORM_DATA = "ARG_EXTRA_SHIPMENT_FORM_DATA";
    public static final String ARG_EXTRA_CART_PROMO_SUGGESTION = "ARG_EXTRA_CART_PROMO_SUGGESTION";
    public static final String ARG_EXTRA_PROMO_CODE_APPLIED_DATA = "ARG_EXTRA_PROMO_CODE_APPLIED_DATA";
    private static final String NO_PINPOINT_ETD = "Belum Pinpoint";

    private RecyclerView rvShipment;
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

    public static ShipmentFragment newInstance(CartShipmentAddressFormData cartShipmentAddressFormData,
                                               PromoCodeAppliedData promoCodeAppliedData,
                                               CartPromoSuggestion cartPromoSuggestionData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        bundle.putParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestionData);
        bundle.putParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA, promoCodeAppliedData);
        ShipmentFragment shipmentFragment = new ShipmentFragment();
        shipmentFragment.setArguments(bundle);

        return shipmentFragment;
    }

    @Override
    protected void initInjector() {
        ShipmentComponent component = DaggerShipmentComponent.builder()
                .cartComponent(getComponent(CartComponent.class))
                .shipmentModule(new ShipmentModule(this))
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
        CartShipmentAddressFormData cartShipmentAddressFormData
                = arguments.getParcelable(ARG_EXTRA_SHIPMENT_FORM_DATA);
        if (cartShipmentAddressFormData != null) {
            if (!cartShipmentAddressFormData.isMultiple()) {
                shipmentPresenter.setRecipientAddressModel(
                        shipmentDataConverter.getRecipientAddressModel(cartShipmentAddressFormData));
            }
            shipmentPresenter.setShipmentCartItemModelList(shipmentDataConverter.getShipmentItems(
                    cartShipmentAddressFormData));
        }

        PromoCodeAppliedData promoCodeAppliedData = arguments.getParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA);
        shipmentPresenter.setPromoCodeAppliedData(promoCodeAppliedData);
        shipmentPresenter.setCartPromoSuggestion(
                (CartPromoSuggestion) arguments.getParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION));
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
    }

    @Override
    protected void setViewListener() {
        rvShipment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvShipment.setAdapter(shipmentAdapter);
        rvShipment.addItemDecoration(
                new CartItemDecoration((int) getResources().getDimension(R.dimen.dp_12), false, 0));

        CartItemPromoHolderData cartItemPromoHolderData =
                CartItemPromoHolderData.createInstanceFromAppliedPromo(shipmentPresenter.getPromoCodeAppliedData());
        shipmentAdapter.addPromoVoucherData(cartItemPromoHolderData);
        if (shipmentPresenter.getPromoCodeAppliedData() != null) {
            shipmentPresenter.getCartPromoSuggestion().setVisible(false);
            shipmentAdapter.addPromoSuggestionData(shipmentPresenter.getCartPromoSuggestion());
        }
        if (shipmentPresenter.getRecipientAddressModel() != null) {
            shipmentAdapter.addAddressShipmentData(shipmentPresenter.getRecipientAddressModel());
        }
        shipmentAdapter.addCartItemDataList(shipmentPresenter.getShipmentCartItemModelList());
        shipmentAdapter.addShipmentCostData(shipmentPresenter.getShipmentCostModel());
        shipmentAdapter.addShipmentCheckoutButtonModel(shipmentPresenter.getShipmentCheckoutButtonModel());
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @NonNull
    private View.OnClickListener getOnClickListenerButtonCheckout() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shipmentAdapter.checkDropshipperValidation();
            }
        };
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
            ToasterNormal.make(getView(), message, 5000).show();
        }
    }

    @Override
    public void showToastError(String message) {
        if (getView() != null) {
            ToasterError.make(getView(), message, 5000)
                    .setAction(getActivity().getString(R.string.label_action_snackbar_close), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .show();
        }
    }

    @Override
    public void renderCheckShipmentPrepareCheckoutSuccess() {
        shipmentPresenter.processCheckout();
    }

    @Override
    public void renderErrorDataHasChangedCheckShipmentPrepareCheckout(
            CartShipmentAddressFormData cartShipmentAddressFormData
    ) {
        List<ShipmentCartItemModel> shipmentCartItemModelList = shipmentDataConverter.getShipmentItems(
                cartShipmentAddressFormData
        );
        shipmentPresenter.setShipmentCartItemModelList(shipmentCartItemModelList);
//        for (ShipmentCartItem shipmentCartItem : shipmentCartItemList) {
//
//        }
//        this.singleShipmentData.setError(shipmentCartItemList.isError());
//        this.singleShipmentData.setErrorMessage(shipmentCartItemList.getErrorMessage());
//        this.singleShipmentData.setWarning(shipmentCartItemList.isWarning());
//        this.singleShipmentData.setWarningMessage(shipmentCartItemList.getWarningMessage());
//
//        List<CartSellerItemModel> cartItem = shipmentCartItemList.getCartItem();
//        for (int i = 0, cartItemSize = cartItem.size(); i < cartItemSize; i++) {
//            CartSellerItemModel data = cartItem.get(i);
//            this.singleShipmentData.getCartItem().get(i).setError(data.isError());
//            this.singleShipmentData.getCartItem().get(i).setErrorMessage(data.getErrorMessage());
//            this.singleShipmentData.getCartItem().get(i).setWarning(data.isWarning());
//            this.singleShipmentData.getCartItem().get(i).setWarningMessage(data.getWarningMessage());
//        }

        shipmentAdapter.clearData();

        shipmentAdapter.addPromoVoucherData(
                CartItemPromoHolderData.createInstanceFromAppliedPromo(shipmentPresenter.getPromoCodeAppliedData())
        );
        if (shipmentPresenter.getCartPromoSuggestion() != null) {
            shipmentAdapter.addPromoSuggestionData(shipmentPresenter.getCartPromoSuggestion());
        }
        shipmentAdapter.addAddressShipmentData(shipmentPresenter.getRecipientAddressModel());
        shipmentAdapter.addCartItemDataList(shipmentPresenter.getShipmentCartItemModelList());
        shipmentAdapter.addShipmentCostData(shipmentPresenter.getShipmentCostModel());
        shipmentAdapter.addShipmentCheckoutButtonModel(shipmentPresenter.getShipmentCheckoutButtonModel());
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
    public void renderCheckPromoCodeFromSuggestedPromoSuccess(CheckPromoCodeCartListResult promoCodeCartListData) {
        shipmentPresenter.setPromoCodeAppliedData(new PromoCodeAppliedData.Builder()
                .typeVoucher(PromoCodeAppliedData.TYPE_VOUCHER)
                .promoCode(promoCodeCartListData.getDataVoucher().getCode())
                .description(promoCodeCartListData.getDataVoucher().getMessageSuccess())
                .amount(promoCodeCartListData.getDataVoucher().getCashbackAmount())
                .build()
        );
        CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
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
    public void renderCheckPromoShipmentDataSuccess(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult) {
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
    public void renderChangeAddressSuccess(RecipientAddressModel selectedAddress) {
        shipmentPresenter.setRecipientAddressModel(selectedAddress);
        shipmentAdapter.updateSelectedAddress(selectedAddress);
        courierBottomsheet = null;
        onCartDataDisableToCheckout();
    }

    private void updateAppliedPromo(CartItemPromoHolderData cartPromo) {
        shipmentAdapter.updateItemPromoVoucher(cartPromo);
        if (shipmentAdapter.hasSetAllCourier()) {
            ShipmentAdapter.RequestData requestData =
                    shipmentAdapter.getRequestData();
            shipmentPresenter.setPromoCodeCartShipmentRequestData(requestData.getPromoRequestData());
            shipmentPresenter.checkPromoShipment();

            shipmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        shipmentAdapter.getShipmentCartItemModelByIndex(shipmentAdapter.getLastChooseCourierItemPosition()));
            }
        }
    }

    private void onResultFromPayment(int resultCode) {
        switch (resultCode) {
            case TopPayActivity.PAYMENT_CANCELLED:
                NetworkErrorHelper.showSnackbar(
                        getActivity(),
                        getString(R.string.alert_payment_canceled_or_failed_transaction_module)
                );
                getActivity().setResult(RESULT_CODE_CANCEL_SHIPMENT_PAYMENT);
                getActivity().finish();
                break;
            case TopPayActivity.PAYMENT_SUCCESS:
                shipmentPresenter.processVerifyPayment(shipmentPresenter.getCheckoutData().getTransactionId());
                break;
            case TopPayActivity.PAYMENT_FAILED:
                shipmentPresenter.processVerifyPayment(shipmentPresenter.getCheckoutData().getTransactionId());
                break;
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
                RecipientAddressModel selectedAddress = data.getParcelableExtra(
                        CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA);
                shipmentPresenter.setDataChangeAddressRequestList(shipmentAdapter.getRequestData().getChangeAddressRequestData());
                shipmentPresenter.changeShippingAddress(selectedAddress);
                break;
            case CartAddressChoiceActivity.RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM:
                Intent intent = new Intent();
                intent.putExtra(CartShipmentActivity.EXTRA_SELECTED_ADDRESS_RECIPIENT_DATA,
                        shipmentAdapter.getAddressShipmentData());
                getActivity().setResult(ShipmentActivity.RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM, intent);
                getActivity().finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onAddOrChangeAddress() {
        Intent intent = CartAddressChoiceActivity.createInstance(getActivity(),
                shipmentPresenter.getRecipientAddressModel(),
                CartAddressChoiceActivity.TYPE_REQUEST_SELECT_ADDRESS_FROM_SHORT_LIST);

        startActivityForResult(intent, CartAddressChoiceActivity.REQUEST_CODE);
    }

    @Override
    public void resetTotalPrice() {
        shipmentAdapter.updateCheckoutButtonData("-");
    }

    @Override
    public void onChooseShipment(int position, ShipmentCartItemModel shipmentCartItemModel, RecipientAddressModel recipientAddressModel) {
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
            showCourierChoiceBottomSheet(shipmentDetailData, position);
        }
    }

    private void showCourierChoiceBottomSheet(ShipmentDetailData shipmentDetailData, int position) {
        if (courierBottomsheet == null || position != courierBottomsheet.getLastCartItemPosition()) {
            courierBottomsheet = new CourierBottomsheet(getActivity(), shipmentDetailData, position);
        }
        courierBottomsheet.setListener(this);
        courierBottomsheet.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                courierBottomsheet.updateHeight();
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
        shipmentAdapter.updateCheckoutButtonData(null);
    }

    @Override
    public void onFinishChoosingShipment(List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData,
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
    public void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion, int position) {
        shipmentPresenter.processCheckPromoCodeFromSuggestedPromo(cartPromoSuggestion.getPromoCode());
    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion, int position) {
        cartPromoSuggestion.setVisible(false);
        shipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position) {
        if (getActivity().getApplication() instanceof ICheckoutModuleRouter) {
            startActivityForResult(
                    ((ICheckoutModuleRouter) getActivity().getApplication())
                            .checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartListIntent(
                                    getActivity(), true
                            ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
            );
        }
    }

    @Override
    public void onCartPromoCancelVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position) {
        shipmentPresenter.cancelAutoApplyCoupon();
    }

    @Override
    public void onCartPromoTrackingSuccess(CartItemPromoHolderData cartPromo, int position) {

    }

    @Override
    public void onCartPromoTrackingCancelled(CartItemPromoHolderData cartPromo, int position) {

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
    public void onDropshipperValidationResult(boolean result, ShipmentData shipmentData, int errorPosition) {
        if (!result) {
            if (errorPosition != ShipmentAdapter.DEFAULT_ERROR_POSITION) {
                rvShipment.smoothScrollToPosition(errorPosition);
            }
            showToastError(getActivity().getString(R.string.message_error_dropshipper));
            ((ShipmentCartItemModel) shipmentData).setStateDropshipperHasError(true);
            shipmentAdapter.notifyItemChanged(errorPosition);
        } else {
            shipmentPresenter.processCheckShipmentPrepareCheckout();
        }
    }

    @Override
    public void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position) {

    }

    @Override
    public void onShipmentItemClick(CourierItemData courierItemData, int cartItemPosition) {
        if (courierItemData.getEstimatedTimeDelivery().equalsIgnoreCase(NO_PINPOINT_ETD)) {
            shipmentAdapter.setLastChooseCourierItemPosition(cartItemPosition);
            Intent intent = GeolocationActivity.createInstance(getActivity(), null);
            startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT);
        } else {
            shipmentAdapter.setSelecteCourier(cartItemPosition, courierItemData);
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
    public void onInsuranceTncClicked() {
        startActivity(((ICheckoutModuleRouter) getActivity().getApplication()).checkoutModuleRouterGetInsuranceTncActivityIntent());
    }

    @Override
    public void onNeedUpdateRequestData() {
        shipmentAdapter.checkHasSelectAllCourier();
    }
}
