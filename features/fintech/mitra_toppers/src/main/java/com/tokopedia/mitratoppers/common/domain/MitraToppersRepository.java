package com.tokopedia.mitratoppers.common.domain;

import com.tokopedia.mitratoppers.preapprove.data.source.cache.MitraToppersPreApproveDataCache;
import com.tokopedia.mitratoppers.preapprove.data.source.cloud.MitraToppersPreApproveDataCloud;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class MitraToppersRepository {
    private final MitraToppersPreApproveDataCloud mitraToppersPreApproveDataCloud;
    private final MitraToppersPreApproveDataCache mitraToppersPreApproveDataCache;

    @Inject
    public MitraToppersRepository(MitraToppersPreApproveDataCloud mitraToppersPreApproveDataCloud,
                                  MitraToppersPreApproveDataCache mitraToppersPreApproveDataCache) {
        this.mitraToppersPreApproveDataCloud = mitraToppersPreApproveDataCloud;
        this.mitraToppersPreApproveDataCache = mitraToppersPreApproveDataCache;
    }

    public Observable<ResponsePreApprove> getPreApproveBalance() {
        return this.mitraToppersPreApproveDataCache.getPreApproveBalance()
                .flatMap(new Func1<ResponsePreApprove, Observable<ResponsePreApprove>>() {
            @Override
            public Observable<ResponsePreApprove> call(ResponsePreApprove responsePreApprove) {
                if (responsePreApprove == null) {
                    return mitraToppersPreApproveDataCloud.getPreApproveBalance()
                            .doOnNext(new Action1<ResponsePreApprove>() {
                                @Override
                                public void call(ResponsePreApprove responsePreApprove) {
                                    mitraToppersPreApproveDataCache.savePreApproveBalance(responsePreApprove);
                                }
                            });
                } else {
                    return Observable.just(responsePreApprove);
                }
            }
        });
    }
}
