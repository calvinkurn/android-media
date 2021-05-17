package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.utils.safeCastRupiahToInt
import com.tokopedia.utils.text.currency.StringUtils

class ProductItemDataView() : ImpressHolder(), Parcelable, Visitable<ProductListTypeFactory> {
    var productID: String = ""
    var warehouseID: String = ""
    var productName: String = ""
    var imageUrl: String = ""
    var imageUrl300: String = ""
    var imageUrl700: String = ""
    var ratingString: String = ""
    var price: String = ""
    var priceInt = 0
    var priceRange: String? = null
    var shopID: String = ""
    var shopName: String = ""
    var shopCity: String = ""
    var shopUrl: String = ""
    var isWishlisted = false
    var isWishlistButtonEnabled = true
    var badgesList: List<BadgeItemDataView>? = null
    var position = 0
    var originalPrice = ""
    var discountPercentage = 0
    var categoryID = 0
    var categoryName: String? = ""
    var categoryBreadcrumb: String? = ""
    var isTopAds = false
    var isOrganicAds = false
    var topadsImpressionUrl: String? = null
    var topadsClickUrl: String? = null
    var topadsWishlistUrl: String? = null
    var topadsClickShopUrl: String? = null
    var isNew = false
    var labelGroupList: List<LabelGroupDataView>? = mutableListOf()
    var labelGroupVariantList: List<LabelGroupVariantDataView> = mutableListOf()
    var freeOngkirDataView = FreeOngkirDataView()
    var boosterList = ""
    var sourceEngine = ""
    var minOrder = 1
    var isShopOfficialStore = false
    var isShopPowerMerchant = false
    var productUrl = ""
    var pageTitle: String? = null
    val isAds: Boolean
        get() = isTopAds || isOrganicAds
    val pageNumber: Int
        get() = (position - 1) / SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt() + 1
    val categoryString: String?
        get() = if (StringUtils.isBlank(categoryName)) categoryBreadcrumb else categoryName

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    fun getProductAsObjectDataLayer(filterSortParams: String, dimension90: String): Any {
        return DataLayer.mapOf(
                "name", productName,
                "id", productID,
                "price", priceInt,
                "brand", "none / other",
                "category", categoryBreadcrumb,
                "variant", "none / other",
                "list", actionFieldString,
                "position", position.toString(),
                "shopId", shopID,
                "dimension61", if (filterSortParams.isEmpty()) "none / other" else filterSortParams,
                "dimension83", getFreeOngkirDataLayer(),
                "dimension87", "search result",
                "dimension88", "search - product",
                "dimension90", dimension90,
                "dimension96", boosterList,
                "dimension99", System.currentTimeMillis(),
                "dimension100", sourceEngine
        )
    }

    private fun getFreeOngkirDataLayer(): String {
        val isFreeOngkirActive = isFreeOngkirActive
        val hasLabelGroupFulfillment = hasLabelGroupFulfillment

        return when {
            isFreeOngkirActive && hasLabelGroupFulfillment -> "bebas ongkir extra"
            isFreeOngkirActive && !hasLabelGroupFulfillment -> "bebas ongkir"
            else -> "none / other"
        }
    }

    private val isFreeOngkirActive: Boolean
        get() = freeOngkirDataView.isActive

    val hasLabelGroupFulfillment: Boolean
        get() = labelGroupList?.any { it.position == ProductCardLabel.LABEL_FULFILLMENT } == true

    private val actionFieldString: String
        get() = String.format(ACTION_FIELD, if (isOrganicAds) ORGANIC_ADS else ORGANIC)

    fun getProductAsATCObjectDataLayer(cartId: String): Any = DataLayer.mapOf(
                "name", productName,
                "id", productID,
                "price", safeCastRupiahToInt(price).toString(),
                "brand", "none / other",
                "category", categoryBreadcrumb,
                "variant", "none / other",
                "quantity", minOrder,
                "shop_id", shopID,
                "shop_type", shopType,
                "shop_name", shopName,
                "category_id", categoryID,
                "dimension82", cartId
        )

