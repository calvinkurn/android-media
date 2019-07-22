package com.tokopedia.shipping_recommendation.logisticinsurance;

public class NoOpRuntimeException extends RuntimeException {

    public NoOpRuntimeException() {
        super("This no op module. Please use complete module :features:logistic:logisticinsurance");
    }
}
