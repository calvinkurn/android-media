package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 26/04/19
 */
@Parcelize
class PropertyImageItem(@SerializedName("isLogoPhoto")
                        @Expose
                        val isLogoPhoto: Boolean = false,
                        @SerializedName("urlSquare60")
                        @Expose
                        val urlSquare6: String = "",
                        @SerializedName("mainPhoto")
                        @Expose
                        val mainPhoto: Boolean = false,
                        @SerializedName("urlOriginal")
                        @Expose
                        val urlOriginal: String = "",
                        @SerializedName("urlMax300")
                        @Expose
                        val urlMax300: String = "") : Parcelable