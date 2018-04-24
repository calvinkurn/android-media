package com.tokopedia.checkout.view.view.shipment;

import android.app.Activity;
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

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.checkout.data.entity.request.DataCheckoutRequest;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.view.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.view.cartlist.CartItemDecoration;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentFragment extends BaseCheckoutFragment implements ShipmentContract.View {

    public static final int RESULT_CODE_CANCEL_SHIPMENT_PAYMENT = 4;
    public static final String ARG_EXTRA_SHIPMENT_FORM_DATA = "ARG_EXTRA_SHIPMENT_FORM_DATA";
    public static final String ARG_EXTRA_CART_PROMO_SUGGESTION = "ARG_EXTRA_CART_PROMO_SUGGESTION";
    public static final String ARG_EXTRA_PROMO_CODE_APPLIED_DATA = "ARG_EXTRA_PROMO_CODE_APPLIED_DATA";

    private RecyclerView rvShipment;
    private TextView tvSelectPaymentMethod;
    private LinearLayout llTotalPaymentLayout;
    private TextView tvTotalPayment;
    private TextView tvPromoMessage;
    private CardView cvBottomLayout;

    private TkpdProgressDialog progressDialogNormal;

    private PromoCodeAppliedData promoCodeAppliedData;
    private CartPromoSuggestion cartPromoSuggestion;
    private List<DataCheckoutRequest> mCheckoutRequestData;
    private CheckoutData checkoutData;

    @Inject
    private ShipmentAdapter shipmentAdapter;
    @Inject
    private ShipmentPresenter shipmentPresenter;

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
        CartShipmentAddressFormData cartShipmentAddressFormData
                = arguments.getParcelable(ARG_EXTRA_SHIPMENT_FORM_DATA);

        singleShipmentData = mCartShipmentAddressFormDataConverter.convert(
                cartShipmentAddressFormData
        );
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
        llTotalPaymentLayout = view.findViewById(R.id.ll_total_payment_layout);
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
                    llTotalPaymentLayout.setVisibility(isReachBottomEnd ? View.VISIBLE : View.GONE);
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
        shipmentAdapter.addAddressShipmentData(singleShipmentData.getRecipientAddress());
        shipmentAdapter.addCartItemDataList(singleShipmentData.getCartItem());
        shipmentAdapter.addShipmentCostData(singleShipmentData.getShipmentCost());
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
        }
    }
}
