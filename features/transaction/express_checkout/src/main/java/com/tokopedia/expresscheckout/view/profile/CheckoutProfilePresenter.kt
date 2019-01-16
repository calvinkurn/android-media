package com.tokopedia.expresscheckout.view.profile

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class CheckoutProfilePresenter : BaseDaggerPresenter<CheckoutProfileContract.View>(), CheckoutProfileContract.Presenter {

    override fun attachView(view: CheckoutProfileContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
    }

    override fun loadData() {
        view?.showLoading()
    }

}