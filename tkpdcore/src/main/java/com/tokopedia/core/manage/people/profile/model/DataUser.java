package com.tokopedia.core.manage.people.profile.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

public class DataUser implements Parcelable {

    @SerializedName("hobby")
    @Expose
    private String hobby;
    @SerializedName("birth_day")
    @Expose
    private String birthDay;
    @SerializedName("user_messenger")
    @Expose
    private String userMessenger;
    @SerializedName("reputation")
    @Expose
    private Reputation reputation;
    @SerializedName("birth_month")
    @Expose
    private String birthMonth;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("birth_year")
    @Expose
    private String birthYear;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_image_100")
    @Expose
    private String userImage100;
    @SerializedName("user_generated_name")
    @Expose
    private boolean isUserGeneratedName;

    /**
     *
     * @return
     *     The hobby
     */
    public String getHobby() {
        return MethodChecker.fromHtml(hobby).toString();
    }

    /**
     *
     * @param hobby
     *     The hobby
     */
    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    /**
     *
     * @return
     *     The birthDay
     */
    public String getBirthDay() {
        return birthDay;
    }

    /**
     *
     * @param birthDay
     *     The birth_day
     */
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    /**
     *
     * @return
     *     The userMessenger
     */
    public String getUserMessenger() {
        return MethodChecker.fromHtml(userMessenger).toString();
    }

    /**
     *
     * @param userMessenger
     *     The user_messenger
     */
    public void setUserMessenger(String userMessenger) {
        this.userMessenger = userMessenger;
    }

    /**
     *
     * @return
     *     The reputation
     */
    public Reputation getReputation() {
        return reputation;
    }

    /**
     *
     * @param reputation
     *     The reputation
     */
    public void setReputation(Reputation reputation) {
        this.reputation = reputation;
    }

    /**
     *
     * @return
     *     The birthMonth
     */
    public String getBirthMonth() {
        return birthMonth;
    }

    /**
     *
     * @param birthMonth
     *     The birth_month
     */
    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    /**
     *
     * @return
     *     The fullName
     */
    public String getFullName() {
        return MethodChecker.fromHtml(fullName).toString();
    }

    /**
     *
     * @param fullName
     *     The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     *
     * @return
     *     The userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     *
     * @param userEmail
     *     The user_email
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     *
     * @return
     *     The userPhone
     */
    public String getUserPhone() {
        return userPhone;
    }

    /**
     *
     * @param userPhone
     *     The user_phone
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     *
     * @return
     *     The birthYear
     */
    public String getBirthYear() {
        return birthYear;
    }

    /**
     *
     * @param birthYear
     *     The birth_year
     */
    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    /**
     *
     * @return
     *     The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     *     The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     *     The userImage
     */
    public String getUserImage() {
        return userImage;
    }

    /**
     *
     * @param userImage
     *     The user_image
     */
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserImage100() {
        return userImage100;
    }

    public void setUserImage100(String userImage100) {
        this.userImage100 = userImage100;
    }

    public boolean isUserGeneratedName() {
        return isUserGeneratedName;
    }

    public void setUserGeneratedName(boolean userGeneratedName) {
        isUserGeneratedName = userGeneratedName;
    }

    public DataUser() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hobby);
        dest.writeString(this.birthDay);
        dest.writeString(this.userMessenger);
        dest.writeParcelable(this.reputation, flags);
        dest.writeString(this.birthMonth);
        dest.writeString(this.fullName);
        dest.writeString(this.userEmail);
        dest.writeString(this.userPhone);
        dest.writeString(this.birthYear);
        dest.writeString(this.gender);
        dest.writeString(this.userImage);
        dest.writeString(this.userImage100);
        dest.writeByte(this.isUserGeneratedName ? (byte) 1 : (byte) 0);
    }

    protected DataUser(Parcel in) {
        this.hobby = in.readString();
        this.birthDay = in.readString();
        this.userMessenger = in.readString();
        this.reputation = in.readParcelable(Reputation.class.getClassLoader());
        this.birthMonth = in.readString();
        this.fullName = in.readString();
        this.userEmail = in.readString();
        this.userPhone = in.readString();
        this.birthYear = in.readString();
        this.gender = in.readString();
        this.userImage = in.readString();
        this.userImage100 = in.readString();
        this.isUserGeneratedName = in.readByte() != 0;
    }

    public static final Creator<DataUser> CREATOR = new Creator<DataUser>() {
        @Override
        public DataUser createFromParcel(Parcel source) {
            return new DataUser(source);
        }

        @Override
        public DataUser[] newArray(int size) {
            return new DataUser[size];
        }
    };
}