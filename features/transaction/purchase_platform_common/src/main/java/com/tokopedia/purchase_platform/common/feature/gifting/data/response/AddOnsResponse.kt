package com.tokopedia.purchase_platform.common.feature.gifting.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class AddOnsResponse(

		@SerializedName("add_on_button")
	val addOnButton: AddOnButton = AddOnButton(),

		@SerializedName("add_on_bottomsheet")
	val addOnBottomsheet: AddOnBottomsheet = AddOnBottomsheet(),

		@SerializedName("add_on_data")
	val addOnData: List<AddOnDataItem> = emptyList(),

		@SerializedName("status")
	val status: Int = 0
) {
	data class AddOnButton(

			@SerializedName("left_icon_url")
			val leftIconUrl: String = "",

			@SerializedName("right_icon_url")
			val rightIconUrl: String = "",

			@SerializedName("description")
			val description: String = "",

			@SerializedName("action")
			val action: Int = 0,

			@SerializedName("title")
			val title: String = ""
	)

	data class AddOnBottomsheet(

			@SerializedName("ticker")
			val ticker: Ticker = Ticker(),

			@SerializedName("header_title")
			val headerTitle: String = "",

			@SerializedName("description")
			val description: String = "",

			@SerializedName("products")
			val products: List<ProductsItem> = emptyList()
	) {
		data class Ticker(

				@SerializedName("text")
				val text: String = ""
		)

		data class ProductsItem(

				@SerializedName("product_image_url")
				val productImageUrl: String = "",

				@SerializedName("product_name")
				val productName: String = ""
		)
	}

	data class AddOnDataItem(

			@SerializedName("add_on_price")
			val addOnPrice: Long = 0L,

			@SuppressLint("Invalid Data Type") @SerializedName("add_on_id")
			val addOnId: Long = 0L,

			@SerializedName("add_on_metadata")
			val addOnMetadata: String = "",

			@SerializedName("add_on_qty")
			val addOnQty: Long = 0L
	)
}
