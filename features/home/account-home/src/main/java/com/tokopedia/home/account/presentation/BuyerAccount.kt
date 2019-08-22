package com.tokopedia.home.account.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel
import com.tokopedia.home.account.presentation.listener.BaseAccountView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem


/**
 * @author okasurya on 7/17/18.
 */
interface BuyerAccount {
    interface View : BaseAccountView {
        fun loadBuyerData(model: BuyerViewModel)
        fun hideLoadMoreLoading()
        fun showLoadMoreLoading()
        fun onRenderRecomAccountBuyer(list: List<Visitable<*>>)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getBuyerData(query: String, saldoQuery: String)
        fun getFirstRecomData()
        fun getRecomData(page: Int)
        fun addWishlist(model: RecommendationItem,  callback: (Boolean, Throwable?) -> Unit)
        fun removeWishlist(model: RecommendationItem,  callback: (Boolean, Throwable?) -> Unit)
    }
}
