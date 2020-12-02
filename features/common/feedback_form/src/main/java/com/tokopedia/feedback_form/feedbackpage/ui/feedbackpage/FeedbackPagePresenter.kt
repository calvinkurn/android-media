package com.tokopedia.developer_options.presentation.feedbackpage.ui.feedbackpage

import android.net.Uri
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.ScreenshotResultMapper
import com.tokopedia.feedback_form.feedbackpage.domain.mapper.FeedbackDataMapper
import com.tokopedia.feedback_form.feedbackpage.domain.model.BaseImageFeedbackUiModel
import com.tokopedia.feedback_form.feedbackpage.domain.model.DefaultFeedbackUiModel
import com.tokopedia.feedback_form.feedbackpage.domain.model.FeedbackModel
import com.tokopedia.feedback_form.feedbackpage.domain.model.ImageFeedbackUiModel
import com.tokopedia.feedback_form.feedbackpage.domain.request.FeedbackFormRequest
import com.tokopedia.feedback_form.feedbackpage.domain.response.CommitResponse
import com.tokopedia.feedback_form.feedbackpage.domain.response.FeedbackDataResponse
import com.tokopedia.feedback_form.feedbackpage.domain.response.FeedbackFormResponse
import com.tokopedia.feedback_form.feedbackpage.domain.response.ImageResponse
import com.tokopedia.feedback_form.feedbackpage.domain.service.ApiClient
import com.tokopedia.feedback_form.feedbackpage.domain.service.FeedbackApiInterface
import com.tokopedia.feedback_form.feedbackpage.ui.feedbackpage.FeedbackPageContract
import com.tokopedia.screenshot_observer.ScreenshotData
import okhttp3.MultipartBody
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription


class FeedbackPagePresenter(private val compositeSubscription: CompositeSubscription, val mapper: FeedbackDataMapper, val imageMapper: ScreenshotResultMapper) : BaseDaggerPresenter<FeedbackPageContract.View>(), FeedbackPageContract.Presenter {

    private val feedbackApi: FeedbackApiInterface = ApiClient.getAPIService()
    private var imageData: MutableList<BaseImageFeedbackUiModel> = mutableListOf()
    private var pageModel: FeedbackModel? = null
    private var selectedId = -1

    override fun getFeedbackData() {
        feedbackApi.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<FeedbackDataResponse>() {
                    override fun onNext(t: FeedbackDataResponse?) {
                        if (t != null) {
                            view.setFeedbackData(mapper.mapData(t))
                            pageModel = mapper.mapData(t)
                        }
                    }

                    override fun onCompleted() {
                        //no-op
                    }

                    override fun onError(e: Throwable?) {
                        if (e != null) {
                            view.showError(e)
                        }
                    }

                })
    }

    override fun sendFeedbackForm(feedbackFormRequest: FeedbackFormRequest) {
        view.showLoadingDialog()
        compositeSubscription.add(
                feedbackApi.createFeedbackForm(feedbackFormRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Subscriber<FeedbackFormResponse>() {
                            override fun onNext(t: FeedbackFormResponse?) {
                                view.setSubmitFlag()
                                if (t != null) {
                                    view.checkUriImage(t.data.feedbackID, 0)
                                }
                            }

                            override fun onCompleted() {
                            }

                            override fun onError(e: Throwable?) {
                                view.hideLoadingDialog()
                                if (e != null) {
                                    view.showError(e)
                                    view.setSubmitButton()
                                }
                            }

                        })
        )

    }

    override fun sendAttachment(feedbackId: Int, filedata: MultipartBody.Part, totalImage: Int, imageCount: Int) {
        feedbackApi.uploadAttachment("/api/v1/feedback/$feedbackId/upload-attachment", filedata)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ImageResponse>() {
                    override fun onNext(t: ImageResponse?) {
                        if (totalImage == imageCount) {
                            view.hideLoadingDialog()
                            commitData(feedbackId)
                        }
                    }

                    override fun onCompleted() {
                        //no-op
                    }

                    override fun onError(e: Throwable?) {
                        view.hideLoadingDialog()
                        if (e != null) {
                            view.showError(e)
                            view.setSubmitButton()
                        }
                    }

                })
    }


    override fun commitData(feedbackId: Int) {
        feedbackApi.commitData("api/v1/feedback/$feedbackId/commit")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<CommitResponse>() {
                    override fun onNext(t: CommitResponse?) {
                        view.hideLoadingDialog()
                        /*send jiraURL here*/
                        view.goToTicketCreatedActivity(t?.data?.jiraIssueURL)
                    }

                    override fun onCompleted() {
                        //no-op
                    }

                    override fun onError(e: Throwable?) {
                        view.hideLoadingDialog()
                        if (e != null) {
                            view.showError(e)
                            view.setSubmitButton()
                        }
                    }

                })
    }

    override fun getImageList(selectedImage: ArrayList<String>): MutableList<BaseImageFeedbackUiModel> {
        when (selectedImage.size) {
            5 -> {
                imageData = (selectedImage.map {
                    ImageFeedbackUiModel(it)
                }).toMutableList()
            }
            else -> {
                imageData.addAll(selectedImage.map {
                    ImageFeedbackUiModel(it)
                })
            }
        }

        return imageData
    }

    /*set selected item radio button*/
    override fun setSelectedPage(pageId: Int) {
        val pageModel = pageModel
        if (pageModel != null) {
            selectedId = pageId
            logicSelection(pageModel)
        }
    }

    private fun logicSelection(pageModel: FeedbackModel) {
        val pageList = pageModel.labels
        for (item in pageList) {
            item.isSelected = item.id == selectedId
        }
        pageModel.labels = pageList
        view.setFeedbackData(pageModel)
    }


    override fun initImageData(): MutableList<BaseImageFeedbackUiModel> {
        imageData.clear()
        imageData.add(DefaultFeedbackUiModel())
        return imageData
    }

    override fun screenshotImageResult(data: ScreenshotData): MutableList<BaseImageFeedbackUiModel> {
        imageData.add(imageMapper.mapData(data))
        return imageData
    }

    override fun drawOnPictureResult(uri: Uri?, oldPath: String): MutableList<BaseImageFeedbackUiModel> {
        val oldDopResult = ImageFeedbackUiModel().apply {
            imageUrl = oldPath
        }
        val dopResultUri = ImageFeedbackUiModel().apply {
            imageUrl = uri.toString()
        }
        imageData.remove(oldDopResult)
        imageData.add(dopResultUri)
        return imageData
    }

    override fun removeImage(image: BaseImageFeedbackUiModel): MutableList<BaseImageFeedbackUiModel> {
        imageData.remove(image)

        if(imageData.size <  5 && !imageData.contains(DefaultFeedbackUiModel())) {
            imageData.add(DefaultFeedbackUiModel())
        }
        return imageData
    }

    override fun getSelectedImageUrl(): ArrayList<String> {
        val result = arrayListOf<String>()
        imageData.forEach {
            var imageUrl = ""
            if((it as? ImageFeedbackUiModel)?.imageUrl?.isNotBlank() == true) {
               imageUrl =  (it as? ImageFeedbackUiModel)?.imageUrl.toString()
            }

            if (imageUrl.isNotEmpty()) {
                result.add(imageUrl)
            }
        }
        return result
    }

}