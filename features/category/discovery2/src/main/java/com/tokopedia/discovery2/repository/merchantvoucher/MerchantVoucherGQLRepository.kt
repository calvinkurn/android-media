package com.tokopedia.discovery2.repository.merchantvoucher

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MerchantVoucherGQLRepository @Inject constructor() : BaseRepository(), MerchantVoucherRepository {
    override suspend fun getMerchantVouchers(
        componentId: String,
        queryParamterMap: MutableMap<String, Any>,
        pageEndPoint: String,
        productComponentName: String?
    ): Pair<ArrayList<ComponentsItem>, String?> {
        val response = (getGQLData(
            GQL_COMPONENT,
            DataResponse::class.java,
            Utils.getComponentsGQLParams(
                componentId,
                pageEndPoint,
                Utils.getQueryString(queryParamterMap)
            ),
            GQL_COMPONENT_QUERY_NAME
        ) as DataResponse)

        val componentData = response.data.component?.data
        val componentProperties = response.data.component?.properties
        val creativeName = response.data.component?.creativeName ?: ""
        val compAdditionalInfo = response.data.component?.compAdditionalInfo
        val nextPage = compAdditionalInfo?.nextPage
        val subComponentName = when(productComponentName){
            ComponentNames.MerchantVoucherList.componentName -> ComponentNames.MerchantVoucherListItem.componentName
            ComponentNames.MerchantVoucherGrid.componentName -> ComponentNames.MerchantVoucherGridItem.componentName
            else -> ComponentNames.MerchantVoucherCarouselItem.componentName
        }
        val list = withContext(Dispatchers.Default) {
            DiscoveryDataMapper().mapDataItemToMerchantVoucherComponent(
                componentData,
                subComponentName,
                componentProperties,
                creativeName,
                compAdditionalInfo
            )
        }
        return Pair(list, nextPage)
    }
}
