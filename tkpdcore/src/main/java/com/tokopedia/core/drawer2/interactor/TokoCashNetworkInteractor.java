package com.tokopedia.core.drawer2.interactor;

import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 1/24/17.
 */

public interface TokoCashNetworkInteractor {

    Observable<Response<TopCashItem>> getTokoCash(String accessToken);

}
