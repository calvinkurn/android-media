package com.tokopedia.developer_options.presentation.feedbackpage.ui

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.developer_options.api.request.FeedbackFormRequest
import okhttp3.MultipartBody

interface FeedbackPageContract {

    interface View: CustomerView{
        fun showLoadingDialog()
        fun hideLoadingDialog()
        fun setSubmitFlag()
        fun checkUriImage(feedbackId: Int?)
        fun goToTicketCreatedActivity()
        fun showError(throwable: Throwable)
    }

    interface Presenter{
        fun sendFeedbackForm(feedbackFormRequest: FeedbackFormRequest)
        fun sendAttachment(feedbackId: Int?, fileData: MultipartBody.Part)
        fun commitData(feedbackId: Int?)
    }
}