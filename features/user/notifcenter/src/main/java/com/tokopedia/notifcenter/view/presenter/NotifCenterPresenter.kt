package com.tokopedia.notifcenter.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.notifcenter.domain.usecase.NotifCenterUseCase
import com.tokopedia.notifcenter.view.listener.NotifCenterContract
import com.tokopedia.notifcenter.view.subscriber.NotifCenterSubscriber
import javax.inject.Inject

/**
 * @author by milhamj on 30/08/18.
 */

class NotifCenterPresenter @Inject constructor(private val notifCenterUseCase: NotifCenterUseCase)
    : BaseDaggerPresenter<NotifCenterContract.View>(), NotifCenterContract.Presenter {

    private var filterId = 0
    private var page = 0

    override fun detachView() {
        super.detachView()
        notifCenterUseCase.unsubscribe()
    }

    override fun fetchData() {
        view.showLoading()
        notifCenterUseCase.execute(
                NotifCenterUseCase.getRequestParams(page, filterId),
                NotifCenterSubscriber(view)
        )
    }

    override fun updatePage(page: Int) {
        this.page = page
    }

    override fun updateFilterId(filterId: Int) {
        this.filterId = filterId
    }
}
