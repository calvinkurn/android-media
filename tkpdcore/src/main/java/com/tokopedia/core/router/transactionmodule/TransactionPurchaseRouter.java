package com.tokopedia.core.router.transactionmodule;

/**
 * Created by Nathaniel on 11/9/2016.
 */

public class TransactionPurchaseRouter {


    public static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";
    public static final String EXTRA_STATE_TX_FILTER = "EXTRA_STATE_TX_FILTER";

    // TODO: 7/30/18 oka: need to be deleted
    public final static int TAB_POSITION_PURCHASE_SUMMARY = 0;
    public final static int TAB_POSITION_PURCHASE_VERIFICATION = 1;
    public final static int TAB_POSITION_PURCHASE_STATUS_ORDER = 2;
    public final static int TAB_POSITION_PURCHASE_DELIVER_ORDER = 3;

    public final static int TAB_POSITION_PURCHASE_CONFIRMED = 0;
    public final static int TAB_POSITION_PURCHASE_PROCESSED = 1;
    public final static int TAB_POSITION_PURCHASE_SHIPPED = 2;
    public final static int TAB_POSITION_PURCHASE_DELIVERED = 3;
    public final static int TAB_POSITION_PURCHASE_ALL_ORDER = 4;

    public static final String ALL_STATUS_FILTER_ID = "";
    public static final String PAYMENT_CONFIRMATION_FILTER_ID = "1";
    public static final String PAYMENT_VERIFICATION_FILTER_ID = "2";
    public static final String PROCESSING_TRANSACTION_FILTER_ID = "8";
    public static final String ONGOING_DELIVERY_FILTER_ID = "3";
    public static final String TRANSACTION_DELIVERED_FILTER_ID = "9";
    public static final String TRANSACTION_DONE_FILTER_ID = "4";
    public static final String TRANSACTION_CANCELED_FILTER_ID = "5";


    public static final String TRANSACTION_CONFIRMED_FILTER_ID = PAYMENT_VERIFICATION_FILTER_ID;
    public static final String TRANSACTION_PROCESSED_FILTER_ID = PROCESSING_TRANSACTION_FILTER_ID;
    public static final String TRANSACTION_SHIPPED_FILTER_ID = ONGOING_DELIVERY_FILTER_ID;

    public static final int CREATE_RESCENTER_REQUEST_CODE = 789;
    public static final String ARG_PARAM_EXTRA_INSTANCE_TYPE = "ARG_PARAM_EXTRA_INSTANCE_TYPE";
    public static final int INSTANCE_ALL = 3;
    public static final String ARG_PARAM_EXTRA_INSTANCE_FILTER = "ARG_PARAM_EXTRA_INSTANCE_FILTER";
    public static final String ARG_PARAM_EXTRA_INSTANCE_FROM_NOTIFICATION
            = "ARG_PARAM_EXTRA_INSTANCE_FROM_NOTIFICATION";


}