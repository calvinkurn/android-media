package com.tokopedia.age_restriction.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.age_restriction.usecase.UpdateUserDobUseCase
import com.tokopedia.age_restriction.data.UserDOBUpdateResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class VerifyDOBViewModel @Inject constructor(private val updateUserDobUseCase: UpdateUserDobUseCase) : BaseARViewModel(), CoroutineScope {

    val userIsAdult = MutableLiveData<Boolean>()
    val userNotAdult = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun updateUserDoB(bdayDD: String, bdayMM: String, bdayYY: String) {
        progBarVisibility.value = true
        launchCatchError(block = {
            val response = updateUserDobUseCase.getData(bdayDD,bdayMM,bdayYY)
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
