package com.tokopedia.checkout.view.feature.addressoptions.bottomsheet

import com.tokopedia.checkout.domain.usecase.GetCornerUseCase
import javax.inject.Inject

/**
 * Created by fajarnuha on 2019-05-26.
 */
class CornerListPresenter @Inject constructor(val usecase: GetCornerUseCase) : CornerContract.Presenter {

    private var mView: CornerContract.View? = null

    override fun attachView(view: CornerContract.View) {
        this.mView = view
    }

    override fun detachView() {
        this.mView = null
    }

    override fun getData() {
        usecase.execute("")
                .doOnSubscribe { mView?.setLoadingState(true) }
                .doOnTerminate { mView?.setLoadingState(false) }
                .subscribe(
                        { mView?.showData(it.listAddress) },
                        { e -> mView?.showError(e) }, {})
    }

    override fun searchQuery(query: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}