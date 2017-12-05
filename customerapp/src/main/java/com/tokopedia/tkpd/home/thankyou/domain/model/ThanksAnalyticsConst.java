package com.tokopedia.tkpd.home.thankyou.domain.model;

/**
 * Created by okasurya on 12/4/17.
 */

public interface ThanksAnalyticsConst {
    interface Platform {
        String DIGITAL = "digital";
        String MARKETPLACE = "marketplace";
    }

    interface Template {
        String INSTANT_PAYMENT = "instant_payment";
    }

    interface Key {
        String ID = "order_id";
        String PLATFORM = "platform";
        String TEMPLATE = "template";
    }

}
