package com.tokopedia.loginregister.registerinitial.domain.mapper

import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterValidationPojo
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 10/25/18.
 */
class RegisterValidationMapper @Inject constructor() : Func1<Response<DataResponse<RegisterValidationPojo?>?>, RegisterValidationPojo?> {
    override fun call(response: Response<DataResponse<RegisterValidationPojo?>?>): RegisterValidationPojo? {
        return if (response.isSuccessful && response.body() != null) {
            response.body()?.data
        } else {
            throw RuntimeException(response.code().toString())
        }
    }
}