package com.tokopedia.digital.home.old.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.digital.home.old.presentation.adapter.DigitalHomePageTypeFactory

abstract class DigitalHomePageSectionModel(
        @SerializedName("rechargeSubHomePageSection")
        @Expose
        val data: Data? = null) : DigitalHomePageItemModel() {

    abstract override fun type(typeFactory: DigitalHomePageTypeFactory): Int

    data class Data(@SerializedName("id")
                    @Expose
                    val id: String = "",
                    @SerializedName("section")
                    @Expose
                    val section: Section = Section())

    data class Section(@SerializedName("title")
                       @Expose
                       val title: String = "",
                       @SerializedName("items")
                       @Expose
                       val items: List<Item> = listOf())

    data class Item(@SerializedName("id")
                    @Expose
                    val id: String = "",
                    @SerializedName("title")
                    @Expose
                    val title: String = "",
                    @SerializedName("media_url")
                    @Expose
                    val mediaUrl: String = "",
                    @SerializedName("navigate_url")
                    @Expose
                    val navigateUrl: String = "",
                    @SerializedName("app_link")
                    @Expose
                    val applink: String = "",
                    @SerializedName("html_content")
                    @Expose
                    val htmlContent: String = "",
                    @SerializedName("status")
                    @Expose
                    val status: Int = 0)
}

class DigitalHomePageFavoritesModel: DigitalHomePageSectionModel() {
    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }
}

class DigitalHomePageTrustMarkModel: DigitalHomePageSectionModel() {
    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }
}

class DigitalHomePageNewUserZoneModel: DigitalHomePageSectionModel() {
    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }
}

class DigitalHomePageSpotlightModel: DigitalHomePageSectionModel() {
    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }
}

class DigitalHomePageSubscriptionModel: DigitalHomePageSectionModel() {
    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }
}