
package com.tokopedia.core.network.entity.intermediary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.model.ObjContainer;

@Deprecated
public class CategoryHadesModel implements Parcelable {

    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    protected CategoryHadesModel(Parcel in) {
        serverProcessTime = in.readString();
        data = (Data) in.readValue(Data.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serverProcessTime);
        dest.writeValue(data);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CategoryHadesModel> CREATOR = new Parcelable.Creator<CategoryHadesModel>() {
        @Override
        public CategoryHadesModel createFromParcel(Parcel in) {
            return new CategoryHadesModel(in);
        }

        @Override
        public CategoryHadesModel[] newArray(int size) {
            return new CategoryHadesModel[size];
        }
    };

    public static final class CategoriesHadesContainer implements ObjContainer<CategoryHadesModel> {
        CategoryHadesModel categoriesHadesModel;

        public CategoriesHadesContainer(CategoryHadesModel categoriesHadesModel) {
            this.categoriesHadesModel = categoriesHadesModel;
        }

        @Override
        public CategoryHadesModel body() {
            return categoriesHadesModel;
        }
    }
}