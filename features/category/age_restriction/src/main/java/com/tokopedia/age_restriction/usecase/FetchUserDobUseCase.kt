package com.tokopedia.age_restriction.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.age_restriction.repository.ARRepository
import com.tokopedia.age_restriction.data.UserDOBResponse
import com.tokopedia.common.network.data.model.RequestType
import javax.inject.Inject

class FetchUserDobUseCase @Inject constructor(val repository: ARRepository) {


    suspend fun getData(userDobPath: String): UserDOBResponse {
        return (repository.getRestData(userDobPath,
                object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                RequestType.GET) as DataResponse<UserDOBResponse>).data
    }

}