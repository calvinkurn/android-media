package com.tokopedia.manageaddress.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.manageaddress.data.query.GetPeopleAddressQuery
import com.tokopedia.manageaddress.domain.response.GetPeopleAddressResponse
import javax.inject.Inject

class GetPeopleAddressUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase<GetPeopleAddressResponse>) {

    fun execute(query: String, onSuccess: (GetPeopleAddressResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(GetPeopleAddressQuery.keroAddressCorner)
        graphqlUseCase.setRequestParams(mapOf(
                "input" to mapOf(
                        "search_key" to query,
                        "show_address" to true
                )
        ))
        graphqlUseCase.setTypeClass(GetPeopleAddressResponse::class.java)
        return graphqlUseCase.execute(
                { peopleAddressResponse ->
                    onSuccess(peopleAddressResponse)
                }, { throwable ->
            onError(throwable)
        })
    }


}