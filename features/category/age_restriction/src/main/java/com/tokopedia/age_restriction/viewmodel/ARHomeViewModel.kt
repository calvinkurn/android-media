package com.tokopedia.age_restriction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.age_restriction.usecase.FetchUserDobUseCase
import com.tokopedia.age_restriction.data.UserDOBResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ARHomeViewModel @Inject constructor(private val fetchUserDobUseCase: FetchUserDobUseCase,
                                          private val userSession: UserSessionInterface) : BaseARViewModel(), CoroutineScope {

    private val askUserLogin = MutableLiveData<Int>()
    private val USER_DOB_PATH = "https://accounts.tokopedia.com/userapp/api/v1/profile/get-dob"
    val notAdult = MutableLiveData<Int>()
    val notVerified = MutableLiveData<String>()
    val notFilled = MutableLiveData<Int>()
    val userAdult = MutableLiveData<Int>()

    override fun doOnCreate() {
        super.doOnCreate()
        if (!userSession.isLoggedIn)
            askUserLogin.value = 1
        else {
            fetchUserDOB()
        }
    }

    fun fetchUserDOB() {
        progBarVisibility.value = true
        launchCatchError(
                block = {
                    val response = fetchUserDobUseCase.getData(USER_DOB_PATH)
                    processUserDOB(response.data)
                },
                onError = {
                    warningMessage.value = it.localizedMessage
                }

        )
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getAskUserLogin(): LiveData<Int> {
        return askUserLogin
    }

    private fun processUserDOB(userDOBResponse: UserDOBResponse?) {
        userDOBResponse?.let {
            if (it.isDobVerified) {
                if (it.isAdult || it.age >= 18)
                    userAdult.value = 1
                else
                    notAdult.value = 1
            } else {
                if (it.isDobExist)
                    notVerified.value = it.bday
                else
                    notFilled.value = 1
            }
        }
        progBarVisibility.value = false
    }
}