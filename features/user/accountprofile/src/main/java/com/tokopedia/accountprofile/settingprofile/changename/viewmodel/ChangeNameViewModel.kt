package com.tokopedia.accountprofile.settingprofile.changename.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.accountprofile.settingprofile.changename.domain.pojo.ChangeNameResult
import com.tokopedia.accountprofile.data.ProfileCompletionQueryConstant.PARAM_NAME
import com.tokopedia.accountprofile.domain.ChangeNameUseCase
import com.tokopedia.accountprofile.domain.UserProfileRuleUseCase
import com.tokopedia.accountprofile.profilecompletion.data.ProfileRoleData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * created by rival 23/10/19
 */
class ChangeNameViewModel @Inject constructor(
    private val changeNameUseCase: ChangeNameUseCase,
    private val userProfileRuleUseCase: UserProfileRuleUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mutableChangeNameResponse = MutableLiveData<Result<ChangeNameResult>>()
    val changeNameResponse: LiveData<Result<ChangeNameResult>>
        get() = mutableChangeNameResponse

    private val mutableUserProfileRole = MutableLiveData<Result<ProfileRoleData>>()
    val userProfileRole: LiveData<Result<ProfileRoleData>>
        get() = mutableUserProfileRole

    fun changePublicName(publicName: String) {
        launchCatchError(block = {
            val response = changeNameUseCase(publicName)

            val errorMessage = response.data.errors
            val isSuccess = response.data.isSuccess
            if (errorMessage.size == 0 && isSuccess == 1) {
                mutableChangeNameResponse.value =
                    Success(ChangeNameResult(response.data, publicName))
            } else if (errorMessage.isNotEmpty()) {
                mutableChangeNameResponse.value = Fail(MessageErrorException(errorMessage[0]))
            }
        }, onError = {
            mutableChangeNameResponse.value = Fail(it)
        })
    }

    fun getUserProfileRole() {
        launchCatchError(block = {
            val response = userProfileRuleUseCase(Unit)
            mutableUserProfileRole.value = Success(response.profileRoleData)
        }, onError = {
            mutableUserProfileRole.value = Fail(it)
        })
    }
}
