package com.tokopedia.core.drawer.listener;

import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;

/**
 * Created by kris on 4/21/17. Tokopedia
 */

public interface TokoCashUpdateListener {

    void onReceivedTokoCashData(DrawerTokoCash tokoCashData);

    void onTokoCashDataError(String errorMessage);
}
