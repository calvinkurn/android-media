package com.tokopedia.entertainment.pdp.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.EMPTY_TYPE
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactory
import java.io.Serializable

data class EventProductDetailEntity(
        @SerializedName("event_product_detail_v3")
        @Expose
        val eventProductDetail: EventProductDetail = EventProductDetail()
)

data class EventProductDetail(
        @SerializedName("productDetailData")
        @Expose
        val productDetailData: ProductDetailData = ProductDetailData()
)

data class ProductDetailData(
        @SerializedName("app_url")
        @Expose
        val appUrl: String = "",
        @SerializedName("web_url")
        @Expose
        val webUrl: String = "",
        @SerializedName("action_text")
        @Expose
        val actionText: String = "",
        @SerializedName("autocode")
        @Expose
        val autocode: String = "",
        @SerializedName("checkout_business_type")
        @Expose
        val checkoutBusinessType: Int = 0,
        @SerializedName("checkout_data_type")
        @Expose
        val checkoutDataType: String = "",
        @SerializedName("brand")
        @Expose
        val brand: Brand = Brand(),
        @SerializedName("brand_id")
        @Expose
        val brandId: String = "",
        @SerializedName("catalog")
        @Expose
        val catalog: Catalog = Catalog(),
        @SerializedName("category")
        @Expose
        val category: List<Category> = emptyList(),
        @SerializedName("category_id")
        @Expose
        val categoryId: String = "",
        @SerializedName("censor")
        @Expose
        val censor: String = "",
        @SerializedName("child_category_ids")
        @Expose
        val childCategoryIds: String = "",
        @SerializedName("city_name")
        @Expose
        val cityName: String = "",
        @SerializedName("code")
        @Expose
        val code: String = "",
        @SerializedName("convenience_fee")
        @Expose
        val convenienceFee: String = "",
        @SerializedName("created_at")
        @Expose
        val createdAt: String = "",
        @SerializedName("custom_labels")
        @Expose
        val customLabels: String = "",
        @SerializedName("custom_text_1")
        @Expose
        val customText1: String = "",
        @SerializedName("custom_text_2")
        @Expose
        val customText2: String = "",
        @SerializedName("custom_text_3")
        @Expose
        val customText3: String = "",
        @SerializedName("custom_text_4")
        @Expose
        val customText4: String = "",
        @SerializedName("custom_text_5")
        @Expose
        val customText5: String = "",
        @SerializedName("dates")
        @Expose
        val dates: List<String> = emptyList(),
        @SerializedName("date_range")
        @Expose
        val dateRange: Boolean = false,
        @SerializedName("display_name")
        @Expose
        val displayName: String = "",
        @SerializedName("display_tags")
        @Expose
        val displayTags: String = "",
        @SerializedName("duration")
        @Expose
        val duration: String = "",
        @SerializedName("facilities")
        @Expose
        val facilities: List<Facilities> = emptyList(),
        @SerializedName("facility_group_id")
        @Expose
        val facilityGroupId: String = "",
        @SerializedName("form")
        @Expose
        val form: String = "",
        @SerializedName("forms")
        @Expose
        val forms: List<Form> = emptyList(),
        @SerializedName("genre")
        @Expose
        val genre: String = "",
        @SerializedName("has_seat_layout")
        @Expose
        val hasSeatLayout: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("image_app")
        @Expose
        val imageApp: String = "",
        @SerializedName("image_web")
        @Expose
        val imageWeb: String = "",
        @SerializedName("is_featured")
        @Expose
        val isFeatured: Int = 0,
        @SerializedName("is_food_available")
        @Expose
        val isFoodAvailable: Int = 0,
        @SerializedName("is_liked")
        @Expose
        val isLiked: Boolean = false,
        @SerializedName("is_promo")
        @Expose
        val isPromo: Int = 0,
        @SerializedName("is_searchable")
        @Expose
        val isSearchable: Int = 0,
        @SerializedName("is_top")
        @Expose
        val isTop: Int = 0,
        @SerializedName("likes")
        @Expose
        val likes: Int = 0,
        @SerializedName("location")
        @Expose
        val location: String = "",
        @SerializedName("long_rich_desc")
        @Expose
        val longRichDesc: String = "",
        @SerializedName("max_end_date")
        @Expose
        val maxEndDate: String = "",
        @SerializedName("max_end_time")
        @Expose
        val maxEndTime: String = "",
        @SerializedName("media")
        @Expose
        val media: List<Media> = emptyList(),
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("message_error")
        @Expose
        val messageError: String = "",
        @SerializedName("meta_description")
        @Expose
        val metaDescription: String = "",
        @SerializedName("meta_keywords")
        @Expose
        val metaKeywords: String = "",
        @SerializedName("meta_title")
        @Expose
        val metaTitle: String = "",
        @SerializedName("min_start_date")
        @Expose
        val minStartDate: String = "",
        @SerializedName("min_start_time")
        @Expose
        val minStartTime: String = "",
        @SerializedName("mrp")
        @Expose
        val mrp: String = "",
        @SerializedName("offer_text")
        @Expose
        val offerText: String = "",
        @SerializedName("outlets")
        @Expose
        val outlets: List<Outlet> = emptyList(),
        @SerializedName("packages")
        @Expose
        val packages: List<PackageV3> = emptyList(),
        @SerializedName("parent_id")
        @Expose
        val parentId: String = "",
        @SerializedName("priority")
        @Expose
        val priority: String = "",
        @SerializedName("promotion_text")
        @Expose
        val promotionText: String = "",
        @SerializedName("provider_id")
        @Expose
        val providerId: String = "",
        @SerializedName("provider_product_code")
        @Expose
        val providerProductCode: String = "",
        @SerializedName("provider_product_id")
        @Expose
        val providerProductId: String = "",
        @SerializedName("provider_product_name")
        @Expose
        val providerProductName: String = "",
        @SerializedName("quantity")
        @Expose
        val quantity: String = "",
        @SerializedName("rating")
        @Expose
        val rating: String = "",
        @SerializedName("recommendation_url")
        @Expose
        val recommendationUrl: String = "",
        @SerializedName("redirect")
        @Expose
        val redirect: Int = 0,
        @SerializedName("remaining_sale_time")
        @Expose
        val remainingSaleTime: String = "",
        @SerializedName("sale_end_date")
        @Expose
        val saleEndDate: String = "",
        @SerializedName("sale_end_time")
        @Expose
        val saleEndTime: String = "",
        @SerializedName("sale_start_date")
        @Expose
        val saleStartDate: String = "",
        @SerializedName("sale_start_time")
        @Expose
        val saleStartTime: String = "",
        @SerializedName("sales_price")
        @Expose
        val salesPrice: String = "",
        @SerializedName("salient_features")
        @Expose
        val salientFeatures: String = "",
        @SerializedName("saving")
        @Expose
        val saving: String = "",
        @SerializedName("saving_percentage")
        @Expose
        val savingPercentage: String = "",
        @SerializedName("schedules")
        @Expose
        val schedules: List<Schedules> = emptyList(),
        @SerializedName("search_tags")
        @Expose
        val searchTags: String = "",
        @SerializedName("seat_chart_type")
        @Expose
        val seatChartType: String = "",
        @SerializedName("seatmap_image")
        @Expose
        val seatmapImage: String = "",
        @SerializedName("sell_rate")
        @Expose
        val sellRate: String = "",
        @SerializedName("seo_url")
        @Expose
        val seoUrl: String = "",
        @SerializedName("short_desc")
        @Expose
        val shortDesc: String = "",
        @SerializedName("sold_quantity")
        @Expose
        val soldQuantity: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("thumbnail_app")
        @Expose
        val thumbnailApp: String = "",
        @SerializedName("thumbnail_web")
        @Expose
        val thumbnailWeb: String = "",
        @SerializedName("thumbs_down")
        @Expose
        val thumbsDown: String = "",
        @SerializedName("thumbs_up")
        @Expose
        val thumbsUp: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("tnc")
        @Expose
        val tnc: String = "",
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String = "",
        @SerializedName("url")
        @Expose
        val url: String = "",
        @SerializedName("use_pdf")
        @Expose
        val usePdf: Int = 0
)

