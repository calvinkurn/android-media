package com.tokopedia.searchbar;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * Created by meta on 13/07/18.
 */
public interface SearchBarRouter {

    AnalyticTracker getAnalyticTracker();

    Intent getLoginIntent(Context context);

    Intent gotoWishlistPage(Context context);

    Intent gotoNotificationPage(Context context);

    Intent gotoQrScannerPage();

    Intent gotoSearchPage(Context context);
}