package com.tokopedia.notifcenter.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.notifcenter.domain.usecase.NotifCenterUseCase
import com.tokopedia.notifcenter.view.listener.NotifCenterContract
import com.tokopedia.notifcenter.view.subscriber.NotifCenterSubscriber
import com.tokopedia.notifcenter.view.util.NotifCenterDateUtil
import javax.inject.Inject

/**
 * @author by milhamj on 30/08/18.
 */

class NotifCenterPresenter @Inject constructor(private val notifCenterUseCase: NotifCenterUseCase,
                                               private val dateUtil: NotifCenterDateUtil)
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
                NotifCenterSubscriber(view, dateUtil)
        )
    }

    override fun fetchDataWithoutCache() {
        view.showLoading()
        notifCenterUseCase.executeNoCache(
                NotifCenterUseCase.getRequestParams(page, filterId),
                NotifCenterSubscriber(view, dateUtil)
        )
    }

    override fun updatePage() {
        this.page++
    }

    override fun updateFilterId(filterId: Int) {
        this.filterId = filterId
    }

    override fun resetParam() {
        filterId = 0
        page = 0
    }
}
