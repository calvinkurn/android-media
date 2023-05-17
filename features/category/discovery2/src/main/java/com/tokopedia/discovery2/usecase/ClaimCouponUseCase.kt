package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.data.claim_coupon.CatalogWithCouponList
import com.tokopedia.discovery2.data.claim_coupon.ClaimCouponRequest
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponGqlRepository
import javax.inject.Inject

class ClaimCouponUseCase @Inject constructor(val claimCouponRepository: IClaimCouponGqlRepository) {

    suspend fun getClickCouponData(componentId: String, pageIdentifier: String,claimCouponRequest: ClaimCouponRequest): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        if (component?.noOfPagesLoaded == 1)
            return false
        component?.let { cmp ->
            var claimCouponListData = mutableListOf<ComponentsItem>()
            val claimCouponResponse = claimCouponRepository.getClickCouponData(claimCouponRequest)
            claimCouponResponse.tokopointsCatalogWithCouponList?.let { claimCouponRes ->
                if (!claimCouponRes.catalogWithCouponList.isNullOrEmpty()) {
                    claimCouponListData = mapClaimCouponListToComponentList(claimCouponRes.catalogWithCouponList,
                        ComponentNames.ClaimCouponItem.componentName, component.name
                        ?: "", component.id, component.properties)
                }
            }
            cmp.setComponentsItem(claimCouponListData, component.tabName)

            cmp.noOfPagesLoaded = 1
            return true
        }
        return false
    }

    private fun mapClaimCouponListToComponentList(itemList: List<CatalogWithCouponList>, subComponentName: String = "",
                                                  parentComponentName: String = "", compId: String = "",properties: Properties?): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        itemList.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = subComponentName
            componentsItem.parentListSize = itemList.size
            componentsItem.parentComponentName = parentComponentName
            componentsItem.parentComponentId = compId
            componentsItem.properties = properties
            val dataItem = mutableListOf<CatalogWithCouponList>()
            dataItem.add(it)
            componentsItem.claimCouponList = dataItem
            list.add(componentsItem)
        }
        return list
    }
}
