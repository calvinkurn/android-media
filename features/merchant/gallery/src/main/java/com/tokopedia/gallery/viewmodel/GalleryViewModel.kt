package com.tokopedia.gallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gallery.domain.GetReviewImagesUseCase
import com.tokopedia.gallery.networkmodel.ProductrevGetReviewImage
import com.tokopedia.gallery.networkmodel.ReviewDetail
import com.tokopedia.gallery.networkmodel.ReviewGalleryImage
import com.tokopedia.gallery.uimodel.GalleryData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    private val getReviewImagesUseCase: GetReviewImagesUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(coroutineDispatchers.io) {

    private var productId = ""
    private val currentPage = MutableLiveData<Int>()

    private val _reviewImages = MediatorLiveData<Result<GalleryData>>()
    val reviewImages: LiveData<Result<GalleryData>>
        get() = _reviewImages

    init {
        _reviewImages.addSource(currentPage) {
            getReviewImages(it)
        }
    }

    fun setPage(productId: String, page: Int) {
        this.productId = productId
        currentPage.value = page
    }

    private fun getReviewImages(page: Int) {
        launchCatchError(block = {
            getReviewImagesUseCase.setParams(productId, page)
            val data = getReviewImagesUseCase.executeOnBackground()
            _reviewImages.postValue(
                Success(
                    GalleryData(
                        convertNetworkResponseToImageReviewItemList(data.productrevGetReviewImage),
                        data.productrevGetReviewImage.hasNext
                    )
                )
            )
        }) {
            _reviewImages.postValue(Fail(it))
        }
    }

    private fun convertNetworkResponseToImageReviewItemList(gqlResponse: ProductrevGetReviewImage): List<ImageReviewItem> {
        val reviewMap = HashMap<String, ReviewDetail>()
        val imageMap = HashMap<String, ReviewGalleryImage>()

        gqlResponse.detail.reviewGalleryImages.map {
            imageMap[it.attachmentId] = it
        }

        gqlResponse.detail.reviewDetail.map {
            reviewMap[it.feedbackId] = it
        }

        return gqlResponse.reviewImages.map {
            val image = imageMap[it.imageId]
            val review = reviewMap[it.feedbackId]
            ImageReviewItem(
                it.feedbackId,
                review?.createTimestamp ?: "",
                review?.user?.fullName ?: "",
                image?.thumbnailURL ?: "",
                image?.fullsizeURL ?: "",
                review?.rating ?: 0
            )
        }
    }
}