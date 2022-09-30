package com.tokopedia.manageaddress.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.manageaddress.domain.response.DeletePeopleAddressGqlResponse
import com.tokopedia.manageaddress.util.ManageAddressConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.manageaddress.util.ManageAddressConstant.STATUS_OK
import com.tokopedia.manageaddress.util.ManageAddressConstant.SUCCESS
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class DeletePeopleAddressUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<DeletePeopleAddressGqlResponse>) {

    fun execute(inputAddressId: Int, isTokonow: Boolean, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(
            mapOf(
                PARAM_KEY to inputAddressId,
                PARAM_IS_TOKONOW_REQUEST to isTokonow
            )
        )
        graphqlUseCase.setTypeClass(DeletePeopleAddressGqlResponse::class.java)
        graphqlUseCase.execute({ response: DeletePeopleAddressGqlResponse ->
            if(response.response.status.equals(STATUS_OK, true))  {
                if(response.response.data.success == 1) {
                    onSuccess(SUCCESS)
                } else {
                    onError(MessageErrorException(DEFAULT_ERROR_MESSAGE))
                }
            } else {
                onError(MessageErrorException(DEFAULT_ERROR_MESSAGE))
            }
        }, {
            throwable: Throwable -> onError(throwable)
        })

    }

    companion object{
        const val PARAM_KEY = "inputAddressId"
        const val PARAM_IS_TOKONOW_REQUEST = "isTokonowRequest"

        val QUERY = """
            mutation deleteAddress(${"$"}inputAddressId: Int!, ${"$"}isTokonowRequest: Boolean!) {
              kero_remove_address(addr_id: ${"$"}inputAddressId, is_tokonow_request: ${"$"}isTokonowRequest) {
                    data{
                        is_success
                    }
                    status
                    config
                    server_process_time
              }
            }
        """.trimIndent()
    }
}