package com.tokopedia.developer_options.presentation.feedbackpage.ui.feedbackpage

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.CategoriesModel
import com.tokopedia.developer_options.presentation.feedbackpage.domain.request.FeedbackFormRequest
import okhttp3.MultipartBody

interface FeedbackPageContract {

    interface View: CustomerView{
        fun showLoadingDialog()
        fun hideLoadingDialog()
        fun setSubmitFlag()
        fun checkUriImage(feedbackId: Int)
        fun goToTicketCreatedActivity()
        fun showError(throwable: Throwable)
        fun categoriesMapper(data: CategoriesModel)
    }

    interface Presenter{
        fun getCategories()
        fun sendFeedbackForm(feedbackFormRequest: FeedbackFormRequest)
        fun sendAttachment(feedbackId: Int, fileData: MultipartBody.Part)
        fun commitData(feedbackId: Int)
    }
}