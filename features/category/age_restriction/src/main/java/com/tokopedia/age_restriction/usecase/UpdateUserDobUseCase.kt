package com.tokopedia.age_restriction.usecase

import com.tokopedia.age_restriction.ARQueryConstants.Companion.gql_user_profile_dob_update
import com.tokopedia.age_restriction.repository.ARRepository
import com.tokopedia.age_restriction.data.UserDOBUpdateResponse
import javax.inject.Inject
import javax.inject.Named

class UpdateUserDobUseCase @Inject constructor(@Named(gql_user_profile_dob_update) val updateDob: String, val repository: ARRepository) {


    suspend fun getData(bdayDD: String, bdayMM: String, bdayYY: String):UserDOBUpdateResponse {
        return repository.getGQLData(updateDob,
                UserDOBUpdateResponse::class.java,
                getDobMap(bdayDD,bdayMM,bdayYY))
    }

    fun getDobMap(bdayDD: String, bdayMM: String, bdayYY: String):HashMap<String, String>{
        val dobMap = HashMap<String, String>()
        dobMap["bdayDD"] = bdayDD
        dobMap["bdayMM"] = bdayMM
        dobMap["bdayYY"] = bdayYY
        return dobMap
    }

}