package com.tokopedia.digital.tokocash.listener;

import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashData;

/**
 * Created by kris on 7/17/17. Tokopedia
 */

public interface TokoCashReceivedListener {

    void onReceivedTokoCashData(TokoCashData tokoCashData);

    void onTokoCashDataError(String errorMessage);
}
