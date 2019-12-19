package com.tokopedia.instantloan.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.instantloan.domain.interactor.GetFilterDataUseCase
import com.tokopedia.instantloan.domain.subscriber.GetFilterDataSubscriber
import com.tokopedia.instantloan.view.contractor.OnlineLoanContractor
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class OnlineLoanPresenter @Inject
constructor(val mGetFilterDataUseCase: GetFilterDataUseCase) :
        BaseDaggerPresenter<OnlineLoanContractor.View>(), OnlineLoanContractor.Presenter {

    @Inject
    lateinit var userSession: UserSession
    var subscriber = GetFilterDataSubscriber(this.view)

    override fun attachView(view: OnlineLoanContractor.View) {
        super.attachView(view)
        getFilterData()
    }

    override fun detachView() {
        mGetFilterDataUseCase.setQuery(view.getFilterDataQuery())
        mGetFilterDataUseCase.unsubscribe()
        super.detachView()
    }

    private fun getFilterData() {
        mGetFilterDataUseCase.execute(subscriber)
    }
}
