package com.tokopedia.interestpick.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.interestpick.view.viewmodel.InterestPickItemViewModel

/**
 * @author by milhamj on 07/09/18.
 */
interface InterestPickContract {
    interface View : CustomerView {
        fun getContext(): Context?

        fun showLoading()

        fun showProgress()

        fun hideLoading()

        fun onSuccessGetInterest(interestList: ArrayList<InterestPickItemViewModel>,
                                 title: String)

        fun onErrorGetInterest(message: String)

        fun onSuccessUpdateInterest()

        fun onErrorUpdateInterest(message: String)

        fun onItemSelected(isSelected: Boolean)

        fun onBackPressed()
    }

    interface Presenter : CustomerPresenter<View> {
        fun fetchData()

        fun updateInterest(interestIds: Array<Int>)

        fun onBackPressed()
    }
}