package com.tokopedia.home.beranda.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase
import com.tokopedia.home.beranda.presentation.view.subscriber.GetHomeFeedsSubscriber
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Lukas on 2019-07-16
 */
open class HomeFeedPresenter @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getHomeFeedUseCase: GetHomeFeedUseCase
) : BaseDaggerPresenter<HomeFeedContract.View>(), HomeFeedContract.Presenter{

    fun loadData(recomId: Int, count: Int, page: Int) {
        getHomeFeedUseCase.execute(
                getHomeFeedUseCase.getHomeFeedParam(recomId, count, page),
                GetHomeFeedsSubscriber(view))
    }

    override fun detachView() {
        super.detachView()
        getHomeFeedUseCase.unsubscribe()
    }
}