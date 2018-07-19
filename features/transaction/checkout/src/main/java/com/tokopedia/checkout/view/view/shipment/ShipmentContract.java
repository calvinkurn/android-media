package com.tokopedia.checkout.view.view.shipment;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Donation;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;
import com.tokopedia.checkout.view.view.shipment.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCheckoutButtonModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSellerCashbackModel;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.transactiondata.entity.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transactiondata.entity.request.DataChangeAddressRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;

import java.util.List;

/**
 * @author Irfan Khoirul on 24/04/18.
 */

public interface ShipmentContract {

    interface View extends CustomerView {
        void showInitialLoading();

        void hideInitialLoading();

        void showLoading();

        void hideLoading();

        void showToastNormal(String message);

        void showToastError(String message);

        void renderErrorPage(String message);

        void renderCheckoutPage(CartShipmentAddressFormData cartShipmentAddressFormData);

        void renderCheckShipmentPrepareCheckoutSuccess();

        void renderErrorDataHasChangedCheckShipmentPrepareCheckout(
                CartShipmentAddressFormData cartShipmentAddressFormData, boolean needToRefreshItemList
        );

        void renderNoRecipientAddressShipmentForm(CartShipmentAddressFormData cartShipmentAddressFormData);

        void renderErrorDataHasChangedAfterCheckout(List<ShipmentCartItemModel> shipmentCartItemModelList);

        void renderThanksTopPaySuccess(String message);

        void renderCheckoutCartSuccess(CheckoutData checkoutData);

        void renderCheckoutCartError(String message);

        void sendAnalyticsChoosePaymentMethodSuccess();

        void sendAnalyticsChoosePaymentMethodFailed();

        void sendAnalyticsChoosePaymentMethodCourierNotComplete();

        void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData);

        void renderErrorCheckPromoCodeFromSuggestedPromo(String message);

        void renderErrorCheckPromoShipmentData(String message);

        void renderCheckPromoShipmentDataSuccess(PromoCodeCartShipmentData checkPromoCodeCartShipmentResult);

        void renderEditAddressSuccess(String latitude, String longitude);

        void renderChangeAddressSuccess(RecipientAddressModel recipientAddressModel);

        void renderCancelAutoApplyCouponSuccess();

        void navigateToSetPinpoint(String message, LocationPass locationPass);

        List<DataCheckoutRequest> generateNewCheckoutRequest(List<ShipmentCartItemModel> shipmentCartItemModelList);

        ShipmentDataConverter getShipmentDataConverter();

        Activity getActivityContext();
    }

    interface Presenter extends CustomerPresenter<View> {

        void processLoadCheckoutPage(boolean isInitialLoad);

        void processReloadCheckoutPageBecauseOfError();

        void processCheckShipmentPrepareCheckout();

        void processCheckout();

        void processVerifyPayment(String transactionId);

        void checkPromoShipment();

        void processCheckPromoCodeFromSuggestedPromo(String promoCode);

        RecipientAddressModel getRecipientAddressModel();

        void setRecipientAddressModel(RecipientAddressModel recipientAddressModel);

        List<ShipmentCartItemModel> getShipmentCartItemModelList();

        void setShipmentCartItemModelList(List<ShipmentCartItemModel> recipientCartItemList);

        PromoCodeAppliedData getPromoCodeAppliedData();

        void setPromoCodeAppliedData(PromoCodeAppliedData promoCodeAppliedData);

        CartPromoSuggestion getCartPromoSuggestion();

        void setCartPromoSuggestion(CartPromoSuggestion cartPromoSuggestion);

        CheckoutData getCheckoutData();

        void setCheckoutData(CheckoutData checkoutData);

        void setDataCheckoutRequestList(List<DataCheckoutRequest> dataCheckoutRequestList);

        void setPromoCodeCartShipmentRequestData(
                List<CheckPromoCodeCartShipmentRequest.Data> promoCodeCartShipmentRequestData
        );

        void setDataChangeAddressRequestList(List<DataChangeAddressRequest> dataChangeAddressRequestList);

        ShipmentCostModel getShipmentCostModel();

        void setShipmentCostModel(ShipmentCostModel shipmentCostModel);

        ShipmentCheckoutButtonModel getShipmentCheckoutButtonModel();

        void setShipmentCheckoutButtonModel(ShipmentCheckoutButtonModel shipmentCheckoutButtonModel);

        void editAddressPinpoint(String latitude, String longitude, ShipmentCartItemModel shipmentCartItemModel, LocationPass locationPass);

        void cancelAutoApplyCoupon();

        void changeShippingAddress(RecipientAddressModel recipientAddressModel);

        void setShipmentDonationModel(ShipmentDonationModel shipmentDonationModel);

        ShipmentDonationModel getShipmentDonationModel();

    }

}
