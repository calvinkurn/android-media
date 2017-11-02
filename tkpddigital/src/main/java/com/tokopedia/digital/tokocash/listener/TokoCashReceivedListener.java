package com.tokopedia.digital.tokocash.listener;


import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;

/**
 * Created by kris on 7/17/17. Tokopedia
 */

public interface TokoCashReceivedListener {

    void onReceivedTokoCashData(TokoCashData tokoCashData);

    void onTokoCashDataError(String errorMessage);
}
