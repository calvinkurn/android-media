package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageAdapterFactory

class DigitalHomePageBannerModel(@SerializedName("rechargeBanner")
                                 @Expose
                                 val bannerList: List<Banner> = listOf()) : DigitalHomePageItemModel() {

    override fun visitableId(): Int {
        return BANNER_SECTION_ID
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is DigitalHomePageBannerModel) {
            this.bannerList == b.bannerList
        } else false
    }

    override fun type(typeFactory: DigitalHomePageAdapterFactory): Int {
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

    companion object {
        const val BANNER_SECTION_ID = -5
    }
}