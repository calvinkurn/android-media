package com.tokopedia.deals.pdp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DealsProductDetail(
    @SerializedName("event_product_detail_v3")
    val eventProductDetail: EventProductDetail = EventProductDetail()
)

data class EventProductDetail(
    @SerializedName("productDetailData")
    val productDetailData: ProductDetailData = ProductDetailData()
)

@Parcelize
data class ProductDetailData(
    @SerializedName("app_url")
    val appUrl: String = "",
    @SerializedName("web_url")
    val webUrl: String = "",
    @SerializedName("action_text")
    val actionText: String = "",
    @SerializedName("autocode")
    val autocode: String = "",
    @SerializedName("checkout_business_type")
    val checkoutBusinessType: Int = 0,
    @SerializedName("checkout_data_type")
    val checkoutDataType: String = "",
    @SerializedName("brand")
    val brand: Brand = Brand(),
    @SerializedName("brand_id")
    val brandId: String = "",
    @SerializedName("catalog")
    val catalog: Catalog = Catalog(),
    @SerializedName("category")
    val category: List<Category> = emptyList(),
    @SerializedName("category_id")
    val categoryId: String = "",
    @SerializedName("censor")
    val censor: String = "",
    @SerializedName("child_category_ids")
    val childCategoryIds: String = "",
    @SerializedName("city_name")
    val cityName: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("convenience_fee")
    val convenienceFee: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("custom_labels")
    val customLabels: String = "",
    @SerializedName("custom_text_1")
    val customText1: String = "",
    @SerializedName("custom_text_2")
    val customText2: String = "",
    @SerializedName("custom_text_3")
    val customText3: String = "",
    @SerializedName("custom_text_4")
    val customText4: String = "",
    @SerializedName("custom_text_5")
    val customText5: String = "",
    @SerializedName("dates")
    val dates: List<String> = emptyList(),
    @SerializedName("date_range")
    val dateRange: Boolean = false,
    @SerializedName("display_name")
    val displayName: String = "",
    @SerializedName("min_qty")
    val minQty: Int = 0,
    @SerializedName("max_qty")
    val maxQty: Int = 0,
    @SerializedName("display_tags")
    val displayTags: String = "",
    @SerializedName("duration")
    val duration: String = "",
    @SerializedName("facilities")
    val facilities: List<Facilities>? = emptyList(),
    @SerializedName("facility_group_id")
    val facilityGroupId: String = "",
    @SerializedName("form")
    val form: String = "",
    @SerializedName("forms")
    val forms: List<Form> = emptyList(),
    @SerializedName("genre")
    val genre: String = "",
    @SerializedName("has_seat_layout")
    val hasSeatLayout: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("image_app")
    val imageApp: String = "",
    @SerializedName("image_web")
    val imageWeb: String = "",
    @SerializedName("is_featured")
    val isFeatured: Int = 0,
    @SerializedName("is_food_available")
    val isFoodAvailable: Int = 0,
    @SerializedName("is_liked")
    val isLiked: Boolean = false,
    @SerializedName("is_promo")
    val isPromo: Int = 0,
    @SerializedName("is_searchable")
    val isSearchable: Int = 0,
    @SerializedName("is_top")
    val isTop: Int = 0,
    @SerializedName("likes")
    val likes: Int = 0,
    @SerializedName("location")
    val location: String = "",
    @SerializedName("long_rich_desc")
    val longRichDesc: String = "",
    @SerializedName("max_end_date")
    val maxEndDate: String = "",
    @SerializedName("max_end_time")
    val maxEndTime: String = "",
    @SerializedName("media")
    val media: List<Media> = emptyList(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("message_error")
    val messageError: String = "",
    @SerializedName("meta_description")
    val metaDescription: String = "",
    @SerializedName("meta_keywords")
    val metaKeywords: String = "",
    @SerializedName("meta_title")
    val metaTitle: String = "",
    @SerializedName("min_start_date")
    val minStartDate: String = "",
    @SerializedName("min_start_time")
    val minStartTime: String = "",
    @SerializedName("mrp")
    val mrp: String = "",
    @SerializedName("offer_text")
    val offerText: String = "",
    @SerializedName("outlets")
    val outlets: List<Outlet> = emptyList(),
    @SerializedName("packages")
    val packages: List<PackageV3> = emptyList(),
    @SerializedName("parent_id")
    val parentId: String = "",
    @SerializedName("priority")
    val priority: String = "",
    @SerializedName("promotion_text")
    val promotionText: String = "",
    @SerializedName("provider_id")
    val providerId: String = "",
    @SerializedName("provider_product_code")
    val providerProductCode: String = "",
    @SerializedName("provider_product_id")
    val providerProductId: String = "",
    @SerializedName("provider_product_name")
    val providerProductName: String = "",
    @SerializedName("quantity")
    val quantity: String = "",
    @SerializedName("rating")
    val rating: String = "",
    @SerializedName("recommendation_url")
    val recommendationUrl: String = "",
    @SerializedName("redirect")
    val redirect: Int = 0,
    @SerializedName("remaining_sale_time")
    val remainingSaleTime: String = "",
    @SerializedName("sale_end_date")
    val saleEndDate: String = "",
    @SerializedName("sale_end_time")
    val saleEndTime: String = "",
    @SerializedName("sale_start_date")
    val saleStartDate: String = "",
    @SerializedName("sale_start_time")
    val saleStartTime: String = "",
    @SerializedName("sales_price")
    val salesPrice: String = "",
    @SerializedName("salient_features")
    val salientFeatures: String = "",
    @SerializedName("saving")
    val saving: String = "",
    @SerializedName("saving_percentage")
    val savingPercentage: String = "",
    @SerializedName("schedules")
    val schedules: List<Schedules> = emptyList(),
    @SerializedName("search_tags")
    val searchTags: String = "",
    @SerializedName("seat_chart_type")
    val seatChartType: String = "",
    @SerializedName("seatmap_image")
    val seatmapImage: String = "",
    @SerializedName("sell_rate")
    val sellRate: String = "",
    @SerializedName("seo_url")
    val seoUrl: String = "",
    @SerializedName("short_desc")
    val shortDesc: String = "",
    @SerializedName("sold_quantity")
    val soldQuantity: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("thumbnail_app")
    val thumbnailApp: String = "",
    @SerializedName("thumbnail_web")
    val thumbnailWeb: String = "",
    @SerializedName("thumbs_down")
    val thumbsDown: String = "",
    @SerializedName("thumbs_up")
    val thumbsUp: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("tnc")
    val tnc: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("use_pdf")
    val usePdf: Int = 0
) : Parcelable

@Parcelize
data class Brand(
    @SerializedName("city_name")
    val cityName: String = "",
    @SerializedName("featured_image")
    val featuredImage: String = "",
    @SerializedName("featured_thumbnail_image")
    val featuredThumbnailImage: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("seo_url")
    val seoUrl: String = ""
) : Parcelable

@Parcelize
data class Catalog(
    @SerializedName("digital_category_id")
    val digitalCategoryId: String = "",
    @SerializedName("digital_product_code")
    val digitalProductCode: String = "",
    @SerializedName("digital_product_id")
    val digitalProductId: String = ""
) : Parcelable

@Parcelize
data class Category(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("media_url")
    val mediaUrl: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = ""
) : Parcelable

@Parcelize
data class AddressDetail(
    @SerializedName("address")
    val address: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("district")
    val district: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("product_schedule_id")
    val productScheduleId: String = "",
    @SerializedName("product_schedule_package_id")
    val productSchedulePackageId: String = "",
    @SerializedName("state")
    val state: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("updated_at")
    val updatedAt: String = ""
) : Parcelable

@Parcelize
data class Form(
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("element_type")
    val elementType: String = "",
    @SerializedName("options")
    val options: String = "",
    @SerializedName("error_message")
    val errorMessage: String = "",
    @SerializedName("help_text")
    val helpText: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("required")
    val required: Int = 0,
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = "",
    @SerializedName("validator_regex")
    val validatorRegex: String = "",
    @SerializedName("value")
    var value: String = ""
) : Parcelable

@Parcelize
data class Group(
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("packages")
    val packages: List<Package> = emptyList(),
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("product_schedule_id")
    val productScheduleId: String = "",
    @SerializedName("provider_application")
    val providerApplication: String = "",
    @SerializedName("provider_group_id")
    val providerGroupId: String = "",
    @SerializedName("provider_is_full_layout")
    val providerIsFullLayout: String = "",
    @SerializedName("provider_meta_data")
    val providerMetaData: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("tnc")
    val tnc: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
) : Parcelable

@Parcelize
data class Outlet(
    @SerializedName("coordinates")
    val coordinates: String = "",
    @SerializedName("country")
    val country: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("district")
    val district: String = "",
    @SerializedName("gmap_address")
    val gmapAddress: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("is_searchable")
    val isSearchable: Int = 0,
    @SerializedName("location_id")
    val locationId: String = "",
    @SerializedName("location_status")
    val locationStatus: Int = 0,
    @SerializedName("meta_description")
    val metaDescription: String = "",
    @SerializedName("meta_keywords")
    val metaKeywords: String = "",
    @SerializedName("meta_title")
    val metaTitle: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("neighbourhood")
    val neighbourhood: String = "",
    @SerializedName("priority")
    val priority: Int = 0,
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("search_name")
    val searchName: String = "",
    @SerializedName("state")
    val state: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
) : Parcelable

@Parcelize
data class Package(
    @SerializedName("available")
    val available: String = "",
    @SerializedName("booked")
    val booked: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("commission")
    val commission: String = "",
    @SerializedName("commission_type")
    val commissionType: String = "",
    @SerializedName("convenience_fee")
    val convenienceFee: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("display_name")
    val displayName: String = "",
    @SerializedName("end_date")
    val endDate: String = "",
    @SerializedName("fetch_section_url")
    val fetchSectionUrl: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("max_qty")
    val maxQty: String = "",
    @SerializedName("min_qty")
    val minQty: String = "",
    @SerializedName("mrp")
    val mrp: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("price_code")
    val priceCode: String = "",
    @SerializedName("product_group_id")
    val productGroupId: String = "",
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("product_schedule_id")
    val productScheduleId: String = "",
    @SerializedName("provider_meta_data")
    val providerMetaData: String = "",
    @SerializedName("provider_schedule_id")
    val providerScheduleId: String = "",
    @SerializedName("provider_status")
    val providerStatus: String = "",
    @SerializedName("provider_ticket_id")
    val providerTicketId: String = "",
    @SerializedName("sales_price")
    val salesPrice: String = "",
    @SerializedName("schedule_status_bahasa")
    val scheduleStatusBahasa: String = "",
    @SerializedName("schedule_status_english")
    val scheduleStatusEnglish: String = "",
    @SerializedName("show_date")
    val showDate: String = "",
    @SerializedName("sold")
    val sold: String = "",
    @SerializedName("start_date")
    val startDate: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("tnc")
    val tnc: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = "",
    @SerializedName("venue_detail")
    val venueDetail: String = "",
    @SerializedName("venue_id")
    val venueId: String = ""
) : Parcelable

@Parcelize
data class Schedules(
    @SerializedName("address_detail")
    val addressDetail: AddressDetail,
    @SerializedName("groups")
    val groups: List<Group> = emptyList(),
    @SerializedName("schedule")
    val schedule: Schedule
) : Parcelable

@Parcelize
data class Schedule(
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("end_date")
    val endDate: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("provider_meta_data")
    val providerMetaData: String = "",
    @SerializedName("provider_schedule_id")
    val providerScheduleId: String = "",
    @SerializedName("start_date")
    val startDate: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("tnc")
    val tnc: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
) : Parcelable

@Parcelize
data class Facilities(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("priority")
    val priority: Int = 0,
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: Int = 0
) : Parcelable

@Parcelize
data class Media(
    @SerializedName("client")
    val client: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("is_thumbnail")
    val isThumbnail: Int = 0,
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = "",
    @SerializedName("url")
    val url: String = ""
) : Parcelable

@Parcelize
data class PackageV3(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("provider_package_id")
    val providerPackageId: String = "",
    @SerializedName("provider_package_name")
    val providerPackageName: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("sales_price")
    val salesPrice: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("start_date")
    val startDate: String = "",
    @SerializedName("dates")
    val dates: List<String> = emptyList(),
    @SerializedName("end_date")
    val endDate: String = "",
    @SerializedName("package_items")
    val packageItems: List<PackageItem> = emptyList(),
    @SerializedName("forms_package")
    val formsPackages: List<Form> = emptyList(),
    var isRecommendationPackage: Boolean = false
) : Parcelable

@Parcelize
data class PackageItem(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("provider_schedule_id")
    val providerScheduleId: String = "",
    @SerializedName("provider_ticket_id")
    val providerTicketId: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("tnc")
    val tnc: String = "",
    @SerializedName("convenience_fee")
    val convenienceFee: String = "",
    @SerializedName("mrp")
    val mrp: String = "",
    @SerializedName("sales_price")
    val salesPrice: String = "",
    @SerializedName("available")
    val available: String = "",
    @SerializedName("provider_meta_data")
    val providerMetaData: String = "",
    @SerializedName("provider_status")
    val providerStatus: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("dates")
    val dates: List<String> = emptyList(),
    @SerializedName("min_qty")
    val minQty: String = "",
    @SerializedName("max_qty")
    val maxQty: String = "",
    @SerializedName("start_date")
    val startDate: String = "",
    @SerializedName("end_date")
    val endDate: String = "",
    @SerializedName("provider_custom_text")
    val providerCustomText: String = "",
    @SerializedName("forms_item")
    val formsItems: List<Form> = emptyList()
) : Parcelable
