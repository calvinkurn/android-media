package com.tokopedia.tkpd.network.apiservices.etc.apis;

import com.tokopedia.tkpd.myproduct.model.DepartmentParentModel;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * DepartmentApi
 * Created by Angga.Prasetiyo on 07/12/2015.
 */
public interface DepartmentApi {

    @GET(TkpdBaseURL.Etc.PATH_GET_DEPARTMENT_CHILD)
    Observable<Response<TkpdResponse>> getChild(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Etc.PATH_GET_DEPARTMENT_CHILD)
    Observable<DepartmentParentModel> getChild2(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Etc.PATH_GET_DEPARTMENT_PARENT)
    Observable<Response<TkpdResponse>> getParent(@QueryMap Map<String, String> params);
}
