package com.tokopedia.affiliate.feature.createpost.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class CreatePostViewModel(
        var postId: String = "",
        var token: String = "",
        var authorType: String = "",
        var mainImageIndex: Int = 0,
        var maxImage: Int = 5,
        val productIdList: MutableList<String> = arrayListOf(),
        val adIdList: MutableList<String> = arrayListOf(),
        val fileImageList: MutableList<String> = arrayListOf(),
        val urlImageList: MutableList<String> = arrayListOf(),
        val relatedProducts: MutableList<RelatedProductItem> = arrayListOf()
) : Parcelable {

    val completeImageList: ArrayList<String>
        get() {
            val completeImageList = ArrayList<String>()
            completeImageList.addAll(fileImageList)
            completeImageList.addAll(urlImageList)
            return completeImageList
        }

    constructor(source: Parcel) : this(
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readInt(),
            source.readInt(),
            source.createStringArrayList() ?: arrayListOf(),
            source.createStringArrayList() ?: arrayListOf(),
            source.createStringArrayList() ?: arrayListOf(),
            source.createStringArrayList() ?: arrayListOf(),
            source.createTypedArrayList(RelatedProductItem.CREATOR) ?: arrayListOf()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(postId)
        writeString(token)
        writeString(authorType)
        writeInt(mainImageIndex)
        writeInt(maxImage)
        writeStringList(productIdList)
        writeStringList(adIdList)
        writeStringList(fileImageList)
        writeStringList(urlImageList)
        writeTypedList(relatedProducts)
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
