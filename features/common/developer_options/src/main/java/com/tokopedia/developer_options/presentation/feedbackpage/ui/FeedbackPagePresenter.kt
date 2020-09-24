package com.tokopedia.developer_options.presentation.feedbackpage.ui

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.developer_options.api.ApiClient
import com.tokopedia.developer_options.api.FeedbackApi
import com.tokopedia.developer_options.api.request.FeedbackFormRequest
import com.tokopedia.developer_options.api.response.CategoriesResponse
import com.tokopedia.developer_options.api.response.FeedbackFormResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class FeedbackPagePresenter(private val compositeSubscription: CompositeSubscription) : BaseDaggerPresenter<FeedbackPageContract.View>(), FeedbackPageContract.Presenter {

    private val feedbackApi: FeedbackApi = ApiClient.getAPIService()

    override fun getCategories() {
        feedbackApi.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<CategoriesResponse>() {
                    override fun onNext(t: CategoriesResponse?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                                view.checkUriImage(t?.data?.feedbackID)
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

    override fun sendAttachment(feedbackId: Int?, filedata: MultipartBody.Part) {
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "file")
        feedbackApi.uploadAttachment("/api/v1/feedback/$feedbackId/upload-attachment", requestFile, filedata)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<String>() {
                    override fun onNext(t: String?) {
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


    override fun commitData(feedbackId: Int?) {
        feedbackApi.commitData("api/v1/feedback/$feedbackId/commit")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<String>() {
                    override fun onNext(t: String?) {
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

}