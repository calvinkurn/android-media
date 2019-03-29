
package com.tokopedia.core.network.entity.home;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Brands implements Parcelable
{

    @SerializedName("data")
    @Expose
    private List<Brand> data = new ArrayList<Brand>();
    @SerializedName("process-time")
    @Expose
    private String processTime;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Parcelable.Creator<Brands> CREATOR = new Creator<Brands>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Brands createFromParcel(Parcel in) {
            Brands instance = new Brands();
            in.readList(instance.data, (Brand.class.getClassLoader()));
            instance.processTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Brands[] newArray(int size) {
            return (new Brands[size]);
        }

    }
    ;

    /**
     * 
     * @return
     *     The data
     */
    public List<Brand> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<Brand> data) {
        this.data = data;
    }

    /**
     * 
     * @return
     *     The processTime
     */
    public String getProcessTime() {
        return processTime;
    }

    /**
     * 
     * @param processTime
     *     The process-time
     */
    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(data);
        dest.writeValue(processTime);
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}
