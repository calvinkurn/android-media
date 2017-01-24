//package com.tokopedia.core.payment.model.responsecartstep2;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
///**
// * SystemBank
// * Created by Angga.Prasetiyo on 15/07/2016.
// */
//public class SystemBank implements Parcelable{
//    @SerializedName("sb_bank_cabang")
//    @Expose
//    private String sbBankCabang;
//    @SerializedName("sb_picture")
//    @Expose
//    private String sbPicture;
//    @SerializedName("sb_info")
//    @Expose
//    private String sbInfo;
//    @SerializedName("sb_bank_name")
//    @Expose
//    private String sbBankName;
//    @SerializedName("sb_active")
//    @Expose
//    private Integer sbActive;
//    @SerializedName("sb_account_no")
//    @Expose
//    private String sbAccountNo;
//    @SerializedName("sb_account_name")
//    @Expose
//    private String sbAccountName;
//
//    public String getSbBankCabang() {
//        return sbBankCabang;
//    }
//
//    public void setSbBankCabang(String sbBankCabang) {
//        this.sbBankCabang = sbBankCabang;
//    }
//
//    public String getSbPicture() {
//        return sbPicture;
//    }
//
//    public void setSbPicture(String sbPicture) {
//        this.sbPicture = sbPicture;
//    }
//
//    public String getSbInfo() {
//        return sbInfo;
//    }
//
//    public void setSbInfo(String sbInfo) {
//        this.sbInfo = sbInfo;
//    }
//
//    public String getSbBankName() {
//        return sbBankName;
//    }
//
//    public void setSbBankName(String sbBankName) {
//        this.sbBankName = sbBankName;
//    }
//
//    public Integer getSbActive() {
//        return sbActive;
//    }
//
//    public void setSbActive(Integer sbActive) {
//        this.sbActive = sbActive;
//    }
//
//    public String getSbAccountNo() {
//        return sbAccountNo;
//    }
//
//    public void setSbAccountNo(String sbAccountNo) {
//        this.sbAccountNo = sbAccountNo;
//    }
//
//    public String getSbAccountName() {
//        return sbAccountName;
//    }
//
//    public void setSbAccountName(String sbAccountName) {
//        this.sbAccountName = sbAccountName;
//    }
//
//    protected SystemBank(Parcel in) {
//        sbBankCabang = in.readString();
//        sbPicture = in.readString();
//        sbInfo = in.readString();
//        sbBankName = in.readString();
//        sbActive = in.readByte() == 0x00 ? null : in.readInt();
//        sbAccountNo = in.readString();
//        sbAccountName = in.readString();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(sbBankCabang);
//        dest.writeString(sbPicture);
//        dest.writeString(sbInfo);
//        dest.writeString(sbBankName);
//        if (sbActive == null) {
//            dest.writeByte((byte) (0x00));
//        } else {
//            dest.writeByte((byte) (0x01));
//            dest.writeInt(sbActive);
//        }
//        dest.writeString(sbAccountNo);
//        dest.writeString(sbAccountName);
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<SystemBank> CREATOR = new Parcelable.Creator<SystemBank>() {
//        @Override
//        public SystemBank createFromParcel(Parcel in) {
//            return new SystemBank(in);
//        }
//
//        @Override
//        public SystemBank[] newArray(int size) {
//            return new SystemBank[size];
//        }
//    };
//}
