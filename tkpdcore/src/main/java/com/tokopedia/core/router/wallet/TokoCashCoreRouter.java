package com.tokopedia.core.router.wallet;

import android.content.BroadcastReceiver;

/**
 * Created by nabillasabbaha on 04/05/18.
 */
public interface TokoCashCoreRouter {

    /**
     *
     * @return BroadcastReceiver for pending cashback
     */
    BroadcastReceiver getBroadcastReceiverTokocashPending();
}
