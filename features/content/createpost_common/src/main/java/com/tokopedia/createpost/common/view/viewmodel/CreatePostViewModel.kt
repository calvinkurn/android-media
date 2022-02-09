package com.tokopedia.createpost.common.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class CreatePostViewModel(
    var postId: String = "",
    var token: String = "",
    var authorType: String = "",
    var caption: String = "",
    var maxImage: Int = 5,
    var maxProduct: Int = 1,
    var allowImage: Boolean = false,
    var allowVideo: Boolean = false,
    val productIdList: MutableList<String> = arrayListOf(),
    val adIdList: MutableList<String> = arrayListOf(),
    val fileImageList: MutableList<MediaModel> = arrayListOf(),
    val urlImageList: MutableList<MediaModel> = arrayListOf(),
    val relatedProducts: MutableList<RelatedProductItem> = arrayListOf(),
    var defaultPlaceholder: String = "",
    var currentCorouselIndex: Int = 0,
    var mediaWidth: Int = 0,
    var mediaHeight: Int = 0,
    var shopName: String = ""
) : Parcelable {
    val isEditState: Boolean
        get() = postId.isNotBlank()

    val completeImageList: ArrayList<MediaModel>
        get() {
            val completeImageList = ArrayList<MediaModel>()
            completeImageList.clear()
            completeImageList.addAll(fileImageList)
            completeImageList.addAll(urlImageList)
            return completeImageList
        }

    constructor(source: Parcel) : this(
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readInt(),
            source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            source.createStringArrayList() ?: arrayListOf(),
            source.createStringArrayList() ?: arrayListOf(),
            source.createTypedArrayList(MediaModel.CREATOR) ?: arrayListOf(),
            source.createTypedArrayList(MediaModel.CREATOR) ?: arrayListOf(),
            source.createTypedArrayList(RelatedProductItem.CREATOR) ?: arrayListOf(),
            source.readString() ?: "",
            )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(postId)
        writeString(token)
        writeString(authorType)
        writeString(caption)
        writeInt(maxImage)
        writeInt(maxProduct)
        writeInt((if (allowImage) 1 else 0))
        writeInt((if (allowVideo) 1 else 0))
        writeStringList(productIdList)
        writeStringList(adIdList)
        writeTypedList(fileImageList)
        writeTypedList(urlImageList)
        writeTypedList(relatedProducts)
        writeString(defaultPlaceholder)
    }

    companion object {
        @JvmField
        val TAG: String = CreatePostViewModel::class.java.simpleName

        @JvmField
        val CREATOR: Parcelable.Creator<CreatePostViewModel> = object : Parcelable.Creator<CreatePostViewModel> {
            override fun createFromParcel(source: Parcel): CreatePostViewModel = CreatePostViewModel(source)
            override fun newArray(size: Int): Array<CreatePostViewModel?> = arrayOfNulls(size)
        }
    }
}
