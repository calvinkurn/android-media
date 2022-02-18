package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponGqlRepository
import com.tokopedia.discovery2.repository.mycoupon.MyCouponRepository
import javax.inject.Inject

class MyCouponUseCase @Inject constructor(val myCouponRepository: MyCouponRepository) {

    suspend fun getClickCouponData(componentId: String, pageIdentifier: String): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        if (component?.noOfPagesLoaded == 1)
            return false
        component?.let { cmp ->
            cmp.setComponentsItem(myCouponRepository.getCouponData(componentId, mutableMapOf(), pageIdentifier), component.tabName)
            cmp.noOfPagesLoaded = 1
            return true
        }
        return false
    }
}