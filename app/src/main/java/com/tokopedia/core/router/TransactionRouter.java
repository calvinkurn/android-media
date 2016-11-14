package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by Nathaniel on 11/9/2016.
 */

public class TransactionRouter {

    private static final String TRANSACTION_PURCHASE_ACTIVITY = "com.tokopedia.transaction.purchase.activity.PurchaseActivity";
    private static final String TRANSACTION_TX_LIST_FRAGMENT = "com.tokopedia.transaction.purchase.fragment.TxListFragment";

    public static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";
    public static final String EXTRA_STATE_TX_FILTER = "EXTRA_STATE_TX_FILTER";
    public static final String EXTRA_UPDATE_BALANCE = "EXTRA_UPDATE_BALANCE";
    public static final int TAB_TX_SUMMARY = 0;
    public static final int TAB_TX_CONFIRMATION = 1;
    public static final int TAB_TX_VERIFICATION = 2;
    public static final int TAB_TX_STATUS = 3;
    public static final int TAB_TX_DELIVER = 4;
    public static final int TAB_TX_ALL = 5;

    public static final String ALL_STATUS_FILTER_ID = "";
    public static final String PAYMENT_CONFIRMATION_FILTER_ID = "1";
    public static final String PAYMENT_VERIFICATION_FILTER_ID = "2";
    public static final String PROCESSING_TRANSACTION_FILTER_ID = "8";
    public static final String ONGOING_DELIVERY_FILTER_ID = "3";
    public static final String TRANSACTION_DELIVERED_FILTER_ID = "9";
    public static final String TRANSACTION_DONE_FILTER_ID = "4";
    public static final String TRANSACTION_CANCELED_FILTER_ID = "5";
    public static final int CREATE_RESCENTER_REQUEST_CODE = 789;
    public static final String ARG_PARAM_EXTRA_INSTANCE_TYPE = "ARG_PARAM_EXTRA_INSTANCE_TYPE";
    public static final int INSTANCE_ALL = 3;
    public static final String ARG_PARAM_EXTRA_INSTANCE_FILTER = "ARG_PARAM_EXTRA_INSTANCE_FILTER";
    public static final String ARG_PARAM_EXTRA_INSTANCE_FROM_NOTIFICATION
            = "ARG_PARAM_EXTRA_INSTANCE_FROM_NOTIFICATION";

    public static Intent createIntentPurchaseActivity(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, TRANSACTION_PURCHASE_ACTIVITY);
        return intent;
    }

    public static Intent createIntentTxSummary(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, TRANSACTION_PURCHASE_ACTIVITY);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_SUMMARY);
        intent.putExtra(EXTRA_UPDATE_BALANCE, true);
        return intent;
    }

    public static Intent createIntentTxCanceled(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, TRANSACTION_PURCHASE_ACTIVITY);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_ALL);
        intent.putExtra(EXTRA_STATE_TX_FILTER, TRANSACTION_CANCELED_FILTER_ID);
        return intent;
    }

    public static Intent createIntentTxAll(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, TRANSACTION_PURCHASE_ACTIVITY);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_ALL);
        intent.putExtra(EXTRA_STATE_TX_FILTER, ALL_STATUS_FILTER_ID);
        return intent;
    }

    public static Intent createIntentConfirmShipping(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, TRANSACTION_PURCHASE_ACTIVITY);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_DELIVER);
        return intent;
    }

    public static Intent createIntentTxVerification(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, TRANSACTION_PURCHASE_ACTIVITY);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_VERIFICATION);
        return intent;
    }

    public static Intent createIntentTxStatus(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, TRANSACTION_PURCHASE_ACTIVITY);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_STATUS);
        return intent;
    }

    public static Intent createIntentConfirmPayment(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, TRANSACTION_PURCHASE_ACTIVITY);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_CONFIRMATION);
        return intent;
    }

    public static ComponentName getPurchaseActivityComponentName(Context context) {
        return RouterUtils.getActivityComponentName(context, TRANSACTION_PURCHASE_ACTIVITY);
    }

    public static Fragment instanceTxListFromNotification(Context context, String txFilterID) {
        Fragment fragment = Fragment.instantiate(context, TRANSACTION_TX_LIST_FRAGMENT);
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PARAM_EXTRA_INSTANCE_TYPE, INSTANCE_ALL);
        bundle.putString(ARG_PARAM_EXTRA_INSTANCE_FILTER, txFilterID);
        bundle.putBoolean(ARG_PARAM_EXTRA_INSTANCE_FROM_NOTIFICATION, true);
        fragment.setArguments(bundle);
        return fragment;
    }
}