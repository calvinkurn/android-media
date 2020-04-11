package com.tokopedia.logout.data

import com.tokopedia.logout.domain.pojo.LogoutPojo
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable
import java.util.HashMap

/**
 * @author by nisie on 5/30/18.
 * Please include fingerprint retrofit when building this API.
 */
interface LogoutApi{

    @FormUrlEncoded
    @POST(LogoutUrl.PATH_LOGOUT)
    fun logout(@FieldMap params: HashMap<String, Any>):
            Observable<Response<LogoutPojo>>
}