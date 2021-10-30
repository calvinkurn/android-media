package com.tokopedia.wishlist.data.model

/**
 * Created by fwidjaja on 14/10/21.
 */
data class WishlistV2TypeLayoutData(
        val dataObject: Any = Any(),
        val typeLayout: String? = "",
        val listThreeDotsMenu: List<WishlistV2Response.Data.WishlistV2.ItemsItem.Buttons.AdditionalButtonsItem> = listOf())