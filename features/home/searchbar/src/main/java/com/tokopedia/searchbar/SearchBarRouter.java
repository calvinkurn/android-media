package com.tokopedia.searchbar;

import android.content.Context;
import android.content.Intent;

/**
 * Created by meta on 13/07/18.
 */
public interface SearchBarRouter {

    Intent gotoWishlistPage(Context context);

    Intent gotoQrScannerPage(boolean needResult);

    Intent gotoSearchPage(Context context);

    Intent gotoInboxMainPage(Context context);
}