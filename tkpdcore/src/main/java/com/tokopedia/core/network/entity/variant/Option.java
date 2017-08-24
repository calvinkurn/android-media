
package com.tokopedia.core.network.entity.variant;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Option implements Parcelable {

    @SerializedName("pvo_id")
    @Expose
    private Integer pvoId;
    @SerializedName("v_id")
    @Expose
    private Integer vId;
    @SerializedName("vu_id")
    @Expose
    private Integer vuId;
    @SerializedName("vuv_id")
    @Expose
    private Integer vuvId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("hex")
    @Expose
    private String hex;
    @SerializedName("picture")
    @Expose
    private String picture;

    private boolean enabled = true;

    public Integer getPvoId() {
        return pvoId;
    }

    public void setPvoId(Integer pvoId) {
        this.pvoId = pvoId;
    }

    public Integer getVId() {
        return vId;
    }

    public void setVId(Integer vId) {
        this.vId = vId;
    }

    public Integer getVuId() {
        return vuId;
    }

    public void setVuId(Integer vuId) {
        this.vuId = vuId;
    }

    public Integer getVuvId() {
        return vuvId;
    }

    public void setVuvId(Integer vuvId) {
        this.vuvId = vuvId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected Option(Parcel in) {
        pvoId = in.readByte() == 0x00 ? null : in.readInt();
        vId = in.readByte() == 0x00 ? null : in.readInt();
        vuId = in.readByte() == 0x00 ? null : in.readInt();
        vuvId = in.readByte() == 0x00 ? null : in.readInt();
        value = in.readString();
        status = in.readByte() == 0x00 ? null : in.readInt();
        hex = in.readString();
        picture = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (pvoId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(pvoId);
        }
        if (vId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(vId);
        }
        if (vuId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(vuId);
        }
        if (vuvId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(vuvId);
        }
        dest.writeString(value);
        if (status == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(status);
        }
        dest.writeString(hex);
        dest.writeString(picture);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Option> CREATOR = new Parcelable.Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };
}