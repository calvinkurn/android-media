package com.tokopedia.topads.data.response

import com.google.gson.annotations.SerializedName

data class AdCreationOption(

		@field:SerializedName("topAdsGetShopInfoV1_1")
		val topAdsGetShopInfo: TopAdsGetShopInfo = TopAdsGetShopInfo()
) {
	data class TopAdsGetShopInfo(

			@field:SerializedName("data")
			val data: Data = Data()
	) {
		data class Data(

				@field:SerializedName("category_desc")
				val categoryDesc: String = "",

				@field:SerializedName("category")
				val category: Int = 0
		)
	}
}
