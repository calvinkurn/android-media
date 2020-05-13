package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class CashbackDetailsItem(

	@field:SerializedName("benefit_type")
	val benefitType: String? = null,

	@field:SerializedName("amount_points")
	val amountPoints: Int? = null,

	@field:SerializedName("amount_idr")
	val amountIdr: Int? = null
)