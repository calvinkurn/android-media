package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 15/03/19.
 */
data class PostTag (

        @SerializedName("id")
        @Expose
        var id:String = "",
        @SerializedName("text")
        @Expose
        var text:String = "",
        @SerializedName("type")
        @Expose
        var type:String = "",
        @SerializedName("totalItems")
        @Expose
        var totalItems:Int = 0,
        @SerializedName("items")
        @Expose
        var items: List<PostTagItem> = ArrayList()
){
        fun copy(): PostTag {
                val newItems: ArrayList<PostTagItem> = arrayListOf()
                for (item in items) {
                        newItems.add(item.copy())
                }
                return PostTag(id,
                        text,
                        type,
                        totalItems,
                        newItems)
        }
}