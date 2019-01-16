package com.tokopedia.expresscheckout.view.profile

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

interface CheckoutProfileContract {

    interface View : CustomerView {

        fun showLoading()

        fun hideLoading()

        fun getActivityContext(): Context?

        fun showToasterError(message: String?)

        fun setData(data: ArrayList<ProfileViewModel>)
    }

    interface Presenter : CustomerPresenter<View> {

        fun loadData()
    }

}