package com.tokopedia.ordermanagement.snapshot.data.model

import com.google.gson.annotations.SerializedName

data class SnapshotResponse(

	@field:SerializedName("data")
	val data: Data = Data()
) {
	data class Data(

			@field:SerializedName("get_order_snapshot")
			val getOrderSnapshot: GetOrderSnapshot = GetOrderSnapshot()
	) {
		data class GetOrderSnapshot(

				@field:SerializedName("product_price_formatted")
				val productPriceFormatted: String = "",

				@field:SerializedName("shop_image_primary_url")
				val shopImagePrimaryUrl: String = "",

				@field:SerializedName("product_url")
				val productUrl: String = "",

				@field:SerializedName("product_total_weight_formatted")
				val productTotalWeightFormatted: String = "",

				@field:SerializedName("is_os")
				val isOs: Boolean = false,

				@field:SerializedName("shop_summary")
				val shopSummary: ShopSummary = ShopSummary(),

				@field:SerializedName("pre_order")
				val preOrder: Boolean = false,

				@field:SerializedName("product_additional_data")
				val productAdditionalData: ProductAdditionalData = ProductAdditionalData(),

				@field:SerializedName("order_detail")
				val orderDetail: OrderDetail = OrderDetail(),

				@field:SerializedName("is_pm")
				val isPm: Boolean = false,

				@field:SerializedName("product_weight_formatted")
				val productWeightFormatted: String = "",

				@field:SerializedName("pre_order_duration")
				val preOrderDuration: String = "",

				@field:SerializedName("product_image_secondary")
				val productImageSecondary: List<ProductImageSecondaryItem> = listOf(),

				@field:SerializedName("product_total_price_formatted")
				val productTotalPriceFormatted: String = "",

				@field:SerializedName("campaign_data")
				val campaignData: CampaignData = CampaignData()
		) {
			data class ShopSummary(

					@field:SerializedName("shop_id")
					val shopId: Int = -1,

					@field:SerializedName("user_id")
					val userId: Int = -1,

					@field:SerializedName("shop_domain")
					val shopDomain: String = "",

					@field:SerializedName("logo")
					val logo: String = "",

					@field:SerializedName("shop_name")
					val shopName: String = ""
			)

			data class ProductAdditionalData(

					@field:SerializedName("create_time")
					val createTime: String = "",

					@field:SerializedName("product_price")
					val productPrice: String = ""
			)

			data class OrderDetail(

					@field:SerializedName("subtotal_price")
					val subtotalPrice: Int = -1,

					@field:SerializedName("quantity")
					val quantity: Int = -1,

					@field:SerializedName("notes")
					val notes: String = "",

					@field:SerializedName("create_time")
					val createTime: String = "",

					@field:SerializedName("product_weight")
					val productWeight: Double = 0.0,

					@field:SerializedName("quantity_reject")
					val quantityReject: Int = -1,

					@field:SerializedName("finsurance")
					val finsurance: Int = -1,

					@field:SerializedName("product_price")
					val productPrice: Int = -1,

					@field:SerializedName("product_name")
					val productName: String = "",

					@field:SerializedName("total_weight")
					val totalWeight: Double = 0.0,

					@field:SerializedName("product_desc")
					val productDesc: String = "",

					@field:SerializedName("order_dtl_id")
					val orderDtlId: Int = -1,

					@field:SerializedName("condition")
					val condition: Int = -1,

					@field:SerializedName("returnable")
					val returnable: Int = -1,

					@field:SerializedName("insurance_price")
					val insurancePrice: Int = -1,

					@field:SerializedName("normal_price")
					val normalPrice: Int = -1,

					@field:SerializedName("product_id")
					val productId: Int = -1,

					@field:SerializedName("quantity_deliver")
					val quantityDeliver: Int = -1,

					@field:SerializedName("child_cat_id")
					val childCatId: Int = -1,

					@field:SerializedName("min_order")
					val minOrder: Int = -1,

					@field:SerializedName("order_id")
					val orderId: Int = -1,

					@field:SerializedName("currency_id")
					val currencyId: Int = -1,

					@field:SerializedName("must_insurance")
					val mustInsurance: Int = -1
			)

			data class ProductImageSecondaryItem(

					@field:SerializedName("file_path")
					val filePath: String = "",

					@field:SerializedName("image_url")
					val imageUrl: String = "",

					@field:SerializedName("file_name")
					val fileName: String = ""
			)

			data class CampaignData(

					@field:SerializedName("product_id")
					val productId: Int = -1,

					@field:SerializedName("campaign")
					val campaign: Campaign = Campaign()
			) {
				data class Campaign(

						@field:SerializedName("discount_percentage_text_color")
						val discountPercentageTextColor: String = "",

						@field:SerializedName("discount_percentage_text")
						val discountPercentageText: String = "",

						@field:SerializedName("original_price")
						val originalPrice: String = "",

						@field:SerializedName("original_price_fmt")
						val originalPriceFmt: String = "",

						@field:SerializedName("discount_percentage_label_color")
						val discountPercentageLabelColor: String = "",

						@field:SerializedName("discounted_price")
						val discountedPrice: String = ""
				)
			}
		}
	}
}
