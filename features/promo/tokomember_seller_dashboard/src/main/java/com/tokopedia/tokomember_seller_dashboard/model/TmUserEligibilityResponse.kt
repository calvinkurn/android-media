package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.SerializedName

data class TmUserEligibilityResponse(

	@SerializedName("data")
	val data: TmUserElligibilityResponseData? = null
)

data class RbacAuthorizeAccess(

	@SerializedName("is_authorized")
	val isAuthorized: Boolean? = null,

	@SerializedName("error")
	val error: String? = null
)

data class TmUserElligibilityResponseData(

	@SerializedName("rbacAuthorizeAccess")
	val rbacAuthorizeAccess: RbacAuthorizeAccess? = null
)
