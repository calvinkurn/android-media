package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by nisie on 8/24/17.
 */
class ImageAttachmentUiModel : Parcelable {
    var attachmentId: Int
        private set
    var description: String?
        private set
    var uriThumbnail: String?
        private set
    var uriLarge: String?
        private set

    constructor(
        attachmentId: Int, description: String?,
        uriThumbnail: String?, uriLarge: String?
    ) {
        this.attachmentId = attachmentId
        this.description = description
        this.uriThumbnail = uriThumbnail
        this.uriLarge = uriLarge
    }

    protected constructor(`in`: Parcel) {
        attachmentId = `in`.readInt()
        description = `in`.readString()
        uriThumbnail = `in`.readString()
        uriLarge = `in`.readString()
    }

    public override fun describeContents(): Int {
        return 0
    }

    public override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(attachmentId)
        dest.writeString(description)
        dest.writeString(uriThumbnail)
        dest.writeString(uriLarge)
    }

    companion object {
        val CREATOR: Parcelable.Creator<ImageAttachmentUiModel> =
            object : Parcelable.Creator<ImageAttachmentUiModel?> {
                public override fun createFromParcel(`in`: Parcel): ImageAttachmentUiModel? {
                    return ImageAttachmentUiModel(`in`)
                }

                public override fun newArray(size: Int): Array<ImageAttachmentUiModel?> {
                    return arrayOfNulls(size)
                }
            }
    }
}