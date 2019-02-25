package com.tokopedia.affiliate.feature.createpost.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import java.io.File
import java.util.*

data class CreatePostViewModel(
        var productId: String = "",
        var adId: String = "",
        var postId: String = "",
        var token: String = "",
        var mainImageIndex: Int = 0,
        var maxImage: Int = 5,
        var isEdit: Boolean = false,
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

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.productId)
        dest.writeString(this.adId)
        dest.writeString(this.postId)
        dest.writeString(this.token)
        dest.writeInt(this.mainImageIndex)
        dest.writeInt(this.maxImage)
        dest.writeByte(if (this.isEdit) 1.toByte() else 0.toByte())
        dest.writeStringList(this.fileImageList)
        dest.writeStringList(this.urlImageList)
        dest.writeTypedList(this.relatedProducts)
    }

    private constructor(`in`: Parcel) : this(
            `in`.readString() ?: "",
            `in`.readString() ?: "",
            `in`.readString() ?: "",
            `in`.readString() ?: "",
            `in`.readInt(),
            `in`.readInt(),
            `in`.readByte().toInt() != 0,
            `in`.createStringArrayList() ?: arrayListOf(),
            `in`.createStringArrayList() ?: arrayListOf(),
            `in`.createTypedArrayList(RelatedProductItem.CREATOR) ?: arrayListOf()
    )

    companion object {
        private const val FILE_PREFIX = "file:"

        fun urlIsFile(input: String): Boolean {
            if (input.startsWith(FILE_PREFIX)) return true
            return try {
                File(input).exists()
            } catch (e: Exception) {
                false
            }

        }

        @JvmField
        val CREATOR: Parcelable.Creator<CreatePostViewModel> =
                object : Parcelable.Creator<CreatePostViewModel> {
                    override fun createFromParcel(source: Parcel): CreatePostViewModel {
                        return CreatePostViewModel(source)
                    }

                    override fun newArray(size: Int): Array<CreatePostViewModel?> {
                        return arrayOfNulls(size)
                    }
                }
    }
}
