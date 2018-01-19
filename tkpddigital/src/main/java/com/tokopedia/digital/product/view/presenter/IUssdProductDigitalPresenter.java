package com.tokopedia.digital.product.view.presenter;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.PulsaBalance;

/**
 * Created by ashwanityagi on 19/07/17.
 */

public interface IUssdProductDigitalPresenter {
    String TAG = IUssdProductDigitalPresenter.class.getSimpleName();

    DigitalCheckoutPassData generateCheckoutPassData(
            Operator operator, PulsaBalance pulsaBalance, String categoryId, String categoryName,
            String productId, boolean isInstantCheckou
    );

    void processAddToCartProduct(DigitalCheckoutPassData digitalCheckoutPassData);

    void storeUssdPhoneNumber(int selectedSim,String number);

    String getUssdPhoneNumberFromCache(int selectedSim);
}
