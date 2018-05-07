package com.tokopedia.checkout.view.view.shipment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.checkout.data.entity.request.DataCheckoutRequest;
import com.tokopedia.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
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
import com.tokopedia.checkout.view.view.shipment.shippingoptions.CourierBottomsheet;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressItem;
import com.tokopedia.checkout.view.view.shipmentform.CartShipmentActivity;
import com.tokopedia.checkout.view.view.shippingoptions.ShipmentChoiceBottomSheet;
import com.tokopedia.checkout.view.view.shippingoptions.ShipmentDetailActivity;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.checkout.view.view.shippingoptions.ShipmentDetailActivity.EXTRA_POSITION;
import static com.tokopedia.checkout.view.view.shippingoptions.ShipmentDetailActivity.EXTRA_SHIPMENT_DETAIL_DATA;

/**
 * @author Irfan Khoirul on 23/04/18.
 * Originaly authored by Aghny, Angga, Kris
 */

public class ShipmentFragment extends BaseCheckoutFragment implements ShipmentContract.View,
        ShipmentAdapterActionListener, CourierBottomsheet.ActionListener {

    private static final int REQUEST_CODE_SHIPMENT_DETAIL = 11;
    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;
    public static final int RESULT_CODE_CANCEL_SHIPMENT_PAYMENT = 4;
    public static final String ARG_EXTRA_SHIPMENT_FORM_DATA = "ARG_EXTRA_SHIPMENT_FORM_DATA";
    public static final String ARG_EXTRA_CART_PROMO_SUGGESTION = "ARG_EXTRA_CART_PROMO_SUGGESTION";
    public static final String ARG_EXTRA_PROMO_CODE_APPLIED_DATA = "ARG_EXTRA_PROMO_CODE_APPLIED_DATA";

    private RecyclerView rvShipment;
    private TextView tvSelectPaymentMethod;
    private TextView tvTotalPayment;
    private TextView tvPromoMessage;
    private CardView cvBottomLayout;

    private TkpdProgressDialog progressDialogNormal;

    private List<ShipmentItem> shipmentItems;
    private RecipientAddressModel recipientAddressModel;
    private PromoCodeAppliedData promoCodeAppliedData;
    private CartPromoSuggestion cartPromoSuggestion;
    private List<DataCheckoutRequest> mCheckoutRequestData;
    private CheckoutData checkoutData;
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
                recipientAddressModel = shipmentDataConverter.getRecipientAddressModel(cartShipmentAddressFormData);
            }
            shipmentItems = shipmentDataConverter.getShipmentItems(cartShipmentAddressFormData);
        }

        promoCodeAppliedData = arguments.getParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA);
        cartPromoSuggestion = arguments.getParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment;
    }

    @Override
    protected void initView(View view) {
        rvShipment = view.findViewById(R.id.rv_shipment);
        tvSelectPaymentMethod = view.findViewById(R.id.tv_select_payment_method);
        tvTotalPayment = view.findViewById(R.id.tv_total_payment);
        tvPromoMessage = view.findViewById(R.id.tv_promo_message);
        cvBottomLayout = view.findViewById(R.id.bottom_layout);
        progressDialogNormal = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        rvShipment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvShipment.setAdapter(shipmentAdapter);
        rvShipment.addItemDecoration(
                new CartItemDecoration((int) getResources().getDimension(R.dimen.new_margin_med),
                        false, 0));

        rvShipment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (rvShipment != null) {
                    boolean isReachBottomEnd = rvShipment.canScrollVertically(1);
                    if (!TextUtils.isEmpty(tvPromoMessage.getText().toString())) {
                        tvPromoMessage.setVisibility(isReachBottomEnd ? View.VISIBLE : View.GONE);
                    } else {
                        tvPromoMessage.setVisibility(View.GONE);
                    }
                }
            }
        });

        tvSelectPaymentMethod.setOnClickListener(getOnClickListenerButtonCheckout());

        tvTotalPayment.setText("-");
        cvBottomLayout.setVisibility(View.VISIBLE);

        shipmentAdapter.addPromoVoucherData(
                CartItemPromoHolderData.createInstanceFromAppliedPromo(promoCodeAppliedData)
        );
        if (promoCodeAppliedData != null) {
            cartPromoSuggestion.setVisible(false);
        }
        shipmentAdapter.addPromoSuggestionData(cartPromoSuggestion);
        if (recipientAddressModel != null) {
            shipmentAdapter.addAddressShipmentData(recipientAddressModel);
        }
        shipmentAdapter.addCartItemDataList(shipmentItems);
        shipmentAdapter.addShipmentCostData(new ShipmentCostModel());
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
                shipmentPresenter.processCheckShipmentPrepareCheckout();
            }
        };
    }

    @Override
    public void showLoading() {
        if (!progressDialogNormal.isProgress()) progressDialogNormal.showDialog();
    }

    @Override
    public void hideLoading() {
        if (!progressDialogNormal.isProgress()) progressDialogNormal.dismiss();
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
            ToasterError.make(getView(), message, 5000).show();
        }
    }

    @Override
    public void renderCheckShipmentPrepareCheckoutSuccess() {
        CheckoutRequest checkoutData = generateCheckoutRequest(
                promoCodeAppliedData != null && promoCodeAppliedData.getPromoCode() != null ?
                        promoCodeAppliedData.getPromoCode() : "", 0
        );
        if (checkoutData != null) {
            shipmentPresenter.processCheckout(checkoutData);
        }
    }

    @Override
    public void renderErrorDataHasChangedCheckShipmentPrepareCheckout(
            CartShipmentAddressFormData cartShipmentAddressFormData
    ) {

    }

    private CheckoutRequest generateCheckoutRequest(String promoCode, int isDonation) {
        if (mCheckoutRequestData == null) {
            // Show error cant checkout
            return null;
        }

        return new CheckoutRequest.Builder()
                .promoCode(promoCode)
                .isDonation(isDonation)
                .data(mCheckoutRequestData)
                .build();
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
        this.checkoutData = checkoutData;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TopPayActivity.REQUEST_CODE) {
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
                    shipmentPresenter.processVerifyPayment(checkoutData.getTransactionId());
                    break;
                case TopPayActivity.PAYMENT_FAILED:
                    shipmentPresenter.processVerifyPayment(checkoutData.getTransactionId());
                    break;
            }
        } else if ((requestCode == REQUEST_CHOOSE_PICKUP_POINT || requestCode == REQUEST_CODE_SHIPMENT_DETAIL)
                && resultCode == Activity.RESULT_OK) {
            onResultFromRequestCodeCourierOptions(requestCode, data);
        } else if (requestCode == CartAddressChoiceActivity.REQUEST_CODE) {
            onResultFromRequestCodeAddressOptions(resultCode, data);
        }
    }

    private void onResultFromRequestCodeCourierOptions(int requestCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSE_PICKUP_POINT:
//                Store pickupBooth = data.getParcelableExtra(INTENT_DATA_STORE);
//                shipmentAdapter.setPickupPoint(pickupBooth);
                break;
            case REQUEST_CODE_SHIPMENT_DETAIL:

            default:
                break;
        }
    }

    private void onResultFromRequestCodeAddressOptions(int resultCode, Intent data) {
        switch (resultCode) {
            case CartAddressChoiceActivity.RESULT_CODE_ACTION_SELECT_ADDRESS:
                RecipientAddressModel selectedAddress = data.getParcelableExtra(
                        CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA);

                shipmentAdapter.updateSelectedAddress(selectedAddress);
                break;
            case CartAddressChoiceActivity.RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM:
                // todo : reload fragment with multiple address
                Intent intent = new Intent();
                intent.putExtra(CartShipmentActivity.EXTRA_SELECTED_ADDRESS_RECIPIENT_DATA,
                        shipmentAdapter.getAddressShipmentData());
                getActivity().setResult(CartShipmentActivity.RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM, intent);
                getActivity().finish();
                break;

            default:
                break;
        }
    }


    // Adapter Listener
    @Override
    public void onAddOrChangeAddress() {
        Intent intent = CartAddressChoiceActivity.createInstance(getActivity(),
                CartAddressChoiceActivity.TYPE_REQUEST_SELECT_ADDRESS_FROM_SHORT_LIST);

        startActivityForResult(intent, CartAddressChoiceActivity.REQUEST_CODE);
    }

    @Override
    public void resetTotalPrice() {
        tvTotalPayment.setText("-");
    }

    @Override
    public void onChooseShipment(int position, ShipmentItem shipmentItem, RecipientAddressModel recipientAddressModel) {
        ShipmentDetailData shipmentDetailData = null;
        if (shipmentItem.getSelectedShipmentDetailData() != null) {
            shipmentDetailData = shipmentItem.getSelectedShipmentDetailData();
        } else {
            shipmentDetailData = ratesDataConverter.getShipmentDetailData(shipmentItem,
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
        double price = shipmentCostModel.getTotalPrice();
        tvTotalPayment.setText(price == 0 ? "-" : CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (int) price, true));
    }

    @Override
    public void onFinishChoosingShipment(List<CheckPromoCodeCartShipmentRequest.Data> data, List<DataCheckoutRequest> checkoutRequest) {

    }

    @Override
    public void onShowPromoMessage(String promoMessage) {

    }

    @Override
    public void onHidePromoMessage() {

    }

    @Override
    public void onRemovePromoCode() {

    }

    @Override
    public void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion, int position) {

    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion, int position) {

    }

    @Override
    public void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position) {

    }

    @Override
    public void onCartPromoCancelVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position) {

    }

    @Override
    public void onCartPromoTrackingSuccess(CartItemPromoHolderData cartPromo, int position) {

    }

    @Override
    public void onCartPromoTrackingCancelled(CartItemPromoHolderData cartPromo, int position) {

    }

    @Override
    public void onCartDataEnableToCheckout() {
        tvSelectPaymentMethod.setBackgroundResource(R.drawable.bg_button_orange_enabled);
        tvSelectPaymentMethod.setTextColor(getResources().getColor(R.color.white));
        tvSelectPaymentMethod.setOnClickListener(getOnClickListenerButtonCheckout());
    }

    @Override
    public void onCartDataDisableToCheckout() {
        tvSelectPaymentMethod.setBackgroundResource(R.drawable.bg_button_disabled);
        tvSelectPaymentMethod.setTextColor(getResources().getColor(R.color.grey_500));
        tvSelectPaymentMethod.setOnClickListener(null);
    }

    @Override
    public void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position) {

    }

    @Override
    public void onShipmentItemClick(CourierItemData courierItemData, int cartItemPosition) {
        shipmentAdapter.setSelecteCourier(cartItemPosition, courierItemData);
    }
}
