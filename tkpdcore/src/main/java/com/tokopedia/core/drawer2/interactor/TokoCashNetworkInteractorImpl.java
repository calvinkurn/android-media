package com.tokopedia.core.drawer2.interactor;

import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.core.network.apiservices.transaction.TokoCashService;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 1/24/17.
 */

public class TokoCashNetworkInteractorImpl implements TokoCashNetworkInteractor {

    private TokoCashService tokoCashService;

    public TokoCashNetworkInteractorImpl(TokoCashService tokoCashService) {
        this.tokoCashService = tokoCashService;
    }

    @Override
    public Observable<Response<TopCashItem>> getTokoCash(String accessToken) {
        tokoCashService.setToken(accessToken);
        return tokoCashService.getApi().getTokoCash();
    }
}