data class Brand(
        @SerializedName("city_name")
        @Expose
        val cityName: String = "",
        @SerializedName("featured_image")
        @Expose
        val featuredImage: String = "",
        @SerializedName("featured_thumbnail_image")
        @Expose
        val featuredThumbnailImage: String = "",
        @SerializedName("title")
        @Expose
        val title: String = ""
)

data class Catalog(
        @SerializedName("digital_category_id")
        @Expose
        val digitalCategoryId: String = "",
        @SerializedName("digital_product_code")
        @Expose
        val digitalProductCode: String = "",
        @SerializedName("digital_product_id")
        @Expose
        val digitalProductId: String = ""
)

data class Category(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("media_url")
        @Expose
        val mediaUrl: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("url")
        @Expose
        val url: String = ""
)

data class AddressDetail(
        @SerializedName("address")
        @Expose
        val address: String = "",
        @SerializedName("city")
        @Expose
        val city: String = "",
        @SerializedName("created_at")
        @Expose
        val createdAt: String = "",
        @SerializedName("district")
        @Expose
        val district: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("latitude")
        @Expose
        val latitude: String = "",
        @SerializedName("longitude")
        @Expose
        val longitude: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("product_schedule_id")
        @Expose
        val productScheduleId: String = "",
        @SerializedName("product_schedule_package_id")
        @Expose
        val productSchedulePackageId: String = "",
        @SerializedName("state")
        @Expose
        val state: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String = ""
)

