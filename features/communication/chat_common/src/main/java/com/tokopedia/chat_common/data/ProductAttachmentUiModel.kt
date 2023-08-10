package com.tokopedia.chat_common.data

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.data.ProductAttachmentUiModel.Builder
import com.tokopedia.chat_common.domain.pojo.productattachment.*
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero

/**
 * Primary constructor, use [Builder] class to create this instance.
 */
open class ProductAttachmentUiModel protected constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<BaseChatTypeFactory>, DeferredAttachment {

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String get() = attachmentId

    var productId: String = builder.productId
        private set
    var productName: String = builder.productName
        private set
    var productPrice: String = builder.productPrice
        private set
    var dateTimeInMilis: Long = builder.replyTime.toLongOrZero()
        private set
    var productUrl: String = builder.productUrl
        private set
    var productImage: String = builder.productImage
        private set
    var canShowFooter: Boolean = builder.canShowFooter
        private set
    var priceNumber: Double = builder.priceNumber
        private set
    var category: String = builder.category
        private set
    var dropPercentage: String = builder.dropPercentage
        private set
    var priceBefore: String = builder.priceBefore
        private set
    var shopId: String = builder.shopId
    var freeShipping: FreeShipping = builder.freeShipping
    var playStoreData: PlayStoreData = builder.playStoreData
    var categoryId: String = builder.categoryId
    var minOrder: Int = builder.minOrder
    var variants: List<AttachmentVariant> = builder.variants
    var remainingStock: Int = builder.remainingStock
    var status: Int = builder.status
    var wishList: Boolean = builder.wishList
    var rating: TopchatProductRating = builder.rating
    var isPreOrder: Boolean = builder.isPreOrder
    var images: List<String> = builder.images
        get() {
            return if (field.isNotEmpty()) {
                field
            } else {
                listOf(productImage)
            }
        }

    val hasDiscount: Boolean
        get() {
            return priceBefore.isNotEmpty() && dropPercentage.isNotEmpty() &&
                priceBefore != productPrice && dropPercentage != "0"
        }
    val stringBlastId: String get() = blastId
    var campaignId: String = builder.campaignId
    var isFulfillment: Boolean = builder.isFulfillment
    var urlTokocabang: String = builder.urlTokoCabang
    var descTokoCabang: String = builder.descTokoCabang
    var parentId: String = "0"

    var colorVariantId: String = ""
    var colorVariant: String = ""
    var colorHexVariant: String = ""
    var sizeVariantId: String = ""
    var sizeVariant: String = ""
    var isSupportVariant: Boolean = builder.isSupportVariant
    var cartId: String = ""

    var isUpcomingCampaign: Boolean = builder.isUpcomingCampaign
    var locationStock: LocationStock = builder.locationStock
    var androidUrl: String = builder.androidUrl
    var iosUrl: String = builder.iosUrl

    init {
        if (variants.isNotEmpty()) {
            setupVariantsField()
        }
        if (!builder.needSync) {
            finishLoading()
        }
    }

    protected fun updateCanShowFooter(canShowFooter: Boolean) {
        this.canShowFooter = canShowFooter
    }

    override fun updateData(attribute: Any?) {
        if (attribute is ProductAttachmentAttributes) {
            productId = attribute.productId
            productName = attribute.productProfile.name
            productPrice = attribute.productProfile.price
            productUrl = attribute.productProfile.url
            productImage = attribute.productProfile.imageUrl
            priceNumber = attribute.productProfile.priceInt
            category = attribute.productProfile.category
            variants = attribute.productProfile.variant ?: emptyList()
            dropPercentage = attribute.productProfile.dropPercentage
            priceBefore = attribute.productProfile.priceBefore
            shopId = attribute.productProfile.shopId
            freeShipping = attribute.productProfile.freeShipping
            categoryId = attribute.productProfile.categoryId
            playStoreData = attribute.productProfile.playStoreData
            minOrder = attribute.productProfile.minOrder
            remainingStock = attribute.productProfile.remainingStock
            status = attribute.productProfile.status
            wishList = attribute.productProfile.wishList
            images = attribute.productProfile.images
            rating = attribute.productProfile.rating
            isPreOrder = attribute.productProfile.isPreOrder
            campaignId = attribute.productProfile.campaignId
            isFulfillment = attribute.productProfile.isFulFillment
            urlTokocabang = attribute.productProfile.urlTokocabang
            descTokoCabang = attribute.productProfile.descTokocabang
            if (variants.isNotEmpty()) {
                setupVariantsField()
            }
            this.isLoading = false
            parentId = attribute.productProfile.parentId
            isSupportVariant = attribute.productProfile.isSupportVariant
            isUpcomingCampaign = attribute.productProfile.isUpcomingCampaign
            locationStock = attribute.productProfile.locationStock
            androidUrl = attribute.productProfile.androidUrl
            iosUrl = attribute.productProfile.iosUrl
        }
    }

