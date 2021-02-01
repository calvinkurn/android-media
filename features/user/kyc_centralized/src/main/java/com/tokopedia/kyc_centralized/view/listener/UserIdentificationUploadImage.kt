package com.tokopedia.kyc_centralized.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel
import com.tokopedia.usecase.RequestParams
import rx.Observable

/**
 * @author by alvinatin on 21/11/18.
 */
interface UserIdentificationUploadImage {
    interface View : CustomerView {
        val getContext: Context?
        fun showLoading()
        fun hideLoading()
    }
}