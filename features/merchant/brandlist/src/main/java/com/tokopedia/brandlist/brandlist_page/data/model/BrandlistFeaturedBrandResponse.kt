package com.tokopedia.brandlist.brandlist_page.data.model

import com.google.gson.annotations.SerializedName

data class BrandlistFeaturedBrandResponse(
        @SerializedName("OfficialStoreFeaturedShop")
        val officialStoreFeaturedShop: OfficialStoreFeaturedShop = OfficialStoreFeaturedShop()
)