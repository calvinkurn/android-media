package com.tokopedia.kol.feature.report.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract

/**
 * @author by milhamj on 12/11/18.
 */
class ContentReportPresenter : BaseDaggerPresenter<ContentReportContract.View>(),
        ContentReportContract.Presenter {
    override fun sendReport() {

    }
}