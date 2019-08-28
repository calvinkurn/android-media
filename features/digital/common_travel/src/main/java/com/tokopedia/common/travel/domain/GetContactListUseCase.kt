package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.data.entity.TravelContactListModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-26
 */

class GetContactListUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(query: String,
                        product: String,
                        filterType: String,
                        keyword: String = "") : List<TravelContactListModel.Contact> {

        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).apply {
            setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
        }.build())
        useCase.clearRequest()

        try {
            val params = mapOf(PARAM_GET_CONTACT_LIST_PRODUCT to product,
                    PARAM_GET_CONTACT_LIST_FILTER_TYPE to filterType)
            val graphqlRequest = GraphqlRequest(query, TravelContactListModel.Response::class.java, params)
            useCase.addRequest(graphqlRequest)
            val contactList = useCase.executeOnBackground().getSuccessData<TravelContactListModel.Response>().response

            return filterByKeyword(contactList.contacts, keyword)
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
    }

}