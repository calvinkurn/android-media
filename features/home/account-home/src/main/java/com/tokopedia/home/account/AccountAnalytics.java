package com.tokopedia.home.account;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.home.account.presentation.AccountHomeRouter;

import static com.tokopedia.home.account.AccountConstants.Analytics.*;

/**
 * Created by meta on 04/08/18.
 */
public class AccountAnalytics {

    private AnalyticTracker analyticTracker;

    public AccountAnalytics(Context context) {
        if (context == null)
            return;

        if (context.getApplicationContext() instanceof AccountHomeRouter) {
            analyticTracker = ((AccountHomeRouter) context.getApplicationContext())
                    .getAnalyticTracker();
        }
    }

    public void eventClickAccount(String title, String section, String item) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                CLICK_HOME_PAGE,
                String.format("%s %s", AKUN_SAYA, title),
                String.format("%s - %s - %s", CLICK, section, item),
                ""
        );
    }
}
