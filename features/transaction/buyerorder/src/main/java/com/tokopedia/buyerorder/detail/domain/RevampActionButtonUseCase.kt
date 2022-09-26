package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.ActionButtonList
import com.tokopedia.buyerorder.detail.domain.queries.QuerySetActionButton
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

/**
 * created by @bayazidnasir on 19/8/2022
 */

class RevampActionButtonUseCase @Inject constructor(
    private val repository: GraphqlRepository
): GraphqlUseCase<ActionButtonList>(repository) {

    private var params: Map<String, List<ActionButton>> = mapOf()

    override suspend fun executeOnBackground(): ActionButtonList {
        val request = GraphqlRequest(QuerySetActionButton(), ActionButtonList::class.java, params)
        val gqlResponse = repository.response(listOf(request))
        val error = gqlResponse.getError(ActionButtonList::class.java)
        if (error == null || error.isEmpty()){
            return gqlResponse.getData(ActionButtonList::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun setParams(actionButtons: List<ActionButton>){
        params = mapOf(PARAM to actionButtons)
    }

    companion object{
        private const val PARAM = "param"
    }
}