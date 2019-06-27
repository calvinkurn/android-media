package com.tokopedia.core.product.model.goldmerchant;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class VideoData implements Parcelable{

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("video")
    @Expose
    private List<Video> video = new ArrayList<Video>();

    protected VideoData(Parcel in) {
        productId = in.readString();
        video = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Creator<VideoData> CREATOR = new Creator<VideoData>() {
        @Override
        public VideoData createFromParcel(Parcel in) {
            return new VideoData(in);
        }

        @Override
        public VideoData[] newArray(int size) {
            return new VideoData[size];
        }
    };

    /**
     *
     * @return
     *     The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     *
     * @param productId
     *     The product_id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     *
     * @return
     *     The video
     */
    public List<Video> getVideo() {
        return video;
    }

    /**
     *
     * @param video
     *     The video
     */
    public void setVideo(List<Video> video) {
        this.video = video;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeTypedList(video);
    }
}