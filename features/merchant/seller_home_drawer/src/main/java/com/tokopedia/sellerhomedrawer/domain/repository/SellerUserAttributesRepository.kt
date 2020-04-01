package com.tokopedia.sellerhomedrawer.domain.repository

import com.tokopedia.sellerhomedrawer.data.SellerUserData
import com.tokopedia.usecase.RequestParams
import rx.Observable

interface SellerUserAttributesRepository {

    fun getSellerUserAttributes(parameters: RequestParams): Observable<SellerUserData>
}