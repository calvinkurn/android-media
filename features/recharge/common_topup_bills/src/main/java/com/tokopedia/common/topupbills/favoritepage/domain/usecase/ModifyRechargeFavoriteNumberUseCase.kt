package com.tokopedia.common.topupbills.favoritepage.domain.usecase

import com.tokopedia.common.topupbills.favoritepage.data.TopupBillsSeamlessFavNumberModData
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlMutation
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ModifyRechargeFavoriteNumberUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<TopupBillsSeamlessFavNumberModData>(graphqlRepository) {

    private var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): TopupBillsSeamlessFavNumberModData {
        val gqlRequest = GraphqlRequest(
            CommonTopupBillsGqlMutation.updateSeamlessFavoriteNumber,
            TopupBillsSeamlessFavNumberModData::class.java,
            params.parameters
        )
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest))

        val error = gqlResponse.getError(TopupBillsSeamlessFavNumberModData::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(TopupBillsSeamlessFavNumberModData::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun setRequestParams(
        categoryId: Int,
        productId: Int,
        clientNumber: String,
        hashedClientNumber: String,
        totalTransaction: Int,
        label: String,
        isDelete: Boolean,
        source: String,
    ) {
        val requestData = mapOf(
            FAVORITE_NUMBER_PARAM_UPDATE_REQUEST to mapOf(
                FAVORITE_NUMBER_PARAM_CATEGORY_ID to categoryId,
                FAVORITE_NUMBER_PARAM_CLIENT_NUMBER to clientNumber,
                FAVORITE_NUMBER_PARAM_HASHED_CLIENT_NUMBER to hashedClientNumber,
                FAVORITE_NUMBER_PARAM_LAST_PRODUCT to productId,
                FAVORITE_NUMBER_PARAM_LABEL to label,
                FAVORITE_NUMBER_PARAM_TOTAL_TRANSACTION to totalTransaction,
                FAVORITE_NUMBER_PARAM_UPDATE_LAST_ORDER_DATE to false,
                FAVORITE_NUMBER_PARAM_SOURCE to source,
                FAVORITE_NUMBER_PARAM_UPDATE_STATUS to true,
                FAVORITE_NUMBER_PARAM_WISHLIST to !isDelete
            )
        )

        params = RequestParams.create().apply {
            putAll(requestData)
        }
    }

    fun createSourceParam(categoryIds: List<Int>): String {
        val joinedCategoryIds = categoryIds.joinToString(separator = ",")
        return "${FAVORITE_NUMBER_PARAM_SOURCE_PERSO}$joinedCategoryIds"
    }

    companion object {
        const val FAVORITE_NUMBER_PARAM_SOURCE = "source"
        const val FAVORITE_NUMBER_PARAM_UPDATE_REQUEST = "updateRequest"
        const val FAVORITE_NUMBER_PARAM_CATEGORY_ID = "categoryID"
        const val FAVORITE_NUMBER_PARAM_CLIENT_NUMBER = "clientNumber"
        const val FAVORITE_NUMBER_PARAM_HASHED_CLIENT_NUMBER = "hashedClientNumber"
        const val FAVORITE_NUMBER_PARAM_LAST_PRODUCT = "lastProduct"
        const val FAVORITE_NUMBER_PARAM_LABEL = "label"
        const val FAVORITE_NUMBER_PARAM_TOTAL_TRANSACTION = "totalTransaction"
        const val FAVORITE_NUMBER_PARAM_UPDATE_LAST_ORDER_DATE = "updateLastOrderDate"
        const val FAVORITE_NUMBER_PARAM_UPDATE_STATUS = "updateStatus"
        const val FAVORITE_NUMBER_PARAM_WISHLIST = "wishlist"

        const val FAVORITE_NUMBER_PARAM_SOURCE_PERSO = "digital-personalization"
    }
}
