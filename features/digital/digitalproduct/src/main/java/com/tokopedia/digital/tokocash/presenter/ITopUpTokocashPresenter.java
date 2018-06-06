package com.tokopedia.digital.tokocash.presenter;

import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;

/**
 * Created by nabillasabbaha on 8/21/17.
 */

public interface ITopUpTokocashPresenter {

    void processGetCategoryTopUp();

    void processGetBalanceTokoCash();

    void getTokenWallet();

    void processAddToCartProduct(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct);

}
