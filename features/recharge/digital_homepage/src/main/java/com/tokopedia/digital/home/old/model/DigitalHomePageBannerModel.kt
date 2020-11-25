package com.tokopedia.digital.home.old.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.digital.home.old.presentation.adapter.DigitalHomePageTypeFactory

class DigitalHomePageBannerModel(@SerializedName("rechargeBanner")
                                 @Expose
                                 val bannerList: List<Banner> = listOf()) : DigitalHomePageItemModel() {

    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class Banner(@SerializedName("id")
                      @Expose
                      val id: Int = 0,
                      @SerializedName("img_url")
                      @Expose
                      val imgUrl: String = "",
                      @SerializedName("title")
                      @Expose
                      val title: String = "",
                      @SerializedName("app_link")
                      @Expose
                      val applink: String = "",
                      @SerializedName("filename")
                      @Expose
                      val filename: String = "")
}