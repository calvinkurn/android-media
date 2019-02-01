
package com.tokopedia.core.network.entity.categories;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.network.entity.discovery.BannerOfficialStoreModel;

@Deprecated
public class Data implements Parcelable {

    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    protected Data(Parcel in) {
        result = (Result) in.readValue(Result.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public static final class CategoryNavigationContainer implements ObjContainer<Data> {

        Data model;

        public CategoryNavigationContainer(Data model) {
            this.model = model;
        }

        @Override
        public Data body() {
            return model;
        }
    }
}