package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.core.network.apiservices.transaction.TokoCashService;
import com.tokopedia.digital.tokocash.mapper.ActivateTokoCashMapper;
import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class ActivateTokoCashRepository implements IActivateTokoCashRepository {

    private final TokoCashService tokoCashService;
    private final ActivateTokoCashMapper activateTokoCashMapper;

    public ActivateTokoCashRepository(TokoCashService tokoCashService) {
        this.tokoCashService = tokoCashService;
        this.activateTokoCashMapper = new ActivateTokoCashMapper();
    }

    @Override
    public Observable<ActivateTokoCashData> requestOTPWallet() {
        return tokoCashService.getApi()
                .requestOtpWallet()
                .map(activateTokoCashMapper)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ActivateTokoCashData> linkedWalletToTokoCash(String otpCode) {
        return tokoCashService.getApi()
                .linkedWalletToTokocash(otpCode)
                .map(activateTokoCashMapper)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
