package com.tokopedia.searchbar;

import android.content.Intent;

/**
 * Created by meta on 13/07/18.
 */
public interface SearchBarRouter {

    Intent gotoQrScannerPage(boolean needResult);
}