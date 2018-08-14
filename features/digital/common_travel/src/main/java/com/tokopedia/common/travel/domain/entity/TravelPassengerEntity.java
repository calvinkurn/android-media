package com.tokopedia.common.travel.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 14/08/18.
 */
public class TravelPassengerEntity {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("idNumber")
    @Expose
    private String idNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("salutationId")
    @Expose
    private int salutationId;
    @SerializedName("birthDate")
    @Expose
    private String birthDate;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("isBuyer")
    @Expose
    private int isBuyer;
    @SerializedName("paxType")
    @Expose
    private int paxType;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getName() {
        return name;
    }

    public int getSalutationId() {
        return salutationId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getIsBuyer() {
        return isBuyer;
    }

    public int getPaxType() {
        return paxType;
    }
}
