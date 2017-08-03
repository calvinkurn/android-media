package com.tokopedia.core.router.transactionmodule;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by kris on 7/21/17. Tokopedia
 */

public interface TransactionRouter {

    void goToBcaOneClick(Activity activity, Bundle bundle, int requestCode);

    void goToUserPaymentList(Activity activity);
}
