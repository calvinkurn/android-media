package com.tokopedia.core.share.fragment;

import android.content.BroadcastReceiver;

/**
 * Created by sebastianuskh on 2/10/17.
 */

public interface ProductShareFragmentCallback {
    void registerReceiver(BroadcastReceiver receiver, String intentFilter);

    void uregisterReceiver(BroadcastReceiver addProductReceiver);
}
