package com.tokopedia.flight_dbflow;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Rizky on 25/10/18.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightPassengerDB extends BaseModel implements Parcelable {

    @PrimaryKey
    @Column(name = "id")
    private String passengerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birthdate")
    private String birthdate;

    @Column(name = "title_id")
    private int titleId;

    @Column(name = "is_selected")
    private int isSelected;

    @Column(name = "nationality")
    private String passportNationality;

    @Column(name = "passport_country")
    private String passportCountry;

    @Column(name = "passport_expiry")
    private String passportExpiry;

    @Column(name = "passport_no")
    private String passportNo;

    public FlightPassengerDB(String passengerId, String firstName, String lastName, String birthdate,
                             int titleId, int isSelected, String passportNationality,
                             String passportCountry, String passportExpiry, String passportNo) {
        this.passengerId = passengerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.titleId = titleId;
        this.isSelected = isSelected;
        this.passportNationality = passportNationality;
        this.passportCountry = passportCountry;
        this.passportExpiry = passportExpiry;
        this.passportNo = passportNo;
    }

    public FlightPassengerDB() {}

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getPassportNationality() {
        return passportNationality;
    }

    public void setPassportNationality(String passportNationality) {
        this.passportNationality = passportNationality;
    }

    public String getPassportCountry() {
        return passportCountry;
    }

    public void setPassportCountry(String passportCountry) {
        this.passportCountry = passportCountry;
    }

    public String getPassportExpiry() {
        return passportExpiry;
    }

    public void setPassportExpiry(String passportExpiry) {
        this.passportExpiry = passportExpiry;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    protected FlightPassengerDB(Parcel in) {
        passengerId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        birthdate = in.readString();
        titleId = in.readInt();
        isSelected = in.readInt();
        passportNationality = in.readString();
        passportCountry = in.readString();
        passportExpiry = in.readString();
        passportNo = in.readString();
    }

    public static final Creator<FlightPassengerDB> CREATOR = new Creator<FlightPassengerDB>() {
        @Override
        public FlightPassengerDB createFromParcel(Parcel in) {
            return new FlightPassengerDB(in);
        }

        @Override
        public FlightPassengerDB[] newArray(int size) {
            return new FlightPassengerDB[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(passengerId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(birthdate);
        dest.writeInt(titleId);
        dest.writeInt(isSelected);
        dest.writeString(passportNationality);
        dest.writeString(passportCountry);
        dest.writeString(passportExpiry);
        dest.writeString(passportNo);
    }
}
