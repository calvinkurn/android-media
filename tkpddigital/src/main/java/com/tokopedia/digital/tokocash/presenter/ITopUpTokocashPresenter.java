package com.tokopedia.digital.tokocash.presenter;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;

/**
 * Created by nabillasabbaha on 8/21/17.
 */

public interface ITopUpTokocashPresenter {

    void processGetCategoryTopUp();

    void processGetBalanceTokoCash();

    void processAddToCartProduct(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct);

}
