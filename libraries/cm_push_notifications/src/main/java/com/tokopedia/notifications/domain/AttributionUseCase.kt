package com.tokopedia.notifications.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifications.data.model.AttributionNotifier

typealias UseCase = GraphqlUseCase<AttributionNotifier>

class AttributionUseCase(
        private val useCase: UseCase,
        private val query: String
) {

    fun execute(setParams: Map<String, Any>) {
        useCase.apply {
            setTypeClass(AttributionNotifier::class.java)
            setRequestParams(setParams)
            setGraphqlQuery(query)
            execute({}, {})
        }
    }

    fun params(
            transactionId: String?,
            userTransId: String?,
            recipientId: String?,
            shopId: String?,
            blastId: String?
    ): Map<String, Any> {
        return hashMapOf<String, Any>().apply {
            put(PARAM_TRANSACTION_ID, transactionId?: "")
            put(PARAM_USER_TRANSACTION_ID, userTransId?: "")
            put(PARAM_RECIPIENT_ID, recipientId?: "")
            put(PARAM_STATUS, VALUE_STATUS)
            put(PARAM_SHOP_ID, shopId?: "")
            put(PARAM_BLAST_ID, blastId?: "")
        }
    }

    companion object {
        //query parameters
        private const val PARAM_TRANSACTION_ID = "trans_id"
        private const val PARAM_USER_TRANSACTION_ID = "user_trans_id"
        private const val PARAM_RECIPIENT_ID = "recipient_id"
        private const val PARAM_STATUS = "status"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_BLAST_ID = "blast_id"

        //static value of param
        private const val VALUE_STATUS = "clicked"
    }

}