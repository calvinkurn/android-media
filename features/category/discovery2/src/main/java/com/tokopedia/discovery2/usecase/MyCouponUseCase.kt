package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
import com.tokopedia.discovery2.data.mycoupon.MyCouponsRequest
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.mycoupon.MyCouponRepository
import javax.inject.Inject

class MyCouponUseCase @Inject constructor(private val myCouponRepository: MyCouponRepository) {

    suspend fun getMyCouponData(componentId: String, pageEndPoint: String, myCouponsRequest: MyCouponsRequest): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            var couponListData = mutableListOf<ComponentsItem>()
            val myCouponResponse = myCouponRepository.getCouponData(myCouponsRequest)
            myCouponResponse.tokopointsCouponListStack?.let { myCouponRes ->
                if (!myCouponRes.coupons.isNullOrEmpty()) {
                    couponListData = mapCouponListToComponentList(myCouponRes.coupons!!,
                            ComponentNames.MyCouponItem.componentName, component.name
                            ?: "", component.id)
                }
            }
            it.setComponentsItem(couponListData)
            it.noOfPagesLoaded = 1
            return true
        }
        return false
    }

    private fun mapCouponListToComponentList(itemList: List<MyCoupon>, subComponentName: String = "",
                                             parentComponentName: String = "", compId: String = ""): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        itemList.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = subComponentName
            componentsItem.parentComponentName = parentComponentName
            componentsItem.parentComponentId = compId
            val dataItem = mutableListOf<MyCoupon>()
            dataItem.add(it)
            componentsItem.myCouponList = dataItem
            list.add(componentsItem)
        }
        return list
    }
}