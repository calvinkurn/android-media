package com.tokopedia.feedcomponent.view.viewmodel.data

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by yfsx on 23/03/19.
 */
data class FooterViewModel(
        var like: LikeViewModel = LikeViewModel(),
        var comment: CommentViewModel = CommentViewModel(),
        var buttonCta: ButtonCtaViewModel = ButtonCtaViewModel(),
        var share: ShareViewModel = ShareViewModel()
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<LikeViewModel>(LikeViewModel::class.java.classLoader),
            source.readParcelable<CommentViewModel>(CommentViewModel::class.java.classLoader),
            source.readParcelable<ButtonCtaViewModel>(ButtonCtaViewModel::class.java.classLoader),
            source.readParcelable<ShareViewModel>(ShareViewModel::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(like, 0)
        writeParcelable(comment, 0)
        writeParcelable(buttonCta, 0)
        writeParcelable(share, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FooterViewModel> = object : Parcelable.Creator<FooterViewModel> {
            override fun createFromParcel(source: Parcel): FooterViewModel = FooterViewModel(source)
            override fun newArray(size: Int): Array<FooterViewModel?> = arrayOfNulls(size)
        }
    }
}