package com.tokopedia.purchase_platform.features.promo.data.request.validate_use

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.features.promo.data.request.CouponListRequest
import com.tokopedia.purchase_platform.features.promo.data.request.varidate_use.PromoRequest

@Generated("com.robohorse.robopojogenerator")
data class Params(

	@field:SerializedName("promo")
	var promo: CouponListRequest? = CouponListRequest()
)