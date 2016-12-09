package com.tokopedia.core.myproduct.api;

import com.tokopedia.core.myproduct.model.DepartmentParentModel;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by m.normansyah on 15/01/2016.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface Department {

    String V4_DEPARTMENT_GET_DEPARTMENT_PARENT_PL = "/v4/department/get_department_parent.pl";
    String V4_DEPARTMENT_GET_DEPARTMENT_CHILD_PL = "/v4/department/get_department_child.pl";
    String DEPARTMENT_ID = "department_id";

    @GET(V4_DEPARTMENT_GET_DEPARTMENT_CHILD_PL)
    Observable<DepartmentParentModel> getDepChild(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Query(NetworkCalculator.USER_ID) String userId,// 5
            @Query(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Query(NetworkCalculator.HASH) String hash,// 7
            @Query(NetworkCalculator.DEVICE_TIME) String deviceTime,// 8
            @Query(DEPARTMENT_ID) String departmentId
    );

    @FormUrlEncoded
    @POST(V4_DEPARTMENT_GET_DEPARTMENT_PARENT_PL)
    Observable<DepartmentParentModel> getDepParent(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Field(NetworkCalculator.USER_ID) String userId,// 5
            @Field(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Field(NetworkCalculator.HASH) String hash,// 7
            @Field(NetworkCalculator.DEVICE_TIME) String deviceTime// 8
    );
}
