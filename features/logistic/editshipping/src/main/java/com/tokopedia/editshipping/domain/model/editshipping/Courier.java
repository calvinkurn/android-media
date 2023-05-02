package com.tokopedia.editshipping.domain.model.editshipping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.editshipping.util.EditShippingConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kris on 2/24/2016.
 */
public class Courier implements Parcelable{
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("logo")
    @Expose
    public String logo;
    @SerializedName("weight")
    @Expose
    public String weight;
    @SerializedName("weight_policy")
    @Expose
    public String weightPolicy = "";
    @SerializedName("available")
    @Expose
    public String available;
    @SerializedName("by_zip_code")
    @Expose
    public String byZipCode;
    @SerializedName("url_additional_option")
    @Expose
    public String urlAdditionalOption;
    @SerializedName("services")
    @Expose
    public List<Service> services = new ArrayList<>();

    public Map<String, String> additionalOptionDatas = new HashMap<>();

    public Courier() {
        additionalOptionDatas = new HashMap<>();
    }

    protected Courier(Parcel in) {
        name = in.readString();
        id = in.readString();
        logo = in.readString();
        weight = in.readString();
        weightPolicy = in.readString();
        available = in.readString();
        byZipCode = in.readString();
        urlAdditionalOption = in.readString();
        services = in.createTypedArrayList(Service.CREATOR);
    }

    public static final Creator<Courier> CREATOR = new Creator<Courier>() {
        @Override
        public Courier createFromParcel(Parcel in) {
            return new Courier(in);
        }

        @Override
        public Courier[] newArray(int size) {
            return new Courier[size];
        }
    };

    public void setAdditionalOptionDatas (Map<String, String> queryMap){
        additionalOptionDatas.clear();
        additionalOptionDatas.putAll(queryMap);
    }

    public int getCourierWeight(){
        return Integer.parseInt(weight.replaceAll("[^0-9]", ""));
    }

    public boolean isWhitelabelService() {
        return EditShippingConstant.INSTANCE.getWHITELABEL_SHIPPER_ID().contains(Long.parseLong(id));
    }

    public Map<String, String> getAdditionalOptionDatas(){
        return additionalOptionDatas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeString(logo);
        parcel.writeString(weight);
        parcel.writeString(weightPolicy);
        parcel.writeString(available);
        parcel.writeString(byZipCode);
        parcel.writeString(urlAdditionalOption);
        parcel.writeTypedList(services);
    }
}
