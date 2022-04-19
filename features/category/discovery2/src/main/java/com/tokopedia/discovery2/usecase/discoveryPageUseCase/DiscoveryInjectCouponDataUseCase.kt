package com.tokopedia.discovery2.usecase.discoveryPageUseCase

import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryInjectCouponGQLRepository
import javax.inject.Inject

class DiscoveryInjectCouponDataUseCase @Inject constructor(private val discoveryInjectCouponGQLRepository: DiscoveryInjectCouponGQLRepository) {
    suspend fun sendDiscoveryInjectCouponData(){
        discoveryInjectCouponGQLRepository.sendInjectCouponData()
    }
}
