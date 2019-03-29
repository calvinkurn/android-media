package com.tokopedia.searchbar;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * Created by meta on 13/07/18.
 */
public interface SearchBarRouter {

    AnalyticTracker getAnalyticTracker();

    Intent gotoWishlistPage(Context context);

    Intent gotoQrScannerPage(boolean needResult);

    Intent gotoSearchPage(Context context);

    Intent gotoInboxMainPage(Context context);
}