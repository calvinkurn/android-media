package com.tokopedia.changephonenumber;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import okhttp3.Interceptor;

/**
 * @author by alvinatin on 01/10/18.
 */

public interface ChangePhoneNumberRouter {
    AnalyticTracker getAnalyticTracker();

    Intent getWithdrawIntent(Context context, boolean isSeller);

    Interceptor getChuckInterceptor();
}
