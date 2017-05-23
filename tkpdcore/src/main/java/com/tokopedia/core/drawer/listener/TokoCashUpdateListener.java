package com.tokopedia.core.drawer.listener;

import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;

/**
 * Created by kris on 4/21/17. Tokopedia
 */

public interface TokoCashUpdateListener {

    void onReceivedTokoCashData(TopCashItem tokoCashData);

    void onTokoCashDataError(String errorMessage);
}
