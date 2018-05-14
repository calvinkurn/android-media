package com.tokopedia.checkout.view.view.shipment;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItem;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;

import java.util.List;

/**
 * @author Irfan Khoirul on 24/04/18.
 */

public interface ShipmentContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void showToastNormal(String message);

        void showToastError(String message);

        void renderCheckShipmentPrepareCheckoutSuccess();

        void renderErrorDataHasChangedCheckShipmentPrepareCheckout(
                CartShipmentAddressFormData cartShipmentAddressFormData
        );

        void renderThanksTopPaySuccess(String message);

        void renderCheckoutCartSuccess(CheckoutData checkoutData);

        void renderCheckoutCartError(String message);

        void renderCheckPromoCodeFromSuggestedPromoSuccess(CheckPromoCodeCartListResult promoCodeCartListData);

        void renderErrorCheckPromoCodeFromSuggestedPromo(String message);

        void renderErrorCheckPromoShipmentData(String message);

        void renderCheckPromoShipmentDataSuccess(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult);

        Activity getActivity();
    }

    interface Presenter extends CustomerPresenter<View> {
        void processCheckShipmentPrepareCheckout();

        void processCheckout();

        void processVerifyPayment(String transactionId);

        void checkPromoShipment();

        void processCheckPromoCodeFromSuggestedPromo(String promoCode);

        RecipientAddressModel getRecipientAddressModel();

        void setRecipientAddressModel(RecipientAddressModel recipientAddressModel);

        List<ShipmentCartItem> getShipmentCartItemList();

        void setShipmentCartItemList(List<ShipmentCartItem> recipientCartItemList);

        PromoCodeAppliedData getPromoCodeAppliedData();

        void setPromoCodeAppliedData(PromoCodeAppliedData promoCodeAppliedData);

        CartPromoSuggestion getCartPromoSuggestion();

        void setCartPromoSuggestion(CartPromoSuggestion cartPromoSuggestion);

        CheckoutData getCheckoutData();

        void setCheckoutData(CheckoutData checkoutData);

        List<DataCheckoutRequest> getDataCheckoutRequestList();

        void setDataCheckoutRequestList(List<DataCheckoutRequest> dataCheckoutRequestList);

        List<CheckPromoCodeCartShipmentRequest.Data> getPromoCodeCartShipmentRequestData();

        void setPromoCodeCartShipmentRequestData(
                List<CheckPromoCodeCartShipmentRequest.Data> promoCodeCartShipmentRequestData
        );

        ShipmentCostModel getShipmentCostModel();

        void setShipmentCostModel(ShipmentCostModel shipmentCostModel);
    }

}
