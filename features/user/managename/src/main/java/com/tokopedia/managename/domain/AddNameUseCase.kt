package com.tokopedia.managename.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.managename.constant.ManageNameConstants
import com.tokopedia.managename.data.model.UpdateNameModel
import com.tokopedia.managename.data.model.UpdateNameResponse
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Yoris Prayogo on 04/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class AddNameUseCase @Inject constructor(
        @Named(ManageNameConstants.Query.UPDATE_NAME_QUERY)
        private val query: String,
        private val useCase: GraphqlUseCase<UpdateNameResponse>) {

    fun update(requestParams: Map<String, Any>,
            onSuccess: (UpdateNameModel) -> Unit,
            onError: (Throwable) -> Unit) {
        useCase.apply {
            setTypeClass(UpdateNameResponse::class.java)
            setRequestParams(requestParams)
            setGraphqlQuery(query)
            execute({ result ->
                if(result.data?.errors?.isEmpty() == true && result.data.isSuccess == 1) {
                    result.data.run {
                        onSuccess(this)
                    }
                }else onError(MessageErrorException(result.data?.errors?.get(0)))
            }, { error ->
                onError(error)
            })
        }
    }

    companion object {
        private const val PARAM_NAME = "fullname"
        private const val PARAM_TOKEN = "currValidateToken"

        fun params(
                fullname: String,
                currValidateToken: String,
        ): HashMap<String, Any> {
            return hashMapOf(
                PARAM_NAME to fullname,
                PARAM_TOKEN to currValidateToken
            )
        }

    }

}
