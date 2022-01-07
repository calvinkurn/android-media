package com.tokopedia.profilecompletion.addbod.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addbod.data.AddBodData
import com.tokopedia.profilecompletion.addbod.data.UserProfileCompletionUpdateBodData
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 */

class AddBodViewModel @Inject constructor(
        private val bodGraphqlUseCase: GraphqlUseCase<UserProfileCompletionUpdateBodData>,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {


    val editBodUserProfileResponse = MutableLiveData<Result<AddBodData>>()

    fun editBodUserProfile(context: Context, selectedDate: String) {

        GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_bod)?.let { query ->
            bodGraphqlUseCase.run {
                setGraphqlQuery(query)

                setTypeClass(UserProfileCompletionUpdateBodData::class.java)

                val params = mapOf(ProfileCompletionQueryConstant.PARAM_BOD to selectedDate)
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