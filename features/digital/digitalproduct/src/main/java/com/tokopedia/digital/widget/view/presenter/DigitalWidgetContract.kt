package com.tokopedia.digital.widget.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.digital.widget.view.model.category.Category

/**
 * Created by Rizky on 19/11/18.
 */
class DigitalWidgetContract {

    interface View : CustomerView {

        fun renderDataRechargeCategory(rechargeCategory: List<Category>)

        fun failedRenderDataRechargeCategory()

        fun renderErrorNetwork(resId: Int)

    }

    interface Presenter : CustomerPresenter<View> {

        fun fetchDataRechargeCategory()

    }

}