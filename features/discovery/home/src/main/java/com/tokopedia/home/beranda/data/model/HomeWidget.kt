package com.tokopedia.home.beranda.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business.BusinessWidgetTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

class HomeWidget(
        @SerializedName("widget_tab")
        @Expose
        val tabBusinessList: List<TabItem> = listOf(),
        @SerializedName("widget_grid")
        @Expose
        val contentItemTabList: List<ContentItemTab> = listOf()
) {

    data class Data (
            @SerializedName("home_widget")
            @Expose
            val homeWidget: HomeWidget = HomeWidget()
    )

    data class Response(
            @SerializedName("data")
            @Expose
            val data: Data = Data(),
            @Expose
            @SerializedName("errors")
            val errors: List<GraphqlError> = listOf()
    )

    data class TabItem(
            @SerializedName("id")
            @Expose
            val id: Int,
            @SerializedName("name")
            @Expose
            val name: String = ""
    ): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString() ?: ""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<TabItem> {
            override fun createFromParcel(parcel: Parcel): TabItem {
                return TabItem(parcel)
            }

            override fun newArray(size: Int): Array<TabItem?> {
                return arrayOfNulls(size)
            }
        }
    }

    class ContentItemTab(
            @SerializedName("id")
            @Expose
            val id: Int,
            @SerializedName("name")
            @Expose
            val name: String = "",
            @SerializedName("image_url")
            @Expose
            val imageUrl: String = "",
            @SerializedName("url")
            @Expose
            val url: String = "",
            @SerializedName("applink")
            @Expose
            val applink: String = "",
            @SerializedName("title_1")
            @Expose
            val title1st: String = "",
            @SerializedName("desc_1")
            @Expose
            val desc1st: String = "",
            @SerializedName("title_2")
            @Expose
            val title2nd: String = "",
            @SerializedName("desc_2")
            @Expose
            val desc2nd: String = "",
            @SerializedName("tag_name")
            @Expose
            val tagName: String = "",
            @SerializedName("tag_type")
            @Expose
            val tagType: Int,
            @SerializedName("price")
            @Expose
            val price: String = "",
            @SerializedName("original_price")
            @Expose
            val originalPrice: String = "",
            @SerializedName("price_prefix")
            @Expose
            val pricePrefix: String = "",
            @SerializedName("template_id")
            @Expose
            val templateId: Int
    ): Parcelable, Visitable<BusinessWidgetTypeFactory> {

        var impressHolder: ImpressHolder = ImpressHolder()

        override fun type(typeFactory: BusinessWidgetTypeFactory?): Int {
            return typeFactory!!.type(this)
        }

        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readInt(),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readInt()
        ) {
            this.impressHolder = parcel.readParcelable(ImpressHolder::class.java.classLoader) ?: ImpressHolder()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeString(name)
            parcel.writeString(imageUrl)
            parcel.writeString(url)
            parcel.writeString(applink)
            parcel.writeString(title1st)
            parcel.writeString(desc1st)
            parcel.writeString(title2nd)
            parcel.writeString(desc2nd)
            parcel.writeString(tagName)
            parcel.writeInt(tagType)
            parcel.writeString(price)
            parcel.writeString(originalPrice)
            parcel.writeString(pricePrefix)
            parcel.writeInt(templateId)
            parcel.writeParcelable(impressHolder, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ContentItemTab> {
            override fun createFromParcel(parcel: Parcel): ContentItemTab {
                return ContentItemTab(parcel)
            }

            override fun newArray(size: Int): Array<ContentItemTab?> {
                return arrayOfNulls(size)
            }
        }
    }
}
