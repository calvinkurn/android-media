package com.tokopedia.shop.settings.basicinfo.view.presenter

import com.tokopedia.gm.common.constant.GMParamApiContant
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetShopBasicDataAndStatusUseCase @Inject
constructor(private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
            private val getShopStatusUseCase: GetShopStatusUseCase) : UseCase<Pair<ShopBasicDataModel?, ShopStatusModel?>>() {

    override fun createObservable(requestParams: RequestParams): Observable<Pair<ShopBasicDataModel?, ShopStatusModel?>> {
        return Observable.zip(
            getShopBasicDataUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io()),
            getShopStatusUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())){ t1: ShopBasicDataModel?, t2: ShopStatusModel? ->
            t1 to t2
        }
    }

    companion object {
        fun createRequestParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putString(GMParamApiContant.SHOP_ID, shopId)
            }
        }
    }

    override fun unsubscribe() {
        super.unsubscribe()
        getShopBasicDataUseCase.unsubscribe()
        getShopStatusUseCase.unsubscribe()
    }

}
