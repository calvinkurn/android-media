package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 8/24/17.
 */

public class ImageAttachmentUiModel implements Parcelable {

    private Long attachmentId;
    private String description;
    private String uriThumbnail;
    private String uriLarge;

    public ImageAttachmentUiModel(Long attachmentId, String description,
                                  String uriThumbnail, String uriLarge) {
        this.attachmentId = attachmentId;
        this.description = description;
        this.uriThumbnail = uriThumbnail;
        this.uriLarge = uriLarge;
    }

    protected ImageAttachmentUiModel(Parcel in) {
        attachmentId = in.readLong();
        description = in.readString();
        uriThumbnail = in.readString();
        uriLarge = in.readString();
    }

    public static final Creator<ImageAttachmentUiModel> CREATOR = new Creator<ImageAttachmentUiModel>() {
        @Override
        public ImageAttachmentUiModel createFromParcel(Parcel in) {
            return new ImageAttachmentUiModel(in);
        }

        @Override
        public ImageAttachmentUiModel[] newArray(int size) {
            return new ImageAttachmentUiModel[size];
        }
    };

    public Long getAttachmentId() {
        return attachmentId;
    }

    public String getDescription() {
        return description;
    }

    public String getUriThumbnail() {
        return uriThumbnail;
    }

    public String getUriLarge() {
        return uriLarge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(attachmentId);
        dest.writeString(description);
        dest.writeString(uriThumbnail);
        dest.writeString(uriLarge);
    }
}