    override fun syncError() {
        this.isLoading = false
        this.isError = true
    }

    private fun setupVariantsField() {
        for (variant in variants) {
            val variantOption = variant.options
            if (variantOption.isColor()) {
                variantOption.id?.let {
                    colorVariantId = it
                }
                variantOption.value?.let {
                    colorVariant = it
                }
                variantOption.hex?.let {
                    colorHexVariant = it
                }
            } else {
                variantOption.id?.let {
                    sizeVariantId = it
                }
                variantOption.value?.let {
                    sizeVariant = it
                }
            }
        }
    }

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun hasFreeShipping(): Boolean {
        return freeShipping.isActive && freeShipping.imageUrl.isNotEmpty()
    }

    fun getFreeShippingImageUrl(): String {
        return freeShipping.imageUrl
    }

    fun hasVariant(): Boolean {
        return variants.isNotEmpty()
    }

    fun doesNotHaveVariant(): Boolean {
        return variants.isEmpty()
    }

    fun hasColorVariant(): Boolean {
        return colorVariantId.isNotEmpty()
    }

    fun hasSizeVariant(): Boolean {
        return sizeVariant.isNotEmpty()
    }

    fun hasEmptyStock(): Boolean {
        return status != statusActive || remainingStock == 0
    }

    fun isWishListed(): Boolean {
        return wishList
    }

    fun isProductDeleted(): Boolean {
        return status == statusDeleted
    }

    fun getStringProductId(): String {
        return productId.toString()
    }

    fun getIdString(): String {
        return productId.toString()
    }

    final override fun finishLoading() {
        this.isLoading = false
        this.isError = false
    }

    fun getAtcDimension40(sourcePage: String): String {
        return when (sourcePage) {
            ApplinkConst.Chat.SOURCE_CHAT_SEARCH -> "/chat - search chat"
            else -> getField()
        }
    }

    private fun getField(): String {
        return if (blastId.isNotEmpty() && blastId != "0") {
            "/broadcast"
        } else {
            "/chat"
        }
    }

    private fun getAttachmentSource(): String {
        val blastIdInt = blastId.toIntOrZero()
        return if (blastIdInt < Int.ZERO) {
            "drop alert"
        } else if (blastIdInt > Int.ZERO) {
            "broadcast"
        } else {
            "chat"
        }
    }

    fun hasReview(): Boolean {
        return rating.count > 0
    }

    fun fromBroadcast(): Boolean {
        return (blastId.isNotEmpty() && blastId != "0")
    }

    fun isProductCampaign(): Boolean {
        return campaignId != "0"
    }

    fun getProductSource(): String {
        return if (fromBroadcast()) {
            "broadcast"
        } else {
            "buyer attached"
        }
    }

    fun getEventLabelImpression(amISeller: Boolean): String {
        val role = if (amISeller) {
            "seller"
        } else {
            "buyer"
        }
        val isCampaign = if (isProductCampaign()) {
            "campaign"
        } else {
            "notcampaign"
        }

        return "$role - ${getAttachmentSource()} - $blastId - $isCampaign"
    }

    // not a variant, not product campaign, not broadcast, & not pre-order
    fun isEligibleOCC(): Boolean {
        return !isSupportVariant && !isProductCampaign() && !fromBroadcast() && !isPreOrder
    }

