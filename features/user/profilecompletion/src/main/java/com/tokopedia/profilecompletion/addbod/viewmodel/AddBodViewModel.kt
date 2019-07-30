package com.tokopedia.profilecompletion.addbod.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addbod.data.AddBodData
import com.tokopedia.profilecompletion.addbod.data.UserProfileCompletionUpdateBodData
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderPojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 */

class AddBodViewModel @Inject constructor(
        private val bodGraphqlUseCase: GraphqlUseCase<UserProfileCompletionUpdateBodData>,
        private val rawQueries: Map<String, String>,
        private val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {


    val editBodUserProfileResponse = MutableLiveData<Result<AddBodData>>()

    fun editBodUserProfile(selectedDate: String) {


        rawQueries[ProfileCompletionQueriesConstant.MUTATION_ADD_BOD]?.let { query ->
            bodGraphqlUseCase.run {
                setGraphqlQuery(query)

                setTypeClass(UserProfileCompletionUpdateBodData::class.java)

                val params = mapOf(ProfileCompletionQueriesConstant.PARAM_BOD to selectedDate)
                setRequestParams(params)

                execute(onSuccessEditBOD(),
                        onErrorEditBOD()
                )
            }

        }
    }

    private fun onErrorEditBOD(): (Throwable) -> Unit {
        return {
            editBodUserProfileResponse.value = Fail(it)
        }
    }

    private fun onSuccessEditBOD(): (UserProfileCompletionUpdateBodData) -> Unit {
        return {
            if (it.addBodData.isSuccess) {
                editBodUserProfileResponse.value = Success(it.addBodData)
            } else if (it.addBodData.birthDateMessage.isNotBlank()) {
                editBodUserProfileResponse.value = Fail(MessageErrorException(it.addBodData.birthDateMessage))
            } else {
                editBodUserProfileResponse.value = Fail(RuntimeException())
            }
        }
    }
}