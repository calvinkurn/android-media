package com.tokopedia.shop.review.shop.view.adapter

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.shop.review.product.view.adapter.ReviewProductModelContent
import com.tokopedia.shop.review.product.view.adapter.ReviewProductTypeFactoryAdapter
import com.tokopedia.shop.review.shop.view.uimodel.ImageAttachmentUiModel

/**
 * Created by zulfikarrahman on 1/19/18.
 */
class ReviewShopModelContent : ReviewProductModelContent {
    var productName: String? = null
    var productImageUrl: String? = null
    var productPageUrl: String? = null

    constructor() {}

    override fun type(typeFactory: ReviewProductTypeFactoryAdapter?): Int {
        return if (typeFactory is ReviewShopTypeFactoryAdapter) {
            typeFactory.type(this)
        } else 0
    }

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
        dest.writeString(productName)
        dest.writeString(productImageUrl)
        dest.writeString(productPageUrl)
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
        productName = `in`.readString()
        productImageUrl = `in`.readString()
        productPageUrl = `in`.readString()
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ReviewShopModelContent?> = object : Parcelable.Creator<ReviewShopModelContent?> {
            override fun createFromParcel(source: Parcel): ReviewShopModelContent? {
                return ReviewShopModelContent(source)
            }

            override fun newArray(size: Int): Array<ReviewShopModelContent?> {
                return arrayOfNulls(size)
            }
        }
    }
}