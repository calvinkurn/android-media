package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.claim_coupon.CatalogWithCouponList
import com.tokopedia.discovery2.data.claim_coupon.ClaimCouponRequest
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponGqlRepository
import com.tokopedia.kotlin.extensions.view.ONE
import javax.inject.Inject

class ClaimCouponUseCase @Inject constructor(val claimCouponRepository: IClaimCouponGqlRepository) {

    suspend fun getClickCouponData(componentId: String, pageIdentifier: String, claimCouponRequest: ClaimCouponRequest): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        if (component?.noOfPagesLoaded == Int.ONE) {
            return false
        }
        component?.let { cmp ->
            var claimCouponListData = mutableListOf<ComponentsItem>()
            val claimCouponResponse = claimCouponRepository.getClickCouponData(claimCouponRequest)
            claimCouponResponse.tokopointsCatalogWithCouponList?.let { claimCouponRes ->
                if (!claimCouponRes.catalogWithCouponList.isNullOrEmpty()) {
                    claimCouponListData = mapClaimCouponListToComponentList(
                        claimCouponRes.catalogWithCouponList,
                        component
                    )
                }
            }
            cmp.setComponentsItem(claimCouponListData, component.tabName)

            cmp.noOfPagesLoaded = Int.ONE
            return true
        }
        return false
    }

    private fun mapClaimCouponListToComponentList(
        itemList: List<CatalogWithCouponList>,
        component: ComponentsItem
    ): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        itemList.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem().apply {
                position = index
                name = ComponentNames.ClaimCouponItem.componentName
                parentListSize = itemList.size
                parentComponentName = component.name
                parentComponentId = component.id
                creativeName = component.creativeName
                properties = component.properties

                claimCouponList = mutableListOf(it)
            }

            list.add(componentsItem)
        }
        return list
    }
}
