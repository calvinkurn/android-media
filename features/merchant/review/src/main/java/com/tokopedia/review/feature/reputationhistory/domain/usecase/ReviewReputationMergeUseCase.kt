package com.tokopedia.review.feature.reputationhistory.domain.usecase

import javax.inject.Inject
import com.tokopedia.review.feature.reputationhistory.domain.interactor.ReviewReputationUseCase
import com.tokopedia.review.feature.reputationhistory.domain.interactor.ShopInfoUseCase
import com.tokopedia.review.feature.reputationhistory.util.ShopNetworkController
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author normansyahputa on 3/17/17.
 */
class ReviewReputationMergeUseCase @Inject constructor(
    private val reviewReputationUseCase: ReviewReputationUseCase,
    private val shopInfoUseCase: ShopInfoUseCase
) : UseCase<List<Any>>() {

    fun createObservable(
        userid: String?, deviceId: String?,
        shopInfoParam: ShopNetworkController.ShopInfoParam?,
        shopId: String?, param: Map<String?, String?>?
    ): Observable<List<Any>> {
        return Observable.concat(
            shopInfoUseCase.createObservable(userid, deviceId, shopInfoParam),
            reviewReputationUseCase.createObservable(shopId, param)
        ).toList()
    }

    fun execute(
        userid: String?, deviceId: String?,
        shopInfoParam: ShopNetworkController.ShopInfoParam?,
        shopId: String?, param: Map<String?, String?>?, subscriber: Subscriber<List<Any>>?
    ) {
        createObservable(userid, deviceId, shopInfoParam, shopId, param)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<Any>> {
        return Observable.concat(
            shopInfoUseCase.createObservable(requestParams.getObject(RequestParamFactory.KEY_SHOP_INFO_CONTAINER_PARAM) as RequestParams),
            reviewReputationUseCase.createObservable(requestParams.getObject(RequestParamFactory.KEY_REVIEW_REPUTATION_CONTAINER_PARAM) as RequestParams)
        ).toList()
    }

    object RequestParamFactory {
        const val KEY_REVIEW_REPUTATION_CONTAINER_PARAM = "KEY_REVIEW_REPUTATION_CONTAINER_PARAM"
        const val KEY_SHOP_INFO_CONTAINER_PARAM = "KEY_SHOP_INFO_CONTAINER_PARAM"
        @JvmStatic
        fun generateRequestParam(
            reviewParam: RequestParams?,
            shopInfoParam: RequestParams?
        ): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(KEY_REVIEW_REPUTATION_CONTAINER_PARAM, reviewParam)
            requestParams.putObject(KEY_SHOP_INFO_CONTAINER_PARAM, shopInfoParam)
            return requestParams
        }
    }
}