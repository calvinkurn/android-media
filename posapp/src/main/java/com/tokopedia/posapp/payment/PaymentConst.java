package com.tokopedia.posapp.payment;

/**
 * @author okasurya on 4/25/18.
 */

public class PaymentConst {
    public static final String CHECKOUT_DATA = "checkout_data";

    public class Parameter {

    }

    public class TransactionStatus {
        public static final int SUCCESS = 1;
        public static final int PENDING = 2;
        public static final int FAILED = 3;
    }
}
