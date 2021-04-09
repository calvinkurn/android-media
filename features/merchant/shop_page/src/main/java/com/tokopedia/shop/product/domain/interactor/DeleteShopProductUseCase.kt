package com.tokopedia.shop.product.domain.interactor

import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by normansyahputa on 2/23/18.
 */
class DeleteShopProductUseCase @Inject constructor(private val deleteShopProductAceUseCase: DeleteShopProductAceUseCase,
                                                   private val deleteShopProductTomeUseCase: DeleteShopProductTomeUseCase) : UseCase<Boolean>() {
    fun createObservable(): Observable<Boolean> {
        return createObservable(RequestParams.create())
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        return Observable.zip(deleteShopProductAceUseCase.createObservable().subscribeOn(Schedulers.io()),
                deleteShopProductTomeUseCase.createObservable().subscribeOn(Schedulers.io())
        ) { aBoolean, aBoolean2 -> aBoolean && aBoolean2 }
    }
}