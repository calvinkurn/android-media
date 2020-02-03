package com.tokopedia.sellerhomedrawer.domain.repository

import android.database.Observable
import com.tokopedia.sellerhomedrawer.data.SellerUserData
import com.tokopedia.usecase.RequestParams

interface SellerUserRepository {

    fun getConsumerUserAttributes(parameters: RequestParams): Observable<SellerUserData>
    fun getSellerUserAttributes(parameters: RequestParams): Observable<SellerUserData>
}