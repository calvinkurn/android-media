package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.DeleteAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.DeleteUpdateCartData;
import com.tokopedia.checkout.domain.datamodel.ResetAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.ResetAndShipmentFormCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateToSingleAddressShipmentData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartListInteractor {

    void deleteCart(Subscriber<DeleteCartData> subscriber, TKPDMapParam<String, String> param);

    void deleteCartAndRefreshCartList(Subscriber<DeleteAndRefreshCartListData> subscriber,
                                      TKPDMapParam<String, String> paramDelete,
                                      TKPDMapParam<String, String> paramGetCartList);

    void deleteAndUpdateCart(Subscriber<DeleteUpdateCartData> subscriber,
                             TKPDMapParam<String, String> paramDelete,
                             TKPDMapParam<String, String> paramUpdate);

    void updateCart(Subscriber<UpdateCartData> subscriber, TKPDMapParam<String, String> param);

    void updateCartToSingleAddressShipment(Subscriber<UpdateToSingleAddressShipmentData> subscriber,
                                           TKPDMapParam<String, String> paramUpdate,
                                           TKPDMapParam<String, String> paramGetShipmentForm);

    void checkPromoCodeCartList(
            Subscriber<PromoCodeCartListData> subscriber, TKPDMapParam<String, String> param
    );

    void getShipmentForm(Subscriber<CartShipmentAddressFormData> subscriber,
                         TKPDMapParam<String, String> param);

    void resetAndRefreshCartData(Subscriber<ResetAndRefreshCartListData> subscriber,
                                 TKPDMapParam<String, String> paramReset,
                                 TKPDMapParam<String, String> paramGetCart);

    void resetAndShipmentFormData(Subscriber<ResetAndShipmentFormCartData> subscriber,
                                  TKPDMapParam<String, String> paramReset,
                                  TKPDMapParam<String, String> paramShipmentForm);
}
