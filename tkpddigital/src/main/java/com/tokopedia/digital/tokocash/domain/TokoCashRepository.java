package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.core.network.apiservices.transaction.TokoCashService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.tokocash.entity.TokoCashHistoryEntity;
import com.tokopedia.digital.tokocash.mapper.ActivateTokoCashMapper;
import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;

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

    public TokoCashRepository(TokoCashService tokoCashService) {
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

    @Override
    public Observable<TokoCashHistoryEntity> getTokoCashHistoryData(String type, String startDate,
                                                                    String endDate, String afterId) {
        return tokoCashService.getApi().getHistoryTokocash(type, startDate, endDate, afterId)
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<TokoCashHistoryEntity>>() {
                    @Override
                    public Observable<TokoCashHistoryEntity> call(Response<TkpdDigitalResponse> response) {
                        return Observable
                                .just(response.body().convertDataObj(TokoCashHistoryEntity.class));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
