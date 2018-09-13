package com.tokopedia.shop.page.view.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException
import com.tokopedia.gm.common.domain.interactor.DeleteFeatureProductListCacheUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.data.pojo.WhitelistQuery
import com.tokopedia.kolcommon.domain.usecase.GetWhitelistUseCase
import com.tokopedia.kolcommon.domain.usecase.GetWhitelistUseCase.WHITELIST_SHOP
import com.tokopedia.reputation.common.domain.interactor.DeleteReputationSpeedDailyCacheUseCase
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.etalase.domain.interactor.DeleteShopEtalaseUseCase
import com.tokopedia.shop.note.domain.interactor.DeleteShopNoteUseCase
import com.tokopedia.shop.page.domain.interactor.ToggleFavouriteShopAndDeleteCacheUseCase
import com.tokopedia.shop.page.view.listener.ShopPageView
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductUseCase

import javax.inject.Inject

import rx.Subscriber

/**
 * Created by normansyahputa on 2/13/18.
 */

class ShopPagePresenter @Inject
constructor(private val getShopInfoUseCase: GetShopInfoUseCase,
            private val getShopInfoByDomainUseCase: GetShopInfoByDomainUseCase,
            private val toggleFavouriteShopAndDeleteCacheUseCase: ToggleFavouriteShopAndDeleteCacheUseCase,
            private val deleteShopProductUseCase: DeleteShopProductUseCase,
            private val deleteFeatureProductListCacheUseCase: DeleteFeatureProductListCacheUseCase,
            private val deleteShopInfoUseCase: DeleteShopInfoUseCase,
            private val deleteShopEtalaseUseCase: DeleteShopEtalaseUseCase,
            private val deleteShopNoteUseCase: DeleteShopNoteUseCase,
            private val deleteReputationSpeedDailyUseCase: DeleteReputationSpeedDailyCacheUseCase,
            private val getWhitelistUseCase: GetWhitelistUseCase,
            private val userSession: UserSession) : BaseDaggerPresenter<ShopPageView>() {

    fun isMyShop(shopId: String) = (userSession.shopId == shopId)

    fun getShopInfo(shopId: String) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), object : Subscriber<ShopInfo>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                e.printStackTrace()
                view?.onErrorGetShopInfo(e)
            }

            override fun onNext(shopInfo: ShopInfo) {
                view?.onSuccessGetShopInfo(shopInfo)
            }
        })
    }

    fun getShopInfoByDomain(shopDomain: String) {
        getShopInfoByDomainUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(shopDomain), object : Subscriber<ShopInfo>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                view?.onErrorGetShopInfo(e)
            }

            override fun onNext(shopInfo: ShopInfo) {
                view?.onSuccessGetShopInfo(shopInfo)
            }
        })
    }

    fun toggleFavouriteShop(shopId: String) {
        if (!userSession.isLoggedIn) {
            view?.onErrorToggleFavourite(UserNotLoginException())
            return
        }
        toggleFavouriteShopAndDeleteCacheUseCase.execute(
                ToggleFavouriteShopUseCase.createRequestParam(shopId), object : Subscriber<Boolean>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        view?.onErrorToggleFavourite(e)
                    }

                    override fun onNext(success: Boolean) {
                        view?.onSuccessToggleFavourite(success)
                    }
        })
    }

    fun getFeedWhitelist(shopId: String) {
        getWhitelistUseCase.execute(
                GetWhitelistUseCase.createRequestParams(GetWhitelistUseCase.WHITELIST_SHOP),
                object : Subscriber<GraphqlResponse>() {
                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(graphqlResponse: GraphqlResponse?) {
                        graphqlResponse?.let {
                            val whitelistQuery: WhitelistQuery = it.getData(WhitelistQuery::class.java)
                            whitelistQuery.whitelist
                        }?.let {
                            if (!TextUtils.isEmpty(it.error)) {
                                return
                            }
                            view.onSuccessGetFeedWhitelist(it.isWhitelist)
                        }
                    }
                }
        )
    }

    fun clearCache() {
        deleteShopInfoUseCase.executeSync()
        deleteShopProductUseCase.executeSync()
        deleteShopEtalaseUseCase.executeSync()
        deleteShopNoteUseCase.executeSync()
        deleteFeatureProductListCacheUseCase.executeSync()
        deleteReputationSpeedDailyUseCase.executeSync()
    }

    override fun detachView() {
        super.detachView()
        getShopInfoUseCase.unsubscribe()
        getShopInfoByDomainUseCase.unsubscribe()
        toggleFavouriteShopAndDeleteCacheUseCase.unsubscribe()
        deleteShopInfoUseCase.unsubscribe()
        deleteShopProductUseCase.unsubscribe()
        deleteShopEtalaseUseCase.unsubscribe()
        deleteShopNoteUseCase.unsubscribe()
        deleteFeatureProductListCacheUseCase.unsubscribe()
        deleteReputationSpeedDailyUseCase.unsubscribe()
        getWhitelistUseCase.unsubscribe()
    }
}
