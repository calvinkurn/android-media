package com.tokopedia.kol.feature.comment.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory

open class KolCommentNewModel : Visitable<KolCommentTypeFactory?>, Parcelable {
    var id: String?
    var avatarUrl: String?
    var name: String?
    var review: String?
    var time: String?
    var userUrl: String?
    var isOfficial: Boolean
    var userId: String?
        protected set
    protected var canDeleteComment: Boolean
    var userBadges: String?
        protected set
    var isShop: Boolean

    constructor(id: String?, userId: String?, userUrl: String?, avatarUrl: String?, name: String?,
                review: String?, time: String?, isOfficial: Boolean,
                canDeleteComment: Boolean, userBadges: String?, isShop: Boolean) {
        this.id = id
        this.userId = userId
        this.userUrl = userUrl
        this.avatarUrl = avatarUrl
        this.name = name
        this.review = review
        this.time = time
        this.isOfficial = isOfficial
        this.canDeleteComment = canDeleteComment
        this.userBadges = userBadges
        this.isShop = isShop
    }

    fun canDeleteComment(): Boolean {
        return canDeleteComment
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(avatarUrl)
        dest.writeString(name)
        dest.writeString(review)
        dest.writeString(time)
        dest.writeString(userUrl)
        dest.writeByte(if (isOfficial) 1.toByte() else 0.toByte())
        dest.writeString(userId)
        dest.writeByte(if (canDeleteComment) 1.toByte() else 0.toByte())
        dest.writeString(userBadges)
        dest.writeByte(if (isShop) 1.toByte() else 0.toByte())
    }

    protected constructor(`in`: Parcel) {
        id = `in`.readString()
        avatarUrl = `in`.readString()
        name = `in`.readString()
        review = `in`.readString()
        time = `in`.readString()
        userUrl = `in`.readString()
        isOfficial = `in`.readByte().toInt() != 0
        userId = `in`.readString()
        canDeleteComment = `in`.readByte().toInt() != 0
        userBadges = `in`.readString()
        isShop = `in`.readByte().toInt() != 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<KolCommentNewModel?> =
            object : Parcelable.Creator<KolCommentNewModel?> {
                override fun createFromParcel(source: Parcel): KolCommentNewModel? {
                    return KolCommentNewModel(source)
                }

                override fun newArray(size: Int): Array<KolCommentNewModel?> {
                    return arrayOfNulls(size)
                }
            }
    }

    override fun type(typeFactory: KolCommentTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0

    }
}