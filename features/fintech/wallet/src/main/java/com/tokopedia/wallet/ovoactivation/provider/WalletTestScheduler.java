package com.tokopedia.wallet.ovoactivation.provider;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
public class WalletTestScheduler implements WalletProvider {

    @Override
    public Scheduler computation() {
        return Schedulers.immediate();
    }

    @Override
    public Scheduler uiScheduler() {
        return Schedulers.immediate();
    }
}
