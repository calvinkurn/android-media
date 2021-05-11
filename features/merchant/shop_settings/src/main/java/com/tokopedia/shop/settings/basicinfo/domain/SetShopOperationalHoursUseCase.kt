package com.tokopedia.shop.settings.basicinfo.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.settings.basicinfo.data.SetShopOperationalHoursRequestParams
import com.tokopedia.shop.settings.basicinfo.data.SetShopOperationalHoursResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by Rafli Syam on 07/05/2021
 */
class SetShopOperationalHoursUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
): GraphqlUseCase<SetShopOperationalHoursResponse>(gqlRepository) {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): SetShopOperationalHoursResponse {
        val request = GraphqlRequest(MUTATION, SetShopOperationalHoursResponse::class.java, params.parameters)
        return gqlRepository.getReseponse(listOf(request)).getData<SetShopOperationalHoursResponse>(
                SetShopOperationalHoursResponse::class.java
        )
    }

    companion object {

        fun createRequestParams(shopId: String, newOpsHourList: List<ShopOperationalHour>): RequestParams = RequestParams.create().apply {
            putObject(KEY_INPUT, SetShopOperationalHoursRequestParams(
                    shopId = shopId,
                    type = DEFAULT_TYPE,
                    newOpsHourList = newOpsHourList
            ))
        }

        private const val KEY_INPUT = "input"
        private const val DEFAULT_TYPE = 1

        private const val MUTATION = "mutation setShopOperationalHours(\$input: ParamSetShopOperationalHours!) {\n" +
                "  setShopOperationalHours(input: \$input) {\n" +
                "    success\n" +
                "    message\n" +
                "  }\n" +
                "}"

    }

}