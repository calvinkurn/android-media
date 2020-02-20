package com.tokopedia.age_restriction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.age_restriction.data.UserDOBResponse
import com.tokopedia.age_restriction.usecase.FetchUserDobUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

private const val minimumAdultAge = 21

class ARHomeViewModel @Inject constructor(private val fetchUserDobUseCase: FetchUserDobUseCase,
                                          private val userSession: UserSessionInterface) : BaseARViewModel(){

    private val askUserLogin = MutableLiveData<Int>()
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
        val userDobQuery = TokopediaUrl.Companion.getInstance().ACCOUNTS.plus("userapp/api/v1/profile/get-dob")
        progBarVisibility.value = true
        launchCatchError(
                block = {
                    val response = fetchUserDobUseCase.getData(userDobQuery)
                    processUserDOB(response)
                },
                onError = {
                    warningMessage.value = it.localizedMessage
                }

        )
    }

    fun getAskUserLogin(): LiveData<Int> {
        return askUserLogin
    }

    private fun processUserDOB(userDOBResponse: UserDOBResponse?) {
        userDOBResponse?.let {
            if (it.isDobVerified) {
                if (it.isAdult || it.age >= minimumAdultAge)
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