package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.domain.pojo.productattachment.FreeShipping
import com.tokopedia.chat_common.domain.pojo.productattachment.PlayStoreData
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductAttachmentAttributes
import com.tokopedia.chat_common.domain.pojo.productattachment.TopchatProductRating
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory
import java.util.*

/**
 * @author by nisie on 5/14/18.
 */
open class ProductAttachmentViewModel : SendableViewModel,
        Visitable<BaseChatTypeFactory>,
        DeferredAttachment {

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String get() = attachmentId

    var productId: Int = 0
        private set
    var productName: String = ""
        private set
    var productPrice: String = ""
        private set
    var dateTimeInMilis: Long = 0
        private set
    var productUrl: String = ""
        private set
    var productImage: String = ""
        private set
    var canShowFooter: Boolean = false
        private set
    var blastId: Int = 0
        private set
    var priceInt: Int = 0
        private set
    var category: String = ""
        private set
    var dropPercentage: String = ""
        private set
    var priceBefore: String = ""
        private set
    var shopId: Int = 0
    var freeShipping: FreeShipping = FreeShipping()
    var playStoreData: PlayStoreData = PlayStoreData()
    var categoryId: Int = 0
    var minOrder: Int = 1
    var variants: List<AttachmentVariant> = emptyList()
    var colorVariantId: String = ""
    var colorVariant: String = ""
    var colorHexVariant: String = ""
    var sizeVariantId: String = ""
    var sizeVariant: String = ""
    var remainingStock: Int = 1
    var status: Int = 0
    var wishList: Boolean = false
    var rating: TopchatProductRating = TopchatProductRating()
    var images: List<String> = emptyList()
        get() {
            return if (field.isNotEmpty()) {
                field
            } else {
                listOf(productImage)
            }
        }
    val hasDiscount: Boolean
        get() {
            return priceBefore.isNotEmpty() && dropPercentage.isNotEmpty()
        }
    val stringBlastId: String get() = blastId.toString()

    override fun updateData(attribute: Any?) {
        if (attribute is ProductAttachmentAttributes) {
            productId = attribute.productId
            productName = attribute.productProfile.name
            productPrice = attribute.productProfile.price
            productUrl = attribute.productProfile.url
            productImage = attribute.productProfile.imageUrl
            priceInt = attribute.productProfile.priceInt
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
            if (variants.isNotEmpty()) {
                setupVariantsField()
            }
            this.isLoading = false
        }
    }

    override fun syncError() {
        this.isLoading = false
        this.isError = true
    }

    constructor(
            messageId: String, fromUid: String, from: String, fromRole: String,
            attachmentId: String, attachmentType: String, replyTime: String, startTime: String,
            isRead: Boolean, isDummy: Boolean, isSender: Boolean, message: String,
            source: String
    ) : super(
            messageId, fromUid, from, fromRole,
            attachmentId, attachmentType, replyTime, startTime,
            isRead, isDummy, isSender, message,
            source
    ) {
    }

    /**
     * Constructor for API response.
     *
     * @param messageId      message Id
     * @param fromUid        user id of sender
     * @param from           username of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     * [AttachmentType] types
     * @param replyTime      replytime in unixtime
     * @param isRead         is message already read by opponent
     * @param productId      product id
     * @param productName    product name
     * @param productPrice   product price
     * @param productUrl     product url
     * @param productImage   product image url
     */
    constructor(
            messageId: String, fromUid: String, from: String, fromRole: String,
            attachmentId: String, attachmentType: String, replyTime: String, isRead: Boolean,
            productId: Int, productName: String, productPrice: String, productUrl: String,
            productImage: String, isSender: Boolean, message: String, canShowFooter: Boolean,
            blastId: Int, productPriceInt: Int, category: String, variants: List<AttachmentVariant>,
            dropPercentage: String, priceBefore: String, shopId: Int, freeShipping: FreeShipping,
            categoryId: Int, playStoreData: PlayStoreData, minOrder: Int, remainingStock: Int,
            status: Int, wishList: Boolean, images: List<String>, source: String,
            rating: TopchatProductRating
    ) : super(
            messageId, fromUid, from, fromRole,
            attachmentId, attachmentType, replyTime, "",
            isRead, false, isSender, message,
            source
    ) {
        this.productId = productId
        this.productName = productName
        this.productPrice = productPrice
        this.dateTimeInMilis = java.lang.Long.parseLong(replyTime)
        this.productUrl = productUrl
        this.productImage = productImage
        this.canShowFooter = canShowFooter
        this.blastId = blastId
        this.priceInt = productPriceInt
        this.category = category
        this.dropPercentage = dropPercentage
        this.priceBefore = priceBefore
        this.shopId = shopId
        this.freeShipping = freeShipping
        this.categoryId = categoryId
        this.playStoreData = playStoreData
        this.minOrder = minOrder
        this.remainingStock = remainingStock
        this.status = status
        this.rating = rating
        if (variants.isNotEmpty()) {
            this.variants = variants
            setupVariantsField()
        }
        this.wishList = wishList
        this.images = images
    }

    /**
     * Constructor for WebSocket.
     *
     * @param messageId      message Id
     * @param fromUid        user id of sender
     * @param from           username of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     * [AttachmentType] types
     * @param replyTime      replytime in unixtime
     * @param productId      product id
     * @param productName    product name
     * @param productPrice   product price
     * @param productUrl     product url
     * @param productImage   product image url
     * @param startTime
     */
    constructor(
            messageId: String, fromUid: String, from: String, fromRole: String,
            attachmentId: String, attachmentType: String, replyTime: String, productId: Int,
            productName: String, productPrice: String, productUrl: String, productImage: String,
            isSender: Boolean, message: String, startTime: String, canShowFooter: Boolean,
            blastId: Int, productPriceInt: Int, category: String, variants: List<AttachmentVariant>,
            dropPercentage: String, priceBefore: String, shopId: Int, freeShipping: FreeShipping,
            categoryId: Int, playStoreData: PlayStoreData, remainingStock: Int, status: Int,
            source: String
    ) : super(
            messageId, fromUid, from, fromRole,
            attachmentId, attachmentType, replyTime, startTime,
            false, false, isSender, message,
            source
    ) {
        this.productId = productId
        this.productName = productName
        this.productPrice = productPrice
        this.dateTimeInMilis = java.lang.Long.parseLong(replyTime)
        this.productUrl = productUrl
        this.productImage = productImage
        this.canShowFooter = canShowFooter
        this.blastId = blastId
        this.priceInt = productPriceInt
        this.category = category
        this.dropPercentage = dropPercentage
        this.priceBefore = priceBefore
        this.shopId = shopId
        this.freeShipping = freeShipping
        this.categoryId = categoryId
        this.playStoreData = playStoreData
        this.remainingStock = remainingStock
        this.status = status
        if (variants.isNotEmpty()) {
            this.variants = variants
            setupVariantsField()
        }
    }

    private fun setupVariantsField() {
        for (variant in variants) {
            val variantOption = variant.options
            if (variantOption.isColor()) {
                colorVariantId = variantOption.id.toString()
                colorVariant = variantOption.value
                colorHexVariant = variantOption.hex
            } else {
                sizeVariantId = variantOption.id.toString()
                sizeVariant = variantOption.value
            }
        }
    }

    /**
     * Constructor for sending product attachment.
     *
     * @param loginID      current user id.
     * @param productId    product id
     * @param productName  product name
     * @param productPrice product price
     * @param productUrl   product url
     * @param productImage product image url
     * @param startTime    send time to validate dummy mesages.
     */
    constructor(
            loginID: String, productId: Int, productName: String, productPrice: String,
            productUrl: String, productImage: String, startTime: String, canShowFooter: Boolean,
            shopId: Int
    ) : super(
            "", loginID, "", "",
            "", AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT, SENDING_TEXT, startTime,
            false, true, true, productUrl,
            ""
    ) {
        this.productId = productId
        this.productName = productName
        this.productPrice = productPrice
        this.dateTimeInMilis = Date().time
        this.productUrl = productUrl
        this.productImage = productImage
        this.canShowFooter = canShowFooter
        this.shopId = shopId
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

    fun getAtcEventLabel(): String {
        val atcEventLabel = when {
            blastId == 0 -> "chat"
            blastId == -1 -> "drop price alert"
            blastId == -2 -> "limited stock"
            blastId > 0 -> "broadcast"
            else -> "chat"
        }

        return "$atcEventLabel - $blastId"
    }

    fun getAtcEventAction(): String {
        return "click atc on bottom sheet"
    }

    fun getBuyEventAction(): String {
        return "click buy on bottom sheet"
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
        return status == statusDeleted || status == statusWarehouse
    }

    fun isWishListed(): Boolean {
        return wishList
    }

    fun getStringProductId(): String {
        return productId.toString()
    }

    fun getIdString(): String {
        return productId.toString()
    }

    override fun finishLoading() {
        this.isLoading = false
        this.isError = false
    }

    fun getAtcDimension40(sourcePage: String): String {
        return when (sourcePage) {
            ApplinkConst.Chat.SOURCE_CHAT_SEARCH -> "/chat - search chat"
            else -> "/chat - 0"
        }
    }

    fun hasReview(): Boolean {
        return rating.count > 0
    }

    fun fromBroadcast(): Boolean {
        return blastId != 0
    }

    companion object {
        const val statusDeleted = 0
        const val statusActive = 1
        const val statusWarehouse = 3
    }
}
