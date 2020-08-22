package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageAdapterFactory

abstract class DigitalHomePageSectionModel(
        @SerializedName("rechargeSubHomePageSection")
        @Expose
        val data: Data? = null) : DigitalHomePageItemModel() {

    override fun visitableId(): Int {
        return data?.section?.id ?: -1
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is DigitalHomePageSectionModel) {
            this.data == b.data
        } else false
    }

    data class Data(@SerializedName("slug_name")
                    @Expose
                    val slugName: String = "",
                    @SerializedName("section")
                    @Expose
                    val section: Section = Section())

    data class Section(@SerializedName("id")
                       @Expose
                       val id: Int = -1,
                       @SerializedName("title")
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
    override fun type(typeFactory: DigitalHomePageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

class DigitalHomePageTrustMarkModel: DigitalHomePageSectionModel() {
    override fun type(typeFactory: DigitalHomePageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

class DigitalHomePageNewUserZoneModel: DigitalHomePageSectionModel() {
    override fun type(typeFactory: DigitalHomePageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

class DigitalHomePageSpotlightModel: DigitalHomePageSectionModel() {
    override fun type(typeFactory: DigitalHomePageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

class DigitalHomePageSubscriptionModel: DigitalHomePageSectionModel() {
    override fun type(typeFactory: DigitalHomePageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}