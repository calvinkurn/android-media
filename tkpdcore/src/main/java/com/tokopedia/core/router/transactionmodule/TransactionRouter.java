package com.tokopedia.core.router.transactionmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kris on 7/21/17. Tokopedia
 */

public interface TransactionRouter {

    void goToUserPaymentList(Activity activity);

    void goToOrderHistory(Context context, String orderId, int userMode);

    Intent getInboxReputationIntent(Context context);

    Intent getDetailResChatIntentBuyer(Context context, String resoId, String shopName);

    Intent getResolutionCenterIntent(Context context);
}
