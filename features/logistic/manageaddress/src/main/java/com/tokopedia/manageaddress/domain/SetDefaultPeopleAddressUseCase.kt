package com.tokopedia.manageaddress.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.manageaddress.domain.response.SetDefaultPeopleAddressGqlResponse
import com.tokopedia.manageaddress.util.ManageAddressConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.manageaddress.util.ManageAddressConstant.STATUS_OK
import com.tokopedia.manageaddress.util.ManageAddressConstant.SUCCESS
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class SetDefaultPeopleAddressUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<SetDefaultPeopleAddressGqlResponse>) {

    fun execute(inputAddressId: Int, setAsStateChosenAddress: Boolean, isTokonow: Boolean, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(
            mapOf(
                PARAM_KEY to inputAddressId,
                PARAM_SET_AS_CHOSEN to setAsStateChosenAddress,
                PARAM_IS_TOKONOW_REQUEST to isTokonow
            )
        )
        graphqlUseCase.setTypeClass(SetDefaultPeopleAddressGqlResponse::class.java)
        graphqlUseCase.execute({ response: SetDefaultPeopleAddressGqlResponse ->
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
        const val PARAM_SET_AS_CHOSEN = "setAsStateChosenAddress"
        const val PARAM_IS_TOKONOW_REQUEST = "isTokonowRequest"

        val QUERY = """
            mutation defaultAddress(${"$"}inputAddressId : Int!, ${"$"}setAsStateChosenAddress: Boolean, ${"$"}isTokonowRequest: Boolean!) {
              kero_set_default_address(addr_id: ${"$"}inputAddressId, set_as_state_chosen_address: ${"$"}setAsStateChosenAddress, is_tokonow_request: ${"$"}isTokonowRequest) {
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

