package com.tokopedia.common.travel.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by nabillasabbaha on 17/12/18.
 */
@Entity
public class TravelPassengerTable {
    @PrimaryKey
    @NonNull
    private String idPassenger;
    private String id;
    private int userId;
    private int title;
    private String idNumber;
    @ColumnInfo(name = "name") private String namePassenger;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String nationality;
    private String passportNo;
    private String passportCountry;
    private String passportExpiry;
    private int isBuyer;
    private int paxType;
    private int travelId;
    private int selected;

    @NonNull
    public String getIdPassenger() {
        return idPassenger;
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getTitle() {
        return title;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getNamePassenger() {
        return namePassenger;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public String getPassportCountry() {
        return passportCountry;
    }

    public String getPassportExpiry() {
        return passportExpiry;
    }

    public int getIsBuyer() {
        return isBuyer;
    }

    public int getPaxType() {
        return paxType;
    }

    public int getTravelId() {
        return travelId;
    }

    public int getSelected() {
        return selected;
    }

    public void setIdPassenger(@NonNull String idPassenger) {
        this.idPassenger = idPassenger;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public void setNamePassenger(String namePassenger) {
        this.namePassenger = namePassenger;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public void setPassportCountry(String passportCountry) {
        this.passportCountry = passportCountry;
    }

    public void setPassportExpiry(String passportExpiry) {
        this.passportExpiry = passportExpiry;
    }

    public void setIsBuyer(int isBuyer) {
        this.isBuyer = isBuyer;
    }

    public void setPaxType(int paxType) {
        this.paxType = paxType;
    }

    public void setTravelId(int travelId) {
        this.travelId = travelId;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
