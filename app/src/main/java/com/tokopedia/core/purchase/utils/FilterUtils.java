package com.tokopedia.core.purchase.utils;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.R;
import com.tokopedia.core.purchase.model.TxFilterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * FilterUtils
 * Created by Angga.Prasetiyo on 25/04/2016.
 */
public class FilterUtils {
    public static final String ALL_STATUS_FILTER_ID = "";
    public static final String PAYMENT_CONFIRMATION_FILTER_ID = "1";
    public static final String PAYMENT_VERIFICATION_FILTER_ID = "2";
    public static final String PROCESSING_TRANSACTION_FILTER_ID = "8";
    public static final String ONGOING_DELIVERY_FILTER_ID = "3";
    public static final String TRANSACTION_DELIVERED_FILTER_ID = "9";
    public static final String TRANSACTION_DONE_FILTER_ID = "4";
    public static final String TRANSACTION_CANCELED_FILTER_ID = "5";

    public static List<TxFilterItem> filterTxAllItems(Context context) {
        List<TxFilterItem> list = new ArrayList<>();
        list.add(new TxFilterItem(ALL_STATUS_FILTER_ID, context.getString(R.string.item_all_stats)));
        list.add(new TxFilterItem(PAYMENT_CONFIRMATION_FILTER_ID, context.getString(R.string.item_payment_confirmation)));
        list.add(new TxFilterItem(PAYMENT_VERIFICATION_FILTER_ID, context.getString(R.string.item_payment_verification)));
        list.add(new TxFilterItem(PROCESSING_TRANSACTION_FILTER_ID, context.getString(R.string.item_on_process)));
        list.add(new TxFilterItem(ONGOING_DELIVERY_FILTER_ID, context.getString(R.string.item_on_shipping)));
        list.add(new TxFilterItem(TRANSACTION_DELIVERED_FILTER_ID, context.getString(R.string.item_delivered)));
        list.add(new TxFilterItem(TRANSACTION_DONE_FILTER_ID, context.getString(R.string.item_finished)));
        list.add(new TxFilterItem(TRANSACTION_CANCELED_FILTER_ID, context.getString(R.string.item_cancel)));
        return list;
    }

    public static boolean isStateCanceledTransaction(Activity activity) {
        boolean result = false;
        if (activity.getIntent().hasExtra("state")) {
            String stringExtra = activity.getIntent().getStringExtra("state");
            if (stringExtra != null && "cancel".equals(stringExtra)) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isStateCanceledTransaction(String state) {
        return state != null && "cancel".equals(state);
    }

    public static boolean isChangedFilter(String txFilterBefore, String txFilterID) {
        return !txFilterBefore.equalsIgnoreCase(txFilterID);
    }
}
