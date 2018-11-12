package com.tokopedia.common.travel.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 14/08/18.
 */
public class TravelPassengerEntity {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("title")
    @Expose
    private int title;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("birthDate")
    @Expose
    private String birthDate;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("passportNo")
    @Expose
    private String passportNo;
    @SerializedName("passportCountry")
    @Expose
    private String passportCountry;
    @SerializedName("passportExpiry")
    @Expose
    private String passportExpiry;
    @SerializedName("idNumber")
    @Expose
    private String idNumber;
    @SerializedName("isBuyer")
    @Expose
    private int isBuyer;
    @SerializedName("paxType")
    @Expose
    private int paxType;
    @SerializedName("travelId")
    @Expose
    private int travelId;

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getTitle() {
        return title;
    }

    public String getName() {
        return name;
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

    public String getIdNumber() {
        return idNumber;
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
}
