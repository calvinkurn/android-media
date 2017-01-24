package com.tokopedia.core.drawer2.interactor;

import android.content.Context;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 1/23/17.
 */

public interface ProfileNetworkInteractor {

    Observable<Response<TkpdResponse>> getProfileInfo(Context context,
                                                             TKPDMapParam<String, String> params);
}
