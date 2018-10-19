package com.tokopedia.promocheckout.detail

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface PromoCheckoutDetailContract {
    interface View : CustomerView{

    }

    interface Presenter : CustomerPresenter<View>{

    }
}