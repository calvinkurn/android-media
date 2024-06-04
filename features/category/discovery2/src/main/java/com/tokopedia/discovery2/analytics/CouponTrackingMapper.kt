package com.tokopedia.discovery2.analytics

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.claim_coupon.CatalogWithCouponList
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
import com.tokopedia.discovery2.datamapper.getComponent

object CouponTrackingMapper {

    //region Image Banner
    fun List<DataItem>.toTrackingProps() = map {
        it.toTrackingProps()
    }

    fun DataItem.toTrackingProps() = CouponTrackingProperties(
        parentComponentName.orEmpty(),
        name.orEmpty(),
        id.orEmpty(),
        code.orEmpty(),
        creativeName.orEmpty(),
        positionForParentItem,
        action.orEmpty(),
        gtmItemName,
        tabName.orEmpty()
    )
    //endregion

    //region My Coupon
    fun List<MyCoupon>.toTrackingProps(component: ComponentsItem): List<CouponTrackingProperties> {
        val catalogSlug = component.data?.first()?.catalogSlug.orEmpty()

        return map { item ->

            val couponName = catalogSlug
                .filter { !it.isNullOrEmpty() }
                .find { slug -> item.code?.contains(slug!!) == true }

            CouponTrackingProperties(
                component.parentComponentName.orEmpty(),
                couponName.orEmpty(),
                component.parentComponentId,
                item.code.orEmpty(),
                component.creativeName.orEmpty(),
                component.position,
                EMPTY_STRING,
                getGTMItemName(component.parentComponentId, component.pageEndPoint),
                component.tabName.orEmpty()
            )
        }
    }
    //endregion

    //region Claim Coupon
    fun ComponentsItem.toTrackingProps(): List<CouponTrackingProperties> {
        return claimCouponList?.map { item ->
            toTrackingProperties(item)
        } ?: emptyList()
    }

    fun ComponentsItem.toTrackingProperties(
        claimCouponItem: CatalogWithCouponList
    ) = with(claimCouponItem) {
        CouponTrackingProperties(
            parentComponentName.orEmpty(),
            slug.orEmpty(),
            parentComponentId,
            couponCode.orEmpty(),
            creativeName.orEmpty(),
            position,
            buttonStr.orEmpty(),
            getGTMItemName(parentComponentId, pageEndPoint),
            tabName.orEmpty()
        )
    }
    //endregion

    //region automate claim coupon
    fun ComponentsItem.toTrackingProperties(ctaText: String = ""): CouponTrackingProperties {
        val slug = data?.firstOrNull()?.catalogSlug?.firstOrNull().orEmpty()
        val gtmItemName = data?.firstOrNull()
            ?.gtmItemName
            .orEmpty()

        return CouponTrackingProperties(
            componentName = parentComponentName.orEmpty(),
            name = slug,
            id = id.substringAfter("_"),
            promoCode = slug,
            creativeName = creativeName.orEmpty(),
            position = position,
            action = ctaText,
            gtmItem = gtmItemName,
            tabName = tabName.orEmpty()
        )
    }

    //endregion

    private fun getGTMItemName(componentId: String, pageName: String) =
        getComponent(componentId, pageName)
            ?.data?.firstOrNull()
            ?.gtmItemName
            .orEmpty()
}
