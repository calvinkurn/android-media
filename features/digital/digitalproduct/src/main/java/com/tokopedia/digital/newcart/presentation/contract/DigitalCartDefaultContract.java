package com.tokopedia.digital.newcart.presentation.contract;

import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.newcart.presentation.model.DigitalSubscriptionParams;

public interface DigitalCartDefaultContract {
    interface View extends DigitalBaseContract.View {
        void inflateDealsPage(CartDigitalInfoData cartDigitalInfoData,
                              DigitalCheckoutPassData cartPassData);

        void inflateMyBillsSubscriptionPage(CartDigitalInfoData cartDigitalInfoData,
                                            DigitalCheckoutPassData cartPassData,
                                            DigitalSubscriptionParams subParams);
    }

    interface Presenter extends DigitalBaseContract.Presenter<View> {

    }
}
