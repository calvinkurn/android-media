package com.tokopedia.core.rescenter.create.model.passdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.rescenter.create.model.responsedata.CreateResCenterFormData;

/**
 * Created on 8/1/16.
 */
public class PassProductTrouble implements Parcelable {

    private CreateResCenterFormData.ProductData productData;
    private CreateResCenterFormData.TroubleData troubleData;
    private Integer inputQuantity;
    private String inputDescription;

    public CreateResCenterFormData.ProductData getProductData() {
        return productData;
    }

    public void setProductData(CreateResCenterFormData.ProductData productData) {
        this.productData = productData;
    }

    public CreateResCenterFormData.TroubleData getTroubleData() {
        return troubleData;
    }

    public void setTroubleData(CreateResCenterFormData.TroubleData troubleData) {
        this.troubleData = troubleData;
    }

    public Integer getInputQuantity() {
        return inputQuantity;
    }

    public void setInputQuantity(Integer inputQuantity) {
        this.inputQuantity = inputQuantity;
    }

    public String getInputDescription() {
        return inputDescription;
    }

    public void setInputDescription(String inputDescription) {
        this.inputDescription = inputDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.productData, flags);
        dest.writeParcelable(this.troubleData, flags);
        dest.writeValue(this.inputQuantity);
        dest.writeString(this.inputDescription);
    }

    public PassProductTrouble() {
    }

    protected PassProductTrouble(Parcel in) {
        this.productData = in.readParcelable(CreateResCenterFormData.ProductData.class.getClassLoader());
        this.troubleData = in.readParcelable(CreateResCenterFormData.TroubleData.class.getClassLoader());
        this.inputQuantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.inputDescription = in.readString();
    }

    public static final Parcelable.Creator<PassProductTrouble> CREATOR = new Parcelable.Creator<PassProductTrouble>() {
        @Override
        public PassProductTrouble createFromParcel(Parcel source) {
            return new PassProductTrouble(source);
        }

        @Override
        public PassProductTrouble[] newArray(int size) {
            return new PassProductTrouble[size];
        }
    };
}
