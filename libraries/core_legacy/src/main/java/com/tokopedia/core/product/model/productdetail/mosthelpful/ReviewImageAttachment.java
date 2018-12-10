
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewImageAttachment implements Parcelable {

    @SerializedName("attachment_id")
    @Expose
    private int attachmentId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("uri_thumbnail")
    @Expose
    private String uriThumbnail;
    @SerializedName("uri_large")
    @Expose
    private String uriLarge;

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUriThumbnail() {
        return uriThumbnail;
    }

    public void setUriThumbnail(String uriThumbnail) {
        this.uriThumbnail = uriThumbnail;
    }

    public String getUriLarge() {
        return uriLarge;
    }

    public void setUriLarge(String uriLarge) {
        this.uriLarge = uriLarge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.attachmentId);
        dest.writeString(this.description);
        dest.writeString(this.uriThumbnail);
        dest.writeString(this.uriLarge);
    }

    public ReviewImageAttachment() {
    }

    protected ReviewImageAttachment(Parcel in) {
        this.attachmentId = in.readInt();
        this.description = in.readString();
        this.uriThumbnail = in.readString();
        this.uriLarge = in.readString();
    }

    public static final Creator<ReviewImageAttachment> CREATOR = new Creator<ReviewImageAttachment>() {
        @Override
        public ReviewImageAttachment createFromParcel(Parcel source) {
            return new ReviewImageAttachment(source);
        }

        @Override
        public ReviewImageAttachment[] newArray(int size) {
            return new ReviewImageAttachment[size];
        }
    };
}
