package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageAdapterFactory

class DigitalHomePageCategoryModel(@SerializedName("rechargeCatalogMenu")
                                   @Expose
                                   val listSubtitle: List<Subtitle> = listOf()) : DigitalHomePageItemModel() {

    override fun visitableId(): Int {
        return CATEGORY_SECTION_ID
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is DigitalHomePageCategoryModel) {
            this.listSubtitle == b.listSubtitle
        } else false
    }

    override fun type(typeFactory: DigitalHomePageAdapterFactory): Int {
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

    companion object {
        const val CATEGORY_SECTION_ID = -6
    }
}