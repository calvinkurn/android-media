package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.core.network.apiservices.transaction.TokoCashService;
import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;
import com.tokopedia.core.otp.data.mapper.RequestOtpMapper;
import com.tokopedia.core.otp.data.mapper.ValidateOtpMapper;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class ActivateTokoCashRepository implements IActivateTokoCashRepository {

    private final TokoCashService tokoCashService;
    private final RequestOtpMapper requestOtpMapper;
    private final ValidateOtpMapper validateOtpMapper;

    public ActivateTokoCashRepository(TokoCashService tokoCashService) {
        this.tokoCashService = tokoCashService;
        this.requestOtpMapper = new RequestOtpMapper();
        this.validateOtpMapper = new ValidateOtpMapper();
    }


    @Override
    public Observable<RequestOtpModel> requestOTPWallet() {
        return tokoCashService.getApi()
                .requestOtpWallet()
                .map(requestOtpMapper)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ValidateOtpModel> linkedWalletToTokoCash(String otpCode) {
        return tokoCashService.getApi()
                .linkedWalletToTokocash(otpCode)
                .map(validateOtpMapper)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
