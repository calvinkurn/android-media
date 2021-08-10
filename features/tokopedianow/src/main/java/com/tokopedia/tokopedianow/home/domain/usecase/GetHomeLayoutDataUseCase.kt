package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.query.GetHomeLayoutData
import com.tokopedia.usecase.RequestParams
import java.lang.StringBuilder
import javax.inject.Inject

/**
 * Dynamic Home Channel Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/381550603/HPB+Home+-+Mojito+Channel
 */

class GetHomeLayoutDataUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetHomeLayoutResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_CHANNEL_ID = "channelId"
        private const val LOCATION = "location"
    }

    init {
        setGraphqlQuery(GetHomeLayoutData.QUERY)
        setTypeClass(GetHomeLayoutResponse::class.java)
    }

    suspend fun execute(channelId: String?, localCacheModel: LocalCacheModel?): HomeLayoutResponse {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_CHANNEL_ID, channelId)
            putString(LOCATION, mapLocation(localCacheModel))
        }.parameters)

        val response = executeOnBackground().response
        val data = response.data.firstOrNull()
        return data ?: throw MessageErrorException()
    }

    private fun mapLocation(localCacheModel: LocalCacheModel?): String {
        if (localCacheModel == null) return ""

        val stringBuilder = StringBuilder()
        if (localCacheModel.lat.isNotBlank()) {
            stringBuilder.append("user_lat=${localCacheModel.lat}")
        }
        if (localCacheModel.long.isNotBlank()) {
            if (stringBuilder.isNotBlank()) {
                stringBuilder.append("&")
            }
            stringBuilder.append("user_long=${localCacheModel.long}")
        }
        if (localCacheModel.city_id.isNotBlank()) {
            if (stringBuilder.isNotBlank()) {
                stringBuilder.append("&")
            }
            stringBuilder.append("user_cityId=${localCacheModel.city_id}")
        }
        if (localCacheModel.district_id.isNotBlank()) {
            if (stringBuilder.isNotBlank()) {
                stringBuilder.append("&")
            }
            stringBuilder.append("user_districtId=${localCacheModel.district_id}")
        }
        if (localCacheModel.postal_code.isNotBlank()) {
            if (stringBuilder.isNotBlank()) {
                stringBuilder.append("&")
            }
            stringBuilder.append("user_postCode=${localCacheModel.postal_code}")
        }
        if (localCacheModel.address_id.isNotBlank()) {
            if (stringBuilder.isNotBlank()) {
                stringBuilder.append("&")
            }
            stringBuilder.append("user_addressId=${localCacheModel.address_id}")
        }
        if (localCacheModel.warehouse_id.isNotBlank()) {
            if (stringBuilder.isNotBlank()) {
                stringBuilder.append("&")
            }
            stringBuilder.append("warehouse_ids=${localCacheModel.warehouse_id}")
        }
        return stringBuilder.toString()
    }
}