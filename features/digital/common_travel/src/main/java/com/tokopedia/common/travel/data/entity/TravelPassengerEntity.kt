package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 14/08/18.
 */
class TravelPassengerEntity (
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("userId")
    @Expose
    val userId: Int,
    @SerializedName("title")
    @Expose
    val title: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("firstName")
    @Expose
    val firstName: String,
    @SerializedName("lastName")
    @Expose
    val lastName: String,
    @SerializedName("birthDate")
    @Expose
    val birthDate: String,
    @SerializedName("nationality")
    @Expose
    val nationality: String,
    @SerializedName("passportNo")
    @Expose
    val passportNo: String,
    @SerializedName("passportCountry")
    @Expose
    val passportCountry: String,
    @SerializedName("passportExpiry")
    @Expose
    val passportExpiry: String,
    @SerializedName("idNumber")
    @Expose
    val idNumber: String,
    @SerializedName("isBuyer")
    @Expose
    val isBuyer: Int,
    @SerializedName("paxType")
    @Expose
    val paxType: Int,
    @SerializedName("travelId")
    @Expose
    val travelId: Int)
