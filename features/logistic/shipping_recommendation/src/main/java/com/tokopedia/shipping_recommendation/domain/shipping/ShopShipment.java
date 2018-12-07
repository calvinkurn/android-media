package com.tokopedia.shipping_recommendation.domain.shipping;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ShopShipment implements Parcelable {

    private int shipId;
    private String shipName;
    private String shipCode;
    private String shipLogo;
    private List<ShipProd> shipProds = new ArrayList<>();
    private boolean isDropshipEnabled;

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getShipLogo() {
        return shipLogo;
    }

    public void setShipLogo(String shipLogo) {
        this.shipLogo = shipLogo;
    }

    public List<ShipProd> getShipProds() {
        return shipProds;
    }

    public void setShipProds(List<ShipProd> shipProds) {
        this.shipProds = shipProds;
    }

    public boolean isDropshipEnabled() {
        return isDropshipEnabled;
    }

    public void setDropshipEnabled(boolean dropshipEnabled) {
        isDropshipEnabled = dropshipEnabled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.shipId);
        dest.writeString(this.shipName);
        dest.writeString(this.shipCode);
        dest.writeString(this.shipLogo);
        dest.writeList(this.shipProds);
        dest.writeByte(this.isDropshipEnabled ? (byte) 1 : (byte) 0);
    }

    public ShopShipment() {
    }

    protected ShopShipment(Parcel in) {
        this.shipId = in.readInt();
        this.shipName = in.readString();
        this.shipCode = in.readString();
        this.shipLogo = in.readString();
        this.shipProds = new ArrayList<ShipProd>();
        in.readList(this.shipProds, ShipProd.class.getClassLoader());
        this.isDropshipEnabled = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ShopShipment> CREATOR = new Parcelable.Creator<ShopShipment>() {
        @Override
        public ShopShipment createFromParcel(Parcel source) {
            return new ShopShipment(source);
        }

        @Override
        public ShopShipment[] newArray(int size) {
            return new ShopShipment[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ShopShipment)) return false;

        ShopShipment that = (ShopShipment) o;

        return new EqualsBuilder()
                .append(getShipId(), that.getShipId())
                .append(isDropshipEnabled(), that.isDropshipEnabled())
                .append(getShipName(), that.getShipName())
                .append(getShipCode(), that.getShipCode())
                .append(getShipLogo(), that.getShipLogo())
                .append(getShipProds(), that.getShipProds())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getShipId())
                .append(getShipName())
                .append(getShipCode())
                .append(getShipLogo())
                .append(getShipProds())
                .append(isDropshipEnabled())
                .toHashCode();
    }
}
