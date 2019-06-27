package com.tokopedia.expresscheckout.view.profile

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.expresscheckout.domain.model.profile.ProfileResponseModel
import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

interface CheckoutProfileContract {

    interface View : CustomerView {

        fun showLoading()

        fun hideLoading()

        fun showErrorPage(message: String)

        fun getActivityContext(): Context?

        fun setData(data: ArrayList<ProfileViewModel>)
    }

    interface Presenter : CustomerPresenter<View> {

        fun loadData()

        fun prepareViewModel(profileResponseModel: ProfileResponseModel)
    }

}