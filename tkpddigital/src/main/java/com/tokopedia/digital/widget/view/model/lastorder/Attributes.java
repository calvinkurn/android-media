package com.tokopedia.digital.widget.view.model.lastorder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class Attributes implements Parcelable {

    private String clientNumber;
    private int productId;
    private int operatorId;
    private int categoryId;

    public Attributes() {
    }

    protected Attributes(Parcel in) {
        clientNumber = in.readString();
        productId = in.readInt();
        operatorId = in.readInt();
        categoryId = in.readInt();
    }

    public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
        @Override
        public Attributes createFromParcel(Parcel in) {
            return new Attributes(in);
        }

        @Override
        public Attributes[] newArray(int size) {
            return new Attributes[size];
        }
    };

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clientNumber);
        dest.writeInt(productId);
        dest.writeInt(operatorId);
        dest.writeInt(categoryId);
    }
}
