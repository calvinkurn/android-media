package com.tokopedia.salam.umrah.travel.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelGalleryAdapterTypeFactory
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelProductAdapterTypeFactory

/**
 * @author by Firman on 3/2/20
 */

data class UmrahGalleriesEntity(
        @SerializedName("umrahGalleries")
        @Expose
        val umrahGalleries: List<UmrahGallery> = emptyList()
)
data class UmrahGallery(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("subTitle")
        @Expose
        val subTitle: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("createdAt")
        @Expose
        val createdAt: String = "",
        @SerializedName("medias")
        @Expose
        val medias: List<Media> = emptyList(),
        var isViewed : Boolean = false
): Visitable<UmrahTravelGalleryAdapterTypeFactory> {
        override fun type(typeFactory: UmrahTravelGalleryAdapterTypeFactory?): Int =
                typeFactory?.type(type, medias.size) ?: 0
}

data class Media(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("source")
        @Expose
        val source: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String = ""
)