data class Form(
        @SerializedName("created_at")
        @Expose
        val createdAt: String = "",
        @SerializedName("element_type")
        @Expose
        val elementType: String = "",
        @SerializedName("options")
        @Expose
        val options: String = "",
        @SerializedName("error_message")
        @Expose
        val errorMessage: String = "",
        @SerializedName("help_text")
        @Expose
        val helpText: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("required")
        @Expose
        val required: Int = 0,
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String = "",
        @SerializedName("validator_regex")
        @Expose
        val validatorRegex: String = "",
        @SerializedName("value")
        @Expose
        var value: String = "",
        var valuePosition: String = "",
        var valueList: String = "",
        var isError: Boolean = false,
        var errorType: Int = EMPTY_TYPE
) : Serializable

data class Group(
        @SerializedName("created_at")
        @Expose
        val createdAt: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("packages")
        @Expose
        val packages: MutableList<Package> = mutableListOf(),
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("product_schedule_id")
        @Expose
        val productScheduleId: String = "",
        @SerializedName("provider_application")
        @Expose
        val providerApplication: String = "",
        @SerializedName("provider_group_id")
        @Expose
        val providerGroupId: String = "",
        @SerializedName("provider_is_full_layout")
        @Expose
        val providerIsFullLayout: String = "",
        @SerializedName("provider_meta_data")
        @Expose
        val providerMetaData: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("tnc")
        @Expose
        val tnc: String = "",
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String = ""
)

data class Outlet(
        @SerializedName("coordinates")
        @Expose
        val coordinates: String = "",
        @SerializedName("country")
        @Expose
        val country: String = "",
        @SerializedName("created_at")
        @Expose
        val createdAt: String = "",
        @SerializedName("district")
        @Expose
        val district: String = "",
        @SerializedName("gmap_address")
        @Expose
        val gmapAddress: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("is_searchable")
        @Expose
        val isSearchable: Int = 0,
        @SerializedName("location_id")
        @Expose
        val locationId: String = "",
        @SerializedName("location_status")
        @Expose
        val locationStatus: Int = 0,
        @SerializedName("meta_description")
        @Expose
        val metaDescription: String = "",
        @SerializedName("meta_keywords")
        @Expose
        val metaKeywords: String = "",
        @SerializedName("meta_title")
        @Expose
        val metaTitle: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("neighbourhood")
        @Expose
        val neighbourhood: String = "",
        @SerializedName("priority")
        @Expose
        val priority: Int = 0,
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("search_name")
        @Expose
        val searchName: String = "",
        @SerializedName("state")
        @Expose
        val state: String = "",
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String = ""
)

data class Package(
        @SerializedName("available")
        @Expose
        val available: String = "",
        @SerializedName("booked")
        @Expose
        val booked: String = "",
        @SerializedName("color")
        @Expose
        val color: String = "",
        @SerializedName("commission")
        @Expose
        val commission: String = "",
        @SerializedName("commission_type")
        @Expose
        val commissionType: String = "",
        @SerializedName("convenience_fee")
        @Expose
        val convenienceFee: String = "",
        @SerializedName("created_at")
        @Expose
        val createdAt: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("display_name")
        @Expose
        val displayName: String = "",
        @SerializedName("end_date")
        @Expose
        val endDate: String = "",
        @SerializedName("fetch_section_url")
        @Expose
        val fetchSectionUrl: String = "",
        @SerializedName("icon")
        @Expose
        val icon: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("max_qty")
        @Expose
        val maxQty: String = "",
        @SerializedName("min_qty")
        @Expose
        val minQty: String = "",
        @SerializedName("mrp")
        @Expose
        val mrp: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("price_code")
        @Expose
        val priceCode: String = "",
        @SerializedName("product_group_id")
        @Expose
        val productGroupId: String = "",
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("product_schedule_id")
        @Expose
        val productScheduleId: String = "",
        @SerializedName("provider_meta_data")
        @Expose
        val providerMetaData: String = "",
        @SerializedName("provider_schedule_id")
        @Expose
        val providerScheduleId: String = "",
        @SerializedName("provider_status")
        @Expose
        val providerStatus: String = "",
        @SerializedName("provider_ticket_id")
        @Expose
        val providerTicketId: String = "",
        @SerializedName("sales_price")
        @Expose
        val salesPrice: String = "",
        @SerializedName("schedule_status_bahasa")
        @Expose
        val scheduleStatusBahasa: String = "",
        @SerializedName("schedule_status_english")
        @Expose
        val scheduleStatusEnglish: String = "",
        @SerializedName("show_date")
        @Expose
        val showDate: String = "",
        @SerializedName("sold")
        @Expose
        val sold: String = "",
        @SerializedName("start_date")
        @Expose
        val startDate: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("tnc")
        @Expose
        val tnc: String = "",
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String = "",
        @SerializedName("venue_detail")
        @Expose
        val venueDetail: String = "",
        @SerializedName("venue_id")
        @Expose
        val venueId: String = ""
)

