package com.tokopedia.checkout.view.view.cartlist;

import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartTickerErrorData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.view.base.IBaseView;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;

import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListView extends IBaseView {

    void renderInitialGetCartListDataSuccess(CartListData cartListData);

    void renderErrorInitialGetCartListData(String message);

    void renderErrorHttpInitialGetCartListData(String message);

    void renderErrorNoConnectionInitialGetCartListData(String message);

    void renderErrorTimeoutConnectionInitialGetCartListData(String message);


    void renderActionDeleteCartDataSuccess(CartItemData cartItemData, String message, boolean addWishList);

    void renderErrorActionDeleteCartData(String message);

    void renderErrorHttpActionDeleteCartData(String message);

    void renderErrorNoConnectionActionDeleteCartData(String message);

    void renderErrorTimeoutConnectionActionDeleteCartData(String message);


    void renderNoRecipientAddressShipmentForm(CartShipmentAddressFormData shipmentAddressFormData);

    void renderToShipmentFormSuccess(CartShipmentAddressFormData shipmentAddressFormData);

    void renderErrorToShipmentForm(String message);

    void renderErrorHttpToShipmentForm(String message);

    void renderErrorNoConnectionToShipmentForm(String message);

    void renderErrorTimeoutConnectionToShipmentForm(String message);


    void renderToShipmentMultipleAddressSuccess(CartListData cartListData, RecipientAddressModel selectedAddress);

    void renderErrorToShipmentMultipleAddress(String message);

    void renderErrorHttpToShipmentMultipleAddress(String message);

    void renderErrorNoConnectionToShipmentMultipleAddress(String message);

    void renderErrorTimeoutConnectionToShipmentMultipleAddress(String message);


    void renderCheckPromoCodeFromSuggestedPromoSuccess(CheckPromoCodeCartListResult promoCodeCartListData);

    void renderErrorCheckPromoCodeFromSuggestedPromo(String message);

    void renderErrorHttpCheckPromoCodeFromSuggestedPromo(String message);

    void renderErrorNoConnectionCheckPromoCodeFromSuggestedPromo(String message);

    void renderErrorTimeoutConnectionCheckPromoCodeFromSuggestedPromo(String message);

    void renderEmptyCartData(CartListData cartListData);

    void disableSwipeRefresh();

    void enableSwipeRefresh();

    List<CartItemData> getCartDataList();

    void renderDetailInfoSubTotal(String qty, String subtotalPrice);

    void renderPromoVoucher();

    void showToastMessageRed(String message);

    void renderLoadGetCartData();

    void renderLoadGetCartDataFinish();

    void renderCartTickerError(CartTickerErrorData cartTickerErrorData);

    void renderCancelAutoApplyCouponSuccess();

    void renderCancelAutoApplyCouponError();
}
