package com.tokopedia.digital.cart.presenter;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;

/**
 * @author anggaprasetiyo on 2/24/17.
 */

public interface ICartDigitalPresenter {

    void processGetCartData();

    void processAddToCart(DigitalCheckoutPassData passData);

    void processCheckVoucher();
}
