package com.tokopedia.age_restriction.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.age_restriction.data.UserDOBUpdateResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class VerifyDOBViewModel : BaseViewModel(), CoroutineScope {

    val userIsAdult = MutableLiveData<Boolean>()
    val userNotAdult = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun updateUserDoB(query: String, bdayDD: String, bdayMM: String, bdayYY: String) {
        progBarVisibility.value = true
        launchCatchError(block = {
            val dobMap = HashMap<String, String>()
            dobMap["bdayDD"] = bdayDD
            dobMap["bdayMM"] = bdayMM
            dobMap["bdayYY"] = bdayYY

            val response = repository?.getGQLData(query, UserDOBUpdateResponse::class.java, dobMap) as UserDOBUpdateResponse
            checkIfAdult(response)
        }, onError = {
            it.printStackTrace()
            warningMessage.value = it.localizedMessage
        })
    }

    private fun checkIfAdult(userDOBUpdateResponse: UserDOBUpdateResponse) {
        userDOBUpdateResponse.userDobUpdateData.error.let {
            if (it.isNotEmpty()) {
                progBarVisibility.value = false
                warningMessage.value = it
                return
            }

        }
        if (userDOBUpdateResponse.userDobUpdateData.isDobVerified) {
            if (userDOBUpdateResponse.userDobUpdateData.age > 18)
                userIsAdult.value = true
            else
                userNotAdult.value = true
        }
    }
}
