package com.tokopedia.core.product.model.etalase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 13/11/2015.
 */

@Deprecated
public class EtalaseData implements Parcelable {
    private static final String TAG = EtalaseData.class.getSimpleName();

    @SerializedName("is_allow")
    @Expose
    private Integer isAllow;
    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("list")
    @Expose
    private List<Etalase> etalaseList = new ArrayList<>();

    public Integer getIsAllow() {
        return isAllow;
    }

    public void setIsAllow(Integer isAllow) {
        this.isAllow = isAllow;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<Etalase> getEtalaseList() {
        return etalaseList;
    }

    public void setEtalaseList(List<Etalase> etalaseList) {
        this.etalaseList = etalaseList;
    }

    protected EtalaseData(Parcel in) {
        isAllow = in.readByte() == 0x00 ? null : in.readInt();
        paging = (Paging) in.readValue(Paging.class.getClassLoader());
        if (in.readByte() == 0x01) {
            etalaseList = new ArrayList<Etalase>();
            in.readList(etalaseList, Etalase.class.getClassLoader());
        } else {
            etalaseList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (isAllow == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(isAllow);
        }
        dest.writeValue(paging);
        if (etalaseList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(etalaseList);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<EtalaseData> CREATOR = new Creator<EtalaseData>() {
        @Override
        public EtalaseData createFromParcel(Parcel in) {
            return new EtalaseData(in);
        }

        @Override
        public EtalaseData[] newArray(int size) {
            return new EtalaseData[size];
        }
    };
}
