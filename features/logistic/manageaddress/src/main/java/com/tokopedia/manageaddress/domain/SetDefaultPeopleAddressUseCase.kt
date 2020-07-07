package com.tokopedia.manageaddress.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.manageaddress.domain.response.SetDefaultPeopleAddressGqlResponse
import com.tokopedia.manageaddress.util.ManageAddressConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.manageaddress.util.ManageAddressConstant.STATUS_OK
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class SetDefaultPeopleAddressUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<SetDefaultPeopleAddressGqlResponse>) {

    fun execute(inputAddressId: Int, onSuccess: (SetDefaultPeopleAddressGqlResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to inputAddressId))
        graphqlUseCase.setTypeClass(SetDefaultPeopleAddressGqlResponse::class.java)
        graphqlUseCase.execute({ response: SetDefaultPeopleAddressGqlResponse ->
            if(response.response.status.equals(STATUS_OK, true))  {
                if(response.response.data.success == 1) {
                    onSuccess(response)
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
            mutation defaultAddress(${"$"}inputAddressId : Int!) {
              kero_set_default_address(addr_id: ${"$"}inputAddressId) {
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

