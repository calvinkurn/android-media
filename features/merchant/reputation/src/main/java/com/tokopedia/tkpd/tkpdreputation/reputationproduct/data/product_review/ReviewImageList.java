
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

public class ReviewImageList implements Parcelable{

    @SerializedName("uri_thumbnail")
    @Expose
    String imageUrl;
    @SerializedName("uri_large")
    @Expose
    String imageUrlLarge;
    @SerializedName("description")
    @Expose
    String imageCaption;
    @SerializedName("attachment_id")
    @Expose
    String attachmentId;

    protected ReviewImageList(Parcel in) {
        imageUrl = in.readString();
        imageUrlLarge = in.readString();
        imageCaption = in.readString();
        attachmentId = in.readString();
    }

    public static final Creator<ReviewImageList> CREATOR = new Creator<ReviewImageList>() {
        @Override
        public ReviewImageList createFromParcel(Parcel in) {
            return new ReviewImageList(in);
        }

        @Override
        public ReviewImageList[] newArray(int size) {
            return new ReviewImageList[size];
        }
    };

    /**
     * 
     * @return
     *     The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 
     * @param imageUrlLarge
     *     The uri_large
     */
    public void setImageUrlLarge(String imageUrlLarge) {
        this.imageUrlLarge = imageUrlLarge;
    }

    /**
     *
     * @return
     *     The imageUrlLarge
     */
    public String getImageUrlLarge() {
        return imageUrlLarge;
    }

    /**
     *
     * @param imageUrl
     *     The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     *
     * @return
     *     The imageCaption
     */
    public String getImageCaption() {
        return MethodChecker.fromHtml(imageCaption).toString();
    }

    /**
     *
     * @param imageCaption
     *     The image_caption
     */
    public void setImageCaption(String imageCaption) {
        this.imageCaption = imageCaption;
    }

    /**
     *
     * @return
     *     The attachmentId
     */
    public String getAttachmentId() {
        return attachmentId;
    }

    /**
     *
     * @param attachmentId
     *     The attachment_id
     */
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(imageUrlLarge);
        dest.writeString(imageCaption);
        dest.writeString(attachmentId);
    }
}
