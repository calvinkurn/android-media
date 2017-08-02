package com.tokopedia.digital.product.presenter;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.PulsaBalance;

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

}
