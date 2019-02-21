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
        var fileImageList: ArrayList<String> = ArrayList(),
        var urlImageList: ArrayList<String> = ArrayList())
    : Parcelable {

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
    }

    private constructor(`in`: Parcel) : this() {
        this.productId = `in`.readString() ?: ""
        this.adId = `in`.readString() ?: ""
        this.postId = `in`.readString() ?: ""
        this.token = `in`.readString() ?: ""
        this.mainImageIndex = `in`.readInt()
        this.maxImage = `in`.readInt()
        this.isEdit = `in`.readByte().toInt() != 0
        this.fileImageList = `in`.createStringArrayList() ?: arrayListOf()
        this.urlImageList = `in`.createStringArrayList() ?: arrayListOf()
    }

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
