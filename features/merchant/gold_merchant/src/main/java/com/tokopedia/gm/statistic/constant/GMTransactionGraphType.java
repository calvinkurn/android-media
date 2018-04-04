package com.tokopedia.gm.statistic.constant;

/**
 * Created by normansyahputa on 7/11/17.
 */

import android.support.annotation.IntDef;

import static com.tokopedia.gm.statistic.constant.GMTransactionGraphType.GROSS_REVENUE;
import static com.tokopedia.gm.statistic.constant.GMTransactionGraphType.NET_REVENUE;
import static com.tokopedia.gm.statistic.constant.GMTransactionGraphType.REJECTED_AMOUNT;
import static com.tokopedia.gm.statistic.constant.GMTransactionGraphType.REJECT_TRANS;
import static com.tokopedia.gm.statistic.constant.GMTransactionGraphType.SHIPPING_COST;
import static com.tokopedia.gm.statistic.constant.GMTransactionGraphType.SUCCESS_TRANS;
import static com.tokopedia.gm.statistic.constant.GMTransactionGraphType.TOTAL_TRANSACTION;

@IntDef({TOTAL_TRANSACTION, GROSS_REVENUE, NET_REVENUE, REJECTED_AMOUNT, SHIPPING_COST, SUCCESS_TRANS, REJECT_TRANS})
public @interface GMTransactionGraphType {
    int TOTAL_TRANSACTION = 0;
    int GROSS_REVENUE = 1;
    int NET_REVENUE = 2;
    int REJECTED_AMOUNT = 3;
    int SHIPPING_COST = 4;
    int SUCCESS_TRANS = 5;
    int REJECT_TRANS = 6;
}
