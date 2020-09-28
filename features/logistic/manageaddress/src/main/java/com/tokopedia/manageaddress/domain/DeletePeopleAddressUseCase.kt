package com.tokopedia.manageaddress.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.manageaddress.domain.response.DeletePeopleAddressGqlResponse
import com.tokopedia.manageaddress.util.ManageAddressConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.manageaddress.util.ManageAddressConstant.STATUS_OK
import com.tokopedia.manageaddress.util.ManageAddressConstant.SUCCESS
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class DeletePeopleAddressUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<DeletePeopleAddressGqlResponse>) {

    fun execute(inputAddressId: Int, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to inputAddressId))
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

        val QUERY = """
            mutation deleteAddress(${"$"}inputAddressId: Int!) {
              kero_remove_address(addr_id: ${"$"}inputAddressId) {
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