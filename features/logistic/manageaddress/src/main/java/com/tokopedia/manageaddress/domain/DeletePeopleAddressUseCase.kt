package com.tokopedia.manageaddress.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.manageaddress.domain.response.DeletePeopleAddressResponse
import javax.inject.Inject

class DeletePeopleAddressUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<DeletePeopleAddressResponse>) {

    fun execute(addressId: Int, onSuccess: (DeletePeopleAddressResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to addressId))
        graphqlUseCase.setTypeClass(DeletePeopleAddressResponse::class.java)
        graphqlUseCase.execute({ response: DeletePeopleAddressResponse ->
            onSuccess(response)
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