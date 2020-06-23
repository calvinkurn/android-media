package com.tokopedia.manageaddress.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.manageaddress.domain.response.DeletePeopleAddressGqlResponse
import com.tokopedia.manageaddress.util.STATUS_OK
import javax.inject.Inject

class DeletePeopleAddressUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<DeletePeopleAddressGqlResponse>) {

    fun execute(addressId: String, onSuccess: (DeletePeopleAddressGqlResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to addressId))
        graphqlUseCase.setTypeClass(DeletePeopleAddressGqlResponse::class.java)
        graphqlUseCase.execute({ response: DeletePeopleAddressGqlResponse ->
            if(response.response.status.equals(STATUS_OK, true) && response.response.data.success == 1) {
                onSuccess(response)
            }

            //ToDo:: else kalo salah
        }, {
            throwable: Throwable -> onError(throwable)
        })

    }

    companion object{
        const val PARAM_KEY = "inputAddressId"
        val QUERY = """
            mutation deleteAddress(${"$"}inputAddressId: Int) {
              kero_remove_address(addr_id:${"$"}inputAddressId) {
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