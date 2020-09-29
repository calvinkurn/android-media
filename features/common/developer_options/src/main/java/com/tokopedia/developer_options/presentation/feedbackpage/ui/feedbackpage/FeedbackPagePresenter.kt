package com.tokopedia.developer_options.presentation.feedbackpage.ui.feedbackpage

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.developer_options.api.ApiClient
import com.tokopedia.developer_options.api.FeedbackApiInterface
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.BaseImageFeedbackUiModel
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.CategoriesMapper
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.DefaultFeedbackUiModel
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.ImageFeedbackUiModel
import com.tokopedia.developer_options.presentation.feedbackpage.domain.request.FeedbackFormRequest
import com.tokopedia.developer_options.presentation.feedbackpage.domain.response.CategoriesResponse
import com.tokopedia.developer_options.presentation.feedbackpage.domain.response.FeedbackFormResponse
import com.tokopedia.developer_options.presentation.feedbackpage.domain.response.ImageResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class FeedbackPagePresenter(private val compositeSubscription: CompositeSubscription, val mapper: CategoriesMapper) : BaseDaggerPresenter<FeedbackPageContract.View>(), FeedbackPageContract.Presenter {

    private val feedbackApi: FeedbackApiInterface = ApiClient.getAPIService()
    private var imageData: MutableList<BaseImageFeedbackUiModel> = mutableListOf()
    private var originalImage: MutableList<String> = mutableListOf()

    override fun getCategories() {
        feedbackApi.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<CategoriesResponse>() {
                    override fun onNext(t: CategoriesResponse?) {
                        if (t != null) {
                            view.categoriesMapper(mapper.mapData(t))
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
                                    view.checkUriImage(t.data.feedbackID)
                                }
                            }

                            override fun onCompleted() {
                            }

                            override fun onError(e: Throwable?) {
                                view.hideLoadingDialog()
                                if (e != null) {
                                    view.showError(e)
                                }
                            }

                        })
        )

    }

    override fun sendAttachment(feedbackId: Int, filedata: MultipartBody.Part) {
        feedbackApi.uploadAttachment("/api/v1/feedback/$feedbackId/upload-attachment", filedata)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ImageResponse>() {
                    override fun onNext(t: ImageResponse?) {
                        view.hideLoadingDialog()
                        commitData(feedbackId)
                    }

                    override fun onCompleted() {
                        //no-op
                    }

                    override fun onError(e: Throwable?) {
                        view.hideLoadingDialog()
                        if (e != null) {
                            view.showError(e)
                        }
                    }

                })
    }


    override fun commitData(feedbackId: Int) {
        feedbackApi.commitData("api/v1/feedback/$feedbackId/commit")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ResponseBody>() {
                    override fun onNext(t: ResponseBody?) {
                        view.hideLoadingDialog()
                        view.goToTicketCreatedActivity()
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

    override fun getImageList(selectedImage: ArrayList<String>): MutableList<BaseImageFeedbackUiModel> {
        when (selectedImage.size) {
            5 -> {
                imageData = (selectedImage.take(4).map {
                    ImageFeedbackUiModel(it, shouldDisplayOverlay = true)
                }).asReversed().toMutableList()
            }
            else -> {
                imageData.addAll(selectedImage.map {
                    ImageFeedbackUiModel(it, shouldDisplayOverlay = false)
                }.asReversed())
            }
        }

        return imageData
    }

    override fun initImageData(): MutableList<BaseImageFeedbackUiModel> {
        imageData.clear()
        imageData.add(DefaultFeedbackUiModel())
        return imageData
    }

}