    fun generateVariantRequest(): JsonElement? {
        val list = JsonArray()

        if (hasColorVariant()) {
            val color = JsonObject()
            val colorOption = JsonObject()
            colorOption.addProperty("id", colorVariantId.toLongOrNull() ?: 0)
            colorOption.addProperty("value", colorVariant)
            colorOption.addProperty("hex", colorHexVariant)
            color.add("option", colorOption)
            list.add(color)
        }

        if (hasSizeVariant()) {
            val size = JsonObject()
            val sizeOption = JsonObject()
            sizeOption.addProperty("id", sizeVariantId.toLongOrNull() ?: 0)
            sizeOption.addProperty("value", sizeVariant)
            size.add("option", sizeOption)
            list.add(size)
        }

        return list
    }

    companion object {
        const val statusDeleted = 0
        const val statusActive = 1
        const val statusWarehouse = 3

        const val NO_PRODUCT_ID = "0"
    }

    open class Builder : SendableUiModel.Builder<Builder, ProductAttachmentUiModel>() {

        internal var productId: String = "0"
        internal var productName: String = ""
        internal var productPrice: String = ""
        internal var dateTimeInMilis: Long = 0
        internal var productUrl: String = ""
        internal var productImage: String = ""
        internal var canShowFooter: Boolean = false
        internal var priceNumber: Double = 0.0
        internal var category: String = ""
        internal var dropPercentage: String = ""
        internal var priceBefore: String = ""
        internal var shopId: String = "0"
        internal var freeShipping: FreeShipping = FreeShipping()
        internal var categoryId: String = "0"
        internal var playStoreData: PlayStoreData = PlayStoreData()
        internal var minOrder: Int = 1
        internal var remainingStock: Int = 0
        internal var status: Int = 0
        internal var rating: TopchatProductRating = TopchatProductRating()
        internal var variants: List<AttachmentVariant> = emptyList()
        internal var wishList: Boolean = false
        internal var isPreOrder: Boolean = false
        internal var images: List<String> = emptyList()
        internal var needSync: Boolean = true
        internal var isSupportVariant: Boolean = false
        internal var campaignId: String = "0"
        internal var locationStock: LocationStock = LocationStock()
        internal var isUpcomingCampaign: Boolean = false
        internal var isFulfillment: Boolean = false
        internal var urlTokoCabang: String = ""
        internal var descTokoCabang: String = ""
        internal var androidUrl: String = ""
        internal var iosUrl: String = ""

        fun withProductAttributesResponse(product: ProductAttachmentAttributes): Builder {
            withProductId(product.productId)
            withProductName(product.productProfile.name)
            withProductPrice(product.productProfile.price)
            withDateTimeInMilis(replyTime.toLongOrZero())
            withProductUrl(product.productProfile.url)
            withProductImage(product.productProfile.imageUrl)
            withPriceInt(product.productProfile.priceInt)
            withCategory(product.productProfile.category)
            withVariants(product.productProfile.variant ?: emptyList())
            withDropPercentage(product.productProfile.dropPercentage)
            withPriceBefore(product.productProfile.priceBefore)
            withShopId(product.productProfile.shopId)
            withFreeShipping(product.productProfile.freeShipping)
            withCategoryId(product.productProfile.categoryId)
            withPlayStoreData(product.productProfile.playStoreData)
            withMinOrder(product.productProfile.minOrder)
            withRemainingStock(product.productProfile.remainingStock)
            withStatus(product.productProfile.status)
            withWishList(product.productProfile.wishList)
            withImages(product.productProfile.images)
            withRating(product.productProfile.rating)
            withIsSupportVariant(product.productProfile.isSupportVariant)
            withCampaignId(product.productProfile.campaignId)
            withIsPreOrder(product.productProfile.isPreOrder)
            withLocationStock(product.productProfile.locationStock)
            withIsUpcomingCampaign(product.productProfile.isUpcomingCampaign)
            withIsFulfillment(product.productProfile.isFulFillment)
            withUrlTokoCabang(product.productProfile.urlTokocabang)
            withDescTokoCabang(product.productProfile.descTokocabang)
            withAndroidUrl(product.productProfile.androidUrl)
            withIOSUrl(product.productProfile.iosUrl)
            return self()
        }

