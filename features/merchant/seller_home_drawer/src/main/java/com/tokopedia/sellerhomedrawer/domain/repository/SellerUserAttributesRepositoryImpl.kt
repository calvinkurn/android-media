package com.tokopedia.sellerhomedrawer.domain.repository

import com.tokopedia.sellerhomedrawer.data.SellerUserData
import com.tokopedia.sellerhomedrawer.domain.factory.SellerUserAttributesFactory
import com.tokopedia.usecase.RequestParams
import rx.Observable

class SellerUserAttributesRepositoryImpl(val sellerUserAttributesFactory: SellerUserAttributesFactory): SellerUserAttributesRepository {

    override fun getSellerUserAttributes(parameters: RequestParams): Observable<SellerUserData> {
        return sellerUserAttributesFactory.getCloudAttrDataSource().getSellerUserAttributes(parameters)
    }
}