package com.tokopedia.shop.sort.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nathan on 3/4/18.
 */
class ShopProductSort {
    /**
     * @return The name
     */
    /**
     * @param name The name
     */
    @SerializedName("name")
    @Expose
    var name: String? = null
    /**
     * @return The key
     */
    /**
     * @param key The key
     */
    @SerializedName("key")
    @Expose
    var key: String? = null
    /**
     * @return The value
     */
    /**
     * @param value The value
     */
    @SerializedName("value")
    @Expose
    var value: String? = null
    /**
     * @return The inputType
     */
    /**
     * @param inputType The input_type
     */
    @SerializedName("input_type")
    @Expose
    var inputType: String? = null
    override fun toString(): String {
        return name!!
    }
}