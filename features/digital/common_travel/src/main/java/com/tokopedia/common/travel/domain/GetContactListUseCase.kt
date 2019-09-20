package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.data.entity.TravelContactListModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import javax.inject.Inject

import rx.Observable
import rx.functions.Func1
import java.lang.Exception

/**
 * @author by jessica on 2019-08-26
 */

class GetContactListUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(query: String,
                        product: String,
                        filterType: String = "")
            : List<TravelContactListModel.Contact> {
        
        useCase.clearRequest()

        try {
            val params = mapOf(PARAM_GET_CONTACT_LIST_PRODUCT to product,
                    PARAM_GET_CONTACT_LIST_FILTER_TYPE to filterType)
            val graphqlRequest = GraphqlRequest(query, TravelContactListModel.Response::class.java, params)
            useCase.addRequest(graphqlRequest)
            val contactList = useCase.executeOnBackground().getSuccessData<TravelContactListModel.Response>().response

            return contactList.contacts
        } catch (throwable: Throwable) {
            return listOf()
        }
    }

    fun filterByKeyword(contacts: List<TravelContactListModel.Contact>, keyword: String): List<TravelContactListModel.Contact> {
        return contacts.filter { contact -> contact.fullName.contains(keyword) }
    }

    companion object {
        val PARAM_GET_CONTACT_LIST_PRODUCT = "product"
        val PARAM_GET_CONTACT_LIST_FILTER_TYPE = "filterType"

        val PARAM_PRODUCT_FLIGHT = "flight"
        val PARAM_PRODUCT_HOTEL = "hotel"

        val PARAM_PRODUCT_ADULT = "adult"
        val PARAM_PRODUCT_CHILD = "child"
        val PARAM_PRODUCT_INFANT = "infant"
    }

}