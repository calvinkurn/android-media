package com.tokopedia.digital.tokocash.listener;

import com.tokopedia.digital.tokocash.model.tokocashitem.TopCashItem;

/**
 * Created by kris on 7/17/17. Tokopedia
 */

public interface TokoCashReceivedListener {

    void onReceivedTokoCashData(TopCashItem tokoCashData);

    void onTokoCashDataError(String errorMessage);
}
