package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DigitalHomePageSearchAutoComplete(
        @SerializedName("digiPersoSearchSuggestion")
        @Expose
        val digiPersoSearchSuggestion : DigiPersoSearchSuggestion = DigiPersoSearchSuggestion()
)

data class DigiPersoSearchSuggestion (
        @SerializedName("data")
        @Expose
        val data : Data = Data()
)

data class Data (
        @SerializedName("id")
        @Expose
        val id : String = "",
        @SerializedName("name")
        @Expose
        val name : String = "",
        @SerializedName("tracking")
        @Expose
        val tracking : Tracking = Tracking(),
        @SerializedName("items")
        @Expose
        val items : List<Items> = emptyList(),
        @SerializedName("__typename")
        @Expose
        val typename : String = ""
)

data class Tracking (
        @SerializedName("userType")
        @Expose
        val userType : String = "",
        @SerializedName("keyword")
        @Expose
        val keyword : String = "",
        @SerializedName("categoryId")
        @Expose
        val categoryId : String = "",
        @SerializedName("categoryName")
        @Expose
        val categoryName : String = "",
        @SerializedName("operatorId")
        @Expose
        val operatorId : String = "",
        @SerializedName("operatorName")
        @Expose
        val operatorName : String = "",
        @SerializedName("itemType")
        @Expose
        val itemType : String = "",
        @SerializedName("__typename")
        @Expose
        val typename : String = ""
)

data class Items (
        @SerializedName("template")
        @Expose
        val template : String = "",
        @SerializedName("type")
        @Expose
        val type : String = "",
        @SerializedName("appLink")
        @Expose
        val applink : String = "",
        @SerializedName("url")
        @Expose
        val url : String = "",
        @SerializedName("title")
        @Expose
        val title : String = "",
        @SerializedName("subtitle")
        @Expose
        val subtitle : String = "",
        @SerializedName("iconTitle")
        @Expose
        val iconTitle : String = "",
        @SerializedName("iconSubtitle")
        @Expose
        val iconSubtitle : String = "",
        @SerializedName("shortcutImage")
        @Expose
        val shortcutImage : String = "",
        @SerializedName("imageURL")
        @Expose
        val imageUrl : String = "",
        @SerializedName("urlTracker")
        @Expose
        val urlTracker : String = "",
        @SerializedName("tracking")
        @Expose
        val tracking : Tracking = Tracking(),
        @SerializedName("discountPercentage")
        @Expose
        val discountPercentage : String = "",
        @SerializedName("discountedPrice")
        @Expose
        val discountedPrice : String = "",
        @SerializedName("originalPrice")
        @Expose
        val originalPrice : String = "",
        @SerializedName("__typename")
        @Expose
        val typename : String = ""
)