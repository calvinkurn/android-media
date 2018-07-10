package com.tokopedia.posapp.payment;

/**
 * @author okasurya on 4/25/18.
 */

public class PaymentConst {
    public static final String CHECKOUT_DATA = "checkout_data";

    public class Parameter {
        public static final String TRANSACTION_ID = "transaction_id";
    }

    public class TransactionStatus {
        public static final int PAYMENT_SUCCESS = 11;
        public static final int PAYMENT_PROGRESS = 12;
        public static final int PAYMENT_FAILED = 13;

        public static final int ORDER_SUCCESS = 21;
        public static final int ORDER_PROGRESS = 22;
        public static final int ORDER_FAILED = 23;
    }
}
