package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.track.Tracking

/**
 * @author by yfsx on 04/12/18.
 */
data class PostTagItem(
        @SerializedName("id")
        @Expose
        var id: String = "",

        @SerializedName("text")
        @Expose
        var text: String = "",

        @SerializedName("price")
        @Expose
        var price: String = "",

        @SerializedName("priceOriginal")
        @Expose
        var priceOriginal: String = "",

        @SerializedName("type")
        @Expose
        var type: String = "",

        @SerializedName("appLink")
        @Expose
        var applink: String = "",

        @SerializedName("webLink")
        @Expose
        var weblink: String = "",

        @SerializedName("thumbnail")
        @Expose
        var thumbnail: String = "",

        @SerializedName("percentage")
        val percentage: String = "",

        @SerializedName("isSelected")
        val isSelected: Boolean = false,

        @SerializedName("position")
        @Expose
        var position: MutableList<Float> = ArrayList(),

        @SerializedName("isWishlisted")
        @Expose
        var isWishlisted: Boolean = false,

        @SerializedName("tracking")
        @Expose
        val tracking: List<Tracking> = ArrayList(),

        @SerializedName("tags")
        @Expose
        val tags: List<TagsItem> = ArrayList(),

        @SerializedName("shop")
        @Expose
        val shop: List<PostTagItemShop> = ArrayList(),

        @SerializedName("buttonCTA")
        @Expose
        val buttonCTA: List<PostTagItemButtonCTA> = ArrayList(),

        @SerializedName("rating")
        val rating: Int = 0
) {
        fun copy(): PostTagItem {
                val newTracking: ArrayList<Tracking> = arrayListOf()
                for (track in tracking) {
                        newTracking.add(track.copy())
                }
                val newTags: ArrayList<TagsItem> = arrayListOf()
                for (tag in tags) {
                        newTags.add(tag.copy())
                }
                val newShops: ArrayList<PostTagItemShop> = arrayListOf()
                for (newShop in shop) {
                        newShops.add(newShop.copy())
                }
                val newButtonCtas: ArrayList<PostTagItemButtonCTA> = arrayListOf()
                for (newButtonCta in buttonCTA) {
                        newButtonCtas.add(newButtonCta.copy())
                }
                return PostTagItem(id,
                        text,
                        price,
                        priceOriginal,
                        type,
                        applink,
                        weblink,
                        thumbnail,
                        percentage,
                        isSelected,
                        position,
                        isWishlisted,
                        newTracking,
                        newTags,
                        newShops,
                        newButtonCtas,
                        rating)
        }
}