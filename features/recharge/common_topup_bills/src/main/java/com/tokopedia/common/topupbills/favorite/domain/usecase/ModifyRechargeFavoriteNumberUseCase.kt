package com.tokopedia.common.topupbills.favorite.domain.usecase

import com.tokopedia.common.topupbills.favorite.data.TopupBillsSeamlessFavNumberModData
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlMutation
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
        totalTransaction: Int,
        label: String,
        isDelete: Boolean
    ) {
        val paramSource = if (categoryId == CATEGORY_ID_PASCABAYAR)
            FAVORITE_NUMBER_PARAM_SOURCE_POSTPAID else FAVORITE_NUMBER_PARAM_SOURCE_PREPAID

        val requestData = mapOf(
            FAVORITE_NUMBER_PARAM_UPDATE_REQUEST to mapOf(
                FAVORITE_NUMBER_PARAM_CATEGORY_ID to categoryId,
                FAVORITE_NUMBER_PARAM_CLIENT_NUMBER to clientNumber,
                FAVORITE_NUMBER_PARAM_LAST_PRODUCT to productId,
                FAVORITE_NUMBER_PARAM_LABEL to label,
                FAVORITE_NUMBER_PARAM_TOTAL_TRANSACTION to totalTransaction,
                FAVORITE_NUMBER_PARAM_UPDATE_LAST_ORDER_DATE to false,
                FAVORITE_NUMBER_PARAM_SOURCE to paramSource,
                FAVORITE_NUMBER_PARAM_UPDATE_STATUS to true,
                FAVORITE_NUMBER_PARAM_WISHLIST to !isDelete
            )
        )

        params = RequestParams.create().apply {
            putAll(requestData)
        }
    }

    companion object {
        const val FAVORITE_NUMBER_PARAM_SOURCE = "source"
        const val FAVORITE_NUMBER_PARAM_UPDATE_REQUEST = "updateRequest"
        const val FAVORITE_NUMBER_PARAM_CATEGORY_ID = "categoryID"
        const val FAVORITE_NUMBER_PARAM_CLIENT_NUMBER = "clientNumber"
        const val FAVORITE_NUMBER_PARAM_LAST_PRODUCT = "lastProduct"
        const val FAVORITE_NUMBER_PARAM_LABEL = "label"
        const val FAVORITE_NUMBER_PARAM_TOTAL_TRANSACTION = "totalTransaction"
        const val FAVORITE_NUMBER_PARAM_UPDATE_LAST_ORDER_DATE = "updateLastOrderDate"
        const val FAVORITE_NUMBER_PARAM_UPDATE_STATUS = "updateStatus"
        const val FAVORITE_NUMBER_PARAM_WISHLIST = "wishlist"

        const val CATEGORY_ID_PASCABAYAR = 9

        const val FAVORITE_NUMBER_PARAM_SOURCE_POSTPAID = "pdp_favorite_list_telco_postpaid"
        const val FAVORITE_NUMBER_PARAM_SOURCE_PREPAID = "pdp_favorite_list_telco_prepaid"
    }
}