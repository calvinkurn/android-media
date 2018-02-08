package com.tokopedia.digital.widget.model.product;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class Attributes implements Parcelable {

    private String desc;
    private String detail;
    private String detailUrl;
    private String detailUrlText;
    private String info;
    private String price;
    private long pricePlain;
    private Promo promo;
    private int status;
    private int weight;

    public Attributes() {
    }


    protected Attributes(Parcel in) {
        desc = in.readString();
        detail = in.readString();
        detailUrl = in.readString();
        info = in.readString();
        price = in.readString();
        pricePlain = in.readLong();
        promo = in.readParcelable(Promo.class.getClassLoader());
        status = in.readInt();
        weight = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(desc);
        dest.writeString(detail);
        dest.writeString(detailUrl);
        dest.writeString(info);
        dest.writeString(price);
        dest.writeLong(pricePlain);
        dest.writeParcelable(promo, flags);
        dest.writeInt(status);
        dest.writeInt(weight);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getDetailUrlText() {
        return detailUrlText;
    }

    public void setDetailUrlText(String detailUrlText) {
        this.detailUrlText = detailUrlText;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getPricePlain() {
        return pricePlain;
    }

    public void setPricePlain(long pricePlain) {
        this.pricePlain = pricePlain;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
