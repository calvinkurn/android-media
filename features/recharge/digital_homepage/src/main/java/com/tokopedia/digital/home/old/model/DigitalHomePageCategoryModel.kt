package com.tokopedia.digital.home.old.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.digital.home.old.presentation.adapter.DigitalHomePageTypeFactory

class DigitalHomePageCategoryModel(@SerializedName("rechargeCatalogMenu")
                                   @Expose
                                   val listSubtitle: List<Subtitle> = listOf()) : DigitalHomePageItemModel() {
    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class Subtitle(@SerializedName("name")
                      @Expose
                      val name: String = "",
                      @SerializedName("label")
                      @Expose
                      val label: String = "",
                      @SerializedName("app_link")
                      @Expose
                      val applink: String = "",
                      @SerializedName("sub_menu")
                      @Expose
                      val submenu: List<Submenu> = listOf())

    open class Submenu(@SerializedName("id")
                       @Expose
                       val id: String = "",
                       @SerializedName("name")
                       @Expose
                       val name: String = "",
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