        fun withProductId(productId: String): Builder {
            this.productId = productId
            return self()
        }

        fun withProductName(productName: String): Builder {
            this.productName = productName
            return self()
        }

        fun withProductPrice(productPrice: String): Builder {
            this.productPrice = productPrice
            return self()
        }

        fun withDateTimeInMilis(dateTimeInMilis: Long): Builder {
            this.dateTimeInMilis = dateTimeInMilis
            return self()
        }

        fun withProductUrl(productUrl: String): Builder {
            this.productUrl = productUrl
            return self()
        }

        fun withProductImage(productImage: String): Builder {
            this.productImage = productImage
            return self()
        }

        fun withCanShowFooter(canShowFooter: Boolean): Builder {
            this.canShowFooter = canShowFooter
            return self()
        }

        fun withPriceInt(priceInt: Double): Builder {
            this.priceNumber = priceInt
            return self()
        }

        fun withCategory(category: String): Builder {
            this.category = category
            return self()
        }

        fun withDropPercentage(dropPercentage: String): Builder {
            this.dropPercentage = dropPercentage
            return self()
        }

        fun withPriceBefore(priceBefore: String): Builder {
            this.priceBefore = priceBefore
            return self()
        }

        fun withShopId(shopId: String): Builder {
            this.shopId = shopId
            return self()
        }

        fun withFreeShipping(freeShipping: FreeShipping): Builder {
            this.freeShipping = freeShipping
            return self()
        }

        fun withCategoryId(categoryId: String): Builder {
            this.categoryId = categoryId
            return self()
        }

        fun withPlayStoreData(playStoreData: PlayStoreData): Builder {
            this.playStoreData = playStoreData
            return self()
        }

        fun withMinOrder(minOrder: Int): Builder {
            this.minOrder = minOrder
            return self()
        }

        fun withRemainingStock(remainingStock: Int): Builder {
            this.remainingStock = remainingStock
            return self()
        }

        fun withStatus(status: Int): Builder {
            this.status = status
            return self()
        }

        fun withRating(rating: TopchatProductRating): Builder {
            this.rating = rating
            return self()
        }

        fun withVariants(variants: List<AttachmentVariant>?): Builder {
            this.variants = variants ?: emptyList()
            return self()
        }

        fun withWishList(wishList: Boolean): Builder {
            this.wishList = wishList
            return self()
        }

        fun withIsPreOrder(isPreOrder: Boolean): Builder {
            this.isPreOrder = isPreOrder
            return self()
        }

        fun withImages(images: List<String>): Builder {
            this.images = images
            return self()
        }

        fun withNeedSync(needSync: Boolean): Builder {
            this.needSync = needSync
            return self()
        }

        fun withIsSupportVariant(isSupportVariant: Boolean): Builder {
            this.isSupportVariant = isSupportVariant
            return self()
        }

        fun withCampaignId(campaignId: String): Builder {
            this.campaignId = campaignId
            return self()
        }

        fun withLocationStock(locationStock: LocationStock): Builder {
            this.locationStock = locationStock
            return self()
        }

        fun withIsUpcomingCampaign(isUpcomingCampaign: Boolean): Builder {
            this.isUpcomingCampaign = isUpcomingCampaign
            return self()
        }

        fun withIsFulfillment(isFulfillment: Boolean): Builder {
            this.isFulfillment = isFulfillment
            return self()
        }

        fun withUrlTokoCabang(urlTokoCabang: String): Builder {
            this.urlTokoCabang = urlTokoCabang
            return self()
        }

        fun withDescTokoCabang(descTokoCabang: String): Builder {
            this.descTokoCabang = descTokoCabang
            return self()
        }

        fun withAndroidUrl(androidUrl: String): Builder {
            this.androidUrl = androidUrl
            return self()
        }

        fun withIOSUrl(iosUrl: String): Builder {
            this.iosUrl = iosUrl
            return self()
        }

        override fun build(): ProductAttachmentUiModel {
            return ProductAttachmentUiModel(this)
        }
    }
}
