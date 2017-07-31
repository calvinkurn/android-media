package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;
import com.tokopedia.digital.tokocash.domain.ActivateTokoCashRepository;
import com.tokopedia.digital.tokocash.errorhandle.ResponseTokoCashRuntimeException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
    public void requestOTPWallet(Subscriber<RequestOtpModel> subscriber) {
        compositeSubscription.add(
                repository.requestOTPWallet()
                        .doOnNext(new Action1<RequestOtpModel>() {
                            @Override
                            public void call(RequestOtpModel requestOtpModel) {
                                if (!requestOtpModel.getErrorMessage().isEmpty()) {
                                    throw new ResponseTokoCashRuntimeException(requestOtpModel.getErrorMessage());
                                }
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void activateTokoCash(String otpCode, Subscriber<ValidateOtpModel> subscriber) {
        compositeSubscription.add(
                repository.linkedWalletToTokoCash(otpCode)
                        .doOnNext(new Action1<ValidateOtpModel>() {
                            @Override
                            public void call(ValidateOtpModel validateOtpModel) {
                                if (!validateOtpModel.getErrorMessage().isEmpty()) {
                                    throw new ResponseTokoCashRuntimeException(validateOtpModel.getErrorMessage());
                                }
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void onDestroy() {
        compositeSubscription.unsubscribe();
    }
}