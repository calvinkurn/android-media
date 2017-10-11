package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.core.network.apiservices.transaction.TokoCashService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.digital.tokocash.entity.WalletTokenEntity;
import com.tokopedia.digital.tokocash.mapper.ActivateTokoCashMapper;
import com.tokopedia.digital.tokocash.mapper.TokenTokoCashMapper;
import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashData;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class TokoCashRepository implements ITokoCashRepository {

    private final TokoCashService tokoCashService;
    private final ActivateTokoCashMapper activateTokoCashMapper;
    private final TokenTokoCashMapper tokenTokoCashMapper;

    public TokoCashRepository(TokoCashService tokoCashService) {
        this.tokoCashService = tokoCashService;
        this.activateTokoCashMapper = new ActivateTokoCashMapper();
        tokenTokoCashMapper = new TokenTokoCashMapper();
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

    @Override
    public Observable<TokoCashData> getBalanceTokoCash() {
        return tokoCashService.getApi().getTokoCash()
                .flatMap(new Func1<Response<TkpdResponse>, Observable<TokoCashData>>() {
                    @Override
                    public Observable<TokoCashData> call(Response<TkpdResponse> topCashItemResponse) {
                        return Observable
                                .just(topCashItemResponse.body().convertDataObj(TokoCashData.class));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<WalletTokenEntity> getWalletToken() {
        return tokoCashService.getApi().getTokenWallet()
                .map(tokenTokoCashMapper)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
