package com.tokopedia.kyc_centralized.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.kyc_centralized.view.subscriber.GetUserProjectInfoSubcriber

/**
 * @author by alvinatin on 08/11/18.
 */
interface UserIdentificationInfo {
    interface View : CustomerView {
        fun showLoading()
        fun hideLoading()
        val userProjectInfoListener: GetUserProjectInfoSubcriber.GetUserProjectInfoListener?
        val getContext: Context?
    }

    interface Presenter : CustomerPresenter<View?> {
        fun getInfo(projectId: Int)
    }
}