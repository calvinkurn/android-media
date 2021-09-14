package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.query.GetHomeLayoutList
import com.tokopedia.usecase.RequestParams
import java.lang.StringBuilder
import javax.inject.Inject

class GetHomeLayoutListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetHomeLayoutResponse>(graphqlRepository) {

    companion object {
        private const val TYPE = "type"
        private const val LOCATION = "location"
        private const val TOKONOW = "tokonow"
    }

    init {
        setGraphqlQuery(GetHomeLayoutList.QUERY)
        setTypeClass(GetHomeLayoutResponse::class.java)
    }

    suspend fun execute(localCacheModel: LocalCacheModel?): List<HomeLayoutResponse> {
        setRequestParams(RequestParams.create().apply {
            putString(TYPE, TOKONOW)
            putString(LOCATION, mapLocation(localCacheModel))
        }.parameters)
        // temp hardcoded
        val data = executeOnBackground().response.data
        val d: MutableList<HomeLayoutResponse> = mutableListOf()
        d.addAll(data)
        d.add(HomeLayoutResponse(id = "test1", layout = "tokonow_usp"))
        d.add(HomeLayoutResponse(id = "test2", layout = "tokonow_share"))
        return d
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