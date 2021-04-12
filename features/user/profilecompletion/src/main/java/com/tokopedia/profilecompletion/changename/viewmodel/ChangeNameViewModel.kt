package com.tokopedia.profilecompletion.changename.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNamePojo
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNameResult
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_NAME
import com.tokopedia.profilecompletion.settingprofile.data.ProfileRoleData
import com.tokopedia.profilecompletion.settingprofile.data.UserProfileRoleData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * created by rival 23/10/19
 */
class ChangeNameViewModel @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<ChangeNamePojo>,
        private val userProfileRoleUseCase: GraphqlUseCase<UserProfileRoleData>,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val mutableChangeNameResponse = MutableLiveData<Result<ChangeNameResult>>()
    val changeNameResponse: LiveData<Result<ChangeNameResult>>
        get() = mutableChangeNameResponse

    private val mutableUserProfileRole = MutableLiveData<Result<ProfileRoleData>>()
    val userProfileRole: LiveData<Result<ProfileRoleData>>
        get() = mutableUserProfileRole

    fun changePublicName(publicName: String) {
        rawQueries[ProfileCompletionQueryConstant.MUTATION_CHANGE_NAME]?.let { query ->
            val params = mapOf(PARAM_NAME to publicName)

            graphqlUseCase.setTypeClass(ChangeNamePojo::class.java)
            graphqlUseCase.setRequestParams(params)
            graphqlUseCase.setGraphqlQuery(query)

            graphqlUseCase.execute(
                    onSuccessMutateChangeName(publicName),
                    onErrorMutateChangeName()
            )
        }
    }

    private fun onErrorMutateChangeName(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableChangeNameResponse.postValue(Fail(it))
        }
    }

    private fun onSuccessMutateChangeName(fullname: String): (ChangeNamePojo) -> Unit {
        return {
            try {
                val errorMessage = it.data.errors
                val isSuccess = it.data.isSuccess
                if (errorMessage.size == 0 && isSuccess == 1) {
                    mutableChangeNameResponse.postValue(Success(ChangeNameResult(it.data, fullname)))
                } else if (!errorMessage[0].isBlank()) {
                    mutableChangeNameResponse.postValue(Fail(MessageErrorException(errorMessage[0])))
                }
            } catch (e: Exception) {
                mutableChangeNameResponse.postValue(Fail(MessageErrorException(e.message)))
            }
        }
    }

    fun getUserProfileRole(){
        rawQueries[ProfileCompletionQueryConstant.QUERY_PROFILE_ROLE]?.let { query ->
            userProfileRoleUseCase.run {
                setGraphqlQuery(query)
                execute( onSuccess = {
                    mutableUserProfileRole.postValue(Success(it.profileRoleData))
                }, onError = {
                    mutableUserProfileRole.postValue(Fail(it))
                })
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        graphqlUseCase.cancelJobs()
        userProfileRoleUseCase.cancelJobs()
    }
}