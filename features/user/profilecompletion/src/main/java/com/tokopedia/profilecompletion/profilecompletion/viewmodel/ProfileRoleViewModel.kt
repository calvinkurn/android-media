package com.tokopedia.profilecompletion.profilecompletion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.profilecompletion.profilecompletion.data.ProfileRoleData
import com.tokopedia.profilecompletion.profilecompletion.data.UserProfileRoleData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
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

    private val mutableUserProfileRole = MutableLiveData<Result<ProfileRoleData>>()
    val userProfileRole: LiveData<Result<ProfileRoleData>>
	get() = mutableUserProfileRole

    fun getUserProfileRole() {
	rawQueries[ProfileCompletionQueryConstant.QUERY_PROFILE_ROLE]?.let { query ->
	    userProfileRoleUseCase.run {
		setGraphqlQuery(query)
		execute(
		    {
			mutableUserProfileRole.postValue(Success(it.profileRoleData))
		    },
		    {
			mutableUserProfileRole.postValue(Fail(it))
		    }
		)
	    }
	}
    }
}