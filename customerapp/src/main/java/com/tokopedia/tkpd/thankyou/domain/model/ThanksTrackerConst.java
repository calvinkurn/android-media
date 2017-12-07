package com.tokopedia.tkpd.thankyou.domain.model;

/**
 * Created by okasurya on 12/4/17.
 */

public interface ThanksTrackerConst {
    interface Platform {
        String DIGITAL = "digital";
        String MARKETPLACE = "marketplace";
    }

    interface Template {
        String INSTANT_PAYMENT = "instant_payment";
    }

    interface Key {
        String ID = "transaction_id";
        String PLATFORM = "platform";
        String TEMPLATE = "template";
    }

}