data class Schedules(
        @SerializedName("address_detail")
        @Expose
        val addressDetail: AddressDetail,
        @SerializedName("groups")
        @Expose
        val groups: List<Group> = emptyList(),
        @SerializedName("schedule")
        @Expose
        val schedule: Schedule
)

data class Schedule(
        @SerializedName("created_at")
        @Expose
        val createdAt: String = "",
        @SerializedName("end_date")
        @Expose
        val endDate: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("provider_meta_data")
        @Expose
        val providerMetaData: String = "",
        @SerializedName("provider_schedule_id")
        @Expose
        val providerScheduleId: String = "",
        @SerializedName("start_date")
        @Expose
        val startDate: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("tnc")
        @Expose
        val tnc: String = "",
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String = ""
)

data class Facilities(
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("icon_url")
        @Expose
        val iconUrl: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("priority")
        @Expose
        val priority: Int = 0,
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("type")
        @Expose
        val type: Int = 0
)

data class Media(
        @SerializedName("client")
        @Expose
        val client: String = "",
        @SerializedName("created_at")
        @Expose
        val createdAt: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("is_thumbnail")
        @Expose
        val isThumbnail: Int = 0,
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String = "",
        @SerializedName("url")
        @Expose
        val url: String = ""
)

data class PackageV3(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("provider_package_id")
        @Expose
        val providerPackageId: String = "",
        @SerializedName("provider_package_name")
        @Expose
        val providerPackageName: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("sales_price")
        @Expose
        val salesPrice: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("start_date")
        @Expose
        val startDate: String = "",
        @SerializedName("dates")
        @Expose
        val dates: List<String> = emptyList(),
        @SerializedName("end_date")
        @Expose
        val endDate: String = "",
        @SerializedName("package_items")
        @Expose
        val packageItems: List<PackageItem> = emptyList(),
        @SerializedName("forms_package")
        @Expose
        val formsPackages: List<Form> = emptyList(),
        var isRecommendationPackage: Boolean = false
) : EventPDPTicketModel() {

    override fun type(typeFactory: PackageTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class PackageItem(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("provider_schedule_id")
        @Expose
        val providerScheduleId: String = "",
        @SerializedName("provider_ticket_id")
        @Expose
        val providerTicketId: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("tnc")
        @Expose
        val tnc: String = "",
        @SerializedName("convenience_fee")
        @Expose
        val convenienceFee: String = "",
        @SerializedName("mrp")
        @Expose
        val mrp: String = "",
        @SerializedName("sales_price")
        @Expose
        val salesPrice: String = "",
        @SerializedName("available")
        @Expose
        val available: String = "",
        @SerializedName("provider_meta_data")
        @Expose
        val providerMetaData: String = "",
        @SerializedName("provider_status")
        @Expose
        val providerStatus: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("dates")
        @Expose
        val dates: List<String> = emptyList(),
        @SerializedName("min_qty")
        @Expose
        val minQty: String = "",
        @SerializedName("max_qty")
        @Expose
        val maxQty: String = "",
        @SerializedName("start_date")
        @Expose
        val startDate: String = "",
        @SerializedName("end_date")
        @Expose
        val endDate: String = "",
        @SerializedName("provider_custom_text")
        @Expose
        val providerCustomText: String = "",
        @SerializedName("forms_item")
        @Expose
        val formsItems: List<Form> = emptyList(),
        var isClicked: Boolean = false

)