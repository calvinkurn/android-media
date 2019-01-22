package com.tokopedia.promocheckout.list.model.listcoupon

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckout.list.view.adapter.PromoCheckoutListAdapterFactory

class PromoCheckoutListModel() : Visitable<PromoCheckoutListAdapterFactory>, Parcelable {
    override fun type(typeFactory: PromoCheckoutListAdapterFactory?): Int {
        return typeFactory?.type(this)?:0
    }

    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("promoID")
    @Expose
    var promoID: Int = 0
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("expired")
    @Expose
    var expired: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("catalogTitle")
    @Expose
    var catalogTitle: String? = null
    @SerializedName("subTitle")
    @Expose
    var subTitle: String? = null
    @SerializedName("catalogSubTitle")
    @Expose
    var catalogSubTitle: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null
    @SerializedName("imageUrlMobile")
    @Expose
    var imageUrlMobile: String? = null
    @SerializedName("thumbnailUrl")
    @Expose
    var thumbnailUrl: String? = null
    @SerializedName("thumbnailUrlMobile")
    @Expose
    var thumbnailUrlMobile: String? = null
    @SerializedName("imageV2Url")
    @Expose
    var imageV2Url: String? = null
    @SerializedName("imageV2UrlMobile")
    @Expose
    var imageV2UrlMobile: String? = null
    @SerializedName("thumbnailV2Url")
    @Expose
    var thumbnailV2Url: String? = null
    @SerializedName("thumbnailV2UrlMobile")
    @Expose
    var thumbnailV2UrlMobile: String? = null
    @SerializedName("cta")
    @Expose
    var cta: String? = null
    @SerializedName("ctaDesktop")
    @Expose
    var ctaDesktop: String? = null
    @SerializedName("slug")
    @Expose
    var slug: String? = null
    @SerializedName("usage")
    @Expose
    var usage: Usage? = null
    @SerializedName("minimum_usage")
    @Expose
    var minimumUsage: String? = null
    @SerializedName("minimum_usage_label")
    @Expose
    var minimumUsageLabel: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        promoID = parcel.readInt()
        code = parcel.readString()
        expired = parcel.readString()
        title = parcel.readString()
        catalogTitle = parcel.readString()
        subTitle = parcel.readString()
        catalogSubTitle = parcel.readString()
        description = parcel.readString()
        icon = parcel.readString()
        imageUrl = parcel.readString()
        imageUrlMobile = parcel.readString()
        thumbnailUrl = parcel.readString()
        thumbnailUrlMobile = parcel.readString()
        imageV2Url = parcel.readString()
        imageV2UrlMobile = parcel.readString()
        thumbnailV2Url = parcel.readString()
        thumbnailV2UrlMobile = parcel.readString()
        cta = parcel.readString()
        ctaDesktop = parcel.readString()
        slug = parcel.readString()
        minimumUsage = parcel.readString()
        minimumUsageLabel = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(promoID)
        parcel.writeString(code)
        parcel.writeString(expired)
        parcel.writeString(title)
        parcel.writeString(catalogTitle)
        parcel.writeString(subTitle)
        parcel.writeString(catalogSubTitle)
        parcel.writeString(description)
        parcel.writeString(icon)
        parcel.writeString(imageUrl)
        parcel.writeString(imageUrlMobile)
        parcel.writeString(thumbnailUrl)
        parcel.writeString(thumbnailUrlMobile)
        parcel.writeString(imageV2Url)
        parcel.writeString(imageV2UrlMobile)
        parcel.writeString(thumbnailV2Url)
        parcel.writeString(thumbnailV2UrlMobile)
        parcel.writeString(cta)
        parcel.writeString(ctaDesktop)
        parcel.writeString(slug)
        parcel.writeString(minimumUsage)
        parcel.writeString(minimumUsageLabel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoCheckoutListModel> {
        override fun createFromParcel(parcel: Parcel): PromoCheckoutListModel {
            return PromoCheckoutListModel(parcel)
        }

        override fun newArray(size: Int): Array<PromoCheckoutListModel?> {
            return arrayOfNulls(size)
        }
    }


}
