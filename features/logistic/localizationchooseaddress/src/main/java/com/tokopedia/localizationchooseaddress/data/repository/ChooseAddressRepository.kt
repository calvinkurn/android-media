package com.tokopedia.localizationchooseaddress.data.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.localizationchooseaddress.data.query.ChooseAddressQuery
import com.tokopedia.localizationchooseaddress.domain.response.GetChosenAddressListQglResponse
import com.tokopedia.localizationchooseaddress.util.getResponse
import javax.inject.Inject

class ChooseAddressRepository @Inject constructor(private val gql: GraphqlRepository){

    suspend fun getChosenAddressList(): GetChosenAddressListQglResponse {
        val request = GraphqlRequest(ChooseAddressQuery.getChosenAddressList,
                GetChosenAddressListQglResponse::class.java)
        return gql.getResponse(request)
    }
}