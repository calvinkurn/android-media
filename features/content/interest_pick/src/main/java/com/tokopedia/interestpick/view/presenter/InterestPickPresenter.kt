package com.tokopedia.interestpick.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.interestpick.domain.usecase.GetInterestUseCase
import com.tokopedia.interestpick.view.listener.InterestPickContract
import javax.inject.Inject

/**
 * @author by milhamj on 07/09/18.
 */
class InterestPickPresenter @Inject constructor(val getInterestUseCase: GetInterestUseCase)
    : BaseDaggerPresenter<InterestPickContract.View>(), InterestPickContract.Presenter {

    override fun detachView() {
        super.detachView()
        getInterestUseCase.unsubscribe()
    }

    override fun fetchData() {
        getInterestUseCase.execute(
                null,
                null
        )
    }
}