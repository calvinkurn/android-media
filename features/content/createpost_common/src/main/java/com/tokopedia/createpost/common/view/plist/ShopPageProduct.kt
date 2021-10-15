package com.tokopedia.createpost.common.view.plist

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem
import java.io.Serializable

class ShopPageProduct : BaseItem() , Serializable {
    @field:SerializedName("name")
    val name: String? = null

    @field:SerializedName("primary_image")
    val pImage: PrimaryImage? = null

    @field:SerializedName("price")
    val price: SPrice? = null

    @field:SerializedName("product_id")
    val pId: String? = null

    @field:SerializedName("campaign")
    val campaign: Campaign? = null

    override fun getSearchQuery(): String {
        return name!!
    }
}