package com.tokopedia.core.product.model.goldmerchant;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 11/9/16. Tokopedia
 */

@Deprecated
public class ProductVideoData implements Parcelable{

    @SerializedName("data")
    @Expose
    private List<VideoData> data = new ArrayList<VideoData>();

    protected ProductVideoData(Parcel in) {
        data = in.createTypedArrayList(VideoData.CREATOR);
    }

    public static final Creator<ProductVideoData> CREATOR = new Creator<ProductVideoData>() {
        @Override
        public ProductVideoData createFromParcel(Parcel in) {
            return new ProductVideoData(in);
        }

        @Override
        public ProductVideoData[] newArray(int size) {
            return new ProductVideoData[size];
        }
    };

    /**
     *
     * @return
     * The data
     */
    public List<VideoData> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<VideoData> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
    }
}