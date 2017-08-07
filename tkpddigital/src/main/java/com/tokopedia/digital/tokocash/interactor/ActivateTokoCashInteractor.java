package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.digital.tokocash.domain.ActivateTokoCashRepository;
import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class ActivateTokoCashInteractor implements IActivateTokoCashInteractor {

    private final ActivateTokoCashRepository repository;
    private final CompositeSubscription compositeSubscription;

    public ActivateTokoCashInteractor(ActivateTokoCashRepository repository) {
        this.repository = repository;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void requestOTPWallet(Subscriber<ActivateTokoCashData> subscriber) {
        compositeSubscription.add(
                repository.requestOTPWallet()
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void activateTokoCash(String otpCode, Subscriber<ActivateTokoCashData> subscriber) {
        compositeSubscription.add(
                repository.linkedWalletToTokoCash(otpCode)
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }
}