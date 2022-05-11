package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.requests.DigiPersoRequestParam
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.digital_product_detail.data.model.data.perso.PersoFavNumberChipsData
import com.tokopedia.digital_product_detail.data.model.data.perso.PersoFavNumberListData
import com.tokopedia.digital_product_detail.data.model.data.perso.PersoFavNumberPrefillData
import com.tokopedia.digital_product_detail.data.model.data.perso.PersoFavNumberGroup
import com.tokopedia.digital_product_detail.domain.util.FavoriteNumberType
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetRechargeFavoriteNumberUseCase @Inject constructor(
    private val useCase: MultiRequestGraphqlUseCase
): UseCase<PersoFavNumberGroup>() {
    var persoFavNumberGroup = PersoFavNumberGroup()

    override suspend fun executeOnBackground(): PersoFavNumberGroup {
        persoFavNumberGroup = PersoFavNumberGroup()

        val gqlResponse: GraphqlResponse
        try {
            gqlResponse = useCase.executeOnBackground()
        } catch (error: Throwable) {
            return persoFavNumberGroup
        }

        FavoriteNumberType.values().forEach {
            try {
                when (it) {
                    FavoriteNumberType.LIST -> {
                        val favoriteNumberListData =
                            gqlResponse.getSuccessData<PersoFavNumberListData>()
                        persoFavNumberGroup.favoriteNumberList = favoriteNumberListData
                    }
                    FavoriteNumberType.CHIP -> {
                        val favoriteNumberChipsData =
                            gqlResponse.getSuccessData<PersoFavNumberChipsData>()
                        persoFavNumberGroup.favoriteNumberChips = favoriteNumberChipsData
                    }
                    FavoriteNumberType.PREFILL -> {
                        val favoriteNumberPrefillData =
                            gqlResponse.getSuccessData<PersoFavNumberPrefillData>()
                        persoFavNumberGroup.favoriteNumberPrefill = favoriteNumberPrefillData
                    }
                }
            } catch (error: Throwable) {
                // no data
            }
        }

        return persoFavNumberGroup
    }

    fun addFavoriteNumberByChannel(
        favoriteNumberType: FavoriteNumberType,
        categoryIds: List<Int>,
        operatorIds: List<Int>
    ) {
        val (channel, responseClassType) = when (favoriteNumberType) {
            FavoriteNumberType.LIST -> CHANNEL_FAVORITE_NUMBER_LIST to PersoFavNumberListData::class.java
            FavoriteNumberType.CHIP -> CHANNEL_FAVORITE_NUMBER_CHIPS to PersoFavNumberChipsData::class.java
            FavoriteNumberType.PREFILL -> CHANNEL_FAVORITE_NUMBER_PREFILL to PersoFavNumberPrefillData::class.java
        }
        val requestParams = createRequestParams(
            categoryIds, operatorIds, channel)
        val gqlRequest = GraphqlRequest(
            CommonTopupBillsGqlQuery.rechargePersoFavoriteNumber,
            responseClassType,
            requestParams.parameters
        )

        useCase.addRequest(gqlRequest)
    }

    fun clearRequests() {
        useCase.clearRequest()
    }

    private fun createRequestParams(
        categoryIds: List<Int>,
        operatorIds: List<Int> = listOf(),
        channelName: String
    ): RequestParams {
        return RequestParams.create().apply {
            putObject(
                FAVORITE_NUMBER_PARAM_INPUT, DigiPersoRequestParam(
                    channelName = channelName,
                    clientNumbers = listOf(),
                    dgCategoryIDs = categoryIds,
                    pgCategoryIDs = listOf(),
                    dgOperatorIds = operatorIds
                )
            )
        }
    }

    companion object {
        const val FAVORITE_NUMBER_PARAM_INPUT = "input"

        const val CHANNEL_FAVORITE_NUMBER_LIST = "favorite_number_list"
        const val CHANNEL_FAVORITE_NUMBER_CHIPS = "favorite_number_chips"
        const val CHANNEL_FAVORITE_NUMBER_PREFILL = "favorite_number_prefill"
    }

}