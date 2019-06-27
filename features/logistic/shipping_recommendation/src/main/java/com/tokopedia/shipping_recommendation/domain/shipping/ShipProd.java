package com.tokopedia.shipping_recommendation.domain.shipping;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ShipProd implements Parcelable {
    private int shipProdId;
    private String shipProdName;
    private String shipGroupName;
    private int shipGroupId;
    private int additionalFee;
    private int minimumWeight;

    public void setShipProdId(int shipProdId) {
        this.shipProdId = shipProdId;
    }

    public void setShipProdName(String shipProdName) {
        this.shipProdName = shipProdName;
    }

    public void setShipGroupName(String shipGroupName) {
        this.shipGroupName = shipGroupName;
    }

    public void setShipGroupId(int shipGroupId) {
        this.shipGroupId = shipGroupId;
    }

    public void setAdditionalFee(int additionalFee) {
        this.additionalFee = additionalFee;
    }

    public void setMinimumWeight(int minimumWeight) {
        this.minimumWeight = minimumWeight;
    }

    public int getShipProdId() {
        return shipProdId;
    }

    public String getShipProdName() {
        return shipProdName;
    }

    public String getShipGroupName() {
        return shipGroupName;
    }

    public int getShipGroupId() {
        return shipGroupId;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public int getMinimumWeight() {
        return minimumWeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.shipProdId);
        dest.writeString(this.shipProdName);
        dest.writeString(this.shipGroupName);
        dest.writeInt(this.shipGroupId);
        dest.writeInt(this.additionalFee);
        dest.writeInt(this.minimumWeight);
    }

    public ShipProd() {
    }

    protected ShipProd(Parcel in) {
        this.shipProdId = in.readInt();
        this.shipProdName = in.readString();
        this.shipGroupName = in.readString();
        this.shipGroupId = in.readInt();
        this.additionalFee = in.readInt();
        this.minimumWeight = in.readInt();
    }

    public static final Parcelable.Creator<ShipProd> CREATOR = new Parcelable.Creator<ShipProd>() {
        @Override
        public ShipProd createFromParcel(Parcel source) {
            return new ShipProd(source);
        }

        @Override
        public ShipProd[] newArray(int size) {
            return new ShipProd[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ShipProd)) return false;

        ShipProd shipProd = (ShipProd) o;

        return new EqualsBuilder()
                .append(getShipProdId(), shipProd.getShipProdId())
                .append(getShipGroupId(), shipProd.getShipGroupId())
                .append(getAdditionalFee(), shipProd.getAdditionalFee())
                .append(getMinimumWeight(), shipProd.getMinimumWeight())
                .append(getShipProdName(), shipProd.getShipProdName())
                .append(getShipGroupName(), shipProd.getShipGroupName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getShipProdId())
                .append(getShipProdName())
                .append(getShipGroupName())
                .append(getShipGroupId())
                .append(getAdditionalFee())
                .append(getMinimumWeight())
                .toHashCode();
    }
}
