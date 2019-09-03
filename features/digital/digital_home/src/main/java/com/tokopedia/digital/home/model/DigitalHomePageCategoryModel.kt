package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageBannerViewHolder
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageItemModel
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageTypeFactory

class DigitalHomePageCategoryModel(@SerializedName("rechargeCatalogMenu")
                                   @Expose
                                   val listSubtitle: List<Subtitle> = listOf()) : DigitalHomePageItemModel() {
    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class Subtitle(@SerializedName("name")
                      @Expose
                      val name: Int = 0,
                      @SerializedName("label")
                      @Expose
                      val label: String = "",
                      @SerializedName("app_link")
                      @Expose
                      val applink: String = "",
                      @SerializedName("sub_menu")
                      @Expose
                      val submenu: List<Submenu> = listOf())

    data class Submenu(@SerializedName("name")
                       @Expose
                       val name: Int = 0,
                       @SerializedName("label")
                       @Expose
                       val label: String = "",
                       @SerializedName("app_link")
                       @Expose
                       val applink: String = "",
                       @SerializedName("icon")
                       @Expose
                       val icon: String = "")
}