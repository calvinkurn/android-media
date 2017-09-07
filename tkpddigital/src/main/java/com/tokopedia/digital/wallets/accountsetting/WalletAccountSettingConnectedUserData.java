package com.tokopedia.digital.wallets.accountsetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 8/29/17.
 */

public class WalletAccountSettingConnectedUserData implements Parcelable {

    private String email;
    private String userName;
    private String labelButtonAction;
    private String registeredDate;
    private String iconUrl;
    private int resIconId;

    private WalletAccountSettingConnectedUserData(Builder builder) {
        setEmail(builder.email);
        setUserName(builder.userName);
        setLabelButtonAction(builder.labelButtonAction);
        setRegisteredDate(builder.registeredDate);
        setIconUrl(builder.iconUrl);
        setResIconId(builder.resIconId);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getResIconId() {
        return resIconId;
    }

    public void setResIconId(int resIconId) {
        this.resIconId = resIconId;
    }

    public String getLabelButtonAction() {
        return labelButtonAction;
    }

    public void setLabelButtonAction(String labelButtonAction) {
        this.labelButtonAction = labelButtonAction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.userName);
        dest.writeString(this.registeredDate);
        dest.writeString(this.iconUrl);
        dest.writeInt(this.resIconId);
    }

    public WalletAccountSettingConnectedUserData() {
    }

    protected WalletAccountSettingConnectedUserData(Parcel in) {
        this.email = in.readString();
        this.userName = in.readString();
        this.registeredDate = in.readString();
        this.iconUrl = in.readString();
        this.resIconId = in.readInt();
    }

    public static final Creator<WalletAccountSettingConnectedUserData> CREATOR =
            new Creator<WalletAccountSettingConnectedUserData>() {
                @Override
                public WalletAccountSettingConnectedUserData createFromParcel(Parcel source) {
                    return new WalletAccountSettingConnectedUserData(source);
                }

                @Override
                public WalletAccountSettingConnectedUserData[] newArray(int size) {
                    return new WalletAccountSettingConnectedUserData[size];
                }
            };


    public static final class Builder {
        private String email;
        private String userName;
        private String labelButtonAction;
        private String registeredDate;
        private String iconUrl;
        private int resIconId;

        public Builder() {
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder userName(String val) {
            userName = val;
            return this;
        }

        public Builder labelButtonAction(String val) {
            labelButtonAction = val;
            return this;
        }

        public Builder registeredDate(String val) {
            registeredDate = val;
            return this;
        }

        public Builder iconUrl(String val) {
            iconUrl = val;
            return this;
        }

        public Builder resIconId(int val) {
            resIconId = val;
            return this;
        }

        public WalletAccountSettingConnectedUserData build() {
            return new WalletAccountSettingConnectedUserData(this);
        }
    }


}
