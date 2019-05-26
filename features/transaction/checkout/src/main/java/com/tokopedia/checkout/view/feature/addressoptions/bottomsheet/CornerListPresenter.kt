package com.tokopedia.checkout.view.feature.addressoptions.bottomsheet

import com.tokopedia.checkout.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.checkout.domain.usecase.GetCornerUseCase
import javax.inject.Inject

/**
 * Created by fajarnuha on 2019-05-26.
 */
class CornerListPresenter @Inject constructor(val usecase: GetCornerUseCase) {

    private var mView: CornerListFragment? = null

    fun attachView(view: CornerListFragment) {
        this.mView = view
    }

    fun getCorner() {
        usecase.execute("").subscribe({ mView?.setData(it.listAddress)}, {}, {})
    }

}