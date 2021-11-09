package com.tokopedia.kol.feature.comment.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory

class KolCommentHeaderNewModel : KolCommentNewModel, Visitable<KolCommentTypeFactory?>, Parcelable {
    var isCanLoadMore: Boolean
    var isLoading: Boolean
    var tagsLink: String?

    constructor(avatarUrl: String?, name: String?, review: String?, time: String?,
                userId: String?, userUrl: String?, tagsLink: String?, userBadges: String?, isShop: Boolean) : super("0", userId, userUrl, avatarUrl, name, review, time, true, false, userBadges, isShop) {
        isCanLoadMore = false
        isLoading = false
        this.tagsLink = tagsLink
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeByte(if (isCanLoadMore) 1.toByte() else 0.toByte())
        dest.writeByte(if (isLoading) 1.toByte() else 0.toByte())
        dest.writeString(tagsLink)
    }

    protected constructor(`in`: Parcel) : super(`in`) {
        isCanLoadMore = `in`.readByte().toInt() != 0
        isLoading = `in`.readByte().toInt() != 0
        tagsLink = `in`.readString()
    }

    companion object {
    }

    override fun type(typeFactory: KolCommentTypeFactory?): Int {
        return typeFactory?.type(this)?:0
    }
}