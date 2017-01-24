package com.tokopedia.core.drawer2.interactor;

import android.content.Context;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 1/23/17.
 */

public interface NotificationNetworkInteractor {

    Observable<Response<TkpdResponse>> getNotification(Context context,
                                                       TKPDMapParam<String, String> params);

}
