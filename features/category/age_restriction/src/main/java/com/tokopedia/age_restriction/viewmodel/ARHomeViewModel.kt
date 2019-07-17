package com.tokopedia.age_restriction.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.age_restriction.data.UserDOBResponse
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class ARHomeViewModel : BaseViewModel(), CoroutineScope {

    private var userDetailLiveData: UserDOBResponse? = null
    private val askUserLogin = MutableLiveData<Int>()
    private val USER_DOB_PATH = "https://accounts.tokopedia.com/userapp/api/v1/profile/get-dob"
    val notAdult = MutableLiveData<Int>()
    val notVerified = MutableLiveData<String>()
    val notFilled = MutableLiveData<Int>()
    val userAdult = MutableLiveData<Int>()

    override fun doOnCreate() {
        super.doOnCreate()
        repository?.let {
            if (!it.getUserLoginState()?.isLoggedIn)
                askUserLogin.value = 1
            else {
                fetchUserDOB()
            }
        }
    }

    override fun doOnPause() {

    }

    override fun doOnStop() {

    }

    override fun doOnDestroy() {

    }

    fun fetchUserDOB() {
        progBarVisibility.value = true
        launchCatchError(
                block = {
                    val response = repository.getRestData(USER_DOB_PATH,
                            object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                            RequestType.GET,
                            RequestParams.EMPTY.parameters)
                    val userDataResponse = response?.getData() as DataResponse<UserDOBResponse>
                    userDetailLiveData = userDataResponse.data
                    processUserDOB(userDetailLiveData)
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