package com.tokopedia.kol.feature.report.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by milhamj on 08/11/18.
 */
interface ContentReportContract {
    interface View : CustomerView {

    }

    interface Presenter : CustomerPresenter<View> {

    }
}