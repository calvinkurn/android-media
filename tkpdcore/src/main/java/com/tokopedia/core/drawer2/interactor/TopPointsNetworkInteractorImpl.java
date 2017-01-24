package com.tokopedia.core.drawer2.interactor;

import android.content.Context;

import com.tokopedia.core.network.apiservices.clover.CloverService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 1/24/17.
 */

public class TopPointsNetworkInteractorImpl implements TopPointsNetworkInteractor {


    private CloverService cloverService;

    public TopPointsNetworkInteractorImpl(CloverService cloverService) {
        this.cloverService = cloverService;
    }

    @Override
    public Observable<Response<TkpdResponse>> getTopPoints(Context context,
                                                           TKPDMapParam<String, String> params) {
        return cloverService.getApi()
                .getTopPoints(AuthUtil.generateParamsNetwork(context, params));
    }
}
