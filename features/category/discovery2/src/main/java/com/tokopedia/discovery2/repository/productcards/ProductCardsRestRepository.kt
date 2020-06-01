package com.tokopedia.discovery2.repository.productcards

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.network.data.model.response.DataResponse
import javax.inject.Inject

class ProductCardsRestRepository @Inject constructor() : BaseRepository(), ProductCardsRepository {

    override suspend fun getProducts(componentId: Int, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, isHorizontal : Boolean): ArrayList<ComponentsItem> {
        val response = getRestData<DataResponse<DiscoveryResponse>>(GenerateUrl.getComponentUrl(pageEndPoint, componentId),
                object : TypeToken<DataResponse<DiscoveryResponse>>() {}.type,
                RequestType.GET,
                queryParamterMap)

        return if(isHorizontal)
            DiscoveryDataMapper().mapListToComponentList(response.data.component?.data , "product_card_horizontal_item", null, "carousel")
        else
            DiscoveryDataMapper().mapListToComponentList(response.data.component?.data , "product_card_item", null, "product_card_revamp")
    }
}