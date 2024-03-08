package com.tokopedia.feedback_form.feedbackpage.ui.feedbackpage

import android.net.Uri
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.feedback_form.feedbackpage.domain.model.BaseImageFeedbackUiModel
import com.tokopedia.feedback_form.feedbackpage.domain.model.FeedbackModel
import com.tokopedia.feedback_form.feedbackpage.domain.request.FeedbackFormRequest
import com.tokopedia.screenshot_observer.ScreenshotData
import okhttp3.MultipartBody

interface FeedbackPageContract {

    interface View: CustomerView{
        fun showLoadingDialog()
        fun hideLoadingDialog()
        fun showLoadingFrame()
        fun hideLoadingFrame()
        fun setSubmitFlag()
        fun checkUriImage(feedbackId: Long, imageCount: Int)
        fun goToTicketCreatedActivity(issueUrl: String?)
        fun showError(throwable: Throwable)
        fun setFeedbackData(model: FeedbackModel)
        fun setSubmitButton()
    }

    interface Presenter{
        fun getFeedbackData()
        fun sendFeedbackForm(feedbackFormRequest: FeedbackFormRequest)
        fun sendAttachment(feedbackId: Long, fileData: MultipartBody.Part, totalImage: Int, imageCount: Int)
        fun commitData(feedbackId: Long)
        fun getImageList(selectedImage: ArrayList<String>): MutableList<BaseImageFeedbackUiModel>
        fun initImageData(): MutableList<BaseImageFeedbackUiModel>
        fun screenshotImageResult(data: ScreenshotData) : MutableList<BaseImageFeedbackUiModel>
        fun removeImage(image: BaseImageFeedbackUiModel) : MutableList<BaseImageFeedbackUiModel>
        fun getSelectedImageUrl(): ArrayList<String>
        fun drawOnPictureResult(uri: Uri?, oldPath: String): MutableList<BaseImageFeedbackUiModel>
        fun setSelectedPage(pageId: Int)
    }
}
