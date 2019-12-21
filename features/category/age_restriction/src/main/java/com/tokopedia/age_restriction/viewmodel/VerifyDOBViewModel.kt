package com.tokopedia.age_restriction.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.age_restriction.data.UserDOBUpdateResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class VerifyDOBViewModel(application: Application) : BaseViewModel(application), CoroutineScope {

    val userIsAdult = MutableLiveData<Boolean>()
    val userNotAdult = MutableLiveData<Boolean>()

    lateinit var response: UserDOBUpdateResponse

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun updateUserDoB(query: String, bdayDD: String, bdayMM: String, bdayYY: String) {
        progBarVisibility.value = true
        launchCatchError(block = {
            response = getRepo()?.getGQLData(query, UserDOBUpdateResponse::class.java,
                    generateDOBRequestparam(bdayDD, bdayMM, bdayYY)) as UserDOBUpdateResponse
            checkIfAdult(response)
        }, onError = {
            it.printStackTrace()
            warningMessage.value = it.localizedMessage
        })
    }

    fun checkIfAdult(userDOBUpdateResponse: UserDOBUpdateResponse) {
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

    fun generateDOBRequestparam(bdayDD: String, bdayMM: String, bdayYY: String) =
            HashMap<String, Any>().apply {
                put("bdayDD", bdayDD)
                put("bdayMM", bdayMM)
                put("bdayYY", bdayYY)
            }

    fun getRepo() = repository
}
