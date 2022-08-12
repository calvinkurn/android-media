package com.tokopedia.travel.passenger.domain

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import javax.inject.Inject

class GetContactListUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(query: GqlQueryInterface,
                        product: String,
                        filterType: String = "")
            : List<TravelContactListModel.Contact> {

        useCase.clearRequest()

        return try {
            val params = mapOf(PARAM_GET_CONTACT_LIST_PRODUCT to product,
                    PARAM_GET_CONTACT_LIST_FILTER_TYPE to filterType)
            val graphqlRequest = GraphqlRequest(query, TravelContactListModel.Response::class.java, params)
            useCase.addRequest(graphqlRequest)
            val contactList = useCase.executeOnBackground().getSuccessData<TravelContactListModel.Response>().response

            contactList.contacts
        } catch (throwable: Throwable) {
            listOf()
        }
    }

    fun filterByKeyword(contacts: List<TravelContactListModel.Contact>, keyword: String): List<TravelContactListModel.Contact> {
        return contacts.filter { contact -> contact.fullName.contains(keyword) }
    }

    companion object {
        const val PARAM_GET_CONTACT_LIST_PRODUCT = "product"
        const val PARAM_GET_CONTACT_LIST_FILTER_TYPE = "filterType"

        val PARAM_PRODUCT_FLIGHT = "flight"
        val PARAM_PRODUCT_HOTEL = "hotel"

        val PARAM_PRODUCT_ADULT = "adult"
        val PARAM_PRODUCT_CHILD = "child"
        val PARAM_PRODUCT_INFANT = "infant"
    }

}