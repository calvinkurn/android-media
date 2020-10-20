package com.tokopedia.developer_options.presentation.feedbackpage.ui.feedbackpage

import android.net.Uri
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.BaseImageFeedbackUiModel
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.FeedbackModel
import com.tokopedia.developer_options.presentation.feedbackpage.domain.request.FeedbackFormRequest
import com.tokopedia.screenshot_observer.ScreenshotData
import okhttp3.MultipartBody

interface FeedbackPageContract {

    interface View: CustomerView{
        fun showLoadingDialog()
        fun hideLoadingDialog()
        fun setSubmitFlag()
        fun checkUriImage(feedbackId: Int, imageCount: Int)
        fun goToTicketCreatedActivity()
        fun showError(throwable: Throwable)
        fun setFeedbackData(model: FeedbackModel)
    }

    interface Presenter{
        fun getFeedbackData()
        fun sendFeedbackForm(feedbackFormRequest: FeedbackFormRequest)
        fun sendAttachment(feedbackId: Int, fileData: MultipartBody.Part, totalImage: Int, imageCount: Int)
        fun commitData(feedbackId: Int)
        fun getImageList(selectedImage: ArrayList<String>): MutableList<BaseImageFeedbackUiModel>
        fun initImageData(): MutableList<BaseImageFeedbackUiModel>
        fun screenshotImageResult(data: ScreenshotData) : MutableList<BaseImageFeedbackUiModel>
        fun removeImage(image: BaseImageFeedbackUiModel) : MutableList<BaseImageFeedbackUiModel>
        fun getSelectedImageUrl(): ArrayList<String>
        fun drawOnPictureResult(uri: Uri?, oldPath: String): MutableList<BaseImageFeedbackUiModel>
    }
}