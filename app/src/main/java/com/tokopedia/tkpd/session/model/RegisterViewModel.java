package com.tokopedia.tkpd.session.model;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.util.Calendar;

/**
 * @author m.normansyah
 * @since 12-11-2015
 */
@Parcel
public class RegisterViewModel {
    int mDateYear;
    int mDateMonth;
    int mDateDay;
    long maxDate;
    long minDate;
    String mEmail;
    String mPassword;
    String mConfirmPassword;
    String mName;
    String mPhone;
    int mGender;
    String dateText;
    boolean isAgreedTermCondition;
    boolean isRegisterLoading;
    boolean isAutoVerify;

    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    // VIEW TYPE
    public static final int IS_REGISTER_LOADING_TYPE = 0;
    public static final int IS_REGISTER_NEXT_LOADING_TYPE = 1;

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmConfirmPassword() {
        return mConfirmPassword;
    }

    public void setmConfirmPassword(String mConfirmPassword) {
        this.mConfirmPassword = mConfirmPassword;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public int getmGender() {
        return mGender;
    }

    public void setmGender(int mGender) {
        this.mGender = mGender;
    }

    public long getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }

    public long getMinDate() {
        return minDate;
    }

    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    public int getmDateYear() {
        return mDateYear;
    }

    public void setmDateYear(int mDateYear) {
        this.mDateYear = mDateYear;
    }

    public int getmDateMonth() {
        return mDateMonth;
    }

    public void setmDateMonth(int mDateMonth) {
        this.mDateMonth = mDateMonth;
    }

    public int getmDateDay() {
        return mDateDay;
    }

    public void setmDateDay(int mDateDay) {
        this.mDateDay = mDateDay;
    }

    public boolean isAgreedTermCondition() {
        return isAgreedTermCondition;
    }

    public boolean isAutoVerify() {
        return isAutoVerify;
    }

    public void setIsAutoVerify(boolean isAutoVerify) {
        this.isAutoVerify = isAutoVerify;
    }

    public void setIsAgreedTermCondition(boolean isAgreedTermCondition) {
        this.isAgreedTermCondition = isAgreedTermCondition;
    }

    public boolean isRegisterLoading() {
        return isRegisterLoading;
    }

    public void setIsRegisterLoading(boolean isRegisterLoading) {
        this.isRegisterLoading = isRegisterLoading;
    }

    @Override
    public String toString() {
        return "RegisterViewModel{" +
                "mDateYear=" + mDateYear +
                ", mDateMonth=" + mDateMonth +
                ", mDateDay=" + mDateDay +
                ", maxDate=" + maxDate +
                ", minDate=" + minDate +
                ", mEmail='" + mEmail + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mConfirmPassword='" + mConfirmPassword + '\'' +
                ", mName='" + mName + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mGender=" + mGender +
                ", dateText='" + dateText + '\'' +
                ", isAgreedTermCondition=" + isAgreedTermCondition +
                ", isRegisterLoading=" + isRegisterLoading +
                ", isAutoVerify=" + isAutoVerify +
                '}';
    }
}
