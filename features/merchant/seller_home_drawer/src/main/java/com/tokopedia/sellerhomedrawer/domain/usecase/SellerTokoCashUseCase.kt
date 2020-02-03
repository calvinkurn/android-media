package com.tokopedia.sellerhomedrawer.domain.usecase

import com.tokopedia.sellerhomedrawer.data.SellerTokoCashData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SellerTokoCashUseCase(private val sellerTokoCashData: Observable<SellerTokoCashData>): UseCase<SellerTokoCashData>() {

    override fun createObservable(requestParams: RequestParams?): Observable<SellerTokoCashData> {
        return sellerTokoCashData
    }

}