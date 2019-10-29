package com.tokopedia.affiliate.feature.dashboard.data.pojo.commission

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AffiliateProductDetailResponse(

	@SerializedName("affiliatedProductDetail")
	@Expose
	val affiliatedProductDetail: AffiliatedProductDetail = AffiliatedProductDetail()
)