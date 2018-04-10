package com.tokopedia.checkout.view.view.shipmentform;

import android.support.annotation.NonNull;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;

import java.util.HashMap;
import java.util.List;

import rx.Subscriber;

import static com.tokopedia.transaction.common.constant.PickupPointConstant.Params.DEFAULT_PAGE;
import static com.tokopedia.transaction.common.constant.PickupPointConstant.Params.PARAM_DISTRICT_ID;
import static com.tokopedia.transaction.common.constant.PickupPointConstant.Params.PARAM_PAGE;
import static com.tokopedia.transaction.common.constant.PickupPointConstant.Params.PARAM_TOKEN;
import static com.tokopedia.transaction.common.constant.PickupPointConstant.Params.PARAM_UT;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressShipmentPresenter {

    CheckoutRequest generateCheckoutRequest(List<MultipleAddressShipmentAdapterData> shipmentData,
                                            MultipleAddressPriceSummaryData priceData, String promoCode);

    List<MultipleAddressShipmentAdapterData> initiateAdapterData(
            CartShipmentAddressFormData dataFromWebService
    );

    CheckPromoCodeCartShipmentRequest generateCheckPromoRequest(
            List<MultipleAddressShipmentAdapterData> shipmentData, CartItemPromoHolderData appliedPromo
    );

    CartItemPromoHolderData generateCartPromoHolderData(PromoCodeAppliedData appliedPromoData);

    Subscriber<CheckPromoCodeCartShipmentResult> checkPromoSubscription(
            CartItemPromoHolderData cartItemPromoHolderData);

    void processCheckShipmentFormPrepareCheckout();

    void processCheckPromoCodeFromSuggestedPromo(String promoCode);

    HashMap<String, String> generatePickupPointParams(MultipleAddressShipmentAdapterData addressAdapterData);

}
