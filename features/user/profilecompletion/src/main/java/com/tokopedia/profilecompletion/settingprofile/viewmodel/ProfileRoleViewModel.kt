package com.tokopedia.profilecompletion.settingprofile.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.profilecompletion.settingprofile.data.ProfileRoleData
import com.tokopedia.profilecompletion.settingprofile.data.UserProfileRoleData
import kotlinx.coroutines.CoroutineDispatcher
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-08-01.
 * ade.hadian@tokopedia.com
 */

class ProfileRoleViewModel @Inject constructor(
        private val userProfileRoleUseCase: GraphqlUseCase<UserProfileRoleData>,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val userProfileRole = MutableLiveData<Result<ProfileRoleData>>()

    fun getUserProfileRole(){
        val rawQuery = rawQueries[ProfileCompletionQueriesConstant.QUERY_PROFILE_ROLE]
        if(!rawQuery.isNullOrEmpty()){
            userProfileRoleUseCase.run {
                setGraphqlQuery(rawQuery)
                execute(
                        {
                            userProfileRole.value = Success(it.profileRoleData)
                        },
                        {
                            userProfileRole.value = Fail(it)
                        }
                )
            }
        }
    }
}