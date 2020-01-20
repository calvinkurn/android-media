package com.tokopedia.wallet.ovoactivation.provider;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
public class WalletScheduler implements WalletProvider {

    @Override
    public Scheduler computation() {
        return Schedulers.io();
    }

    @Override
    public Scheduler uiScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
