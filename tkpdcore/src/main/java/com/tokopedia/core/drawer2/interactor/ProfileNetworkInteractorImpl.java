package com.tokopedia.core.drawer2.interactor;

import android.content.Context;

import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 1/23/17.
 */

public class ProfileNetworkInteractorImpl implements ProfileNetworkInteractor {

    private PeopleService peopleService;

    public ProfileNetworkInteractorImpl(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public Observable<Response<TkpdResponse>> getProfileInfo(Context context, TKPDMapParam<String, String> params) {
        return peopleService.getApi()
                .getPeopleInfo(AuthUtil.generateParamsNetwork(context,params));
    }


}
