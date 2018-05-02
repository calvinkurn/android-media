package com.tokopedia.checkout.view.view.shipmentform;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.view.base.BaseCheckoutActivity;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.di.component.CartShipmentComponent;
import com.tokopedia.checkout.view.di.component.DaggerCartComponent;
import com.tokopedia.checkout.view.di.component.DaggerCartShipmentComponent;
import com.tokopedia.checkout.view.di.module.CartShipmentModule;
import com.tokopedia.checkout.view.di.module.DataModule;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 25/01/18.
 */

public class CartShipmentActivity extends BaseCheckoutActivity implements ICartShipmentActivity,
        HasComponent<CartComponent> {
    public static final int REQUEST_CODE = 983;
    public static final int RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM = 1;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT = 2;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT = 3;
    public static final int RESULT_CODE_CANCEL_SHIPMENT_PAYMENT = 4;

    public static final String EXTRA_SHIPMENT_FORM_DATA = "EXTRA_SHIPMENT_FORM_DATA";
    public static final String EXTRA_SELECTED_ADDRESS_RECIPIENT_DATA = "EXTRA_DEFAULT_ADDRESS_RECIPIENT_DATA";
    public static final String EXTRA_CART_PROMO_SUGGESTION = "EXTRA_CART_PROMO_SUGGESTION";
    public static final String EXTRA_ADDRESS_SHIPMENT_TYPE = "EXTRA_ADDRESS_SHIPMENT_TYPE";
    public static final String EXTRA_PROMO_CODE_APPLIED_DATA = "EXTRA_PROMO_CODE_APPLIED_DATA";
    public static final int TYPE_ADDRESS_SHIPMENT_SINGLE = 1;
    public static final int TYPE_ADDRESS_SHIPMENT_MULTIPLE = 2;

    private int typeAddressShipment;
    private CartShipmentAddressFormData cartShipmentAddressFormData;
    private CartPromoSuggestion cartPromoSuggestionData;
    private PromoCodeAppliedData promoCodeAppliedData;

    @Inject
    ICartShipmentPresenter cartShipmentPresenter;
    @Inject
    CompositeSubscription compositeSubscription;
    private CheckoutData checkoutData;
    private TkpdProgressDialog progressDialogNormal;


    public static Intent createInstanceSingleAddress(Context context,
                                                     CartShipmentAddressFormData cartShipmentAddressFormData,
                                                     PromoCodeAppliedData promoCodeCartListData,
                                                     CartPromoSuggestion cartPromoSuggestion) {
        Intent intent = new Intent(context, CartShipmentActivity.class);
        intent.putExtra(EXTRA_PROMO_CODE_APPLIED_DATA, promoCodeCartListData);
        intent.putExtra(EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        intent.putExtra(EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestion);
        intent.putExtra(EXTRA_ADDRESS_SHIPMENT_TYPE, TYPE_ADDRESS_SHIPMENT_SINGLE);
        return intent;
    }

    public static Intent createInstanceMultipleAddress(Context context,
                                                       CartShipmentAddressFormData cartShipmentAddressFormData,
                                                       PromoCodeAppliedData promoCodeCartListData,
                                                       CartPromoSuggestion cartPromoSuggestion) {
        Intent intent = new Intent(context, CartShipmentActivity.class);
        intent.putExtra(EXTRA_PROMO_CODE_APPLIED_DATA, promoCodeCartListData);
        intent.putExtra(EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        intent.putExtra(EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestion);
        intent.putExtra(EXTRA_ADDRESS_SHIPMENT_TYPE, TYPE_ADDRESS_SHIPMENT_MULTIPLE);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        promoCodeAppliedData = extras.getParcelable(EXTRA_PROMO_CODE_APPLIED_DATA);
        typeAddressShipment = extras.getInt(EXTRA_ADDRESS_SHIPMENT_TYPE);
        cartShipmentAddressFormData = extras.getParcelable(EXTRA_SHIPMENT_FORM_DATA);
        cartPromoSuggestionData = extras.getParcelable(EXTRA_CART_PROMO_SUGGESTION);
    }


    protected void initInjector() {
        CartShipmentComponent component = DaggerCartShipmentComponent.builder()
                .cartComponent(getComponent())
                .cartShipmentModule(new CartShipmentModule(this))
                .build();
        component.inject(this);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        updateTitle(title.toString());
    }

    @Override
    protected void initView() {
        progressDialogNormal = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

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
        this.startActivityForResult(
                TopPayActivity.createInstance(this, paymentPassData),
                TopPayActivity.REQUEST_CODE);
    }

    @Override
    public void renderErrorCheckoutCart(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorHttpCheckoutCart(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorNoConnectionCheckoutCart(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorTimeoutConnectionCheckoutCart(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderThanksTopPaySuccess(String message) {
        showToastMessage(getString(R.string.message_payment_succeded_transaction_module));
        navigateToActivity(TransactionPurchaseRouter.createIntentTxSummary(this));
        CartBadgeNotificationReceiver.resetBadgeCart(this);
        closeView();
    }

    @Override
    public void renderErrorThanksTopPay(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorHttpThanksTopPay(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorNoConnectionThanksTopPay(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorTimeoutConnectionThanksTopPay(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void closeWithResult(int resultCode, @Nullable Intent intent) {
        if (intent == null) setResult(resultCode);
        else setResult(resultCode, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        showResetDialog();
    }

    void showResetDialog() {
        DialogFragment dialog = ResetShipmentFormDialog.newInstance(
                new ResetShipmentFormDialog.ResetShipmentFormCallbackAction() {

                    @Override
                    public void onResetCartShipmentForm() {
                        if (getSupportFragmentManager().findFragmentById(R.id.parent_view)
                                instanceof SingleAddressShipmentFragment)
                            setResult(RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT);
                        else if (getSupportFragmentManager().findFragmentById(R.id.parent_view)
                                instanceof MultipleAddressShipmentFragment)
                            setResult(RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT);
                        finish();
                    }

                    @Override
                    public void onCancelResetCartShipmentForm() {

                    }
                });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(dialog, ResetShipmentFormDialog.DIALOG_FRAGMENT_TAG);
        ft.commitAllowingStateLoss();
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
        if (!progressDialogNormal.isProgress()) progressDialogNormal.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        if (progressDialogNormal.isProgress()) progressDialogNormal.dismiss();
    }

    @Override
    public void showToastMessage(String message) {

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
    public com.tokopedia.abstraction.common.utils.TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            com.tokopedia.abstraction.common.utils.TKPDMapParam<String, String> originParams
    ) {
        return originParams == null
                ?
                AuthUtil.generateParamsNetwork(
                        this, SessionHandler.getLoginID(this),
                        GCMHandler.getRegistrationId(this)
                )
                :
                AuthUtil.generateParamsNetwork(
                        this, originParams,
                        SessionHandler.getLoginID(this),
                        GCMHandler.getRegistrationId(this)
                );
    }

    @Override
    public void closeView() {

    }

    @Override
    public void checkoutCart(CheckoutRequest checkoutRequest) {
        cartShipmentPresenter.processCheckout(checkoutRequest);
    }

    @Override
    public void checkPromoCodeShipment(Subscriber<CheckPromoCodeCartShipmentResult> subscriber,
                                       CheckPromoCodeCartShipmentRequest checkPromoCodeCartShipmentRequest) {
        cartShipmentPresenter.checkPromoShipment(subscriber, checkPromoCodeCartShipmentRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TopPayActivity.REQUEST_CODE) {
            switch (resultCode) {
                case TopPayActivity.PAYMENT_CANCELLED:
                    NetworkErrorHelper.showSnackbar(
                            this,
                            getString(R.string.alert_payment_canceled_or_failed_transaction_module)
                    );
                    setResult(RESULT_CODE_CANCEL_SHIPMENT_PAYMENT);
                    finish();
                    break;
                case TopPayActivity.PAYMENT_SUCCESS:
                    cartShipmentPresenter.processVerifyPayment(checkoutData.getTransactionId());
                    break;
                case TopPayActivity.PAYMENT_FAILED:
                    cartShipmentPresenter.processVerifyPayment(checkoutData.getTransactionId());
                    break;
            }
        }
    }

    @Override
    protected android.support.v4.app.Fragment getNewFragment() {
        if (typeAddressShipment == TYPE_ADDRESS_SHIPMENT_SINGLE) {
            return
                    SingleAddressShipmentFragment.newInstance(
                            cartShipmentAddressFormData, promoCodeAppliedData, cartPromoSuggestionData);
        } else {
            return
                    MultipleAddressShipmentFragment.newInstance(
                            cartShipmentAddressFormData, promoCodeAppliedData, cartPromoSuggestionData
                    );
        }
    }

    @Override
    public CartComponent getComponent() {
        return CartComponentInjector.newInstance(
                DaggerCartComponent.builder()
                        .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                        .dataModule(new DataModule())
                        .build())
                .getCartApiServiceComponent();
    }
}
