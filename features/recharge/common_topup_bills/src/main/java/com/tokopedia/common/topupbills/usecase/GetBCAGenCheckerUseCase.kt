package com.tokopedia.common.topupbills.usecase

import com.tokopedia.common.topupbills.data.requests.DigiPersoRequestParam
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.usecase.query.BCAGenCheckerQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetBCAGenCheckerUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TopupBillsPersoFavNumberData>(graphqlRepository) {

    init {
        setGraphqlQuery(BCAGenCheckerQuery())
        setTypeClass(TopupBillsPersoFavNumberData::class.java)
    }

    suspend fun execute(clientNumbers: List<String>): TopupBillsPersoFavNumberData {
        setRequestParams(createRequestParam(clientNumbers))
        return executeOnBackground()
    }

    fun createRequestParam(
        clientNumbers: List<String>
    ) = HashMap<String, Any>().apply {
        val params = DigiPersoRequestParam(
            channelName = CHANNEL_NAME_BCA_GEN,
            clientNumbers = clientNumbers,
            dgCategoryIDs = listOf(),
            pgCategoryIDs = listOf(),
            dgOperatorIds = listOf()
        )
        put(FAVORITE_NUMBER_PARAM_INPUT, params)
    }

    companion object {
        const val FAVORITE_NUMBER_PARAM_INPUT = "input"
        private const val CHANNEL_NAME_BCA_GEN = "pdp_bca_flash"
    }

}
