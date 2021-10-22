package com.tokopedia.gallery.mapper

import com.tokopedia.gallery.networkmodel.ProductrevGetReviewImage
import com.tokopedia.gallery.networkmodel.ReviewDetail
import com.tokopedia.gallery.networkmodel.ReviewGalleryImage
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import java.util.*

object GalleryMapper {
    fun convertNetworkResponseToImageReviewItemList(gqlResponse: ProductrevGetReviewImage): List<ImageReviewItem> {
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