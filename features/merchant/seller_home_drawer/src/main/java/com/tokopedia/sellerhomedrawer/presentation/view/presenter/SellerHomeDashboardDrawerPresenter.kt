package com.tokopedia.sellerhomedrawer.presentation.view.presenter

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.data.GoldGetPmOsStatus
import com.tokopedia.sellerhomedrawer.domain.usecase.FlashSaleGetSellerStatusUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.GetShopStatusUseCase
import com.tokopedia.sellerhomedrawer.presentation.view.SellerHomeDashboardContract
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class SellerHomeDashboardDrawerPresenter @Inject constructor(val getShopStatusUseCase: GetShopStatusUseCase,
                                                             val flashSaleGetSellerStatusUseCase: FlashSaleGetSellerStatusUseCase,
                                                             val userSession: UserSessionInterface,
                                                             val context: Context) : SellerHomeDashboardContract.Presenter {

    init {
        flashSaleGetSellerStatusUseCase.isCached = true
    }

    var view: SellerHomeDashboardContract.View? = null

    fun attachView(sellerHomeDashboardView: SellerHomeDashboardContract.View) {
        view = sellerHomeDashboardView
    }

    fun getFlashSaleSellerStatus() {
        val rawQuery = GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_seller_status)
        val params = FlashSaleGetSellerStatusUseCase.createRequestParams(rawQuery, userSession.shopId)
        flashSaleGetSellerStatusUseCase.execute(params, object : Subscriber<Boolean>() {
            override fun onNext(isVisible: Boolean?) {
                if (isVisible != null)
                    view?.onSuccessGetFlashSaleSellerStatus(isVisible)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }
        })
    }

    fun isGoldMerchantAsync() {
        getShopStatusUseCase.execute(GetShopStatusUseCase.createRequestParams(userSession.shopId), object : Subscriber<GoldGetPmOsStatus>() {
            override fun onNext(goldGetPmOsStatus: GoldGetPmOsStatus?) {
                if (goldGetPmOsStatus != null)
                    view?.onSuccessGetShopInfo(goldGetPmOsStatus)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }
        })
    }

    fun unsubscribe() {
        flashSaleGetSellerStatusUseCase.unsubscribe()
        getShopStatusUseCase.unsubscribe()
        view = null
    }

}