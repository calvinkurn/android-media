package com.tokopedia.core.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevenfredian on 5/26/16.
 */
public class InfoModel implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("loginid")
    @Expose
    private String loginid;
    @SerializedName("from")
    @Expose
    private int from;
    @SerializedName("remember")
    @Expose
    private int remember;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("bday")
    @Expose
    private String bday;
    @SerializedName("phone_verified")
    @Expose
    private boolean phoneVerified;
    @SerializedName("roles")
    @Expose
    private List<Integer> roles = new ArrayList<>();
    @SerializedName("created_password")
    @Expose
    private boolean createdPassword;
    @SerializedName("create_password_list")
    @Expose
    private List<String> createPasswordList = new ArrayList<>();
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("w_refresh_token")
    @Expose
    private String walletRefreshToken;

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The loginid
     */
    public String getLoginid() {
        return loginid;
    }

    /**
     * @param loginid The loginid
     */
    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    /**
     * @return The from
     */
    public int getFrom() {
        return from;
    }

    /**
     * @param from The from
     */
    public void setFrom(int from) {
        this.from = from;
    }

    /**
     * @return The remember
     */
    public int getRemember() {
        return remember;
    }

    /**
     * @param remember The remember
     */
    public void setRemember(int remember) {
        this.remember = remember;
    }

    /**
     * @return The userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return The bday
     */
    public String getBday() {
        return bday;
    }

    /**
     * @param bday The bday
     */
    public void setBday(String bday) {
        this.bday = bday;
    }

    /**
     * @return The phoneVerified
     */
    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    /**
     * @param phoneVerified The phone_verified
     */
    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    /**
     * @return The roles
     */
    public List<Integer> getRoles() {
        return roles;
    }

    /**
     * @param roles The roles
     */
    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    /**
     * @return The createdPassword
     */
    public boolean isCreatedPassword() {
        return createdPassword;
    }

    /**
     * @param createdPassword The created_password
     */
    public void setCreatedPassword(boolean createdPassword) {
        this.createdPassword = createdPassword;
    }

    /**
     * @return The createPasswordList
     */
    public List<String> getCreatePasswordList() {
        return createPasswordList;
    }

    /**
     * @param createPasswordList The create_password_list
     */
    public void setCreatePasswordList(List<String> createPasswordList) {
        this.createPasswordList = createPasswordList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWalletRefreshToken() {
        return walletRefreshToken;
    }

    public void setWalletRefreshToken(String walletRefreshToken) {
        this.walletRefreshToken = walletRefreshToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.loginid);
        dest.writeInt(this.from);
        dest.writeInt(this.remember);
        dest.writeInt(this.userId);
        dest.writeString(this.bday);
        dest.writeByte(this.phoneVerified ? (byte) 1 : (byte) 0);
        dest.writeList(this.roles);
        dest.writeByte(this.createdPassword ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.createPasswordList);
        dest.writeString(this.phone);
        dest.writeString(this.walletRefreshToken);
    }

    public InfoModel() {
    }

    protected InfoModel(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.loginid = in.readString();
        this.from = in.readInt();
        this.remember = in.readInt();
        this.userId = in.readInt();
        this.bday = in.readString();
        this.phoneVerified = in.readByte() != 0;
        this.roles = new ArrayList<Integer>();
        in.readList(this.roles, Integer.class.getClassLoader());
        this.createdPassword = in.readByte() != 0;
        this.createPasswordList = in.createStringArrayList();
        this.phone = in.readString();
        this.walletRefreshToken = in.readString();
    }

    public static final Creator<InfoModel> CREATOR = new Creator<InfoModel>() {
        @Override
        public InfoModel createFromParcel(Parcel source) {
            return new InfoModel(source);
        }

        @Override
        public InfoModel[] newArray(int size) {
            return new InfoModel[size];
        }
    };
}
