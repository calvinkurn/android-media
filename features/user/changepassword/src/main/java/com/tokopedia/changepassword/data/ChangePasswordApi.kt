package com.tokopedia.changepassword.data

import com.tokopedia.changepassword.domain.pojo.ChangePasswordPojo
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable
import java.util.*

/**
 * @author by nisie on 7/25/18.
 */
interface ChangePasswordApi {

    @FormUrlEncoded
    @POST(ChangePasswordUrl.PATH_CHANGE_PASSWORD)
    fun changePassword(@FieldMap params: HashMap<String, Any>):
            Observable<Response<ChangePasswordPojo>>

}