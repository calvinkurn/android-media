
package com.tokopedia.core.network.entity.variant;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VariantOption implements Parcelable {

    @SerializedName("pv_id")
    @Expose
    private Integer pvId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("v_id")
    @Expose
    private Integer vId;
    @SerializedName("vu_id")
    @Expose
    private Integer vuId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("option")
    @Expose
    private List<Option> option = null;

    public Integer getPvId() {
        return pvId;
    }

    public void setPvId(Integer pvId) {
        this.pvId = pvId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<Option> getOption() {
        return option;
    }

    public void setOption(List<Option> option) {
        this.option = option;
    }


    protected VariantOption(Parcel in) {
        pvId = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        identifier = in.readString();
        unitName = in.readString();
        vId = in.readByte() == 0x00 ? null : in.readInt();
        vuId = in.readByte() == 0x00 ? null : in.readInt();
        status = in.readByte() == 0x00 ? null : in.readInt();
        position = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            option = new ArrayList<Option>();
            in.readList(option, Option.class.getClassLoader());
        } else {
            option = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (pvId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(pvId);
        }
        dest.writeString(name);
        dest.writeString(identifier);
        dest.writeString(unitName);
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
        if (status == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(status);
        }
        if (position == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(position);
        }
        if (option == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(option);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VariantOption> CREATOR = new Parcelable.Creator<VariantOption>() {
        @Override
        public VariantOption createFromParcel(Parcel in) {
            return new VariantOption(in);
        }

        @Override
        public VariantOption[] newArray(int size) {
            return new VariantOption[size];
        }
    };
}