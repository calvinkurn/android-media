package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.BaseImageReviewViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.DefaultImageReviewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ImageReviewViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevGetForm
import com.tokopedia.tkpd.tkpdreputation.createreputation.usecase.GetProductReputationForm
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.*
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewUseCase
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewValidateUseCase
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

class CreateReviewViewModel @Inject constructor(@Named("Main")
                                                val dispatcher: CoroutineDispatcher,
                                                private val getProductReputationForm: GetProductReputationForm,
                                                private val sendReviewWithoutImage: SendReviewValidateUseCase,
                                                private val sendReviewWithImage: SendReviewUseCase) : BaseViewModel(dispatcher) {

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private var imageData: MutableList<BaseImageReviewViewModel> = mutableListOf()

    private var reputationDataForm = MutableLiveData<Result<ProductRevGetForm>>()
    val getReputationDataForm = reputationDataForm

    private var submitReviewResponse = MutableLiveData<LoadingDataState<SendReviewValidateDomain>>()
    val getSubmitReviewResponse: LiveData<LoadingDataState<SendReviewValidateDomain>> = submitReviewResponse

    fun submitReview(reviewId: String, reputationId: String, productId: String, shopId: String, reviewDesc: String,
                     ratingCount: Float, listOfImages: List<String>, isAnonymous: Boolean) {

        if (listOfImages.isEmpty()) {
            sendReviewWithoutImage(reviewId, reputationId, productId, shopId, reviewDesc, ratingCount, isAnonymous)
        } else {
            sendReviewWithImage(reviewId, reputationId, productId, shopId, reviewDesc, ratingCount, isAnonymous, listOfImages)
        }
    }

    fun getImageList(selectedImage: ArrayList<String>): MutableList<BaseImageReviewViewModel> {
        when (selectedImage.size) {
            5 -> {
                imageData = (selectedImage.take(4).map {
                    ImageReviewViewModel(it, shouldDisplayOverlay = true)
                }).asReversed().toMutableList()
            }
            4 -> {
                imageData.addAll(selectedImage.take(3).map {
                    ImageReviewViewModel(it, shouldDisplayOverlay = true)
                }.asReversed())
            }
            else -> {
                imageData.addAll(selectedImage.map {
                    ImageReviewViewModel(it, shouldDisplayOverlay = false)
                }.asReversed())
            }
        }

        return imageData
    }

    fun initImageData(): MutableList<BaseImageReviewViewModel> {
        imageData.clear()
        imageData.add(DefaultImageReviewModel())
        return imageData
    }

    fun getProductReputation(productId: Int, reptutationId: Int) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(reptutationId, productId))
            }
            reputationDataForm.value = CoroutineSuccess(data)

        }) {
            reputationDataForm.value = CoroutineFail(it)

        }
    }

    private fun sendReviewWithoutImage(reviewId: String, reputationId: String, productId: String, shopId: String,
                                       reviewDesc: String, ratingCount: Float, isAnonymous: Boolean) {
        submitReviewResponse.value = LoadingView
        sendReviewWithoutImage.execute(SendReviewValidateUseCase.getParam(reviewId, productId,
                reputationId, shopId, ratingCount.toString(), reviewDesc, isAnonymous)
                , object : Subscriber<SendReviewValidateDomain>() {
            override fun onNext(data: SendReviewValidateDomain) {
                submitReviewResponse.value = Success(data)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                submitReviewResponse.value = Fail(e)
            }

        })
    }

    private fun sendReviewWithImage(reviewId: String, reputationId: String, productId: String, shopId: String,
                                    reviewDesc: String, ratingCount: Float, isAnonymous: Boolean, listOfImages: List<String>) {
        submitReviewResponse.value = LoadingView
        sendReviewWithImage.execute(SendReviewUseCase.getParam(reviewId, productId, reputationId, shopId, ratingCount.toString(),
                reviewDesc, mapImageToObjectUpload(listOfImages), listOf(), isAnonymous), object : Subscriber<SendReviewDomain>() {
            override fun onNext(data: SendReviewDomain) {
                if (data.isSuccess) {
                    submitReviewResponse.value = Success(SendReviewValidateDomain("", 0, if (data.isSuccess) 1 else 0))
                } else {
                    onError(Throwable())
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                submitReviewResponse.value = Fail(e)
            }

        })
    }

    private fun mapImageToObjectUpload(listOfImages: List<String>): ArrayList<ImageUpload> {
        val imageUpload: ArrayList<ImageUpload> = arrayListOf()
        listOfImages.forEachIndexed { index, s ->
            val imageUploadPojo = ImageUpload()
            imageUploadPojo.fileLoc = s
            imageUploadPojo.description = ""
            imageUploadPojo.position = index
            imageUploadPojo.imageId = "${SendReviewUseCase.IMAGE}${UUID.randomUUID()}"
            imageUpload.add(imageUploadPojo)
        }

        return imageUpload
    }
}