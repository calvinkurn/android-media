package com.tokopedia.tokocash.common;

import rx.Scheduler;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
public interface WalletProvider {

    Scheduler computation();

    Scheduler uiScheduler();
}
