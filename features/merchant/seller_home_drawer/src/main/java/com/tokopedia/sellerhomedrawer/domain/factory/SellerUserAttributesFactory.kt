package com.tokopedia.sellerhomedrawer.domain.factory

import com.tokopedia.sellerhomedrawer.domain.datasource.SellerCloudAttrDataSource
import com.tokopedia.sellerhomedrawer.domain.service.SellerDrawerService

class SellerUserAttributesFactory(val drawerService: SellerDrawerService) {

    fun getCloudAttrDataSource() = SellerCloudAttrDataSource(drawerService)
}