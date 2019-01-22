package com.tokopedia.digital.widget.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.digital.widget.view.model.Recommendation

/**
 * Created by Rizky on 15/11/18.
 */
class DigitalChannelContract {

    interface View: CustomerView {

        fun renderRecommendationList(recommendations: List<Recommendation>)

        fun fetchCategoryList()

        fun showError(resId: Int)

        fun renderDigitalTitle(stringRes: Int)

    }

    interface Presenter: CustomerPresenter<View> {

        fun getRecommendationList(deviceId: Int)

    }

}