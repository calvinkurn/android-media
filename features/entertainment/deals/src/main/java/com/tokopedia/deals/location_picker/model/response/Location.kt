package com.tokopedia.deals.location_picker.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.deals.location_picker.ui.typefactory.DealsSelectLocationTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationData(
        @SerializedName("event_location_search")
        @Expose
        var eventLocationSearch: EventLocationSearch = EventLocationSearch()
): Parcelable

@Parcelize
data class EventLocationSearch(
        @SerializedName("locations")
        @Expose
        var locations: List<Location> = arrayListOf(),

        @SerializedName("page")
        @Expose
        var page: Page = Page(),

        @SerializedName("count")
        @Expose
        var count: String = ""
): Parcelable

@Parcelize
data class Location(
        @SerializedName("id")
        @Expose
        var id: String = "0",

        @SerializedName("name")
        @Expose
        var name: String = "",

        @SerializedName("search_name")
        @Expose
        var searchName: String = "",

        @SerializedName("coordinates")
        @Expose
        var coordinates: String = "",

        @SerializedName("address")
        @Expose
        var address: String = "",

        @SerializedName("city_id")
        @Expose
        var cityId: String = "0",

        @SerializedName("city_name")
        @Expose
        var cityName: String = "",

        @SerializedName("priority")
        @Expose
        var priority: String = "",

        @SerializedName("icon_web")
        @Expose
        var iconWeb: String = "",

        @SerializedName("icon_app")
        @Expose
        var iconApp: String = "",

        @SerializedName("image_web")
        @Expose
        var imageWeb: String = "",

        @SerializedName("image_app")
        @Expose
        var imageApp: String = "",

        @SerializedName("location_type")
        @Expose
        var locType: LocationType = LocationType()

): Parcelable, Visitable<DealsSelectLocationTypeFactory> {

        override fun type(typeFactoryDeals: DealsSelectLocationTypeFactory): Int {
                return typeFactoryDeals.type(this)
        }

        override fun equals(other: Any?): Boolean {
                return if (other is Location) {
                        this.name.equals(other.name) && this.id.equals(other.id)
                } else false
        }
}