    fun getProductAsShopPageObjectDataLayer(): Any = DataLayer.mapOf(
                "id", shopID,
                "name", String.format(ACTION_FIELD, if (isAds) ORGANIC_ADS else ORGANIC),
                "creative", shopName,
                "creative_url", shopUrl,
                "position", position.toString(),
                "category", categoryBreadcrumb,
                "promo_id", "none / other",
                "promo_code", "none / other"
        )

    private val shopType: String
        get() = when {
            isShopOfficialStore -> "official_store"
            isShopPowerMerchant -> "gold_merchant"
            else -> "reguler"
        }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(productID)
        dest.writeString(warehouseID)
        dest.writeString(productName)
        dest.writeString(imageUrl)
        dest.writeString(imageUrl700)
        dest.writeString(ratingString)
        dest.writeString(price)
        dest.writeString(priceRange)
        dest.writeString(shopID)
        dest.writeString(shopName)
        dest.writeString(shopCity)
        dest.writeByte(if (isWishlisted) 1.toByte() else 0.toByte())
        dest.writeByte(if (isWishlistButtonEnabled) 1.toByte() else 0.toByte())
        dest.writeTypedList(badgesList)
        dest.writeInt(position)
        dest.writeString(originalPrice)
        dest.writeInt(discountPercentage)
        dest.writeInt(categoryID)
        dest.writeString(categoryName)
        dest.writeString(categoryBreadcrumb)
        dest.writeByte(if (isTopAds) 1.toByte() else 0.toByte())
        dest.writeByte(if (isNew) 1.toByte() else 0.toByte())
        dest.writeString(topadsImpressionUrl)
        dest.writeString(topadsClickUrl)
        dest.writeString(topadsWishlistUrl)
        dest.writeTypedList(labelGroupList)
        dest.writeParcelable(freeOngkirDataView, flags)
    }

    private constructor(parcel: Parcel): this() {
        productID = parcel.readString() ?: ""
        warehouseID = parcel.readString() ?: ""
        productName = parcel.readString() ?: ""
        imageUrl = parcel.readString() ?: ""
        imageUrl700 = parcel.readString() ?: ""
        ratingString = parcel.readString() ?: ""
        price = parcel.readString() ?: ""
        priceRange = parcel.readString()
        shopID = parcel.readString() ?: ""
        shopName = parcel.readString() ?: ""
        shopCity = parcel.readString() ?: ""
        isWishlisted = parcel.readByte().toInt() != 0
        isWishlistButtonEnabled = parcel.readByte().toInt() != 0
        badgesList = parcel.createTypedArrayList(BadgeItemDataView)
        position = parcel.readInt()
        originalPrice = parcel.readString() ?: ""
        discountPercentage = parcel.readInt()
        categoryID = parcel.readInt()
        categoryName = parcel.readString()
        categoryBreadcrumb = parcel.readString()
        isTopAds = parcel.readByte().toInt() != 0
        isNew = parcel.readByte().toInt() != 0
        topadsImpressionUrl = parcel.readString()
        topadsClickUrl = parcel.readString()
        topadsWishlistUrl = parcel.readString()
        labelGroupList = parcel.createTypedArrayList(LabelGroupDataView)
        freeOngkirDataView = parcel.readParcelable(FreeOngkirDataView::class.java.classLoader) ?: FreeOngkirDataView()
    }

    companion object {
        private const val ACTION_FIELD = "/searchproduct - %s"
        private const val ORGANIC = "organic"
        private const val ORGANIC_ADS = "organic ads"

        @JvmField
        val CREATOR: Parcelable.Creator<ProductItemDataView> = object : Parcelable.Creator<ProductItemDataView> {
            override fun createFromParcel(source: Parcel): ProductItemDataView {
                return ProductItemDataView(source)
            }

            override fun newArray(size: Int): Array<ProductItemDataView?> {
                return arrayOfNulls(size)
            }
        }
    }
}