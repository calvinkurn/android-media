package com.tokopedia.expresscheckout.view.variant

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantPresenter : BaseDaggerPresenter<CheckoutVariantContract.View>(), CheckoutVariantContract.Presenter {

    override fun attachView(view: CheckoutVariantContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
    }

    override fun loadData() {
        // Todo : load data using usecase
//        view.showData()
    }

}