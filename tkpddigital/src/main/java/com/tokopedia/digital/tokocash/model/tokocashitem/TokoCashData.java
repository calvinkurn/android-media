
package com.tokopedia.digital.tokocash.model.tokocashitem;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TokoCashData implements Parcelable{

    @SerializedName("action")
    private Action mAction;
    @SerializedName("balance")
    private String mBalance;
    @SerializedName("redirect_url")
    private String mRedirectUrl;
    @SerializedName("text")
    private String mText;
    @SerializedName("wallet_id")
    private Long mWalletId;
    @SerializedName("link")
    private Integer link;


    public Action getAction() {
        return mAction;
    }

    public void setAction(Action action) {
        mAction = action;
    }

    public String getBalance() {
        return mBalance;
    }

    public void setBalance(String balance) {
        mBalance = balance;
    }

    public String getRedirectUrl() {
        return mRedirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        mRedirectUrl = redirectUrl;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Long getWalletId() {
        return mWalletId;
    }

    public void setWalletId(Long wallet_id) {
        mWalletId = wallet_id;
    }

    public Integer getLink() {
        return link;
    }

    public void setLink(Integer link) {
        this.link = link;
    }

    protected TokoCashData(Parcel in) {
        mAction = (Action) in.readValue(Action.class.getClassLoader());
        mBalance = in.readString();
        mRedirectUrl = in.readString();
        mText = in.readString();
        mWalletId = in.readByte() == 0x00 ? null : in.readLong();
        link = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mAction);
        dest.writeString(mBalance);
        dest.writeString(mRedirectUrl);
        dest.writeString(mText);
        if (mWalletId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(mWalletId);
        }
        if (link == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(link);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TokoCashData> CREATOR = new Parcelable.Creator<TokoCashData>() {
        @Override
        public TokoCashData createFromParcel(Parcel in) {
            return new TokoCashData(in);
        }

        @Override
        public TokoCashData[] newArray(int size) {
            return new TokoCashData[size];
        }
    };
}
