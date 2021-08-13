package com.tokopedia.shop.sort.view.presenter

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
import com.tokopedia.shop.sort.domain.interactor.GetShopProductSortUseCase
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by normansyahputa on 2/23/18.
 */
class ShopProductSortPresenter @Inject constructor(private val getShopProductFilterUseCase: GetShopProductSortUseCase, private val shopProductFilterMapper: ShopProductSortMapper, private val userSession: UserSessionInterface) : BaseDaggerPresenter<BaseListViewListener<ShopProductSortModel>>() {
    fun getShopFilterList() {
        getShopProductFilterUseCase.execute(object : Subscriber<List<ShopProductSort>>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view?.showGetListError(e)
                }
            }

            override fun onNext(dataValue: List<ShopProductSort>) {
                dataValue?.let {
                    view?.renderList(shopProductFilterMapper.convertSort(it))
                }
            }
        })
    }

    fun isMyShop(shopId: String): Boolean {
        return userSession.shopId == shopId
    }

    override fun detachView() {
        super.detachView()
        getShopProductFilterUseCase.unsubscribe()
    }
}