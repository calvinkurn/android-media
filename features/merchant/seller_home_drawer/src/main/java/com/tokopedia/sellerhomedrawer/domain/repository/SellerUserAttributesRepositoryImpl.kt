package com.tokopedia.sellerhomedrawer.domain.repository

import com.tokopedia.sellerhomedrawer.data.SellerUserData
import com.tokopedia.usecase.RequestParams
import rx.Observable

class SellerUserAttributesRepositoryImpl: SellerUserAttributesRepository {

    override fun getConsumerUserAttributes(parameters: RequestParams): Observable<SellerUserData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSellerUserAttributes(parameters: RequestParams): Observable<SellerUserData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}