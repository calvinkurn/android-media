package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
import com.tokopedia.discovery2.data.mycoupon.MyCouponsRequest
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.mycoupon.MyCouponRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class MyCouponUseCase @Inject constructor(private val myCouponRepository: MyCouponRepository) {

    suspend fun getMyCouponData(
        componentId: String,
        pageEndPoint: String,
        myCouponsRequest: MyCouponsRequest
    ): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            var couponListData = mutableListOf<ComponentsItem>()
            val myCouponResponse = myCouponRepository.getCouponData(myCouponsRequest)
            myCouponResponse.tokopointsCouponListStack?.let { myCouponRes ->
                if (!myCouponRes.coupons.isNullOrEmpty()) {
                    couponListData = mapCouponListToComponentList(
                        myCouponRes.coupons!!,
                        ComponentNames.MyCouponItem.componentName,
                        component.name ?: "",
                        it
                    )
                }
            }
            it.setComponentsItem(couponListData)
            it.noOfPagesLoaded = 1
            return true
        }
        return false
    }

    private suspend fun mapCouponListToComponentList(
        coupons: List<MyCoupon>, subComponentName: String = "",
        parentComponentName: String = "", component: ComponentsItem
    ): ArrayList<ComponentsItem> {
        return withContext(Dispatchers.Default) {
            val list = ArrayList<ComponentsItem>()
            val itemList = getSortedCouponsBasedOnSlugs(component, coupons)
            itemList.forEachIndexed { index, it ->
                val componentsItem = ComponentsItem()
                componentsItem.position = index
                componentsItem.name = subComponentName
                componentsItem.parentListSize = itemList.size
                componentsItem.parentComponentName = parentComponentName
                componentsItem.parentComponentId = component.id
                val dataItem = mutableListOf<MyCoupon>()
                dataItem.add(it)
                componentsItem.myCouponList = dataItem
                list.add(componentsItem)
            }
            return@withContext list
        }
    }

    private suspend fun getSortedCouponsBasedOnSlugs(
        component: ComponentsItem,
        coupons: List<MyCoupon>
    ): List<MyCoupon> {
        return withContext(Dispatchers.Default) {
            if (component.data?.firstOrNull()?.pinnedSlugs.isNullOrEmpty())
                return@withContext coupons
            val map = mutableMapOf<String, MutableList<MyCoupon>>()
            component.data?.firstOrNull()?.pinnedSlugs?.let { slugList ->
                slugList.forEach { slug ->
                    if (!slug.isNullOrEmpty()) {
                        map[slug] = mutableListOf()
                    }
                }
            }
            if (map.isEmpty())
                return@withContext coupons
            val listOfUnPinnedSlugCoupons = mutableListOf<MyCoupon>()
            coupons.forEach { coupon ->
                var slugFound = false
                for ((slug, couponList) in map) {
                    if (coupon.code?.startsWith(slug) == true) {
                        couponList.add(coupon)
                        slugFound = true
                        break
                    }
                }
                if (!slugFound)
                    listOfUnPinnedSlugCoupons.add(coupon)
            }
            val itemList: LinkedList<MyCoupon> = LinkedList()
            for ((_, couponList) in map) {
                itemList.addAll(couponList)
            }
            itemList.addAll(listOfUnPinnedSlugCoupons)
            return@withContext itemList
        }
    }
}
