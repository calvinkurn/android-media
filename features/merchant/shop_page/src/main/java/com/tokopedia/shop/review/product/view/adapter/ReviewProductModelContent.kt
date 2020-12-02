package com.tokopedia.shop.review.product.view.adapter

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.shop.review.shop.view.uimodel.ImageAttachmentUiModel

/**
 * Created by zulfikarrahman on 1/16/18.
 */
open class ReviewProductModelContent : ReviewProductModel, Parcelable {
    var isReviewIsAnonymous = false
    var reviewerName: String? = null
    var reviewerId: String? = null
    var reviewStar = 0f
    var reviewMessage: String? = null
    var reviewTime: String? = null
    var reviewAttachment: List<ImageAttachmentUiModel?>? = null
    var sellerName: String? = null
    var shopId: String? = null
    var reviewId: String? = null
    var isReviewHasReplied = false
    var responseMessage: String? = null
    var responseCreateTime: String? = null
    var isSellerRepliedOwner = false
    var isReviewCanReported = false
    var reputationId: String? = null
    var productId: String? = null
    var isLikeStatus = false
    var totalLike = 0
    var isLogin = false
    var isHelpful = false
    var isReplyOpened = false
    override fun type(typeFactory: ReviewProductTypeFactoryAdapter?): Int {
        return typeFactory?.type(this) ?: -1
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (isReviewIsAnonymous) 1.toByte() else 0.toByte())
        dest.writeString(reviewerName)
        dest.writeString(reviewerId)
        dest.writeFloat(reviewStar)
        dest.writeString(reviewMessage)
        dest.writeString(reviewTime)
        dest.writeTypedList(reviewAttachment)
        dest.writeString(sellerName)
        dest.writeString(shopId)
        dest.writeString(reviewId)
        dest.writeByte(if (isReviewHasReplied) 1.toByte() else 0.toByte())
        dest.writeString(responseMessage)
        dest.writeString(responseCreateTime)
        dest.writeByte(if (isSellerRepliedOwner) 1.toByte() else 0.toByte())
        dest.writeByte(if (isReviewCanReported) 1.toByte() else 0.toByte())
        dest.writeString(reputationId)
        dest.writeString(productId)
        dest.writeByte(if (isLikeStatus) 1.toByte() else 0.toByte())
        dest.writeInt(totalLike)
        dest.writeByte(if (isLogin) 1.toByte() else 0.toByte())
        dest.writeByte(if (isHelpful) 1.toByte() else 0.toByte())
    }

    protected constructor(`in`: Parcel) {
        isReviewIsAnonymous = `in`.readByte().toInt() != 0
        reviewerName = `in`.readString()
        reviewerId = `in`.readString()
        reviewStar = `in`.readFloat()
        reviewMessage = `in`.readString()
        reviewTime = `in`.readString()
        reviewAttachment = `in`.createTypedArrayList(ImageAttachmentUiModel.Companion.CREATOR)
        sellerName = `in`.readString()
        shopId = `in`.readString()
        reviewId = `in`.readString()
        isReviewHasReplied = `in`.readByte().toInt() != 0
        responseMessage = `in`.readString()
        responseCreateTime = `in`.readString()
        isSellerRepliedOwner = `in`.readByte().toInt() != 0
        isReviewCanReported = `in`.readByte().toInt() != 0
        reputationId = `in`.readString()
        productId = `in`.readString()
        isLikeStatus = `in`.readByte().toInt() != 0
        totalLike = `in`.readInt()
        isLogin = `in`.readByte().toInt() != 0
        isHelpful = `in`.readByte().toInt() != 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ReviewProductModelContent?> = object : Parcelable.Creator<ReviewProductModelContent?> {
            override fun createFromParcel(source: Parcel): ReviewProductModelContent? {
                return ReviewProductModelContent(source)
            }

            override fun newArray(size: Int): Array<ReviewProductModelContent?> {
                return arrayOfNulls(size)
            }
        }
    }
}