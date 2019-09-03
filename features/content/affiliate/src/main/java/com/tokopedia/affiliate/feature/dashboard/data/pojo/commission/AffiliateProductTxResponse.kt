package com.tokopedia.affiliate.feature.dashboard.data.pojo.commission

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class AffiliateProductTxResponse(

	@SerializedName("affiliatedProductTxList")
	@Expose
	val affiliatedProductTxList: AffiliatedProductTxList = AffiliatedProductTxList()
)